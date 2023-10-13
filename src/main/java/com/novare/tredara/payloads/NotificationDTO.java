package com.novare.tredara.payloads;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class NotificationDTO {
    private Long id;
    private String status;
    private Date timestamp;
    private Date filterStartInterval;
    private Date filterEndInterval;
    private Integer filterStatus;
    private Long userId;
    private Long bidId;
    private Long itemId;
}
