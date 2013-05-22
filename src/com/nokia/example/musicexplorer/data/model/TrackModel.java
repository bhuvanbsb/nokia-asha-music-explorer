/**
 * Copyright (c) 2013 Nokia Corporation. All rights reserved. Nokia and Nokia
 * Connecting People are registered trademarks of Nokia Corporation. Oracle and
 * Java are trademarks or registered trademarks of Oracle and/or its affiliates.
 * Other product and company names mentioned herein may be trademarks or trade
 * names of their respective owners. See LICENSE.TXT for license information.
 */
package com.nokia.example.musicexplorer.data.model;

import org.json.me.JSONObject;
import org.json.me.JSONException;

import org.tantalum.util.L;

/**
 * Represents a Track product.
 */
public class TrackModel 
		extends GenericProductModel {

    public int duration = 0; // In seconds
    public int sequence = 0;

    public TrackModel(JSONObject track) {
        super(track);

        try {
                this.duration = track.getInt("duration");
                this.sequence = track.getInt("sequence");
        } catch (JSONException e) {
                L.e("Could not parse Track from JSON", "", e);
        }
    }

    public String getFormattedDuration() {
        if(duration <= 0) {
                return "";
        }

        int seconds = duration;
        int minutes = seconds / 60;
        
        seconds = seconds - (minutes * 60);

        String leadingZero = "";
        // Add leading zero
        if(seconds < 10) {
            leadingZero = "0";
        }

        return 
            "(" + 
            Integer.toString(minutes) + 
            ":" + 
            leadingZero +
            Integer.toString(seconds) + 
            ")";
    }

    /**
     * For debugging purposes.
     * @return String A description of the object.
     */
    public String toString() {
            return "TrackModel: name=" + name + ", id=" + id;
    }
}
