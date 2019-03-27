package com.kuzan.permission.service.impl;

import com.google.common.base.Preconditions;
import com.kuzan.permission.commons.BeanValidator;
import com.kuzan.permission.commons.RequestHolder;
import com.kuzan.permission.dao.SysRoleMapper;
import com.kuzan.permission.entity.SysRole;
import com.kuzan.permission.exception.ParamException;
import com.kuzan.permission.service.SysRoleService;
import com.kuzan.permission.util.IpUtil;
import com.kuzan.permission.vo.RoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by sui on 2019/3/25.
 */
@Service
public class SysRoleServiceImpl implements SysRoleService{
    @Autowired
    private SysRoleMapper roleMapper;

    @Override
    public void save(RoleVo roleVo) {
        BeanValidator.check(roleVo);
        if (checkExists(roleVo.getName(),roleVo.getId())){
            throw new ParamException("角色名称已存在");
        }
        SysRole role = SysRole.builder().name(roleVo.getName()).type(roleVo.getType()).status(roleVo.getStatus()).remark(roleVo.getRemark()).build();
        role.setOperator(RequestHolder.getCurrentUser().getUsername());
        role.setOperateIp(IpUtil.getUserIP(RequestHolder.getCurrentRequest()));
        role.setOperateTime(new Date());

        roleMapper.insertSelective(role);
    }

    @Override
    public void update(RoleVo roleVo) {
        BeanValidator.check(roleVo);
        if (checkExists(roleVo.getName(),roleVo.getId())){
            throw new ParamException("角色名称已存在");
        }
        SysRole before = roleMapper.selectByPrimaryKey(roleVo.getId());
        Preconditions.checkNotNull(before,"待更新角色不存在");

        SysRole role = SysRole.builder().id(roleVo.getId()).name(roleVo.getName()).type(roleVo.getType()).status(roleVo.getStatus()).remark(roleVo.getRemark()).build();
        role.setOperator(RequestHolder.getCurrentUser().getUsername());
        role.setOperateIp(IpUtil.getUserIP(RequestHolder.getCurrentRequest()));
        role.setOperateTime(new Date());

        roleMapper.updateByPrimaryKey(role);

    }

    @Override
    public List<SysRole> listAll() {
        return roleMapper.listAll();
    }


    private boolean checkExists(String name,Integer id){
        return roleMapper.countRoleByNameAndId(name,id)>0;
    }
}
