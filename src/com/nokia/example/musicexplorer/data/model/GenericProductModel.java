/**
* Copyright (c) 2013 Nokia Corporation. All rights reserved.
* Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
* Oracle and Java are trademarks or registered trademarks of Oracle and/or its
* affiliates. Other product and company names mentioned herein may be trademarks
* or trade names of their respective owners. 
* See LICENSE.TXT for license information.
*/

package com.nokia.example.musicexplorer.data.model;

import java.util.Enumeration;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Date;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.tantalum.util.L;

/**
 * Defines the common properties and methods for all products.
 */
public class GenericProductModel {
	
	public String name;
	public String sortname;
	public String label;
	public String copyright;
	public String type;
	public int id;
	public Date streetReleaseDate;
	public CategoryModel category;
	public Vector genres;
	public Vector creators;	

	/**
	 * Holds the URLs that are used in retrieving image files.
	 * The images aren't directly accessed but via an image cache.
	 * TODO: Image cache implementation?
	 */
	private Hashtable thumbnailUrls;
	
	public GenericProductModel(JSONObject obj) {
		
		this.thumbnailUrls = new Hashtable();
		
		try {
			setThumbnails(obj.getJSONObject("thumbnails"));
		} catch (JSONException e1) {
			L.i("Could not initialize thumbnails in GenericProductModel.", e1.toString());
			L.i("With JSONObject", obj.toString());
		}
		
		try {
			setCategory(obj.getJSONObject("category"));
		} catch (JSONException e1) {
			L.i("Could not initialize categories in GenericProductModel.", e1.toString());
			L.i("With JSONObject", obj.toString());
		}
		
		try {
			setGenres(obj.getJSONArray("genres"));
		} catch (JSONException e1) {
			L.i("Could not initialize genres in GenericProductModel.", e1.toString());
			L.i("With JSONObject", obj.toString());
		}
		
		try {
			this.name = obj.getString("name");
			this.id = obj.getInt("id");
		} catch (JSONException e1) {
			L.i("Could not initialize name or id in GenericProductModel.", e1.toString());
			L.i("With JSONObject", obj.toString());
		}
	
		L.i("This GenericProductModel", this.toString());
		
	}
	

		
	/**
	 * Parses thumbnail URLs.
	 * @param thumbnailURLs
	 */
	public void setThumbnails(JSONObject urls) throws JSONException {
		Enumeration keys = urls.keys();
		String key;
		String url;
		
		while(keys.hasMoreElements()) {
			key = (String) keys.nextElement();
			url = (String) urls.get(key);
			thumbnailUrls.put(key, url);
		}
	}
	
	/**
	 * Size is one of these strings: "50x50", "100x100", "200x200", "320x320"
	 * @param size
	 * @return
	 */
	public String getThumbnailUrl(String size) {
		if(!thumbnailUrls.containsKey(size)) {
			L.i("Given thumbnail size was not found. Size ", size);
			return null;
		}
		return (String) thumbnailUrls.get(size);
	}
	
	/**
	 * Parses genres from a JSONArray that contains JSONObjects. Each
	 * JSONObject represents a genre.
	 * 
	 * @param genresAsJSONArray
	 * @throws JSONException
	 */
	public void setGenres(JSONArray genresAsJSONArray) throws JSONException {
		int loopMax = genresAsJSONArray.length();
		JSONObject tmpGenre;

		if(genres == null) {
			genres = new Vector();
		}
		
		for(int i = 0; i < loopMax; i++) {
			tmpGenre = genresAsJSONArray.getJSONObject(i);
			genres.addElement(
					new GenreModel(
							tmpGenre.getString("id"),
							tmpGenre.getString("name")
							)
					);
		}
	}
	
	/**
	 * Setter for album name.
	 * @param n
	 */
	public void setName(String n) {
		name = n;
	}

	/**
	 * Parses the category from a JSONObject.
	 * 
	 * @param categoryAsJSONObject
	 * @throws JSONException
	 */
	public void setCategory(JSONObject categoryAsJSONObject) throws JSONException {
		CategoryModel categoryModel = new CategoryModel();
		categoryModel.id = categoryAsJSONObject.getString("id");
		categoryModel.name = categoryAsJSONObject.getString("name");
	}

	public String getGenres() {
		String genres = "";
		if(this.genres != null) {
			int loopMax = this.genres.size();
			
			for(int i = 0; i < loopMax; i++) {
				genres += ((GenreModel) this.genres.elementAt(i)).name;
				if(i < loopMax - 1) {
					genres += ", ";
				}
			}			
		} else {
			L.i("Genres not initialized.", this.toString());
		}
		return genres;
	}

}
