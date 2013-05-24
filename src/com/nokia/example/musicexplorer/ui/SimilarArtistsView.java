/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nokia.example.musicexplorer.ui;

import com.nokia.example.musicexplorer.data.ApiCache;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import org.json.me.JSONObject;
import org.tantalum.CancellationException;
import org.tantalum.Task;
import org.tantalum.TimeoutException;
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
        getSimilarArtists(artistId);
    }

    public void getSimilarArtists(int artistId) {
        ApiCache.getSimilarArtistsById(artistId, new PlaceResultsTask(), "");
    }
    
    public void commandAction(Command c, Displayable d) {
        L.i("Received command", c.toString());
    }
    
    public class PlaceResultsTask 
            extends Task {
        
        public PlaceResultsTask() {
        }

        protected Object exec(Object response) {
            if(response instanceof JSONObject) {
                append(response.toString());
            }
            return response;
        }
        
    }
    
}
