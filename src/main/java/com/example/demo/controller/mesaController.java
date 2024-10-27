package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Mesa;
import com.example.demo.service.mesaimpl;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@Slf4j
@RequestMapping("/mesa")
public class mesaController {
    @Autowired
    private mesaimpl mesadao;

    @PostMapping("/traer")
    @ResponseBody
    public String obtenerMesaLibre(@RequestBody Map<String, String> request) {
        int capacidad = Integer.parseInt(request.get("capacidad"));
        LocalDate fecha = LocalDate.parse(request.get("fecha"));
        LocalTime hora = LocalTime.parse(request.get("hora"));
        LocalDateTime fechaHora = LocalDateTime.of(fecha, hora);

        List<Mesa> mesaLibre = mesadao.mostrarmesalibre(capacidad, fechaHora);

        StringBuilder responseHtml = new StringBuilder();

        if (!mesaLibre.isEmpty()) {
            responseHtml.append("<h3>Mesas disponibles:</h3>");
            responseHtml.append("<div class='d-flex justify-content-center flex-wrap'>");
            for (Mesa mesa : mesaLibre) {
                responseHtml.append("<div class='mt-3 mb-3'>");
                responseHtml.append("<div class='circulo'>");
                responseHtml.append("<input type='radio' name='mesa' value='").append(mesa.getNroMesa())
                        .append("' id='mesa").append(mesa.getNroMesa()).append("'>");
                responseHtml.append("<label for='mesa").append(mesa.getNroMesa()).append("'>")
                        .append(mesa.getNroMesa()).append("</label>");
                responseHtml.append("</div>");
                responseHtml.append("</div>");
            }
            responseHtml.append("</div>");
        } else {
            responseHtml.append("<p>No hay mesas disponibles para esta cantidad de personas.</p>");
        }

        return responseHtml.toString();
    }

}
