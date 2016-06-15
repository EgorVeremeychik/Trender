package com.trender.service;

import com.trender.entity.Role;
import com.trender.service.exception.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by EgorVeremeychik on 14.06.2016.
 */

public interface RoleService {
    void create(Role role) throws ServiceException;

    void delete(Long id) throws ServiceException;

    void update(Role role) throws ServiceException;

    Role getById(Long id) throws ServiceException;

    List<Role> getAll() throws ServiceException;
}
