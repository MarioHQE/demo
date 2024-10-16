
package com.example.demo.service;

import com.example.demo.entity.Usuario;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface UsuarioService {

    public List<Usuario> traerusuarios();

    public String signup(Map<String, String> requesmap);

    public ResponseEntity<String> login(Map<String, String> requesmap, HttpSession session,
            HttpServletResponse response);

}
