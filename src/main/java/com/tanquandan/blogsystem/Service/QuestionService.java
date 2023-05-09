package com.tanquandan.blogsystem.Service;

import com.tanquandan.blogsystem.DAO.Question;
import com.tanquandan.blogsystem.DAO.QuestionExample;
import com.tanquandan.blogsystem.DTO.PaginationDTO;
import com.tanquandan.blogsystem.DTO.QuestionDTO;
import com.tanquandan.blogsystem.Exception.CustomizedErrorCode;
import com.tanquandan.blogsystem.Exception.CustomizedException;
import com.tanquandan.blogsystem.mapper.QuestionExtMapper;
import com.tanquandan.blogsystem.mapper.QuestionMapper;
import com.tanquandan.blogsystem.mapper.UserMapper;
import org.apache.ibatis.session.RowBounds;
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
    QuestionExtMapper questionExtMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserService userService;
    // 原子方法
    public int selectAllQuestionsCount(){
        QuestionExample questionExample = new QuestionExample();
        return (int) questionMapper.countByExample(questionExample);
    }

    public int selectQuestionsByAccountIdCount(String accountId){
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(accountId);
        return (int) questionMapper.countByExample(questionExample);
    }

    public List<Question> selectAllQuestions(int offset, int size){
        QuestionExample questionExample = new QuestionExample();
        return questionMapper.selectByExampleWithBLOBsWithRowbounds(questionExample,new RowBounds(offset,size));
    }

    public List<Question> selectQuestionsByAccountId(String accountId,int offset,int size){
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(accountId);
        return questionMapper.selectByExampleWithBLOBsWithRowbounds(questionExample,new RowBounds(offset,size));
    }

    public Question selectQuestionById(long questionId){
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andIdEqualTo(questionId);
        List<Question> questions = questionMapper.selectByExampleWithBLOBs(questionExample);
        if(questions.size() <=0 ){
            return null;
        }
        return questions.get(0);
    }

    public int updateQuestionById(long questionId,String title,String description,String tag){
        Question question = selectQuestionById(questionId);
        if(question != null){
            question.setTitle(title);
            question.setDescription(description);
            question.setTag(tag);
        }
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andIdEqualTo(questionId);
        return questionMapper.updateByExampleSelective(question,questionExample);
    }

    // 复合方法
    public PaginationDTO listAllQuestions(int currentPage, int size){
        int offset = (currentPage-1)*size;

        List<Question> tmp_questions = selectAllQuestions(offset, size);
        int questionsAmounts = selectAllQuestionsCount();

        List<QuestionDTO> questions = setQuestions(tmp_questions);
        PaginationDTO paginationDTO = new PaginationDTO(questions,currentPage,size,questionsAmounts);
        paginationDTO.setPagination();
        return paginationDTO;
    }

    public PaginationDTO listQuestionsByUser(String account_id,int currentPage, int size){
        int offset = (currentPage-1)*size;

        List<Question> tmp_questions = selectQuestionsByAccountId(account_id,offset, size);
        int questionsAmounts = selectQuestionsByAccountIdCount(account_id);

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
            target_question.setUser(userService.findByAccountId(question.getCreator()));
            questions.add(target_question);
        }
        return questions;
    }

    public QuestionDTO setQuestion(Question question){
            QuestionDTO target_question = new QuestionDTO();
            BeanUtils.copyProperties(question,target_question);
            target_question.setUser(userService.findByAccountId(question.getCreator()));
            return target_question;
    }

    public QuestionDTO queryQuestionById(int questionId) {
        Question question = selectQuestionById(questionId);
        QuestionDTO target_question = null;
        if(question!=null) {
            target_question = setQuestion(question);
        }
        else{
            throw new CustomizedException(CustomizedErrorCode.QUESTION_NOT_FOUND);
        }
        return target_question;
    }

    public void updateViewCountById(int id) {
        Question question = selectQuestionById(id);
        question.setViewCount(question.getViewCount()+1);
        questionExtMapper.updateViewCountById(question);
    }
}
