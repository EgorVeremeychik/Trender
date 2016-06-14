package com.trender.dao.jpa;

import com.trender.dao.Dao;
import com.trender.dao.QuestionDao;
import com.trender.entity.Question;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * Created by EgorVeremeychik on 13.06.2016.
 */

@Repository
public class QuestionDaoImpl extends AbstractDao<Question,Long> implements QuestionDao {

    public QuestionDaoImpl() {
        super(Question.class);
    }
}
