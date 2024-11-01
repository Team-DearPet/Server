package com.dearpet.dearpet.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;        // PK

    private String username;    // 회원 실명
    private String nickname;    // 회원 닉네임
    private String password;    // 회원 비밀번호
    private String email;       // 회원 이메일

    @Enumerated(EnumType.STRING)
    private OAuthType oauth;    // 소셜 로그인 여부

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    private LocalDateTime createdAt;
    private Boolean isDeleted;

    public enum OAuthType {
        KAKAO, GOOGLE, NONE
    }

    public enum UserGrade {
        MEMBER, ADMIN
    }

    // 권한 정보를 GrantedAuthority로 변환
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.getRoleName()));
    }

    public User update(String username, String nickname, String email) {
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        return this;
    }
}