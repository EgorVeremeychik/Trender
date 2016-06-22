package com.trender.dao;

import com.trender.entity.Role;
import com.trender.entity.User;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Egor.Veremeychik on 14.06.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:common-context.xml")
public class AbstractDaoTest extends Assert {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private UserDao userDAO;

    @Test
    public void testReadAll() throws Exception {
        for(int i = 0; i< userDAO.readAll().size(); i++){
            System.out.println(userDAO.readAll().get(i));
        }
    }

    @Ignore
    @Test
    public void testCreate() throws Exception {
        Set<Role> roles = new HashSet<>();
        Role role = roleDao.readRoleByName("USER");
        Role role1 = roleDao.readRoleByName("ADMIN");
        roles.add(role);
        roles.add(role1);
        userDAO.create(new User("password", "dsfgsdgdsf", "firstName", "secondName",roles));
    }

}