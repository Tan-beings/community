package com.tanquandan.blogsystem.Service;

import com.tanquandan.blogsystem.DAO.*;
import com.tanquandan.blogsystem.DTO.CommentDTO;
import com.tanquandan.blogsystem.DTO.CommentTransmissionDTO;
import com.tanquandan.blogsystem.Enums.CommentTypeEnum;
import com.tanquandan.blogsystem.Enums.NotificationStatusEnum;
import com.tanquandan.blogsystem.Exception.CustomizedErrorCode;
import com.tanquandan.blogsystem.Exception.CustomizedException;
import com.tanquandan.blogsystem.mapper.CommentMapper;
import com.tanquandan.blogsystem.mapper.NotificationMapper;
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

    @Autowired
    NotificationMapper notificationMapper;


    @Transactional
    public int insertComment(Comment comment, String userId){

        // 如果parentId为0或者不存在
        if(comment.getParentId() == null || comment.getParentId() == 0){
            throw new CustomizedException(CustomizedErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        // 如果type为0或者不存在
        if(comment.getType() == null || comment.getType() == 0){
            throw new CustomizedException(CustomizedErrorCode.TYPE_PARAM_NULL);
        }
        // 新增comment,获取自增主键
        int rows = commentMapper.insert(comment)>1?1:0;
        // 如果comment是对文章的评论
        if(comment.getType() == CommentTypeEnum.QUESTION.getType()) {
            // 获取QuestionId，CommentCount更新，新增Notification
            Question parentQuestion = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (parentQuestion == null) {
                throw new CustomizedException(CustomizedErrorCode.QUESTION_NOT_FOUND);
            }
            parentQuestion.setCommentCount(1);
            questionExtMapper.updateCommentCountById(parentQuestion);
            createNotification(userId, comment.getReceiver(), CommentTypeEnum.QUESTION.getType(), comment.getId(), parentQuestion.getId());
        }
        // 如果comment是对评论的回复
        if(comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            Comment parentComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (parentComment == null) {
                throw new CustomizedException(CustomizedErrorCode.COMMENT_NOT_FOUND);
            }
            Question parentQuestion = questionMapper.selectByPrimaryKey(parentComment.getParentId());
            if (parentQuestion == null) {
                throw new CustomizedException(CustomizedErrorCode.QUESTION_NOT_FOUND);
            }
            createNotification(userId, comment.getReceiver(), CommentTypeEnum.COMMENT.getType(), comment.getId(), parentQuestion.getId());
        }

        return rows;
    }

    public Notification createNotification(String notifier, String receiver, Integer type,Long commentId, Long questionId){
        Notification notification = new Notification();
        notification.setNotifier(notifier);
        notification.setReceiver(receiver);
        notification.setType(type);
        notification.setCommentId(commentId);
        notification.setQuestionId(questionId);
        notification.setStatus(NotificationStatusEnum.UNREAD.getId());
        notification.setGmtCreate(System.currentTimeMillis());
        notificationMapper.insert(notification);
        return notification;
    }


    public List<Comment> queryCommentsByQuestionId(long questionId){
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andTypeEqualTo(1).andParentIdEqualTo(questionId);
        commentExample.setOrderByClause("gmt_createTime desc");
        return commentMapper.selectByExampleWithBLOBs(commentExample);
    }
    public List<Comment> queryCommentsByParentId(long parentId){
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andTypeEqualTo(2).andParentIdEqualTo(parentId);
        commentExample.setOrderByClause("gmt_createTime desc");
        return commentMapper.selectByExampleWithBLOBs(commentExample);
    }

    public Comment queryCommentByCommentId(long commentId){
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andIdEqualTo(commentId);
        return commentMapper.selectByExampleWithBLOBs(commentExample).get(0);
    }



    public List<CommentDTO> queryCommentDTOsByQuestionId(long id) {
        // 1.查询出某个问题的所有一级评论
        List<Comment> comments = queryCommentsByQuestionId(id);
        return setCommentDTOs(comments);
    }

    public Comment findCommentById(long id){
        CommentExample example = new CommentExample();
        example.createCriteria().andIdEqualTo(id);
        return commentMapper.selectByExampleWithBLOBs(example).get(0);
    }

    public List<CommentDTO> setCommentDTOs(List<Comment> comments){
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

    public List<CommentDTO> querySubComments(long parentId) {
        List<Comment> subComments = queryCommentsByParentId(parentId);
        return setCommentDTOs(subComments);
    }
}
