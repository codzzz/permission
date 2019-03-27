package com.kuzan.permission.dao;

import com.kuzan.permission.entity.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

    int countRoleByNameAndId(@Param("name") String name,@Param("id") Integer id);

    List<SysRole> listAll();
}