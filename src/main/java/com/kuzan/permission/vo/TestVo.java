package com.kuzan.permission.vo;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Created by sui on 2019/3/21.
 */
@Data
public class TestVo {
    @NotBlank
    private String name;
    @NotNull
    private Integer id;
}
