package com.kuzan.permission.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by sui on 2019/3/21.
 */
public class LevelUtil {

    private final static String SEPARATOR = ".";
    public final static String ROOT = "0";

    public static String calLevel(String parentLevel,Integer parentId){
        if (StringUtils.isBlank(parentLevel)){
            return ROOT;
        }
        return StringUtils.join(parentLevel,SEPARATOR,parentId);
    }
}
