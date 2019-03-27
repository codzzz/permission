package com.kuzan.permission.service;

import com.kuzan.permission.entity.SysAcl;

import java.util.List;

/**
 * Created by sui on 2019/3/25.
 */
public interface SysCoreService {
    List<SysAcl> getCurrentUserAclList();

    List<SysAcl> getRoleAclList(Integer roleId);

    boolean hasUrlAcl(String url);
}
