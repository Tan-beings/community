package com.tanquandan.blogsystem.mapper;


import com.tanquandan.blogsystem.DAO.Comment;
import com.tanquandan.blogsystem.DAO.Question;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface  QuestionExtMapper {
    int updateViewCountById(Question question);
    int updateCommentCountById(Question question);
    List<Question> selectRelatedQuestions(@Param("questionId") long questionId,
                                          @Param("tagIds")String tagIds,
                                          @Param("size") int size);
}
