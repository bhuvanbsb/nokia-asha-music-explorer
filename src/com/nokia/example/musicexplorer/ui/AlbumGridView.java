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
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;
import javax.microedition.lcdui.ItemStateListener;

import java.util.Vector;

import org.json.me.JSONObject;
import org.json.me.JSONArray;
import org.json.me.JSONException;

import org.tantalum.Task;
import org.tantalum.util.L;

import com.nokia.example.musicexplorer.data.QueryPager;
import com.nokia.example.musicexplorer.data.model.AlbumModel;

/**
 * A form based abstract view for displaying album cover thumbnails in different
 * contexts by extending this class.
 */
public abstract class AlbumGridView
        extends Form
        implements
        CommandListener,
        ItemStateListener,
        ItemCommandListener {

    public static final int ITEMS_PER_PAGE = 9;
    protected QueryPager queryPager;
    protected Vector viewModel;
    protected final ViewManager viewManager;
    protected GridLayout grid;

    private static final int SCREEN_WIDTH_IN_PORTRAIT = 240;
    private final Command backCommand;
    private int loadMoreButtonIndex = -1;
    private LoadMoreButton loadMoreButton;
        
    public AlbumGridView(ViewManager viewManager, String title, boolean showMoreByArtistButton) {
        super(title);

        this.viewManager = viewManager;
        this.backCommand = new Command("Back", Command.BACK, 1);
        this.loadMoreButton = new LoadMoreButton(this);

        // Initialize the query pager.
        this.queryPager = new QueryPager();
        this.queryPager.setItemsPerPage(ITEMS_PER_PAGE);

        // Initialize the grid layout.
        this.grid = new GridLayout(SCREEN_WIDTH_IN_PORTRAIT, this.viewManager);

        if (!showMoreByArtistButton) {
            this.grid.disableShowMoreByArtistButtonInAlbumViews();
        }
        
        addCommand(backCommand);
        setCommandListener(this);
        setItemStateListener(this);
    }

    public void itemStateChanged(Item item) {
    }

    public void sizeChanged(int w, int h) {
    }

    public void commandAction(Command c, Item item) {
        if (c == loadMoreButton.getCommand()) {
            // Load more triggered
            loadNextDataset();
        }
    }

    public void commandAction(Command command, Displayable displayable) {
        if (backCommand.equals(command)) {
            // Hardware back button was pressed
            viewManager.goBack();
        }
    }

    protected void appendGrid() {
        append(grid);
    }

    /**
     * Converts JSON items to AlbumModels and places them to Vector viewModel.
     *
     * @param items
     * @throws JSONException
     */
    protected void addItemsToGrid(JSONArray items) throws JSONException {
        int loopMax = items.length();
        JSONObject tmp;

        for (int i = 0; i < loopMax; i++) {
            tmp = (JSONObject) items.get(i);
            this.grid.addItem(new AlbumModel(tmp));
        }
    }

    /**
     * Deletes the current load more button.
     */
    private void deleteLoadMoreButton() {
        if (loadMoreButtonIndex >= 0) {
            delete(loadMoreButtonIndex);
            loadMoreButtonIndex = -1;
        }
    }

    /**
     * Parses a JSONObject for use in grid view.
     *
     * @param model
     */
    protected void parseJSONForView(JSONObject model) {
        try {
            addItemsToGrid(model.getJSONArray("items"));
            this.queryPager.setPaging(model.getJSONObject("paging"));
            notifyTotalUpdated();
            
            deleteLoadMoreButton();

            if (queryPager.hasMorePages()) {
                loadMoreButtonIndex = this.append(loadMoreButton.getButton());
            }
        } catch (JSONException e) {
            L.e("Error while parsing items to JSON.", "", e);
        }
    }

    protected void notifyTotalUpdated() {
        // Overridden in ArtistView
    }
    
    /**
     * Callback task for parsing the JSON response for the view.
     */
    protected class PlaceResultsTask extends Task {

        public PlaceResultsTask() {
            super(Task.NORMAL_PRIORITY);
        }

        public Object exec(Object response) {
            if (response instanceof JSONObject) {
                parseJSONForView((JSONObject) response);
            }
            return response;
        }
    }

    /**
     * Load the first set of data.
     */
    protected void loadDataset() {
        L.i("Load dataset not implemented", "");
    }

    /**
     * Load the next set of data.
     */
    protected void loadNextDataset() {
        L.i("Load next data set not implemented", "");
    }
}
