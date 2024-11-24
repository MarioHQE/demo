package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.service.pagoimpl;
import com.example.demo.service.pedidoimpl;
import com.example.demo.stripe.pago;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/pedido")
public class pedidocontroller {
    @Autowired
    pedidoimpl pedidoimpl;
    @Autowired
    pagoimpl pagoimpl;

    @PostMapping("/guardar")
    public ResponseEntity<String> guardar(@RequestBody Map<String, Object> params) {
        LocalDateTime datetime = LocalDateTime.now();
        return pedidoimpl.guardarPedido(params, datetime);
    }

    @PostMapping("/agregar-platos")
    public ResponseEntity<String> agregarplatos(@RequestBody Map<String, Object> params) {

        return pedidoimpl.agregarplatos(params);
    }

    @PostMapping("/eliminar-plato")
    public ResponseEntity<String> eliminarplato(@RequestParam("id_plato") int id_plato,
            @RequestParam("id_pedidoplato") int id_pedidoplato) {

        return pedidoimpl.eliminarplatopedido(id_plato, id_pedidoplato);
    }

    @PostMapping("/create-checkout-session")
    public ResponseEntity<Map<String, String>> checkout(@RequestBody Map<String, Object> mapeo) {
        // TODO: process POST request

        return pagoimpl.sesionpay(mapeo);
    }

}
