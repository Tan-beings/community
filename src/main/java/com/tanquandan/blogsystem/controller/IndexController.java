package com.tanquandan.blogsystem.controller;

import com.tanquandan.blogsystem.DAO.User;
import com.tanquandan.blogsystem.DTO.PaginationDTO;
import com.tanquandan.blogsystem.DTO.QuestionDTO;
import com.tanquandan.blogsystem.Mapper.UserMapper;
import com.tanquandan.blogsystem.Service.QuestionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    UserMapper userMapper;

    @Autowired
    QuestionService questionService;

    @GetMapping("/")
    public String index(HttpServletRequest request, Model model,
                        @RequestParam(name="startNumber",defaultValue= "1")int offset,
                        @RequestParam(name="size",defaultValue = "5")int size){
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for (Cookie e : cookies) {
                if (e.getName().equals("token")) {
                    User current_user = userMapper.findByToken(e.getValue());
                    request.getSession().setAttribute("CurrentUser", current_user);
                }
            }
        }

        PaginationDTO PaginationList = questionService.list(offset, size);
        model.addAttribute("PaginationList",PaginationList);
        return "index";
    }
}
