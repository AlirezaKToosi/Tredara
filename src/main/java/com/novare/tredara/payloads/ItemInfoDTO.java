package com.novare.tredara.payloads;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemInfoDTO {
    private String imageUrl;
    private String title;
    private String description;
    private double startPrice;
    private String timeToBidEnd;
    private int numberOfBids;
}
