package com.nokia.example.musicexplorer.ui;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;
import javax.microedition.lcdui.StringItem;

public class LoadMoreButton {

	private StringItem loadMoreButton;
	private Command loadMoreCommand;

	public LoadMoreButton(Form formView) {
    	// Initialize a load more button.
    	this.loadMoreButton = new StringItem(null, "Load more...", Item.BUTTON);
    	this.loadMoreCommand = new Command("Load more", Command.ITEM, 1);
    	this.loadMoreButton.setDefaultCommand(loadMoreCommand);
    	this.loadMoreButton.setItemCommandListener((ItemCommandListener) formView);
	}
	
	public StringItem getButton() {
		return loadMoreButton;
	}
	
	public Command getCommand() {
		return loadMoreCommand;
	}
}
