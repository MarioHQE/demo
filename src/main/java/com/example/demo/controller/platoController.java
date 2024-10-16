package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Plato;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/plato")
public class platoController {
    @PostMapping("/traerplatos")
    public ArrayList<Plato> traerplatos() {

        ArrayList<Plato> plato = new ArrayList<>();

        return plato;
    }

}