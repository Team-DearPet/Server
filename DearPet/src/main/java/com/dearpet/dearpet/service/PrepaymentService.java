package com.dearpet.dearpet.service;

import com.dearpet.dearpet.dto.PrePaymentDTO;
import com.dearpet.dearpet.entity.PrePayment;
import com.dearpet.dearpet.repository.PrePaymentRepository;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.request.PrepareData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;

/*
 * PrePayment Service
 * @Author 위지훈
 * @Since 2024.10.30
 */
@Service
public class PrepaymentService {

    private final IamportClient iamportClient;
    private final PrePaymentRepository prePaymentRepository;

    @Autowired
    public PrepaymentService(
            PrePaymentRepository prePaymentRepository,
            @Value("${iamport.api.key}") String apiKey,
            @Value("${iamport.api.secret}") String apiSecret) {
        this.iamportClient = new IamportClient(apiKey, apiSecret);
        this.prePaymentRepository = prePaymentRepository;
    }

    // 사전 검증
    public void preparePayment(PrePaymentDTO prePaymentDTO){
        PrePayment prePayment = new PrePayment();
        prePayment.setMerchantUid(prePaymentDTO.getMerchantUid());
        prePayment.setExpectedAmount(prePaymentDTO.getExpectedAmount());
        prePayment.setStatus(PrePayment.Status.PENDING);

        try {
            PrepareData prepareData = new PrepareData(prePaymentDTO.getMerchantUid(), prePaymentDTO.getExpectedAmount());
            iamportClient.postPrepare(prepareData);
            prePayment.setStatus(PrePayment.Status.CONFIRMED);

        } catch (IamportResponseException | IOException e) {
            prePayment.setStatus(PrePayment.Status.FAILED);
        }

        prePaymentRepository.save(prePayment);
    }

    // 사후 검증
    public Payment validatePayment(String impUid, String merchantUid) throws IamportResponseException, IOException {
        PrePayment prePayment = prePaymentRepository.findByMerchantUid(merchantUid)
                .orElseThrow(() -> new IllegalArgumentException("사전 등록된 결제 정보가 없습니다." + merchantUid));
        BigDecimal preAmount = prePayment.getExpectedAmount();

        IamportResponse<Payment> iamportResponse = iamportClient.paymentByImpUid(impUid);
        Payment paymentInfo = iamportResponse.getResponse();
        BigDecimal paidAmount = paymentInfo.getAmount();

        if (preAmount.compareTo(paidAmount) != 0) {
            // 금액 불일치 시 결제 취소 및 상태 기록
            CancelData cancelData = new CancelData(impUid, true);
            cancelData.setReason("결제 금액 불일치로 인한 자동 취소");
            iamportClient.cancelPaymentByImpUid(cancelData);

            // 새로운 취소 기록 생성
            PrePayment failedRecord = new PrePayment();
            failedRecord.setMerchantUid(merchantUid);
            failedRecord.setExpectedAmount(preAmount);
            failedRecord.setStatus(PrePayment.Status.FAILED);
            prePaymentRepository.save(failedRecord);

            throw new IllegalArgumentException("결제 금액이 일치하지 않아 결제가 취소되었습니다.");
        }

        // 결제가 성공한 경우, 상태 기록을 성공으로 업데이트
        PrePayment successRecord = new PrePayment();
        successRecord.setMerchantUid(merchantUid);
        successRecord.setExpectedAmount(preAmount);
        successRecord.setStatus(PrePayment.Status.CONFIRMED);
        prePaymentRepository.save(successRecord);

        return paymentInfo;
    }
}
