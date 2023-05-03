package com.tanquandan.blogsystem.Interceptor;

import com.tanquandan.blogsystem.DAO.User;
import com.tanquandan.blogsystem.Mapper.UserMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class LocalInterceptor implements HandlerInterceptor {

    @Autowired
    UserMapper userMapper;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for (Cookie e : cookies) {
                if (e.getName().equals("token")) {
                    User current_user = userMapper.findByToken(e.getValue());
                    request.getSession().setAttribute("CurrentUser", current_user);
                    return true;
                }
            }
        }
        return true;

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
