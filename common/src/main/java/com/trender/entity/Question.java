package com.trender.entity;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by Egor.Veremeychik on 13.06.2016.
 */

@Entity
@Table(name = "question", catalog = "trender")
@NamedQueries({})
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


}
