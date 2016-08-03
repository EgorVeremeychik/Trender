package com.trender.solr.relation.impl;

import com.trender.entity.Keyword;
import com.trender.solr.relation.SolrKeywordsSearchService;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.BinaryRequestWriter;
import org.apache.solr.client.solrj.impl.BinaryResponseParser;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by EgorVeremeychik on 18.07.2016.
 */

@Service
public class SolrKeywordsSearchServiceImpl implements SolrKeywordsSearchService {

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
                    .setFields("id", "keyword", "words", "symbols", "high_frequency", "exact_frequency")
                    .setStart(1)
                    .setRows(10);
            response = solrKeywordsClient.query(query);
            result = getKeywordsFromSolrResponse(response);
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
                    .setFields("id", "keyword", "words", "symbols", "high_frequency", "exact_frequency")
                    .setStart(0)
                    .setRows(5);
            response = solrKeywordsClient.query(query);
            result = getKeywordsFromSolrResponse(response);
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Keyword searchLastKeyword() {
        Keyword result = null;
        QueryResponse response = null;
        try {
            SolrQuery query = new SolrQuery()
                    .setQuery("*:*")
                    .setSort("id", SolrQuery.ORDER.desc)
                    .setStart(1)
                    .setRows(1);
            response = solrKeywordsClient.query(query);
            result = getKeywordFromSolr(response.getResults().get(0));
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<Keyword> getKeywordsFromSolrResponse(QueryResponse response) {
        List<Keyword> result = new ArrayList<>();

        for (SolrDocument solrDocument : response.getResults()) {
            Keyword keyword = getKeywordFromSolr(solrDocument);
            result.add(keyword);
        }
        return result;
    }

    private Keyword getKeywordFromSolr(SolrDocument resultDoc) {
        if (resultDoc == null) {
            return null;
        }
        Long keywordId = Long.parseLong(String.valueOf(resultDoc.getFieldValue("id")));
        if (keywordId == null) {
            keywordId = (Long) resultDoc.getFieldValue("keyword.id");
        }
        if (keywordId != null) {
            Keyword keyword = new Keyword();
            keyword.setId(keywordId);
            keyword.setValue(String.valueOf(resultDoc.getFieldValue("keyword")));
            keyword.setWords(Integer.valueOf((String) resultDoc.getFieldValue("words")));
            keyword.setSymbols(Integer.valueOf((String) resultDoc.getFieldValue("symbols")));
            keyword.setHighFrequency(Long.valueOf((String) resultDoc.getFieldValue("high_frequency")));
            keyword.setExactFrequency(Long.valueOf((String) resultDoc.getFieldValue("exact_frequency")));
            return keyword;
        }
        return null;
    }
}
