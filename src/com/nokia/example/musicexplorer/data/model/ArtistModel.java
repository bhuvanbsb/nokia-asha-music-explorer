/**
 * Copyright (c) 2013 Nokia Corporation. All rights reserved. Nokia and Nokia
 * Connecting People are registered trademarks of Nokia Corporation. Oracle and
 * Java are trademarks or registered trademarks of Oracle and/or its affiliates.
 * Other product and company names mentioned herein may be trademarks or trade
 * names of their respective owners. See LICENSE.TXT for license information.
 */
package com.nokia.example.musicexplorer.data.model;

import org.json.me.JSONObject;

/**
 * Represents an Artist Product.
 */
public class ArtistModel extends GenericProductModel {

    /**
     * Constructor.
     * @param artist
     */
    public ArtistModel(JSONObject artist) {
        super(artist);
    }

    /**
     * For debugging purposes.
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "ArtistModel: name=" + name + ", id=" + id;
    }
}
