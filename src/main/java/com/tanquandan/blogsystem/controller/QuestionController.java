package com.tanquandan.blogsystem.controller;

import com.tanquandan.blogsystem.DAO.Question;
import com.tanquandan.blogsystem.DAO.Tag;
import com.tanquandan.blogsystem.DTO.CommentDTO;
import com.tanquandan.blogsystem.DTO.PaginationDTO;
import com.tanquandan.blogsystem.DTO.QuestionDTO;
import com.tanquandan.blogsystem.DTO.RequestDTO;
import com.tanquandan.blogsystem.Service.CommentService;
import com.tanquandan.blogsystem.Service.QuestionService;
import com.tanquandan.blogsystem.Service.TagService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class QuestionController {
    @Autowired
    QuestionService questionService;
    @Autowired
    CommentService commentService;
    @Autowired
    TagService tagService;


    @GetMapping("/question/{id}")
    public String toQuestion(@PathVariable(name="id")int id,
                             Model model){
        QuestionDTO question = questionService.queryQuestionById(id);
        List<CommentDTO> comments = commentService.queryCommentDTOsByQuestionId(id);
        model.addAttribute("comments",comments);
        model.addAttribute("question",question);
        questionService.updateViewCountById(id);

        return "question";
    }

    @GetMapping("/toEdit/{id}")
    public String toEdit(@PathVariable(name="id")int id,
                         Model model){
        QuestionDTO question = questionService.queryQuestionById(id);
        List<Integer> tagIds = tagService.queryTagsByQuestionId(id).stream().map(Tag::getId).collect(Collectors.toList());
        model.addAttribute("question_id",id);
        model.addAttribute("question_title",question.getTitle());
        model.addAttribute("question_description",question.getDescription());
        model.addAttribute("question_tag",tagIds);
        model.addAttribute("tags",tagService.queryAllTags());

        return "edit";
    }


    @PostMapping("/doEdit/{id}")
    public String doEdit(@PathVariable(name="id")int questionId,
                         @RequestParam("question_title")String title,
                         @RequestParam("question_description")String description,
                         @RequestParam("question_tag")String tag,
                         Model model){

        int res = questionService.updateQuestionById(questionId,title,description,tag);

        if(res != 1){
            model.addAttribute("error","修改失败：出现了未知异常");
            return "/toEdit/"+questionId;
        }
        return "redirect:/";
    }



    @ResponseBody
    @GetMapping("/getAuthorQuestions/{accountId}")
    public RequestDTO<List<QuestionDTO>> getAuthorQuestion(@PathVariable(name="accountId")String accountId){
        List<QuestionDTO> questionDTOS = questionService.listQuestionByAuthorAccountId(accountId);
        return new RequestDTO<>(200,questionDTOS);
    }


    @ResponseBody
    @GetMapping("/getRelatedQuestions/{questionId}/{tags}")
    public RequestDTO<List<QuestionDTO>> getRelatedQuestion(@PathVariable(name="questionId")long questionId,@PathVariable(name="tags")String tags){
        List<QuestionDTO> questionDTOS = questionService.queryRelatedQuestions(questionId,tags);
        return new RequestDTO<>(200,questionDTOS);
    }

    @ResponseBody
    @GetMapping("/getAllRelatedQuestions/{tagId}")
    public String getAllRelatedQuestion(@PathVariable(name="tagId")String tagId,
                                       @RequestParam(name="startNumber",defaultValue= "1")int offset,
                                       @RequestParam(name="size",defaultValue = "5")int size,
                                        Model model){
        PaginationDTO<QuestionDTO> PaginationList = questionService.queryAllRelatedQuestions(tagId, offset, size);
        model.addAttribute("PaginationList",PaginationList);
        return "index";
    }




}
