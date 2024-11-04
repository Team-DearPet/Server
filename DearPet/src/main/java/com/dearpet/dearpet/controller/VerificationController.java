package com.dearpet.dearpet.controller;

import com.dearpet.dearpet.dto.VerificationDTO;
import com.dearpet.dearpet.service.VerificationService;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

/*
 * Verification Controller
 * @Author 위지훈
 * @Since 2024.10.30
 */
@RestController
@RequestMapping("/api/verification")
@CrossOrigin("*")
public class VerificationController {

    @Autowired
    private VerificationService verificationService;

    // 사전 검증
    @PostMapping("/prepare")
    public ResponseEntity<?> preparePayment(@RequestBody VerificationDTO prePaymentDTO) {
        try {
            verificationService.preparePayment(prePaymentDTO);
            return ResponseEntity.ok().body(Map.of("status", "CONFIRMED"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    // 사후 검증
    @PostMapping("/confirm")
    public ResponseEntity<?> confirmPayment(@RequestBody Map<String, String> requestData) throws IamportResponseException, IOException {
        String impUid = requestData.get("impUid");
        String merchantUid = requestData.get("merchantUid");

        Payment paymentInfo = verificationService.confirmPayment(impUid, merchantUid);
        return ResponseEntity.ok().body(paymentInfo);
    }

}
