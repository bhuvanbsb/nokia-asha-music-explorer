/**
* Copyright (c) 2013 Nokia Corporation. All rights reserved.
* Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
* Oracle and Java are trademarks or registered trademarks of Oracle and/or its
* affiliates. Other product and company names mentioned herein may be trademarks
* or trade names of their respective owners. 
* See LICENSE.TXT for license information.
*/

package com.nokia.example.musicexplorer.data.model;

import org.json.me.JSONObject;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.tantalum.CancellationException;
import org.tantalum.Task;
import org.tantalum.TimeoutException;
import org.tantalum.util.L;
import java.util.Vector;

import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;

import com.nokia.example.musicexplorer.data.ApiCache;
import com.nokia.example.musicexplorer.ui.AlbumView;
import com.nokia.example.musicexplorer.ui.NewReleasesView;

public class AlbumModel extends GenericProductModel {
	
	public Vector tracks;
	
	/**
	 * Initializes an AlbumModel based on a JSONObject.
	 * @param album
	 * @throws JSONException
	 */
	public AlbumModel(JSONObject album) throws JSONException {
		super(album);
		this.tracks = new Vector();
		//getTracks();
		//setTracks(album.getJSONArray("tracks"));
	}
	
	public class GetTracksTask extends Task {
		private AlbumModel album;
		private AlbumView albumView;

		public GetTracksTask(AlbumModel albumModel, AlbumView albumView) {
			super(Task.NORMAL_PRIORITY);
			this.album = albumModel;
			this.albumView = albumView;
		}
		public Object exec(Object response) {  		    		
			try {
				JSONObject obj = (JSONObject) response;
    			this.album.setTracks(obj.getJSONArray("tracks"));
    			this.albumView.appendTracks(this.album);
			}
			catch(JSONException exception) {    				
				L.e("Unable to parse Track in GetTracksTask", "", exception);
				return null;
			}

			return response;
    	}
	}
	
	/**
	 * Fetches tracks asynchronously from the REST API.
	 */
	public void getTracks(AlbumView view) {
		//if(this.tracks == null || this.tracks.isEmpty()) {
			ApiCache.getAlbumDetails(this.id, new GetTracksTask(this, view));			
		//} else {
		//	view.appendTracks(this);
		//}
	}
	
	/**
	 * Parses a tracks array from the JSON response into individual TrackModels.
	 * @param tracksAsJSONArray
	 */
	public void setTracks(JSONArray tracksAsJSONArray) throws JSONException {		
		if(tracksAsJSONArray == null || tracksAsJSONArray.length() == 0) {
			throw new JSONException("Tracks array must contain one or more tracks.");
		}
		
		if(this.tracks == null) {
			this.tracks = new Vector();
		} else {
			this.tracks.removeAllElements();
		}
		
		JSONObject track;
		int loopMax = tracksAsJSONArray.length();
		for(int i = 0; i < loopMax; i++) {
			track = tracksAsJSONArray.getJSONObject(i);
			this.tracks.addElement(new TrackModel(track));
		}
	}
	
	/**
	 * For debugging purposes.
	 * @return String A description of the object.
	 */
	public String toString() {
		return "AlbumModel: name=" + name + ", id=" + id;
	}
}
