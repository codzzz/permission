package com.kuzan.permission.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

/**
 * Created by sui on 2019/3/22.
 */
public class PageQuery {
    @Getter
    @Setter
    @Min(value = 1,message = "当前页码不合法")
    private int pageNo = 1;

    @Getter
    @Setter
    @Min(value = 1,message = "每页展示数据不合法")
    private int pageSize = 10;

    @Setter
    private int offset;

    public int getOffset() {
        return (pageNo-1)*pageSize;
    }
}
