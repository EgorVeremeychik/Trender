package com.trender.solr.keywords.impl;

import com.trender.entity.Keyword;
import com.trender.solr.keywords.SolrKeywordsSearchService;
import com.trender.solr.keywords.SolrKeywordsUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Egor.Veremeychik on 02.08.2016.
 */

@Service
public class SolrKeywordsUpdateServiceImpl implements SolrKeywordsUpdateService {

    @Autowired
    private SolrKeywordsSearchService solrKeywordsSearchService;

    @Override
    public void updateById(Keyword keyword){

    }
}
