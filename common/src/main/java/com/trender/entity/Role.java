package com.trender.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by Egor.Veremeychik on 13.06.2016.
 */

@Entity
@Table(name = "role", catalog = "trender")
@NamedQueries({
        @NamedQuery(name = "Role.readAll", query = "SELECT role FROM Role role"),
        @NamedQuery(name = "Role.readById", query = "SELECT role FROM Role role WHERE role.id = :id"),
        @NamedQuery(name = "Role.readUserRoles", query = "SELECT role FROM Role role join role.users users WHERE users.id = :id"),

})
public class Role implements Serializable {

    public static final String READ_USER_ROLES = "Role.readUserRoles";

    private static final long serialVersionUID = -23243423823982L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "role_name", nullable = false, length = 45)
    private String roleName;

    /*@JoinColumn(name = "question_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User us;*/

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "user_has_role", joinColumns = {
            @JoinColumn(name = "role_id", referencedColumnName = "id")}, inverseJoinColumns = {
            @JoinColumn(name = "user_id", referencedColumnName = "id")})
    private Set<User> users;

    public Role() {
    }

    public Role(String roleName) {
        this.roleName = roleName;
    }

    public Role(Long id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Long getRoleID() {
        return id;
    }

    public void setRoleID(Long roleID) {
        this.id = roleID;
    }

    /*public User getUser() {
        return us;
    }

    public void setUser(User us) {
        this.us = us;
    }*/

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", roleName='" + roleName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Role role = (Role) o;

        if (!id.equals(role.id)) return false;
        return roleName.equals(role.roleName);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + roleName.hashCode();
        return result;
    }
}
