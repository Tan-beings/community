package com.tanquandan.blogsystem.controller;

import com.tanquandan.blogsystem.DAO.User;
import com.tanquandan.blogsystem.Mapper.UserMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;

@Controller
public class IndexController {

    @Autowired
    UserMapper userMapper;

    @GetMapping("/")
    public String index(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        System.out.println("cookies: "+Arrays.toString(cookies));
        if(cookies != null) {
            for (Cookie e : cookies) {
                if (e.getName().equals("token")) {
                    User current_user = userMapper.findByToken(e.getValue());
                    request.getSession().setAttribute("CurrentUser", current_user);
                    System.out.println();
                }

            }
        }

        return "index";
    }
}
