package com.novare.tredara.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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
    private Date startDateTime;

    @Column(name = "END_TIME")
    private Date endDateTime;

    @Column(name = "STATUS")
    private EItemStatus status;

    @Column(name = "IMAGE_URL")
    private String image_url;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "SELLER_USER_ID")
    private User user;

    @OneToMany(mappedBy = "item",cascade = CascadeType.ALL)
    private List<Bid> bids;
    @OneToMany(mappedBy = "item",cascade = CascadeType.ALL)
    private List<Log> logs;

    @OneToMany(mappedBy = "item",cascade = CascadeType.ALL)
    private List<Notification> notifications;
}