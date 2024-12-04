package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.reservaimpl;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Email;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@Slf4j
@RequestMapping("/reserva")
public class reservaController {
    @Autowired
    private reservaimpl reservadao;

    @PostMapping("/crear")
    public ResponseEntity<String> crear(
            @RequestParam("dia") String dia,
            @RequestParam("hora") String horaStr,
            @RequestParam(value = "mesa", required = false) Integer nro_mesa,
            @RequestParam("name") String nombre,
            @RequestParam("lastname") String apellido,
            @RequestParam("correo") @Email String correo,
            @RequestParam("numero") @Length(min = 9, max = 9) String telefono,
            HttpSession session) {

        if (nro_mesa == null) {
            return ResponseEntity.badRequest().body("Debe seleccionar una mesa.");
        }
        int nromesaint = nro_mesa;
        int id = (int) session.getAttribute("id_usuario");
        log.info("Creando reserva para usuario {} en mesa {} a las {} del {}", id, nro_mesa, horaStr, dia);
        LocalDate fecha = LocalDate.parse(dia);
        LocalTime hora = LocalTime.parse(horaStr);
        LocalDateTime fechaHora = LocalDateTime.of(fecha, hora);

        return reservadao.crear(fechaHora, hora, nromesaint, nombre, apellido, correo, telefono, id);
    }

    @PostMapping("/eliminar")
    public ResponseEntity<String> eliminar(@RequestBody Map<String, String> requesmap) {

        return reservadao.eliminar(Integer.parseInt(requesmap.get("id_reserva")));
    }

    @PostMapping("/actualizarEstado")
    public ResponseEntity<String> actualizarEstado(@RequestBody Map<String, Integer> request) {
        return reservadao.actualizarEstado(request);
    }

}
