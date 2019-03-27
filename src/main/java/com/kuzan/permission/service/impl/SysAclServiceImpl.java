package com.kuzan.permission.service.impl;

import com.google.common.base.Preconditions;
import com.kuzan.permission.commons.BeanValidator;
import com.kuzan.permission.commons.RequestHolder;
import com.kuzan.permission.dao.SysAclMapper;
import com.kuzan.permission.entity.SysAcl;
import com.kuzan.permission.exception.ParamException;
import com.kuzan.permission.service.SysAclService;
import com.kuzan.permission.util.IpUtil;
import com.kuzan.permission.vo.AclVo;
import com.kuzan.permission.vo.PageQuery;
import com.kuzan.permission.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by sui on 2019/3/23.
 */
@Service
public class SysAclServiceImpl implements SysAclService{
    @Autowired
    private SysAclMapper aclMapper;

    @Override
    public void save(AclVo aclVo) {
        BeanValidator.check(aclVo);
        if (checkIsExists(aclVo.getAclModuleId(), aclVo.getName(), aclVo.getId())){
            throw new ParamException("同一权限模块下不能存在相同名称的权限点");
        }
        SysAcl acl = SysAcl.builder().name(aclVo.getName()).aclModuleId(aclVo.getAclModuleId()).url(aclVo.getUrl()).type(aclVo.getType()).status(aclVo.getStatus())
                .remark(aclVo.getRemark()).seq(aclVo.getSeq()).build();
        acl.setCode(generateCode());
        acl.setOperator(RequestHolder.getCurrentUser().getUsername());
        acl.setOperateIp(IpUtil.getUserIP(RequestHolder.getCurrentRequest()));
        acl.setOperateTime(new Date());

        aclMapper.insertSelective(acl);
    }

    @Override
    public void update(AclVo aclVo) {
        BeanValidator.check(aclVo);
        if (checkIsExists(aclVo.getAclModuleId(), aclVo.getName(), aclVo.getId())){
            throw new ParamException("同一权限模块下不能存在相同名称的权限点");
        }
        SysAcl before = aclMapper.selectByPrimaryKey(aclVo.getId());
        Preconditions.checkNotNull(before);
        SysAcl acl = SysAcl.builder().id(aclVo.getId()).name(aclVo.getName()).aclModuleId(aclVo.getAclModuleId()).url(aclVo.getUrl()).type(aclVo.getType()).status(aclVo.getStatus())
                .remark(aclVo.getRemark()).seq(aclVo.getSeq()).build();
        acl.setOperator(RequestHolder.getCurrentUser().getUsername());
        acl.setOperateIp(IpUtil.getUserIP(RequestHolder.getCurrentRequest()));
        acl.setOperateTime(new Date());
        aclMapper.updateByPrimaryKeySelective(acl);
    }

    @Override
    public PageResult<SysAcl> getPageByAclModuleId(Integer aclModuleId, PageQuery page) {
        BeanValidator.check(page);
        int total = aclMapper.countByAclModuleId(aclModuleId);
        if (total>0){
            List<SysAcl> aclList = aclMapper.getPageByAclModuleId(aclModuleId, page);
            PageResult<SysAcl> pageResult = PageResult.<SysAcl>builder().total(total).data(aclList).build();
            return pageResult;
        }
        return PageResult.<SysAcl>builder().build();
    }

    private boolean checkIsExists(Integer aclModuleId,String aclName,Integer aclId){
        int count = aclMapper.countByNameAndAclModuleId(aclModuleId, aclName, aclId);
        return count > 0;
    }

    private String generateCode(){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        return format.format(new Date()) + "_" + (int)Math.random()*100;
    }
}
