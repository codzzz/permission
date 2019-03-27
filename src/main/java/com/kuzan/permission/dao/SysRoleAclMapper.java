package com.kuzan.permission.dao;

import com.kuzan.permission.entity.SysRoleAcl;

import java.util.List;

public interface SysRoleAclMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRoleAcl record);

    int insertSelective(SysRoleAcl record);

    SysRoleAcl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRoleAcl record);

    int updateByPrimaryKey(SysRoleAcl record);

    List<Integer> getAclIdByRoleIdList(List<Integer> roleIdList);

    int deleteByRoleId(Integer roleId);

    int batchInsertSysRoleAcl(List<SysRoleAcl> roleAcls);



}