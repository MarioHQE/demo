package com.example.demo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface reservaservice {

    public ResponseEntity<String> crear(LocalDateTime fecha, LocalTime hora, int nro_mesa, String nombre,
            String apellido,
            String correo, String telefono, int id_usuario);

}
