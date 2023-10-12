package com.novare.tredara.payloads;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class LogDTO {
    private Long id;
    private String actionType;
    private String actionDetails;
    private Date timestamp;
    private Date filterStartInterval;
    private Date filterEndInterval;
    private Integer filterActionType;
    private Long userId;
}
