/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nokia.example.musicexplorer.ui;

import com.nokia.example.musicexplorer.data.ApiCache;
import com.nokia.example.musicexplorer.data.QueryPager;
import com.nokia.example.musicexplorer.data.model.ArtistModel;
import com.nokia.example.musicexplorer.data.model.GenericProductModel;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.tantalum.CancellationException;
import org.tantalum.Task;
import org.tantalum.TimeoutException;
import org.tantalum.util.L;

/**
 *
 * @author jukka_000
 */
public class SimilarArtistsView 
        extends Form
        implements ItemCommandListener {
    
    private QueryPager queryPager;
    private LoadMoreButton loadMoreButton;
    private ViewManager viewManager;
    private int artistId = 0;
    
    public SimilarArtistsView(ViewManager viewManager, int artistId) {
        super("Artists you might like...");
        this.queryPager = new QueryPager();
        this.loadMoreButton = new LoadMoreButton(this);
        this.viewManager = viewManager;
        this.artistId = artistId;
        
        getSimilarArtists();
    }

    public void getSimilarArtists() {
        ApiCache.getSimilarArtistsById(artistId, new PlaceResultsTask(), queryPager.getCurrentQueryString());
    }

    public void commandAction(Command c, Item item) {
        if(c == loadMoreButton.getCommand()) {
            ApiCache.getSimilarArtistsById(artistId, new PlaceResultsTask(), queryPager.getQueryStringForNextPage());
        }
    }
    
    public class PlaceResultsTask 
            extends Task {
        
        public PlaceResultsTask() {
        }

        protected Object exec(Object response) {
            if(response instanceof JSONObject) {
                try {
                    
                    // Parse JSON to ArtistModels and append as ListItems
                    JSONObject paging = ((JSONObject) response).getJSONObject("paging");
                    queryPager.setPaging(paging);
                    
                    // If has load more button, remove it before appending items
                    loadMoreButton.remove();
                    
                    JSONArray items = ((JSONObject) response).getJSONArray("items");
                    int loopMax = items.length();
                    
                    for(int i = 0; i < loopMax; i++) {
                        JSONObject artist = (JSONObject) items.get(i);
                        ArtistModel artistModel = new ArtistModel(artist);
                        ListItem listItem = 
                                new ListItem(viewManager, artistModel);
                        
                        append(listItem);
                    }
                    
                    if(queryPager.hasMorePages()) {
                        loadMoreButton.append();
                    } else {
                        // Prevent showing the button if there are no more pages
                        loadMoreButton.remove();
                    }
                    
                } catch(JSONException e) {
                    L.e("Could not parse similar artists.", "", e);
                }
                
            }
            return response;
        }
        
    }
    
    
}
