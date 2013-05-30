/**
 * Copyright (c) 2013 Nokia Corporation. All rights reserved. Nokia and Nokia
 * Connecting People are registered trademarks of Nokia Corporation. Oracle and
 * Java are trademarks or registered trademarks of Oracle and/or its affiliates.
 * Other product and company names mentioned herein may be trademarks or trade
 * names of their respective owners. See LICENSE.TXT for license information.
 */
package com.nokia.example.musicexplorer.ui;

import com.nokia.mid.ui.DirectGraphics;
import com.nokia.mid.ui.DirectUtils;
import java.io.IOException;
import javax.microedition.lcdui.CustomItem;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import org.tantalum.util.L;

/**
 * A menu item for the home screen.
 */
public class MenuItem 
        extends CustomItem {

    private Image icon;
    private String title;
    private HomeView homeView;
    private boolean pointerActive;
    private int lastX;
    private int lastY;
    private static final int POINTER_JITTER = 15;
    private static final int HEIGHT = 60; // Includes custom item's paddings
    private static final int WIDTH = 240; // Includes custom item's paddings
    
    public MenuItem(
            String pathToImage, 
            String title,
            HomeView view) {
        
        super(null);

        this.title = title;
        this.homeView = view;
        
        try {
            this.icon = Image.createImage(pathToImage);
        } catch (IOException e) {
            L.e("Resource was not found", pathToImage, e);
        }

        setPreferredSize(WIDTH, HEIGHT);
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

    protected void paintHighlight(
            Graphics graphics, int x, int y, int width, int height) {
        
        int originalColor = graphics.getColor(); // Save color
        
        DirectGraphics directGraphics = 
                DirectUtils.getDirectGraphics(graphics);

        int highlightColorARGB = 0x88FFFFFF;
        directGraphics.setARGBColor(highlightColorARGB);

        graphics.fillRect(
                x, 
                y, 
                width, 
                height);        
        
        graphics.setColor(originalColor); // Restore original color
    }    
    
    protected void paint(Graphics graphics, int w, int h) {
        graphics.drawImage(icon, 5, 5, Graphics.TOP | Graphics.LEFT);

        if (pointerActive) {
            paintHighlight(graphics, 0, 0, WIDTH, HEIGHT);
        }
        
        graphics.setColor(70,70,70);
        graphics.setFont(
                Font.getFont(
                Font.FACE_SYSTEM, 
                Font.STYLE_PLAIN, 
                Font.SIZE_MEDIUM));

        graphics.drawString(
                title, 
                60, 
                13, 
                Graphics.TOP | Graphics.LEFT);      
    }
    
    public void pointerPressed(int x, int y) {
        this.lastX = x;
        this.lastY = y;
        this.pointerActive = true;
    }

    public void pointerDragged(int x, int y) {
        if (!(Math.abs(x - lastX) < POINTER_JITTER
            && Math.abs(y - lastY) < POINTER_JITTER)) {
            pointerActive = false;
            repaint();
        }
    }

    public void pointerReleased(int x, int y) {
        if (pointerActive) {
            homeView.triggerAction(this);
        }
        pointerActive = false;
        repaint(); // Called to de-highlight
    }
}
