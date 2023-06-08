package com.tanquandan.blogsystem.Service;

import com.tanquandan.blogsystem.DAO.Tag;
import com.tanquandan.blogsystem.DAO.TagExample;
import com.tanquandan.blogsystem.mapper.TagExtMapper;
import com.tanquandan.blogsystem.mapper.TagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    @Autowired
    TagExtMapper tagExtMapper;

    @Autowired
    TagMapper tagMapper;

    public void insertTagQuestionLinks(String tags,long questionId){
        String[] tagIdArr = tags.split(",");
        for (String tagId : tagIdArr) {
            tagExtMapper.insert(questionId,Integer.parseInt(tagId));
        }
    }

    public List<Tag> queryTagsByQuestionId(long questionId) {
       return tagExtMapper.selectTagsByQuestionId(questionId);
    }

    public List<Tag> queryAllTags(){
        return tagMapper.selectByExample(new TagExample());
    };
}
