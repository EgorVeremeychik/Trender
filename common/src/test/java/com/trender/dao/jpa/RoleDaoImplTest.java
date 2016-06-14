package com.trender.dao.jpa;

import com.trender.dao.RoleDao;
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
public class RoleDaoImplTest extends Assert {

    @Autowired
    private RoleDao roleDao;

    @Test
    public void testReadRoleByUserId() throws Exception {
        System.out.println(roleDao.readRoleByUserId(1L));
    }
}