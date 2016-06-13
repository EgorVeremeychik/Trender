package com.trender.dao.JPA;

import com.trender.dao.IRoleDAO;
import com.trender.dao.exception.DAOException;
import com.trender.entity.Role;
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
public class IRoleDAOImpl implements IRoleDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long create(Role entity) throws DAOException {
        return null;
    }

    @Override
    public Role read(Long id) throws DAOException {
        return null;
    }

    @Override
    public List<Role> readAll() throws DAOException {
        return null;
    }

    @Override
    public void update(Role entity) throws DAOException {

    }

    @Override
    public void delete(Long id) throws DAOException {

    }
}
