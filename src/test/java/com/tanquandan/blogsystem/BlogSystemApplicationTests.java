package com.tanquandan.blogsystem;

import com.tanquandan.blogsystem.DAO.Question;
import com.tanquandan.blogsystem.mapper.QuestionExtMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class BlogSystemApplicationTests {
    @Autowired
    QuestionExtMapper questionExtMapper;


    @Test
    void contextLoads() {
    }

    @Test
    void testSelectRelatedQuestions(){
        ArrayList<Integer> tags = new ArrayList<>();
        tags.add(1);
        tags.add(2);
        tags.add(3);


    }

}
