package com.kuzan.permission.dao;

import com.kuzan.permission.entity.SysAclModule;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysAclModuleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysAclModule record);

    int insertSelective(SysAclModule record);

    SysAclModule selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysAclModule record);

    int updateByPrimaryKey(SysAclModule record);

    int countByNameAndParentId(@Param("parentId") Integer parentId, @Param("name") String name, @Param("aclModuleId") Integer id);

    List<SysAclModule> listChildAclModuleByLevel(String level);

    int batchUpdataAclModule(List<SysAclModule> aclModules);

    List<SysAclModule> listAllAclModule();

    int countByParentid(Integer parentId);
}