package com.kuzan.permission.commons;

import com.kuzan.permission.exception.GlobalException;
import com.kuzan.permission.exception.ParamException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by sui on 2019/3/20.
 */
@Slf4j
public class GlobalExceptionResolver implements HandlerExceptionResolver{
    private final String CONTENTTYPE_PAGE = "text/html";
    private final String CONTENTTYPE_JSON = "application/json";
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String url = request.getRequestURL().toString();
        //判断是json请求异常还是jsp页面请求异常
        String requestAccept = request.getHeader("accept");
        String contentType = CONTENTTYPE_PAGE;
        if (StringUtils.isNotEmpty(requestAccept)){
            if (StringUtils.contains(requestAccept,"application/json")||StringUtils.contains(requestAccept,"test/javascript")||StringUtils.contains(requestAccept,"text/json")){
                contentType = CONTENTTYPE_JSON;
            }
        }
        final String defaultErrMsg = "System Error";
        ModelAndView mv = null;
        BaseResult result = null;
        //返回错误的jsp页面
        if (StringUtils.equals(contentType,CONTENTTYPE_PAGE)){
            log.error("unknown page exception,url:{}",url,ex);
            result = BaseResult.fail(defaultErrMsg);
            mv = new ModelAndView("exception",result.toMap());
        }
        //返回json格式的数据
        else{
            log.error("unknown json exception,url:{}",url,ex);
            //如果是自定义的异常
            if (ex instanceof GlobalException || ex instanceof ParamException){
                result = BaseResult.fail(ex.getMessage());
            }
            //抛出非自定义异常
            else{
                result = BaseResult.fail(defaultErrMsg);
            }
            mv = new ModelAndView("jsonView",result.toMap());
        }
        return mv;
    }
}
