/**
 * Copyright (c) 2013 Nokia Corporation. All rights reserved. Nokia and Nokia
 * Connecting People are registered trademarks of Nokia Corporation. Oracle and
 * Java are trademarks or registered trademarks of Oracle and/or its affiliates.
 * Other product and company names mentioned herein may be trademarks or trade
 * names of their respective owners. See LICENSE.TXT for license information.
 */

package com.nokia.example.musicexplorer.data;

import java.io.UnsupportedEncodingException;

import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.tantalum.storage.CacheView;
import org.tantalum.util.L;

/**
 * Used for converting a raw string response to a JSONObject. The response comes
 * from a server and represents the body of an HTTP response.
 */
public class JSONResponseHandler implements CacheView {

    public Object convertToUseForm(Object key, byte[] bytes) {
        String jsonString = null;
        try {
            jsonString = new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            //#debug			
            L.e("Unable to instantiate a UTF-8 string from bytes.", "", e);
        }
        
        JSONObject response = null;
        try {
            response = new JSONObject(jsonString);
        } catch (JSONException e) {
            //#debug			
            L.e("Unable to instantiate a JSONObject from response.", "", e);
        }
        
        return response;
    }
}
