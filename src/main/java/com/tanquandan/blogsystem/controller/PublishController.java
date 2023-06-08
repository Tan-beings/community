package com.tanquandan.blogsystem.controller;

import com.tanquandan.blogsystem.DAO.Question;
import com.tanquandan.blogsystem.DAO.User;
import com.tanquandan.blogsystem.Service.QuestionService;
import com.tanquandan.blogsystem.Service.TagService;
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
    QuestionService questionService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    TagService tagService;

    @GetMapping("/publish")
    public String toPublish(Model model){
        model.addAttribute("tags",tagService.queryAllTags());
        return "publish";
    }

    @PostMapping("/doPublish")
    public String doPublish(Model model,
                            @RequestParam("question_title")String title,
                            @RequestParam("question_description")String description,
                            @RequestParam("question_tag")String tags,
                            HttpSession session,
                            HttpServletRequest request){

        model.addAttribute("question_title",title);
        model.addAttribute("question_description",description);
        model.addAttribute("question_tag",tags);
        // (本来是前端的任务...)先检验数据，如果数据不合法，直接往Model里setError
        if(title == null || title.equals("")){
            model.addAttribute("error","标题信息不能为空");
            return "/publish";
        }
        if(description == null || description.equals("")){
            model.addAttribute("error","问题描述不能为空");
            return "/publish";
        }
        if(tags == null || tags.equals("")){
            model.addAttribute("error","标签信息不能为空");
            return "/publish";
        }

        // 插入数据
        Question q = new Question();
        q.setTitle(title);
        q.setDescription(description);
        q.setCreator(((User)session.getAttribute("CurrentUser")).getAccountId());
        questionService.insertAQuestion(q,tags);

        // 插入问题-标签-关联表

        return "redirect:/";
    }


}
