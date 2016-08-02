package com.trender.solr.keywords.impl;

import com.trender.entity.Keyword;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by EgorVeremeychik on 02.08.2016.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:common-context.xml")
public class SolrKeywordsSearchServiceImplTest extends Assert {

    @Autowired
    private SolrKeywordsSearchServiceImpl solrKeywordsSearchServiceImpl;

    @Test
    public void testSearchById() throws Exception {
        for (Keyword keyword : solrKeywordsSearchServiceImpl.searchById()){
            System.out.println(keyword);
        }
    }
}