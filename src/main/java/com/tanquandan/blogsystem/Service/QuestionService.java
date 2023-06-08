package com.tanquandan.blogsystem.Service;

import com.tanquandan.blogsystem.DAO.Question;
import com.tanquandan.blogsystem.DAO.QuestionExample;
import com.tanquandan.blogsystem.DAO.Tag;
import com.tanquandan.blogsystem.DAO.User;
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
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    TagService tagService;

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
        questionExample.setOrderByClause("gmt_create desc");
        return questionMapper.selectByExampleWithBLOBsWithRowbounds(questionExample,new RowBounds(offset,size));
    }

    public Question findQuestionById(long questionId){
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andIdEqualTo(questionId);
        List<Question> questions = questionMapper.selectByExampleWithBLOBs(questionExample);
        if(questions.size() <=0 ){
            return null;
        }
        return questions.get(0);
    }

    public int updateQuestionById(long questionId,String title,String description,String tag){
        Question question = findQuestionById(questionId);
        if(question != null){
            question.setTitle(title);
            question.setDescription(description);
        }
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andIdEqualTo(questionId);
        return questionMapper.updateByExampleSelective(question,questionExample);
    }

    /*复合方法*/

    // 1.用户访问index.html: 显示所有的问题
    public PaginationDTO<QuestionDTO> listAllQuestions(int currentPage, int size){
        int offset = (currentPage-1)*size;

        List<Question> tmp_questions = selectAllQuestions(offset, size);
        int questionsAmounts = selectAllQuestionsCount();

        List<QuestionDTO> questions = setQuestions(tmp_questions);
        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO(questions,currentPage,size,questionsAmounts);
        paginationDTO.setPagination();
        return paginationDTO;
    }
    // 2.用户访问其他用户的个人页面question.html:显示当前用户发布的问题
    public PaginationDTO<QuestionDTO> listQuestionsByUser(String account_id,int currentPage, int size){
        int offset = (currentPage-1)*size;

        List<Question> tmp_questions = selectQuestionsByAccountId(account_id,offset, size);
        int questionsAmounts = selectQuestionsByAccountIdCount(account_id);

        List<QuestionDTO> questions = setQuestions(tmp_questions);
        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO(questions,currentPage,size,questionsAmounts);
        paginationDTO.setPagination();
        return paginationDTO;
    }
    // 3.将List<Question>转换成为List<QuestionDTO>
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
    // 4.将Question转换成为QuestionDTO
    public QuestionDTO setQuestion(Question question){
            QuestionDTO target_question = new QuestionDTO();
            BeanUtils.copyProperties(question,target_question);
            target_question.setUser(userService.findByAccountId(question.getCreator()));
            return target_question;
    }

    // 5.用户修改问题/其他用户查看问题: 查询单个问题
    public QuestionDTO queryQuestionById(long questionId) {
        Question question = findQuestionById(questionId);
        List<Tag> tags = tagService.queryTagsByQuestionId(questionId);
        QuestionDTO target_question = null;
        if(question!=null) {
            target_question = setQuestion(question);
            target_question.setTag(tags);
        }
        else{
            throw new CustomizedException(CustomizedErrorCode.QUESTION_NOT_FOUND);
        }
        return target_question;
    }
    // 6.用户查看某个问题时，侧栏会展示作者近期发布的其他问题
    public List<QuestionDTO> listQuestionByAuthorAccountId(String accountId){
         return setQuestions(selectQuestionsByAccountId(accountId,0,3));
    }
    // 7.用户查看某个问题时，更新该问题的浏览量
    public void updateViewCountById(int id) {
        Question question = findQuestionById(id);
        question.setViewCount(question.getViewCount()+1);
        questionExtMapper.updateViewCountById(question);
    }
    // 8.用户查看某个问题时，侧栏会展示tag相同的其他问题
    public List<QuestionDTO> queryRelatedQuestions(long questionId,String tags) {
        String tagIds = "("+tags+")";
        List<Question> questions = questionExtMapper.selectRelatedQuestions(questionId,tagIds,3);
        return setQuestions(questions);
    }
    // 9.新增问题
    @Transactional
    public int insertAQuestion(Question q,String tags){
        q.setGmtCreate(System.currentTimeMillis());
        q.setGmtModified(q.getGmtCreate());
        q.setViewCount(0);
        q.setCommentCount(0);
        q.setLikeCount(0);
        int row = questionMapper.insert(q);
        if(row == 0){
            throw new CustomizedException(CustomizedErrorCode.TAG_INSERT_FAIL);
        }
        tagService.insertTagQuestionLinks(tags,q.getId());



        return 1;

       
    }
}
