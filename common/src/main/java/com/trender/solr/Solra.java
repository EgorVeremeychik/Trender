package com.trender.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.BinaryRequestWriter;
import org.apache.solr.client.solrj.impl.BinaryResponseParser;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
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
      String coreName = "trender";

      HttpSolrClient trenderClient = new HttpSolrClient(serverURL + coreName);

      trenderClient.setRequestWriter(new BinaryRequestWriter());
      trenderClient.setConnectionTimeout(5000);
      trenderClient.setParser(new BinaryResponseParser());

      solrTrenderClient = trenderClient;
   }

   public void parse(){
      SolrQuery query = (SolrQuery) new SolrQuery()
              .setQuery("id: [ 0 TO 100]")
              .setSort("id", SolrQuery.ORDER.asc)
              .setFields("id","keyword","words","symbols","high_frequency","exact_frequency")
              .setStart(0)
              .setRows(100);

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

}
