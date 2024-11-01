package com.dearpet.dearpet.dto;

import com.dearpet.dearpet.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String username;
    private String nickname;
    private String email;

    @JsonProperty("role_name")
    private String roleName;

    @JsonProperty("oauth")
    private User.OAuthType oauth;
    private String password;

    private Boolean isDeleted;

    // 비밀번호 변경을 위한 필드 추가
    private String currentPassword;
    private String newPassword;

    public UserDTO(Long id, String username, String nickname, String email, String roleName, User.OAuthType oauth) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.roleName = roleName;
        this.oauth = oauth;
    }

    public UserDTO(Long id, String username, String nickname, String email, String roleName, User.OAuthType oauth, String password) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.roleName = roleName;
        this.oauth = oauth;
        this.password = password;
    }
}