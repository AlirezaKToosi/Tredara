package com.novare.tredara.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "EMAIL", unique = true , nullable = false)
    private String email;

    @Column(name = "FULL_Name", nullable = false)
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

    public User(String fullName, String email, String password) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
    }
}