package com.trender.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.BinaryRequestWriter;
import org.apache.solr.client.solrj.impl.BinaryResponseParser;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * Created by Egor.Veremeychik on 27.06.2016.
 */

@Service
public class Solra {

    private HttpSolrClient solrTrenderClient;

    @PostConstruct
    private void init() {
        String serverURL = "http://localhost:8983/solr/";
        String coreName = "keywords";

        HttpSolrClient trenderClient = new HttpSolrClient(serverURL + coreName);

        trenderClient.setRequestWriter(new BinaryRequestWriter());
        trenderClient.setConnectionTimeout(5000);
        trenderClient.setParser(new BinaryResponseParser());

        solrTrenderClient = trenderClient;
    }

    public void parseFromSolr() {
        SolrQuery query = new SolrQuery()
                .setQuery("*:*")
                .setFields("id", "keyword", "words", "symbols", "high_frequency", "exact_frequency")
                .setStart(1)
                .setRows(10);

        QueryResponse response = null;
        try {
            response = solrTrenderClient.query(query);
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SolrDocumentList results = response.getResults();
        for (int i = 0; i < results.size(); ++i) {
            System.out.println(results.get(i));
        }
    }

    public void parse() {
        SolrQuery query = new SolrQuery()
                .setQuery("keyword:doc1")
                .setFields("id", "keyword", "words", "symbols", "high_frequency", "exact_frequency")
                .setStart(0)
                .setRows(5);
        QueryResponse response = null;
        try {
            response = solrTrenderClient.query(query);
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SolrDocumentList results = response.getResults();
        for (int i = 0; i < results.size(); ++i) {
            System.out.println(results.get(i));
        }
    }

    public void parseToSolr() {
        try {
            Long id = getLastId()-1;
            System.out.println(id);
            SolrInputDocument doc1 = new SolrInputDocument();
            doc1.addField("id", id);
            doc1.addField("keyword", "doc2");
            doc1.addField("words", 2);
            doc1.addField("symbols", 2);
            doc1.addField("high_frequency", 2);
            doc1.addField("exact_frequency", 2);

            solrTrenderClient.add(doc1);
            solrTrenderClient.commit();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Long getLastId() {
        Long id = null;
        try {
            SolrQuery query = new SolrQuery()
                    .setQuery("*:*")
                    .setRows(0);
            QueryResponse response = null;
            response = solrTrenderClient.query(query);
            id = response.getResults().getNumFound();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return id;
    }
}
