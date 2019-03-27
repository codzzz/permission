package com.kuzan.permission.service.impl;

import com.google.common.base.Preconditions;
import com.kuzan.permission.commons.BeanValidator;
import com.kuzan.permission.commons.RequestHolder;
import com.kuzan.permission.dao.SysAclMapper;
import com.kuzan.permission.dao.SysAclModuleMapper;
import com.kuzan.permission.entity.SysAclModule;
import com.kuzan.permission.exception.GlobalException;
import com.kuzan.permission.exception.ParamException;
import com.kuzan.permission.service.SysAclModuleService;
import com.kuzan.permission.util.IpUtil;
import com.kuzan.permission.util.LevelUtil;
import com.kuzan.permission.vo.AclModuleVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by sui on 2019/3/23.
 */
@Service
public class SysAclModuleServiceImpl implements SysAclModuleService {
    @Autowired
    private SysAclModuleMapper aclModuleMapper;
    @Autowired
    private SysAclMapper aclMapper;
    @Override
    public void save(AclModuleVo aclModuleVo) {
        BeanValidator.check(aclModuleVo);
        if (checkIsExists(aclModuleVo.getParentId(),aclModuleVo.getName(),aclModuleVo.getId())){
            throw new ParamException("同一层级下不能存在相同名称的权限模块");
        }
        SysAclModule aclModule = SysAclModule.builder().name(aclModuleVo.getName()).parentId(aclModuleVo.getParentId())
                .seq(aclModuleVo.getSeq()).status(aclModuleVo.getStatus()).remark(aclModuleVo.getRemark()).build();
        aclModule.setLevel(LevelUtil.calLevel(getLevel(aclModuleVo.getParentId()),aclModuleVo.getParentId()));
        aclModule.setOperator(RequestHolder.getCurrentUser().getUsername());
        aclModule.setOperateTime(new Date());
        aclModule.setOperateIp(IpUtil.getUserIP(RequestHolder.getCurrentRequest()));

        aclModuleMapper.insertSelective(aclModule);
    }

    @Override
    public void update(AclModuleVo aclModuleVo) {
        BeanValidator.check(aclModuleVo);
        if (checkIsExists(aclModuleVo.getParentId(),aclModuleVo.getName(),aclModuleVo.getId())){
            throw new ParamException("同一层级下不能存在相同名称的权限模块");
        }
        SysAclModule before = aclModuleMapper.selectByPrimaryKey(aclModuleVo.getId());
        if (before == null){
            throw new ParamException("更新的权限模块不存在");
        }
        SysAclModule after = SysAclModule.builder().id(aclModuleVo.getId()).name(aclModuleVo.getName()).parentId(aclModuleVo.getParentId())
                .seq(aclModuleVo.getSeq()).status(aclModuleVo.getStatus()).remark(aclModuleVo.getRemark()).level(LevelUtil.calLevel(getLevel(aclModuleVo.getParentId()),aclModuleVo.getParentId())).build();
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateTime(new Date());
        after.setOperateIp(IpUtil.getUserIP(RequestHolder.getCurrentRequest()));

        updateWithChild(before,after);
    }

    @Override
    public void delete(Integer aclModuleId) {
        SysAclModule aclModule = aclModuleMapper.selectByPrimaryKey(aclModuleId);
        Preconditions.checkNotNull(aclModule,"要删除的权限模块不存在");
        if (aclModuleMapper.countByParentid(aclModuleId)>0){
            throw new ParamException("权限模块下有子模块，无法删除");
        }
        if (aclMapper.countByAclModuleId(aclModuleId)>0){
            throw new ParamException("权限模块下面有权限点，无法删除");
        }
        aclModuleMapper.deleteByPrimaryKey(aclModuleId);
    }

    @Transactional(rollbackFor = GlobalException.class)
    public void updateWithChild(SysAclModule before,SysAclModule after){
        if (!after.getLevel().equals(before.getLevel())){
            List<SysAclModule> aclModuleList = aclModuleMapper.listChildAclModuleByLevel(before.getLevel());
            if (CollectionUtils.isNotEmpty(aclModuleList)){
                for (SysAclModule aclModule:aclModuleList){
                    String level = aclModule.getLevel();
                    if (level.indexOf(before.getLevel()) ==0){
                        aclModule.setLevel(StringUtils.join(after.getLevel(),aclModule.getLevel().substring(before.getLevel().length())));
                    }
                }
                aclModuleMapper.batchUpdataAclModule(aclModuleList);
            }
        }
        aclModuleMapper.updateByPrimaryKey(after);

    }

    private boolean checkIsExists(Integer parentId,String aclModuleName,Integer aclModuleId){
        int count = aclModuleMapper.countByNameAndParentId(parentId, aclModuleName, aclModuleId);
        return count > 0;
    }
    private String getLevel(Integer aclModuleId){
        SysAclModule aclModule = aclModuleMapper.selectByPrimaryKey(aclModuleId);
        if (aclModule == null){
            return null;
        }
        return aclModule.getLevel();
    }
}
