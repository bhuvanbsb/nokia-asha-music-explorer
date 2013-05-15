/**
* Copyright (c) 2013 Nokia Corporation. All rights reserved.
* Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
* Oracle and Java are trademarks or registered trademarks of Oracle and/or its
* affiliates. Other product and company names mentioned herein may be trademarks
* or trade names of their respective owners. 
* See LICENSE.TXT for license information.
*/

package com.nokia.example.musicexplorer.ui;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.StringItem;

import java.util.Vector;
import java.lang.Math;
import javax.microedition.lcdui.CustomItem;
import javax.microedition.lcdui.ItemStateListener;

import com.nokia.example.musicexplorer.data.QueryPager;
import com.nokia.example.musicexplorer.data.ApiCache;
import com.nokia.example.musicexplorer.data.model.GenericProductModel;
import com.nokia.example.musicexplorer.data.model.AlbumModel;
import com.nokia.example.musicexplorer.data.model.ArtistModel;
import com.nokia.example.musicexplorer.ui.AlbumView;
import com.nokia.example.musicexplorer.ui.GridItem;

import org.tantalum.Task;
import org.tantalum.net.json.JSONModel;
import org.json.me.JSONObject;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.tantalum.util.L;


/**
 * An example Form-based view, which just shows a single text line in the form
 * content area. Implements CommandLister interface to be able to detect back
 * button presses, so it can ask ViewManager to show the previous view.
 */
public class ArtistView
    extends Form
    implements CommandListener, ItemStateListener {


    // Class members
    private final ViewManager viewManager;
    private final Command backCommand;
    
    private static final int GRID_LAYOUT_COLUMN_COUNT_IN_PORTRAIT =
            GridLayout.DEFAULT_COLUMN_COUNT;
    private static final int GRID_LAYOUT_COLUMN_COUNT_IN_LANDSCAPE =
            GRID_LAYOUT_COLUMN_COUNT_IN_PORTRAIT + 1;
    private static final int SCREEN_WIDTH_IN_PORTRAIT = 240;
    private static final int GRID_ITEM_COUNT = 15;
    
    private Vector viewModel;
    private QueryPager queryPager;
    private GridLayout grid;
    private ArtistModel artistModel;
    private ListItem headerItem;
    
    /**
     * Constructor which sets the view title, adds a back command to it and adds
     * the dummy text content to it.
     * @param viewTitle Title shown in the title bar of this view
     * @param viewManager View manager which will handle view switching
     */
    public ArtistView(ViewManager viewManager, ArtistModel artistModel) {
        super(null);
        this.artistModel = artistModel;
        this.viewManager = viewManager;
        this.backCommand = new Command("Back", Command.BACK, 1);
        this.grid = new GridLayout(this.getWidth(), this.viewManager); // TODO: getWidth() gives something under 220 and leaves too much space to the right edge?

        try {
			this.headerItem = new ListItem(
					viewManager,
					artistModel
				);
			this.headerItem.disablePointer();
			
			append(this.headerItem);
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        append(this.grid);

        addCommand(backCommand);
        setCommandListener(this);

        // TODO: Enable paging by passing a QueryPager?
        ApiCache.getAlbumsForArtist(this.artistModel.id, new PlaceResultsTask(this));
    }
        
    /**
     * @see javax.microedition.ItemStateListener#itemStateChanged(Item)
     */
    public void itemStateChanged(Item item) {
    	L.i("Item state changed: ", "?!??!?!");
    }
   
    /**
     * Adjusts the grid size according to the display size.
     *
     * @see javax.microedition.lcdui.Displayable#sizeChanged(int, int)
     */
    public void sizeChanged(int w, int h) {
    }
    
    /**
     * Converts JSON items to AlbumModels and places them to Vector viewModel.
     * @param items
     * @throws JSONException
     */
    protected void convertItemsToModels(JSONArray items) throws JSONException {
    	int loopMax = items.length();
    	JSONObject tmp;
    	
    	for(int i = 0; i < loopMax; i++) {
    		tmp = (JSONObject) items.get(i);
    		this.viewModel.addElement(new AlbumModel(tmp));
    	}
    }
    
    protected void parseJSONForView(JSONObject model) {
    	if(viewModel == null) {
    		viewModel = new Vector();
    	}
    	
    	/*
    	 * Structure of the response
    	 * -------------------------
    	 * 
    	 * JSONArray items 			-> In this case this holds albums
    	 * 		JSONObject album	-> An array item represents an album
    	 * 
    	 * JSONObject paging 		-> Details for making paged queries
    	 * 		int startindex		-> first response 0
    	 * 		int total 			-> (amount of items)
    	 * 		int itemsperpage	-> (defaults to 10)
    	 * 
    	 * String name 				-> Name of the listing
    	 * 
    	 * String type 				-> Type of the response, in this case "musiccollectionlist"
    	 * 
    	 * JSONObject category 		-> The category where the response belongs to
    	 * 		String name			-> Localized category name
    	 * 		String id			-> Generic category name/id
    	 * 
    	 * int id					-> An id for the listing
    	 */
    	
    	try {
    		convertItemsToModels(model.getJSONArray("items"));
        	
        	this.queryPager = new QueryPager(model.getJSONObject("paging"));
    	
    	} catch(Exception exception) {
    		L.e("Error parsing items", "", exception);
    	}
   	
    }
    
    /**
     * Appends items to list. Detects if there are more pages.
     */
    protected void appendToList() {
    	int loopMax = viewModel.size();
    	if(grid != null) {
        	for(int i = loopMax - 1; i >= 0; i--) {
        		//append(viewModel.elementAt(i).toString());
        		grid.addItem((GenericProductModel) viewModel.elementAt(i));
        	}
        	// TODO: Implement a "Load more" button to the end of each paged query.
    	}
    }
    
    // TODO: Extend these tasks as they have all similar constructors?
    public class PlaceResultsTask extends Task {
    	private Form parentView;
    	public PlaceResultsTask(Form view) {
    		super(Task.NORMAL_PRIORITY);
    		parentView = view;
    	}
    	public Object exec(Object response) {
    		if(response instanceof JSONObject) {
    			((ArtistView) parentView).parseJSONForView((JSONObject) response);
    			((ArtistView) parentView).appendToList();
    		}
    		return response;
    	}
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
        } else {        
        	
        	/*
        	L.i("Index: ", Integer.toString(getSelectedIndex()));
        	int index = getSelectedIndex();

        	
        	AlbumModel album = (AlbumModel) viewModel.elementAt(index);
        	viewManager.showView(new AlbumView(viewManager, album));
        	*/
        }
        
    }
}
