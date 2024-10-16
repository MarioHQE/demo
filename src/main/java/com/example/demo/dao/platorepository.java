package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Plato;

@Repository
public interface platorepository extends JpaRepository<Plato, Integer> {

}
