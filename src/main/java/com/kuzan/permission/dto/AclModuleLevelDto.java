package com.kuzan.permission.dto;

import com.google.common.collect.Lists;
import com.kuzan.permission.entity.SysAclModule;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * Created by sui on 2019/3/23.
 */
@Getter
@Setter
@ToString
public class AclModuleLevelDto extends SysAclModule{
    private List<AclModuleLevelDto> aclModuleList = Lists.newArrayList();

    private List<AclDto> aclList = Lists.newArrayList();

    public static AclModuleLevelDto adapt(SysAclModule aclModule){
        AclModuleLevelDto aclModelLevelDto = new AclModuleLevelDto();
        BeanUtils.copyProperties(aclModule,aclModelLevelDto);
        return aclModelLevelDto;
    }
}
