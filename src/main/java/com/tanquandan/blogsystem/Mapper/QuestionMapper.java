package com.tanquandan.blogsystem.Mapper;

import com.tanquandan.blogsystem.DAO.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuestionMapper {

    @Insert("insert into question(title,description,gmt_create,gmt_modified,creator,tag) values(#{title},#{description},#{gmt_create},#{gmt_modified},#{creator},#{tag})")
    int insert(Question q);

    @Select("select * from question limit #{offset},#{size}")
    List<Question> selectAllQuestions(int offset,int size);

    @Select("select count(*) from question")
    int selectAllQuestionsCount();

    @Select("select * from question where creator=#{id} limit #{offset},#{size}")
    List<Question> selectQuestionsByAccountId(String id,int offset,int size);

    @Select("select count(*) from question where creator=#{id}")
    int selectQuestionsByAccountIdCount(String id);
}
