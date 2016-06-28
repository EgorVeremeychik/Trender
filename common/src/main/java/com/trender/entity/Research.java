package com.trender.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by Egor.Veremeychik on 27.06.2016.
 */

@Entity
@Table(name = "research", catalog = "trender")
@NamedQueries({
})
public class Research implements Serializable {

    private static final long serialVersionUID = -8237942231429623231L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "id")
    private Long id;
    @Column(name = "value", nullable = false, length = 100)
    private String value;

    @ManyToMany(mappedBy = "researches")
    private Set<User> users;


    public Research() {
    }

    public Research(String value) {
        this.value = value;
    }

    public Research(long id, String value) {
        this.id = id;
        this.value = value;
    }

    public Research(String value, Set<User> users) {
        this.value = value;
        this.users = users;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", value='" + value + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Research research = (Research) o;
        if (!id.equals(research.id)) return false;
        return value.equals(research.value);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }
}
