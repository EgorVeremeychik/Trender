package com.trender.dao.jpa;

import com.trender.dao.RoleDao;
import com.trender.dao.exception.DaoException;
import com.trender.entity.Role;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

import static com.trender.entity.Role.READ_ROLE_BY_NAME;
import static com.trender.entity.Role.READ_USER_ROLES;

/**
 * Created by EgorVeremeychik on 13.06.2016.
 */

@Repository
public class RoleDaoImpl extends AbstractDao<Role, Long> implements RoleDao {

    public RoleDaoImpl() {
        super(Role.class);
    }

    @Override
    public List<Role> readRoleByUserId(Long id) throws DaoException {
        TypedQuery<Role> query = entityManager.createNamedQuery(READ_USER_ROLES, Role.class);
        query.setParameter("id", id);
        List<Role> roles = query.getResultList();
        return roles;
    }

    @Override
    public Role readRoleByName(String name) throws DaoException {
        TypedQuery<Role> query = entityManager.createNamedQuery(READ_ROLE_BY_NAME, Role.class);
        query.setParameter("roleName", name);
        Role role = query.getSingleResult();
        return role;
    }
}
