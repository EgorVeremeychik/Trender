package com.trender.solr.impl.relation;

import com.trender.entity.RelationKeywordAndCore;
import com.trender.solr.SolrRelationsService;
import com.trender.solr.impl.AbstractSolr;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.BinaryRequestWriter;
import org.apache.solr.client.solrj.impl.BinaryResponseParser;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by EgorVeremeychik on 04.08.2016.
 */
public class SolrRelationsServiceImpl extends AbstractSolr<RelationKeywordAndCore, Long> implements SolrRelationsService {

    private HttpSolrClient solrKeywordsClient;

    @PostConstruct
    private void init() {
        String serverURL = "http://localhost:8983/solr/";
        String coreName = "core_has_keywords";
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
    public List<RelationKeywordAndCore> searchFirstTen() {
        QueryResponse response = null;
        List<RelationKeywordAndCore> result = new ArrayList<>();
        try {
            SolrQuery query = new SolrQuery()
                    .setQuery("*:*")
                    .setStart(1)
                    .setRows(10);
            response = solrKeywordsClient.query(query);
            result = response.getBeans(RelationKeywordAndCore.class);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public RelationKeywordAndCore searchLast() {
        return null;
    }
}
