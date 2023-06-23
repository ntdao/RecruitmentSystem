package com.recruitmentsystem.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;

@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Audit implements Serializable {
    private Instant createdAt;
    private Instant updatedAt;
    private Integer createdBy;
    private Integer updatedBy;
    private Boolean deleteFlag = false;
    private Integer oldId;

    public boolean isDeleteFlag() {
        return (deleteFlag == true) ? true : false;
    }
}
