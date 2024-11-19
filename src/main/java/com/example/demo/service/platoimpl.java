package com.example.demo.service;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.dao.platorepository;
import com.example.demo.entity.Plato;
import jakarta.servlet.http.HttpSession;
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
                String contentype = imagen.getContentType();
                if (!Objects.equals(contentype, "image/jpeg") && !Objects.equals(contentype, "image/png")) {
                    return new ResponseEntity<>("Formato de imagen no válido (JPG o PNG)", HttpStatus.BAD_REQUEST);
                } else {

                    Path directorioimagenes = Paths.get("src//main//resources//static/uploads");
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

    @Override
    public ResponseEntity<String> actualizar(String id, String nombre, String descripcion, String precio,
            MultipartFile imagen, HttpSession sesion) {

        // Traer el plato existente por ID
        Plato platoexistente = platodao.findById(Integer.parseInt(id));
        log.info("plato existente");

        if (platoexistente != null) {
            // Validar que todos los campos están completos
            if (validateActu(nombre, descripcion, precio)) {
                // Actualizar campos de texto
                platoexistente.setNombre(nombre);
                platoexistente.setDescripcion(descripcion);
                platoexistente.setPrecio(Double.parseDouble(precio));

                // Si se seleccionó una nueva imagen, actualizarla

                if (!imagen.isEmpty()) {
                    String contentype = imagen.getContentType();
                    if (!Objects.equals(contentype, "image/jpeg") && !Objects.equals(contentype, "image/png")) {
                        return new ResponseEntity<>("Formato de imagen no válido (JPG o PNG)", HttpStatus.BAD_REQUEST);
                    } else {
                        Path directorioimagenes = Paths.get("src//main//resources//static/uploads");
                        String rutaabsoluta = directorioimagenes.toFile().getAbsolutePath();
                        try {
                            byte[] bytesimg = imagen.getBytes();
                            Path rutacompletoa = Paths.get(rutaabsoluta + "//" + imagen.getOriginalFilename());
                            Files.write(rutacompletoa, bytesimg);
                            platoexistente.setFoto(imagen.getOriginalFilename());
                            log.info("dentro de la imagen aa ver si funciona");
                            platodao.save(platoexistente);
                            return new ResponseEntity<String>("Plato actualizado correctamente", HttpStatus.OK);
                        } catch (IOException e) {
                            log.error("Error al guardar la imagen: ", e);
                            return new ResponseEntity<>("Error al guardar la imagen", HttpStatus.INTERNAL_SERVER_ERROR);
                        }
                    }
                }
            }
            return new ResponseEntity<>("No se ha completado todos los campos", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<String>("No se ha encontrado el plato", HttpStatus.NOT_FOUND);
        }
    }

    private boolean validateActu(String actuNombre, String actuDescripcion,
            String actuprecio) {
        return !(actuNombre == null || actuNombre.isEmpty() ||
                actuDescripcion == null || actuDescripcion.isEmpty() ||
                actuprecio == null || actuprecio.isEmpty());
    }

    @Override
    public ResponseEntity<String> eliminar(String id) {
        // Traer el plato existente por ID
        Plato platoexistente = platodao.findById(Integer.parseInt(id));
        if (Objects.isNull(platoexistente)) {
            return new ResponseEntity<>("Plato no encontrado", HttpStatus.NOT_FOUND);

        }
        // Eliminar el plato de la base de datos
        Path directorioimagenes = Paths.get("src//main//resources//static/uploads");
        String rutaabsoluta = directorioimagenes.toFile().getAbsolutePath();
        Path rutacompletoa = Paths.get(rutaabsoluta + "//" + platoexistente.getFoto());
        try {
            Files.deleteIfExists(rutacompletoa);
        } catch (IOException e) {
            log.error("Error al eliminar la imagen: ", e);
        } finally {
            if (!Files.exists(rutacompletoa)) {
                log.info("Imagen eliminada correctamente");
            }
        }

        platodao.delete(platoexistente);

        return new ResponseEntity<>("Se ha eliminado este plato", HttpStatus.OK);
    }

}
