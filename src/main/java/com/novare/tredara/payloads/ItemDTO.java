package com.novare.tredara.payloads;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.novare.tredara.models.EItemStatus;
import com.novare.tredara.models.Image;
import com.novare.tredara.models.User;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.sql.Blob;
import java.util.Date;


@NoArgsConstructor
@Getter
@Setter
public class ItemDTO {

    @NotEmpty (message = "Title is required !!")
    private String title;

    private String description;

    private double startPrice;

    private Date startDateTime;

    private Date endDateTime;

    private EItemStatus status;

    private long userID;

    private String imageString;
}
