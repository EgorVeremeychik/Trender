package com.trender.dao.jpa;

import com.trender.dao.AnswerDao;
import com.trender.dao.Dao;
import com.trender.entity.Answer;
import org.springframework.stereotype.Repository;

/**
 * Created by EgorVeremeychik on 13.06.2016.
 */

@Repository
public class AnswerDaoImpl extends AbstractDao<Answer, Long> implements AnswerDao{

    public AnswerDaoImpl() {
        super(Answer.class);
    }
}
