package com.trender.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by Egor.Veremeychik on 13.06.2016.
 */

@Entity
@Table(name = "answer", catalog = "trender")
@NamedQueries({})
public class Answer implements Serializable {

    private static final long serialVersionUID = 2365237873623L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "id")
    private Long id;

    @Column(name = "value", nullable = false, length = 45)
    private String value;

    @Column(name = "question_id")
    private long questionId;

    @JoinTable(name = "user_has_answer", joinColumns = {
        @JoinColumn(name = "answer_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "user_id", referencedColumnName = "id")})
    @ManyToMany(targetEntity = User.class, mappedBy = "answers", fetch = FetchType.LAZY)
    private Set<User> users;

    @JoinColumn(name = "question_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Question question;

    public Answer() {
    }

    public Answer(String value) {
        this.value = value;
    }

    public Answer(Long id, String value) {
        this.value = value;
        this.id = id;
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

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", value='" + value + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Answer answer = (Answer) o;

        if (!id.equals(answer.id)) return false;
        return value.equals(answer.value);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }
}
