/**
 * Copyright (c) 2013 Nokia Corporation. All rights reserved. Nokia and Nokia
 * Connecting People are registered trademarks of Nokia Corporation. Oracle and
 * Java are trademarks or registered trademarks of Oracle and/or its affiliates.
 * Other product and company names mentioned herein may be trademarks or trade
 * names of their respective owners. See LICENSE.TXT for license information.
 */
package com.nokia.example.musicexplorer.ui;

import javax.microedition.lcdui.CustomItem;
import javax.microedition.lcdui.Graphics;

/**
 * Separator item for lists.
 */
public class LineSeparator
        extends CustomItem {

    public LineSeparator() {
        super(null);
        setPreferredSize(240, 12);
    }
    
    protected int getMinContentWidth() {
        return 0;
    }

    protected int getMinContentHeight() {
        return 0;
    }

    protected int getPrefContentWidth(int height) {
        return 0;
    }

    protected int getPrefContentHeight(int width) {
        return 0;
    }

    protected void paint(Graphics graphics, int w, int h) {    
        graphics.setColor(219, 219, 219);
        graphics.drawLine(0, 0,  239, 0);

        graphics.setColor(255, 255, 255);
        graphics.drawLine(0, 1,  239, 1);
    }
    
}
