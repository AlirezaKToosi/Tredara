package com.novare.tredara.payloads;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@NoArgsConstructor
@Getter
@Setter
public class BidDto {

    private Long id;
    @NotNull
    private double amount;
    private Date bidTime;
    private String bidderName;
    @NotNull
    private Long itemId;

}