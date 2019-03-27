package com.kuzan.permission.service;

import com.kuzan.permission.entity.SysRole;
import com.kuzan.permission.vo.RoleVo;

import java.util.List;

/**
 * Created by sui on 2019/3/25.
 */
public interface SysRoleService {
    void save(RoleVo roleVo);
    void update(RoleVo roleVo);
    List<SysRole> listAll();
}
