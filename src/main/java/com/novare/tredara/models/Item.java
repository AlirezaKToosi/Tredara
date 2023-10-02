package com.novare.tredara.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "item")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "TITLE")
    private String title;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "START_PRICE")
    private double startPrice;
    @Column(name = "START_TIME")
    private Date startTime;
    @Column(name = "END_TIME")
    private Date endTime;

    @Column(name = "STATUS")
    private EItemStatus status;

    @ManyToOne
    @JoinColumn(name = "SELLER_USER_ID")
    private User user;
    @OneToMany(mappedBy = "item")
    private List<Bid> bids;
    @OneToMany(mappedBy = "item")
    private List<Notification> notifications;

}
