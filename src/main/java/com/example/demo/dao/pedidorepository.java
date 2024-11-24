package com.example.demo.dao;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Pedido;

@Repository
public interface pedidorepository extends JpaRepository<Pedido, Integer> {
    // Implementación del método buscar pedidos por usuario y fecha
    public ArrayList<Pedido> findPedidobyCorreo(@Param("correo") String correo);

}
