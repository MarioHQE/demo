package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.service.pedidoimpl;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/pedido")
public class pedidocontroller {
    @Autowired
    pedidoimpl pedidoimpl;

    @PostMapping("/guardar")
    public ResponseEntity<String> guardar(@RequestBody Map<String, Object> params) {
        LocalDateTime datetime = LocalDateTime.now();
        return pedidoimpl.guardarPedido(params, datetime);
    }

}
