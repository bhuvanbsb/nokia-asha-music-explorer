/**
 * Copyright (c) 2013 Nokia Corporation. All rights reserved. Nokia and Nokia
 * Connecting People are registered trademarks of Nokia Corporation. Oracle and
 * Java are trademarks or registered trademarks of Oracle and/or its affiliates.
 * Other product and company names mentioned herein may be trademarks or trade
 * names of their respective owners. See LICENSE.TXT for license information.
 */

package com.nokia.example.musicexplorer.data.model;

import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.tantalum.util.L;

/**
 * Represents a genre.
 */
public class GenreModel {

    public String id;
    public String name;

    /**
     * Constructor.
     * @param genreModel
     */
    public GenreModel(JSONObject genreModel) {
        try {
            this.id = genreModel.getString("id");
            this.name = genreModel.getString("name");
        } catch (JSONException e) {
            L.e("Could not parse genre JSON", "", e);
        }
    }

    /**
     * Constructor.
     * @param id
     * @param name
     */
    public GenreModel(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * For debugging purposes.
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "GenreModel: name=" + this.name + ", id=" + this.id;
    }
}
