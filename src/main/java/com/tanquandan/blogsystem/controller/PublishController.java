package com.tanquandan.blogsystem.controller;

import com.tanquandan.blogsystem.DAO.Question;
import com.tanquandan.blogsystem.DAO.User;
import com.tanquandan.blogsystem.mapper.QuestionMapper;
import com.tanquandan.blogsystem.mapper.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PublishController {

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    UserMapper userMapper;

    @GetMapping("/publish")
    public String toPublish(){
        return "publish";
    }

    @PostMapping("/doPublish")
    public String doPublish(Model model,
                            @RequestParam("question_title")String title,
                            @RequestParam("question_description")String description,
                            @RequestParam("question_tag")String tag,
                            HttpSession session,
                            HttpServletRequest request){

        model.addAttribute("question_title",title);
        model.addAttribute("question_description",description);
        model.addAttribute("question_tag",tag);
        // (本来是前端的任务...)先检验数据，如果数据不合法，直接往Model里setError
        if(title == null || title.equals("")){
            model.addAttribute("error","标题信息不能为空");
            return "/publish";
        }
        if(description == null || description.equals("")){
            model.addAttribute("error","问题描述不能为空");
            return "/publish";
        }
        if(tag == null || tag.equals("")){
            model.addAttribute("error","标签信息不能为空");
            return "/publish";
        }

        // 插入数据
        Question q = new Question();
        q.setTitle(title);
        q.setDescription(description);
        q.setTag(tag);
        q.setGmtCreate(System.currentTimeMillis());
        q.setGmtModified(q.getGmtCreate());
        q.setCreator(((User)session.getAttribute("CurrentUser")).getAccountId());
        q.setViewCount(0);
        q.setCommentCount(0);
        q.setLikeCount(0);
        int insert = questionMapper.insert(q);
        return "redirect:/";
    }


}
