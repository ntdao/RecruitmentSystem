package com.recruitmentsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@Table
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@ToString
public class Application extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Integer applicationId;
    @Column(nullable = false)
    private String fileSave;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "collection_id")
    private Category collection;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Application that = (Application) o;
        return getApplicationId() != null && Objects.equals(getApplicationId(), that.getApplicationId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
