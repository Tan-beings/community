package com.tanquandan.blogsystem.mapper;

import com.tanquandan.blogsystem.DAO.Tag;
import com.tanquandan.blogsystem.DAO.TagExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface TagExtMapper {
    int insert(long questionId,int tagId);

    List<Tag> selectTagsByQuestionId(long questionId);
}