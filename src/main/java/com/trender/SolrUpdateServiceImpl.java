/*
package com.trender;

*/
/**
 * Created by Egor.Veremeychik on 27.06.2016.
 *//*


        import com.nda.mss.dao.SongDao;
        import com.nda.mss.dto.SolrAlbumDTO;
        import com.nda.mss.dto.SolrArtistDTO;
        import com.nda.mss.dto.SolrSongDTO;
        import com.nda.mss.entity.Album;
        import com.nda.mss.entity.Artist;
        import com.nda.mss.entity.Song;
        import com.nda.mss.exceptions.ProcessingError;
        import com.nda.mss.global.GlobalService;
        import com.nda.mss.global.GlobalServiceConnection;
        import com.nda.mss.service.SolrSearchService;
        import com.nda.mss.service.SolrUpdateService;
        import org.apache.thrift.TException;
        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;

        import java.util.ArrayList;
        import java.util.List;
        import java.util.Map;


@Service
public class SolrUpdateServiceImpl implements SolrUpdateService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SolrUpdateServiceImpl.class);

    @Autowired
    private SongDao songDao;

    @Autowired
    private SolrSearchService solrSearchService;

    @Override
    public void updateArtist(Artist artist) {
        List<Song> artistSongs = songDao.findByArtistId(artist.getId());

        GlobalServiceConnection globalServiceConnection = new GlobalServiceConnection();
        try {
            GlobalService.Iface globalServiceClient = globalServiceConnection.openGlobalClient();

            for (Song artistSong : artistSongs) {
                globalServiceClient.updateSongToSolr(SolrSongDTO.toSolrSong(artistSong));
            }
            globalServiceClient.commitToSolr();
        } catch (TException e) {
            LOGGER.error("Can not open global client", e);
            throw new ProcessingError(e);
        } finally {
            globalServiceConnection.closeGlobalClient();
        }
    }

    @Override
    public void updateArtistWithoutSong(Artist artist) {
        GlobalServiceConnection globalServiceConnection = new GlobalServiceConnection();
        try {
            GlobalService.Iface globalServiceClient = globalServiceConnection.openGlobalClient();
            globalServiceClient.updateArtistToSolr(SolrArtistDTO.toSolrArtist(artist));
            globalServiceClient.commitToSolr();
        } catch (TException e) {
            LOGGER.error("Can not open global client", e);
            throw new ProcessingError(e);
        } finally {
            globalServiceConnection.closeGlobalClient();
        }
    }

    @Override
    public void updateAlbum(Album album) {
        List<Integer> artistIds = new ArrayList<>();
        List<String> artistNames = new ArrayList<>();
        List<Song> albumSongs = songDao.findByAlbumIdExcludingDuplicates(album.getId());

        GlobalServiceConnection globalServiceConnection = new GlobalServiceConnection();
        try {
            Integer albumId = album.getId();

            Integer artistId = album.getId();
            String artistName = album.getName();

            Map<String, List<Object>> idsAndNames = solrSearchService.findArtistIdAndNamesByAlbumId(albumId);

            List<Object> ids = idsAndNames.get("id");
            List<Object> names = idsAndNames.get("name");

            for (Object o : ids) {
                artistIds.add((Integer) o);
            }
            artistIds.add(artistId);

            for (Object o : names) {
                artistNames.add((String) o);
            }
            artistNames.add(artistName);

            GlobalService.Iface globalServiceClient = globalServiceConnection.openGlobalClient();

            for (Song albumSong : albumSongs) {
                globalServiceClient.updateSongToSolr(SolrSongDTO.toSolrSong(albumSong));
                globalServiceClient.updateAlbumToSolr(SolrAlbumDTO.toSolrAlbum(album, artistIds, artistNames));
            }
            globalServiceClient.commitToSolr();
        } catch (TException e) {
            LOGGER.error("Can not open global client", e);
            throw new ProcessingError(e);
        } finally {
            globalServiceConnection.closeGlobalClient();
        }
    }

    @Override
    public void updateAlbum(Song song){
        List<Integer> artistIds = new ArrayList<>();
        List<String> artistNames = new ArrayList<>();

        GlobalServiceConnection globalServiceConnection = new GlobalServiceConnection();
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
        }
    }

    @Override
    public void updateSong(Song song) {
        GlobalServiceConnection globalServiceConnection = new GlobalServiceConnection();
        try {
            GlobalService.Iface globalServiceClient = globalServiceConnection.openGlobalClient();
            globalServiceClient.updateSongToSolr(SolrSongDTO.toSolrSong(song));
            globalServiceClient.commitToSolr();
        } catch (TException e) {
            LOGGER.error("Can not open global client", e);
            throw new ProcessingError(e);
        } finally {
            globalServiceConnection.closeGlobalClient();
        }
    }
}
*/
