package com.kuzan.permission.util;

import com.google.common.base.Splitter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : sui
 * @time : 2019/3/25
 */
public class StringUtil {
    public static List<Integer> splitToListInt(String str,String separator){
        List<String> list = Splitter.on(separator).trimResults().omitEmptyStrings().splitToList(str);
        return list.stream().map(strItem -> Integer.parseInt(strItem)).collect(Collectors.toList());
    }
}
