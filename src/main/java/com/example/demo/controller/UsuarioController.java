
package com.example.demo.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.security.jwt.jwtUtil;
import com.example.demo.service.Usuarioimpl;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/empleado")
public class UsuarioController {

    @Autowired
    private jwtUtil jwtUtil;
    @Autowired
    private Usuarioimpl usuarioimpl;

    @PostMapping("/registrar")
    public ResponseEntity<String> registrarusuario(@RequestBody Map<String, String> requesmap) {

        try {

            String resultado = usuarioimpl.signup(requesmap);

            if ("El usuario ya existe".equals(resultado)) {
                return new ResponseEntity<>(resultado, HttpStatus.CONFLICT);
            } else if ("Se ha registrado correctamente".equals(resultado)) {
                return new ResponseEntity<>(resultado, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No se ha podido registrar", HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error al registrar el usuario", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> requesmap, HttpSession sesion,
            HttpServletResponse response) {

        try {
            return usuarioimpl.login(requesmap, sesion, response);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error al iniciar sesión", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/verificarRol")
    public ResponseEntity<String> verificarRol(HttpSession session, HttpServletResponse response) {
        // Añadir el token al encabezado de la respuesta
        System.out.println("verificar: " + session.getAttribute("token"));

        response.setHeader("Authorization", "Bearer " + (String) session.getAttribute("token"));
        log.info("asas " + (String) session.getAttribute("token"));

        if (jwtUtil.getrol((String) session.getAttribute("token")).equals("ROLE_admin")) {
            return new ResponseEntity<>("{\"mensaje\": \"Eres un admin\"}", HttpStatus.OK);
        } else if (jwtUtil.getrol((String) session.getAttribute("token")).equals("ROLE_user")) {
            return new ResponseEntity<>("{\"mensaje\": \"Eres un usuario\"}", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("{\"mensaje\": \"Rol no reconocido\"}", HttpStatus.BAD_REQUEST);
        }
    }

}
