package com.trender.service.impl;

import com.trender.dao.UserDao;
import com.trender.dao.exception.DaoException;
import com.trender.entity.User;
import com.trender.service.UserService;
import com.trender.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by Egor.Veremeychik on 14.06.2016.
 */
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    public UserServiceImpl() {}

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void create(User user) throws ServiceException {
        try {
            userDao.create(user);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(Long id) throws ServiceException {
        try {
            userDao.delete(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void update(User user) throws ServiceException {
        try {
            userDao.update(user);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public User getById(Long id) throws ServiceException {
        User result = null;
        try {
            result = userDao.read(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return result;
    }

    @Override
    public List<User> getAll() throws ServiceException {
        List<User> result = null;
        try {
            result = userDao.readAll();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return result;
    }
}
