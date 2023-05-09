package com.tanquandan.blogsystem.DTO;

import com.tanquandan.blogsystem.DAO.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

    private Long id;

    private Long parentId;

    private Integer type;

    private String commentator;

    private Long gmtCreatetime;

    private Long gmtModified;

    private Long likeCount;

    private String content;

    private User user;

}