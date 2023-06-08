package com.tanquandan.blogsystem.controller;

import com.tanquandan.blogsystem.DAO.User;
import com.tanquandan.blogsystem.Service.NotificationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    @GetMapping("/notification/{id}")
    public String profile(@PathVariable(name="id")String notificationId,
                          HttpServletRequest httpServletRequest){
        User user = (User)httpServletRequest.getSession().getAttribute("CurrentUser");
        Long outerId = notificationService.read(Long.valueOf(notificationId), user);
        if(outerId != null){
            return "redirect:/question/"+outerId;
        }else{
            return "redirect:/";
        }
    }
}
