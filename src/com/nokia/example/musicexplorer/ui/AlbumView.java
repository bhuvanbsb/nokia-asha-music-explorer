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

import com.nokia.example.musicexplorer.data.model.AlbumModel;
import com.nokia.example.musicexplorer.data.model.TrackModel;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;
import javax.microedition.lcdui.StringItem;
import org.tantalum.util.L;

/**
 * Displays album's details.
 */
public class AlbumView
        extends Form
        implements CommandListener, ItemCommandListener {

    private final ViewManager viewManager;
    private final Command backCommand;
    private AlbumModel albumModel;
    private ListItem headerItem;
    private StringItem moreByArtist;
    private Command moreByArtistCommand;
    private boolean showMoreByArtistButton;

    /**
     * Constructor which sets the view title, adds a back command to it and adds
     * the dummy text content to it.
     *
     * @param viewManager Title shown in the title bar of this view
     * @param album View manager which will handle view switching
     */
    public AlbumView(
            ViewManager viewManager, 
            AlbumModel album,
            boolean showMoreByArtistButton) {
        super(null); // No title
        
        this.viewManager = viewManager;


        
        this.albumModel = album;
        this.albumModel.getTracks(this); // Updates albumview when done.

        this.backCommand = new Command("Back", Command.BACK, 1);

        this.headerItem = new ListItem(viewManager, album);
        this.headerItem.disablePointer();
        
        this.showMoreByArtistButton = showMoreByArtistButton && !this.albumModel.byVariousArtists;

        // Set to false if launched from Artist view or artist is "Various artists".
        if (this.showMoreByArtistButton) {
            this.moreByArtist = new StringItem(null, "More by artist...", Item.BUTTON);
            this.moreByArtistCommand = new Command("More by artist", Command.ITEM, 1);
            this.moreByArtist.setDefaultCommand(this.moreByArtistCommand);
            this.moreByArtist.setItemCommandListener(this);
        }
        
        append(headerItem);
        addCommand(backCommand);
        setCommandListener(this);
    }

    public void setAlbumOrTrackAmountText(String text) {
        if (this.headerItem != null) {
            this.headerItem.setAlbumOrTrackAmountText(text);
        }
    }
    
    /**
     * Appends track names to view.
     *
     * @param album
     */
    public void appendTracks(AlbumModel album) {    	
    	int loopMax = album.tracks.size();
    	
    	for (int i = 0; i < loopMax; i++) {
    		TrackModel track = (TrackModel) album.tracks.elementAt(i);
        	append(Integer.toString(i+1) + ". " + track.name + " " + track.getFormattedDuration());
        }
        
        if (showMoreByArtistButton) {
            append(this.moreByArtist);
        }
    }

    /**
     * Implementation of a required commandAction method from CommandListener
     * interface. Handles eg. hardware back button press event.
     *
     * @param command The command which was fired
     * @param displayable The view from where the command originated (in this
     * case it is this view itself)
     * @see
     * javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command,
     * javax.microedition.lcdui.Displayable)
     */
    public void commandAction(Command command, Displayable displayable) {
        if (backCommand.equals(command)) {
            // Hardware back button was pressed
            viewManager.goBack();
        }
        

    }

    public void commandAction(Command c, Item item) {
        if (moreByArtistCommand.equals(c)) {
            L.i("Performer details", Integer.toString(albumModel.getPerformerId()));
            
            /**
             * Initialize an ArtistView by performer ID. The view is basically
             * just a blank Form that displays a SimilarArtistsView and 
             * a ArtistInfoView based on the current category bar selection.
             */
            ArtistView artistView = new ArtistView(viewManager, albumModel.getPerformerId());
            
            /**
             * Add the view to stack but do not display it. An ArtistView takes 
             * care of displaying the category bar items / sub-views.
             */
            viewManager.addToStack(artistView);
        }
    }
}
