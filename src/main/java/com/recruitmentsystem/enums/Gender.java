package com.recruitmentsystem.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Gender {
    MALE("Nam"),
    FEMALE("Nữ"),
    OTHER("Khác");
    public final String label;

    Gender(String label) {
        this.label = label;
    }

    public static List<String> getAll() {
        return Arrays.stream(Gender.values())
                .map(l -> l.toString())
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return this.name();
    }
}
