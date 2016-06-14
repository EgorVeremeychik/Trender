package com.trender.dao.jpa;

import com.trender.dao.Dao;
import com.trender.dao.UserDao;
import com.trender.entity.User;
import org.springframework.stereotype.Repository;

/**
 * Created by EgorVeremeychik on 13.06.2016.
 */

@Repository
public class UserDaoImpl extends AbstractDao<User, Long> implements UserDao {

    public UserDaoImpl() {
        super(User.class);
    }

}
