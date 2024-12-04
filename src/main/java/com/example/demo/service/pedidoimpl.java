package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.dao.UsuarioRepository;
import com.example.demo.dao.pedido_platorepository;
import com.example.demo.dao.pedidorepository;
import com.example.demo.dao.platorepository;
import com.example.demo.entity.Pedido;
import com.example.demo.entity.PedidoPlato;
import com.example.demo.entity.Plato;
import com.example.demo.entity.Usuario;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Refund;
import com.stripe.param.RefundCreateParams;

@Service
public class pedidoimpl implements pedidoservice {
    @Autowired
    private pedidorepository pedidorepo;
    @Autowired
    private UsuarioRepository usuariorepo;
    @Autowired
    private platorepository platorepo;
    @Autowired
    private pedido_platorepository pedido_platodao;

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
            List<Integer> cantidades = (List<Integer>) params.get("cantidades");
            List<Plato> platos = platorepo.findAllById(idPlatos);
            if (platos.isEmpty()) {
                return ResponseEntity.badRequest().body("Platos no encontrados");
            }

            // Crear y guardar pedido
            Pedido pedido = new Pedido();
            pedido.setId_usuario(usuario);
            pedido.setFecha(datetime);
            pedido.setEstado((String) params.get("estado"));
            List<PedidoPlato> pedidoPlatos = new ArrayList<>();
            for (int i = 0; i < platos.size(); i++) {
                Plato plato = platos.get(i);
                int cantidad = cantidades.get(i);

                PedidoPlato pedidoPlato = new PedidoPlato();
                pedidoPlato.setPedido(pedido);
                pedidoPlato.setPlato(plato);
                pedidoPlato.setCantidad(cantidad);
                pedidoPlatos.add(pedidoPlato);
            }
            pedido.setPedidoPlatos(pedidoPlatos);
            pedido.calcularTotal();
            pedidorepo.save(pedido);
            return ResponseEntity.ok("Pedido guardado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al guardar el pedido");
        }
    }

    @Override
    public List<Pedido> traerPedidobycorreo(String correo) {
        List<Pedido> pedido = pedidorepo.findPedidobyCorreo(correo);
        for (Pedido pedidos : pedido) {
            pedidos.calcularTotal();
            pedidorepo.save(pedidos);
        }
        if (pedido == null) {
            return new ArrayList<>();
        }
        return pedido;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ResponseEntity<String> agregarplatos(Map<String, Object> params) {
        Pedido pedido = pedidorepo.findById((int) params.get("id")).orElse(null);

        PedidoPlato pedidoPlato = new PedidoPlato();
        pedidoPlato.setPedido(pedido);
        List<Integer> idplatos = (List<Integer>) params.get("id_platos");
        List<Integer> cantidades = (List<Integer>) params.get("cantidades");
        for (int cantidad : cantidades) {
            if (cantidad == 0) {

            }

        }
        if (Objects.isNull(pedido)) {
            return ResponseEntity.badRequest().body("Pedido no encontrado");
        }
        if (Objects.isNull(idplatos) || idplatos.isEmpty()) {
            return ResponseEntity.badRequest().body("No se agregaron platos al pedido");
        }
        if (Objects.isNull(cantidades) || cantidades.isEmpty()) {
            return ResponseEntity.badRequest().body("No se agregaron cantidades al pedido");
        }
        if (idplatos.size() != cantidades.size()) {
            return ResponseEntity.badRequest().body("Cantidades y id_platos deben tener la misma cantidad");
        }
        List<Plato> platos = platorepo.findAllById(idplatos);
        for (int i = 0; i < platos.size(); i++) {
            Plato plato = platos.get(i);
            int cantidad = cantidades.get(i);
            pedidoPlato.setPlato(plato);
            pedidoPlato.setCantidad(cantidad);
            pedido_platodao.save(pedidoPlato);
            pedido.calcularTotal();
            pedidorepo.save(pedido);
        }

        pedido_platodao.save(pedidoPlato);

        return ResponseEntity.ok("Platos agregados al pedido correctamente");
    }

    @Override
    public ResponseEntity<String> eliminarplatopedido(int id_plato, int id_pedidoplato) {
        // TODO Auto-generated method stub
        Plato plato = platorepo.findById(id_plato);
        if (plato == null) {
            return ResponseEntity.badRequest().body("Plato no encontrado");
        }
        PedidoPlato pedidoplato = pedido_platodao.encontrarpedidoplatoporplatoyid(plato.getId_plato(), id_pedidoplato);
        if (pedidoplato == null) {
            return ResponseEntity.badRequest().body("Pedido-Plato no encontrado");
        }
        pedido_platodao.delete(pedidoplato);

        Pedido pedido = pedidoplato.getPedido();
        pedido.calcularTotal();
        pedidorepo.save(pedido);

        List<PedidoPlato> pedidoPlatosRestantes = pedido_platodao.findByPedidoId(pedido.getId_pedido());
        if (pedidoPlatosRestantes.isEmpty()) {
            // Eliminar el pedido si no tiene más platos
            pedidorepo.delete(pedido);
            return ResponseEntity.ok("Plato eliminado. El pedido fue eliminado porque no tiene más platos.");
        }
        return ResponseEntity.ok("Plato eliminado del pedido correctamente");

    }

    public ResponseEntity<String> actualizarEstado(Map<String, String> request) {
        String idString = request.get("id_pedido");
        Pedido pedido = pedidorepo.findById(Integer.parseInt(idString)).orElse(null);
        if (Objects.isNull(pedido)) {
            return ResponseEntity.badRequest().body("Pedido no encontrado");
        }
        pedido.setEstado("atendido");
        pedidorepo.save(pedido);
        return ResponseEntity.ok("Pedido actualizado correctamente");
    }

    @Override
    public ResponseEntity<String> rembolso(Map<String, String> requesmap, String secretkey) throws StripeException {
        String idString = requesmap.get("id_pedido");

        Pedido pedido = pedidorepo.findById(Integer.parseInt(idString)).orElse(null);
        if (Objects.isNull(pedido)) {
            return ResponseEntity.badRequest().body("Pedido no encontrado");
        }
        Stripe.apiKey = secretkey;
        RefundCreateParams params = RefundCreateParams.builder().setPaymentIntent(pedido.getId_pago()).build();
        Refund refund = Refund.create(params);
        if (refund.getStatus().equals("succeeded")) {

            pedido.setEstado("REMBOLSADO");
            pedidorepo.save(pedido);
            return ResponseEntity.ok("Rembolso realizado correctamente");
        } else {
            return ResponseEntity.status(500).body("Error al realizar el rembolso");
        }
    }

}
