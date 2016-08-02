package com.trender.entity;

/**
 * Created by Egor.Veremeychik on 02.08.2016.
 */
public class RelationKeywordAndCore {

    private Long id;
    private Long keywordsId;
    private Long coreId;

    public RelationKeywordAndCore() {
    }

    public RelationKeywordAndCore(Long id, Long keywordsId, Long coreId) {
        this.id = id;
        this.keywordsId = keywordsId;
        this.coreId = coreId;
    }

    public RelationKeywordAndCore(Long coreId, Long keywordsId) {
        this.coreId = coreId;
        this.keywordsId = keywordsId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getKeywordsId() {
        return keywordsId;
    }

    public void setKeywordsId(Long keywordsId) {
        this.keywordsId = keywordsId;
    }

    public Long getCoreId() {
        return coreId;
    }

    public void setCoreId(Long coreId) {
        this.coreId = coreId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RelationKeywordAndCore that = (RelationKeywordAndCore) o;

        if (!id.equals(that.id)) return false;
        if (!keywordsId.equals(that.keywordsId)) return false;
        return coreId.equals(that.coreId);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + keywordsId.hashCode();
        result = 31 * result + coreId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "RelationKeywordAndCore{" +
                "id=" + id +
                ", keywordsId=" + keywordsId +
                ", coreId=" + coreId +
                '}';
    }
}
