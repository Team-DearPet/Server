package com.dearpet.dearpet.controller;

import com.dearpet.dearpet.entity.Payment;
import com.dearpet.dearpet.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/save")
    public ResponseEntity<Payment> savePayment(@RequestBody Map<String, String> payload) {
        String impUid = payload.get("impUid");
        System.out.println("Received impUid: " + impUid);

        Payment savedPayment = paymentService.savePayment(impUid);
        System.out.println("Saved Payment: " + savedPayment);

        return ResponseEntity.ok(savedPayment);
    }

    @GetMapping("/{imp_uid}")
    public ResponseEntity<Payment> getPaymentByImpUid(@PathVariable("imp_uid") String impUid) {
        Optional<Payment> payment = paymentService.getPaymentByImpUid(impUid);
        return payment.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
