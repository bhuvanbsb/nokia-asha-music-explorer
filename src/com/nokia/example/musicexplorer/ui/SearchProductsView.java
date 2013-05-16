/**
* Copyright (c) 2013 Nokia Corporation. All rights reserved.
* Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
* Oracle and Java are trademarks or registered trademarks of Oracle and/or its
* affiliates. Other product and company names mentioned herein may be trademarks
* or trade names of their respective owners. 
* See LICENSE.TXT for license information.
*/

package com.nokia.example.musicexplorer.ui;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.Date;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;
import javax.microedition.lcdui.TextField;
import javax.microedition.lcdui.ItemStateListener;

import org.json.me.JSONObject;
import org.json.me.JSONArray;
import org.json.me.JSONException;

import org.tantalum.Task;
import org.tantalum.util.L;

import com.nokia.example.musicexplorer.data.QueryPager;
import com.nokia.example.musicexplorer.data.ApiCache;
import com.nokia.example.musicexplorer.data.model.GenericProductModel;
import com.nokia.example.musicexplorer.data.model.AlbumModel;
import com.nokia.example.musicexplorer.data.model.ArtistModel;
import com.nokia.example.musicexplorer.data.model.CategoryModel;
import com.nokia.example.musicexplorer.data.model.TrackModel;

/**
 * Search view. The search textfield waits an amount of time before making
 * an API call.
 */
public class SearchProductsView
    extends Form
    implements 
    	CommandListener, 
    	ItemStateListener,
    	ItemCommandListener {

	public static final String title = "Search music";
	
    private final ViewManager viewManager;
    private final Command backCommand;
    private static final int MAX_QUERY_LENGTH_CHARS = 100;
    private static final long QUERY_THROTTLE_MILLISECONDS = 500;
    private static final int MIN_SEARCH_QUERY_LENGTH = 2;

	private static final int ITEMS_PER_PAGE = 20;
    
    private Vector viewModel;
    private QueryPager queryPager;
    private TextField searchField;
    private String searchQuery;
	private Timer throttle;
	private LoadMoreButton loadMoreButton;
	private int loadMoreButtonIndex = -1;
    

	
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
        this.searchField = new TextField(null, null, MAX_QUERY_LENGTH_CHARS, TextField.ANY);

        // Initialize the query pager.
    	this.queryPager = new QueryPager();
    	this.queryPager.setItemsPerPage(ITEMS_PER_PAGE);
        this.loadMoreButton = new LoadMoreButton(this);
        
        append(this.searchField);
        
        addCommand(backCommand);
        setCommandListener(this);
        setItemStateListener(this);
    }
        
    /**
     * Whenever the search query is changed, the state change of the textfield
     * is registered here.
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
				
		if(searchQuery.length() >= MIN_SEARCH_QUERY_LENGTH) {
			throttleSearch();
		}
	}

    /**
     * Delays the search and cancels a possible pending search.
     */
    private void throttleSearch() {
        if (throttle != null) {
            throttle.cancel();
        }

        throttle = new Timer();
        throttle.schedule(new TimerTask() {
            public void run() {
                performSearch(true, false); // Clear previous results. No paging.
                cancel();
            }
        }, QUERY_THROTTLE_MILLISECONDS);
    }    
    
    /**
     * Performs a search API call. The method can as well handle clearing
     * any previous search results (in the case of new query) and query
     * the next page of a paged query.
     * 
     * @param clearResults
     * @param nextPage
     */
	private void performSearch(boolean clearResults, boolean nextPage) {
		String paging;
		
		if(nextPage) {
			paging = queryPager.getQueryStringForNextPage();
		} else {
			paging = queryPager.getCurrentQueryString();
		}
		
		ApiCache.search(
				this.searchQuery, 
				new SearchResultHandlerTask(clearResults), 
				paging);
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
    
    private void parseResultsToViewModel(JSONArray results) {
    	if(viewModel != null) {
    		viewModel.removeAllElements();
    	} else {
    		viewModel = new Vector();
    	}

		/**
		 * Response parsing
		 * 
		 * Get "items" JSONArray and iterate over the JSONObjects.
		 * 1. Check objects "category" JSONObject
		 * 2. Decide which model to instantiate based on the category
		 * 3. Save models (search results) to viewModel vector. 
		 */
    	int loopMax = results.length();
		    	
    	for(int i = 0; i < loopMax; i++) {
        	String category = "";
        	JSONObject obj;
        	GenericProductModel model = null;
        	
    		try {
    			
				obj = (JSONObject) results.get(i);
				category = obj.getJSONObject("category").getString("id");
				
				switch(getCategoryEnum(category.toLowerCase())) {
					case CategoryModel.SINGLE:
						L.i("Got single.", obj.toString());
						model = new AlbumModel(obj);
						break;
					case CategoryModel.ALBUM:
						L.i("Got album.", obj.toString());
						model = new AlbumModel(obj);
						break;
					case CategoryModel.ARTIST:
						L.i("Got artist.", obj.toString());
						model = new ArtistModel(obj);
						break;
					case CategoryModel.TRACK:
						L.i("Got track.", obj.toString());
						model = new TrackModel(obj);
						break;
					default:
						L.i("Category type not detected.", "");
						break;
				}

				if(model != null) {
					viewModel.addElement(new ListItem(viewManager, model));
				}
				
    		} catch (JSONException e) {
				L.e("Failed to convert item of index " + Integer.toString(i), "", e);
			}    		
    	}
	}	
	
    private class SearchResultHandlerTask extends Task {
    	private boolean clearResults;
    	
    	public SearchResultHandlerTask(boolean clearResults) {
    		super(Task.NORMAL_PRIORITY);
    		this.clearResults = clearResults;
    	}
    	
		public Object exec(Object response) {
			if(response instanceof JSONObject) {
				JSONArray resultsArray;
				JSONObject paging;
				
				try {
					resultsArray = ((JSONObject) response).getJSONArray("items");
					paging = ((JSONObject) response).getJSONObject("paging");
					
					if(clearResults) {
						clearSearchResults();
					}
					
					// Set query pager and pass results to parser.
					queryPager.setPaging(paging);
					parseResultsToViewModel(resultsArray);
					appendToView();
				}
				catch(JSONException ex) {
					L.e("Could not parse JSON", "", ex);
				}
			}
			return null;
		}
    }
        
    /**
     * Clears the view.
     */
    private void clearSearchResults() {
		this.deleteAll();
		this.append(this.searchField);    	
    }
    
    private void appendToView() {
    	if(viewModel != null) {
    		int loopMax = viewModel.size();
    		for(int i = 0; i < loopMax; i++) {
    			append((ListItem) viewModel.elementAt(i));
    		}
    		
    		// Avoid leaving the button in between the results.
    		if(this.loadMoreButtonIndex >= 0) {
    			delete(loadMoreButtonIndex);
    		}
    		
    		// Append load more button if there is paging available.
    		if(queryPager.hasMorePages()) {
    			loadMoreButtonIndex = append(loadMoreButton.getButton());
    		}
    	}
	}
    
    public void commandAction(Command command, Displayable displayable) {
        if (backCommand.equals(command)) {
            // Hardware back button was pressed
            viewManager.goBack();
        }
    }

	public void commandAction(Command c, Item item) {
		if(c == loadMoreButton.getCommand()) {
			// Load more triggered
			performSearch(false, true); // Don't clear results. Load a new page.
		}
	}
}
