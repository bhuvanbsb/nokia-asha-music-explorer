/**
 * Copyright (c) 2013 Nokia Corporation. All rights reserved. Nokia and Nokia
 * Connecting People are registered trademarks of Nokia Corporation. Oracle and
 * Java are trademarks or registered trademarks of Oracle and/or its affiliates.
 * Other product and company names mentioned herein may be trademarks or trade
 * names of their respective owners. See LICENSE.TXT for license information.
 */
package com.nokia.example.musicexplorer.ui;

import com.nokia.example.musicexplorer.data.ApiCache;

/**
 * Displays new releases using the New Releases Resource.
 */
public class NewReleasesView
        extends AlbumGridView {

    public static final String VIEW_TITLE = "See what's new";
    public static final String PATH_TO_ICON = "/new_releases_icon.png";
    
    /**
     * Factory method for instantiating.
     * @param viewManager
     * @return 
     */
    public static NewReleasesView getViewInstance(ViewManager viewManager) {
        return new NewReleasesView(viewManager);
    }
    
    public NewReleasesView(ViewManager viewManager) {
        super(viewManager, VIEW_TITLE, true);

    }
    
    protected void loadDataset() {
        ApiCache.getNewReleases(
                new AlbumGridView.PlaceResultsTask(),
                super.queryPager.getCurrentQueryString());
    }

    protected void loadNextDataset() {
        ApiCache.getNewReleases(
                new AlbumGridView.PlaceResultsTask(),
                super.queryPager.getQueryStringForNextPage());
    }
}
