package com.kuzan.permission.service;

import com.kuzan.permission.vo.AclModuleVo;

/**
 * Created by sui on 2019/3/23.
 */
public interface SysAclModuleService {
    void save(AclModuleVo aclModuleVo);
    void update(AclModuleVo aclModuleVo);
    void delete(Integer aclModuleId);
}
