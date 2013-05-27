/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nokia.example.musicexplorer.ui;

import com.nokia.example.musicexplorer.data.model.ArtistModel;
import com.nokia.example.musicexplorer.utils.CategoryBarUtils;
import com.nokia.mid.ui.CategoryBar;
import com.nokia.mid.ui.ElementListener;
import com.nokia.mid.ui.IconCommand;
import java.io.IOException;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import org.tantalum.util.L;

/**
 * Responsible for displaying a category bar and switching views between
 * Artist Info and Similar Artists.
 */
public class ArtistView
        extends Form
        implements CategoryBarUtils.ElementListener,
        CategoryBarUtils.CategoryBarHolder {
    
    private static final int AMOUNT_OF_CATEGORY_BAR_ITEMS = 2;
    private static final int ARTIST_INFO_VIEW_INDEX = 0;
    private static final int SIMILAR_ARTISTS_VIEW_INDEX = 1;
    
    private CategoryBar categoryBar;
    private ViewManager viewManager;
    private ArtistModel artistModel;
    private ArtistInfoView artistInfoView;
    private SimilarArtistsView similarArtistsView;
    private Displayable lastViewedTab;
    
    /**
     * Constructs the view based on an artist model.
     * @param viewManager
     * @param artistModel 
     */
    public ArtistView(ViewManager viewManager, ArtistModel artistModel) {
        this(viewManager);
        this.artistModel = artistModel;
        this.artistInfoView = new ArtistInfoView(viewManager, artistModel);
        displayCategoryBar();
        showArtistInfoView();
    }
    
    /**
     * Constructs the view based on an artist id.
     * @param viewManager
     * @param artistId 
     */
    public ArtistView(ViewManager viewManager, int artistId) {
        this(viewManager);
        this.artistInfoView = new ArtistInfoView(viewManager, artistId, this);
        displayCategoryBar();
        showArtistInfoView();
    }
    
    public void setArtistModel(ArtistModel artistModel) {
        this.artistModel = artistModel;
    }
    
    /**
     * Sets view manager.
     * @param viewManager 
     */
    private ArtistView(ViewManager viewManager) {
        super(null);
        this.viewManager = viewManager;
    }
    
    /**
     * Initializes a category bar for switching between 
     * Artist Info and Similar Artists views.
     */
    private boolean displayCategoryBar() {
        if (this.categoryBar == null) {
            // Load images
            Image imageInfo = loadImage("/info_icon.png");        
            Image imageSimilar = loadImage("/similar_icon.png");     

            if (imageInfo == null || imageSimilar == null) {
                L.i("Could not load category bar images.", "");
                return false;
            }

            IconCommand artistInfo = new IconCommand(
                                            "Info", 
                                            imageInfo, 
                                            null,
                                            Command.SCREEN, 1);

            IconCommand similarArtists = new IconCommand(
                                            "Similar", 
                                            imageSimilar, 
                                            null,
                                            Command.SCREEN, 1);    

            IconCommand[] iconCommands = 
                    new IconCommand[AMOUNT_OF_CATEGORY_BAR_ITEMS];

            // Indices are used later to detect which of the icons was pressed.
            iconCommands[ARTIST_INFO_VIEW_INDEX] = artistInfo;
            iconCommands[SIMILAR_ARTISTS_VIEW_INDEX] = similarArtists;

            this.categoryBar = new CategoryBar(
                    iconCommands, 
                    true, 
                    CategoryBar.ELEMENT_MODE_STAY_SELECTED);        
        }
        
        CategoryBarUtils.setListener(categoryBar, this);
        categoryBar.setVisibility(true);

        return true;
    }
    
    private Image loadImage(String pathToResource) {
        Image image = null;
        
        try {
            image = Image.createImage(pathToResource);
        } catch(IOException e) {
            L.e(
                "Could not load image.", 
                pathToResource != null ? 
                pathToResource : "Path to resource is null", 
                e);
        }
        
        return image;
    }
    
    /**
     * Handles CategoryBar events, tells the currently visible CategoryBarView
     * to switch view to whatever item is tapped
     * @param categoryBar
     * @param selectedIndex 
     */
    public void notifyElementSelected(CategoryBar categoryBar, int selectedIndex) {
        switch (selectedIndex) {
            case ElementListener.BACK:
                categoryBar.setVisibility(false);
                viewManager.goBack();
                break;
            case ARTIST_INFO_VIEW_INDEX:
                showArtistInfoView();
                break;
            case SIMILAR_ARTISTS_VIEW_INDEX:
                showSimilarArtistsView();
                break;
            default:
                break;
        }
    }

    /**
     * Relies on that artist info view is always instantiated in constructor.
     */
    private void showArtistInfoView() {
        lastViewedTab = artistInfoView;
        viewManager.showSubview(artistInfoView);
    }
    
    /**
     * Creates the view and displays it.
     */
    private void showSimilarArtistsView() {
        if (similarArtistsView == null) {
            similarArtistsView = new SimilarArtistsView(viewManager, this.artistModel.id);
        }
        lastViewedTab = similarArtistsView;
        viewManager.showSubview(similarArtistsView);
    }

    public void hideCategoryBar() {
        this.categoryBar.setVisibility(false);
    }

    public void showCategoryBar() {
        this.categoryBar.setVisibility(true);
    }

    public void showLastViewedTab() {
        viewManager.showSubview(lastViewedTab);
    }
    
}
