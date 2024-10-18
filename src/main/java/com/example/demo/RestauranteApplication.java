package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;;

@SpringBootApplication
public class RestauranteApplication {

	public static void main(String[] args) {

		SpringApplication.run(RestauranteApplication.class, args);
	}
	//Agregar las clases de controladores y servicios necesarios para el funcionamiento del Restaurante.
	//Por ejemplo, RestController, PlatoService, PlatoController, etc.
	//También deben estar configuradas las rutas en la clase RestauranteApplication.
	//Para probar el funcionamiento del Restaurante, usar Postman o un cliente HTTP.
	//Ejemplos de endpoints:
	//GET http://localhost:8080/platos
	//GET http://localhost:8080/platos/{id}
	// POST http://localhost:8080/platos
	// PUT http://localhost:8080/platos/{id}
	// DELETE http://localhost:8080/platos/{id}
	//GET http://localhost:8080/usuarios/login
	// POST http://localhost:8080/usuarios/registrar
	//GET http://localhost:8080/menu_login
	




}
