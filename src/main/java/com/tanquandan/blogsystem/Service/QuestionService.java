package com.tanquandan.blogsystem.Service;

import com.tanquandan.blogsystem.DAO.Question;
import com.tanquandan.blogsystem.DTO.PaginationDTO;
import com.tanquandan.blogsystem.DTO.QuestionDTO;
import com.tanquandan.blogsystem.Mapper.QuestionMapper;
import com.tanquandan.blogsystem.Mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    QuestionMapper questionMapper;
    @Autowired
    UserMapper userMapper;

    public PaginationDTO listAllQuestions(int currentPage, int size){
        int offset = (currentPage-1)*size;

        List<Question> tmp_questions = questionMapper.selectAllQuestions(offset, size);
        int questionsAmounts = questionMapper.selectAllQuestionsCount();

        List<QuestionDTO> questions = setQuestions(tmp_questions);
        PaginationDTO paginationDTO = new PaginationDTO(questions,currentPage,size,questionsAmounts);
        paginationDTO.setPagination();
        return paginationDTO;
    }

    public PaginationDTO listQuestionsByUser(String account_id,int currentPage, int size){
        int offset = (currentPage-1)*size;

        List<Question> tmp_questions = questionMapper.selectQuestionsByAccountId(account_id,offset, size);
        int questionsAmounts = questionMapper.selectQuestionsByAccountIdCount(account_id);

        List<QuestionDTO> questions = setQuestions(tmp_questions);
        PaginationDTO paginationDTO = new PaginationDTO(questions,currentPage,size,questionsAmounts);
        paginationDTO.setPagination();
        return paginationDTO;
    }



    public List<QuestionDTO> setQuestions(List<Question> tmp_questions){
        List<QuestionDTO> questions = new ArrayList<>();
        for (Question question : tmp_questions) {
            QuestionDTO target_question = new QuestionDTO();
            BeanUtils.copyProperties(question,target_question);
            target_question.setUser(userMapper.findById(question.getCreator()));
            questions.add(target_question);
        }
        return questions;
    }

    public QuestionDTO setQuestion(Question question){
            QuestionDTO target_question = new QuestionDTO();
            BeanUtils.copyProperties(question,target_question);
            target_question.setUser(userMapper.findById(question.getCreator()));
            return target_question;
    }

    public QuestionDTO queryQuestionById(int id) {
        Question question = questionMapper.selectQuestionById(id);
        QuestionDTO target_question = setQuestion(question);
        return target_question;
    }
}
