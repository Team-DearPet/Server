package com.dearpet.dearpet.controller;

import com.dearpet.dearpet.dto.PrePaymentDTO;
import com.dearpet.dearpet.service.PrepaymentService;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

/*
 * PrePayment Controller
 * @Author 위지훈
 * @Since 2024.10.30
 */
@RestController
@RequestMapping("/api/prepayment")
@CrossOrigin("*")
public class PrePaymentController {

    @Autowired
    private PrepaymentService prePaymentService;

    // 사전 검증
    @PostMapping("/prepare")
    public ResponseEntity<?> preparePayment(@RequestBody PrePaymentDTO prePaymentDTO) {
        try {
            prePaymentService.preparePayment(prePaymentDTO);
            // JSON 응답으로 status 값을 포함하여 반환
            return ResponseEntity.ok().body(Map.of("status", "CONFIRMED"));
        } catch (Exception e) {
            // 오류가 발생할 경우 적절한 오류 메시지 반환
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    // 사후 검증
    @PostMapping("/validate")
    public ResponseEntity<?> validatePayment(@RequestBody Map<String, String> requestData) throws IamportResponseException, IOException {
        String impUid = requestData.get("impUid");
        String merchantUid = requestData.get("merchantUid");

        Payment paymentInfo = prePaymentService.validatePayment(impUid, merchantUid);
        return ResponseEntity.ok().body(paymentInfo);
    }

}
