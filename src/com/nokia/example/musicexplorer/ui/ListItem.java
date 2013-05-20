/**
 * Copyright (c) 2013 Nokia Corporation. All rights reserved. Nokia and Nokia
 * Connecting People are registered trademarks of Nokia Corporation. Oracle and
 * Java are trademarks or registered trademarks of Oracle and/or its affiliates.
 * Other product and company names mentioned herein may be trademarks or trade
 * names of their respective owners. See LICENSE.TXT for license information.
 */
package com.nokia.example.musicexplorer.ui;

import javax.microedition.lcdui.CustomItem;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import org.tantalum.Task;
import org.tantalum.util.L;

import com.nokia.example.musicexplorer.data.ApiCache;
import com.nokia.example.musicexplorer.data.model.ArtistModel;
import com.nokia.example.musicexplorer.data.model.GenericProductModel;
import com.nokia.example.musicexplorer.settings.ThumbnailSizes;

/**
 * Displays ArtistModel info: thumbnail, artist name, genre, etc...
 */
public class ListItem
        extends CustomItem {

    public String thumbnailSize;
    protected ViewManager viewManager;
    private static final int POINTER_JITTER = 10;
    private static final String THUMBNAIL_SIZE = ThumbnailSizes.SIZE_50X50;
    private static final int LIST_ITEM_SIDE = 50;
    private static final int LIST_ITEM_WIDTH = 230;
    private static final int LIST_ITEM_HEIGHT = LIST_ITEM_SIDE;
    private static final int TEXT_OFFSET_X = LIST_ITEM_SIDE + 5;
    private static final int TEXT_OFFSET_Y = 20;
    private String text;
    private int width;
    private int height;
    private Image thumbnail;
    private int lastX;
    private int lastY;
    private boolean pointerActive;
    private boolean pointerEnabled = true;
    private GenericProductModel model;

    public ListItem(ViewManager viewManager,
            GenericProductModel model) {
        this();
        this.viewManager = viewManager;
        this.model = model;
        this.height = ListItem.LIST_ITEM_HEIGHT;
        this.width = ListItem.LIST_ITEM_WIDTH;
    }

    public void sizeChanged(int w, int h) {
    }

    public void pointerPressed(int x, int y) {
        if (pointerEnabled) {
            L.i("Pointer pressed, (" + x + "," + y + ")", this.text);
            this.lastX = x;
            this.lastY = y;
            this.pointerActive = true;
        }
    }

    /**
     * Detect if pointer gets dragged outside of the item's bounds.
     */
    public void pointerDragged(int x, int y) {
        if (pointerEnabled
                && !(Math.abs(x - lastX) < ListItem.POINTER_JITTER
                && Math.abs(y - lastY) < ListItem.POINTER_JITTER)) {

            this.pointerActive = false;
        }
    }

    /**
     * If pointer is still inside bounds then select it.
     */
    public void pointerReleased(int x, int y) {
        if (pointerEnabled && pointerActive) {
            pointerReleaseAction();
        }
    }

    /**
     * Private default constructor.
     */
    private ListItem() {
        super(null);
    }

    /**
     * Currently opens only an artist view.
     */
    private void pointerReleaseAction() {
        if (this.model instanceof ArtistModel) {
            viewManager.showView(new ArtistView(viewManager, (ArtistModel) this.model));
        }
    }

    /**
     * @see javax.microedition.lcdui.CustomItem#paint(Graphics, int, int)
     */
    public void paint(Graphics graphics, int width, int height) {
        paint(graphics, 0, 0, width, height);
    }

    public void setThumbnail(Image thumbnail) {
        this.thumbnail = thumbnail;
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

        private ListItem item;

        public PlaceImageTask(ListItem item) {
            super(Task.NORMAL_PRIORITY);
            this.item = item;
        }

        public Object exec(Object thumbnail) {
            if (thumbnail instanceof Image) {
                this.item.setThumbnail((Image) thumbnail);
                this.item.repaint();
            }
            return thumbnail;
        }
    }

    private void getThumbnail() {
        if (this.thumbnail == null) {
            try {
                ApiCache.getImage(this.model.getThumbnailUrl(ListItem.THUMBNAIL_SIZE), new PlaceImageTask(this));
            } catch (Exception e) {
                L.e("No thumbnail available or not loaded yet for item", this.toString(), e);
            }
        }
    }

    /**
     * For convenience.
     */
    protected void paint(Graphics graphics, int x, int y, int width, int height) {
        // Paint the name.
        graphics.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM));
        graphics.drawString(this.model.name, x + ListItem.TEXT_OFFSET_X, y, Graphics.TOP | Graphics.LEFT);

        // Paint the genre(s).
        graphics.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL));
        graphics.drawString(this.model.getGenres(), x + ListItem.TEXT_OFFSET_X, y + ListItem.TEXT_OFFSET_Y, Graphics.TOP | Graphics.LEFT);

        if (thumbnail != null) {
            graphics.drawImage(thumbnail, x, y, Graphics.TOP | Graphics.LEFT);
        } else {
            graphics.drawRoundRect(x, y, ListItem.LIST_ITEM_SIDE, ListItem.LIST_ITEM_SIDE, 3, 3);
            getThumbnail();
        }
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
        return "ListItem: " + this.text;
    }

    public void disablePointer() {
        this.pointerEnabled = false;
    }
}