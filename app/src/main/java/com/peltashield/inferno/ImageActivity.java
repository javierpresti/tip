package com.peltashield.inferno;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ImageView;

import com.android.vending.expansion.zipfile.ZipResourceFile;
import com.google.android.gms.ads.AdView;
import com.google.android.vending.expansion.downloader.Helpers;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.peltashield.inferno.cardsDB.CardField;
import com.peltashield.inferno.utils.ImageLoaderInitializer;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by javier on 7/21/15.
 */
public class ImageActivity extends DbActivity {

    protected ZipResourceFile expansionFile;

    @Override
    protected void onCreate(Bundle savedInstanceState, int contentLayout) {
        super.onCreate(savedInstanceState, contentLayout);
        ImageLoaderInitializer.init(this);
    }

    public void loadCard(ImageView imageView, int cardId, boolean isPortrait, boolean isThumb) {
        try {
            InputStream fileStream = getCardImageStream(cardId, false);
            if (fileStream != null) {
                String imageId = "stream://" + fileStream.hashCode();
                ImageLoaderInitializer.getInstance(this).displayImage(imageId, imageView,
                        ImageLoaderInitializer.getOptions(fileStream, cardId, isPortrait, isThumb));
            } else {
                imageView.setImageDrawable(null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayCardImage(int cardId, ImageView imageView, boolean flip,
                                 boolean showImageOnLoading) {
        try {
            InputStream fileStream = getCardImageStream(cardId, flip);
            if (fileStream != null) {
                String imageId = "stream://" + fileStream.hashCode();
                ImageLoaderInitializer.getInstance(this).displayImage(imageId, imageView,
                        ImageLoaderInitializer.getOptions(fileStream, showImageOnLoading));
                imageView.setVisibility(View.VISIBLE);
            } else {
                imageView.setImageDrawable(null);
                imageView.setVisibility(View.GONE);
            }
        } catch (IOException e) {
            imageView.setVisibility(View.GONE);
            e.printStackTrace();
        }
    }

    protected ZipResourceFile getExpansionFile() throws IOException {
        if (expansionFile == null) {
            String fileName = Helpers.getExpansionAPKFileName(this, true,
                    Configs.EXP_FILE_VERSION);
            fileName = Helpers.generateSaveFileName(this, fileName);
            expansionFile = new ZipResourceFile(fileName);
        }
        return expansionFile;
    }

    public InputStream getCardImageStream(int cardId, boolean flip) throws IOException {
        return getExpansionFile().getInputStream(CardField.getCardImageName(cardId, false, flip));
    }

    public boolean canDisplayImage() {
        boolean canDisplay = true;
        try {
            getExpansionFile();
        } catch (IOException e) {
            canDisplay = false;
            e.printStackTrace();
        }
        return canDisplay;
    }

    public void hideActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar.isShowing()) {
            try {
                actionBar.getClass().getDeclaredMethod("setShowHideAnimationEnabled",
                        boolean.class).invoke(actionBar, false);
            } catch (Exception exception) {
                // Animation will be run
            }
            actionBar.hide();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Configs.isShowingAds()) {
            ((AdView) findViewById(R.id.adView)).resume();
        }
    }

    @Override
    protected void onPause() {
        if (Configs.isShowingAds()) {
            ((AdView) findViewById(R.id.adView)).pause();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (Configs.isShowingAds()) {
            ((AdView) findViewById(R.id.adView)).destroy();
        }
        super.onDestroy();
    }
}
