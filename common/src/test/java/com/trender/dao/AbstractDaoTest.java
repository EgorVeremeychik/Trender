package com.trender.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Egor.Veremeychik on 14.06.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:common-context.xml")
public class AbstractDaoTest extends Assert {

    @Autowired
    private UserDao userDAO;

    @Test
    public void testReadAll() throws Exception {
        for(int i = 0; i< userDAO.readAll().size(); i++){
            System.out.println(userDAO.readAll().get(i));
        }
    }

    /*@Test
    public void testCreate() throws Exception {
        userDAO.create(new User("password", "email", "firstName", "secondName"));
    }*/

}