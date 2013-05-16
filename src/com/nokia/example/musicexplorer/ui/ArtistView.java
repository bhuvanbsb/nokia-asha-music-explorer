/**
* Copyright (c) 2013 Nokia Corporation. All rights reserved.
* Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
* Oracle and Java are trademarks or registered trademarks of Oracle and/or its
* affiliates. Other product and company names mentioned herein may be trademarks
* or trade names of their respective owners. 
* See LICENSE.TXT for license information.
*/

package com.nokia.example.musicexplorer.ui;

import org.tantalum.util.L;

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
        
		L.i("Getting in artist view", artistModel.toString());
		
		append(headerItem);
		appendGrid();
		loadDataset();
    }
    
	protected void loadDataset() {
		ApiCache.getAlbumsForArtist(
				this.artistModel.id,
				new PlaceResultsTask(), 
				super.queryPager.getCurrentQueryString()
		);
	}
	
	protected void loadNextDataset() {		
		ApiCache.getAlbumsForArtist(
				this.artistModel.id,
				new PlaceResultsTask(), 
				super.queryPager.getQueryStringForNextPage()
		);
	}
    
}
