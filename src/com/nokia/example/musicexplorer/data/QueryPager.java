/**
 * Copyright (c) 2013 Nokia Corporation. All rights reserved. Nokia and Nokia
 * Connecting People are registered trademarks of Nokia Corporation. Oracle and
 * Java are trademarks or registered trademarks of Oracle and/or its affiliates.
 * Other product and company names mentioned herein may be trademarks or trade
 * names of their respective owners. See LICENSE.TXT for license information.
 */
package com.nokia.example.musicexplorer.data;

import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.tantalum.util.L;

/**
 * Keeps track of query paging.
 */
public class QueryPager {

    /**
     * Start index of a query. First query starts from 0.
     */
    private int currentIndex = 0;
    /**
     * Total amount of items.
     */
    private int total = 0;
    /**
     * Amount of items per query.
     */
    private int itemsPerPage = 10;

    /**
     * Constructor with default settings.
     */
    public QueryPager() {
    }

    /**
     * Initialize from a JSONObject.
     *
     * @param paging
     */
    public QueryPager(JSONObject paging) {
        setPaging(paging);
    }

    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public void reset() {
        currentIndex = 0;
        total = 0;
        itemsPerPage = 10;
    }

    public String getCurrentQueryString() {
        return "&itemsperpage=" + itemsPerPage + "&startindex=" + currentIndex;
    }

    public String getQueryStringForNextPage() {
        currentIndex += itemsPerPage;
        return getCurrentQueryString();
    }

    /**
     * Checks whether there are pages still left.
     *
     * @return boolean True if there are more pages.
     */
    public boolean hasMorePages() {
        int itemsLeft = total - currentIndex;

        // If itemsLeft is greater than itemsPerPage, there's still items left.
        return itemsLeft > itemsPerPage;
    }

    public void setPaging(JSONObject paging) {
        try {
            currentIndex = paging.getInt("startindex");
            total = paging.getInt("total");
        } catch (JSONException e) {
            L.e("Could not initialize a QueryPager", "", e);
        }
    }
}
