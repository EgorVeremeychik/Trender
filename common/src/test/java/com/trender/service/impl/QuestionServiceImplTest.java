package com.trender.service.impl;

import com.trender.dao.RoleDao;
import com.trender.entity.Question;
import com.trender.service.QuestionService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by Egor.Veremeychik on 14.06.2016.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:common-context.xml")
public class QuestionServiceImplTest extends Assert {

    @Autowired
    private QuestionService questionService;

    @Test
    public void create() throws Exception {
        questionService.create(new Question("fgsd"));
    }

}