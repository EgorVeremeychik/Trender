package com.trender.solr.relation.impl;

import com.trender.entity.Keyword;
import com.trender.solr.relation.SolrKeywordsCreateService;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.BinaryRequestWriter;
import org.apache.solr.client.solrj.impl.BinaryResponseParser;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

/**
 * Created by Egor.Veremeychik on 03.08.2016.
 */

@Service
public class SolrKeywordsCreateServiceImpl implements SolrKeywordsCreateService {

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
    public void createKeyword(Keyword keyword) {
        try {
            Long id = getLastId();
            System.out.println(id);
            SolrInputDocument doc = new SolrInputDocument();
            doc.addField("id", id);
            doc.addField("keyword", keyword.getValue());
            doc.addField("words", keyword.getWords());
            doc.addField("symbols", keyword.getSymbols());
            doc.addField("high_frequency", keyword.getHighFrequency());
            doc.addField("exact_frequency", keyword.getExactFrequency());
            solrKeywordsClient.add(doc);
            solrKeywordsClient.commit();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Long getLastId() {
        Long id = null;
        try {
            SolrQuery query = new SolrQuery()
                    .setQuery("*:*")
                    .setRows(0);
            QueryResponse response = null;
            response = solrKeywordsClient.query(query);
            id = response.getResults().getNumFound();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return id;
    }

}
