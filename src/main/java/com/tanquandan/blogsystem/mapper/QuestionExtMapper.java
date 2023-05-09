package com.tanquandan.blogsystem.mapper;


import com.tanquandan.blogsystem.DAO.Comment;
import com.tanquandan.blogsystem.DAO.Question;

public interface  QuestionExtMapper {
    int updateViewCountById(Question question);
    int updateCommentCountById(Question question);
}
