package com.kuzan.permission.controller;

import com.kuzan.permission.commons.BaseResult;
import com.kuzan.permission.commons.BeanValidator;
import com.kuzan.permission.exception.GlobalException;
import com.kuzan.permission.exception.ParamException;
import com.kuzan.permission.vo.TestVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by sui on 2019/3/20.
 */
@Controller
@RequestMapping("/test")
@Slf4j
public class TestController {

    @RequestMapping("/hello")
    public String hello(){
        throw new GlobalException("hello world ecception");
        //return "hello";
    }

    @RequestMapping(value = "/json",method = RequestMethod.GET)
    @ResponseBody
    public BaseResult json(){
        log.info("hello world");
        //throw new GlobalException("hello world ecception");
        return BaseResult.success("hello world");
    }

    @RequestMapping(value = "/validate",method = RequestMethod.GET)
    @ResponseBody
    public BaseResult validate(TestVo vo){
        Map<String, String> validate = BeanValidator.validate(vo);
        if (MapUtils.isNotEmpty(validate)){
            throw new ParamException(validate.toString());
        }
        return BaseResult.success("hello world");
    }
}
