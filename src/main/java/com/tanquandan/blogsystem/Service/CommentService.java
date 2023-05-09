package com.tanquandan.blogsystem.Service;

import com.tanquandan.blogsystem.DAO.Comment;
import com.tanquandan.blogsystem.DAO.CommentExample;
import com.tanquandan.blogsystem.DAO.Question;
import com.tanquandan.blogsystem.DAO.User;
import com.tanquandan.blogsystem.DTO.CommentDTO;
import com.tanquandan.blogsystem.DTO.CommentTransmissionDTO;
import com.tanquandan.blogsystem.Exception.CustomizedErrorCode;
import com.tanquandan.blogsystem.Exception.CustomizedException;
import com.tanquandan.blogsystem.mapper.CommentMapper;
import com.tanquandan.blogsystem.mapper.QuestionExtMapper;
import com.tanquandan.blogsystem.mapper.QuestionMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    public CommentMapper commentMapper;

    @Autowired
    public QuestionMapper questionMapper;

    @Autowired
    public QuestionExtMapper questionExtMapper;

    @Autowired
    public UserService userService;

    @Transactional
    public int insertComment(CommentTransmissionDTO commentTransmissionDTO, String userId){
        Comment comment = new Comment();
        // 如果parentId为0或者不存在
        if(commentTransmissionDTO.getParentId() == null || commentTransmissionDTO.getParentId() == 0){
            throw new CustomizedException(CustomizedErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        // 如果type为0或者不存在
        if(commentTransmissionDTO.getType() == null || commentTransmissionDTO.getType() == 0){
            throw new CustomizedException(CustomizedErrorCode.TYPE_PARAM_NULL);

        }
        // 如果type为1，则表示这条评论是直接对问题的回复，则检查问题是否存在
        if(commentTransmissionDTO.getType() == 1){
            Question question = questionMapper.selectByPrimaryKey(commentTransmissionDTO.getParentId());
            if(question == null){
                throw new CustomizedException(CustomizedErrorCode.QUESTION_NOT_FOUND);
            }
            question.setCommentCount(1);
            questionExtMapper.updateViewCountById(question);

        }
        // 如果type为2，则表示这条评论是对某评论的回复，则检查回复的评论是否存在
        if(commentTransmissionDTO.getType() == 2){
            Comment check_comment = commentMapper.selectByPrimaryKey(commentTransmissionDTO.getParentId());
            if(check_comment == null){
                throw new CustomizedException(CustomizedErrorCode.COMMENT_NOT_FOUND);
            }
        }


        BeanUtils.copyProperties(commentTransmissionDTO,comment);
        comment.setCommentator(userId);
        comment.setGmtCreatetime(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreatetime());
        System.out.println(comment);
        return commentMapper.insert(comment);
    }

    public List<Comment> queryCommentsByQuestionId(long id){
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andTypeEqualTo(1).andParentIdEqualTo(id);

        return commentMapper.selectByExample(commentExample);
    }


    public List<CommentDTO> queryCommentDTOsByQuestionId(long id) {
        // 1.查询出某个问题的所有一级评论
        List<Comment> comments = queryCommentsByQuestionId(id);
        // 2.查询出所有评论对应的用户ID
        Set<String> userIdsTemp = comments.stream().map(Comment::getCommentator).collect(Collectors.toSet());
        List<String> userIds = new ArrayList<>(userIdsTemp);
        // 3.生成 userId:user 的Map
        Map<String, User> users =userIds.stream().collect(Collectors.toMap(userId -> userId,userId -> userService.findByAccountId(userId)));
        // 4.把user赋给commentDTO
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment,commentDTO);
            commentDTO.setUser(users.get(commentDTO.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());

        return commentDTOS;

    }
}
