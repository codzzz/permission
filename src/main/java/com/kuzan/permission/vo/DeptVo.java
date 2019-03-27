package com.kuzan.permission.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Created by sui on 2019/3/21.
 */
@Data
public class DeptVo {
    private Integer id;

    @NotBlank(message = "部门名不能为空")
    @Length(max = 15,min = 2,message = "部门名在2到15之间")
    private String name;

    private Integer parentId = 0;

    @NotNull(message = "展示顺序不能为空")
    private Integer seq;

    @Length(max = 150,message = "备注长度不能超过150")
    private String remark;


}
