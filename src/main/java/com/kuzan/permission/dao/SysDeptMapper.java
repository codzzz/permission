package com.kuzan.permission.dao;

import com.kuzan.permission.entity.SysDept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysDeptMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysDept record);

    int insertSelective(SysDept record);

    SysDept selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysDept record);

    int updateByPrimaryKey(SysDept record);

    List<SysDept> listAllDept();

    List<SysDept> listChildDeptByLevel(String level);

    int batchUpdataDept(List<SysDept> depts);

    int countByNameAndParentId(@Param("parentId") Integer parentId,@Param("name") String name,@Param("deptId") Integer id);

    List<Integer> getChildIdByDeptId(@Param("parentId") Integer parentId);

    int countByParentId(Integer parentId);
}