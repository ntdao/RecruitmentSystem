package com.recruitmentsystem.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@MappedSuperclass
@Getter
@Setter
public class Audit {
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private int createdBy;
    private int updatedBy;
    private boolean deleteFlag;
    private int oldId;

    @Override
    public String toString() {
        return "Audit{" +
                "createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", createdBy=" + createdBy +
                ", updatedBy=" + updatedBy +
                ", deleteFlag=" + deleteFlag +
                ", oldId=" + oldId +
                '}';
    }
}
