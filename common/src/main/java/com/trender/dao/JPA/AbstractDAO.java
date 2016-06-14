package com.trender.dao.jpa;

import com.trender.dao.Dao;
import com.trender.dao.exception.DaoException;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

/**
 * Created by Egor.Veremeychik on 14.06.2016.
 */
public abstract class AbstractDao<E,PK> implements Dao<E,PK> {

    private Class<E> clazz;

    public AbstractDao() {
    }

    public AbstractDao(Class<E> clazz) {
        this.clazz = clazz;
    }

    @PersistenceContext
    protected EntityManager entityManager;

    @Transactional
    @Override
    public void create(E entity) throws DaoException {
        entityManager.persist(entity);
    }

    @Override
    public E read(PK id) throws DaoException {
        return entityManager.find(clazz,id);
    }

    @Override
    public List<E> readAll() throws DaoException {
        CriteriaQuery criteriaQuery = entityManager.getCriteriaBuilder().createQuery();
        criteriaQuery.select(criteriaQuery.from(clazz));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Transactional
    @Override
    public void update(E entity) throws DaoException {
        entityManager.merge(entity);
    }

    @Transactional
    @Override
    public void delete(PK id) throws DaoException {
        E object = entityManager.find(clazz, id);
        if (object == null) {
            throw new DaoException();
        }
        entityManager.remove(object);
    }
}
