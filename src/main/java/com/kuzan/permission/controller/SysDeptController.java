package com.kuzan.permission.controller;

import com.kuzan.permission.commons.BaseResult;
import com.kuzan.permission.dto.DeptLevelDto;
import com.kuzan.permission.service.SysDeptService;
import com.kuzan.permission.service.SysTreeService;
import com.kuzan.permission.vo.DeptVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by sui on 2019/3/21.
 */
@Controller
@RequestMapping("/sys/dept")
@Slf4j
public class SysDeptController {
    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private SysTreeService treeService;

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @ResponseBody
    public BaseResult saveDept(DeptVo deptVo){
        sysDeptService.save(deptVo);
        return BaseResult.success();
    }

    @RequestMapping(value = "/tree",method = RequestMethod.GET)
    @ResponseBody
    public BaseResult tree(){
        List<DeptLevelDto> dtoList = treeService.deptTree();
        return BaseResult.success(dtoList);
    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public BaseResult updateDept(DeptVo deptVo){
        sysDeptService.update(deptVo);
        return BaseResult.success();
    }

    @RequestMapping("/view")
    public String view(){
        return "deptView";
    }

    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
    public BaseResult delete(@RequestParam("id") Integer deptId){
        sysDeptService.delete(deptId);
        return BaseResult.success();
    }
}
