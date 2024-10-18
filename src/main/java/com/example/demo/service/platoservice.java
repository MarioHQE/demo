package com.example.demo.service;

import java.util.*;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Plato;

@Service
public interface platoservice {

    public ArrayList<Plato> traerplatos();

    public ResponseEntity<String> guardar(MultipartFile imagen, String nombre, String descripcion, String precio);
}
