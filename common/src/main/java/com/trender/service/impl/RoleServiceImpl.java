package com.trender.service.impl;

import com.trender.dao.RoleDao;
import com.trender.dao.exception.DaoException;
import com.trender.entity.Role;
import com.trender.service.RoleService;
import com.trender.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Egor.Veremeychik on 14.06.2016.
 */

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    public RoleServiceImpl() {}

    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public void create(Role role) throws ServiceException {
        try {
            roleDao.create(role);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(Long id) throws ServiceException {
        try {
            roleDao.delete(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void update(Role role) throws ServiceException {
        try {
            roleDao.update(role);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Role getById(Long id) throws ServiceException {
        Role result = null;
        try {
            result = roleDao.read(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return result;
    }

    @Override
    public List<Role> getAll() throws ServiceException {
        List<Role> result = null;
        try {
            result = roleDao.readAll();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return result;
    }
}
