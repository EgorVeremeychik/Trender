package com.trender.dao.JPA;

import com.trender.dao.IAnswerDAO;
import com.trender.dao.exception.DAOException;
import com.trender.entity.Answer;
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
public class IAnswerDAOImpl implements IAnswerDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long create(Answer entity) throws DAOException {
        return null;
    }

    @Override
    public Answer read(Long id) throws DAOException {
        return null;
    }

    @Override
    public List<Answer> readAll() throws DAOException {
        return null;
    }

    @Override
    public void update(Answer entity) throws DAOException {

    }

    @Override
    public void delete(Long id) throws DAOException {

    }
}
