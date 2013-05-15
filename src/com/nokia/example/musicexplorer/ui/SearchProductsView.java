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
import javax.microedition.lcdui.TextField;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.io.UnsupportedEncodingException;
import java.lang.Math;
import javax.microedition.lcdui.CustomItem;
import javax.microedition.lcdui.ItemStateListener;

import com.nokia.example.musicexplorer.data.QueryPager;
import com.nokia.example.musicexplorer.data.ApiCache;
import com.nokia.example.musicexplorer.data.model.GenericProductModel;
import com.nokia.example.musicexplorer.data.model.AlbumModel;
import com.nokia.example.musicexplorer.data.model.ArtistModel;
import com.nokia.example.musicexplorer.data.model.CategoryModel;
import com.nokia.example.musicexplorer.data.model.TrackModel;
import com.nokia.example.musicexplorer.ui.AlbumView;
import com.nokia.example.musicexplorer.ui.GridItem;

import org.tantalum.Task;
import org.tantalum.net.json.JSONModel;
import org.json.me.JSONObject;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.tantalum.util.L;
import java.util.Date;

/**
 * An example Form-based view, which just shows a single text line in the form
 * content area. Implements CommandLister interface to be able to detect back
 * button presses, so it can ask ViewManager to show the previous view.
 */
public class SearchProductsView
    extends Form
    implements CommandListener, ItemStateListener {

	public static final String title = "Search music";

	
    // Class members
    private final ViewManager viewManager;
    private final Command backCommand;
    
    private static final int GRID_LAYOUT_COLUMN_COUNT_IN_PORTRAIT =
            GridLayout.DEFAULT_COLUMN_COUNT;
    private static final int GRID_LAYOUT_COLUMN_COUNT_IN_LANDSCAPE =
            GRID_LAYOUT_COLUMN_COUNT_IN_PORTRAIT + 1;
    private static final int SCREEN_WIDTH_IN_PORTRAIT = 240;
    private static final int GRID_ITEM_COUNT = 15;
    private static final int MAX_QUERY_LENGTH = 100;
    private static final long QUERY_THROTTLE_IN_MILLISECONDS = 500;
    private static final int MIN_SEARCH_QUERY_LENGTH = 2;
    
    private Vector viewModel; // TODO: Place search result items here?
    private QueryPager queryPager;
    private GridLayout grid;
    private TextField searchField;
    private String searchQuery;
    private Date lastSearchDate;
	private Timer throttle;
    

	
    /**
     * Constructor which sets the view title, adds a back command to it and adds
     * the dummy text content to it.
     * @param viewTitle Title shown in the title bar of this view
     * @param viewManager View manager which will handle view switching
     */
    public SearchProductsView(ViewManager viewManager) {
        super(title);
        this.viewManager = viewManager;
        this.backCommand = new Command("Back", Command.BACK, 1);
        this.searchField = new TextField(null, null, MAX_QUERY_LENGTH, TextField.ANY); // TODO: Prevent line breaks, handle white space? Now white space breaks the search.
        this.lastSearchDate = new Date();
        
        append(this.searchField);
        
        addCommand(backCommand);
        setCommandListener(this);
        setItemStateListener(this);
    }
        
    /**
     * @see javax.microedition.ItemStateListener#itemStateChanged(Item)
     */
    public void itemStateChanged(Item item) {

    	if(item instanceof TextField) {
    		handleSearchField((TextField) item);
    	}

    }
    
	public void setViewModel(Vector vec) {
		this.viewModel = vec;
	}
	
	/**
     * Handles the search TextField actions.
     * @param searchField
     */
    private void handleSearchField(TextField searchField) {
		searchQuery = searchField.getString();
		
		if(searchQuery.length() > MIN_SEARCH_QUERY_LENGTH) {
			throttleSearch();
		}
	}

    /**
     * Delays location search and cancels a possible pending search
     */
    private void throttleSearch() {
        if (throttle != null) {
            throttle.cancel();
        }

        throttle = new Timer();
        throttle.schedule(new TimerTask() {

            public void run() {
                performSearch();
                cancel();
            }
        }, 1000);
    }    
    
	private void performSearch() {
		ApiCache.search(this.searchQuery, new SearchResultHandlerTask(this));
    }    
    
    private class SearchResultHandlerTask extends Task {
    	private SearchProductsView searchView; 

    	public SearchResultHandlerTask(Form searchView) {
    		super(Task.NORMAL_PRIORITY);
    		this.searchView = (SearchProductsView) searchView;
    	}
    	
		public Object exec(Object response) {
			if(response instanceof JSONObject) {
				JSONArray resultsArray;
				JSONObject paging;
				
				try {
					resultsArray = ((JSONObject) response).getJSONArray("items");
					paging = ((JSONObject) response).getJSONObject("paging");
					
					// Set query pager and pass results to parser.
					this.searchView.setPager(new QueryPager(paging));
					this.searchView.setViewModel(parseResults(resultsArray));
					this.searchView.appendToList();
				}
				catch(JSONException ex) {
					L.e("Could not parse JSON", "", ex);
				}
			}
			
			return null;
		}
	    
		private int getCategoryEnum(String category) {
	    	category = category.toLowerCase();

	    	if(category.indexOf("track") > -1) {
	    		return CategoryModel.TRACK;
	    	} else if(category.indexOf("artist") > -1) {
	    		return CategoryModel.ARTIST;
	    	} else if(category.indexOf("album") > -1) {
	    		return CategoryModel.ALBUM;
	    	} else if(category.indexOf("single") > -1) {
	    		return CategoryModel.SINGLE;
	    	}
	    	
	    	return 0;
	    }    
	    
	    private Vector parseResults(JSONArray results) {
			Vector parsed = new Vector();

			/**
			 * Response parsing
			 * 
			 * Get "items" JSONArray and iterate over the JSONObjects.
			 * 1. Check objects "category" JSONObject
			 * 2. Decide which model to instantiate based on the category
			 * 3. Save models (search results) to viewModel vector. 
			 */
	    	int loopMax = results.length();
	    	String category = "";
	    	JSONObject obj;    	
	   	
	    	for(int i = 0; i < loopMax; i++) {
	    		try {
					obj = (JSONObject) results.get(i);
					category = obj.getJSONObject("category").getString("id");

					switch(getCategoryEnum(category.toLowerCase())) {
						case CategoryModel.SINGLE:
							L.i("Got single.", obj.toString());
							parsed.addElement(new AlbumModel(obj));
							break;
						case CategoryModel.ALBUM:
							L.i("Got album.", obj.toString());
							parsed.addElement(new AlbumModel(obj));
							break;
						case CategoryModel.ARTIST:
							L.i("Got artist.", obj.toString());
							parsed.addElement(new ArtistModel(obj));
							break;
						case CategoryModel.TRACK:
							L.i("Got track.", obj.toString());
							parsed.addElement(new TrackModel(obj));
							break;
						default:
							L.i("No success in detecting category type.", "");
							break;
					}
	    		} catch (JSONException e) {
					L.e("Failed to convert item of index " + Integer.toString(i), "", e);
				}    		
	    	}
			return parsed;
		}
    }
    
    public void setPager(QueryPager queryPager) {
		this.queryPager = queryPager;
	}
    
    public void appendToList() {
    	if(viewModel != null) {
    		
    		// TODO: Create a better cleaning method? This cleans the whole view and then appends the search field again..
    		this.deleteAll();
    		this.append(this.searchField);
    		
    		int loopMax = viewModel.size();
    		Object tmp;
    		for(int i = 0; i < loopMax; i++) {
    			tmp = viewModel.elementAt(i);
    			//this.append(viewModel.elementAt(i).toString());
				tmp = (GenericProductModel) tmp;
				append(new ListItem(
						viewManager,
						(GenericProductModel) tmp
					));
    		}
    	}
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
        	
        	L.i("Command not recognized.", "");
        	
        	/*
        	L.i("Index: ", Integer.toString(getSelectedIndex()));
        	int index = getSelectedIndex();

        	AlbumModel album = (AlbumModel) viewModel.elementAt(index);
        	viewManager.showView(new AlbumView(viewManager, album));
        	*/
        	
        }
        
    }
}
