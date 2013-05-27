/**
 * Copyright (c) 2013 Nokia Corporation. All rights reserved. Nokia and Nokia
 * Connecting People are registered trademarks of Nokia Corporation. Oracle and
 * Java are trademarks or registered trademarks of Oracle and/or its affiliates.
 * Other product and company names mentioned herein may be trademarks or trade
 * names of their respective owners. See LICENSE.TXT for license information.
 */
package com.nokia.example.musicexplorer.ui;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import org.tantalum.Task;
import org.tantalum.util.L;

import com.nokia.example.musicexplorer.data.QueryPager;

/**
 * For implementing a list using ListItems. Enables paging.
 */
public abstract class ListItemView 
        extends Form
        implements ItemCommandListener {
    
    protected QueryPager queryPager;
    protected LoadMoreButton loadMoreButton;
    protected ViewManager viewManager;
    
    public ListItemView(ViewManager viewManager, String title) {
        super(title);
        this.viewManager = viewManager;
        this.queryPager = new QueryPager();
        this.loadMoreButton = new LoadMoreButton(this);
        this.viewManager = viewManager;
    }

    protected abstract void loadDataset();
    protected abstract void loadNextDataset();
    protected abstract void parseAndAppendToView(JSONObject response) throws JSONException;
    
    public void commandAction(Command c, Item item) {
        if (c == loadMoreButton.getCommand()) {
            loadNextDataset();
        }
    }
    
    public class PlaceResultsTask 
            extends Task {
        
        public PlaceResultsTask() {
        }

        protected Object exec(Object response) {
            if (response instanceof JSONObject) {
                try {
                    JSONObject paging = ((JSONObject) response).getJSONObject("paging");
                    queryPager.setPaging(paging);
                    
                    // If has load more button, remove it before appending items
                    loadMoreButton.remove();
                    
                    parseAndAppendToView((JSONObject) response);
                    
                    if (queryPager.hasMorePages()) {
                        loadMoreButton.append();
                    } else {
                        // Prevent showing the button if there are no more pages
                        loadMoreButton.remove();
                    }
                } catch(JSONException e) {
                    L.e("Could not parse response.", "", e);
                }
            }
            return response;
        }
    }
}
