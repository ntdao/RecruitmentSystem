package com.recruitmentsystem.common.myEnum;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum JobLevel {
    INTERN("Thực tập sinh/Sinh viên"),
    FRESHER("Mới tốt nghiệp"),
    NON_MANAGER("Nhân viên"),
    MANAGER("Trưởng phòng"),
    DIRECTOR_AND_ABOVE("Giám đốc và cấp cao hơn");
    public final String label;

    JobLevel(String label) {
        this.label = label;
    }

    public static List<String> getAll() {
        return Arrays.stream(JobLevel.values())
                .map(l -> l.toString())
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return this.label;
    }
}
