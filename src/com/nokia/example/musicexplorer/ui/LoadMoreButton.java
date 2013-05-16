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
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;
import javax.microedition.lcdui.StringItem;

/**
 * Initializes a Load more button that can be used in a view with paging needs.
 */
public class LoadMoreButton {

	private StringItem loadMoreButton;
	private Command loadMoreCommand;
	private Form formView;
	
	public LoadMoreButton(Form formView) {
    	// Initialize a load more button.
		this.formView = formView;
    	this.loadMoreCommand = new Command("Load more", Command.ITEM, 1);

		initializeButton();
	}
	
	public StringItem getButton() {
		if(loadMoreButton == null) {
			initializeButton();
		}
		return loadMoreButton;
	}
	
	public Command getCommand() {
		return loadMoreCommand;
	}
	
	private void initializeButton() {
    	this.loadMoreButton = new StringItem(null, "Load more...", Item.BUTTON);
    	this.loadMoreButton.setDefaultCommand(loadMoreCommand);
    	this.loadMoreButton.setItemCommandListener((ItemCommandListener) formView);	}
}
