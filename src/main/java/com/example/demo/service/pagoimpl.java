package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.dao.pedidorepository;
import com.example.demo.entity.Pedido;
import com.example.demo.stripe.pago;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.PaymentIntentCancelParams;
import com.stripe.param.PaymentIntentConfirmParams;
import com.stripe.param.PaymentIntentSearchParams;
import com.stripe.param.checkout.SessionCreateParams;

@Service
public class pagoimpl implements pagoservice {

    @Value("${stripe.key.secret}")
    String secretkey;

    @Autowired
    pedidorepository pedidodao;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public PaymentIntent paymentintent(pago paymentintent) throws StripeException {
        Stripe.apiKey = secretkey;
        Map<String, Object> params = new HashMap<>();
        params.put("amount", paymentintent.getAmount());
        params.put("currency", paymentintent.getCurrency());
        params.put("description", paymentintent.getDescription());

        // Si deseas añadir tipos de pago específicos, agrega aquí
        ArrayList payment_method_types = new ArrayList<>();
        payment_method_types.add("card"); // Aquí puedes ajustar el tipo si es necesario

        // Asegúrate de que el PaymentIntent sea creado correctamente
        return PaymentIntent.create(params);
    }

    // Método de confirmación con un método de pago específico
    @Override
    public PaymentIntent confirmtest(String id) throws StripeException {
        Stripe.apiKey = secretkey;
        PaymentIntent resource = PaymentIntent.retrieve(id);

        // Confirma el PaymentIntent con un método de pago específico
        PaymentIntentConfirmParams params = PaymentIntentConfirmParams.builder()
                .setPaymentMethod("pm_card_visa").setReturnUrl("http://localhost:3600/index") // Asegúrate de que este
                                                                                              // método de pago esté
                // registrado en Stripe
                .build();

        return resource.confirm(params);
    }

    @Override
    public PaymentIntent cancel(String id) throws StripeException {
        Stripe.apiKey = secretkey;
        PaymentIntent resource = PaymentIntent.retrieve(id);

        // Cancelar el PaymentIntent si es necesario
        PaymentIntentCancelParams params = PaymentIntentCancelParams.builder().build();

        return resource.cancel(params);
    }

    // Método para obtener detalles del PaymentIntent
    public PaymentIntent sendtest(String id) throws StripeException {
        Stripe.apiKey = secretkey;
        PaymentIntent paymentIntent = PaymentIntent.retrieve(id);

        // Aquí puedes devolver el PaymentIntent si es necesario para otros fines
        return paymentIntent;
    }

    public PaymentIntent search(String id) throws StripeException {
        Stripe.apiKey = secretkey;
        PaymentIntentSearchParams params = PaymentIntentSearchParams.builder().build();

        // Busca el PaymentIntent por ID
        return PaymentIntent.search(params).getData().stream().filter(pi -> pi.getId().equals(id)).findFirst()
                .orElse(null);
    }

    @Override
    public ResponseEntity<Map<String, String>> sesionpay(Map<String, Object> mapeo) {
        try {
            Stripe.apiKey = secretkey;

            // Obtener el pedido por ID
            int pedidoId = (int) mapeo.get("id_pedido");
            Pedido pedido = pedidodao.findById(pedidoId).orElse(null);

            if (pedido == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Pedido no encontrado"));
            }

            // Crear los parámetros para Stripe Checkout Session
            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT).setCustomerEmail((String) mapeo.get("nombre"))
                    .setSuccessUrl("http://localhost:3600/success") // URL de éxito
                    .setCancelUrl("http://localhost:3600/cancel") // URL de cancelación
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L) // Siempre será 1 para un pedido completo
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("usd") // Cambia según tu configuración
                                                    .setUnitAmount((long) (pedido.getTotal() * 100)) // Monto en
                                                                                                     // centavos
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName("Pedido #" + pedidoId)
                                                                    .build())
                                                    .build())
                                    .build())
                    .build();

            // Crear la sesión de Stripe
            Session session = Session.create(params);

            // Retornar la URL de la sesión
            return ResponseEntity.ok(Map.of("url", session.getUrl()));
        } catch (StripeException e) {
            // Manejar errores de Stripe
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

}