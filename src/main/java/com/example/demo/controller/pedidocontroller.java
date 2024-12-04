package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dao.pedidorepository;
import com.example.demo.entity.Pedido;
import com.example.demo.service.pagoimpl;
import com.example.demo.service.pedidoimpl;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Slf4j
@Controller
@RequestMapping("/pedido")
public class pedidocontroller {
    @Value("${stripe.key.secret}")
    String secretkey;
    @Autowired
    private JavaMailSender mailSender;
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

    @PostMapping("/actualizarEstado")
    public ResponseEntity<String> actualizarEstado(@RequestBody Map<String, String> request) {

        return pedidoimpl.actualizarEstado(request);
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String Payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {
        Stripe.apiKey = secretkey;
        String endpoint = "whsec_c6tISUjzdoPY0dt89vYDKqf3Ix6QRjs9";
        Event event;
        SimpleMailMessage mensaje = new SimpleMailMessage();

        try {
            event = Webhook.constructEvent(Payload, sigHeader, endpoint);

            if ("payment_intent.succeeded".equals(event.getType())) {

                PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer().getObject().get();
                log.info("Id_pedido" + intent.getMetadata().get("pedido_id"));
                String pedidoid = intent.getMetadata().get("pedido_id");
                Pedido pedido = pedidodao.findById(Integer.parseInt(pedidoid)).orElse(null);

                if (pedido != null) {
                    pedido.setEstado("PAGADO");
                    pedido.setId_pago(intent.getId());
                    pedidodao.save(pedido);
                    mensaje.setTo(pedido.getId_usuario().getEmail());
                    mensaje.setSubject("Pedido pagado");
                    mensaje.setText("El pedido con id: " + pedido.getId_pedido() + " fue pagado exitosamente");
                    mensaje.setFrom("marioelpro08@gmail.gmail");
                    mailSender.send(mensaje);
                }

            }

        } catch (SignatureVerificationException e) {
            e.printStackTrace();
            return ResponseEntity.status(400).build();
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/rembolso")
    public ResponseEntity<String> rembolso(@RequestBody Map<String, String> requesmap) throws StripeException {
        //
        return pedidoimpl.rembolso(requesmap, secretkey);
    }

}
