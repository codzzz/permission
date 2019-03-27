package com.kuzan.permission.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by sui on 2019/3/21.
 */
@Data
public class UserVo {
    private Integer id;
    @NotBlank(message = "用户名不能为空")
    @Length(min = 1,max = 20,message = "用户名长度在1到20之间")
    private String username;
    @NotBlank(message = "手机号不能为空")
    @Length(min = 11,max = 11,message = "手机号格式不正确")
    private String telephone;
    @NotBlank(message = "email不能为空")
    @Length(min = 5,max = 20,message = "邮箱长度在20之内")
    private String mail;
    @NotNull(message = "必须选择用户所在的部门")
    private Integer deptId;
    @NotNull(message = "必须指定用户账号的状态")
    @Min(value = 0,message = "用户状态不合法")
    @Max(value = 2,message = "用户状态不合法")
    private Integer status;
    @Length(max = 200,message = "备注不能超过200")
    private String remark = "";
}
