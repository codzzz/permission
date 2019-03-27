package com.kuzan.permission.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.kuzan.permission.commons.RequestHolder;
import com.kuzan.permission.dao.SysRoleAclMapper;
import com.kuzan.permission.entity.SysRoleAcl;
import com.kuzan.permission.exception.GlobalException;
import com.kuzan.permission.service.SysRoleAclService;
import com.kuzan.permission.util.IpUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author : sui
 * @time : 2019/3/25
 */
@Service
public class SysRoleAclServiceImpl implements SysRoleAclService{

    @Autowired
    private SysRoleAclMapper roleAclMapper;
    @Override
    public void changeRoleAcls(Integer roleId, List<Integer> aclIds) {
        List<Integer> originAclIdList = roleAclMapper.getAclIdByRoleIdList(Lists.newArrayList(roleId));
        if (originAclIdList.size() == aclIds.size()){
            Set<Integer> originAclIdSet = Sets.newHashSet(originAclIdList);
            Set<Integer> aclIdSet = Sets.newHashSet(aclIds);
            originAclIdSet.removeAll(aclIdSet);
            if (CollectionUtils.isEmpty(originAclIdSet)){
                return;
            }
        }
        updateRoleAcls(roleId,aclIds);
    }

    @Transactional(rollbackFor = GlobalException.class)
    public void updateRoleAcls(Integer roleId,List<Integer> aclIds){
        roleAclMapper.deleteByRoleId(roleId);

        if (CollectionUtils.isEmpty(aclIds)){
            return;
        }
        List<SysRoleAcl> roleAclList = Lists.newArrayList();
        for (Integer aclId:aclIds){
            SysRoleAcl roleAcl = SysRoleAcl.builder().aclId(aclId).roleId(roleId).operator(RequestHolder.getCurrentUser().getUsername()).operateIp(IpUtil.getUserIP(RequestHolder.getCurrentRequest())).operateTime(new Date()).build();
            roleAclList.add(roleAcl);
        }
        roleAclMapper.batchInsertSysRoleAcl(roleAclList);
    }
}
