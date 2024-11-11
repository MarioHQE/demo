package com.example.demo;

import com.example.demo.service.Usuarioimpl;
import com.example.demo.service.platoimpl;
import com.example.demo.service.reservaimpl;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import java.time.ZoneId;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

import com.example.demo.dao.RolRepository;
import com.example.demo.entity.Plato;
import com.example.demo.entity.Reserva;
import com.example.demo.entity.Rol;
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
@Slf4j
public class RestController {

    @Autowired
    private RolRepository roldao;
    @Autowired
    private Usuarioimpl usuarioimpl;

    @Autowired
    public jwtFilter jwtFilter;
    @Autowired
    public jwtUtil jwtUtil;
    @Autowired
    private platoimpl platodao;
    @Autowired
    private reservaimpl reservadao;

    @GetMapping("/index")
    public String comienzo(Model modelo, HttpSession session, HttpServletResponse response) {
        response.setHeader("Authorization", "Bearer " +
                session.getAttribute("token"));
        String nombre = (String) session.getAttribute("username");
        modelo.addAttribute("nombre", nombre);
        System.out.println(session.getAttribute("token"));
        System.out.println(session.getAttribute("rol"));

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

    @GetMapping("/carrito")
    public String carrito() {
        return "carrito";
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
    public String carta(Model modelo, HttpSession session) {
        ArrayList<Plato> traerPlatos = platodao.traerplatos();
        modelo.addAttribute("platos", traerPlatos);
        String nombre = (String) session.getAttribute("username");
        modelo.addAttribute("nombre", nombre);
        return "carta";
    }

    @GetMapping("/chef")
    public String chef(Model modelo, HttpSession session) {
        String nombre = (String) session.getAttribute("username");
        modelo.addAttribute("nombre", nombre);
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
        roldao.findByNombre(jwtUtil.getrol((String) session.getAttribute("token")));
        Rol rol = roldao.findByNombre(jwtUtil.getrol((String) session.getAttribute("token")));

        if (session.getAttribute("token") != null) {
            if (rol.getNombre().equals("ROLE_user")) {
                log.info(jwtUtil.getrol((String) session.getAttribute("token")));
                return "redirect:/index";
            }
        } else {
            return "redirect:/index";
        }

        ArrayList<Plato> traerPlatos = platodao.traerplatos();

        modelo.addAttribute("platos", traerPlatos);
        return "platos";
    }

    @GetMapping("/contacto")
    public String contacto(Model modelo, HttpServletResponse response, HttpSession session) {
        response.setHeader("Authorization", "Bearer " +
                session.getAttribute("token"));
        String nombre = (String) session.getAttribute("username");
        modelo.addAttribute("nombre", nombre);
        return "contacto";
    }

    @GetMapping("/reserva")
    public String reserva(Model modelo, HttpServletResponse response, HttpSession session) {
        response.setHeader("Authorization", "Bearer " +
                session.getAttribute("token"));
        return "reserva";
    }

    @GetMapping("/reservas")
    public String reservas(Model modelo, HttpServletResponse response, HttpSession session) {
        response.setHeader("Authorization", "Bearer " +
                session.getAttribute("token"));
        List<Reserva> listareserva = reservadao.traerreserva();
        List<Reserva> listareservatendida = reservadao.atendido();
        ZonedDateTime fechaactual = ZonedDateTime.now(ZoneId.of("America/Lima"));
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        modelo.addAttribute("nombre", session.getAttribute("username"));
        modelo.addAttribute("reservas", listareserva);
        modelo.addAttribute("fechaactual", fechaactual.format(formato));
        modelo.addAttribute("reservasatendidas", listareservatendida);
        return "tablareservas";
    }

    @PostMapping("/cerrar")
    public ResponseEntity<String> logout(HttpSession session) {
        // Eliminar el token y cerrar la sesión
        session.invalidate();
        jwtFilter.destroy();

        return new ResponseEntity<>("Sesión cerrada correctamente", HttpStatus.OK);
    }

}
