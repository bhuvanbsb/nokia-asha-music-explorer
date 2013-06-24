/**
 * Copyright (c) 2013 Nokia Corporation. All rights reserved. Nokia and Nokia
 * Connecting People are registered trademarks of Nokia Corporation. Oracle and
 * Java are trademarks or registered trademarks of Oracle and/or its affiliates.
 * Other product and company names mentioned herein may be trademarks or trade
 * names of their respective owners. See LICENSE.TXT for license information.
 */

package com.nokia.example.musicexplorer.data.model;

import org.json.me.JSONObject;
import org.tantalum.util.L;

/**
 * Represents a performer. Performers are used in listing artists for albums.
 */
public class PerformerModel {
    public String name = "";
    public int id = 0;

    /**
     * Constructor.
     * @param performer
     */
    public PerformerModel(JSONObject performer) {
        try {
            name = performer.getString("name");
            id = performer.getInt("id");
        } catch (Exception e) {
            L.e("Could not parse performer details", "", e);
        }
    }

    /**
     * For debugging purposes.
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "PerformerModel " + 
                "name=" + 
                name + 
                ", id=" + 
                Integer.toString(id);
    }
}
