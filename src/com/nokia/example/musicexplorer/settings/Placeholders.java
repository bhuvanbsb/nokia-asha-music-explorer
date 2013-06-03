/**
 * Copyright (c) 2013 Nokia Corporation. All rights reserved. Nokia and Nokia
 * Connecting People are registered trademarks of Nokia Corporation. Oracle and
 * Java are trademarks or registered trademarks of Oracle and/or its affiliates.
 * Other product and company names mentioned herein may be trademarks or trade
 * names of their respective owners. See LICENSE.TXT for license information.
 */
package com.nokia.example.musicexplorer.settings;

import com.nokia.example.musicexplorer.data.model.AlbumModel;
import com.nokia.example.musicexplorer.data.model.ArtistModel;
import com.nokia.example.musicexplorer.data.model.GenericProductModel;
import java.io.IOException;
import java.util.Hashtable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import org.tantalum.util.L;

/**
 * Placeholder images for artists and albums that do not have a thumbnail.
 */
public class Placeholders {
    public static final String ARTIST_PLACEHOLDER = "/artist_icon.png";
    public static final String ALBUM_PLACEHOLDER = "/album_icon.png";
    
    private static Hashtable images;
    
    /**
     * Loads images to a hashtable for further use.
     * @param pathToImage One of the static strings associated with this class.
     * @return 
     */
    public static Image load(String pathToImage) {
        Image image = null;
        
        if(images == null) {
            images = new Hashtable();
        }
        
        if(images.containsKey(pathToImage)) {
            image = (Image) images.get(pathToImage);
        } else {
            try {
                image = Image.createImage(pathToImage);
                images.put(pathToImage, image);
            } catch (IOException e) {
                L.e("Could not load image", pathToImage, e);
            }
        }
        
        return image;
    }
    
    /**
     * Paints a placeholder image using a centered anchor. The center point is 
     * calculated from x, y, width and height.
     * 
     * @param graphics
     * @param model
     * @param x
     * @param y
     * @param width
     * @param height 
     */
    public static void paint(Graphics graphics, GenericProductModel model, int x, int y, int width, int height) {
        x = x + width / 2;
        y = y + height / 2;
        paint(graphics, model, x, y, Graphics.VCENTER | Graphics.HCENTER);
    }
    
    /**
     * Paint a placeholder image to the given point (x,y) using a custom anchor.
     * @param graphics
     * @param model
     * @param x
     * @param y
     * @param anchor 
     */
    public static void paint(Graphics graphics, GenericProductModel model, int x, int y, int anchor) {
        if (model instanceof ArtistModel) {
            graphics.drawImage(
                    load(Placeholders.ARTIST_PLACEHOLDER), 
                    x, 
                    y, 
                    anchor);

        } else if (model instanceof AlbumModel) {
            graphics.drawImage(
                    load(Placeholders.ALBUM_PLACEHOLDER), 
                    x, 
                    y, 
                    anchor);
        }
    }
}
