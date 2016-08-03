package com.trender.solr.relation;

import com.trender.entity.Keyword;

import java.util.List;

/**
 * Created by EgorVeremeychik on 18.07.2016.
 */
public interface SolrKeywordsSearchService {

    List<Keyword> searchFirstTen();

    List<Keyword> searchByValue(String value);

    Keyword searchLastKeyword();

}
