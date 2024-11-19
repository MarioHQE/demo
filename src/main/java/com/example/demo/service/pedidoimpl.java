package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.dao.UsuarioRepository;
import com.example.demo.dao.pedidorepository;
import com.example.demo.dao.platorepository;
import com.example.demo.entity.Pedido;
import com.example.demo.entity.Plato;
import com.example.demo.entity.Usuario;

@Service
public class pedidoimpl implements pedidoservice {
    @Autowired
    private pedidorepository pedidorepo;
    @Autowired
    private UsuarioRepository usuariorepo;
    @Autowired
    private platorepository platorepo;

    @SuppressWarnings("unchecked")
    @Override
    public ResponseEntity<String> guardarPedido(Map<String, Object> params, LocalDateTime datetime) {
        try {
            // Validar usuario
            String correo = (String) params.get("correo");
            Usuario usuario = usuariorepo.findByCorreo(correo);
            if (usuario == null) {
                return ResponseEntity.badRequest().body("Usuario no encontrado");
            }

            // Validar platos
            List<Integer> idPlatos = (List<Integer>) params.get("id_platos");
            List<Plato> platos = platorepo.findAllById(idPlatos);
            if (platos.isEmpty()) {
                return ResponseEntity.badRequest().body("Platos no encontrados");
            }

            // Crear y guardar pedido
            Pedido pedido = new Pedido();
            pedido.setId_usuario(usuario);
            pedido.setId_plato(platos);
            pedido.setFecha(datetime);
            pedido.setEstado((String) params.get("estado"));
            pedido.calcularTotal();
            pedidorepo.save(pedido);
            return ResponseEntity.ok("Pedido guardado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al guardar el pedido");
        }
    }

}
