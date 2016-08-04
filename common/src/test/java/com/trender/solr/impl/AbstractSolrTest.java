package com.trender.solr.impl;

import com.trender.entity.Keyword;
import com.trender.solr.impl.keywords.SolrKeywordsServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EgorVeremeychik on 04.08.2016.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:common-context.xml")
public class AbstractSolrTest extends Assert {

    @Autowired
    private SolrKeywordsServiceImpl solrKeywordsService;

    @Test
    public void testCreate() throws Exception {
        List<String> qwe = new ArrayList<>();
        qwe.add("value");
        Keyword keyword = new Keyword(57530L,qwe,1,5,1,1);
        solrKeywordsService.create(keyword);
    }

    @Test
    public void testSearchById() throws Exception {
        System.out.println(solrKeywordsService.searchById(57530L));
    }

    @Test
    public void testDelete() throws Exception {

    }
}