package com.trender.solr.keywords.impl;

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
public class SolrKeywordsDeleteServiceImplTest extends Assert {

    @Autowired
    private SolrKeywordsDeleteServiceImpl solrKeywordsDeleteService;

    @Ignore
    @Test
    public void deleteKeywordById() throws Exception {
        solrKeywordsDeleteService.deleteKeywordById(57531L);
    }

}