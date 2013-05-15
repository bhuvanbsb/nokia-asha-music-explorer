/**
* Copyright (c) 2013 Nokia Corporation. All rights reserved.
* Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
* Oracle and Java are trademarks or registered trademarks of Oracle and/or its
* affiliates. Other product and company names mentioned herein may be trademarks
* or trade names of their respective owners. 
* See LICENSE.TXT for license information.
*/

package com.nokia.example.musicexplorer.data.model;

public class GenreModel {
	public String id;
	public String name;

	public GenreModel(String _id, String _name) {
		id = _id;
		name = _name;
	}
	
	/**
	 * For debugging purposes.
	 * @return String A description of the object.
	 */
	public String toString() {
		return "GenreModel: name=" + this.name + ", id=" + this.id;
	}	
}
