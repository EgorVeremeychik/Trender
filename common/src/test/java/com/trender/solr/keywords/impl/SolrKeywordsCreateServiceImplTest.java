package com.trender.solr.keywords.impl;

import com.trender.entity.Keyword;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by Egor.Veremeychik on 03.08.2016.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:common-context.xml")
public class SolrKeywordsCreateServiceImplTest extends Assert {

    @Autowired
    private SolrKeywordsCreateServiceImpl solrKeywordsCreateServiceImpl;

    @Ignore
    @Test
    public void createKeyword() throws Exception {
        Keyword keyword = new Keyword("value", 1, 5, 100L,100L);
        solrKeywordsCreateServiceImpl.createKeyword(keyword);
    }

}