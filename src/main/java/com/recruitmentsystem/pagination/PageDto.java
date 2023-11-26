package com.recruitmentsystem.pagination;

import lombok.Data;

import java.util.List;

@Data
public class PageDto {
    private Integer pageNo;
    private Integer pageSize;
    private String sortBy;
    private String sortDir;
    private String name;
    private List<Integer> categories;
}
