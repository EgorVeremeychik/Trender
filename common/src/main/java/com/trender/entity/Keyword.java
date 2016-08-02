package com.trender.entity;

/**
 * Created by EgorVeremeychik on 04.07.2016.
 */
public class Keyword {
    private Long id;
    private String value;
    private int words;
    private int symbols;
    private long highFrequency;
    private long exactFrequency;

    public Keyword() {
    }

    public Keyword(Long id, String value, int words, int symbols, long highFrequency, long exactFrequency) {
        this.id = id;
        this.value = value;
        this.words = words;
        this.symbols = symbols;
        this.highFrequency = highFrequency;
        this.exactFrequency = exactFrequency;
    }

    public Keyword(String value, int words, int symbols, long highFrequency, long exactFrequency) {
        this.value = value;
        this.words = words;
        this.symbols = symbols;
        this.highFrequency = highFrequency;
        this.exactFrequency = exactFrequency;
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

    public int getWords() {
        return words;
    }

    public void setWords(int words) {
        this.words = words;
    }

    public int getSymbols() {
        return symbols;
    }

    public void setSymbols(int symbols) {
        this.symbols = symbols;
    }

    public long getHighFrequency() {
        return highFrequency;
    }

    public void setHighFrequency(long highFrequency) {
        this.highFrequency = highFrequency;
    }

    public long getExactFrequency() {
        return exactFrequency;
    }

    public void setExactFrequency(long exactFrequency) {
        this.exactFrequency = exactFrequency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Keyword keyword = (Keyword) o;

        if (words != keyword.words) return false;
        if (symbols != keyword.symbols) return false;
        if (highFrequency != keyword.highFrequency) return false;
        if (exactFrequency != keyword.exactFrequency) return false;
        if (!id.equals(keyword.id)) return false;
        return value.equals(keyword.value);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + value.hashCode();
        result = 31 * result + words;
        result = 31 * result + symbols;
        result = 31 * result + (int) (highFrequency ^ (highFrequency >>> 32));
        result = 31 * result + (int) (exactFrequency ^ (exactFrequency >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Keyword{" +
                "id=" + id +
                ", value='" + value + '\'' +
                ", words=" + words +
                ", symbols=" + symbols +
                ", highFrequency=" + highFrequency +
                ", exactFrequency=" + exactFrequency +
                '}';
    }
}
