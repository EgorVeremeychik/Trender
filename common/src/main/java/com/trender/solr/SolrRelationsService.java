package com.trender.solr;

import com.trender.entity.RelationKeywordAndCore;

import java.util.List;

/**
 * Created by Egor.Veremeychik on 03.08.2016.
 */
public interface SolrRelationsService extends Solr<RelationKeywordAndCore, Long> {

    List<RelationKeywordAndCore> searchFirstTen();

    RelationKeywordAndCore searchLast();

}
