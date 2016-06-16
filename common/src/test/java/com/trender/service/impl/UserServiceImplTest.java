package com.trender.service.impl;

import com.trender.service.UserService;
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
public class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Test
    public void readByLogin() throws Exception {
        /*System.out.println(userService.readByLogin("qwe1@mail.ru"));*/
    }

}