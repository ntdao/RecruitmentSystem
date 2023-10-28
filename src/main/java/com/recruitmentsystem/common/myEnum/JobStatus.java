package com.recruitmentsystem.common.myEnum;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum JobStatus {
    RECRUITING("Đang tuyển"),
    COMPLETED("Hoàn thành"),
    EXPIRED("Hết hạn"),
    DELETED("Đã xóa");
    public final String label;

    JobStatus(String label) {
        this.label = label;
    }

    public static List<String> getAll() {
        return Arrays.stream(JobStatus.values())
                .map(l -> l.toString())
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return this.label;
    }
}
