package com.example.demo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
        Reserva reserva = new Reserva();
        Mesa mesa = mesadao.mesanro(nro_mesa);
        Usuario usuario = usuariodao.findById(id_usuario).get();
        reserva.setUsuario(usuario);
        reserva.setHora(hora);
        reserva.setTelefono(telefono);
        reserva.setNombre(nombre);
        reserva.setApellido(apellido);
        reserva.setMesa(mesa);
        reserva.setFecha(fecha);
        reservadao.save(reserva);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

}
