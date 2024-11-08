package com.dearpet.dearpet.service;

import com.dearpet.dearpet.dto.VerificationDTO;
import com.dearpet.dearpet.entity.Verification;
import com.dearpet.dearpet.repository.VerificationRepository;
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
 * Verification Service
 * @Author 위지훈
 * @Since 2024.10.30
 */
@Service
public class VerificationService {

    private final IamportClient iamportClient;
    private final VerificationRepository verificationRepository;

    @Autowired
    public VerificationService(
            VerificationRepository verificationRepository,
            @Value("${iamport.api.key}") String apiKey,
            @Value("${iamport.api.secret}") String apiSecret) {
        this.iamportClient = new IamportClient(apiKey, apiSecret);
        this.verificationRepository = verificationRepository;
    }

    // 사전 검증
    public void preparePayment(VerificationDTO verificationDTO){
        Verification verification = new Verification();
        verification.setMerchantUid(verificationDTO.getMerchantUid());
        verification.setExpectedAmount(verificationDTO.getExpectedAmount());
        verification.setStatus(Verification.Status.PENDING);

        try {
            PrepareData prepareData = new PrepareData(verificationDTO.getMerchantUid(), verificationDTO.getExpectedAmount());
            iamportClient.postPrepare(prepareData);
            verification.setStatus(Verification.Status.CONFIRMED);

        } catch (IamportResponseException | IOException e) {
            verification.setStatus(Verification.Status.FAILED);
        }

        verificationRepository.save(verification);
    }

    // 사후 검증
    public Payment confirmPayment(String impUid, String merchantUid) throws IamportResponseException, IOException {
        Verification verification = verificationRepository.findByMerchantUid(merchantUid)
                .orElseThrow(() -> new IllegalArgumentException("사전 등록된 결제 정보가 없습니다."));
        BigDecimal preAmount = verification.getExpectedAmount();

        IamportResponse<Payment> iamportResponse = iamportClient.paymentByImpUid(impUid);
        Payment paymentInfo = iamportResponse.getResponse();
        BigDecimal paidAmount = paymentInfo.getAmount();

        if (preAmount.compareTo(paidAmount) != 0) {
            // 금액 불일치 시 결제 취소 및 상태 기록
            CancelData cancelData = new CancelData(impUid, true);
            cancelData.setReason("결제 금액 불일치로 인한 자동 취소");
            iamportClient.cancelPaymentByImpUid(cancelData);

            // 새로운 취소 기록 생성
            Verification failedRecord = new Verification();
            failedRecord.setMerchantUid(merchantUid);
            failedRecord.setExpectedAmount(preAmount);
            failedRecord.setStatus(Verification.Status.FAILED);
            verificationRepository.save(failedRecord);

            throw new IllegalArgumentException("결제 금액이 일치하지 않아 결제가 취소되었습니다.");
        }

        // 결제가 성공한 경우, 상태 기록을 성공으로 업데이트
        Verification successRecord = new Verification();
        successRecord.setMerchantUid(merchantUid);
        successRecord.setExpectedAmount(preAmount);
        successRecord.setStatus(Verification.Status.CONFIRMED);
        verificationRepository.save(successRecord);

        return paymentInfo;
    }
}
