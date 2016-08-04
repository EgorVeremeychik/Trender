package com.trender.solr;

import com.trender.solr.exception.SolrException;

/**
 * Created by Egor.Veremeychik on 03.08.2016.
 */
public interface Solr<E,PK> {

    void create(E entity) throws SolrException;

    E searchById(PK id) throws SolrException;

   void update(E entity) throws SolrException;

    void delete(PK id) throws SolrException;

}
