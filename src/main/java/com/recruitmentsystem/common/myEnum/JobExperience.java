package com.recruitmentsystem.common.myEnum;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum JobExperience {
    NO("Chưa có kinh nghiệm"),
    LESS_THAN_1("Dưới 1 năm"),
    YEAR_1("1 năm"),
    YEAR_2("2 năm"),
    YEAR_3("3 năm"),
    YEAR_4("4 năm"),
    YEAR_5("5 năm"),
    YEAR_6("Trên 5 năm");
    public final String label;

    JobExperience(String label) {
        this.label = label;
    }

    public static List<String> getAll() {
        return Arrays.stream(JobExperience.values())
                .map(l -> l.toString())
                .collect(Collectors.toList());
    }
    @Override
    public String toString() {
        return this.label;
    }
}
