package com.recruitmentsystem.common.myEnum;

public enum JobLevel {
    INTERN ("Thực tập sinh/Sinh viên"),
    FRESHER ("Mới tốt nghiệp"),
    NON_MANAGER ("Nhân viên"),
    MANAGER ("Trưởng phòng"),
    DIRECTOR_AND_ABOVE ("Giám đốc và cấp cao hơn");
    public final String label;
    JobLevel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return this.label;
    }
}
