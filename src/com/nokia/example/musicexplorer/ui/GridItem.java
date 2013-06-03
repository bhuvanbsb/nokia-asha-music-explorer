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
import javax.microedition.lcdui.Image;

import org.tantalum.Task;
import org.tantalum.jme.JMEImageUtils;

import com.nokia.example.musicexplorer.data.ApiCache;
import com.nokia.example.musicexplorer.data.model.GenericProductModel;
import com.nokia.example.musicexplorer.settings.Placeholders;
import com.nokia.example.musicexplorer.settings.ThumbnailSizes;
import com.nokia.mid.ui.DirectGraphics;
import com.nokia.mid.ui.DirectUtils;
import org.tantalum.util.L;

/**
 * Displays an image using a CustomItem.
 */
public class GridItem 
        extends CustomItem {

    public GridItem gridItem;
    protected String imageUrl;
    protected String text;
    protected int width = 0;
    protected int height = 0;
    protected boolean highlight = false;
    protected Image thumbnail;
    protected ViewManager viewManager;
    protected GenericProductModel model;
    protected GridLayout parentGrid;
    private Task getImageTask;

    public GridItem() {
        super(null);
    }

    /**
     * Constructor.
     *
     * @param imageUri A URI of the grid item image.
     * @param text A descriptive text of the item.
     */
    public GridItem(String imageUri, String text,
            final int width, final int height) {
        this();

        this.imageUrl = imageUri;
        this.text = text;
        this.width = width;
        this.height = height;
    }

    public GridItem(ViewManager viewManager, GenericProductModel model) {
        this();

        this.viewManager = viewManager;
        this.model = model;
    }

    public GridItem(
            ViewManager viewManager,
            GenericProductModel model,
            GridLayout parentGrid) {
        this();

        this.viewManager = viewManager;
        this.model = model;
        this.parentGrid = parentGrid;
    }

    public GridItem(GridItem GridItem, final int width, final int height) {
        this();

        this.gridItem = GridItem;

        if (this.gridItem != null) {
            imageUrl = getImageUrl();
            text = getLabel();
        }

        this.width = width;
        this.height = height;
    }

    public GridItem getGridItem() {
        return gridItem;
    }

    public void sizeChanged(int w, int h) {
    }
    
    /**
     * If the thumbnail URL is not set, get it from the model's hashtable.
     *
     * @return
     */
    public String getImageUrl() {
        if (imageUrl == null) {
            imageUrl = this.model.getThumbnailUrl(ThumbnailSizes.SIZE_100X100);
        }

        return imageUrl;
    }

    public String getString() {
        return text;
    }

    public void setString(String text) {
        this.text = text;
    }

    public boolean getHighlight() {
        return highlight;
    }

    public void setHighlight(boolean highlight) {
        L.i("Set highlight", highlight ? "true" : "false");
        this.highlight = highlight;
    }

    public void setSize(final int width, final int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * @see javax.microedition.lcdui.CustomItem#paint(Graphics, int, int)
     */
    public void paint(Graphics graphics, int width, int height) {
        paint(graphics, 0, 0, width, height);
    }

    /**
     * For convenience. Paints this item in the given position.
     *
     * @param graphics The Graphics instance.
     * @param x The X coordinate of the item.
     * @param y The Y coordinate of the item.
     */
    public void paintXY(Graphics graphics, int x, int y) {
        paint(graphics, x, y, width, height);
    }

    public class PlaceImageTask extends Task {

        public PlaceImageTask() {
            super(Task.NORMAL_PRIORITY);
        }

        public Object exec(Object image) {
            if (image instanceof Image) {
                // Scale image to fit to the grid. Scales down only.
                image = JMEImageUtils.scaleImage(
                        (Image) image,
                        width,
                        width,
                        true,
                        JMEImageUtils.WEIGHTED_AVERAGE_OPAQUE);

                // Place the image to the GridItem.
                thumbnail = (Image) image;
                
                // Notify the grid layout to paint the thumbnail.
                parentGrid.gridItemStateChanged();
            }
            return image;
        }
    }

    protected void getImage() {
        if(this.getImageTask == null) {
            this.getImageTask = ApiCache.getImage(
                    this.getImageUrl(), 
                    new PlaceImageTask());
        }
    }

    /**
     * For convenience.
     */
    protected void paint(
            Graphics graphics,
            int x,
            int y,
            int width,
            int height) {

        
        // Check if thumbnail is already fetched. If not, get it from the web.
        if (thumbnail != null) {
            graphics.drawImage(thumbnail, x, y, Graphics.TOP | Graphics.LEFT);
        } else {
            // Paint to the center of the item.
            Placeholders.paint(graphics, model, x, y, width, height);
            getImage();
        }
        
        if(highlight) {
            paintHighlight(graphics, x, y, width, height);
        }
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
    
    protected int getMinContentHeight() {
        return height;
    }

    protected int getMinContentWidth() {
        return width;
    }

    protected int getPrefContentHeight(int arg0) {
        return height;
    }

    protected int getPrefContentWidth(int arg0) {
        return width;
    }

    public String toString() {
        return "GridItem: " + this.text;
    }
}