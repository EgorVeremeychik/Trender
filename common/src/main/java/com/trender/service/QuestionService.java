package com.trender.service;

import com.trender.entity.Question;
import com.trender.service.exception.ServiceException;

import java.util.List;

/**
 * Created by EgorVeremeychik on 14.06.2016.
 */
public interface QuestionService {
    void create(Question question) throws ServiceException;

    void delete(Long id) throws ServiceException;

    void update(Question question) throws ServiceException;

    Question getById(Long id) throws ServiceException;

    List<Question> getAll() throws ServiceException;
}
