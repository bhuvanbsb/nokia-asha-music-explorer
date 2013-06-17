/**
 * Copyright (c) 2013 Nokia Corporation. All rights reserved. Nokia and Nokia
 * Connecting People are registered trademarks of Nokia Corporation. Oracle and
 * Java are trademarks or registered trademarks of Oracle and/or its affiliates.
 * Other product and company names mentioned herein may be trademarks or trade
 * names of their respective owners. See LICENSE.TXT for license information.
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
 * Tests network connectivity before making an actual API call.
 */
public class ApiRequestTask
        extends Task {

    private final Task callback;
    private final StaticWebCache cache;
    private final String url;
    private final int taskPriority;
    private final int cacheMode;
    private final ViewManager viewManager;
    
    /**
     * Constructor. Forks the tasks after parameters are set.
     * @param viewManager
     * @param cache
     * @param url
     * @param taskPriority
     * @param cacheMode
     * @param callback 
     */
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

    /**
     * Makes a test request to determine whether a network connection can be
     * established. Displays a network alert when the network is not available.
     * @return 
     */
    private boolean hasNetworkConnection() {
        boolean hasNetwork = false;

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