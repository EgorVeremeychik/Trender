package com.trender.solr;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Egor.Veremeychik on 27.06.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:common-context.xml")
public class SolrTest extends Assert {

    @Autowired
    private Solr solr;

    @Test
    public void parseFromSolr() throws Exception {
        solr.parseFromSolr();
    }

    @Ignore
    @Test
    public void testParseToSolr() throws Exception {
        solr.parseToSolr();
    }

    @Test
    public void testGetLastId() throws Exception {
        System.out.println(solr.getLastId());
    }

    @Test
    public void testParse() throws Exception {
        solr.parse();
    }
}