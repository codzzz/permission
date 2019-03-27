package com.kuzan.permission.service;

import com.kuzan.permission.entity.SysAcl;
import com.kuzan.permission.vo.AclVo;
import com.kuzan.permission.vo.PageQuery;
import com.kuzan.permission.vo.PageResult;

/**
 * Created by sui on 2019/3/23.
 */
public interface SysAclService {
    void save(AclVo aclModuleVo);
    void update(AclVo aclModuleVo);
    PageResult<SysAcl> getPageByAclModuleId(Integer aclModuleId, PageQuery page);
}
