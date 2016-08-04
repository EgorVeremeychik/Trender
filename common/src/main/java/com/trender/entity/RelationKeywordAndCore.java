package com.trender.entity;

import org.apache.solr.client.solrj.beans.Field;

/**
 * Created by Egor.Veremeychik on 02.08.2016.
 */
public class RelationKeywordAndCore {

    @Field("id")
    private Long id;
    @Field("keyword_id")
    private Long keywordId;
    @Field("research_id")
    private Long researchId;

    public RelationKeywordAndCore() {
    }

    public RelationKeywordAndCore(Long id, Long keywordId, Long researchId) {
        this.id = id;
        this.keywordId = keywordId;
        this.researchId = researchId;
    }

    public RelationKeywordAndCore(Long researchId, Long keywordId) {
        this.researchId = researchId;
        this.keywordId = keywordId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getKeywordId() {
        return keywordId;
    }

    public void setKeywordId(Long keywordId) {
        this.keywordId = keywordId;
    }

    public Long getResearchId() {
        return researchId;
    }

    public void setResearchId(Long researchId) {
        this.researchId = researchId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RelationKeywordAndCore that = (RelationKeywordAndCore) o;

        if (!id.equals(that.id)) return false;
        if (!keywordId.equals(that.keywordId)) return false;
        return researchId.equals(that.researchId);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + keywordId.hashCode();
        result = 31 * result + researchId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "RelationKeywordAndCore{" +
                "id=" + id +
                ", keywordId=" + keywordId +
                ", researchId=" + researchId +
                '}';
    }
}
