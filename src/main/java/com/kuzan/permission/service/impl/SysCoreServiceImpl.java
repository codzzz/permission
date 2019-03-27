package com.kuzan.permission.service.impl;

import com.google.common.collect.Lists;
import com.kuzan.permission.commons.RequestHolder;
import com.kuzan.permission.dao.SysAclMapper;
import com.kuzan.permission.dao.SysRoleAclMapper;
import com.kuzan.permission.dao.SysRoleUserMapper;
import com.kuzan.permission.entity.SysAcl;
import com.kuzan.permission.entity.SysUser;
import com.kuzan.permission.service.SysCoreService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by sui on 2019/3/25.
 */
@Service
public class SysCoreServiceImpl implements SysCoreService{

    @Autowired
    private SysRoleUserMapper roleUserMapper;
    @Autowired
    private SysRoleAclMapper roleAclMapper;
    @Autowired
    private SysAclMapper aclMapper;

    @Override
    public List<SysAcl> getCurrentUserAclList() {
        Integer userId = RequestHolder.getCurrentUser().getId();

        return getUserAclList(userId);
    }

    @Override
    public List<SysAcl> getRoleAclList(Integer roleId) {
        List<Integer> aclIdList = roleAclMapper.getAclIdByRoleIdList(Lists.newArrayList(roleId));
        if (CollectionUtils.isEmpty(aclIdList)) {
            return Lists.newArrayList();
        }
        return aclMapper.listByListId(aclIdList);
    }

    @Override
    public boolean hasUrlAcl(String url) {
        if (isSuperAdmin()){
            return true;
        }
        List<SysAcl> acls = aclMapper.listByUrl(url);
        //访问的url没有被权限管理，可以随意访问
        if (CollectionUtils.isEmpty(acls)){
            return true;
        }
        List<SysAcl> aclList = getCurrentUserAclList();
        Set<Integer> aclIdSet = aclList.stream().map(sysAcl -> sysAcl.getId()).collect(Collectors.toSet());
        boolean hasValidAcl = false;
        //只要有一个权限点有权限，就认为是有权限访问的
        for (SysAcl acl:acls){
            if (acl == null || acl.getStatus() != 1){
                continue;
            }
            hasValidAcl = true;
            if (aclIdSet.contains(acl.getId())){
                return true;
            }
        }
        if (!hasValidAcl){
            return true;
        }
        return false;
    }

    private List<SysAcl> getUserAclList(Integer userId){
        if (isSuperAdmin()){
            return aclMapper.listAll();
        }
        List<Integer> roleIds = roleUserMapper.getRoleIdListByUserId(userId);
        if (CollectionUtils.isEmpty(roleIds)){
            return Lists.newArrayList();
        }
        List<Integer> aclIds = roleAclMapper.getAclIdByRoleIdList(roleIds);
        if (CollectionUtils.isEmpty(aclIds)){
            return Lists.newArrayList();
        }
        return aclMapper.listByListId(aclIds);
    }

    private boolean isSuperAdmin(){
        SysUser user = RequestHolder.getCurrentUser();
        if (user.getUsername().equals("Admin")){
            return true;
        }
        return false;
    }
}
