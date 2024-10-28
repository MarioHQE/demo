package com.example.demo.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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

        reservadao.save(traerReserva(usuario, hora, telefono, nombre, apellido, mesa, fecha, correo));
        if (fecha.isBefore(LocalDateTime.now())) {
            return new ResponseEntity<String>("La fecha y hora seleccionadas ya han pasado.", HttpStatus.BAD_REQUEST);
        }
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
    public List<Reserva> traerreserva() {
        List<Reserva> reservas = reservadao.findbyestado();
        return reservas;
    }

    @Override
    public List<Reserva> atendido() {

        return reservadao.atendidos();
    }

}
