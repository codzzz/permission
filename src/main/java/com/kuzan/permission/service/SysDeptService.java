package com.kuzan.permission.service;

import com.kuzan.permission.vo.DeptVo;

/**
 * Created by sui on 2019/3/21.
 */
public interface SysDeptService {
    void save(DeptVo deptVo);
    void update(DeptVo deptVo);
    void delete(Integer id);
}
