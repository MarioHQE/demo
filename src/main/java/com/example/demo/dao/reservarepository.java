package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import com.example.demo.entity.Reserva;

@Repository
public interface reservarepository extends JpaRepository<Reserva, Integer> {
    // Método para buscar reservas por usuario y fecha
    public List<Reserva> findReservabyusuario(@Param("id_usuario") int id_usuario);

    public List<Reserva> findbyestado(@Param("fecha") LocalDate fecha);

    public List<Reserva> atendidos();

}
