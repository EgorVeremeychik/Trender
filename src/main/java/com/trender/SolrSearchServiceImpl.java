package com.trender;

/**
 * Created by Egor.Veremeychik on 27.06.2016.
 */

        import com.nda.mss.dao.CoverDao;
        import com.nda.mss.dao.GenreDao;
        import com.nda.mss.dto.*;
        import com.nda.mss.entity.Cover;
        import com.nda.mss.entity.Genre;
        import com.nda.mss.entity.Settings;
        import com.nda.mss.rating.RatingPeriod;
        import com.nda.mss.service.CatalogService;
        import com.nda.mss.service.SettingsService;
        import com.nda.mss.service.SolrSearchService;
        import com.nda.mss.utils.DTOConverter;
        import com.nda.mss.utils.StringCleanUtils;
        import org.apache.solr.client.solrj.SolrClient;
        import org.apache.solr.client.solrj.SolrQuery;
        import org.apache.solr.client.solrj.SolrServerException;
        import org.apache.solr.client.solrj.impl.BinaryRequestWriter;
        import org.apache.solr.client.solrj.impl.BinaryResponseParser;
        import org.apache.solr.client.solrj.impl.HttpSolrClient;
        import org.apache.solr.client.solrj.response.*;
        import org.apache.solr.client.solrj.util.ClientUtils;
        import org.apache.solr.common.SolrDocument;
        import org.apache.solr.common.SolrDocumentList;
        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.beans.factory.annotation.Value;
        import org.springframework.context.annotation.Lazy;
        import org.springframework.stereotype.Service;

        import javax.annotation.PostConstruct;
        import javax.annotation.PreDestroy;
        import java.io.IOException;
        import java.sql.Timestamp;
        import java.time.LocalDateTime;
        import java.util.*;
        import java.util.stream.Collectors;

/**
 * @author Alexey.Koyro
 */
@Lazy(false)
@Service
public class SolrSearchServiceImpl implements SolrSearchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SolrSearchServiceImpl.class);

    private static final String SOLR_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final String utf8 = "UTF-8";
    private static final String SOLR_REQUEST_HANDLER_SELECT = "select";
    private static final String SOLR_REQUEST_HANDLER_SUGGEST = "select_suggest";

    //TODO if you change this variable, you have to change SOLO_ALBUM_AUTHORSHIP_PARAM_VALUE in AlbumsByArtist too.
    private static final String SOLO_ALBUM = "solo";

    // Define search types:
    @Value("${Solr.QueryFieldsByArtist}")
    private String solrQueryFieldsByArtist;

    @Value("${Solr.QueryFieldsByAlbum}")
    private String solrQueryFieldByAlbum;

    @Value("${Solr.QueryFieldsBySong}")
    private String solrQueryFieldsBySong;

    @Value("${Solr.FuzzyCoeff}")
    private String solrFuzzyCoeff;

    @Autowired
    private SettingsService settingsService;

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private CoverDao coverDao;

    @Autowired
    private GenreDao genreDao;

    //private SolrServer solrSongClient;

    private HttpSolrClient solrSongClient;

    private HttpSolrClient albumSolrClient;

    private HttpSolrClient artistSolrClient;

    @PostConstruct
    private void init() {
        String serverURL = settingsService.getProperty(Settings.SOLR_SERVER_URL);
        String songCoreName = settingsService.getProperty(Settings.SOLR_SONG_CORE_NAME);
        String artistCoreName = settingsService.getProperty(Settings.SOLR_ARTIST_CORE_NAME);
        String albumCoreName = settingsService.getProperty(Settings.SOLR_ALBUM_CORE_NAME);

        // HttpSolrServer server = new HttpSolrServer(serverURL);
        HttpSolrClient songClient = new HttpSolrClient(serverURL + songCoreName);

        songClient.setRequestWriter(new BinaryRequestWriter());
        songClient.setConnectionTimeout(5000);
        songClient.setParser(new BinaryResponseParser());

        solrSongClient = songClient;


        HttpSolrClient artistClient = new HttpSolrClient(serverURL + artistCoreName);

        artistClient.setRequestWriter(new BinaryRequestWriter());
        artistClient.setConnectionTimeout(5000);
        artistClient.setParser(new BinaryResponseParser());

        artistSolrClient = artistClient;


        HttpSolrClient albumClient = new HttpSolrClient(serverURL + albumCoreName);

        albumClient.setRequestWriter(new BinaryRequestWriter());
        albumClient.setConnectionTimeout(5000);
        albumClient.setParser(new BinaryResponseParser());

        albumSolrClient = albumClient;


        /*
        String serverType = settingsService.getProperty(Settings.SOLR_TYPE);
        if (serverType.contains("Embed")) {
            String solrLocalPath = this.getProperty(Settings.SOLR_LOCAL_HOME_PATH);
            System.setProperty("solr.solr.home", solrLocalPath);
            File file = new File(solrLocalPath + "/solr.xml");
            org.apache.solr.core.CoreContainer coreContainer = org.apache.solr.core.CoreContainer.createAndLoad(solrLocalPath, file);
            String[] path = serverURL.split("/");
            String coreName = path[path.length-1];
            solrSongClient = new org.apache.solr.client.solrj.embedded.EmbeddedSolrServer(coreContainer, coreName);

        } else {
        }
        */
    }

    @PreDestroy
    private void destroy() {
        try {
            solrSongClient.close();
            albumSolrClient.close();
            artistSolrClient.close();
        } catch (IOException e) {
            LOGGER.error("Can't close solr client.", e);
        }
    }

    @Override
    public SolrClient getSolrSongClient() {
        return solrSongClient;
    }

    @Override
    public SolrClient getAlbumSolrClient() {
        return albumSolrClient;
    }

    @Override
    public SolrClient getArtistSolrClient() {
        return artistSolrClient;
    }

    @Override
    public Set<String> autoCompleteSearch(String typeWord, int maxResults) {
        Set<String> searchResult = new HashSet<>();
        if (StringCleanUtils.stringIsNullOrEmpty(typeWord)) {
            return searchResult;
        }

        try {
            String resQuery = spelllcheckForAutocomplete(typeWord, SOLR_REQUEST_HANDLER_SUGGEST, solrSongClient);
            resQuery = resQuery.toLowerCase();

            SolrQuery query = new SolrQuery("*:*")
                    .setFacet(true)
                    .setFacetPrefix(resQuery)
                    .addFacetField("suggest")
                    .setFacetMinCount(1)
                    .setFacetLimit(maxResults)
                    .setFields("id")
                    .setParam("facet.method", "enum");

            QueryResponse response = null;
            response = solrSongClient.query(query);
            List<FacetField> facets = response.getFacetFields();
            FacetField ff = facets.get(0);
            List<FacetField.Count> values = ff.getValues();

            for (FacetField.Count val : values) {
                searchResult.add(val.getName());
            }

        } catch (SolrServerException e) {
            LOGGER.error("Solr server error occur", e);
        } catch (IOException e) {
            LOGGER.error("Solr server IO error occur", e);

        }
        return searchResult;
    }

    private String prepareToCollation(String typedWords) {
        StringBuilder checkWordsBuilder = new StringBuilder();
        String[] words = typedWords.split(" ");

        if (words.length > 1) {
            for (int i = 0; i < words.length - 1; i++) {
                checkWordsBuilder.append(words[i])
                        .append(" ");
            }
        }

        return checkWordsBuilder.toString().trim();
    }

    private String findAppendToEndStringInSpellcheck(String typedWords) {
        String[] words = typedWords.split(" ");
        if (words.length != 0) {
            return words[words.length - 1];
        }

        return "";
    }

    private String spelllcheckForAutocomplete(String typedWords, String requestHandler, SolrClient solrClient) {
        String appendToEnd = findAppendToEndStringInSpellcheck(typedWords);
        String collationString = prepareToCollation(typedWords);
        String collationResult = "";

        try {
            collationResult = spellcheck(collationString, requestHandler, solrClient);
        } catch (SolrServerException e) {
            LOGGER.error("Solr server error occur", e);
            return typedWords;
        } catch (IOException e) {
            LOGGER.error("Solr server IO error occur", e);
            return typedWords;
        }

        return collationResult.isEmpty() ? typedWords : collationResult + " " + appendToEnd;
    }

    private String spellcheck(String inputString, String requestHandler, SolrClient solrClient) throws IOException, SolrServerException {
        String checkedString = "";

        if (inputString == null) {
            return checkedString;
        }

        //inputString = URLEncoder.encode(inputString, utf8);

        SolrQuery query = new SolrQuery()
                .setRequestHandler("/" + requestHandler)
                .setParam("spellcheck.q", inputString);

        QueryResponse response = solrClient.query(query);
        SpellCheckResponse spellCheckResponse = response.getSpellCheckResponse();
        for (SpellCheckResponse.Collation collation : spellCheckResponse.getCollatedResults()) {
            for (SpellCheckResponse.Correction correction : collation.getMisspellingsAndCorrections()) {
                if (!correction.getCorrection().isEmpty()) {
                    checkedString = correction.getCorrection();
                }
            }
        }
        return checkedString;
    }

    //https://127.0.0.1/solr/collection1/select?q=*:*&facet.limit=14&facet.field=artist.name&fl=id&facet.prefix=mado&facet.mincount=2&rows=0&facet=true
    @Override
    public Set<String> findArtistsNamesAutoComplete(String name, int maxResults) {
        // startIndex parameter is ignored!!!
        Set<String> set = new HashSet<>();
        try {
            if (StringCleanUtils.stringIsNullOrEmpty(name)) {
                return set;
            }

            String resQuery = spelllcheckForAutocomplete(name.toLowerCase(), SOLR_REQUEST_HANDLER_SELECT, artistSolrClient);
            SolrQuery query = new SolrQuery("*:*")
                    .setFacet(true)
                    .setFacetPrefix(resQuery)
                    .addFacetField("artist.name")
                    .setFacetMinCount(1)
                    .setFacetLimit(maxResults)
                    .setFields("id")
                    .setParam("facet.method", "enum");

            QueryResponse response = solrSongClient.query(query);
            List<FacetField> facets = response.getFacetFields();
            FacetField ff = facets.get(0);
            List<FacetField.Count> values = ff.getValues();

            for (FacetField.Count val : values) {
                set.add(val.getName());
            }
        } catch (SolrServerException e) {
            LOGGER.error("Solr server error occur", e);
        } catch (Exception e) {
            LOGGER.error("Exception", e);
        }
        return set;
    }

    //https://127.0.0.1/solr/collection1/select?q=*:*&facet.limit=14&facet.field=album.name&fl=id&facet.prefix=love&facet.mincount=2&fq=artist.name:%22Madonna%22&rows=0&facet=true
    @Override
    public Set<String> findAlbumNamesAutoComplete(String name, String fullArtistName, int maxResults) {
        Set<String> set = new HashSet<>();
        try {
            if (StringCleanUtils.stringIsNullOrEmpty(name)) {
                return set;
            }

            String resQuery = spelllcheckForAutocomplete(name.toLowerCase(), SOLR_REQUEST_HANDLER_SELECT, albumSolrClient);
            SolrQuery query = new SolrQuery("*:*")
                    .setFacet(true)
                    .setFacetPrefix(resQuery)
                    .addFacetField("album.name")
                    .setFacetMinCount(1)
                    .setFacetLimit(maxResults)
                    .setFields("id")
                    .setParam("facet.method", "enum");

            if (!StringCleanUtils.stringIsNullOrEmpty(fullArtistName)) {
                String fullArtistNameSuggestion = spellcheck(fullArtistName, SOLR_REQUEST_HANDLER_SELECT, artistSolrClient);
                if (!fullArtistNameSuggestion.isEmpty()) {
                    fullArtistName = fullArtistNameSuggestion;
                }
                fullArtistName = ClientUtils.escapeQueryChars(fullArtistName);
                query = query.addFilterQuery("artist.name:\"" + fullArtistName + "\"");
            }

            QueryResponse response = solrSongClient.query(query);
            List<FacetField> facets = response.getFacetFields();
            FacetField ff = facets.get(0);
            List<FacetField.Count> values = ff.getValues();

            //for (FacetField.Count val:values) {
            //    set.add(val.getName());
            //}
            set.addAll(values.stream().map(FacetField.Count::getName).distinct().collect(Collectors.toList()));

        } catch (SolrServerException e) {
            LOGGER.error("Solr server error occur", e);
        } catch (Exception e) {
            LOGGER.error("Exception", e);
        }
        return set;
    }

    //https://127.0.0.1/solr/collection1/select?q=*:*&facet.limit=14&facet.field=album.name&fl=id&facet.prefix=love&facet.mincount=2&fq=artist.name:%22Depeche%20Mode%22%20AND%20album.name:%22love%20in%20itself%202%22&rows=0&facet=true
    @Override
    public Set<String> findSongNamesAutoComplete(String name, String fullArtistName, String fullAlbumName, int maxResults) {
        Set<String> set = new HashSet<>();
        try {
            if (StringCleanUtils.stringIsNullOrEmpty(name)) {
                return set;
            }

            String resQuery = spelllcheckForAutocomplete(name.toLowerCase(), SOLR_REQUEST_HANDLER_SELECT, solrSongClient);
            SolrQuery query = new SolrQuery("*:*")
                    .setFacet(true)
                    .setFacetPrefix(resQuery)
                    .addFacetField("song.name")
                    .setFacetMinCount(1)
                    .setFacetLimit(maxResults)
                    .setFields("id")
                    .setParam("facet.method", "enum");

            String strFilterQuery = "";
            if (!StringCleanUtils.stringIsNullOrEmpty(fullArtistName)) {
                String fullArtistNameSuggestion = spellcheck(fullArtistName, SOLR_REQUEST_HANDLER_SELECT, artistSolrClient);
                if (!fullArtistNameSuggestion.isEmpty()) {
                    fullArtistName = fullArtistNameSuggestion;
                }
                fullArtistName = ClientUtils.escapeQueryChars(fullArtistName);
                strFilterQuery = "artist.name:\"" + fullArtistName + "\"";
            }

            if (!StringCleanUtils.stringIsNullOrEmpty(fullAlbumName)) {
                String fullAlbumNameSuggestion = spellcheck(fullAlbumName, SOLR_REQUEST_HANDLER_SELECT, albumSolrClient);
                if (!fullAlbumNameSuggestion.isEmpty()) {
                    fullAlbumName = fullAlbumNameSuggestion;
                }
                fullAlbumName = ClientUtils.escapeQueryChars(fullAlbumName);
                if (StringCleanUtils.stringIsNullOrEmpty(strFilterQuery)) {
                    strFilterQuery = "album.name:\"" + fullAlbumName + "\"";
                } else {
                    strFilterQuery = strFilterQuery.concat("AND album.name:\"" + fullAlbumName + "\"");
                }
            }

            if (!StringCleanUtils.stringIsNullOrEmpty(strFilterQuery)) {
                query = query.addFilterQuery(strFilterQuery);
            }

            QueryResponse response = solrSongClient.query(query);
            List<FacetField> facets = response.getFacetFields();
            FacetField ff = facets.get(0);
            List<FacetField.Count> values = ff.getValues();

            for (FacetField.Count val : values) {
                set.add(val.getName());
            }

            //set.addAll(values.stream().map(FacetField.Count::getName).distinct().collect(Collectors.toList()));

        } catch (SolrServerException e) {
            LOGGER.error("Solr server error occur", e);
        } catch (Exception e) {
            LOGGER.error("Exception", e);
        }
        return set;
    }

    @Override
    public Set<ArtistDTO> findArtistsByName(String name, int startIndex, int maxResults) {
        SortedSet<ArtistDTO> set = new TreeSet<ArtistDTO>();
        // Set<ArtistDTO> set = new HashSet<>();
        try {
            if (StringCleanUtils.stringIsNullOrEmpty(name)) {
                return set;
            }
            name = ClientUtils.escapeQueryChars(name);

            String resQuery = "artist.name:\"" + name + "\"";
            SolrQuery query = new SolrQuery().setQuery(resQuery)
                    .setSort("artist.rating", SolrQuery.ORDER.desc);

            QueryResponse response = artistSolrClient.query(query);
            SolrDocumentList solrlist = response.getResults();
            for (SolrDocument solrDocument : solrlist) {
                ArtistDTO artistDTO = getArtistDTOFromSolr(solrDocument);
                if (artistDTO != null) {
                    set.add(artistDTO);
                }
            }
        } catch (SolrServerException e) {
            LOGGER.error("Solr server error occur", e);
        } catch (Exception e) {
            LOGGER.error("Exception", e);
        }
        return set;
    }

    private ArtistDTO getArtistDTOFromSolr(SolrDocument resultDoc) {
        if (resultDoc == null) {
            return null;
        }
        Integer artistId = (Integer) resultDoc.getFieldValue("id");
        if (artistId != null) {
            ArtistDTO artist = new ArtistDTO();
            artist.setId(artistId);
            artist.setName((String) resultDoc.getFieldValue("artist.name"));
            Integer coverId = (Integer) resultDoc.getFieldValue("artist.cover_id");
            if (coverId != null) {
                Cover cover = coverDao.find(coverId);
                if (cover != null) {
                    CoverDTO coverDTO = new CoverDTO();
                    coverDTO.setId(cover.getId());
                    coverDTO.setOwnerId(artistId);
                    coverDTO.setPreviewCoverPath(catalogService.getCoverURL(cover, Cover.Size.MEDIUM));
                    coverDTO.setCoverPath(catalogService.getCoverURL(cover, Cover.Size.BIG));
                    artist.setCover(coverDTO);
                }
            }
            return artist;
        }
        return null;
    }

    @Override
    public Set<ArtistDTO> findArtistsByFirstCharacter(String firstLetter, int startIndex, int maxResults) {
        SortedSet<ArtistDTO> set = new TreeSet<ArtistDTO>();
        try {
            if (StringCleanUtils.stringIsNullOrEmpty(firstLetter)) {
                return set;
            }

            String resQuery = "artist.name:" + firstLetter + "*";
            SolrQuery query = new SolrQuery().setQuery(resQuery)
                    .setStart(startIndex)
                    .setRows(maxResults)
                    .setSort("artist.name", SolrQuery.ORDER.asc);

            QueryResponse response = artistSolrClient.query(query);

            SolrDocumentList solrlist = response.getResults();
            for (SolrDocument solrDocument : solrlist) {
                ArtistDTO artistDTO = getArtistDTOFromSolr(solrDocument);
                if (artistDTO != null) {
                    set.add(artistDTO);
                }
            }
        } catch (SolrServerException e) {
            LOGGER.error("Solr server error occur", e);
        } catch (Exception e) {
            LOGGER.error("Exception", e);
        }
        return set;
    }

    @Override
    public int findArtistsCountByFirstCharacter(String firstLetter) {
        int i = 0;
        try {
            if (StringCleanUtils.stringIsNullOrEmpty(firstLetter)) {
                return i;
            }

            String resQuery = "artist.name:" + firstLetter + "*";
            SolrQuery query = new SolrQuery().setQuery(resQuery)
                    .setFields("id", "artist.name")
                    .setRows(0);

            QueryResponse response = artistSolrClient.query(query);

            return (int) response.getResults().getNumFound();

        } catch (SolrServerException e) {
            LOGGER.error("Solr server error occur", e);
        } catch (Exception e) {
            LOGGER.error("Exception", e);
        }
        return i;
    }

    @Override
    public int findArtistsCountByName(String name) {
        int i = 0;
        try {
            if (StringCleanUtils.stringIsNullOrEmpty(name)) {
                return 0;
            }
            name = ClientUtils.escapeQueryChars(name);

            String resQuery = "artist.name:\"" + name + "\"";
            SolrQuery query = new SolrQuery().setQuery(resQuery)
                    .setFields("id", "artist.name")
                    .setRows(0);

            QueryResponse response = artistSolrClient.query(query);

            return (int) response.getResults().getNumFound();

        } catch (SolrServerException e) {
            LOGGER.error("Solr server error occur", e);
        } catch (Exception e) {
            LOGGER.error("Exception", e);
        }
        return i;
    }

    @Override
    public int findAlbumsCountName(String albumName) {
        int i = 0;
        try {
            if (StringCleanUtils.stringIsNullOrEmpty(albumName)) {
                return 0;
            }

            String resQuery = "album.name:\"" + albumName + "\"";
            SolrQuery query = new SolrQuery().setQuery(resQuery)
                    .setFields("id", "album.name");

            QueryResponse response = albumSolrClient.query(query);
            return (int) response.getResults().getNumFound();
        } catch (SolrServerException e) {
            LOGGER.error("Solr server error occur", e);
        } catch (Exception e) {
            LOGGER.error("Exception", e);
        }
        return i;
    }

    @Override
    public int findAlbumsCountByFirstCharacters(String firstCharacter) {
        int i = 0;
        try {
            if (StringCleanUtils.stringIsNullOrEmpty(firstCharacter)) {
                return 0;
            }

            String resQuery = "album.name:" + firstCharacter + "*";
            SolrQuery query = new SolrQuery().setQuery(resQuery)
                    .setFields("id", "album.name")
                    .setRows(0);

            QueryResponse response = albumSolrClient.query(query);
            return (int) response.getResults().getNumFound();
        } catch (SolrServerException e) {
            LOGGER.error("Solr server error occur", e);
        } catch (Exception e) {
            LOGGER.error("Exception", e);
        }
        return i;
    }

    @Override
    public int findAlbumsCountByReleaseDateRange(LocalDateTime start, LocalDateTime end) {
        int i = 0;
        try {
            String from = "*";
            String to = "*";
            if (start != null) {
                from = start.format(java.time.format.DateTimeFormatter.ofPattern(SOLR_DATETIME_FORMAT));
            }
            if (end != null) {
                to = end.format(java.time.format.DateTimeFormatter.ofPattern(SOLR_DATETIME_FORMAT));
            }

            String resQuery = "album.release_date:[" + from + " TO " + to + "]";
            SolrQuery query = new SolrQuery().setQuery(resQuery)
                    .setFields("id")
                    .setRows(0);

            QueryResponse response = albumSolrClient.query(query);
            return (int) response.getResults().getNumFound();
        } catch (SolrServerException e) {
            LOGGER.error("Solr server error occur", e);
        } catch (Exception e) {
            LOGGER.error("Exception", e);
        }
        return i;
    }

    @Override
    public int findAlbumsCountByGenre(List<Integer> genreIdList) {
        int i = 0;
        StringBuilder idListStringBuilder = new StringBuilder("(");
        for (Integer genreId : genreIdList) {
            idListStringBuilder.append(genreId);
            idListStringBuilder.append(" ");
        }
        idListStringBuilder.append(")");

        try {

            String resQuery = "album.genre_id:" + idListStringBuilder.toString();
            SolrQuery query = new SolrQuery().setQuery(resQuery)
                    .setFields("id")
                    .setRows(0);

            QueryResponse response = albumSolrClient.query(query);
            return (int) response.getResults().getNumFound();

        } catch (SolrServerException e) {
            LOGGER.error("Solr server error occur", e);
        } catch (Exception e) {
            LOGGER.error("Exception", e);
        }
        return i;
    }

    @Override
    public Integer getAlbumsCountByArtist(int artistId, String authorship) {
        int result = 0;
        try {
            int limitItems = Integer.parseInt(settingsService.getProperty(Settings.SONGS_TABLE_LIMIT));
            String resQuery = "artist.id:" + artistId;
            if (authorship.equals(SOLO_ALBUM)) {
                resQuery = "(" + resQuery + ") AND album.available_artist_count:1";
            }

            SolrQuery query = new SolrQuery().setQuery(resQuery)
                    .setRows(limitItems);

            QueryResponse response = albumSolrClient.query(query);
            return (int) response.getResults().getNumFound();
        } catch (SolrServerException e) {
            LOGGER.error("Solr server error occur", e);
        } catch (Exception e) {
            LOGGER.error("Exception", e);
        }
        return result;
    }

    @Override
    public List<AlbumDTO> findAlbumsByArtistId(int artistId, int startIndex, int size, String authorship) {
        List<AlbumDTO> albums = new LinkedList<>();
        try {
            StringBuilder resQueryBuilder = new StringBuilder();
            /*for (Integer albumId : findAlbumIdByArtistId(artistId)) {
                resQueryBuilder.append(" OR id:")
                        .append(albumId);
            }*/


            String resQuery = "artist.id:" + artistId;
            if (authorship.equals(SOLO_ALBUM)) {
                resQuery = resQuery + " AND album.available_artist_count:1";
            }
            SolrQuery query = new SolrQuery().setQuery(resQuery)
                    .setSort("album.name", SolrQuery.ORDER.asc)
                    .setStart(startIndex)
                    .setRows(size);

            QueryResponse response = albumSolrClient.query(query);
            albums = getAlbumsFromSolrResponse(response);

        } catch (SolrServerException e) {
            LOGGER.error("Solr server error occur", e);
        } catch (Exception e) {
            LOGGER.error("Exception", e);
        }
        return albums;
    }

    private List<Integer> findAlbumIdByArtistId(Integer artistId) {
        List<Integer> albumIdList = new ArrayList<>();
        try {
            String resQuery = "artist.id:" + artistId;
            SolrQuery query = new SolrQuery().setQuery(resQuery)
                    .setFields("album.id")
                    .setParam("group", "true")
                    .setParam("group.field", "album.id")
                    .setParam("group.main", "true")
                    .setRows(Integer.MAX_VALUE);

            QueryResponse response = solrSongClient.query(query);

            for (SolrDocument solrDocument : response.getResults()) {
                albumIdList.add((Integer) solrDocument.getFieldValue("album.id"));
            }
        } catch (SolrServerException e) {
            LOGGER.error("Solr server error occur", e);
        } catch (Exception e) {
            LOGGER.error("Exception", e);
        }
        return albumIdList;
    }


    private List<AlbumDTO> getAlbumsFromSolrResponseGrouped(QueryResponse response) {
        GroupResponse groupResponse = response.getGroupResponse();
        List<Object[]> albumsData = new ArrayList<>();
        List<AlbumDTO> albums = new ArrayList<>();

        for (GroupCommand gc : groupResponse.getValues()) {
            for (Group g : gc.getValues()) {
                for (SolrDocument solrDocument : g.getResult()) {
                    AlbumDTO album = getAlbumDTOFromSolr(solrDocument);

                    if (album != null) {
                        Object[] albumData = new Object[3];
                        albumData[0] = album;
                        albumData[1] = solrDocument.getFieldValue("album.cover_id");
                        albumData[2] = solrDocument.getFieldValue("album.genre_id");
                        albumsData.add(albumData);
                    }
                }
            }
        }

        if (albumsData.isEmpty()) {
            return albums;
        }

        fillAlbumsWithCovers(albumsData);
        //commented for performance purposes. Uncomment if genres are needed
        //fillAlbumsWithGenres(albumsData);

        for (Object[] albumData : albumsData) {
            albums.add((AlbumDTO) albumData[0]);
        }
        return albums;
    }

    private List<AlbumDTO> getAlbumsFromSolrResponse(QueryResponse response) {
        List<Object[]> albumsData = new ArrayList<>();
        List<AlbumDTO> albums = new ArrayList<>();

        for (SolrDocument solrDocument : response.getResults()) {
            AlbumDTO album = getAlbumDTOFromSolr(solrDocument);

            if (album != null) {
                Object[] albumData = new Object[3];
                albumData[0] = album;
                albumData[1] = solrDocument.getFieldValue("album.cover_id");
                albumData[2] = solrDocument.getFieldValue("album.genre_id");
                albumsData.add(albumData);
            }
        }

        if (albumsData.isEmpty()) {
            return albums;
        }

        fillAlbumsWithCovers(albumsData);
        //commented for performance purposes. Uncomment if genres are needed
        //fillAlbumsWithGenres(albumsData);

        for (Object[] albumData : albumsData) {
            albums.add((AlbumDTO) albumData[0]);
        }
        return albums;
    }

    private AlbumDTO getAlbumDTOFromSolr(SolrDocument resultDoc) {
        if (resultDoc == null) {
            return null;
        }
        Integer albumId = (Integer) resultDoc.getFieldValue("id");
        if (albumId == null) {
            albumId = (Integer) resultDoc.getFieldValue("album.id");
        }
        if (albumId != null) {
            AlbumDTO album = new AlbumDTO();
            album.setId(albumId);
            album.setName((String) resultDoc.getFieldValue("album.name"));
            album.setArtistName(((List<String>) resultDoc.getFieldValue("artist.name")).get(0));

            Date releaseDate = (Date) resultDoc.getFieldValue("album.release_date");
            if (releaseDate != null) {
                album.setReleaseDate(Timestamp.from(releaseDate.toInstant()));
            }
            return album;
        }
        return null;
    }

    private void fillAlbumsWithGenres(List<Object[]> albums) {
        Set<Integer> genreIds = new HashSet<Integer>();

        for (Object[] album : albums) {
            genreIds.add((Integer) album[2]);
        }
        List<Genre> genres = genreDao.find(genreIds);

        for (Object[] album : albums) {
            AlbumDTO albumDTO = (AlbumDTO) album[0];
            for (Genre genre : genres) {
                if (genre.getId().equals(album[2])) {
                    Genre clonedGenre = new Genre();
                    clonedGenre.setListening(0.0);
                    clonedGenre.setId(genre.getId());
                    clonedGenre.setName(genre.getName());
                    albumDTO.setGenre(DTOConverter.convert(clonedGenre));
                    break;
                }
            }
        }
    }

    private void fillAlbumsWithCovers(List<Object[]> albums) {
        Set<Integer> coverIds = new HashSet<Integer>();
        List<Cover> covers = new ArrayList<>();

        for (Object[] album : albums) {
            if (album[1] != null) {
                coverIds.add((Integer) album[1]);
            }
        }
        if (coverIds != null) {
            covers = coverDao.find(coverIds);
        }

        for (Object[] album : albums) {
            AlbumDTO albumDTO = (AlbumDTO) album[0];
            for (Cover cover : covers) {
                if (cover.getId().equals(album[1])) {
                    CoverDTO coverDTO = new CoverDTO();
                    coverDTO.setId(cover.getId());
                    coverDTO.setOwnerId(albumDTO.getId());
                    coverDTO.setPreviewCoverPath(catalogService.getCoverURL(cover, Cover.Size.MEDIUM));
                    coverDTO.setCoverPath(catalogService.getCoverURL(cover, Cover.Size.BIG));
                    albumDTO.setCover(coverDTO);
                    break;
                }
            }
        }
    }

    @Override
    public List<AlbumDTO> findAlbumsByFirstCharacters(String firstCharacter, int startIndex, int maxResults) {
        List<AlbumDTO> albums = new ArrayList<>();

        try {
            if (StringCleanUtils.stringIsNullOrEmpty(firstCharacter)) {
                return albums;
            }

            String resQuery = "album.name:" + firstCharacter + "*";
            SolrQuery query = new SolrQuery().setQuery(resQuery)
                    .setSort("album.name", SolrQuery.ORDER.asc)
                    .setStart(startIndex)
                    .setRows(maxResults);

            QueryResponse response = albumSolrClient.query(query);
            albums = getAlbumsFromSolrResponse(response);

        } catch (SolrServerException e) {
            LOGGER.error("Solr server error occur", e);
        } catch (Exception e) {
            LOGGER.error("Exception", e);
        }
        return albums;
    }

    @Override
    public List<AlbumDTO> findAlbumsByReleaseDateRange(LocalDateTime start, LocalDateTime end, int startIndex, int maxResults) {
        List<AlbumDTO> albums = new ArrayList<AlbumDTO>();
        try {
            String from = "*";
            String to = "*";
            if (start != null) {
                from = start.format(java.time.format.DateTimeFormatter.ofPattern(SOLR_DATETIME_FORMAT));
            }
            if (end != null) {
                to = end.format(java.time.format.DateTimeFormatter.ofPattern(SOLR_DATETIME_FORMAT));
            }

            String resQuery = "album.release_date:[" + from + " TO " + to + "]";
            SolrQuery query = new SolrQuery().setQuery(resQuery)
                    .setSort("album.name", SolrQuery.ORDER.asc)
                    .setStart(startIndex)
                    .setRows(maxResults);

            QueryResponse response = albumSolrClient.query(query);
            albums = getAlbumsFromSolrResponse(response);

        } catch (SolrServerException e) {
            LOGGER.error("Solr server error occur", e);
        } catch (Exception e) {
            LOGGER.error("Exception", e);
        }
        return albums;
    }

    @Override
    public List<AlbumDTO> findAlbumsByGenreId(List<Integer> genreIdList, int startIndex, int maxResults) {
        List<AlbumDTO> albums = new ArrayList<AlbumDTO>();
        StringBuilder idListStringBuilder = new StringBuilder("(");
        for (Integer genreId : genreIdList) {
            idListStringBuilder.append(genreId);
            idListStringBuilder.append(" ");
        }
        idListStringBuilder.append(")");

        try {
            String resQuery = "album.genre_id:" + idListStringBuilder.toString();
            SolrQuery query = new SolrQuery().setQuery(resQuery)
                    .setSort("album.listening", SolrQuery.ORDER.desc)
                    .setStart(startIndex)
                    .setRows(maxResults);

            QueryResponse response = albumSolrClient.query(query);
            albums = getAlbumsFromSolrResponse(response);

        } catch (SolrServerException e) {
            LOGGER.error("Solr server error occur", e);
        } catch (Exception e) {
            LOGGER.error("Exception", e);
        }
        return albums;
    }

    @Override
    public List<AlbumDTO> findAlbumByName(String name, Integer startNumber, Integer maxNumber) {
        List<AlbumDTO> albums = new ArrayList<>();
        try {
            String resQuery = "album.name:\"" + name + "\"";
            SolrQuery query = new SolrQuery().setQuery(resQuery)
                    .setSort("album.listening", SolrQuery.ORDER.desc)
                    .setStart(startNumber)
                    .setRows(maxNumber);

            QueryResponse response = albumSolrClient.query(query);
            albums = getAlbumsFromSolrResponse(response);
        } catch (SolrServerException e) {
            LOGGER.error("Solr server error occur", e);
        } catch (Exception e) {
            LOGGER.error("Exception", e);
        }
        return albums;
    }

    @Override
    public List<SongDTO> findSongs(String searchValue, String sortProperty, Boolean ascending,
                                   int startIndex, int size) {
        List<SongDTO> songs = new LinkedList<SongDTO>();
        searchValue = ClientUtils.escapeQueryChars(searchValue);
        try {

            String resQuery = "suggest:\"" + searchValue + "\"";
            //String propQFGeneric = this.getProperty(Settings.SOLR_QUERY_FIELDS_GENERIC);
            SolrQuery query = new SolrQuery().setQuery(resQuery)
                    //.setParam("defType", "edismax") // edismax query
                    //.setParam("qf", propQFGeneric)
                    .setStart(startIndex)
                    .setRows(size);

            if (sortProperty != null && ascending != null) {
                SolrQuery.ORDER order = ascending ? SolrQuery.ORDER.asc : SolrQuery.ORDER.desc;
                query.setSort(sortProperty, order);
            }

            QueryResponse response = solrSongClient.query(query);
            SolrDocumentList solrlist = response.getResults();

            Iterator<SolrDocument> iter = solrlist.iterator();
            while (iter.hasNext()) {
                SongDTO song = getSongDTOFromSolr(iter.next());
                if (song != null) {
                    songs.add(song);
                }
            }
        } catch (SolrServerException e) {
            LOGGER.error("Solr server error occur", e);
        } catch (Exception e) {
            LOGGER.error("Exception", e);
        }
        return songs;
    }

    private SongDTO getSongDTOFromSolr(SolrDocument resultDoc) {
        if (resultDoc == null) {
            return null;
        }
        Integer songId = (Integer) resultDoc.getFieldValue("id");
        if (songId != null) {
            SongDTO song = new SongDTO();
            song.setId(songId);
            song.setName((String) resultDoc.getFieldValue("song.name"));
            song.setPlayTime((Integer) resultDoc.getFieldValue("song.length"));
            AlbumDTO album = new AlbumDTO();
            album.setId((Integer) resultDoc.getFieldValue("album.id"));
            album.setName((String) resultDoc.getFieldValue("album.name"));
            song.setAlbum(album);

            ArtistDTO artist = new ArtistDTO();
            artist.setId((Integer) resultDoc.getFieldValue("artist.id"));
            artist.setName((String) resultDoc.getFieldValue("artist.name"));
            song.setArtist(artist);
            return song;
        }
        return null;
    }

    @Override
    public List<SongDTO> findSongsBy(String artistName, String albumName, String songName,
                                     String sortProperty, Boolean ascending, int startIndex, int size) {
        List<SongDTO> songs = new LinkedList<SongDTO>();
        try {
            String resQuery = "";
            String qf = "";
            if (artistName != null) {
                if (artistName.length() > 0) {
                    artistName = ClientUtils.escapeQueryChars(artistName);
                    resQuery += " artist.name:\"" + artistName + "\"";
                    qf += solrQueryFieldsByArtist;
                }
            }
            if (albumName != null) {
                if (albumName.length() > 0) {
                    albumName = ClientUtils.escapeQueryChars(albumName);
                    resQuery += " album.name:\"" + albumName + "\"";
                    qf += " " + solrQueryFieldByAlbum;
                }
            }
            if (songName != null) {
                if (songName.length() > 0) {
                    songName = ClientUtils.escapeQueryChars(songName);
                    qf += " " + solrQueryFieldsBySong;
                    resQuery += " song.name:\"" + songName + "\"";
                }
            }
            if (resQuery.length() == 0) {
                return null;
            }

            SolrQuery query = new SolrQuery().setQuery(resQuery)
                    .setParam("defType", "edismax") // edismax query
                    .setParam("qf", qf)
                    .setParam("q.op", "AND")
                    //.setFields("id")
                    .setStart(startIndex)
                    .setRows(size);

            if (sortProperty != null && ascending != null) {
                SolrQuery.ORDER order = ascending ? SolrQuery.ORDER.asc : SolrQuery.ORDER.desc;
                query.setSort(sortProperty, order);
            }

            QueryResponse response = solrSongClient.query(query);
            SolrDocumentList solrlist = response.getResults();

            for (SolrDocument solrDocument : solrlist) {
                SongDTO song = getSongDTOFromSolr(solrDocument);
                if (song != null) {
                    songs.add(song);
                }
            }
        } catch (SolrServerException e) {
            LOGGER.error("Solr server error occur", e);
        } catch (Exception e) {
            LOGGER.error("Exception", e);
        }
        return songs;
    }

    @Override
    public Integer getSongsSize(String searchValue) {
        long result = 0;
        searchValue = ClientUtils.escapeQueryChars(searchValue);
        try {
            String resQuery = "suggest:\"" + searchValue + "\"";
            //String propQFGeneric = this.getProperty(Settings.SOLR_QUERY_FIELDS_GENERIC);
            SolrQuery query = new SolrQuery()
                    .setQuery(resQuery)
                    //.setParam("defType", "edismax") // edismax query
                    //.setParam("qf", propQFGeneric)
                    .setRows(0); // SOLR_MAXROWS zero needed to get count only without returning results.

            QueryResponse response = solrSongClient.query(query);
            result = response.getResults().getNumFound();
        } catch (SolrServerException e) {
            LOGGER.error("Solr server error occur", e);
        } catch (Exception e) {
            LOGGER.error("Exception", e);
        }
        return (int) result;
    }

    @Override
    public Integer getSongsSizeBy(String artistName, String albumName, String songName) {
        long result = 0;
        try {
            String resQuery = "";
            String qf = "";
            if (artistName != null) {
                if (artistName.length() > 0) {
                    artistName = ClientUtils.escapeQueryChars(artistName);
                    resQuery += " artist.name:\"" + artistName + "\"";
                    qf += solrQueryFieldsByArtist;
                }
            }
            if (albumName != null) {
                if (albumName.length() > 0) {
                    albumName = ClientUtils.escapeQueryChars(albumName);
                    resQuery += " album.name:\"" + albumName + "\"";
                    qf += " " + solrQueryFieldByAlbum;
                }
            }
            if (songName != null) {
                if (songName.length() > 0) {
                    songName = ClientUtils.escapeQueryChars(songName);
                    qf += " " + solrQueryFieldsBySong;
                    resQuery += " song.name:\"" + songName + "\"";
                }
            }
            if (resQuery.length() == 0) {
                return 0;
            }
            SolrQuery query = new SolrQuery()
                    .setQuery(resQuery)
                    .setParam("defType", "edismax") // edismax query
                    .setParam("qf", qf)
                    .setParam("q.op", "AND")
                    .setFields("id")
                    .setRows(0); // SOLR_MAXROWS zero needed to get count only without returning results.

            QueryResponse response = solrSongClient.query(query);
            result = response.getResults().getNumFound();
            //SolrDocumentList solrlist = response.getResults().getNumFound();
            //result = solrlist.size();
        } catch (SolrServerException e) {
            LOGGER.error("Solr server error occur", e);
        } catch (Exception e) {
            LOGGER.error("Exception", e);
        }
        return (int) result;
    }

    @Override
    public List<SongDTO> findTheMostPopularSongsByPeriod(RatingPeriod ratingPeriod, Integer startIndex, Integer maxResult) {
        List<SongDTO> songs = new ArrayList<>();

        SolrQuery query = new SolrQuery("*:*")
                .setStart(startIndex)
                .setRows(maxResult);
        switch (ratingPeriod) {
            case DAY:
                query.setSort("song.day_rating", SolrQuery.ORDER.desc);
                break;
            case WEEK:
                query.setSort("song.week_rating", SolrQuery.ORDER.desc);
                break;
            case MONTH:
                query.setSort("song.month_rating", SolrQuery.ORDER.desc);
                break;
            case ALL_TIME:
                query.setSort("song.rating", SolrQuery.ORDER.desc);
                break;
        }
        try {
            QueryResponse response = solrSongClient.query(query);
            SolrDocumentList solrlist = response.getResults();

            for (SolrDocument solrDocument : solrlist) {
                SongDTO song = getSongDTOFromSolr(solrDocument);
                if (song != null) {
                    songs.add(song);
                }
            }
        } catch (SolrServerException e) {
            LOGGER.error("Solr server error occur", e);
        } catch (Exception e) {
            LOGGER.error("Error occur", e);
        }
        return songs;
    }

    @Override
    public List<SongDTO> findTheMostPopularSongsByGenreIdAndPeriod(List<GenreDTO> genreList, RatingPeriod ratingPeriod, Integer startIndex, Integer maxResult) {
        List<SongDTO> songs = new ArrayList<>();

        SolrQuery query = new SolrQuery(buildQueryByGenreList(genreList, "album.genre_id:"))
                .setStart(startIndex)
                .setRows(maxResult);
        switch (ratingPeriod) {
            case DAY:
                query.setSort("song.day_rating", SolrQuery.ORDER.desc);
                break;
            case WEEK:
                query.setSort("song.week_rating", SolrQuery.ORDER.desc);
                break;
            case MONTH:
                query.setSort("song.month_rating", SolrQuery.ORDER.desc);
                break;
            case ALL_TIME:
                query.setSort("song.rating", SolrQuery.ORDER.desc);
                break;
        }
        try {
            QueryResponse response = solrSongClient.query(query);
            SolrDocumentList solrlist = response.getResults();

            for (SolrDocument solrDocument : solrlist) {
                SongDTO song = getSongDTOFromSolr(solrDocument);
                if (song != null) {
                    songs.add(song);
                }
            }
        } catch (SolrServerException e) {
            LOGGER.error("Solr server error occur", e);
        } catch (IOException e) {
            LOGGER.error("Solr server IO error occur", e);
        }

        return songs;
    }

    @Override
    public List<AlbumDTO> findTheMostPopularAlbumsByPeriod(RatingPeriod ratingPeriod, Integer startIndex, Integer maxResult) {
        List<AlbumDTO> albums = new ArrayList<AlbumDTO>();

        SolrQuery query = new SolrQuery().setQuery("*:*")
                .setStart(startIndex)
                .setRows(maxResult);
        switch (ratingPeriod) {
            case DAY:
                query.setSort("album.day_rating", SolrQuery.ORDER.desc);
                break;
            case WEEK:
                query.setSort("album.week_rating", SolrQuery.ORDER.desc);
                break;
            case MONTH:
                query.setSort("album.month_rating", SolrQuery.ORDER.desc);
                break;
            case ALL_TIME:
                query.setSort("album.listening", SolrQuery.ORDER.desc);
                break;
        }

        try {
            QueryResponse response = albumSolrClient.query(query);
            albums = getAlbumsFromSolrResponse(response);

        } catch (SolrServerException e) {
            LOGGER.error("Solr server error occur", e);
        } catch (Exception e) {
            LOGGER.error("Exception", e);
        }
        return albums;
    }

    @Override
    public List<AlbumDTO> findTheMostPopularAlbumsByGenreIdAndPeriod(List<GenreDTO> genreList, RatingPeriod ratingPeriod, Integer startIndex, Integer maxResult) {
        List<AlbumDTO> albums = new ArrayList<AlbumDTO>();

        SolrQuery query = new SolrQuery().setQuery(buildQueryByGenreList(genreList, "album.genre_id:"))
                .setStart(startIndex)
                .setRows(maxResult);
        switch (ratingPeriod) {
            case DAY:
                query.setSort("album.day_rating", SolrQuery.ORDER.desc);
                break;
            case WEEK:
                query.setSort("album.week_rating", SolrQuery.ORDER.desc);
                break;
            case MONTH:
                query.setSort("album.month_rating", SolrQuery.ORDER.desc);
                break;
            case ALL_TIME:
                query.setSort("album.listening", SolrQuery.ORDER.desc);
                break;
        }
        try {
            QueryResponse response = albumSolrClient.query(query);
            albums = getAlbumsFromSolrResponse(response);
        } catch (SolrServerException e) {
            LOGGER.error("Solr server error occur", e);
        } catch (Exception e) {
            LOGGER.error("Exception", e);
        }
        return albums;
    }

    private String buildQueryByGenreList(List<GenreDTO> genreList, String genreField) {
        if (genreList == null || genreList.isEmpty()) {
            return "*:*";
        }
        StringBuilder queryBuilder = new StringBuilder("(");

        for (int i = 0; i < genreList.size() - 1; i++) {
            queryBuilder.append(genreField)
                    .append(genreList.get(i).getId())
                    .append(" OR ");
        }

        queryBuilder.append(genreField)
                .append(genreList.get(genreList.size() - 1).getId())
                .append(")");

        return queryBuilder.toString();
    }

    @Override
    public List<ArtistDTO> findTheMostPopularArtist(Integer startIndex, Integer maxResult) {

        List<ArtistDTO> artistList = new ArrayList<>();

        SolrQuery query = new SolrQuery().setQuery("*:*")
                .setStart(0)
                .setRows(maxResult)
                .setSort("artist.rating", SolrQuery.ORDER.desc);

        try {
            QueryResponse response = artistSolrClient.query(query);
            SolrDocumentList solrlist = response.getResults();
            for (SolrDocument solrDocument : solrlist) {
                ArtistDTO artistDTO = getArtistDTOFromSolr(solrDocument);
                if (artistDTO != null) {
                    artistList.add(artistDTO);
                }
            }
        } catch (SolrServerException e) {
            LOGGER.error("Solr server error occur", e);
        } catch (Exception e) {
            LOGGER.error("Exception", e);
        }
        return artistList;
    }

    @Override
    public List<ArtistDTO> findTheMostPopularArtistByGenre(List<GenreDTO> genreList, Integer startIndex, Integer maxResult) {
        List<ArtistDTO> artistList = new ArrayList<>();

        SolrQuery query = new SolrQuery().setQuery(buildQueryByGenreList(genreList, "artist.genre_id:"))
                .setStart(0)
                .setRows(maxResult)
                .setSort("artist.rating", SolrQuery.ORDER.desc);

        try {
            QueryResponse response = artistSolrClient.query(query);
            SolrDocumentList solrlist = response.getResults();
            for (SolrDocument solrDocument : solrlist) {
                ArtistDTO artistDTO = getArtistDTOFromSolr(solrDocument);
                if (artistDTO != null) {
                    artistList.add(artistDTO);
                }
            }
        } catch (SolrServerException e) {
            LOGGER.error("Solr server error occur", e);
        } catch (Exception e) {
            LOGGER.error("Exception", e);
        }
        return artistList;
    }

    @Override
    public Map<String, List<Object>> findArtistIdAndNamesByAlbumId(Integer albumId) {
        Map<String, List<Object>> artistIdAndNameMap = new HashMap<>();
        try {
            String resQuery = "id:" + albumId;

            SolrQuery query = new SolrQuery().setQuery(resQuery)
                    .setRows(1);

            QueryResponse response = albumSolrClient.query(query);

            for (SolrDocument solrDocument : response.getResults()) {
                List<Object> artistIds = (List<Object>) solrDocument.getFieldValue("artist.id");
                List<Object> artistNames = (List<Object>) solrDocument.getFieldValue("artist.name");

                artistIdAndNameMap.put("id", artistIds);
                artistIdAndNameMap.put("name", artistNames);
            }
        } catch (SolrServerException e) {
            LOGGER.error("Solr server error occur", e);
        } catch (Exception e) {
            LOGGER.error("Exception", e);
        }
        return artistIdAndNameMap;
    }

    @Override
    public int TotalSongs() {
        SolrQuery q = new SolrQuery("*:*");
        q.setRows(0);  // don't actually request any data
        try {
            return (int) solrSongClient.query(q).getResults().getNumFound();
        } catch (SolrServerException e) {
            LOGGER.error("Solr server error occur", e);
        } catch (Exception e) {
            LOGGER.error("Exception", e);
        }
        return 0;
    }

    @Override
    public String findSearchQuerySuggestion(String queryText) {
        try {
            return spellcheck(queryText, SOLR_REQUEST_HANDLER_SUGGEST, solrSongClient);
        } catch (IOException e) {
            LOGGER.error("Solr server error occur", e);
        } catch (SolrServerException e) {
            LOGGER.error("Exception", e);
        }
        return "";
    }

    @Override
    public String findSongNameSuggestion(String songName) {
        try {
            return spellcheck(songName, SOLR_REQUEST_HANDLER_SELECT, solrSongClient);
        } catch (IOException e) {
            LOGGER.error("Solr server error occur", e);
        } catch (SolrServerException e) {
            LOGGER.error("Exception", e);
        }
        return "";
    }

    @Override
    public String findAlbumNameSuggestion(String albumName) {
        try {
            return spellcheck(albumName, SOLR_REQUEST_HANDLER_SELECT, albumSolrClient);
        } catch (IOException e) {
            LOGGER.error("Solr server error occur", e);
        } catch (SolrServerException e) {
            LOGGER.error("Exception", e);
        }
        return "";
    }

    @Override
    public String findArtistNameSuggestion(String artistName) {
        try {
            return spellcheck(artistName, SOLR_REQUEST_HANDLER_SELECT, artistSolrClient);
        } catch (IOException e) {
            LOGGER.error("Solr server error occur", e);
        } catch (SolrServerException e) {
            LOGGER.error("Exception", e);
        }
        return "";
    }
}

