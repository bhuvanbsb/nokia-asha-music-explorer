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
 * Displays popular releases using the Charts Resource.
 */
public class PopularReleasesView extends AlbumGridView {

    public static final String VIEW_TITLE = "Popular albums";
    public static final String PATH_TO_ICON = "/popular_icon.png";

    /**
     * Constructor.
     * @param viewManager
     */
    public PopularReleasesView(ViewManager viewManager) {
        super(viewManager, VIEW_TITLE, true);
    }

    /**
     * @see com.nokia.example.musicexplorer.ui.AlbumGridView#loadDataset()
     */
    protected void loadDataset() {
        ApiCache.getPopularReleases(
                new AlbumGridView.PlaceResultsTask(),
                super.queryPager.getCurrentQueryString());
    }

    /**
     * @see com.nokia.example.musicexplorer.ui.AlbumGridView#loadNextDataset()
     */
    protected void loadNextDataset() {
        String queryString = super.queryPager.getQueryStringForNextPage();
        
        ApiCache.getPopularReleases(
                new AlbumGridView.PlaceResultsTask(),
                queryString);
    }
}
