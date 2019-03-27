package com.kuzan.permission.dao;

import com.kuzan.permission.entity.SysRoleUser;

import java.util.List;

public interface SysRoleUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRoleUser record);

    int insertSelective(SysRoleUser record);

    SysRoleUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRoleUser record);

    int updateByPrimaryKey(SysRoleUser record);

    List<Integer> getRoleIdListByUserId(Integer userId);

    List<Integer> getUserIdByRoleId(Integer roleId);

    int deleteByRoleId(Integer roleId);

    int batchInsertSysRoleUser(List<SysRoleUser> roleUserList);
}