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

/**
 * A list of items which can be selected to access a sub view.
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
    
    public HomeView(ViewManager viewManager) {
        super("Music Explorer");
        this.viewManager = viewManager;

        this.backCommand = new Command("Back", Command.BACK, 1);
        addCommand(backCommand);
        setCommandListener(this);

        populateList();
    }
   
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
        append(new LineSeparator());
        append(popularReleasesItem);
        append(new LineSeparator());
        append(searchItem);
        append(new LineSeparator());
        append(genresItem);
    }
 
    /**
     * Triggered by MenuItems.
     * @param item 
     */
    public void triggerAction(MenuItem item) {
        if (item == newReleasesItem) {
            viewManager.showView((InitializableView) new NewReleasesView(viewManager));
        } else if (item == popularReleasesItem) {
            viewManager.showView((InitializableView) new PopularReleasesView(viewManager));
        } else if (item == searchItem) {
            viewManager.showView(new SearchView(viewManager));
        } else if (item == genresItem) {
            viewManager.showView(new GenresListView(viewManager));
        } 
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
