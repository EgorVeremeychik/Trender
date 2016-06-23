package com.trender.service.impl;

import com.trender.dao.AnswerDao;
import com.trender.dao.RoleDao;
import com.trender.dao.exception.DaoException;
import com.trender.entity.Answer;
import com.trender.entity.Role;
import com.trender.service.AnswerService;
import com.trender.service.exception.ServiceException;
import com.vaadin.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Egor.Veremeychik on 14.06.2016.
 */

@Service
@SpringComponent
public class AnswerServiceImpl implements AnswerService {

    @Autowired
    private AnswerDao answerDao;

    public AnswerServiceImpl() {}

    public AnswerServiceImpl(AnswerDao answerDao) {
        this.answerDao = answerDao;
    }

    @Override
    public void create(Answer answer) throws ServiceException {
        try {
            answerDao.create(answer);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(Long id) throws ServiceException {
        try {
            answerDao.delete(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void update(Answer answer) throws ServiceException {
        try {
            answerDao.update(answer);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Answer getById(Long id) throws ServiceException {
        Answer result = null;
        try {
            result = answerDao.read(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return result;
    }

    @Override
    public List<Answer> getAll() throws ServiceException {
        List<Answer> result = null;
        try {
            result = answerDao.readAll();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return result;
    }
}
