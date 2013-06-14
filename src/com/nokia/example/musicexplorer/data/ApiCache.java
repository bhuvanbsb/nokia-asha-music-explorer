/**
 * Copyright (c) 2013 Nokia Corporation. All rights reserved. Nokia and Nokia
 * Connecting People are registered trademarks of Nokia Corporation. Oracle and
 * Java are trademarks or registered trademarks of Oracle and/or its affiliates.
 * Other product and company names mentioned herein may be trademarks or trade
 * names of their respective owners. See LICENSE.TXT for license information.
 */
package com.nokia.example.musicexplorer.data;

import org.tantalum.PlatformUtils;
import org.tantalum.Task;
import org.tantalum.net.StaticWebCache;
import org.tantalum.net.StaticWebCache.HttpTaskFactory;
import org.tantalum.storage.CacheView;
import org.tantalum.storage.FlashDatabaseException;
import org.tantalum.storage.ImageCacheView;
import org.tantalum.util.L;

import com.nokia.example.musicexplorer.settings.ApiEndpoint;
import com.nokia.example.musicexplorer.ui.ViewManager;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import org.tantalum.jme.JMENetUtils;
import org.tantalum.util.StringUtils;

/**
 * Responsible for creating caches. The caches are used for accessing the REST
 * API.
 */
public class ApiCache {

    public static StaticWebCache apiCache;
    public static StaticWebCache imageCache;

    private static ViewManager viewManager;
    
    /**
     * Initializes caches that are instances of Tantalum's StaticWebCache.
     *
     * @return boolean
     */
    public static synchronized boolean init(ViewManager viewManager) {
        boolean success = false;
        ApiCache.viewManager = viewManager;
        
        if (apiCache == null) {
            try {
                apiCache = StaticWebCache.getWebCache(
                        '1',
                        PlatformUtils.PHONE_DATABASE_CACHE,
                        (CacheView) new JSONResponseHandler(),
                        new HttpTaskFactory(), 
                        null);
                success = true;
            } catch (FlashDatabaseException e) {
                L.e("Could not initialize the API cache.", "", e);
            }
        }

        if (imageCache == null) {
            PlatformUtils platformUtils = PlatformUtils.getInstance();
            ImageCacheView imageCacheView = platformUtils.getImageCacheView();

            try {
                imageCache = StaticWebCache.getWebCache(
                        '2', 
                        PlatformUtils.PHONE_DATABASE_CACHE, 
                        imageCacheView, 
                        new HttpTaskFactory(), 
                        null);
                success = true;
            } catch (FlashDatabaseException e) {
                L.e("Could not initialize the Image cache.", "", e);
            }
        }

        return success;
    }

    /**
     * Gets new releases from the New Releases Resource.
     *
     * @param callback The Task to call when an HTTP response is received.
     * @return
     */
    public static Task getNewReleases(Task callback, String pagingQueryString) {
        if(!hasNetworkConnection()) {
            return null;
        }
                
        return apiCache.getAsync(
                ApiEndpoint.getNewReleasesResourceUrl(pagingQueryString),
                Task.NORMAL_PRIORITY,
                StaticWebCache.GET_WEB,
                callback);
    }

    /**
     * Gets the genres listing.
     *
     * @param callback The Task to call when an HTTP response is received.
     * @return
     */
    public static Task getGenres(Task callback) {
        if(!hasNetworkConnection()) {
            return null;
        }
        
        return apiCache.getAsync(
                ApiEndpoint.getGenresResourceUrl(),
                Task.NORMAL_PRIORITY,
                StaticWebCache.GET_WEB,
                callback);
    }

    /**
     * Gets albums for an artist.
     *
     * @param artistId The artist id to look for.
     * @param callback The Task to call when an HTTP response is received.
     * @return
     */
    public static Task getAlbumsForArtist(
            int artistId,
            Task callback,
            String pagingQueryString) {

        if(!hasNetworkConnection()) {
            return null;
        }
        
        return apiCache.getAsync(
                ApiEndpoint.getReleasesForArtist(artistId, pagingQueryString),
                Task.NORMAL_PRIORITY,
                StaticWebCache.GET_WEB,
                callback);
    }

    /**
     * Gets details for an album specified by an id. The details include e.g. a
     * track listing.
     *
     * @param id The Album's id.
     * @param callback The Task to call when an HTTP response is received.
     * @return
     */
    public static Task getAlbumDetails(int id, Task callback) {
        if(!hasNetworkConnection()) {
            return null;
        }
        return apiCache.getAsync(
                ApiEndpoint.getProductDetailsResourceUrl(id),
                Task.NORMAL_PRIORITY,
                StaticWebCache.GET_WEB,
                callback);
    }

    /**
     * Gets an image file from either the Internet or from the Image cache.
     *
     * @param imageUri The resource identifier for the image.
     * @param callback The Task to call when a response has been found.
     * @return
     */
    public static Task getImage(String imageUri, Task callback) {
        if(!hasNetworkConnection()) {
            return null;
        }
        
        if (imageUri == null || imageUri.length() == 0) {
            L.i("Invalid image URI.", "");
            return null;
        }

        return imageCache.getAsync(
                imageUri,
                Task.NORMAL_PRIORITY,
                StaticWebCache.GET_ANYWHERE,
                callback);
    }

    /**
     * Gets popular releases.
     *
     * @param callback The Task to call when an HTTP response is received.
     * @return
     */
    public static Task getPopularReleases(Task callback, String pagingQueryString) {
        if(!hasNetworkConnection()) {
            return null;
        }        
        
        return apiCache.getAsync(
                ApiEndpoint.getChartsResourceUrl(pagingQueryString),
                Task.NORMAL_PRIORITY,
                StaticWebCache.GET_WEB,
                callback);
    }

    /**
     * Searches using the given search query. The search is currently limited to
     * album and artists.
     *
     * @param searchQuery Search query as a string.
     * @param callback The Task to call when an HTTP response is received.
     * @return
     */
    public static Task search(String searchQuery, Task callback, String pagingQueryString) {
        if(!hasNetworkConnection()) {
            return null;
        }
        
        if (searchQuery == null || searchQuery.length() == 0) {
            L.i("Search query cannot be null or empty.", "");
            return null;
        }

        return apiCache.getAsync(
                ApiEndpoint.getSearchUrl(searchQuery, pagingQueryString),
                Task.NORMAL_PRIORITY,
                StaticWebCache.GET_WEB,
                callback);
    }

    public static Task getArtistDetailsById(int productId, Task callback) {
        if(!hasNetworkConnection()) {
            return null;
        }
        
        return apiCache.getAsync(
                ApiEndpoint.getProductDetailsById(productId),
                Task.NORMAL_PRIORITY,
                StaticWebCache.GET_WEB,
                callback);
    }

    public static Task getSimilarArtistsById(int artistId, Task callback, String pagingQueryString) {
        if(!hasNetworkConnection()) {
            return null;
        }
        
        return apiCache.getAsync(
                ApiEndpoint.getSimilarArtistsById(artistId, pagingQueryString), 
                Task.NORMAL_PRIORITY,
                StaticWebCache.GET_WEB,
                callback);
    }
    
    public static Task getArtistsInGenre(String genreId, Task callback, String pagingQueryString) {
        if(!hasNetworkConnection()) {
            return null;
        }
        
        return apiCache.getAsync(
                ApiEndpoint.getArtistsInGenre(genreId, pagingQueryString), 
                Task.NORMAL_PRIORITY,
                StaticWebCache.GET_WEB,
                callback);
    }
    
    /**
     * Determines the state of network connectivity by making a test request.
     * @return 
     */
    public static boolean hasNetworkConnection() {
        boolean hasNetwork = false;
        
        // Make test request
        HttpConnection httpConnection;
        String testRequestUrl = ApiEndpoint.getBaseUrl();
        
        try {
            L.i("Testing network connection", "URL: " + testRequestUrl);
            httpConnection = 
                    (HttpConnection) Connector.open(testRequestUrl);
            L.i("Response message", httpConnection.getResponseMessage());
            L.i("Network connection established", httpConnection.toString());
            hasNetwork = true;
            httpConnection.close();
        } catch (IOException e) {
            L.i("Network connection failed", e.toString());
            viewManager.showNetworkAlert();
        }
        
        return hasNetwork;
    }
    
}
