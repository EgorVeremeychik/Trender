package com.trender.dao.JPA;

import com.trender.dao.IUserDAO;
import com.trender.dao.exception.DAOException;
import com.trender.entity.User;
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
public class IUserDAOImpl implements IUserDAO{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long create(User entity) throws DAOException {
        return null;
    }

    @Override
    public User read(Long id) throws DAOException {
        return null;
    }

    @Override
    public List<User> readAll() throws DAOException {
        return null;
    }

    @Override
    public void update(User entity) throws DAOException {

    }

    @Override
    public void delete(Long id) throws DAOException {

    }
}
