package com.kuzan.permission.service.impl;

import com.google.common.base.Preconditions;
import com.kuzan.permission.commons.BeanValidator;
import com.kuzan.permission.commons.RequestHolder;
import com.kuzan.permission.dao.SysDeptMapper;
import com.kuzan.permission.dao.SysUserMapper;
import com.kuzan.permission.entity.SysDept;
import com.kuzan.permission.exception.GlobalException;
import com.kuzan.permission.exception.ParamException;
import com.kuzan.permission.service.SysDeptService;
import com.kuzan.permission.util.IpUtil;
import com.kuzan.permission.util.LevelUtil;
import com.kuzan.permission.vo.DeptVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by sui on 2019/3/21.
 */
@Service
public class SysDeptServiceImpl implements SysDeptService{

    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Autowired
    private SysUserMapper userMapper;

    @Override
    public void save(DeptVo deptVo) {
        BeanValidator.check(deptVo);
        if (checkIsExists(deptVo.getParentId(),deptVo.getName(),deptVo.getId())){
            throw new ParamException("同一层级下不能存在相同名称的部门");
        }
        SysDept dept = SysDept.builder().name(deptVo.getName()).parentId(deptVo.getParentId()).seq(deptVo.getSeq()).remark(deptVo.getRemark()).build();
        dept.setLevel(LevelUtil.calLevel(getLevel(deptVo.getParentId()),deptVo.getParentId()));
        dept.setOperator(RequestHolder.getCurrentUser().getUsername());
        dept.setOperateTime(new Date());
        dept.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        int rowResult = sysDeptMapper.insertSelective(dept);
        if (rowResult <= 0){
            throw new GlobalException("系统错误，新增部门失败");
        }
    }

    @Override
    public void update(DeptVo deptVo) {
        BeanValidator.check(deptVo);
        if (checkIsExists(deptVo.getParentId(),deptVo.getName(),deptVo.getId())){
            throw new ParamException("同一层级下不能存在相同名称的部门");
        }
        SysDept before = sysDeptMapper.selectByPrimaryKey(deptVo.getId());
        Preconditions.checkNotNull(before,"待更新的部门不存在");

        SysDept after = SysDept.builder().id(deptVo.getId()).name(deptVo.getName()).parentId(deptVo.getParentId()).seq(deptVo.getSeq()).remark(deptVo.getRemark()).build();
        after.setLevel(LevelUtil.calLevel(getLevel(deptVo.getParentId()),deptVo.getParentId()));
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateTime(new Date());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));

        updateWithChild(before,after);
    }

    @Override
    public void delete(Integer id) {
        SysDept dept = sysDeptMapper.selectByPrimaryKey(id);
        Preconditions.checkNotNull(dept,"要删除的部门不存在");
        if (sysDeptMapper.countByParentId(id)>0){
            throw new ParamException("部门下面有子部门，无法删除");
        }
        if (userMapper.countUserByDeptId(id)>0){
            throw new ParamException("部门下面有用户，无法删除");
        }
        sysDeptMapper.deleteByPrimaryKey(id);

    }

    @Transactional(rollbackFor = GlobalException.class)
    public void updateWithChild(SysDept before,SysDept after){
        if (!after.getLevel().equals(before.getLevel())){
            List<SysDept> deptList = sysDeptMapper.listChildDeptByLevel(before.getLevel());
            if (CollectionUtils.isNotEmpty(deptList)){
                for (SysDept dept:deptList){
                    String level = dept.getLevel();
                    if (level.indexOf(before.getLevel()) ==0){
                        dept.setLevel(StringUtils.join(after.getLevel(),dept.getLevel().substring(before.getLevel().length())));
                    }
                }
                sysDeptMapper.batchUpdataDept(deptList);
            }
        }
        sysDeptMapper.updateByPrimaryKey(after);

    }

    private boolean checkIsExists(Integer parentId,String deptName,Integer deptId){
        // TODO: 2019/3/21
        int count = sysDeptMapper.countByNameAndParentId(parentId, deptName, deptId);
        return count > 0;
    }
    private String getLevel(Integer deptId){
        SysDept dept = sysDeptMapper.selectByPrimaryKey(deptId);
        if (dept == null){
            return null;
        }
        return dept.getLevel();
    }
}
