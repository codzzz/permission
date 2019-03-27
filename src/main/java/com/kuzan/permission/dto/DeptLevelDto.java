package com.kuzan.permission.dto;

import com.google.common.collect.Lists;
import com.kuzan.permission.entity.SysDept;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * Created by sui on 2019/3/21.
 */
@Getter
@Setter
@ToString
public class DeptLevelDto extends SysDept{
    private List<DeptLevelDto> deptList = Lists.newArrayList();

    public static DeptLevelDto adapt(SysDept dept){
        DeptLevelDto deptLevelDto = new DeptLevelDto();
        BeanUtils.copyProperties(dept,deptLevelDto);
        return deptLevelDto;
    }

}
