package com.example.demo.security;

import java.util.Objects;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.demo.dao.UsuarioRepository;
import com.example.demo.entity.Usuario;
import java.util.Collections;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UsuarioDetailsService implements UserDetailsService {
    @Autowired
    UsuarioRepository usuariodao;

    Usuario usuariodetail;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("Dentro de loadUserbyUsername{}", username);
        usuariodetail = usuariodao.findByCorreo(username);

        if (!Objects.isNull(usuariodetail)) {

            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(usuariodetail.getRol().getNombre());
            List<GrantedAuthority> authorities = Collections.singletonList(authority);

            // Devolver una instancia de User con el correo, contraseña y rol
            return new User(usuariodetail.getEmail(), usuariodetail.getContrasena(), authorities);
        } else {
            throw new UsernameNotFoundException("Usuario no encontrado ");
        }

    }

    public Usuario getUserDetail() {
        return usuariodetail;
    }

}