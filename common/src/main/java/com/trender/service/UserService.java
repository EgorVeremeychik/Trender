package com.trender.service;

import com.trender.entity.User;
import com.trender.service.exception.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by EgorVeremeychik on 14.06.2016.
 */

public interface UserService {
    void create(User user) throws ServiceException;

    void delete(Long id) throws ServiceException;

    void update(User user) throws ServiceException;

    User getById(Long id) throws ServiceException;

    List<User> getAll() throws ServiceException;

    User readByLogin(String login) throws ServiceException;
}
