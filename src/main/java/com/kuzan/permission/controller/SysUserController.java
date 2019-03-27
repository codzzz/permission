package com.kuzan.permission.controller;

import com.kuzan.permission.commons.BaseResult;
import com.kuzan.permission.entity.SysUser;
import com.kuzan.permission.service.SysUserService;
import com.kuzan.permission.vo.PageQuery;
import com.kuzan.permission.vo.PageResult;
import com.kuzan.permission.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by sui on 2019/3/21.
 */
@Controller
@RequestMapping("/sys/user")
public class SysUserController {
    @Autowired
    private SysUserService userService;

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @ResponseBody
    public BaseResult saveUser(UserVo userVo){
        userService.save(userVo);
        return BaseResult.success();
    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public BaseResult updateUser(UserVo userVo){
        userService.update(userVo);
        return BaseResult.success();
    }

    @RequestMapping(value = "/view",method = RequestMethod.GET)
    @ResponseBody
    public BaseResult viewUser(@RequestParam("deptId") Integer deptId, PageQuery pageQuery){
        PageResult<SysUser> result = userService.getPageByDeptId(deptId,pageQuery);
        return BaseResult.success(result);
    }

    @RequestMapping(value = "/acls",method = RequestMethod.POST)
    @ResponseBody
    public BaseResult acls(Integer userId){

        return BaseResult.success();
    }

    @RequestMapping("/noAuth")
    public String noAuth(){
        return "noAuth";
    }
}
