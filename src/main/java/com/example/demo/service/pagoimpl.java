package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.stripe.pago;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.param.PaymentIntentCancelParams;
import com.stripe.param.PaymentIntentConfirmParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentIntentSearchParams;

@Service
public class pagoimpl implements pagoservice {

    @Value("${stripe.key.secret}")
    String secretkey;

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
}
