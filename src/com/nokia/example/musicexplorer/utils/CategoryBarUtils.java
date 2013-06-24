/**
* Copyright (c) 2012-2013 Nokia Corporation. All rights reserved.
* Nokia and Nokia Connecting People are registered trademarks of Nokia Corporation. 
* Oracle and Java are trademarks or registered trademarks of Oracle and/or its
* affiliates. Other product and company names mentioned herein may be trademarks
* or trade names of their respective owners. 
* See LICENSE.TXT for license information.
*/

package com.nokia.example.musicexplorer.utils;

import com.nokia.mid.ui.CategoryBar;

public class CategoryBarUtils {

    /**
     * Wrapper for com.nokia.mid.ui.CategoryBar#setElementListener method.
     */
    public static void setListener(final CategoryBar bar,
        final ElementListener listener) {
        bar.setElementListener(listener == null ? null
            : new com.nokia.mid.ui.ElementListener() {
            
            public void notifyElementSelected(CategoryBar bar, int selectedIndex) {
                listener.notifyElementSelected(bar, selectedIndex);
            }
        });
    }

    /**
     * ElementListener interface for devices which don't support com.nokia.mid.ui.ElementListener.
     */
    public interface ElementListener {
        public void notifyElementSelected(CategoryBar categoryBar, int selectedIndex);
    }

    /**
     * For those views that implement category bar. This allows view manager to 
     * show and hide the category bar when e.g. going back from a view with
     * a category bar or coming back to a view that has a category bar.
     */
    public interface CategoryBarHolder {
        public void hideCategoryBar();
        public void showCategoryBar();
        
        /**
         * When coming back to a view that implements a category bar, this 
         * method is responsible for displaying the last viewed tab.
         */
        public void showLastViewedTab();
    }
}
