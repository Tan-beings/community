package com.tanquandan.blogsystem.controller;

import com.tanquandan.blogsystem.DAO.Comment;
import com.tanquandan.blogsystem.DAO.User;
import com.tanquandan.blogsystem.DTO.CommentDTO;
import com.tanquandan.blogsystem.DTO.CommentTransmissionDTO;
import com.tanquandan.blogsystem.DTO.RequestDTO;
import com.tanquandan.blogsystem.Exception.CustomizedErrorCode;
import com.tanquandan.blogsystem.Service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

        Comment comment = new Comment();
        BeanUtils.copyProperties(commentTransmissionDTO,comment);
        comment.setGmtCreatetime(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreatetime());
        comment.setLikeCount(0L);
        comment.setCommentator(currentUser.getAccountId());

        commentService.insertComment(comment,currentUser.getAccountId());
        return RequestDTO.okOf();
    }

    @ResponseBody
    @RequestMapping(value="/getSubComments/{id}",method = RequestMethod.GET)
    public RequestDTO<List<CommentDTO>> getSubComments(@PathVariable(name="id") int parentId){
        List<CommentDTO> subComments = commentService.querySubComments(parentId);
        RequestDTO<List<CommentDTO>> requestDTO = new RequestDTO<>(200,subComments);
        return requestDTO;
    }


}
