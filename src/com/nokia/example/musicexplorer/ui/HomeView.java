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
import javax.microedition.lcdui.List;

/**
 * A list of items which can be selected to access a sub view. Implements a
 * CommandListener interface for handling list selection events as well as
 * hardware back button presses.
 */
public class HomeView
        extends Form
        implements CommandListener {

    // Class members
    private final ViewManager viewManager;
    private final Command backCommand;
    
    private MenuItem newReleasesItem;
    private MenuItem popularReleasesItem;
    private MenuItem searchItem;
    private MenuItem genresItem;
    
    /**
     * Constructor, where the list is marked as an implicit list (ie. only
     * single item can be selected at a time), populated, and a back command is
     * added to it.
     *
     * @param viewManager View manager which will handle switching from one view
     * to another
     */
    public HomeView(ViewManager viewManager) {
        super("Music Explorer");
        this.viewManager = viewManager;

        this.backCommand = new Command("Back", Command.BACK, 1);
        addCommand(backCommand);
        setCommandListener(this);

        populateList();
    }
    
    /**
     * Triggered by MenuItems.
     * @param item 
     */
    public void launchAction(MenuItem item) {
        if (item == newReleasesItem) {
            viewManager.showView(new NewReleasesView(viewManager));
        } else if (item == popularReleasesItem) {
            viewManager.showView(new PopularReleasesView(viewManager));
        } else if (item == searchItem) {
            viewManager.showView(new SearchView(viewManager));
        } else if (item == genresItem) {
            viewManager.showView(new GenresListView(viewManager));
        } 
    }

    /**
     * An utility function which will add some dummy items to the list
     */
    private void populateList() {
        newReleasesItem = new MenuItem(
                NewReleasesView.PATH_TO_ICON, 
                NewReleasesView.VIEW_TITLE,
                this);
        
        popularReleasesItem = new MenuItem(
                PopularReleasesView.PATH_TO_ICON,
                PopularReleasesView.VIEW_TITLE,
                this);
        
        searchItem = new MenuItem(
                SearchView.PATH_TO_ICON, 
                SearchView.VIEW_TITLE,
                this);
        
        genresItem = new MenuItem(
                GenresListView.PATH_TO_ICON, 
                GenresListView.VIEW_TITLE,
                this);

        append(newReleasesItem);
        append(popularReleasesItem);
        append(searchItem);
        append(genresItem);
    }

    /**
     * Handle hardware back button.
     * @param command
     * @param displayable 
     */
    public void commandAction(Command command, Displayable displayable) {
        if (backCommand.equals(command)) {
            // Hardware back button was pressed
            viewManager.goBack();
        }
    }
}
