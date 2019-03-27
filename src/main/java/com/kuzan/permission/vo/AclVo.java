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
public class AclVo {
    private Integer id;

    @NotBlank(message = "权限点名字不能为空")
    @Length(min = 2,max = 20,message = "权限点名称长度在2到64之间")
    private String name;

    @NotNull(message = "必须指定所属权限模块")
    private Integer aclModuleId;

    @Length(min = 6,max = 100,message = "权限点url长度在6到256之间")
    private String url;

    @NotNull(message = "必须指定权限点类型")
    @Min(value = 1,message = "权限点类型不合法")
    @Max(value = 3,message = "权限点类型不合法")
    private Integer type;

    @NotNull(message = "必须指定权限点状态")
    @Min(value = 0,message = "权限点状态不合法")
    @Max(value = 1,message = "权限点状态不合法")
    private Integer status;

    @NotNull(message = "必须指定权限点的顺序")
    private Integer seq;

    @Length(max = 200,message = "")
    private String remark;

}
