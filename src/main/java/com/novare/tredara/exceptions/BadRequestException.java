package com.novare.tredara.exceptions;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BadRequestException extends RuntimeException {

    String message;

    public BadRequestException(String message) {
        super(message);
        this.message = message;
    }

}
