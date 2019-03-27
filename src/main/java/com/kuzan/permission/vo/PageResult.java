package com.kuzan.permission.vo;

import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Created by sui on 2019/3/22.
 */
@Data
@Builder
public class PageResult<T> {
    private List<T> data = Lists.newArrayList();

    private int total;
}
