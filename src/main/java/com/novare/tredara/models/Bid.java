package com.novare.tredara.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity(name = "bid")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "bid")
    public class Bid {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @Column(name = "AMOUNT")
        private double amount;
        @Column(name = "BID_TIME")
        private Date bidTime;


        @ManyToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "USER_ID")
        private User user;

        @ManyToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "ITEM_ID")
        private Item item;
        @OneToMany(mappedBy = "bid")
        private List<Notification> notifications;
    }
