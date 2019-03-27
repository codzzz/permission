package com.kuzan.permission.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.kuzan.permission.commons.RequestHolder;
import com.kuzan.permission.dao.SysRoleUserMapper;
import com.kuzan.permission.dao.SysUserMapper;
import com.kuzan.permission.entity.SysRoleUser;
import com.kuzan.permission.entity.SysUser;
import com.kuzan.permission.exception.GlobalException;
import com.kuzan.permission.service.SysRoleUserService;
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
public class SysRoleUserServiceImpl implements SysRoleUserService{
    @Autowired
    private SysRoleUserMapper roleUserMapper;
    @Autowired
    private SysUserMapper userMapper;
    @Override
    public List<SysUser> getUserByRoleId(Integer roleId) {
        List<Integer> userIdList = roleUserMapper.getUserIdByRoleId(roleId);
        if (CollectionUtils.isEmpty(userIdList)){
            return Lists.newArrayList();
        }
        return userMapper.getUserListByIdlist(userIdList);
    }

    @Override
    public void changeRoleUsers(Integer roleId, List<Integer> userIds) {
        List<Integer> originUserIdList = roleUserMapper.getUserIdByRoleId(roleId);
        if (originUserIdList.size()== userIds.size()){
            Set<Integer> originUserIdSet = Sets.newHashSet(originUserIdList);
            Set<Integer> userIdSet = Sets.newHashSet(userIds);
            originUserIdSet.remove(userIdSet);
            if (CollectionUtils.isEmpty(originUserIdSet)){
                return;
            }
        }
        updateRoleUsers(roleId,userIds);

    }
    @Transactional(rollbackFor = GlobalException.class)
    public void updateRoleUsers(Integer roleId,List<Integer> userIdList){
        roleUserMapper.deleteByRoleId(roleId);
        if (CollectionUtils.isEmpty(userIdList)){
            return;
        }
        List<SysRoleUser> roleUserList = Lists.newArrayList();
        for (Integer userId:userIdList){
            SysRoleUser roleUser = SysRoleUser.builder().roleId(roleId).userId(userId).operator(RequestHolder.getCurrentUser().getUsername()).operateIp(IpUtil.getUserIP(RequestHolder.getCurrentRequest())).operateTime(new Date()).build();
            roleUserList.add(roleUser);
        }
        roleUserMapper.batchInsertSysRoleUser(roleUserList);

    }
}
