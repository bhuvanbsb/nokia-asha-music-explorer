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

    public String duration; // In seconds
    public int sequence;

    public TrackModel(JSONObject track) {
        super(track);

        try {
            this.duration = track.getString("duration");
            this.sequence = track.getInt("sequence");
        } catch (JSONException e) {
            L.e("Could not parse Track from JSON", "", e);
        }
    }

    /**
     * For debugging purposes.
     *
     * @return String A description of the object.
     */
    public String toString() {
        return "TrackModel: name=" + name + ", id=" + id;
    }
}
