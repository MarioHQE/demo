package com.example.demo;

import com.example.demo.service.Usuarioimpl;
import com.example.demo.service.mesaimpl;
import com.example.demo.service.platoimpl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.example.demo.entity.Plato;
import com.example.demo.entity.Usuario;
import com.example.demo.security.jwt.jwtFilter;
import com.example.demo.security.jwt.jwtUtil;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RestController {

    @Autowired
    private Usuarioimpl usuarioimpl;

    @Autowired
    public jwtFilter jwtFilter;
    @Autowired
    public jwtUtil jwtUtil;
    @Autowired
    private platoimpl platodao;

    @Autowired
    private mesaimpl mesadao;

    @GetMapping("/index")
    public String comienzo(Model modelo, HttpSession session, HttpServletResponse response) {

        String nombre = (String) session.getAttribute("username");
        System.out.println(session.getAttribute("token"));
        System.out.println(session.getAttribute("rol"));
        modelo.addAttribute("nombre", nombre);

        return "index";
    }

    @GetMapping("/hola")
    public String hola(Model modelo, HttpServletResponse response, HttpSession session) {
        response.setHeader("Authorization", "Bearer " +
                session.getAttribute("token"));

        System.out.println("hsajjas " + session.getAttribute("token"));
        List<Usuario> traerusuarios = usuarioimpl.traerusuarios();
        modelo.addAttribute("empleados", traerusuarios);

        return "hola";
    }

    @GetMapping("/login_admin")
    public String login(Model modelo, HttpServletResponse response, HttpSession session) {

        if (session.getAttribute("token") != null) {
            return "redirect:/index";
        }
        // Añadir el token al encabezado de la respuesta
        response.addHeader("Authorization", "Bearer " +
                session.getAttribute("token"));
        return "login";
    }

    @GetMapping("/regform")
    public String register(Model modelo) {
        return "regform";
    }

    @GetMapping("/prueba")
    public String prueba(Model modelo, HttpServletResponse response, HttpSession session, HttpServletRequest request) {
        response.setHeader("Authorization", "Bearer " +
                session.getAttribute("token"));
        return "prueba";
    }

    @GetMapping("/carta")
    public String carta(Model modelo) {
        ArrayList<Plato> traerPlatos = platodao.traerplatos();
        modelo.addAttribute("platos", traerPlatos);
        return "carta";
    }

    @GetMapping("/chef")
    public String chef(Model modelo) {
        return "chef";
    }

    @GetMapping("/menu_login")
    public String menu(Model modelo, HttpServletResponse response, HttpSession session) {
        response.setHeader("Authorization", "Bearer " +
                session.getAttribute("token"));
        return "menu_login";
    }

    @GetMapping("platos")
    public String platos(Model modelo, HttpServletResponse response, HttpSession session) {
        response.setHeader("Authorization", "Bearer " +
                session.getAttribute("token"));
        ArrayList<Plato> traerPlatos = platodao.traerplatos();

        modelo.addAttribute("platos", traerPlatos);
        return "platos";
    }

    @GetMapping("/contacto")
    public String contacto(Model modelo, HttpServletResponse response, HttpSession session) {
        response.setHeader("Authorization", "Bearer " +
                session.getAttribute("token"));
        return "contacto";
    }

    @GetMapping("/reserva")
    public String reserva(Model modelo, HttpServletResponse response, HttpSession session) {
        response.setHeader("Authorization", "Bearer " +
                session.getAttribute("token"));
        return "reserva";
    }

    @PostMapping("/cerrar")
    public ResponseEntity<String> logout(HttpSession session) {
        // Eliminar el token y cerrar la sesión
        session.invalidate();

        return new ResponseEntity<>("Sesión cerrada correctamente", HttpStatus.OK);
    }

}
