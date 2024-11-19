package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Plato;
import com.example.demo.service.platoimpl;

import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/plato")
public class platoController {
    @Autowired
    private platoimpl platodao;

    @PostMapping("/traerplatos")
    public ArrayList<Plato> traerplatos() {

        ArrayList<Plato> plato = platodao.traerplatos();

        return plato;
    }

    @PostMapping("/guardar")
    public ResponseEntity<String> guardar(@RequestParam("foto") MultipartFile imagen,
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("precio") String precio) {

        return platodao.guardar(imagen, nombre, descripcion, precio);
    }

    @PostMapping("/actualizar")
    public ResponseEntity<String> actualizarPlato(
            @RequestParam("id_plato") String id,
            @RequestParam("actuNombre") String nombre,
            @RequestParam("actuDescripcion") String descripcion,
            @RequestParam("actuprecio") String precio,
            @RequestParam("actufoto") MultipartFile imagen, HttpSession sesion) {

        return platodao.actualizar(id, nombre, descripcion, precio, imagen, sesion);
    }

    @DeleteMapping("/eliminar")
    public ResponseEntity<String> eliminar(@RequestParam("id_plato") String id) {

        return platodao.eliminar(id);
    }

}
