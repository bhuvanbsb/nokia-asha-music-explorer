/**
 * Copyright (c) 2013 Nokia Corporation. All rights reserved. Nokia and Nokia
 * Connecting People are registered trademarks of Nokia Corporation. Oracle and
 * Java are trademarks or registered trademarks of Oracle and/or its affiliates.
 * Other product and company names mentioned herein may be trademarks or trade
 * names of their respective owners. See LICENSE.TXT for license information.
 */
package com.nokia.example.musicexplorer;

import com.nokia.example.musicexplorer.data.ApiCache;
import com.nokia.example.musicexplorer.ui.MainList;
import com.nokia.example.musicexplorer.ui.ViewManager;
import java.util.Stack;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import org.tantalum.PlatformUtils;
import org.tantalum.util.L;

/**
 * The main MIDlet.
 */
public final class Main
        extends MIDlet
        implements ViewManager {

    private Stack viewStack;
    
    /**
     * To detect views that have been added using showView() that skips stack.
     */
    private boolean currentViewIsInStack = true;
    
    public void startApp() {
        PlatformUtils.getInstance().setProgram(this, 4);

        // Try to create caches.
        if (ApiCache.init()) {
            viewStack = new Stack();
            Displayable mainView = new MainList(this);
            showView(mainView);
        } else {
            L.i("Could not start the application.", "");
        }
    }

    public void pauseApp() {
    }

    public void showView(Displayable view) {
        viewStack.push(view);
        Display.getDisplay(this).setCurrent(view);
        currentViewIsInStack = true;
    }
    
    public void showView(Displayable view, boolean addToStack) {
        if(addToStack) {
            showView(view);
        } else {
            currentViewIsInStack = false;
            Display.getDisplay(this).setCurrent(view);
        }
    }

    public void goBack() {
        
        // Check if the view is added to the stack.
        if(currentViewIsInStack) {
            // Remove the current view from the view stack
            viewStack.pop();            
        } else {
            L.i("Current view is not in stack", "");
        }

        if (viewStack.empty()) {
            // There are no more views in the stack, so destroy the MIDlet
            L.i("No more views in stack", "");
            notifyDestroyed();
        } else {
            // There is still another view below the popped on, so display it
            Displayable previousView = (Displayable) (viewStack.peek());
            Display.getDisplay(this).setCurrent(previousView);
            L.i("Displaying previous view", previousView.toString());
        }
    }

    protected void destroyApp(boolean unconditional)
            throws MIDletStateChangeException {
        PlatformUtils.getInstance().shutdown(unconditional, "Shutting down.");
    }
}
