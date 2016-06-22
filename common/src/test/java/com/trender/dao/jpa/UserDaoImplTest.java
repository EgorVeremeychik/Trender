package com.trender.dao.jpa;

import com.trender.dao.UserDao;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Egor.Veremeychik on 15.06.2016.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:common-context.xml")
public class UserDaoImplTest extends Assert {

    @Autowired
    UserDao userDao;

    @Test
    public void readUserByLogin() throws Exception {
        System.out.println(userDao.readUserByLogin("qwe@mail.ru"));
    }
}