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
import javax.microedition.lcdui.List;
import java.util.Vector;

import com.nokia.example.musicexplorer.data.ApiCache;
import com.nokia.example.musicexplorer.data.model.AlbumModel;
import com.nokia.example.musicexplorer.data.model.TrackModel;

import org.tantalum.Task;
import org.tantalum.util.L;

/**
 * An example Form-based view, which just shows a single text line in the form
 * content area. Implements CommandLister interface to be able to detect back
 * button presses, so it can ask ViewManager to show the previous view.
 */
public class AlbumView
    extends List
    implements CommandListener {

    // Class members

    private final ViewManager viewManager;
    private final Command backCommand;
    private AlbumModel albumModel;
    
    /**
     * Constructor which sets the view title, adds a back command to it and adds
     * the dummy text content to it.
     * @param viewManager Title shown in the title bar of this view
     * @param album View manager which will handle view switching
     */
    public AlbumView(ViewManager viewManager, AlbumModel album) {
        super(album.name, Choice.IMPLICIT);
    	
        this.viewManager = viewManager;
        this.albumModel = album;

    	album.getTracks(this); // Updates albumview when done.
        
        this.backCommand = new Command("Back", Command.BACK, 1);
        addCommand(backCommand);
        setCommandListener(this);
    }

    public void appendTracks(AlbumModel album) {    	
    	int loopMax = album.tracks.size();
    	
    	for(int i = 0; i < loopMax; i++) {
        	append(((TrackModel) album.tracks.elementAt(i)).name, null);
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
