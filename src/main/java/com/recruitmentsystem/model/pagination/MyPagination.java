package com.recruitmentsystem.model.pagination;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Service;

import java.util.List;

@Builder
@Getter
@Setter
@ToString
public class MyPagination<T> {
    long total;
    long totalPage;
    long pageSize;
    long pageNo;
    List<T> list;
}
