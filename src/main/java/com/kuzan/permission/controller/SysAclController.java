package com.kuzan.permission.controller;

import com.kuzan.permission.commons.BaseResult;
import com.kuzan.permission.entity.SysAcl;
import com.kuzan.permission.service.SysAclService;
import com.kuzan.permission.vo.AclVo;
import com.kuzan.permission.vo.PageQuery;
import com.kuzan.permission.vo.PageResult;
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
@RequestMapping("/sys/acl")
@Slf4j
public class SysAclController {
    @Autowired
    private SysAclService aclService;

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @ResponseBody
    public BaseResult saveAcl(AclVo aclVo){
        aclService.save(aclVo);
        return BaseResult.success();
    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public BaseResult updateAcl(AclVo aclVo){
        aclService.update(aclVo);
        return BaseResult.success();
    }

    @RequestMapping(value = "/view",method = RequestMethod.GET)
    @ResponseBody
    public BaseResult view(@RequestParam("aclModuleId") Integer aclModuleId, PageQuery pageQuery){
        PageResult<SysAcl> pageResult = aclService.getPageByAclModuleId(aclModuleId, pageQuery);
        return BaseResult.success(pageResult);
    }

}
