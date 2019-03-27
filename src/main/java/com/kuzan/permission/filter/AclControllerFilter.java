package com.kuzan.permission.filter;

import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import com.kuzan.permission.commons.ApplicationContextHelper;
import com.kuzan.permission.commons.BaseResult;
import com.kuzan.permission.commons.RequestHolder;
import com.kuzan.permission.entity.SysUser;
import com.kuzan.permission.service.SysCoreService;
import com.kuzan.permission.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author : sui
 * @time : 2019/3/26
 */
@Slf4j
public class AclControllerFilter implements Filter{
    private static Set<String> exclusionUrlSet = Sets.newConcurrentHashSet();
    private final static String NOAUTHURL = "/sys/user/noAuth";
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String exclusionUrls = filterConfig.getInitParameter("exclusionUrls");
        List<String> list = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(exclusionUrls);
        exclusionUrlSet = Sets.newConcurrentHashSet(list);
        exclusionUrlSet.add(NOAUTHURL);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String servletPath = request.getServletPath();
        Map<String,String[]> map = request.getParameterMap();
        if (exclusionUrlSet.contains(servletPath)){
            filterChain.doFilter(request,response);
            return;
        }
        SysUser user = RequestHolder.getCurrentUser();
        if (null == user){
            log.info("someone visit {},but no login,parameter:{}",servletPath, JsonMapper.obj2String(map));
            noAuth(request,response);
            return;
        }
        SysCoreService sysCoreService = ApplicationContextHelper.popBean(SysCoreService.class);
        if (!sysCoreService.hasUrlAcl(servletPath)){
            log.info("{} visit {},but no login,paramter:{}",JsonMapper.obj2String(user),servletPath,JsonMapper.obj2String(map));
            noAuth(request,response);
            return;
        }
        filterChain.doFilter(request,response);
        return;
    }
    private void noAuth(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String accept = request.getHeader("accept");
        if (accept.contains("json")){
            BaseResult fail = BaseResult.fail("没有权限访问，如需访问请先联系管理员");
            response.setHeader("Content-Type","application/json");
            response.getWriter().print(JsonMapper.obj2String(fail));
        }else {
            clientRedirect(NOAUTHURL,response);
        }
    }
    private void clientRedirect(String url,HttpServletResponse response) throws IOException {
        response.setHeader("Content-Type","text/html");
        response.getWriter().print("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n"
                + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" + "<head>\n" + "<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\"/>\n"
                + "<title>跳转中...</title>\n" + "</head>\n" + "<body>\n" + "跳转中，请稍候...\n" + "<script type=\"text/javascript\">//<![CDATA[\n"
                + "window.location.href='" + url + "?ret='+encodeURIComponent(window.location.href);\n" + "//]]></script>\n" + "</body>\n" + "</html>\n");
    }
    @Override
    public void destroy() {

    }
}
