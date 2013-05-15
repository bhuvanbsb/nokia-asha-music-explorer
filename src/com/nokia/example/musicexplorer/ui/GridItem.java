/**
* Copyright (c) 2013 Nokia Corporation. All rights reserved.
* Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
* Oracle and Java are trademarks or registered trademarks of Oracle and/or its
* affiliates. Other product and company names mentioned herein may be trademarks
* or trade names of their respective owners. 
* See LICENSE.TXT for license information.
*/

package com.nokia.example.musicexplorer.ui;

import java.io.IOException;
import javax.microedition.lcdui.CustomItem;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import org.json.me.JSONObject;

import org.tantalum.Task;
import org.tantalum.util.L;
import org.tantalum.jme.JMEImageUtils;

import com.nokia.example.musicexplorer.data.ApiCache;
import com.nokia.example.musicexplorer.data.model.AlbumModel;
import com.nokia.example.musicexplorer.data.model.ArtistModel;
import com.nokia.example.musicexplorer.data.model.GenericProductModel;
import com.nokia.example.musicexplorer.settings.ThumbnailSizes;


/**
 * A grid item displaying an image.
 */
public class GridItem extends CustomItem {
	
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
    
    public GridItem() {
    	super(null);
    }
    
    /**
     * Constructor.
     * @param imageUri A URI of the grid item image.
     * @param text A descriptive text of the item.
     */

    public GridItem(String imageUri, String text,
                    final int width, final int height)
    {
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
    
    public GridItem(ViewManager viewManager, GenericProductModel model, GridLayout parentGrid) {
    	this();

    	this.viewManager = viewManager;
    	this.model = model;
    	this.parentGrid = parentGrid;
    }
    
    public void sizeChanged(int w, int h) {
    }

    /**
     * Constructor.
     * @param GridItem The data item associated with this grid item.
     */
    
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
    
    // Setters and getters
    public GridItem getGridItem() {
        return gridItem;
    }
    
    /**
     * If the thumbnail URL is not set, get it from the model's hashtable.
     * @return
     */
    public String getImageUrl() {
    	if(imageUrl == null) {
        	try {
    			imageUrl = this.model.getThumbnailUrl(ThumbnailSizes.SIZE_100X100);
    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
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

    public void setImage(Image image) {
    	// When placing image, save the scaled version.
    	// TODO: Should this be done inside the task so that the scaled image is stored in the ApiCache?
    	thumbnail = JMEImageUtils.scaleImage(image, width, width, true, JMEImageUtils.WEIGHTED_AVERAGE_OPAQUE);

    	// Cannot use super's notifyStateChanged() as GridLayout is not a Form.
    	this.parentGrid.gridItemStateChanged();
    }
        
    /**
     * For convenience. Paints this item in the given position.
     * @param graphics The Graphics instance.
     * @param x The X coordinate of the item.
     * @param y The Y coordinate of the item.
     */
    public void paintXY(Graphics graphics, int x, int y) {
    	paint(graphics, x, y, width, height);
    }

    public class PlaceImageTask extends Task {
    	private GridItem gridItem;
    	public PlaceImageTask(GridItem gridItem) {
    		super(Task.NORMAL_PRIORITY);
    		this.gridItem = gridItem;
    	}
    	public Object exec(Object image) {
    		if(image instanceof Image) {
    			this.gridItem.setImage((Image) image);
    		}   		
    		return image;
    	}
    }
    
    protected void getImage() {
    	ApiCache.getImage(this.getImageUrl(), new PlaceImageTask(this));    		
    }
    
    /**
     * For convenience.
     */
    protected void paint(Graphics graphics, int x, int y, int width, int height) {
        
    	try {
            if (thumbnail != null) {
                graphics.drawImage(thumbnail, x, y, Graphics.TOP | Graphics.LEFT);
            } else {
        		getImage();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
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
    	return "GridItem: " + this.text;
    }
}