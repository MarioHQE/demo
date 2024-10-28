package com.example.demo.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Reserva;

@Service
public interface reservaservice {

    public ResponseEntity<String> crear(LocalDateTime fecha, LocalTime hora, int nro_mesa, String nombre,
            String apellido,
            String correo, String telefono, int id_usuario);

    public List<Reserva> traerreserva();

    public List<Reserva> atendido();

}
