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

/**
 * A list of items which can be selected to access a sub view. Implements a
 * CommandListener interface for handling list selection events as well as
 * hardware back button presses.
 */
public class MainList
    extends List
    implements CommandListener {

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
    public MainList(ViewManager viewManager) {
        super("Music Explorer", List.IMPLICIT);
        this.viewManager = viewManager;
        
        this.backCommand = new Command("Back", Command.BACK, 1);
        addCommand(backCommand);
        setCommandListener(this);
        
        populateList();
    }
    
    /**
     * An utility function which will add some dummy items to the list
     */
    private void populateList() {

    	append(NewReleasesView.title, null);
    	append(PopularReleasesView.title, null);
    	append(SearchProductsView.title, null);
    	append(GenresListView.title, null);

    }

    /**
     * Implementation of a required commandAction method from CommandListener
     * interface. Handles all command actions, such as list item selections,
     * which happen within this view.
     * @param command The command which was fired
     * @param displayable The view from where the command originated (in this
     *        case it is this view itself)
     * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command,
     *      javax.microedition.lcdui.Displayable)
     */
    public void commandAction(Command command, Displayable displayable) {
        if (command == List.SELECT_COMMAND) {
            final int itemNumber = getSelectedIndex() + 1;
            
            switch (itemNumber) {
	        	case 1:
	        		viewManager.showView(new NewReleasesView(viewManager));
	        		break;
	        	case 2:
	        		viewManager.showView(new PopularReleasesView(viewManager));
	        		break;
	        	case 3:
	        		viewManager.showView(new SearchProductsView(viewManager));
	        		break;
	        	case 4:
	        		viewManager.showView(new GenresListView(viewManager));
	        		break;
                default:
                    break;
            }
        }
        else if (backCommand.equals(command)) {
            // Hardware back button was pressed
            viewManager.goBack();
        }
    }
}
