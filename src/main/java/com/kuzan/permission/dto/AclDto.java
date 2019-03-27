package com.kuzan.permission.dto;

import com.kuzan.permission.entity.SysAcl;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * Created by sui on 2019/3/25.
 */
@Data
public class AclDto extends SysAcl{

    //是否要默认选中
    private boolean check = false;

    //是否有权限操作
    private boolean hasAcl = false;

    public static AclDto adapt(SysAcl acl){
        AclDto dto = new AclDto();
        BeanUtils.copyProperties(acl,dto);
        return dto;
    }
}
