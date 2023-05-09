package com.tanquandan.blogsystem.controller;

import com.tanquandan.blogsystem.DAO.User;
import com.tanquandan.blogsystem.DTO.CommentTransmissionDTO;
import com.tanquandan.blogsystem.DTO.RequestDTO;
import com.tanquandan.blogsystem.Exception.CustomizedErrorCode;
import com.tanquandan.blogsystem.Service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CommentController {
    @Autowired
    CommentService commentService;


    @ResponseBody
    @RequestMapping(value = "/doComment",method = RequestMethod.POST)
    public RequestDTO doComment(@RequestBody CommentTransmissionDTO commentTransmissionDTO, HttpServletRequest request, HttpSession session){
        // 如果当前是未登录状态
        User currentUser = (User)request.getSession().getAttribute("CurrentUser");
        if(currentUser ==  null){
            return RequestDTO.errorOf(CustomizedErrorCode.NO_LOGIN);
        }
        commentService.insertComment(commentTransmissionDTO,currentUser.getAccountId());

        return RequestDTO.okOf();
    }
}
