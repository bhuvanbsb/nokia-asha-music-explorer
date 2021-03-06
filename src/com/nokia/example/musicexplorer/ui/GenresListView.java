/**
 * Copyright (c) 2013 Nokia Corporation. All rights reserved. Nokia and Nokia
 * Connecting People are registered trademarks of Nokia Corporation. Oracle and
 * Java are trademarks or registered trademarks of Oracle and/or its affiliates.
 * Other product and company names mentioned herein may be trademarks or trade
 * names of their respective owners. See LICENSE.TXT for license information.
 */

package com.nokia.example.musicexplorer.ui;

import java.util.Vector;
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
import com.nokia.example.musicexplorer.data.model.GenreModel;

/**
 * Displays a list of genres.
 */
public class GenresListView
        extends List
        implements CommandListener, InitializableView {

    public static final String VIEW_TITLE = "Browse genres";
    public static final String PATH_TO_ICON = "/genres_icon.png";

    private final ViewManager viewManager;
    private final Command backCommand;
    private Vector viewModel = new Vector();
    private boolean initialized;

    /**
     * Constructor, where the list is marked as an implicit list (ie. only
     * single item can be selected at a time), populated, and a back command is
     * added to it.
     *
     * @param viewManager View manager which will handle switching from one view
     * to another
     */
    public GenresListView(ViewManager viewManager) {
        super(VIEW_TITLE, List.IMPLICIT);
        this.viewManager = viewManager;
        
        this.backCommand = new Command("Back", Command.BACK, 1);
        addCommand(backCommand);
        setCommandListener(this);
    }

    /**
     * @see com.nokia.example.musicexplorer.ui.InitializableView#initialize()
     */
    public void initialize() {
        if(!initialized) {
            ApiCache.getGenres(new PlaceResultsTask());
            initialized = true;
        } else {
            L.i("Already initialized", this.toString());
        }
    }

    /**
     * @see javax.microedition.lcdui.CommandListener#commandAction(
     * javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
     */
    public void commandAction(Command command, Displayable displayable) {
        if (command == List.SELECT_COMMAND) {
            int selectedIndex = getSelectedIndex();
            GenreModel genreModel = (GenreModel) viewModel.elementAt(selectedIndex);
            viewManager.showView(new ArtistsListView(viewManager, genreModel));
        } else if (backCommand.equals(command)) {
            // Hardware back button was pressed
            viewManager.goBack();
        }
    }

    public void append(GenreModel genreModel) {
        viewModel.addElement(genreModel);
        append(genreModel.name, null);
    }

    /**
     * Implements a task for retrieving the genre data and populating the genre
     * model.
     */
    public class PlaceResultsTask extends Task {

        public PlaceResultsTask() {
            super(Task.NORMAL_PRIORITY);
        }

        public Object exec(Object response) {
            try {
                JSONArray array = ((JSONObject) response).getJSONArray("items");
                int loopMax = array.length();
                
                for (int i = 0; i < loopMax; i++) {
                    GenreModel genreModel =  new GenreModel((JSONObject) array.get(i));
                    append(genreModel);
                }
            } catch (JSONException e) {
                L.e("Unable to parse JSON to genre models", "", e);
                return null;
            }
            
            return response;
        }
    }
}
