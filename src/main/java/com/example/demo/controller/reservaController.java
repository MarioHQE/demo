package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Reserva;
import com.example.demo.service.reservaimpl;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Email;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@Slf4j
@RequestMapping("/reserva")
public class reservaController {
    @Autowired
    private reservaimpl reservadao;

    @PostMapping("/crear")
    public ResponseEntity<String> crear(
            @RequestParam("dia") String dia, // Cambiado a String
            @RequestParam("hora") String horaStr, // Cambiado a String
            @RequestParam("mesa") int nro_mesa,
            @RequestParam("name") String nombre,
            @RequestParam("lastname") String apellido,
            @RequestParam("correo") @Email String correo,
            @RequestParam("numero") @Length(min = 9, max = 9) String telefono,
            HttpSession session) {

        int id = (int) session.getAttribute("id_usuario");

        // Convertir a LocalDateTime y LocalTime
        LocalDate fecha = LocalDate.parse(dia);
        LocalTime hora = LocalTime.parse(horaStr);
        LocalDateTime fechaHora = LocalDateTime.of(fecha, hora);

        // Llamar al servicio para crear la reserva
        return reservadao.crear(fechaHora, hora, nro_mesa, nombre, apellido, correo, telefono, id);
    }

}
