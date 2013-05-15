/**
* Copyright (c) 2013 Nokia Corporation. All rights reserved.
* Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
* Oracle and Java are trademarks or registered trademarks of Oracle and/or its
* affiliates. Other product and company names mentioned herein may be trademarks
* or trade names of their respective owners. 
* See LICENSE.TXT for license information.
*/

package com.nokia.example.musicexplorer.data.model;

import org.json.me.JSONObject;
import org.json.me.JSONArray;
import org.json.me.JSONException;

public class ArtistModel extends GenericProductModel {
	
	public ArtistModel(JSONObject track) {
		super(track);
	}
	
	/**
	 * For debugging purposes.
	 * @return String A description of the object.
	 */
	public String toString() {
		return "ArtistModel: name=" + name + ", id=" + id;
	}
}
