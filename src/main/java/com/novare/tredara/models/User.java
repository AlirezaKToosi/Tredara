package com.novare.tredara.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "EMAIL", unique = true , nullable = false)
    private String email;

    @Column(name = "FULL_Name", unique = true)
    private String fullName;


    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "Role")
    private ERole role;

    @OneToMany(mappedBy = "user")
    private List<Bid> bids;

    @OneToMany(mappedBy = "user")
    private List<Log> logs;

    @OneToMany(mappedBy = "user")
    private List<Item> items;

}