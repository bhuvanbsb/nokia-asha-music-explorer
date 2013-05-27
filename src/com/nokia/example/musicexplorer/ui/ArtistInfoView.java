/**
 * Copyright (c) 2013 Nokia Corporation. All rights reserved. Nokia and Nokia
 * Connecting People are registered trademarks of Nokia Corporation. Oracle and
 * Java are trademarks or registered trademarks of Oracle and/or its affiliates.
 * Other product and company names mentioned herein may be trademarks or trade
 * names of their respective owners. See LICENSE.TXT for license information.
 */
package com.nokia.example.musicexplorer.ui;

import com.nokia.example.musicexplorer.data.ApiCache;
import com.nokia.example.musicexplorer.data.model.ArtistModel;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.tantalum.CancellationException;
import org.tantalum.Task;
import org.tantalum.TimeoutException;
import org.tantalum.util.L;

/**
 * Implements the artist view.
 */
public class ArtistInfoView
        extends AlbumGridView {

    private ArtistModel artistModel;
    private ListItem headerItem;
    private ArtistView artistView;
    private int artistId = 0;

    public ArtistInfoView(ViewManager viewManager, ArtistModel artistModel) {
        super(viewManager, null, false);

        this.artistModel = artistModel;
        
        initializeHeaderItem();
        loadDataset();
        appendItems();
    }

    public ArtistInfoView(ViewManager viewManager, int artistId, ArtistView artistView) {
        super(viewManager, null, false);
        this.artistView = artistView;
        this.artistId = artistId;
        getArtistDetailsById();
    }

    private void initializeHeaderItem() {
        if(artistModel != null && super.viewManager != null) {
            this.headerItem = new ListItem(super.viewManager, artistModel);
            this.headerItem.disablePointer();            
        }
    }
    
    private void appendItems() {
        if(headerItem != null) {
            append(headerItem);
            appendGrid();       
        }
    }
    
    protected void notifyTotalUpdated() {
        String text = Integer.toString(super.queryPager.getTotal()) + " albums";
        this.headerItem.setAlbumOrTrackAmountText(text);
    }
    
    protected void getArtistDetailsById() {
        /*
         * Artist model is initialized in PlaceArtistTask.
         * After the model is initialized the rest of the view is constructed.
         */
        ApiCache.getArtistDetailsById(
                artistId,
                new PlaceArtistTask());
    }
    
    protected class PlaceArtistTask
            extends Task {

        /**
         * Gets in a JSON response that needs to parsed to an artist model.
         * @param response
         * @return 
         */
        protected Object exec(Object response) {
            if(artistView != null && 
                    response != null && 
                    response instanceof JSONObject) {
                
                try {
                    // Response is first of items array {items:[{..}]}
                    JSONArray items = ((JSONObject) response).getJSONArray("items");
                    
                    if(items.length() > 0) {
                        JSONObject artist = (JSONObject) items.get(0); // Get the first one. API should return only one result.
                        artistModel = new ArtistModel(artist);
                        
                        /*
                         * Store the artist model to the ArtistView that was
                         * passed to the constructor.
                         */
                        artistView.setArtistModel(artistModel);
                        
                        /* 
                         * After the artist model is initialized, we can 
                         * continue to initialize the rest of the view.
                         */ 
                        initializeHeaderItem();
                        loadDataset();
                        appendItems();
                    }
                    
                } catch (JSONException e) {
                    L.e("Could not parse artist model from JSON.", "", e);
                }
                
            } else {
                L.i("Response null or not JSONObject", response != null ? response.toString() : "is null");
            }

            return response;
        }
    
    }

            /**
     * Used by the grid layout to fill itself with albums by the artist.
     */
    protected void loadDataset() {
        if(this.artistModel != null) {
            ApiCache.getAlbumsForArtist(
                    this.artistModel.id,
                    new PlaceResultsTask(),
                    super.queryPager.getCurrentQueryString());
        }
    }
    
    /**
     * Used by the grid layout to load more albums by the artist.
     */
    protected void loadNextDataset() {
        if(this.artistModel != null) {
            ApiCache.getAlbumsForArtist(
                    this.artistModel.id,
                    new PlaceResultsTask(),
                    super.queryPager.getQueryStringForNextPage());            
        }
    }
}
