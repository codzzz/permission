package com.kuzan.permission.controller;

import com.kuzan.permission.commons.BaseResult;
import com.kuzan.permission.service.SysAclModuleService;
import com.kuzan.permission.service.SysTreeService;
import com.kuzan.permission.vo.AclModuleVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by sui on 2019/3/23.
 */
@Controller
@RequestMapping("/sys/aclModule")
@Slf4j
public class SysAclModuleController {
    @Autowired
    private SysAclModuleService aclModuleService;
    @Autowired
    private SysTreeService treeService;
    @RequestMapping("view")
    public String view(){
        return "acl";
    }

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @ResponseBody
    public BaseResult saveAclModule(AclModuleVo aclModuleVo){
        aclModuleService.save(aclModuleVo);
        return BaseResult.success();
    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public BaseResult updateDept(AclModuleVo aclModuleVo){
        aclModuleService.update(aclModuleVo);
        return BaseResult.success();
    }

    @RequestMapping("/tree")
    @ResponseBody
    public BaseResult tree(){
        return BaseResult.success(treeService.aclModuleTree());
    }

    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
    public BaseResult delete(@RequestParam("id") Integer aclModuleId){
        aclModuleService.delete(aclModuleId);
        return BaseResult.success();
    }
}
