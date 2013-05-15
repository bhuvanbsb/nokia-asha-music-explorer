/**
* Copyright (c) 2013 Nokia Corporation. All rights reserved.
* Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
* Oracle and Java are trademarks or registered trademarks of Oracle and/or its
* affiliates. Other product and company names mentioned herein may be trademarks
* or trade names of their respective owners. 
* See LICENSE.TXT for license information.
*/

package com.nokia.example.musicexplorer.settings;

import java.io.IOException;
import org.tantalum.util.L;
import org.tantalum.util.StringUtils;
import com.nokia.example.musicexplorer.data.QueryPager;

public class ApiEndpoint {
	
	// API connection details.
	private static final String ENDPOINT_URL = "http://api.ent.nokia.com/";
	private static final String VERSION = "1.x";
	private static final String COUNTRYCODE = "us";
	private static final String APP_ID = "demo_qCG24t50dHOwrLQ";
	private static final String APP_CODE = "NYKC67ShPhQwqaydGIW4yg";
	
	private static final String NEW_RELEASES_RESOURCE = "products/new/album/?domain=music";

	/**
	 * Appended to the end of each API query.
	 * @return HTTP query string
	 */
	private static String getApiCredentials() {
		return "&app_id=" + APP_ID + "&app_code=" + APP_CODE;
	}
	
	public static String getBaseUrl() {
		return ENDPOINT_URL + VERSION + '/';
	}
	
	public static String getCountrycode() {
		return COUNTRYCODE + '/';
	}
	

	/**
	 * Form an API query.
	 * @param query
	 * @param pagingQueryString
	 * @return
	 */
	private static String formUrl(String query, String pagingQueryString) {
		String url = getBaseUrl() + getCountrycode() + query + getApiCredentials();

		if(pagingQueryString != null) {
			url += pagingQueryString;
		}
		
		return url;
	}
	
	private static String formUrl(String query) {
		return formUrl(query, null);
	}
	
	public static String getGenresResourceUrl() {
		return formUrl("genres/?domain=music");
	}

	public static String getNewReleasesResourceUrl(String pagingQueryString) {
		return formUrl(NEW_RELEASES_RESOURCE, pagingQueryString);
	}
	
	public static String getProductDetailsResourceUrl(int id) {
		return formUrl("products/" + id + "/?domain=music");
	}

	public static String getChartsResourceUrl(String pagingQueryString) {
		String category = "album/";
		String query = "products/charts/" + category + "/?domain=music";
		
		if(pagingQueryString != null) {
			query += pagingQueryString;
		}
		
		return formUrl(query);
	}

	public static String getSearchUrl(String searchQuery) {
		L.i("Search query amount: ", Integer.toString(searchQuery.length()));
		try {
			// TODO: Search is now limited to return Artist and/or Album products only. Should it be implemented as a search option?
			return formUrl("?domain=music" + "&q=" + StringUtils.urlEncode(searchQuery.toLowerCase()) + "&category=artist&category=album");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Uses the Artists Releases resource.
	 * @param artistId
	 * @return
	 */
	public static String getReleasesForArtist(int artistId) {
		return formUrl("creators/" + Integer.toString(artistId) + "/products/?domain=music&category=album");
	}
	
	/*
	 * TODO: Implement methods for forming the required API calls.
	 */
}
