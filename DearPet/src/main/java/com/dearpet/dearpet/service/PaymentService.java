package com.dearpet.dearpet.service;

import com.dearpet.dearpet.entity.Payment;
import com.dearpet.dearpet.repository.PaymentRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    // application.properties의 키와 시크릿 값을 가져옴
    @Value("${iamport.api.key}")
    private String apiKey;

    @Value("${iamport.api.secret}")
    private String apiSecret;

    // 환불 요청 메서드 추가
    public Payment requestRefund(String impUid, BigDecimal cancelAmount, String reason) {
        String token = getAuthToken(); // 인증 토큰 가져오기
        RestTemplate restTemplate = new RestTemplate();
        String cancelUrl = "https://api.iamport.kr/payments/cancel";

        JSONObject requestBody = new JSONObject();
        requestBody.put("imp_uid", impUid);
        requestBody.put("amount", cancelAmount);
        requestBody.put("reason", reason);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(cancelUrl, HttpMethod.POST, requestEntity, String.class);
        JSONObject jsonResponse = new JSONObject(response.getBody());

        // 환불 성공 시 Payment 엔티티 업데이트
        if (jsonResponse.getInt("code") == 0) {
            Payment payment = paymentRepository.findByImpUid(impUid)
                    .orElseThrow(() -> new RuntimeException("Payment not found"));

            payment.setCancelAmount(cancelAmount);
            payment.setCancelReason(reason);
            payment.setCancelledAt(LocalDateTime.now());

            return paymentRepository.save(payment);
        } else {
            throw new RuntimeException("Refund failed: " + jsonResponse.getString("message"));
        }
    }


    public Payment savePayment(String impUid) {
        // 아임포트 API 호출하여 결제 데이터 가져오기
        String token = getAuthToken(); // 인증 토큰 가져오기
        Payment payment = fetchPaymentDataFromIamport(impUid, token);
        return paymentRepository.save(payment);
    }

    private String getAuthToken() {
        RestTemplate restTemplate = new RestTemplate();
        String authUrl = "https://api.iamport.kr/users/getToken";

        // 요청에 필요한 키와 시크릿 키를 application.properties에서 불러옴
        JSONObject requestBody = new JSONObject();
        requestBody.put("imp_key", apiKey);
        requestBody.put("imp_secret", apiSecret);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(authUrl, HttpMethod.POST, requestEntity, String.class);
        JSONObject jsonResponse = new JSONObject(response.getBody());

        return jsonResponse.getJSONObject("response").getString("access_token");
    }

    private Payment fetchPaymentDataFromIamport(String impUid, String token) {
        RestTemplate restTemplate = new RestTemplate();
        String paymentUrl = "https://api.iamport.kr/payments/" + impUid;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(paymentUrl, HttpMethod.GET, requestEntity, String.class);

        JSONObject jsonResponse = new JSONObject(response.getBody());
        JSONObject paymentData = jsonResponse.getJSONObject("response");

        // 필요한 데이터만 매핑하여 Payment 엔티티 생성
        Payment payment = new Payment();
        payment.setImpUid(paymentData.getString("imp_uid"));
        payment.setMerchantUid(paymentData.getString("merchant_uid"));
        payment.setAmount(paymentData.getBigDecimal("amount"));
        payment.setBuyerAddr(paymentData.optString("buyer_addr", null));
        payment.setBuyerEmail(paymentData.optString("buyer_email", null));
        payment.setBuyerName(paymentData.optString("buyer_name", null));
        payment.setBuyerTel(paymentData.optString("buyer_tel", null));
        payment.setCancelAmount(paymentData.optBigDecimal("cancel_amount", BigDecimal.ZERO));
        payment.setCancelHistory(paymentData.optString("cancel_history", null)); // JSON 배열 처리 시 수정 필요
        payment.setCancelReason(paymentData.optString("cancel_reason", null));

        // cancelled_at 처리: Unix 타임스탬프일 경우 변환
        if (paymentData.has("cancelled_at") && !paymentData.isNull("cancelled_at")) {
            Object cancelledAtObj = paymentData.get("cancelled_at");
            if (cancelledAtObj instanceof Integer) {
                int timestamp = (Integer) cancelledAtObj;
                payment.setCancelledAt(timestamp > 0 ?
                        LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault()) : null);
            } else if (cancelledAtObj instanceof String) {
                payment.setCancelledAt(LocalDateTime.parse((String) cancelledAtObj));
            }
        }

        // paid_at 처리: Unix 타임스탬프일 경우 변환
        if (paymentData.has("paid_at") && !paymentData.isNull("paid_at")) {
            Object paidAtObj = paymentData.get("paid_at");
            if (paidAtObj instanceof Integer) {
                int timestamp = (Integer) paidAtObj;
                payment.setPaidAt(timestamp > 0 ?
                        LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault()) : null);
            } else if (paidAtObj instanceof String) {
                payment.setPaidAt(LocalDateTime.parse((String) paidAtObj));
            }
        }

        payment.setCashReceiptIssued(paymentData.optBoolean("cash_receipt_issued", false));
        payment.setCardName(paymentData.optString("card_name", null));
        payment.setCurrency(paymentData.optString("currency", "KRW"));
        payment.setEscrow(paymentData.optBoolean("escrow", false));
        payment.setPayMethod(paymentData.optString("pay_method", null));
        payment.setPgProvider(paymentData.optString("pg_provider", null));
        payment.setPgTid(paymentData.optString("pg_tid", null));
        payment.setStatus(paymentData.optString("status", null));
        payment.setName(paymentData.optString("name", null));

        return payment;
    }
    public Optional<Payment> getPaymentByImpUid(String impUid) {
        return paymentRepository.findByImpUid(impUid);
    }
}