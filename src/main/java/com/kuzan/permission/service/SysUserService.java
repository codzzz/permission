package com.kuzan.permission.service;

import com.kuzan.permission.entity.SysAcl;
import com.kuzan.permission.entity.SysUser;
import com.kuzan.permission.vo.PageQuery;
import com.kuzan.permission.vo.PageResult;
import com.kuzan.permission.vo.UserVo;

import java.util.List;

/**
 * Created by sui on 2019/3/21.
 */
public interface SysUserService {
    void save(UserVo userVo);
    void update(UserVo userVo);
    SysUser findUserByKeyword(String keyword);
    PageResult<SysUser> getPageByDeptId(int deptId, PageQuery pageQuery);
    List<SysUser> getAll();
    List<SysAcl> getAclsById(Integer id);
}
