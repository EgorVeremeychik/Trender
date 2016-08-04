package com.trender.solr;

import com.trender.entity.Keyword;

import java.util.List;

/**
 * Created by Egor.Veremeychik on 03.08.2016.
 */
public interface SolrKeywordsService extends Solr<Keyword, Long> {

    List<Keyword> searchFirstTen();

    List<Keyword> searchByValue(String value);

    Keyword searchLast();

}
