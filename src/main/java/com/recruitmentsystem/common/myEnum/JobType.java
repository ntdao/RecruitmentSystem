//package com.recruitmentsystem.common.myEnum;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public enum JobType {
//    FULL_TIME("Toàn thời gian"),
//    PART_TIME("Bán thời gian"),
//    INTERN("Thực tập"),
//    ONLINE("Việc làm online"),
//    SEASON("Hợp đồng thời vụ");
//    public final String label;
//
//    JobType(String label) {
//        this.label = label;
//    }
//
//    public static List<String> getAll() {
//        return Arrays.stream(JobType.values())
//                .map(l -> l.toString())
//                .collect(Collectors.toList());
//    }
//
//    public static JobType fromString(String text) {
//        for (JobType j : JobType.values()) {
//            if (j.label.equalsIgnoreCase(text)) {
//                return j;
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public String toString() {
//        return this.label;
//    }
//}
