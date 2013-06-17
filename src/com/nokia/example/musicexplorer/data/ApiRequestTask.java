/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nokia.example.musicexplorer.data;

import com.nokia.example.musicexplorer.settings.ApiEndpoint;
import com.nokia.example.musicexplorer.ui.ViewManager;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import org.tantalum.Task;
import org.tantalum.net.StaticWebCache;
import org.tantalum.util.L;

/**
 * Determines the state of network connectivity by making a test request.
 * @return 
 */

public class ApiRequestTask
        extends Task {

    private final Task callback;
    private final StaticWebCache cache;
    private final String url;
    private final int taskPriority;
    private final int cacheMode;
    private final ViewManager viewManager;
    
    public ApiRequestTask(
            ViewManager viewManager,
            StaticWebCache cache, 
            String url, 
            int taskPriority, 
            int cacheMode, 
            Task callback) {
        this.cache = cache;
        this.url = url;
        this.taskPriority = taskPriority;
        this.cacheMode = cacheMode;
        this.callback = callback;
        this.viewManager = viewManager;
        this.fork();
    }

    protected Object exec(Object o) {
        if(hasNetworkConnection()) {
            cache.getAsync(
                    url,
                    taskPriority,
                    cacheMode,
                    callback);
        }

        return o;
    }

    private boolean hasNetworkConnection() {
        boolean hasNetwork = false;

        // Make test request
        HttpConnection httpConnection;
        String testRequestUrl = ApiEndpoint.getBaseUrl();

        try {
            L.i("Testing network connection", "URL: " + testRequestUrl);

            httpConnection = 
                    (HttpConnection) Connector.open(testRequestUrl);

            L.i("Response message", httpConnection.getResponseMessage());
            L.i("Network connection established", httpConnection.toString());

            hasNetwork = true;
            httpConnection.close();
        } catch (IOException e) {
            L.i("Network connection failed", e.toString());
            viewManager.showNetworkAlert();
        }

        return hasNetwork;
    }    

}