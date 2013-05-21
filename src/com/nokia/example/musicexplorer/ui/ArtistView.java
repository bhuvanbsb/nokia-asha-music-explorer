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

/**
 * Implements the artist view.
 */
public class ArtistView
        extends AlbumGridView {

    private ArtistModel artistModel;
    private ListItem headerItem;

    public ArtistView(ViewManager viewManager, ArtistModel artistModel) {
        super(viewManager, null);

        this.artistModel = artistModel;
        this.headerItem = new ListItem(super.viewManager, artistModel);
        this.headerItem.disablePointer();

        append(headerItem);
        appendGrid();
        loadDataset();
    }

    public ArtistView(ViewManager viewManager, int performerId) {
        super(viewManager, null);

        /* TODO:
         * 1. Display something
         * 2. Fetch the artist model
         * 3. Initialize grid
         * 4. Others as usual
         */
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    protected void notifyTotalUpdated() {
        String text = Integer.toString(super.queryPager.getTotal()) + " albums";
        this.headerItem.setAlbumOrTrackAmountText(text);
    }
    
    protected void loadDataset() {
        ApiCache.getAlbumsForArtist(
                this.artistModel.id,
                new PlaceResultsTask(),
                super.queryPager.getCurrentQueryString());
    }

    protected void loadNextDataset() {
        ApiCache.getAlbumsForArtist(
                this.artistModel.id,
                new PlaceResultsTask(),
                super.queryPager.getQueryStringForNextPage());
    }
}
