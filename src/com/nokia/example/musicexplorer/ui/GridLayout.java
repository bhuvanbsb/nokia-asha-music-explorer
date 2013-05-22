/**
 * Copyright (c) 2013 Nokia Corporation. All rights reserved. Nokia and Nokia
 * Connecting People are registered trademarks of Nokia Corporation. Oracle and
 * Java are trademarks or registered trademarks of Oracle and/or its affiliates.
 * Other product and company names mentioned herein may be trademarks or trade
 * names of their respective owners. See LICENSE.TXT for license information.
 */
package com.nokia.example.musicexplorer.ui;

import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.lcdui.CustomItem;
import javax.microedition.lcdui.Graphics;
import org.tantalum.util.L;
import com.nokia.example.musicexplorer.data.model.GenericProductModel;
import com.nokia.example.musicexplorer.data.model.AlbumModel;

/**
 * GridLayout is a CustomItem component for displaying a grid of GridItems in a
 * Form.
 */
public class GridLayout
        extends CustomItem {

    public static final int CUSTOM_ITEM_MARGIN_SIZE = 5;
    public static final int DEFAULT_COLUMN_COUNT = 3;
    public static final int JITTER_THRESHOLD = 10;
    protected Vector gridItems;
    protected GridItem selectedItem;
    protected int width = 0;
    protected int columnCount = DEFAULT_COLUMN_COUNT;
    protected int columnWidth = 0;
    protected int rowHeight = 0;
    protected int lastX = 0;
    protected int lastY = 0;
    protected int verticalScrollPosition = 0;
    protected ViewManager viewManager;
    protected boolean showMoreByArtistButton = true;

    public GridLayout(final int width, ViewManager viewManager) {
        super(null);
        this.viewManager = viewManager;
        this.gridItems = new Vector();

        setWidth(width);
    }

    /**
     * Adds an item to the grid.
     *
     * @param productModel
     */
    public void addItem(GenericProductModel productModel) {
        GridItem gridItem = new GridItem(viewManager, productModel, this);

        gridItem.setSize(columnWidth, columnWidth);
        gridItems.addElement(gridItem);

        int amountOfRows = (int) Math.ceil((double) gridItems.size()
                / this.columnCount);
        int newHeight = amountOfRows * this.rowHeight + CUSTOM_ITEM_MARGIN_SIZE * 2;

        this.setPreferredSize(this.width, newHeight);
    }

    /**
     * For convenience. Calculates the size and repaints the layout only once.
     *
     * @param columnCount The new column count.
     * @param width The new width for the layout.
     */
    public void setColumnCountAndWidth(final int columnCount, final int width) {
        if ((this.width != width && columnCount > 0)
                || (columnCount > 0 && this.columnCount != columnCount)) {
            this.columnCount = columnCount;
            this.width = width;
            onWidthOrColumnCountChanged();
        }
    }

    /**
     * @param width The new width for the layout.
     */
    public void setWidth(final int width) {
        if (this.width != width) {
            this.width = width;
            onWidthOrColumnCountChanged();
        }
    }

    /**
     * Sets the number of columns to be displayed in the grid.
     *
     * @param columnCount The number of columns.
     */
    public void setColumnCount(final int columnCount) {
        if (columnCount > 0 && this.columnCount != columnCount) {
            this.columnCount = columnCount;
            onWidthOrColumnCountChanged();
        }
    }

    /**
     * Get the grid item under the pointer and set it as {@link #selectedItem}.
     *
     * @see javax.microedition.lcdui.CustomItem#pointerPressed()
     */
    public void pointerPressed(int x, int y) {
        selectedItem = getItemAt(x, y);

        if (selectedItem != null) {
            // Save the coordinates to be used in pointerReleased().
            lastX = x;
            lastY = y;
        }
    }

    /**
     * Checks if the pointer is still inside the same item where the tap began
     * on.
     *
     * @see javax.microedition.lcdui.CustomItem#pointerReleased()
     */
    public void pointerReleased(int x, int y) {
        if (selectedItem != null && selectedItem == getItemAt(x, y)
                && Math.abs(x - lastX) < JITTER_THRESHOLD
                && Math.abs(y - lastY) < JITTER_THRESHOLD) {
            viewManager.showView(
                    new AlbumView(
                        viewManager, 
                        (AlbumModel) selectedItem.model, 
                        showMoreByArtistButton));
        }
    }

    public void disableShowMoreByArtistButtonInAlbumViews() {
        this.showMoreByArtistButton = false;
    }
    
    /**
     * Clears the {@link #selectedItem} when pointer is dragged outside the grid
     * item.
     *
     * @see javax.microedition.lcdui.CustomItem#pointerDragged()
     */
    public void pointerDragged(int x, int y) {
        if (selectedItem != null
                && (Math.abs(x - lastX) > JITTER_THRESHOLD
                || Math.abs(y - lastY) > JITTER_THRESHOLD)) {
            // Too much jitter. Loose the selected item.
            selectedItem = null;
        }
    }

    /**
     * @see javax.microedition.lcdui.CustomItem#getMinContentHeight()
     */
    protected int getMinContentHeight() {
        return 0;
    }

    /**
     * @see javax.microedition.lcdui.CustomItem#getMinContentWidth()
     */
    protected int getMinContentWidth() {
        return 0;
    }

    /**
     * @see javax.microedition.lcdui.CustomItem#getPrefContentHeight(int)
     */
    protected int getPrefContentHeight(int h) {
        return getPreferredHeight();
    }

    /**
     * @see javax.microedition.lcdui.CustomItem#getPrefContentWidth(int)
     */
    protected int getPrefContentWidth(int w) {
        return getPreferredWidth();
    }

    /**
     * @see javax.microedition.lcdui.CustomItem#paint(Graphics, int, int)
     */
    protected void paint(Graphics graphics, int width, int height) {
        Enumeration e = gridItems.elements();
        int row = 0;
        int col = 0;

        // Loop the grid items
        while (e.hasMoreElements()) {
            GridItem item = (GridItem) e.nextElement();

            // Paint the current item
            item.paintXY(graphics, col * columnWidth, row * rowHeight);
            col++;

            // Advance to the next row if all columns have been painted
            if (col == columnCount) {
                row++;
                col = 0;
            }
        }
    }

    /**
     * Resolves an item at the given position.
     *
     * @param x The X coordinate of the item.
     * @param y The Y coordinate of the item.
     * @return The item instance in the given position or null if not found.
     */
    protected GridItem getItemAt(int x, int y) {

        // Calculate item index based on coordinates.
        final int col = x / columnWidth;
        final int row = y / rowHeight;
        final int index = (columnCount * row) + col;

        GridItem item = null;

        try {
            item = (GridItem) gridItems.elementAt(index);
        } catch (ArrayIndexOutOfBoundsException e) {
            L.e("Given index is not in range.", "", e);
        }

        return item;
    }

    /**
     * (Re)calculates the column width and the preferred size for the layout and
     * repaints the whole thing.
     */
    protected void onWidthOrColumnCountChanged() {
        columnWidth = (width - CUSTOM_ITEM_MARGIN_SIZE * 2) / columnCount;
        rowHeight = columnWidth; // Only square items

        Enumeration e = gridItems.elements();

        while (e.hasMoreElements()) {
            ((GridItem) e.nextElement()).setSize(columnWidth, rowHeight);
        }

        final int rowCount = (int) Math.ceil((double) gridItems.size() / columnCount);
        setPreferredSize(width, rowCount * rowHeight + CUSTOM_ITEM_MARGIN_SIZE * 2);

        repaint();
    }

    /**
     * For toggling grid repaint.
     */
    public void gridItemStateChanged() {
        repaint();
    }
}
