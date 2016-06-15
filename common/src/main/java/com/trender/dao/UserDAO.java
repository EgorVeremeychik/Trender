package com.trender.dao;

import com.trender.dao.exception.DaoException;
import com.trender.entity.User;

/**
 * Created by Egor.Veremeychik on 14.06.2016.
 */

public interface UserDao extends Dao<User,Long> {

    User readUserByLogin(String login) throws DaoException;

}
