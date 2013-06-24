/**
 * Copyright (c) 2013 Nokia Corporation. All rights reserved. Nokia and Nokia
 * Connecting People are registered trademarks of Nokia Corporation. Oracle and
 * Java are trademarks or registered trademarks of Oracle and/or its affiliates.
 * Other product and company names mentioned herein may be trademarks or trade
 * names of their respective owners. See LICENSE.TXT for license information.
 */

package com.nokia.example.musicexplorer;

import java.util.Stack;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import org.tantalum.PlatformUtils;
import org.tantalum.util.L;

import com.nokia.example.musicexplorer.data.ApiCache;
import com.nokia.example.musicexplorer.ui.HomeView;
import com.nokia.example.musicexplorer.ui.ViewManager;
import com.nokia.example.musicexplorer.utils.CategoryBarUtils.CategoryBarHolder;
import com.nokia.example.musicexplorer.ui.InitializableView;

/**
 * The main MIDlet.
 */
public final class Main
        extends MIDlet
        implements ViewManager {

    private Stack viewStack;
    private Displayable subview;
    private Alert networkAlert;

    /**
     * @see javax.microedition.midlet.MIDlet#startApp()
     */
    public void startApp() {
        PlatformUtils.getInstance().setProgram(this, 4);
        
        // Try to create caches.
        if (ApiCache.init(this)) {
            viewStack = new Stack();
            Displayable mainView = new HomeView(this);
            showView(mainView);
        } else {
            L.i("Could not start the application.", "");
        }
    }

    /**
     * @see javax.microedition.midlet.MIDlet#pauseApp()
     */
    public void pauseApp() {
    }

    /**
     * @see javax.microedition.midlet.MIDlet#destroyApp(boolean)
     */
    protected void destroyApp(boolean unconditional)
            throws MIDletStateChangeException {
        PlatformUtils.getInstance().shutdown(unconditional, "Shutting down.");
    }

    /**
     * Shows a view on the display and initialize it.
     * @see com.nokia.example.musicexplorer.ui.ViewManager#showView(
     * com.nokia.example.musicexplorer.ui.InitializableView)
     */
    public void showView(InitializableView view) {
        showView((Displayable) view);
        view.initialize();
    }

    /**
     * For displaying an alert. This does not add to back stack.
     * @see com.nokia.example.musicexplorer.ui.ViewManager#showNetworkAlert()
     */
    public void showNetworkAlert() {
        if (networkAlert == null) {
            networkAlert = new Alert(
                    "No network connection", 
                    "Make sure your network connection is " +
                    "enabled and try again.",
                    null,
                    AlertType.INFO);
            networkAlert.setTimeout(Alert.FOREVER);
            networkAlert.setType(AlertType.INFO);
        }
        
        Display.getDisplay(this).setCurrent(networkAlert);
    }

    /**
     * @see com.nokia.example.musicexplorer.ui.ViewManager#showView(
     * javax.microedition.lcdui.Displayable)
     */
    public void showView(Displayable view) {
        hideCategoryBarIfExists(false);
        subview = null; // Clear subview if it exists.
        viewStack.push(view);
        setCurrentView(view);
    }

    /**
     * @see com.nokia.example.musicexplorer.ui.ViewManager#showSubview(
     * javax.microedition.lcdui.Displayable)
     */
    public void showSubview(Displayable subview) {
        // Do not add to viewstack
        this.subview = subview;
        setCurrentView(subview);
    }

    /**
     * @see com.nokia.example.musicexplorer.ui.ViewManager#goBack()
     */
    public void goBack() {
        if (subview == null) {
            // Remove the current view from the view stack.
            viewStack.pop();
        } else {
            /**
             * As subviews are not added to viewStack, when in a subview the 
             * back button is pressed we have to take a look at the fist item 
             * in the stack. The item is in these cases the Displayable that
             * holds the category bar. Here, the category bar has to be hidden.
             */
            
            /**
             * Hide category bar and pop the category bar holder from stack.
             */
            hideCategoryBarIfExists(true);
            
            // Just discard the subview and continue as normal.
            subview = null;
        }
        
        if (viewStack.empty()) {
            // There are no more views in the stack, so destroy the MIDlet
            notifyDestroyed();
        } else {
            // There is still another view below the popped on, so display it
            Displayable previousView = (Displayable) (viewStack.peek());
            
            // Check if the view implements a category bar and show it.
            if (previousView instanceof CategoryBarHolder) {
                ((CategoryBarHolder) previousView).showCategoryBar();
                ((CategoryBarHolder) previousView).showLastViewedTab();
            } else {
                Display.getDisplay(this).setCurrent(previousView);
            }
        }
    }

    /**
     * @see com.nokia.example.musicexplorer.ui.ViewManager#addToStack(
     * javax.microedition.lcdui.Displayable)
     */
    public void addToStack(Displayable view) {
        viewStack.push(view);
    }

    /**
     * Checks if the item on top of stack implements a category bar and hides
     * the bar if it exists.
     * @param pop Determines whether to pop the view from the stack.
     */
    private void hideCategoryBarIfExists(boolean pop) {
        if (!viewStack.isEmpty()) {
            Displayable topOfStack = (Displayable) viewStack.peek();
            
            if (topOfStack instanceof CategoryBarHolder) { 
                ((CategoryBarHolder) topOfStack).hideCategoryBar();
                
                /**
                 * Pop the view out as it itself doesn't display anything
                 */
                if (pop) {
                    viewStack.pop();
                }
            }
        }
    }

    private void setCurrentView(Displayable view) {
        Display.getDisplay(this).setCurrent(view);
    }
}
