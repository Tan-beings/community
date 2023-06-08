package com.tanquandan.blogsystem.controller;

import com.tanquandan.blogsystem.DTO.PaginationDTO;
import com.tanquandan.blogsystem.DTO.QuestionDTO;
import com.tanquandan.blogsystem.Service.QuestionService;
import com.tanquandan.blogsystem.mapper.UserMapper;
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
                        Model model,
                        @RequestParam(name="startNumber",defaultValue= "1")int offset,
                        @RequestParam(name="size",defaultValue = "5")int size){

        PaginationDTO<QuestionDTO> PaginationList = questionService.listAllQuestions(offset, size);
        model.addAttribute("PaginationList",PaginationList);
        return "index";
    }
}
