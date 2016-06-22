package com.trender.service;

import com.trender.entity.Answer;
import com.trender.service.exception.ServiceException;

import java.util.List;

/**
 * Created by EgorVeremeychik on 14.06.2016.
 */

public interface AnswerService {
    void create(Answer answer) throws ServiceException;

    void delete(Long id) throws ServiceException;

    void update(Answer answer) throws ServiceException;

    Answer getById(Long id) throws ServiceException;

    List<Answer> getAll() throws ServiceException;
}
