package com.kuzan.permission.commons;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sui on 2019/3/20.
 */
@Data
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class BaseResult {
    private boolean ret;
    private String msg;
    private Object obj;

    private BaseResult(boolean ret){
        this.ret = ret;
    }
    public static BaseResult success(Object obj, String msg){
        BaseResult result = new BaseResult(true);
        result.msg = msg;
        result.obj = obj;
        return result;
    }
    public static BaseResult success(String msg){
        return success(null,msg);
    }
    public static BaseResult success(Object obj){
        return success(obj,null);
    }
    public static BaseResult success(){
        return success(null,null);
    }

    public static BaseResult fail(String msg){
        BaseResult result = new BaseResult(false);
        result.msg = msg;
        return result;
    }
    public static BaseResult fail(){
        return fail(null);
    }
    public Map<String,Object> toMap(){
        Map<String,Object> map = new HashMap();
        map.put("ret",this.ret);
        map.put("msg",this.msg);
        return map;
    }
}
