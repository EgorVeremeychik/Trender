package com.trender.entity;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by Egor.Veremeychik on 13.06.2016.
 */

@Entity
@Table(name = "question", catalog = "trender")
@NamedQueries({
        @NamedQuery(name = "Question.readAll", query = "SELECT question FROM Question question"),
        @NamedQuery(name = "Question.readById", query = "SELECT question FROM Question question WHERE question.id = :id"),
        @NamedQuery(name = "Question.readByValue", query = "SELECT question FROM Question question WHERE question.value = :value")
})
public class Question {

    private static final long serialVersionUID = -89892834239281L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "value")
    private String value;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "question", fetch = FetchType.LAZY)
    private Set<Answer> ansvers;

    public Question() {
    }

    public Question(String value) {
        this.value = value;
    }

    public Question(Long id, String value) {
        this.id = id;
        this.value = value;
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

    public Set<Answer> getAnsvers() {
        return ansvers;
    }

    public void setAnsvers(Set<Answer> ansvers) {
        this.ansvers = ansvers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Question question = (Question) o;

        if (!id.equals(question.id)) return false;
        return value.equals(question.value);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", value='" + value + '\'' +
                '}';
    }
}
