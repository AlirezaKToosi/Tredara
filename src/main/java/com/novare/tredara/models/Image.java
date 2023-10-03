package com.novare.tredara.models;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Blob;


@Data
@Entity
@Table(name = "image")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "IMAGE_BLOB")
    private Blob imageBlob;

    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private Item item;
}
