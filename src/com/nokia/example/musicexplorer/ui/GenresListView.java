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
import javax.microedition.lcdui.List;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.tantalum.Task;
import org.tantalum.util.L;

import com.nokia.example.musicexplorer.data.ApiCache;
import com.nokia.example.musicexplorer.data.model.GenreModel;
import java.util.Vector;

/**
 * Displays a list of genres.
 */
public class GenresListView
        extends List
        implements CommandListener {

    public static final String VIEW_TITLE = "Browse genres";
    public static final String PATH_TO_ICON = "/genres_icon.png";

    private final ViewManager viewManager;
    private final Command backCommand;
    private Vector viewModel = new Vector();
    
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

        ApiCache.getGenres(new PlaceResultsTask());
    }

    public void append(GenreModel genreModel) {
        viewModel.addElement(genreModel);
        append(genreModel.name, null);
    }
    
    public class PlaceResultsTask extends Task {

        public PlaceResultsTask() {
            super(Task.NORMAL_PRIORITY);
        }

        public Object exec(Object response) {
            try {
                JSONArray array = ((JSONObject) response).getJSONArray("items");
                int loopMax = array.length();
                for (int i = 0; i < loopMax; i++) {
                    GenreModel genreModel = 
                            new GenreModel((JSONObject) array.get(i));
                    append(genreModel);
                }
            } catch (JSONException e) {
                L.e("Unable to parse JSON to genre models", "", e);
                return null;
            }
            return response;
        }
    }

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
}
