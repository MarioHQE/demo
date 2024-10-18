package com.example.demo.service;

import java.util.ArrayList;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dao.platorepository;
import com.example.demo.entity.Plato;

import java.io.IOException;
import java.nio.file.*;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class platoimpl implements platoservice {

    @Autowired
    platorepository platodao;

    @Override
    public ArrayList<Plato> traerplatos() {
        ArrayList<Plato> listaplato = (ArrayList<Plato>) platodao.findAll();
        return listaplato;
    }

    @Override
    public ResponseEntity<String> guardar(MultipartFile imagen, String nombre, String descripcion, String precio) {
        // Crear un objeto Plato
        Plato plato = new Plato();
        plato.setNombre(nombre);
        plato.setDescripcion(descripcion);
        plato.setPrecio(Double.parseDouble(precio));

        // Validar que todos los campos están completos
        if (validate(nombre, descripcion, precio)) {
            if (!imagen.isEmpty()) {
                Path directorioimagenes = Paths.get("src//main//resources//templates/uploads");
                String rutaabsoluta = directorioimagenes.toFile().getAbsolutePath();
                try {
                    byte[] bytesimg = imagen.getBytes();
                    Path rutacompletoa = Paths.get(rutaabsoluta + "//" + imagen.getOriginalFilename());
                    Files.write(rutacompletoa, bytesimg);
                    plato.setFoto(imagen.getOriginalFilename());

                    // Guardar el plato en la base de datos
                    platodao.save(plato);

                    return new ResponseEntity<>("Se ha guardado correctamente el plato", HttpStatus.OK);

                } catch (IOException e) {
                    log.error("Error al guardar la imagen: ", e);
                    return new ResponseEntity<>("Error al guardar la imagen", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                return new ResponseEntity<>("No se ha seleccionado imagen", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("No se ha completado todos los campos", HttpStatus.BAD_REQUEST);
        }
    }

    public Plato traerplato(Map<String, String> requesmap) {
        Plato plato = new Plato();
        plato.setNombre(requesmap.get("nombre"));
        plato.setDescripcion(requesmap.get("descripcion"));
        plato.setPrecio(Double.parseDouble(requesmap.get("precio")));

        return plato;

    }

    public boolean validate(String nombre, String descripcion, String precio) {
        return !(nombre == null || nombre.isEmpty() ||
                descripcion == null || descripcion.isEmpty() ||
                precio == null || precio.isEmpty());
    }

}
