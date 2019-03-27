package com.kuzan.permission.service.impl;

import com.google.common.collect.*;
import com.kuzan.permission.dao.SysAclMapper;
import com.kuzan.permission.dao.SysAclModuleMapper;
import com.kuzan.permission.dao.SysDeptMapper;
import com.kuzan.permission.dto.AclDto;
import com.kuzan.permission.dto.AclModuleLevelDto;
import com.kuzan.permission.dto.DeptLevelDto;
import com.kuzan.permission.entity.SysAcl;
import com.kuzan.permission.entity.SysAclModule;
import com.kuzan.permission.entity.SysDept;
import com.kuzan.permission.service.SysCoreService;
import com.kuzan.permission.service.SysTreeService;
import com.kuzan.permission.util.LevelUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by sui on 2019/3/21.
 */
@Service
public class SysTreeServiceImpl implements SysTreeService{
    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Autowired
    private SysAclModuleMapper aclModuleMapper;
    @Autowired
    private SysCoreService coreService;
    @Autowired
    private SysAclMapper aclMapper;
    @Override
    public List<DeptLevelDto> deptTree() {
        List<SysDept> deptList = sysDeptMapper.listAllDept();
        List<DeptLevelDto> dtoList = Lists.newArrayList();
        for (SysDept dept:deptList){
            DeptLevelDto dto = DeptLevelDto.adapt(dept);
            dtoList.add(dto);
        }

        return deptListToTree(dtoList);
    }

    @Override
    public List<AclModuleLevelDto> aclModuleTree() {
        List<SysAclModule> aclModuleList = aclModuleMapper.listAllAclModule();
        List<AclModuleLevelDto> dtoList = Lists.newArrayList();
        for (SysAclModule aclModule:aclModuleList){
            dtoList.add(AclModuleLevelDto.adapt(aclModule));
        }
        return aclModuleListToTree(dtoList);
    }

    @Override
    public List<AclModuleLevelDto> roleTree(Integer roleId) {
        //1.当前登录用户已分配的权限点
        List<SysAcl> userAcllist = coreService.getCurrentUserAclList();
        //2.当前角色分配的权限点
        List<SysAcl> roleAclList = coreService.getRoleAclList(roleId);

        Set<Integer> userAclIdSet = userAcllist.stream().map(sysAcl -> sysAcl.getId()).collect(Collectors.toSet());
        Set<Integer> roleAclIdSet = roleAclList.stream().map(sysAcl -> sysAcl.getId()).collect(Collectors.toSet());

        List<SysAcl> sysAcls = aclMapper.listAll();
        HashSet<SysAcl> aclSet = Sets.newHashSet(sysAcls);

        List<AclDto> aclDtoList = Lists.newArrayList();
        for (SysAcl acl:aclSet) {
            AclDto aclDto = AclDto.adapt(acl);
            if (userAclIdSet.contains(acl.getId())){
                aclDto.setHasAcl(true);
            }
            if (roleAclIdSet.contains(acl.getId())){
                aclDto.setCheck(true);
            }
            aclDtoList.add(aclDto);
        }
        return aclListToTree(aclDtoList);
    }

    private List<AclModuleLevelDto> aclListToTree(List<AclDto> aclDtoList){
        if (CollectionUtils.isEmpty(aclDtoList)){
            return Lists.newArrayList();
        }
        List<AclModuleLevelDto> aclModuleLevelList = aclModuleTree();
        Multimap<Integer,AclDto> moduleIdAclMap = ArrayListMultimap.create();
        for (AclDto aclDto:aclDtoList){
            moduleIdAclMap.put(aclDto.getAclModuleId(),aclDto);
        }
        bindAclsWithOrder(aclModuleLevelList,moduleIdAclMap);
        return aclModuleLevelList;
    }
    private void bindAclsWithOrder(List<AclModuleLevelDto> aclModuleLevelList,Multimap<Integer,AclDto> moduleIdAclMap){
        if (CollectionUtils.isEmpty(aclModuleLevelList)){
            return;
        }
        for (AclModuleLevelDto dto:aclModuleLevelList){
            List<AclDto> aclDtoList = (List<AclDto>) moduleIdAclMap.get(dto.getId());
            if (CollectionUtils.isNotEmpty(aclDtoList)){
                dto.setAclList(aclDtoList);
            }
            bindAclsWithOrder(dto.getAclModuleList(),moduleIdAclMap);
        }
    }

    private List<DeptLevelDto> deptListToTree(List<DeptLevelDto> dtoList){
        if (CollectionUtils.isEmpty(dtoList)){
            return Lists.newArrayList();
        }
        Multimap<String,DeptLevelDto> levelDeptMap = ArrayListMultimap.create();
        List<DeptLevelDto> rootList = Lists.newArrayList();
        for (DeptLevelDto dto:dtoList){
            levelDeptMap.put(dto.getLevel(),dto);
            if (LevelUtil.ROOT.equals(dto.getLevel())){
                rootList.add(dto);
            }
        }

        //按 seq排序
        Collections.sort(rootList,deptComparator);

        //递归
        transformDeptTree(rootList,LevelUtil.ROOT,levelDeptMap);

        return rootList;
    }
    private List<AclModuleLevelDto> aclModuleListToTree(List<AclModuleLevelDto> dtoList){
        if (CollectionUtils.isEmpty(dtoList)){
            return Lists.newArrayList();
        }
        Multimap<String,AclModuleLevelDto> levelAclModuleMap = ArrayListMultimap.create();
        List<AclModuleLevelDto> rootList = Lists.newArrayList();
        for (AclModuleLevelDto dto:dtoList){
            levelAclModuleMap.put(dto.getLevel(),dto);
            if (LevelUtil.ROOT.equals(dto.getLevel())){
                rootList.add(dto);
            }
        }

        //按 seq排序
        Collections.sort(rootList,aclModuleComparator);

        //递归
        transformAclModuleTree(rootList,LevelUtil.ROOT,levelAclModuleMap);

        return rootList;
    }

    //递归处理方法
    private void transformDeptTree(List<DeptLevelDto> dtoList,String level,Multimap<String,DeptLevelDto> multimap){
        for (DeptLevelDto deptDto: dtoList){
            //遍历该层的每个元素

            //处理当前层级的数据
            String nextLevel = LevelUtil.calLevel(level,deptDto.getId());
            //处理下一层
            List<DeptLevelDto> tempDeptList = (List<DeptLevelDto>) multimap.get(nextLevel);
            if (CollectionUtils.isNotEmpty(tempDeptList)){
                //排序
                Collections.sort(tempDeptList,deptComparator);
                //设置下一层部门
                deptDto.setDeptList(tempDeptList);
                //进入到下一层处理
                transformDeptTree(tempDeptList,nextLevel,multimap);
            }

        }

    }
    //递归处理方法
    private void transformAclModuleTree(List<AclModuleLevelDto> dtoList,String level,Multimap<String,AclModuleLevelDto> multimap){
        for (AclModuleLevelDto deptDto: dtoList){
            //遍历该层的每个元素

            //处理当前层级的数据
            String nextLevel = LevelUtil.calLevel(level,deptDto.getId());
            //处理下一层
            List<AclModuleLevelDto> tempAclModuleList = (List<AclModuleLevelDto>) multimap.get(nextLevel);
            if (CollectionUtils.isNotEmpty(tempAclModuleList)){
                //排序
                Collections.sort(tempAclModuleList,aclModuleComparator);
                //设置下一层部门
                deptDto.setAclModuleList(tempAclModuleList);
                //进入到下一层处理
                transformAclModuleTree(tempAclModuleList,nextLevel,multimap);
            }

        }

    }
    //定义一个比较器，按 seq 从小到大排序
    private final static Comparator deptComparator = new Comparator<DeptLevelDto>() {
        @Override
        public int compare(DeptLevelDto o1, DeptLevelDto o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };
    private final static Comparator aclModuleComparator = new Comparator<AclModuleLevelDto>() {
        @Override
        public int compare(AclModuleLevelDto o1, AclModuleLevelDto o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };
}
