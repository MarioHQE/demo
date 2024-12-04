package com.example.demo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.dao.UsuarioRepository;
import com.example.demo.dao.mesarepository;
import com.example.demo.dao.reservarepository;
import com.example.demo.entity.Mesa;
import com.example.demo.entity.Reserva;
import com.example.demo.entity.Usuario;

@Service
public class reservaimpl implements reservaservice {
    @Autowired
    private reservarepository reservadao;

    @Autowired
    private mesarepository mesadao;
    @Autowired
    private UsuarioRepository usuariodao;

    @Override
    public ResponseEntity<String> crear(LocalDateTime fecha, LocalTime hora, int nro_mesa, String nombre,
            String apellido,
            String correo, String telefono, int id_usuario) {
        Mesa mesa = mesadao.mesanro(nro_mesa);
        Usuario usuario = usuariodao.findById(id_usuario).get();
        List<Reserva> reservas = reservadao.findReservabyusuario(id_usuario);
        if (reservas.size() > 5) {
            return new ResponseEntity<String>("No puedes realizar más de 5 reservas.", HttpStatus.BAD_REQUEST);

        }
        if (fecha.isBefore(LocalDateTime.now(ZoneId.of("America/Lima")))) {
            return new ResponseEntity<String>("La fecha y hora seleccionadas ya han pasado.", HttpStatus.BAD_REQUEST);
        }
        if (Objects.isNull(mesa)) {
            return new ResponseEntity<String>("Mesa no encontrada", HttpStatus.NOT_FOUND);
        }
        if (reservas.stream().anyMatch(r -> r.getMesa().getNroMesa() == mesa.getNroMesa()
                && r.getHora().equals(hora) && r.getFecha().equals(fecha))) {
            return new ResponseEntity<String>("Ya hay una reserva en esa fecha y hora", HttpStatus.CONFLICT);
        }

        reservadao.save(traerReserva(usuario, hora, telefono, nombre, apellido, mesa, fecha, correo));
        return new ResponseEntity<String>("Reserva realizada correctamente", HttpStatus.OK);

    }

    public Reserva traerReserva(Usuario usuario, LocalTime hora, String telefono, String nombre, String apellido,
            Mesa mesa, LocalDateTime fecha, String correo) {
        Reserva reserva = new Reserva();
        reserva.setUsuario(usuario);
        reserva.setHora(hora);
        reserva.setTelefono(telefono);
        reserva.setNombre(nombre);
        reserva.setApellido(apellido);
        reserva.setMesa(mesa);
        reserva.setFecha(fecha);
        reserva.setCorreo(correo);
        reserva.setEstado("pendiente");
        return reserva;
    }

    @Override
    public List<Reserva> traerreserva(LocalDate fecha) {
        List<Reserva> reservas = reservadao.findbyestado(fecha);
        return reservas;
    }

    @Override
    public List<Reserva> atendido() {

        return reservadao.atendidos();
    }

    @Override
    public void actualizarEstadoReserva() {
        // Obtiene la fecha y hora actuales
        LocalDateTime ahora = LocalDateTime.now();

        // Obtiene todas las reservas de la base de datos
        List<Reserva> reservas = reservadao.findAll();

        for (Reserva reserva : reservas) {

            // Compara si la fecha y hora de la reserva ya pasó
            if (reserva.getFecha().isBefore(ahora) && !reserva.getEstado().equals("terminado")) {
                // Si ya pasó y el estado no está en "terminado", actualiza el estado
                reserva.setEstado("terminado");
                reservadao.save(reserva); // Guarda los cambios en la base de datos
            }
        }
    }

    @Override
    public ResponseEntity<String> eliminar(int idreservaInteger) {
        Reserva reserva = reservadao.findById(idreservaInteger).orElse(null);
        if (Objects.isNull(reserva)) {
            return new ResponseEntity<>("Reserva no encontrada", HttpStatus.NOT_FOUND);
        }
        reservadao.delete(reserva);
        return new ResponseEntity<String>("Reserva eliminada correctamente", HttpStatus.OK);

    }

    @Override
    public ResponseEntity<String> actualizarEstado(Map<String, Integer> request) {
        int idReserva = request.get("idReserva");
        Reserva reserva = reservadao.findById(idReserva).orElse(null);
        if (reserva == null) {
            return new ResponseEntity<>("Reserva no encontrada", HttpStatus.NOT_FOUND);
        }
        reserva.setEstado("atendido");
        reservadao.save(reserva);
        return new ResponseEntity<>("Estado actualizado a atendido", HttpStatus.OK);
    }

}
