package com.kuzan.permission.service;

import java.util.List;

/**
 * @author : sui
 * @time : 2019/3/25
 */
public interface SysRoleAclService {
    void changeRoleAcls(Integer roleId, List<Integer> aclIds);
}
