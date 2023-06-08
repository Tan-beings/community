package com.tanquandan.blogsystem.DTO;

import lombok.Data;

@Data
public class NotificationDTO {

    private Long id;

    private String notifier;

    private String receiver;

    private Integer type;

    private Long questionId;

    private Integer status;

    private Long gmtCreate;



    private String outerTitle;

    private String commentContent;

    private String notifierName;

    private String receiverName;

}
