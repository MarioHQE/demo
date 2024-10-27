package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Reserva;

@Repository
public interface reservarepository extends JpaRepository<Reserva, Integer> {

}
