package com.trender.solr.keywords;

import com.trender.solr.Solr;
import com.trender.solr.exception.SolrException;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.BinaryRequestWriter;
import org.apache.solr.client.solrj.impl.BinaryResponseParser;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

/**
 * Created by Egor.Veremeychik on 03.08.2016.
 */
public abstract class AbstractSolrKeywords<E, PK> implements Solr<E,PK> {

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
    public void create(E entity) throws SolrException {
        try {
            Long id = getLastId();
            System.out.println(id);
            SolrInputDocument doc = new SolrInputDocument();
            doc.addField("id", id);
            doc.addField("keyword", entity.getValue());
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

    @Override
    public E read(PK id) throws SolrException {
        return null;
    }

    @Override
    public void update(E entity) throws SolrException {

    }

    @Override
    public void delete(PK id) throws SolrException {

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
