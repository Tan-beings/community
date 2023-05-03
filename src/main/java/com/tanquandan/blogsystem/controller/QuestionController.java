package com.tanquandan.blogsystem.controller;

import com.tanquandan.blogsystem.DAO.User;
import com.tanquandan.blogsystem.DTO.QuestionDTO;
import com.tanquandan.blogsystem.Mapper.QuestionMapper;
import com.tanquandan.blogsystem.Service.QuestionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class QuestionController {
    @Autowired
    QuestionService questionService;

    @Autowired
    QuestionMapper questionMapper;

    @GetMapping("/question/{id}")
    public String toQuestion(@PathVariable(name="id")int id,
                             Model model){

        QuestionDTO question = questionService.queryQuestionById(id);
        model.addAttribute("question",question);
        return "question";
    }

    @GetMapping("/toEdit/{id}")
    public String toEdit(@PathVariable(name="id")int id,
                         Model model){
        QuestionDTO question = questionService.queryQuestionById(id);
        model.addAttribute("question_id",id);
        model.addAttribute("question_title",question.getTitle());
        model.addAttribute("question_description",question.getDescription());
        model.addAttribute("question_tag",question.getTag());
        return "edit";
    }

    @PostMapping("/doEdit/{id}")
    public String doEdit(@PathVariable(name="id")int id,
                         @RequestParam("question_title")String title,
                         @RequestParam("question_description")String description,
                         @RequestParam("question_tag")String tag,
                         Model model){

        int res = questionMapper.updateQuestionById(title, description, tag, id);
        System.out.println(id);

        if(res != 1){
            model.addAttribute("error","修改失败：出现了未知异常");
            return "/toEdit/"+id;
        }

        return "redirect:/";

    }
}
