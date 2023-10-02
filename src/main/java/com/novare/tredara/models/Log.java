package com.novare.tredara.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "log")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "ACTION_TYPE")
    private String actionType;
    @Column(name = "ACTION_DETAILS")
    private String actionDetails;
    @Column(name = "TIME_STAMP")
    private Date timestamp;
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

}
