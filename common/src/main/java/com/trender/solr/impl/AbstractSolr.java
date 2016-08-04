package com.trender.solr.impl;

import com.trender.solr.Solr;
import com.trender.solr.exception.SolrException;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.BinaryRequestWriter;
import org.apache.solr.client.solrj.impl.BinaryResponseParser;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;

/**
 * Created by Egor.Veremeychik on 03.08.2016.
 */
public abstract class AbstractSolr<E, PK> implements Solr<E,PK> {

    private Class<E> clazz;

    public AbstractSolr(){
        this.clazz = (Class<E>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

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
            solrKeywordsClient.addBean(entity);
            solrKeywordsClient.commit();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public E searchById(PK id) throws SolrException {
        QueryResponse response;
        E result = null;
        try {
            SolrQuery query = new SolrQuery()
                    .setQuery("id:" + id)
                    .setStart(0)
                    .setRows(1);
            response = solrKeywordsClient.query(query);
            result = response.getBeans(clazz).get(0);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void update(E entity) throws SolrException {

    }

    @Override
    public void delete(PK id) throws SolrException {
        try {
            solrKeywordsClient.deleteById((String) id);
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
