/**
 * Copyright (c) 2013 Nokia Corporation. All rights reserved. Nokia and Nokia
 * Connecting People are registered trademarks of Nokia Corporation. Oracle and
 * Java are trademarks or registered trademarks of Oracle and/or its affiliates.
 * Other product and company names mentioned herein may be trademarks or trade
 * names of their respective owners. See LICENSE.TXT for license information.
 */

package com.nokia.example.musicexplorer.ui;

import javax.microedition.lcdui.ItemCommandListener;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.nokia.example.musicexplorer.data.ApiCache;
import com.nokia.example.musicexplorer.data.model.ArtistModel;

/**
 * Displays similar artists as ListItems for a given artist.
 */
public class SimilarArtistsView 
        extends ListItemView
        implements ItemCommandListener {

    private int artistId = 0;

    /**
     * Constructor.
     * @param viewManager
     * @param artistId
     */
    public SimilarArtistsView(ViewManager viewManager, int artistId) {
        super(viewManager, "Similar artists");
        
        this.artistId = artistId;
        
        loadDataset();
    }

    /**
     * @see com.nokia.example.musicexplorer.ui.ListItemView#loadDataset()
     */
    protected void loadDataset() {
        ApiCache.getSimilarArtistsById(artistId, new PlaceResultsTask(), queryPager.getCurrentQueryString());
    }

    /**
     * @see com.nokia.example.musicexplorer.ui.ListItemView#loadNextDataset()
     */
    protected void loadNextDataset() {
        ApiCache.getSimilarArtistsById(artistId, new PlaceResultsTask(), queryPager.getQueryStringForNextPage());
    }

    /**
     * @see com.nokia.example.musicexplorer.ui.ListItemView#parseAndAppendToView(org.json.me.JSONObject)
     */
    protected void parseAndAppendToView(JSONObject response) throws JSONException {
        JSONArray items = response.getJSONArray("items");
        int loopMax = items.length();
        
        for (int i = 0; i < loopMax; i++) {
            JSONObject artist = (JSONObject) items.get(i);
            ArtistModel artistModel = new ArtistModel(artist);
            ListItem listItem = new ListItem(viewManager, artistModel);
            append(listItem);
        }
    }
}
