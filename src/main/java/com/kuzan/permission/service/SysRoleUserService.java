package com.kuzan.permission.service;

import com.kuzan.permission.entity.SysUser;

import java.util.List;

/**
 * @author : sui
 * @time : 2019/3/25
 */
public interface SysRoleUserService {
    List<SysUser> getUserByRoleId(Integer roleId);
    void changeRoleUsers(Integer roleId,List<Integer> userIds);
}
