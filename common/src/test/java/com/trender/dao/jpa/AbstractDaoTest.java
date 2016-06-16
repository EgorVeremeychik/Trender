package com.trender.dao.jpa;

import com.trender.dao.UserDao;
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
    private UserDao userDao;

    @Test
    public void testRead() throws Exception {
        /*System.out.println(userDao.read(2L));*/
    }
}