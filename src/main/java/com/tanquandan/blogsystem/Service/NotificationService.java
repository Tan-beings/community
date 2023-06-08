package com.tanquandan.blogsystem.Service;


import com.tanquandan.blogsystem.DAO.*;
import com.tanquandan.blogsystem.DTO.NotificationDTO;
import com.tanquandan.blogsystem.DTO.PaginationDTO;
import com.tanquandan.blogsystem.Enums.CommentTypeEnum;
import com.tanquandan.blogsystem.Enums.NotificationStatusEnum;
import com.tanquandan.blogsystem.Exception.CustomizedErrorCode;
import com.tanquandan.blogsystem.Exception.CustomizedException;
import com.tanquandan.blogsystem.mapper.NotificationMapper;
import com.tanquandan.blogsystem.mapper.QuestionMapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class NotificationService {
    @Autowired
    NotificationMapper notificationMapper;

    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;

    @Autowired
    CommentService commentService;






    public List<Notification> queryNotificationByUserAccountId(String accountId,int offset,int size){
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(accountId);
        notificationExample.setOrderByClause("gmt_create desc");
        return notificationMapper.selectByExampleWithRowbounds(notificationExample,new RowBounds(offset,size));
    }

    public long queryNotificationsByAccountIdCount(String accountId){
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(accountId).andNotifierNotEqualTo(accountId);
        return notificationMapper.countByExample(notificationExample);
    }

    public PaginationDTO<NotificationDTO> listNotificationsByUser(String accountId, int currentPage, int size) {
        int offset = (currentPage-1)*size;
        List<Notification> tmp_notifications =  queryNotificationByUserAccountId(accountId,offset,size);
        long notificationsAmounts = queryNotificationsByAccountIdCount(accountId);
        List<NotificationDTO> notifications = setNotifications(tmp_notifications);

        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO(notifications,currentPage,size,notificationsAmounts);
        paginationDTO.setPagination();
        return paginationDTO;
    }

    public List<NotificationDTO> setNotifications(List<Notification> tmp_notifications){
        List<NotificationDTO> notifications = new ArrayList<>();
        for (Notification notification : tmp_notifications) {
            NotificationDTO target_notification = new NotificationDTO();
            BeanUtils.copyProperties(notification,target_notification);
            if(target_notification.getType() == CommentTypeEnum.QUESTION.getType()){
                target_notification.setOuterTitle(questionService.findQuestionById(notification.getQuestionId()).getTitle());
            }
            if(target_notification.getType() == CommentTypeEnum.COMMENT.getType()){
                target_notification.setOuterTitle(commentService.findCommentById(notification.getCommentId()).getContent());
            }
            target_notification.setCommentContent(commentService.findCommentById(notification.getCommentId()).getContent());
            target_notification.setNotifierName(userService.findByAccountId(notification.getNotifier()).getName());
            target_notification.setReceiverName(userService.findByAccountId(notification.getReceiver()).getName());
            notifications.add(target_notification);
        }
        return notifications;
    }

    public Long read(Long notificationId, User user) {
        // 如果通知被删除了
        Notification notification = notificationMapper.selectByPrimaryKey(notificationId);
        if(notification == null){
            throw new CustomizedException(CustomizedErrorCode.NOTIFICATION_NOT_FOUND);
        }
        // 如果是其他用户在读此用户的通知
        if(!Objects.equals(notification.getReceiver(),user.getAccountId())){
            throw new CustomizedException(CustomizedErrorCode.NOTIFICATION_READ_FAIL);
        }
        // 修改状态
        notification.setStatus(NotificationStatusEnum.READ.getId());
        notificationMapper.updateByPrimaryKey(notification);
        /**
        // 封装一个NotificationDTO
        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification,notificationDTO);
         **/
        return notification.getQuestionId();
    }
}

