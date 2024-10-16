package com.example.demo.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Plato;

@Service
public interface platoservice {

    public ArrayList<Plato> traerplatos();
}
