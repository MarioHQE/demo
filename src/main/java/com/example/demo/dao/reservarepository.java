package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.example.demo.entity.Reserva;

@Repository
public interface reservarepository extends JpaRepository<Reserva, Integer> {
    // Método para buscar reservas por usuario y fecha
    public List<Reserva> findbyestado();

    public List<Reserva> atendidos();

}
