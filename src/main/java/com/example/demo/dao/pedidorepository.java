package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Pedido;

@Repository
public interface pedidorepository extends JpaRepository<Pedido, Integer> {

}
