package com.kuzan.permission.dao;

import com.kuzan.permission.entity.SysAcl;
import com.kuzan.permission.vo.PageQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysAclMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysAcl record);

    int insertSelective(SysAcl record);

    SysAcl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysAcl record);

    int updateByPrimaryKey(SysAcl record);

    int countByNameAndAclModuleId(@Param("aclModuleId") Integer aclModuleId,@Param("name") String name,@Param("id") Integer id);

    int countByAclModuleId(@Param("aclModuleId") Integer aclModuleId);

    List<SysAcl> getPageByAclModuleId(@Param("aclModuleId") Integer aclModuleId,@Param("page") PageQuery page);

    List<SysAcl> listAll();

    List<SysAcl> listByListId(List<Integer> ids);

    List<SysAcl> listByUrl(String url);
}