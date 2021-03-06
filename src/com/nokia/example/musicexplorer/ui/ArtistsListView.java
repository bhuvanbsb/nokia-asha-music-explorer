/**
 * Copyright (c) 2013 Nokia Corporation. All rights reserved. Nokia and Nokia
 * Connecting People are registered trademarks of Nokia Corporation. Oracle and
 * Java are trademarks or registered trademarks of Oracle and/or its affiliates.
 * Other product and company names mentioned herein may be trademarks or trade
 * names of their respective owners. See LICENSE.TXT for license information.
 */

package com.nokia.example.musicexplorer.ui;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.nokia.example.musicexplorer.data.ApiCache;
import com.nokia.example.musicexplorer.data.model.ArtistModel;
import com.nokia.example.musicexplorer.data.model.GenreModel;

/**
 * Displays similar artists as ListItems for a given artist.
 */
public class ArtistsListView 
        extends ListItemView
        implements CommandListener {

    private String genreId = "";
    private final Command backCommand;

    /**
     * Constructor.
     * @param viewManager
     * @param genreModel
     */
    public ArtistsListView(ViewManager viewManager, GenreModel genreModel) {
        super(viewManager, "Artists in " + genreModel.name);
        
        this.genreId = genreModel.id;
        this.backCommand = new Command("Back", Command.BACK, 1);
        addCommand(backCommand);
        setCommandListener(this);
        
        loadDataset();
    }

    /**
     * @see com.nokia.example.musicexplorer.ui.ListItemView#loadDataset()
     */
    protected void loadDataset() {
        ApiCache.getArtistsInGenre(genreId, new PlaceResultsTask(), queryPager.getCurrentQueryString());
    }

    /**
     * @see com.nokia.example.musicexplorer.ui.ListItemView#loadNextDataset()
     */
    protected void loadNextDataset() {
        ApiCache.getArtistsInGenre(genreId, new PlaceResultsTask(), queryPager.getQueryStringForNextPage());
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

    /**
     * @see javax.microedition.lcdui.CommandListener#commandAction(
     * javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
     */
    public void commandAction(Command c, Displayable d) {
        if (backCommand.equals(c)) {
            // Hardware back button was pressed
            viewManager.goBack();
        }
    }
}
