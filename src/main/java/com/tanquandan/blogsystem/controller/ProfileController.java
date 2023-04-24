package com.tanquandan.blogsystem.controller;

import com.tanquandan.blogsystem.DAO.User;
import com.tanquandan.blogsystem.DTO.PaginationDTO;
import com.tanquandan.blogsystem.Service.QuestionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProfileController {

    @Autowired
    QuestionService questionService;



    @GetMapping("/profile/{action}")
    public String toProfile(@PathVariable(name="action") String action,
            Model model,
                            @RequestParam(name="startNumber",defaultValue= "1")int offset,
                            @RequestParam(name="size",defaultValue = "5")int size,
                            HttpServletRequest request){
        if(action.equals("questions")){
            model.addAttribute("section",action);
            model.addAttribute("sectionName","我的问题");
        }
        User currentUser = (User)request.getSession().getAttribute("CurrentUser");
        PaginationDTO paginationList = questionService.listQuestionsByUser(currentUser.getAccount_id(),offset,size);
        model.addAttribute("PaginationList",paginationList);

        return "profile";
    }
}
