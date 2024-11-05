package com.dearpet.dearpet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String verificationCode) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);

            String htmlContent = "<div style='font-family: Arial, sans-serif; padding: 20px;'>" +
                    "<h2 style='color: #7B52E1;'>CarePet 이메일 인증</h2>" +
                    "<p>안녕하세요, CarePet 서비스를 이용해주셔서 감사합니다.</p>" +
                    "<p>아래의 인증 코드를 입력하여 이메일 인증을 완료해주세요:</p>" +
                    "<h3 style='color: #333; background-color: #F0F0F0; padding: 10px; display: inline-block;'>" + verificationCode + "</h3>" +
                    "<p>이 코드는 10분 동안만 유효합니다.</p>" +
                    "<hr>" +
                    "<p style='font-size: 0.9rem; color: #888;'>본 이메일은 발신 전용입니다. 문의사항은 CarePet 고객센터로 연락해주세요.</p>" +
                    "</div>";

            helper.setText(htmlContent, true); // true를 설정하여 HTML 내용을 사용

            mailSender.send(message);
            System.out.println("이메일 발송 완료: " + to + " - " + subject);
        } catch (MessagingException e) {
            System.err.println("이메일 발송 실패: " + e.getMessage());
        }
    }

    public String generateVerificationCode() {
        int codeLength = 6; // 인증번호의 길이
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < codeLength; i++) {
            int digit = (int) (Math.random() * 10); // 0~9 사이의 랜덤 숫자 생성
            code.append(digit);
        }
        return code.toString();
    }
}