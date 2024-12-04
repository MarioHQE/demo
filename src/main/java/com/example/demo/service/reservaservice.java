package com.example.demo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Reserva;

@Service
public interface reservaservice {

    public ResponseEntity<String> crear(LocalDateTime fecha, LocalTime hora, int nro_mesa, String nombre,
            String apellido,
            String correo, String telefono, int id_usuario);

    public List<Reserva> traerreserva(LocalDate fecha);

    public List<Reserva> atendido();

    public ResponseEntity<String> eliminar(int idreservaInteger);

    public ResponseEntity<String> actualizarEstado(Map<String, Integer> request);

    public void actualizarEstadoReserva();
}
