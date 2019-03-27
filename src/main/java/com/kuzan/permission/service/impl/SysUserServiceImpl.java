package com.kuzan.permission.service.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.kuzan.permission.beans.Mail;
import com.kuzan.permission.commons.BeanValidator;
import com.kuzan.permission.commons.RequestHolder;
import com.kuzan.permission.dao.SysAclMapper;
import com.kuzan.permission.dao.SysDeptMapper;
import com.kuzan.permission.dao.SysUserMapper;
import com.kuzan.permission.entity.SysAcl;
import com.kuzan.permission.entity.SysUser;
import com.kuzan.permission.exception.ParamException;
import com.kuzan.permission.service.SysUserService;
import com.kuzan.permission.util.IpUtil;
import com.kuzan.permission.util.MD5Util;
import com.kuzan.permission.util.MailUtil;
import com.kuzan.permission.vo.PageQuery;
import com.kuzan.permission.vo.PageResult;
import com.kuzan.permission.vo.UserVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by sui on 2019/3/21.
 */
@Service
public class SysUserServiceImpl implements SysUserService{
    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private SysDeptMapper deptMapper;
    @Autowired
    private SysAclMapper aclMapper;
    @Override
    public void save(UserVo userVo) {
        BeanValidator.check(userVo);
        if (checkEmailExist(userVo.getMail(),userVo.getId())){
            throw new ParamException("邮箱已存在");
        }
        if (checkPhoneExist(userVo.getTelephone(),userVo.getId())){
            throw new ParamException("手机号已存在");
        }
        String password = "123456";
        String md5Password = MD5Util.encrypt(password);
        SysUser user = SysUser.builder().username(userVo.getUsername()).telephone(userVo.getTelephone()).mail(userVo.getMail())
                .password(md5Password).deptId(userVo.getDeptId()).status(userVo.getStatus()).remark(userVo.getRemark()).build();
        user.setOperator(RequestHolder.getCurrentUser().getUsername());
        user.setOperateTime(new Date());
        user.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));

        // send email
        Mail mail = Mail.builder().subject("账号注册成功").message("<html><body><p>用户："+user.getUsername()+"注册成功，<a href='http://localhost:8080/user/signin'>你可以点击链接跳转登录</a></p></body></html>")
                .receivers(Sets.newHashSet(user.getMail())).build();
        MailUtil.send(mail);

        userMapper.insertSelective(user);

    }

    @Override
    public void update(UserVo userVo) {
        BeanValidator.check(userVo);
        if (checkEmailExist(userVo.getMail(),userVo.getId())){
            throw new ParamException("邮箱已存在");
        }
        if (checkPhoneExist(userVo.getTelephone(),userVo.getId())){
            throw new ParamException("手机号已存在");
        }
        SysUser before = userMapper.selectByPrimaryKey(userVo.getId());
        Preconditions.checkNotNull(before,"要更新的用户不存在");
        SysUser after = SysUser.builder().id(userVo.getId()).username(userVo.getUsername()).telephone(userVo.getTelephone()).mail(userVo.getMail())
                .deptId(userVo.getDeptId()).status(userVo.getStatus()).remark(userVo.getRemark()).build();
        after.setOperateTime(new Date());
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        userMapper.updateByPrimaryKeySelective(after);
    }

    @Override
    public SysUser findUserByKeyword(String keyword) {
        return userMapper.findUserByKeyword(keyword);
    }

    @Override
    public PageResult<SysUser> getPageByDeptId(int deptId, PageQuery pageQuery) {
        BeanValidator.check(pageQuery);
//        int count = userMapper.countUserByDeptId(deptId);
//        if (count > 0){
//            List<SysUser> pageUser = userMapper.getPageByDeptId(deptId, pageQuery);
//            return PageResult.<SysUser>builder().total(count).data(pageUser).build();
//        }
        List<Integer> ids = Lists.newArrayList();
        ids.add(deptId);
        List<Integer> listId = getAllChildIdDept(deptId, ids);
        int count = userMapper.countUserByListId(listId);
        if (count > 0){
            List<SysUser> pageUser = userMapper.getPageByDeptListId(listId, pageQuery);
            return PageResult.<SysUser>builder().total(count).data(pageUser).build();
        }
        return PageResult.<SysUser>builder().build();
    }

    @Override
    public List<SysUser> getAll() {
        return userMapper.listAll();
    }

    @Override
    public List<SysAcl> getAclsById(Integer id) {
        SysUser user = userMapper.selectByPrimaryKey(id);
        Preconditions.checkNotNull(user,"查询的用户不存在");

        return null;
    }


    private boolean checkEmailExist(String email,Integer userId){
        return userMapper.countUserByEmail(email,userId)>0;
    }
    private boolean checkPhoneExist(String telephone,Integer userId){
        return userMapper.countUserByTelephone(telephone,userId)>0;
    }
    private List<Integer> getAllChildIdDept(Integer parentId,List<Integer> list){
        List<Integer> childIds = deptMapper.getChildIdByDeptId(parentId);
        if (CollectionUtils.isNotEmpty(childIds)){
            list.addAll(childIds);
            for (Integer childId:childIds){
                getAllChildIdDept(childId,list);
            }
        }
        return list;
    }
}
