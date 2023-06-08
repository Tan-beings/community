package com.tanquandan.blogsystem.controller;

import ch.qos.logback.classic.pattern.NopThrowableInformationConverter;
import com.tanquandan.blogsystem.DAO.User;
import com.tanquandan.blogsystem.DTO.NotificationDTO;
import com.tanquandan.blogsystem.DTO.PaginationDTO;
import com.tanquandan.blogsystem.DTO.QuestionDTO;
import com.tanquandan.blogsystem.Service.NotificationService;
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
    @Autowired
    NotificationService notificationService;



    @GetMapping("/profile/{action}")
    public String toProfile(@PathVariable(name="action") String action,
                            Model model,
                            @RequestParam(name="startNumber",defaultValue= "1")int offset,
                            @RequestParam(name="size",defaultValue = "5")int size,
                            HttpServletRequest request){
        User currentUser = (User)request.getSession().getAttribute("CurrentUser");
        if(action.equals("questions")){
            model.addAttribute("section",action);
            model.addAttribute("sectionName","我的问题");
            PaginationDTO<QuestionDTO> paginationList = questionService.listQuestionsByUser(currentUser.getAccountId(),offset,size);
            model.addAttribute("PaginationList",paginationList);
        }

        if(action.equals("notifications")){
            model.addAttribute("section",action);
            model.addAttribute("sectionName","消息通知");
            PaginationDTO<NotificationDTO> paginationList = notificationService.listNotificationsByUser(currentUser.getAccountId(),offset,size);
            model.addAttribute("PaginationList",paginationList);
        }



        return "profile";
    }
}
