/**
* Copyright (c) 2013 Nokia Corporation. All rights reserved.
* Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
* Oracle and Java are trademarks or registered trademarks of Oracle and/or its
* affiliates. Other product and company names mentioned herein may be trademarks
* or trade names of their respective owners. 
* See LICENSE.TXT for license information.
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

/**
 * Responsible for creating caches. The caches are used for accessing 
 * the REST API.
 */
public class ApiCache {	
	public static StaticWebCache apiCache;
	public static StaticWebCache imageCache;
	
	/**
	 * Initializes caches that are instances of Tantalum's StaticWebCache.
	 * @return boolean
	 */
    public static synchronized boolean init() {
    	boolean success = false;
    	
    	if(apiCache == null) {
    		try {
				apiCache = StaticWebCache.getWebCache(
					'1',
					PlatformUtils.PHONE_DATABASE_CACHE, 
					(CacheView) new JSONResponseHandler(), 
					new HttpTaskFactory(
				));
				success = true;
			} catch (FlashDatabaseException e) {
				L.e("Could not initialize the API cache.", "", e);
			}
    	}
    	
    	if(imageCache == null) {
    		PlatformUtils platformUtils = PlatformUtils.getInstance();
    		ImageCacheView imageCacheView = platformUtils.getImageTypeHandler();
    		
			try {
				imageCache = StaticWebCache.getWebCache('2', PlatformUtils.PHONE_DATABASE_CACHE, imageCacheView, new HttpTaskFactory());
				success = true;
			} catch (FlashDatabaseException e) {
				L.e("Could not initialize the Image cache.", "", e);
			}
    	}
    	
    	return success;
	}

    /**
     * Gets new releases from the New Releases Resource.
     * @param callback The Task to call when an HTTP response is received.
     * @return
     */
	public static Task getNewReleases(Task callback, String pagingQueryString) { 
		return apiCache.getAsync(
			ApiEndpoint.getNewReleasesResourceUrl(pagingQueryString),
			Task.NORMAL_PRIORITY,
			StaticWebCache.GET_WEB,
			callback
		);
	}

	/**
	 * Gets the genres listing.
     * @param callback The Task to call when an HTTP response is received.
	 * @return
	 */
    public static Task getGenres(Task callback) {
		return apiCache.getAsync(
    		ApiEndpoint.getGenresResourceUrl(),
			Task.NORMAL_PRIORITY,
			StaticWebCache.GET_WEB, 						
			callback
		);
    }

    /**
     * Gets albums for an artist.
     * @param artistId The artist id to look for.
     * @param callback The Task to call when an HTTP response is received.
     * @return
     */
    public static Task getAlbumsForArtist(
    		int artistId, 
    		Task callback, 
    		String pagingQueryString) {
    	
		return apiCache.getAsync(
	    		ApiEndpoint.getReleasesForArtist(artistId, pagingQueryString),
				Task.NORMAL_PRIORITY,
				StaticWebCache.GET_WEB, 						
				callback
			);
    }
    
    /**
     * Gets details for an album specified by an id. The details include
     * e.g. a track listing.
     * @param id The Album's id.
     * @param callback The Task to call when an HTTP response is received.
     * @return
     */
	public static Task getAlbumDetails(int id, Task callback) {
		return apiCache.getAsync(
    		ApiEndpoint.getProductDetailsResourceUrl(id),
			Task.NORMAL_PRIORITY,
			StaticWebCache.GET_WEB, 						
			callback
		);
	}

	/**
	 * Gets an image file from either the Internet or from the Image cache.
	 * @param imageUri The resource identifier for the image.
     * @param callback The Task to call when a response has been found.
	 * @return
	 */
	public static Task getImage(String imageUri, Task callback) {
		return imageCache.getAsync(
    		imageUri,
			Task.NORMAL_PRIORITY,
			StaticWebCache.GET_ANYWHERE,
			callback
		);
	}
	
	/**
	 * Gets popular releases.
     * @param callback The Task to call when an HTTP response is received.
	 * @return
	 */
	public static Task getPopularReleases(Task callback, String pagingQueryString) {
		return apiCache.getAsync(
				ApiEndpoint.getChartsResourceUrl(pagingQueryString),
				Task.NORMAL_PRIORITY,
				StaticWebCache.GET_WEB,
				callback
			);
	}
	
	/**
	 * Searches using the given search query. The search is currently limited 
	 * to album and artists.
	 * @param searchQuery Search query as a string.
     * @param callback The Task to call when an HTTP response is received.
	 * @return
	 */
	public static Task search(String searchQuery, Task callback) {
		return apiCache.getAsync(
				ApiEndpoint.getSearchUrl(searchQuery),
				Task.NORMAL_PRIORITY,
				StaticWebCache.GET_WEB,
				callback
			);
	}
}
