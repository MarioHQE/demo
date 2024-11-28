package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dao.pedidorepository;
import com.example.demo.entity.Pedido;
import com.example.demo.service.pagoimpl;
import com.example.demo.service.pedidoimpl;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@Controller
@RequestMapping("/pedido")
public class pedidocontroller {
    @Value("${stripe.key.secret}")
    String secretkey;
    @Autowired
    private pedidorepository pedidodao;
    @Autowired
    pedidoimpl pedidoimpl;
    @Autowired
    pagoimpl pagoimpl;

    @PostMapping("/guardar")
    public ResponseEntity<String> guardar(@RequestBody Map<String, Object> params) {
        LocalDateTime datetime = LocalDateTime.now();
        return pedidoimpl.guardarPedido(params, datetime);
    }

    @PostMapping("/agregar-platos")
    public ResponseEntity<String> agregarplatos(@RequestBody Map<String, Object> params) {

        return pedidoimpl.agregarplatos(params);
    }

    @PostMapping("/eliminar-plato")
    public ResponseEntity<String> eliminarplato(@RequestParam("id_plato") int id_plato,
            @RequestParam("id_pedidoplato") int id_pedidoplato) {

        return pedidoimpl.eliminarplatopedido(id_plato, id_pedidoplato);
    }

    @PostMapping("/create-checkout-session")
    public ResponseEntity<Map<String, String>> checkout(@RequestBody Map<String, Object> mapeo) {
        // TODO: process POST request

        return pagoimpl.sesionpay(mapeo);
    }

    @GetMapping("/pagado/{id}")
    public String pagado(@PathVariable("id") int idPedido) {

        // Pedido cambio = pedidodao.findById(idPedido).orElse(null);
        // if (cambio != null) {
        // cambio.setEstado("PAGADO");
        // pedidodao.save(cambio);
        // }

        // TODO: update
        return "redirect:/success";
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String Payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {
        Stripe.apiKey = secretkey;
        String endpoint = "whsec_9850a314cfa962c23f3616368e571af42d3d78f675cac0825c67d2d5bdffbee5";
        Event event;
        try {
            event = Webhook.constructEvent(Payload, sigHeader, endpoint);

            if ("payment_intent.succeeded".equals(event.getType())) {

                PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer().getObject().get();
                log.info("Id_pedido" + intent.getMetadata().get("pedido_id"));
                String pedidoid = intent.getMetadata().get("pedido_id");
                Pedido pedido = pedidodao.findById(Integer.parseInt(pedidoid)).orElse(null);
                log.info("Pedido este" + pedido);

                if (pedido != null) {
                    pedido.setEstado("PAGADO");
                    pedidodao.save(pedido);

                }

            }

        } catch (SignatureVerificationException e) {
            e.printStackTrace();
            return ResponseEntity.status(400).build();
        }

        return ResponseEntity.ok().build();
    }

}
