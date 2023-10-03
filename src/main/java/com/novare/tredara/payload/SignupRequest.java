package com.novare.tredara.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {

    private Long id;
    private String email;
    private String password;
    private String name;
    private Integer type;
}