package com.tanquandan.blogsystem.DTO;

import com.tanquandan.blogsystem.DAO.Tag;
import com.tanquandan.blogsystem.DAO.User;
import lombok.Data;

import java.util.List;

@Data
public class QuestionDTO {
    private Long id;

    private String title;

    private Long gmtCreate;

    private Long gmtModified;

    private String creator;

    private Integer commentCount;

    private Integer viewCount;

    private Integer likeCount;

    private List<Tag> tag;

    private String description;

    private User user;
}
