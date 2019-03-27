package com.kuzan.permission.filter;

import com.kuzan.permission.commons.RequestHolder;
import com.kuzan.permission.entity.SysUser;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by sui on 2019/3/23.
 */
public class LoginFilter implements Filter{
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        SysUser user = (SysUser) request.getSession().getAttribute("user");
        if (user == null){
            response.sendRedirect("/user/signin");
            return;
        }
        RequestHolder.add(request);
        RequestHolder.add(user);
        filterChain.doFilter(request,response);
        return;
    }

    @Override
    public void destroy() {

    }
}
