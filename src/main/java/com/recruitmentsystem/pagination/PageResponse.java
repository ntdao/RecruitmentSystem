package com.recruitmentsystem.pagination;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@Setter
@ToString
public class PageResponse<T> {
    Integer totalElements;
    Integer number;
    List<T> list;
}
