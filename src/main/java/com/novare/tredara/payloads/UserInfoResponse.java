package com.novare.tredara.payloads;

import lombok.Data;


@Data
public class UserInfoResponse {
    private Long id;
    private String email;
    private String fullName;
    private String role;
    private Integer type;
    public UserInfoResponse(Long id, String email, String fullName, String role) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
    }

}
