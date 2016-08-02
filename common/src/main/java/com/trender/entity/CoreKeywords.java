package com.trender.entity;

/**
 * Created by EgorVeremeychik on 16.07.2016.
 */
public class CoreKeywords {

    private Long id;
    private Long id_keywords;
    private Long id_research;

    public CoreKeywords() {
    }

    public CoreKeywords(Long id_keywords, Long id_research) {
        this.id_keywords = id_keywords;
        this.id_research = id_research;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId_research() {
        return id_research;
    }

    public void setId_research(Long id_research) {
        this.id_research = id_research;
    }

    public Long getId_keywords() {
        return id_keywords;
    }

    public void setId_keywords(Long id_keywords) {
        this.id_keywords = id_keywords;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CoreKeywords that = (CoreKeywords) o;

        if (!id.equals(that.id)) return false;
        if (!id_keywords.equals(that.id_keywords)) return false;
        return id_research.equals(that.id_research);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + id_keywords.hashCode();
        result = 31 * result + id_research.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "CoreKeywords{" +
                "id=" + id +
                ", id_keywords=" + id_keywords +
                ", id_research=" + id_research +
                '}';
    }
}
