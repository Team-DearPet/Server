package com.dearpet.dearpet.dto;

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
    private String oauth;
    private String password;

    private Boolean isDeleted;

    public UserDTO(Long id, String username, String nickname, String email, String roleName, String oauth) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.roleName = roleName;
        this.oauth = oauth;
    }

    public UserDTO(Long id, String username, String nickname, String email, String roleName, String oauth, String password) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.roleName = roleName;
        this.oauth = oauth;
        this.password = password;
    }
}
