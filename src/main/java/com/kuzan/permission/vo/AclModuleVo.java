package com.kuzan.permission.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by sui on 2019/3/23.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AclModuleVo {
    private Integer id;

    @NotBlank(message = "权限模块名称不能为空")
    @Length(min = 2,max = 20,message = "权限名称长度在2到64之间")
    private String name;

    private Integer parentId = 0;

    @NotNull(message = "权限模块展示顺序不能为空")
    private Integer seq;

    @NotNull(message = "状态不能为空")
    @Min(value = 0,message = "状态不合法")
    @Max(value = 1,message = "状态不合法")
    private Integer status;

    @Length(max = 200,message = "备注不能超过64")
    private String remark;


}
