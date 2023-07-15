package com.recruitmentsystem.model.user;

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
public class UserPagination {
    long total;
    long totalPage;
    long pageSize;
    long pageNo;
    List<UserDisplayModel> list;
}
