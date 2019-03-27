package com.kuzan.permission.service;

import com.kuzan.permission.dto.AclModuleLevelDto;
import com.kuzan.permission.dto.DeptLevelDto;

import java.util.List;

/**
 * Created by sui on 2019/3/21.
 */
public interface SysTreeService {
    List<DeptLevelDto> deptTree();
    List<AclModuleLevelDto> aclModuleTree();
    List<AclModuleLevelDto> roleTree(Integer roleId);
}
