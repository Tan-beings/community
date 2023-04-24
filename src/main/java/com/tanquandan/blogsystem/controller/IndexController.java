package com.tanquandan.blogsystem.controller;

import com.tanquandan.blogsystem.DAO.User;
import com.tanquandan.blogsystem.DTO.PaginationDTO;
import com.tanquandan.blogsystem.Mapper.UserMapper;
import com.tanquandan.blogsystem.Service.QuestionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class IndexController {

    @Autowired
    UserMapper userMapper;

    @Autowired
    QuestionService questionService;

    @GetMapping("/")
    public String index(HttpSession session,
                        HttpServletRequest request, Model model,
                        @RequestParam(name="startNumber",defaultValue= "1")int offset,
                        @RequestParam(name="size",defaultValue = "5")int size){

        PaginationDTO PaginationList = questionService.listAllQuestions(offset, size);
        model.addAttribute("PaginationList",PaginationList);
        return "index";
    }
}
