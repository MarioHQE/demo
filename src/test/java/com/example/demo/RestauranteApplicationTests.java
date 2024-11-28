package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import com.example.demo.dao.platorepository;
import com.example.demo.entity.Plato;
import com.example.demo.service.platoimpl;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.s3.model.S3Exception;

@SpringBootTest
class RestauranteApplicationTests {

	@InjectMocks
	private platoimpl platodao;

	@Mock
	private platorepository platorepo;

	@Test
	void contextLoads() {
	}

	@Test
	void testGuardarConImagenValida() throws IOException {
		MockMultipartFile imagen = new MockMultipartFile(
				"imagen",
				"cevichediana.jpg",
				"image/jpeg",
				"Imagen de prueba".getBytes());

		String nombre = "Ceviche";
		String descripcion = "Ceviche fresco de pescado";
		String precio = "23.0";

		when(platorepo.save(any(Plato.class))).thenReturn(new Plato());

		ResponseEntity<String> response = platodao.guardar(imagen, nombre, descripcion, precio);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Se ha guardado correctamente el plato", response.getBody());
	}

	@Test
	void testGuardarSinImagen() throws S3Exception, AwsServiceException, SdkClientException, IOException {
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

}
