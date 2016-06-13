package com.trender.dao.JPA;

import com.trender.dao.IQuestionDAO;
import com.trender.dao.exception.DAOException;
import com.trender.entity.Question;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by EgorVeremeychik on 13.06.2016.
 */

@Repository
@Transactional
public class IQuestionDAOImpl implements IQuestionDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long create(Question entity) throws DAOException {
        return null;
    }

    @Override
    public Question read(Long id) throws DAOException {
        return null;
    }

    @Override
    public List<Question> readAll() throws DAOException {
        return null;
    }

    @Override
    public void update(Question entity) throws DAOException {

    }

    @Override
    public void delete(Long id) throws DAOException {

    }
}
