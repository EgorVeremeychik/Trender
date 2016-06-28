package com.trender.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.BinaryRequestWriter;
import org.apache.solr.client.solrj.impl.BinaryResponseParser;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * Created by Egor.Veremeychik on 27.06.2016.
 */

@Service
public class Solr {

    private HttpSolrClient solrTrenderClient;

    @PostConstruct
    private void init() {
        String serverURL = "http://localhost:8983/solr/";
        String coreName = "trender";

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
                .setStart(1)
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
            Long id = getLastId();
            SolrInputDocument doc1 = new SolrInputDocument();
            doc1.addField("id", 45656755);
            doc1.addField("keyword", "doc1");
            doc1.addField("words", 1);
            doc1.addField("symbols", 1);
            doc1.addField("high_frequency", 1);
            doc1.addField("exact_frequency", 1);

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
                    .setSort("id", SolrQuery.ORDER.desc)
                    .setStart(1)
                    .setRows(1);
            QueryResponse response = null;
            response = solrTrenderClient.query(query);
            SolrDocumentList results = response.getResults();
            SolrDocument resultDoc = results.get(0);
            id = Long.valueOf((String) resultDoc.getFieldValue("id"));
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return id;
    }

}
