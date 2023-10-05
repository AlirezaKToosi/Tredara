package com.novare.tredara.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import java.sql.Blob;
import java.util.Objects;


@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "image")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "IMAGE_BLOB")
    public Blob imageBlob;

    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private Item item;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Image image = (Image) o;
        return id != null && Objects.equals(id, image.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
