package com.trender.solr.relation.impl;

import com.trender.entity.Keyword;
import com.trender.solr.relation.SolrKeywordsSearchService;
import com.trender.solr.relation.SolrKeywordsUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Egor.Veremeychik on 02.08.2016.
 */

@Service
public class SolrKeywordsUpdateServiceImpl implements SolrKeywordsUpdateService {

    @Autowired
    private SolrKeywordsSearchService solrKeywordsSearchService;

    @Override
    public void updateKeyword(Keyword keyword){
       /* List<Integer> artistIds = new ArrayList<>();
        List<String> artistNames = new ArrayList<>();

        try {
            Integer albumId = song.getAlbum().getId();

            Integer artistId = song.getArtist().getId();
            String artistName = song.getArtist().getName();

            Map<String, List<Object>> idsAndNames = solrSearchService.findArtistIdAndNamesByAlbumId(albumId);

            List<Object> ids = idsAndNames.get("id");
            List<Object> names = idsAndNames.get("name");

            if (ids == null || names == null) {
                ids = new ArrayList<>();
                names = new ArrayList<>();
            }

            for (Object o : ids) {
                artistIds.add((Integer) o);
            }
            if (!artistIds.contains(artistId)) {
                artistIds.add(artistId);
            }

            for (Object o : names) {
                artistNames.add((String) o);
            }
            if (!artistNames.contains(artistName)) {
                artistNames.add(artistName);
            }

            GlobalService.Iface globalServiceClient = globalServiceConnection.openGlobalClient();
            globalServiceClient.updateAlbumToSolr(SolrAlbumDTO.toSolrAlbum(song.getAlbum(), artistIds, artistNames));
            globalServiceClient.commitToSolr();
        } catch (TException e) {
            LOGGER.error("Can not open global client", e);
            throw new ProcessingError(e);
        } finally {
            globalServiceConnection.closeGlobalClient();
        }*/
    }
}
