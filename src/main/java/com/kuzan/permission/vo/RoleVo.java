package com.kuzan.permission.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by sui on 2019/3/25.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleVo {
    private Integer id;

    @NotBlank(message = "角色名称不能为空")
    private String name;

    @NotNull(message = "角色类型不能为空")
    @Max(value = 2,message = "角色类型不合法")
    @Min(value = 1,message = "角色类型不合法")
    private Integer type=1;

    @NotNull(message = "角色状态不能为空")
    @Max(value = 1,message = "角色状态不合法")
    @Min(value = 0,message = "角色状态不合法")
    private Integer status;

    private String remark;
}
