package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Pedido;

@Service
public interface pedidoservice {

    public ResponseEntity<String> guardarPedido(Map<String, Object> params, LocalDateTime dateTime);

    public List<Pedido> traerPedidobycorreo(String id);

    public ResponseEntity<String> agregarplatos(Map<String, Object> params);

    public ResponseEntity<String> eliminarplatopedido(int id_plato, int id_pedidoplato);

}