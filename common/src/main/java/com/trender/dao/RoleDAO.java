package com.trender.dao;

import com.trender.dao.exception.DaoException;
import com.trender.entity.Role;

import java.util.List;

/**
 * Created by Egor.Veremeychik on 14.06.2016.
 */
public interface RoleDao extends Dao<Role,Long> {

    List<Role> readRoleByUserId(Long id) throws DaoException;
}
