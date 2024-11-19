package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface pedidoservice {

    public ResponseEntity<String> guardarPedido(Map<String, Object> params, LocalDateTime dateTime);

}
