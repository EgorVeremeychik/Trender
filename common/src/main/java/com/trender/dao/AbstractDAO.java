package com.trender.dao;

import com.trender.dao.exception.DAOException;

import java.util.List;

/**
 * Created by EgorVeremeychik on 08.06.2016.
 */
public interface AbstractDAO<E, PK> {

    PK create(E entity) throws DAOException;

    E read(PK id) throws DAOException;

    List<E> readAll() throws DAOException;

    void update(E entity) throws DAOException;

    void delete(PK id) throws DAOException;

}
