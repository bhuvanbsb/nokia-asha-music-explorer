Nokia Asha Music Explorer
=========================

Nokia Asha Music Explorer demonstrates how the Nokia Music REST API can be used.
The application uses LCDUI components and the Tantalum 5 library.

The Nokia Music REST API is divided into resources. The application consists of 
several views that each use different API resources. Each resource is
responsible of a specific function, e.g., the Search Resource handles search
queries. 

For full API reference, see the ​Nokia Music REST API Reference: 
http://api.ent.nokia.com/restapi.html

This example demonstrates:

* Usage of Nokia Music REST API
* How to consume JSON data using the Tantalum 5 library
* Asynchronous image loading
* Implementing a drill-down UI

The application is hosted in GitHub:
https://github.com/nokia-developer/nokia-asha-music-explorer

For more information on the implementation, visit Java Developer's Library:
http://developer.nokia.com/Resources/Library/Java/#!code-examples/web-services-musicexplorer.html


1. Prerequisities
-------------------------------------------------------------------------------

* Java ME basics
* Java ME threads


2. Important files and classes
-------------------------------------------------------------------------------

* `src\..\data\ApiCache.java`
* `src\..\data\JSONResponseHandler.java`
* `src\..\settings\ApiEndpoint.java`
* `src\..\ui\ViewManager.java`

Classes: `GridItem`, `GridLayout`, `ListItem`


3. Design considerations
-------------------------------------------------------------------------------

The application has been designed to allow the user to browse Nokia Music using
a drill down user-interface. The user can view new releases, popular releases,
search for artists and browse artists by genres. As the Nokia Music REST API 
can return albums, tracks, singles and artists as results, the application is
restricted to display only artist and album results. This means that searching
for individual tracks is currently not possible, but is easy to enable if 
such feature is wanted.

The data model is stored in simple classes that represent the data that is
available through the REST API. As the REST API uses JSON (JavaScript Object
Notation) as a response format, the responses are parsed to e.g. models using 
suitable mechanisms and practices provided by the Tantalum 5 library.

To speed up loading of views, image content is loaded asynchronously using
worker threads. The behaviour is similar to what can be found in many 
JavaScript web applications. When loading an image, a placeholder image 
is shown until the image has been successfully loaded.


4. Known issues
-------------------------------------------------------------------------------

* `java.lang.NumberFormatException` shown sometimes in the log during JSON parsing.
  This issue does not affect the application and does not cause any crashes.
* The artist view might appear empty if the artist does not have any albums.
* The user can navigate to new Artist views from the Similar artists view.
  These opened Artist views grow the view history and the device may run out of 
  memory at some point.


5. Build and installation instructions
-------------------------------------------------------------------------------

The example has been created with NetBeans 7.3 and Nokia Asha SDK 1.0. The 
project can be easily opened in NetBeans by selecting 'Open Project' from the
File menu and selecting the application's project folder.

Before opening the project, make sure the Nokia Asha SDK 1.0 or newer is
installed and added to NetBeans. Building is done by selecting 'Build main
project'. Also make sure the JAR files inside the lib folder are included
into the project (Properties -> Libraries & Resources -> Add Jar/ZIP). To see 
debug log texts, the debug version of Tantalum must be included in the
build by selecting it from the lib folder as described.

You can install the application on a phone by transferring the JAR file
with Nokia Suite, over bluetooth or by placing the JAR & JAD files to 
the phone's memory card using a USB cable and the mass storage functionality.

The example can also be built and run with Eclipse.


6. Running the example
-------------------------------------------------------------------------------

The application requires a network connection in order to work. Network
connection is used for accessing the Nokia Music REST API. All views except the
home view make use of the REST API. Developers should register themselves an
API key for the Nokia Music REST API to use in their own implementations. 
This version of Nokia Asha Music Explorer contains a demo API key that can be
used to try out the application and its features.

The search feature requires inputting a search string. The search allows making
full word queries only. For example, searching for "Madon" lists only those 
that contain the word "Madon" but not e.g. "Madonna". A search query is made
automatically after the user has stopped typing.


7. Compatibility
-------------------------------------------------------------------------------

Nokia Asha software platform 1.0 and newer.

An Internet connection is required.

Tested on Nokia Asha 501 (Java Runtime 3.0.0 for Series 40).

Developed with:

* Netbeans 7.3
* Nokia Asha SDK 1.0


8. Change history
-------------------------------------------------------------------------------

* 1.0 The first version published at developer.nokia.com.
