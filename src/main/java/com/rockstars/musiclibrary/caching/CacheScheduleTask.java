package com.rockstars.musiclibrary.caching;


import com.rockstars.musiclibrary.service.ArtistService;
import com.rockstars.musiclibrary.service.SongService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class CacheScheduleTask {

    private final CacheManager cacheManager;
    private final SongService songService;
    private final ArtistService artistService;

    // 15 minutes
    @Scheduled(cron = "0 */15 * * * *")
    public void renewCache() {
        log.info("Refreshing the cache");
        clearSongs();
        clearArtists();
        songService.findAll("Metal", 2016, PageRequest.ofSize(100));
        artistService.findAll(PageRequest.ofSize(100));
    }

    private void clearArtists() {
        Cache cache = cacheManager.getCache("artists");
        if (cache != null) {
            cache.clear();
        }
    }

    private void clearSongs() {
        Cache cache = cacheManager.getCache("songs");
        if (cache != null) {
            cache.clear();
        }
    }

}