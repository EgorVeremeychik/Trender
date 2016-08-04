package com.trender.solr.impl.keywords;

import com.trender.entity.Keyword;
import com.trender.solr.SolrKeywordsService;
import com.trender.solr.impl.AbstractSolr;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.BinaryRequestWriter;
import org.apache.solr.client.solrj.impl.BinaryResponseParser;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Egor.Veremeychik on 03.08.2016.
 */

@Service
public class SolrKeywordsServiceImpl extends AbstractSolr<Keyword, Long> implements SolrKeywordsService {

    private HttpSolrClient solrKeywordsClient;

    @PostConstruct
    private void init() {
        String serverURL = "http://localhost:8983/solr/";
        String coreName = "keywords";
        HttpSolrClient keywordsClient = new HttpSolrClient(serverURL + coreName);
        keywordsClient.setRequestWriter(new BinaryRequestWriter());
        keywordsClient.setConnectionTimeout(5000);
        keywordsClient.setParser(new BinaryResponseParser());
        solrKeywordsClient = keywordsClient;
    }

    @PreDestroy
    private void destroy() {
        try {
            solrKeywordsClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Keyword> searchFirstTen() {
        QueryResponse response = null;
        List<Keyword> result = new ArrayList<>();
        try {
            SolrQuery query = new SolrQuery()
                    .setQuery("*:*")
                    .setStart(1)
                    .setRows(10);
            response = solrKeywordsClient.query(query);
            result = response.getBeans(Keyword.class);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Keyword> searchByValue(String value) {
        QueryResponse response = null;
        List<Keyword> result = new ArrayList<>();
        try {
            String resQuery = "keyword:\"" + value +"\"";
            SolrQuery query = new SolrQuery()
                    .setQuery(resQuery)
                    .setStart(0)
                    .setRows(5);
            response = solrKeywordsClient.query(query);
            result = response.getBeans(Keyword.class);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Keyword searchLast() {
        Keyword result = null;
        QueryResponse response = null;
        try {
            SolrQuery query = new SolrQuery()
                    .setQuery("*:*")
                    .setSort("id", SolrQuery.ORDER.desc)
                    .setStart(1)
                    .setRows(1);
            response = solrKeywordsClient.query(query);
            result = response.getBeans(Keyword.class).get(0);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
