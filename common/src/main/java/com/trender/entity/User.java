package com.trender.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by Egor.Veremeychik on 13.06.2016.
 */

@Entity
@Table(name = "user", catalog = "trender")
@NamedQueries({})
public class User implements Serializable {

    private static final long serialVersionUID = 823794255839623231L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "id")
    private Long id;
    @Column(name = "mail", nullable = false, length = 100)
    private String mail;
    @Column(name = "password", nullable = false, length = 100)
    private String password;
    @Column(name = "first_name", nullable = false, length = 30)
    private String firstName;
    @Column(name = "second_name", nullable = false, length = 30)
    private String secondName;

    @ManyToMany(targetEntity = Answer.class, mappedBy = "users", fetch = FetchType.LAZY)
    private Set<Answer> answers;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Role> roles;

    public User() {
    }

    public User(String password, String mail, String firstName, String secondName) {
        this.password = password;
        this.mail = mail;
        this.firstName = firstName;
        this.secondName = secondName;
    }

    public User(Long id, String mail, String password, String firstName, String secondName) {
        this.id = id;
        this.mail = mail;
        this.password = password;
        this.firstName = firstName;
        this.secondName = secondName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", mail='" + mail + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        if (!id.equals(user.id)) return false;
        if (!mail.equals(user.mail)) return false;
        if (!password.equals(user.password)) return false;
        if (!firstName.equals(user.firstName)) return false;
        return secondName.equals(user.secondName);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + mail.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + firstName.hashCode();
        result = 31 * result + secondName.hashCode();
        return result;
    }
}
