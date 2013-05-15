/**
* Copyright (c) 2013 Nokia Corporation. All rights reserved.
* Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
* Oracle and Java are trademarks or registered trademarks of Oracle and/or its
* affiliates. Other product and company names mentioned herein may be trademarks
* or trade names of their respective owners. 
* See LICENSE.TXT for license information.
*/

package com.nokia.example.musicexplorer.ui;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;
import javax.microedition.lcdui.ItemStateListener;
import javax.microedition.lcdui.StringItem;

import java.util.Vector;

import org.json.me.JSONObject;
import org.json.me.JSONArray;
import org.json.me.JSONException;

import org.tantalum.Task;
import org.tantalum.util.L;

import com.nokia.example.musicexplorer.data.QueryPager;
import com.nokia.example.musicexplorer.data.ApiCache;
import com.nokia.example.musicexplorer.data.model.GenericProductModel;
import com.nokia.example.musicexplorer.data.model.AlbumModel;

/**
 * Form based view for displaying album cover thumbnails.
 */
public class AlbumGridView
    extends Form
    implements 
    	CommandListener, 
    	ItemStateListener,
    	ItemCommandListener {

	public static final int ITEMS_PER_PAGE = 9;
	
    protected QueryPager queryPager;
    protected Vector viewModel;

    private static final int SCREEN_WIDTH_IN_PORTRAIT = 230;
    private final ViewManager viewManager;
    private final Command backCommand;
    private GridLayout grid;
    private StringItem loadMoreItem;
	private int loadMoreItemIndex;
	private Command loadMoreCommand;
    
    /**
     * Constructor which sets the view title, adds a back command to it and adds
     * the dummy text content to it.
     * @param viewTitle Title shown in the title bar of this view
     * @param viewManager View manager which will handle view switching
     */
    public AlbumGridView(ViewManager viewManager, String title) {
    	super(title);
    	
    	this.viewManager = viewManager;
    	this.backCommand = new Command("Back", Command.BACK, 1);
        
    	// Initialize the load more button.
    	this.loadMoreItem = new StringItem(null, "Load more...", Item.BUTTON);
    	this.loadMoreCommand = new Command("Load more", Command.ITEM, 1);
    	this.loadMoreItem.setDefaultCommand(loadMoreCommand);
    	this.loadMoreItem.setItemCommandListener(this);        
        
    	// Initialize the query pager.
    	this.queryPager = new QueryPager();
    	this.queryPager.setItemsPerPage(ITEMS_PER_PAGE);
    	
        this.grid = new GridLayout(SCREEN_WIDTH_IN_PORTRAIT, this.viewManager);
        
        append(this.grid);
        
        loadDataset();
        
        addCommand(backCommand);
        setCommandListener(this);
        setItemStateListener(this);
    }
        
    /**
     * @see javax.microedition.ItemStateListener#itemStateChanged(Item)
     */
    public void itemStateChanged(Item item) {
    }
   
    /**
     * Adjusts the grid size according to the display size.
     *
     * @see javax.microedition.lcdui.Displayable#sizeChanged(int, int)
     */
    public void sizeChanged(int w, int h) {
    }
    
    /**
     * Implementation of a required commandAction method from CommandListener
     * interface. Handles eg. hardware back button press event.
     * @param command The command which was fired
     * @param displayable The view from where the command originated (in this
     *        case it is this view itself)
     * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command,
     *      javax.microedition.lcdui.Displayable)
     */
    public void commandAction(Command command, Displayable displayable) {
    	if (backCommand.equals(command)) {
            // Hardware back button was pressed
            viewManager.goBack();
        }
    }
        
    /**
     * Converts JSON items to AlbumModels and places them to Vector viewModel.
     * @param items
     * @throws JSONException
     */
    protected void addItemsToGrid(JSONArray items) throws JSONException {
    	int loopMax = items.length();
    	JSONObject tmp;
    	
    	for(int i = 0; i < loopMax; i++) {
    		tmp = (JSONObject) items.get(i);
    		this.grid.addItem(new AlbumModel(tmp));
    	}
    }
    
    /**
     * Parses a JSONObject for use in grid view.
     * @param model
     */
    protected void parseJSONForView(JSONObject model) {
    	try {
    		addItemsToGrid(model.getJSONArray("items"));
        	this.queryPager.setPaging(model.getJSONObject("paging"));
        	
        	if(queryPager.hasMorePages()) {
        		loadMoreItemIndex = this.append(this.loadMoreItem);
        	} else {
        		delete(this.loadMoreItemIndex);
        	}        	
        	
    	} catch(JSONException e) {
    		L.e("Error while parsing items to JSON.", "", e);
    	}
    }
    
    /**
     * Appends items to the grid.

    protected void appendToList(Vector newItems) {
    	int loopMax = newItems.size();
    	
    	if(grid != null) {
        	for(int i = loopMax - 1; i >= 0; i--) {
        		grid.addItem((GenericProductModel) newItems.elementAt(i));
        	}


    	}
    }
          */   
    
    protected class PlaceResultsTask extends Task {
    	public PlaceResultsTask() {
    		super(Task.NORMAL_PRIORITY);
    	}
    	
    	public Object exec(Object response) {
    		if(response instanceof JSONObject) {
    			parseJSONForView((JSONObject) response);
    		}
    		return response;
    	}
    }

    protected void loadDataset() {
    	L.i("Load dataset not implemented", "");
    }
    
    protected void loadNextDataset() {
    	L.i("Load next data set not implemented", "");
    }
    
	public void commandAction(Command c, Item item) {
		if(c == this.loadMoreCommand) {
			// Load more triggered
			loadNextDataset();
		}
	}
}
