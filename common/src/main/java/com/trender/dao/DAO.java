package com.trender.dao;

import com.trender.dao.exception.DaoException;

import java.util.List;

/**
 * Created by EgorVeremeychik on 08.06.2016.
 */
public interface Dao<E, PK> {

    void create(E entity) throws DaoException;

    E read(PK id) throws DaoException;

    List<E> readAll() throws DaoException;

    void update(E entity) throws DaoException;

    void delete(PK id) throws DaoException;
}
