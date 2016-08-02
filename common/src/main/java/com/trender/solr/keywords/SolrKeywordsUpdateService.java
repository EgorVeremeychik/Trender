package com.trender.solr.keywords;

import com.trender.entity.Keyword;

/**
 * Created by Egor.Veremeychik on 02.08.2016.
 */
public interface SolrKeywordsUpdateService {

    void updateById(Keyword keyword);

}
