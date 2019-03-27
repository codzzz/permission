package com.kuzan.permission.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kuzan.permission.commons.BaseResult;
import com.kuzan.permission.entity.SysUser;
import com.kuzan.permission.service.*;
import com.kuzan.permission.util.StringUtil;
import com.kuzan.permission.vo.RoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by sui on 2019/3/25.
 */
@Controller
@RequestMapping("/sys/role")
public class SysRoleController {
    @Autowired
    private SysRoleService roleService;
    @Autowired
    private SysTreeService treeService;
    @Autowired
    private SysRoleAclService roleAclService;
    @Autowired
    private SysRoleUserService roleUserService;
    @Autowired
    private SysUserService userService;
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @ResponseBody
    public BaseResult save(RoleVo roleVo){
        roleService.save(roleVo);
        return BaseResult.success();
    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public BaseResult update(RoleVo roleVo){
        roleService.update(roleVo);
        return BaseResult.success();
    }

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ResponseBody
    public BaseResult view(){
        return BaseResult.success(roleService.listAll());
    }

    @RequestMapping(value = "/view")
    public String page(){
        return "role";
    }

    @RequestMapping(value = "/roleTree",method = RequestMethod.POST)
    @ResponseBody
    public BaseResult tree(Integer roleId){
        return BaseResult.success(treeService.roleTree(roleId));
    }

    @RequestMapping(value = "/changeAcls",method = RequestMethod.POST)
    @ResponseBody
    public BaseResult changeAcls(@RequestParam("roleId") Integer roleId,@RequestParam(value = "aclIds",required = false,defaultValue = "") String aclIds){
        List<Integer> idList = StringUtil.splitToListInt(aclIds, ",");
        roleAclService.changeRoleAcls(roleId,idList);
        return BaseResult.success();
    }

    @RequestMapping(value = "/users",method = RequestMethod.POST)
    @ResponseBody
    public BaseResult users(Integer roleId){
        List<SysUser> selectedUserList = roleUserService.getUserByRoleId(roleId);
        List<SysUser> allUserList = userService.getAll();
        List<SysUser> unSelectedUserList = Lists.newArrayList();
        Set<Integer> selectedUserSet = selectedUserList.stream().map(sysUser -> sysUser.getId()).collect(Collectors.toSet());
        for (SysUser user:allUserList){
            if (user.getStatus() ==1 && !selectedUserSet.contains(user.getId())){
                unSelectedUserList.add(user);
            }
        }
        Map<String,List<SysUser>> map = Maps.newHashMap();
        map.put("selected",selectedUserList);
        map.put("unselected",unSelectedUserList);
        return BaseResult.success(map);
    }

    @RequestMapping(value = "/changeUsers",method = RequestMethod.POST)
    @ResponseBody
    public BaseResult changeUsers(@RequestParam("roleId") Integer roleId,@RequestParam(value = "userIds",required = false,defaultValue = "") String userIds){
        List<Integer> idList = StringUtil.splitToListInt(userIds, ",");
        roleUserService.changeRoleUsers(roleId,idList);
        return BaseResult.success();
    }
}
