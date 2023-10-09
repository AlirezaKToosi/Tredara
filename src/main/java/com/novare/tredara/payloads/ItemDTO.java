package com.novare.tredara.payloads;

import com.novare.tredara.models.EItemStatus;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;


@NoArgsConstructor
@Getter
@Setter
public class ItemDTO {

    private Long id;

    @NotEmpty (message = "Title is required !!")
    private String title;

    private String description;

    private double startPrice;

    private Date startDateTime;

    private Date endDateTime;

    private EItemStatus status;

    private long userID;

    private String image_url;
}