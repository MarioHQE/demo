package com.example.demo.service;

import com.example.demo.dao.RolRepository;
import com.example.demo.dao.UsuarioRepository;
import com.example.demo.entity.Rol;
import com.example.demo.entity.Usuario;
import com.example.demo.security.UsuarioDetailsService;
import com.example.demo.security.jwt.jwtFilter;
import com.example.demo.security.jwt.jwtUtil;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Usuarioimpl implements UsuarioService {
    @Autowired
    private RolRepository roldao;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    jwtFilter jwtFilter;
    @Autowired
    UsuarioDetailsService usuarioDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private jwtUtil jwtUtil;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private UsuarioRepository usuariodao;

    @Override
    public List<Usuario> traerusuarios() {
        return usuariodao.findAll();

    }

    @SuppressWarnings("null")
    @Override
    public String signup(Map<String, String> requesmap) {
        if (validatesignup(requesmap)) {
            Usuario usuario = usuariodao.findByCorreo(requesmap.get("email"));

            // Encriptar la contraseña antes de guardar el empleado
            requesmap.put("contrasena", passwordEncoder.encode(requesmap.get("contrasena")));

            if (Objects.isNull(usuario)) {
                String verificationCode = generateVerificationCode();
                requesmap.put("verificationCode", verificationCode);
                Usuario usuario2 = traerusuario(requesmap);
                usuario2.setVerificationCode(verificationCode);
                usuariodao.save(usuario2);
                enviarCodigoVerificacion(traerusuario(requesmap).getEmail(), verificationCode);
                return "Se ha registrado correctamente";
            } else {
                return "El usuario ya existe";
            }
        }
        return "No se ha podido registrar";
    }

    private void enviarCodigoVerificacion(String email, String codigo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Código de verificación");
        message.setText("Tu código de verificación es: " + codigo);
        mailSender.send(message);
    }

    private String generateVerificationCode() {
        return UUID.randomUUID().toString().substring(0, 6); // Código de 6 caracteres
    }

    private boolean validatesignup(Map<String, String> requesmap) {
        return requesmap.containsKey("nombre") &&
                requesmap.containsKey("contrasena") &&
                requesmap.containsKey("email") &&
                requesmap.containsKey("telefono");
    }

    private Usuario traerusuario(Map<String, String> requesmap) {
        Rol rol = roldao.findByNombre(requesmap.get("rol"));
        if (rol == null) {
            rol = roldao.findByNombre("USER");
            if (rol == null) {
                Rol rolvacio = new Rol();
                rolvacio.setNombre("USER");
                rol = rolvacio;
                roldao.save(rolvacio);
            }
        }
        Usuario usuario = new Usuario();

        usuario.setNombre(requesmap.get("nombre"));
        usuario.setContrasena(requesmap.get("contrasena"));
        usuario.setTelefono(requesmap.get("telefono"));
        usuario.setEmail(requesmap.get("email"));
        usuario.setRol(rol);
        usuario.setStatus("false");
        return usuario;
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requesmap, HttpSession session,
            HttpServletResponse response) {

        log.info("Dentro de login");
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requesmap.get("email"), requesmap.get("contrasena")));
            if (authentication.isAuthenticated()) {
                if (usuarioDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")) {
                    String token = jwtUtil.generateToken(usuarioDetailsService.getUserDetail().getEmail(),
                            usuarioDetailsService.getUserDetail().getRol().getNombre(),
                            usuarioDetailsService.getUserDetail().getNombre(),
                            usuarioDetailsService.getUserDetail().getContrasena(),
                            usuarioDetailsService.getUserDetail());
                    session.setAttribute("username", usuarioDetailsService.getUserDetail().getNombre());
                    session.setAttribute("rol", usuarioDetailsService.getUserDetail().getRol());
                    session.setAttribute("id_usuario", usuarioDetailsService.getUserDetail().getId_usuario());
                    response.setHeader("Authorization", "Bearer " + token);
                    session.setAttribute("token", token);
                    return new ResponseEntity<String>("{\"token\":\"" + token + "\"}",
                            HttpStatus.OK);
                } else {
                    return new ResponseEntity<String>(
                            "{\"mensaje\" : \"" + "Falta Verificar el correo " + "\"}",
                            HttpStatus.BAD_REQUEST);
                }

            }
        } catch (Exception e) {
            log.error("{}", e);
        }

        return new ResponseEntity<String>("{\"mensaje\" : \"" + "Credenciales incorrectas " + "\"}",
                HttpStatus.BAD_REQUEST);

    }

    @Override
    public ResponseEntity<String> actualizar(Map<String, String> requesmap) {
        // Validar que el mapa contiene el ID del usuario
        String idUsuarioStr = requesmap.get("id_usuario");
        if (idUsuarioStr == null || idUsuarioStr.isEmpty()) {
            return new ResponseEntity<>("El ID del usuario es requerido", HttpStatus.BAD_REQUEST);
        }

        try {
            // Convertir el ID a entero
            int idUsuario = Integer.parseInt(idUsuarioStr);

            // Buscar el usuario por ID
            Usuario usuario = usuariodao.findById(idUsuario).orElse(null);

            if (usuario != null) {
                // Actualizar campos si están presentes en el mapa
                if (requesmap.containsKey("nombre")) {
                    usuario.setNombre(requesmap.get("nombre"));
                }
                if (requesmap.containsKey("telefono")) {
                    usuario.setTelefono(requesmap.get("telefono"));
                }
                if (requesmap.containsKey("email")) {
                    usuario.setEmail(requesmap.get("email"));
                }
                if (requesmap.containsKey("contrasena")) {
                    usuario.setContrasena(passwordEncoder.encode(requesmap.get("contrasena")));
                }

                // Guardar el usuario actualizado
                usuariodao.save(usuario);
                return new ResponseEntity<>("Usuario actualizado correctamente", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
            }
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("ID del usuario inválido", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al actualizar el usuario", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> eliminarUsuario(int idUsuario) {
        Usuario usuario = usuariodao.findById(idUsuario).orElse(null);

        if (usuario != null) {
            usuariodao.delete(usuario);
            return new ResponseEntity<>("Usuario eliminado correctamente", HttpStatus.OK);
        }
        return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
    }

}
