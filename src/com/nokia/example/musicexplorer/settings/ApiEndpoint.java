/**
 * Copyright (c) 2013 Nokia Corporation. All rights reserved. Nokia and Nokia
 * Connecting People are registered trademarks of Nokia Corporation. Oracle and
 * Java are trademarks or registered trademarks of Oracle and/or its affiliates.
 * Other product and company names mentioned herein may be trademarks or trade
 * names of their respective owners. See LICENSE.TXT for license information.
 */

package com.nokia.example.musicexplorer.settings;

import java.io.IOException;

import org.tantalum.util.L;
import org.tantalum.util.StringUtils;

/**
 * Holds the Nokia Music REST API endpoints, credentials, and such needed for
 * forming API calls.
 */
public class ApiEndpoint {

    private static final String ENDPOINT_URL = "http://api.ent.nokia.com/";
    private static final String VERSION = "1.x";
    private static final String COUNTRYCODE = "us";
    private static final String APP_ID = "demo_qCG24t50dHOwrLQ";
    private static final String APP_CODE = "NYKC67ShPhQwqaydGIW4yg";

    /**
     * Forms the base URL for calls.
     *
     * @return
     */
    public static String getBaseUrl() {
        return ENDPOINT_URL + VERSION + '/';
    }

    /**
     * Helper method for forming URLs.
     *
     * @param query
     * @return
     */
    private static String formUrl(String query) {
        return formUrl(query, null);
    }

    /**
     * Forms a URL for the Genres Resource.
     *
     * @return
     */
    public static String getGenresResourceUrl() {
        return formUrl("genres/?domain=music");
    }

    /**
     * Forms a URL for the New Releases Resource.
     *
     * @param pagingQueryString Paging parameters from a QueryPager instance.
     * @return
     */
    public static String getNewReleasesResourceUrl(String pagingQueryString) {
        return formUrl("products/new/album/?domain=music", pagingQueryString);
    }

    /**
     * Forms a URL for the Product Details Resource. Products are albums,
     * tracks, artists and singles.
     *
     * @param id Product's identifier.
     * @return
     */
    public static String getProductDetailsResourceUrl(int id) {
        return formUrl("products/" + id + "/?domain=music");
    }

    /**
     * Forms a URL for the Charts Resource.
     *
     * @param pagingQueryString Paging parameters from a QueryPager instance.
     * @return
     */
    public static String getChartsResourceUrl(String pagingQueryString) {
        String category = "album/";
        String query = "products/charts/" + category + "/?domain=music";
        return formUrl(query, pagingQueryString);
    }

    /**
     * Forms a URL for the Search Resource.
     *
     * @param searchQuery A string containing the search parameters.
     * @return
     */
    public static String getSearchUrl(String searchQuery, String pagingQueryString) {
        try {
            // Currently limits search results to artists only.
            return formUrl(
                    "?domain=music"
                    + "&q="
                    + StringUtils.urlEncode(searchQuery.toLowerCase())
                    + "&category=artist",
                    pagingQueryString);
        } catch (IOException e) {
            L.e("Couldn't URL encode the given search query.",
                searchQuery != null ? searchQuery : "", e);
        }
        
        return null;
    }

    /**
     * Uses the Artists Releases Resource.
     *
     * @param artistId
     * @return
     */
    public static String getReleasesForArtist(
            int artistId,
            String pagingQueryString) {
    	
        // Gets only albums by an artist of artistId.
        return formUrl(
                "creators/"
                + Integer.toString(artistId)
                + "/products/?domain=music&category=album&category=single",
                pagingQueryString);
    }

    public static String getProductDetailsById(int productId) {
        // Gets product details by product id.
        return formUrl(
                "?domain=music&id=" + 
                Integer.toString(productId));
    }

    public static String getSimilarArtistsById(int artistId, String pagingQueryString) {
        return formUrl(
                "creators/" + 
                Integer.toString(artistId) +
                "/similar/" +
                "?domain=music",
                pagingQueryString);
    }

    public static String getArtistsInGenre(String genreId, String pagingQueryString) {
        return formUrl(
                "?domain=music" +
                "&genre=" + genreId +
                "&category=artist",
                pagingQueryString);
    }

    /**
     * Appended to the end of each API query.
     *
     * @return HTTP query string
     */
    private static String getApiCredentials() {
        return "&app_id=" + APP_ID + "&app_code=" + APP_CODE;
    }

    private static String getCountrycode() {
        return COUNTRYCODE + '/';
    }

    /**
     * Forms an API call.
     *
     * @param query The query.
     * @param pagingQueryString Paging parameters from a QueryPager instance.
     * @return
     */
    private static String formUrl(String query, String pagingQueryString) {
        String url = getBaseUrl() + getCountrycode() + query + getApiCredentials();
        
        // Checks if paging parameters exist.
        if (pagingQueryString != null) {
            url += pagingQueryString;
        }
        
        return url;
    }
}
