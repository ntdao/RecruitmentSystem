package com.recruitmentsystem.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;

@MappedSuperclass
@Getter
@Setter
@ToString
public class Audit implements Serializable {
    private LocalDate createdAt = LocalDate.now();
    private LocalDate updatedAt = LocalDate.now();
    private Integer createdBy;
    private Integer updatedBy;
    private boolean deleteFlag=false;
    private Integer oldId;
}
