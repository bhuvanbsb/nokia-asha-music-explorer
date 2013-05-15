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
import javax.microedition.lcdui.List;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.tantalum.Task;
import org.tantalum.util.L;

import com.nokia.example.musicexplorer.data.ApiCache;


/**
 * Displays a list of genres.
 */
public class GenresListView
    extends List
    implements CommandListener {

	public static final String title = "Browse music";
	
    // Class members
    private final ViewManager viewManager;
    private final Command backCommand;

    /**
     * Constructor, where the list is marked as an implicit list (ie. only
     * single item can be selected at a time), populated, and a back command is
     * added to it.
     * @param viewManager View manager which will handle switching from one view
     *        to another
     */
    public GenresListView(ViewManager viewManager) {
        super(title, List.IMPLICIT);
        this.viewManager = viewManager;

        this.backCommand = new Command("Back", Command.BACK, 1);
        addCommand(backCommand);
        setCommandListener(this);
        
        ApiCache.getGenres(new PlaceResultsTask(this));
    }
    
    public class PlaceResultsTask extends Task {
		private List parentView;
    	public PlaceResultsTask(List view) {
			super(Task.NORMAL_PRIORITY);
			this.parentView = view;
		}
		public Object exec(Object response) {  		    		
			try {
				JSONObject obj = (JSONObject) response;
				JSONArray arr = obj.getJSONArray("items");
				int loopMax = arr.length();
				
				for(int i = 0; i < loopMax; i++) {
					this.parentView.append(((JSONObject) arr.get(i)).getString("name"), null);
				}
			}
			catch(JSONException exception) {    				
				L.e("Unable to parse Track in PlaceResultsTask", "", exception);
				return null;
			}
			return response;
    	}
    }

    public void commandAction(Command command, Displayable displayable) {
        if (command == List.SELECT_COMMAND) {
            // One of the list items was selected
            L.i("Item action not implemented.", "");
        }
        else if (backCommand.equals(command)) {
            // Hardware back button was pressed
            viewManager.goBack();
        }
    }
}
