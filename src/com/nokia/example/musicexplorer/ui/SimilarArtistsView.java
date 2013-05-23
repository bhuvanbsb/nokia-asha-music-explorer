/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nokia.example.musicexplorer.ui;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import org.tantalum.util.L;

/**
 *
 * @author jukka_000
 */
public class SimilarArtistsView 
        extends Form
        implements CommandListener {
    
    public SimilarArtistsView(ViewManager viewManager, int artistId) {
        super("Artists you might like...");
        append("Rihanna");
    }

    public void commandAction(Command c, Displayable d) {
        L.i("Received command", c.toString());
    }
    
}
