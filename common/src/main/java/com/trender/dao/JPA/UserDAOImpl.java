package com.trender.dao.jpa;

import com.trender.dao.Dao;
import com.trender.dao.UserDao;
import com.trender.dao.exception.DaoException;
import com.trender.entity.Role;
import com.trender.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

import static com.trender.entity.Role.READ_USER_ROLES;
import static com.trender.entity.User.READ_USER_BY_LOGIN;

/**
 * Created by EgorVeremeychik on 13.06.2016.
 */

@Repository
public class UserDaoImpl extends AbstractDao<User, Long> implements UserDao {

    public UserDaoImpl() {
        super(User.class);
    }

    @Override
    public User readUserByLogin(String login) throws DaoException {
        User result = null;
        TypedQuery<User> query = entityManager.createNamedQuery(READ_USER_BY_LOGIN, User.class);
        query.setParameter("email", login);
        result = query.getSingleResult();
        return result;
    }
}
