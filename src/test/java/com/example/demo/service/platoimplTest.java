package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import com.example.demo.dao.platorepository;
import com.example.demo.entity.Plato;

class PlatoimplTest {

    @InjectMocks
    private platoimpl platodao;

    @Mock
    private platorepository platorepo;

    @Test
    void testGuardarConImagenValida() throws IOException {
        // Simular un archivo MultipartFile válido
        MockMultipartFile imagen = new MockMultipartFile(
                "imagen",
                "cevichediana.jpg",
                "image/jpeg",
                "Imagen de prueba".getBytes());

        String nombre = "Ceviche";
        String descripcion = "Ceviche fresco de pescado";
        String precio = "23.0";

        // Configurar el comportamiento simulado del repositorio
        when(platorepo.save(any(Plato.class))).thenReturn(new Plato());

        // Ejecutar el método guardar
        ResponseEntity<String> response = platodao.guardar(imagen, nombre, descripcion, precio);

        // Verificar el resultado
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Se ha guardado correctamente el plato", response.getBody());
    }

    @Test
    void testGuardarSinImagen() {
        // Simular un archivo vacío
        MockMultipartFile imagen = new MockMultipartFile(
                "imagen",
                "",
                "image/jpeg",
                new byte[0]);

        String nombre = "Ceviche";
        String descripcion = "Ceviche fresco de pescado";
        String precio = "23.0";

        // Ejecutar el método guardar
        ResponseEntity<String> response = platodao.guardar(imagen, nombre,
                descripcion, precio);

        // Verificar el resultado para imagen faltante
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("No se ha seleccionado imagen", response.getBody());
    }

    @Test
    void testGuardarConCamposIncompletos() {
        MockMultipartFile imagen = new MockMultipartFile(
                "imagen",
                "cevichediana.jpg",
                "image/jpeg",
                "Imagen de prueba".getBytes());

        String nombre = ""; // Campo de nombre vacío
        String descripcion = "Ceviche fresco de pescado";
        String precio = "23.0";

        // Ejecutar el método guardar
        ResponseEntity<String> response = platodao.guardar(imagen, nombre,
                descripcion, precio);

        // Verificar el resultado para campos incompletos
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("No se ha completado todos los campos", response.getBody());
    }
}
