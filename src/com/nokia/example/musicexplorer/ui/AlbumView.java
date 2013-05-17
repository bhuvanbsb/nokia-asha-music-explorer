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
import javax.microedition.lcdui.List;

import com.nokia.example.musicexplorer.data.model.AlbumModel;
import com.nokia.example.musicexplorer.data.model.TrackModel;

/**
 * Displays album's details.
 */
public class AlbumView
    extends Form
    implements CommandListener {

    private final ViewManager viewManager;
    private final Command backCommand;

    private AlbumModel albumModel;
	private ListItem headerItem;
    
    /**
     * Constructor which sets the view title, adds a back command to it and adds
     * the dummy text content to it.
     * @param viewManager Title shown in the title bar of this view
     * @param album View manager which will handle view switching
     */
    public AlbumView(ViewManager viewManager, AlbumModel album) {
        super(null); // No title
        
        this.viewManager = viewManager;
        this.albumModel = album;
        this.albumModel.getTracks(this); // Updates albumview when done.
        this.backCommand = new Command("Back", Command.BACK, 1);

        this.headerItem = new ListItem(viewManager, album);
		this.headerItem.disablePointer();
		append(headerItem);
        
        addCommand(backCommand);
        setCommandListener(this);
    }

    /**
     * Appends track names to view.
     * @param album
     */
    public void appendTracks(AlbumModel album) {    	
    	int loopMax = album.tracks.size();
    	
    	for(int i = 0; i < loopMax; i++) {
    		TrackModel track = (TrackModel) album.tracks.elementAt(i);
        	append(Integer.toString(i+1) + ". " + track.name + " " + track.getFormattedDuration());
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
        }        
    }
}
