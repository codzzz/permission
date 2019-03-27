package com.kuzan.permission.dao;

import com.kuzan.permission.entity.SysUser;
import com.kuzan.permission.vo.PageQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    SysUser findUserByKeyword(String keyword);

    int countUserByEmail(@Param("mail") String mail, @Param("id") Integer id);

    int countUserByTelephone(@Param("telephone") String telephone,@Param("id") Integer id);

    int countUserByDeptId(Integer deptId);

    List<SysUser> getPageByDeptId(@Param("deptId") Integer deptId, @Param("page")PageQuery page);

    int countUserByListId(List<Integer> listid);

    List<SysUser> getPageByDeptListId(@Param("listId") List<Integer> ids,@Param("page") PageQuery page);

    List<SysUser> getUserListByIdlist(List<Integer> ids);

    List<SysUser> listAll();

}