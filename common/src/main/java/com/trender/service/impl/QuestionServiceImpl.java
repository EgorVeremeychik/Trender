package com.trender.service.impl;

import com.trender.dao.QuestionDao;
import com.trender.dao.exception.DaoException;
import com.trender.entity.Question;
import com.trender.service.QuestionService;
import com.trender.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Egor.Veremeychik on 14.06.2016.
 */

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionDao questionDao;

    public QuestionServiceImpl() {}

    public QuestionServiceImpl(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    @Override
    public void create(Question question) throws ServiceException {
        try {
            questionDao.create(question);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(Long id) throws ServiceException {
        try {
            questionDao.delete(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void update(Question question) throws ServiceException {
        try {
            questionDao.update(question);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Question getById(Long id) throws ServiceException {
        Question result = null;
        try {
            result = questionDao.read(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return result;
    }

    @Override
    public List<Question> getAll() throws ServiceException {
        List<Question> result = null;
        try {
            result = questionDao.readAll();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return result;
    }
}
