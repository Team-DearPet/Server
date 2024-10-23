package com.dearpet.dearpet.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;        // PK
    private String username;    // 회원 실명
    private String nickname;    // 회원 닉네임
    private String password;    // 회원 비밀번호
    private String email;       // 회원 이메일

    @Enumerated(EnumType.STRING)
    private OAuthType oauth;    // 소셜 로그인 여부

    @Enumerated(EnumType.STRING)
    private UserGrade grade;    // 회원 등급

    private LocalDateTime createdAt;
    private Boolean isDeleted;

    public enum OAuthType {
        KAKAO, GOOGLE, NONE
    }

    public enum UserGrade {
        MEMBER, ADMIN
    }
}