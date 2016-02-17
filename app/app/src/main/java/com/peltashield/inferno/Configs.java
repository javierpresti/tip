package com.peltashield.inferno;

import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Arrays;
import java.util.List;

/**
 * Created by javier on 4/8/15.
 */
public class Configs {

    //***********************************************************//
    //*** IMPORTANT CONFIGS TO ALWAYS CHECK BEFORE PUBLISHING ***//
    //***********************************************************//

    public static final int DB_VERSION = 1401;

    // Expansion file
    /**
     * If this is changed it must also be changed in the manifest provider CustomAPEZProvider's
     *      meta-data value.
     */
    public static final int EXP_FILE_VERSION = 1400; //!!WARNING!! READ JAVADOC BEFORE CHANGING
    public static final long EXP_FILE_SIZE = 62829497;


    //***********************//
    //*** REST OF CONFIGS ***//
    //***********************//
    private static final boolean SHOWING_ADS_IN_DEBUG = false;

    public static final int INIT_IAD_APPEARANCE_TIME = 180000;
    public static final int IAD_INTERVAL = 290000;

    private static final boolean USING_THUMB_IMAGES = false;

    /** Automatic down-scrolling should be made on a full-screen card view if the device display
     * height divided by its weigth is less than this value
     */
    private static final double SCROLLING_VERTICAL_RELATIVE = 1.35;
    private static final int FIRST_SIDE_NOT_DEMON_ID = 1711117;
    private static final int FIRST_SIDE_DEMON_ID = 1703117;
    private static final double RELATIVE_CARD_WIDTH = 0.69;
    private static final double RELATIVE_CARD_HEIGHT = 1.45;


    private Configs() {}

    public static boolean isUsingThumbImages() {
        return USING_THUMB_IMAGES;
    }
    public static boolean isShowingAds() {
        return !BuildConfig.DEBUG || SHOWING_ADS_IN_DEBUG;
    }
    public static double getScrollingVerticalRelative() {
        return SCROLLING_VERTICAL_RELATIVE;
    }

    public static boolean isPortrait(Resources res) {
        return res.getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    public static boolean isDemon(int id) {
        return id < 1013000 ||
                id > 1185000 && id < 1197000 || id > 1369000 && id < 1373000 ||
                id > 1501000 && id < 1506000 || id > 1621000 && id < 1625000 ||
                id > 1703000 && id < 1711000 || id > 1874000 && id < 1878000 ||
                id > 1964000 && id < 1969000;
    }

    public static boolean isLimited(int id) {
        return id == 1493107 || id == 1600109;
    }

    public static boolean isToken(int id) {
        return  id > 1181000 && id < 1185000 || id > 1365000 && id < 1369000 ||
                id > 1489000 && id < 1493000 || id > 1608000 && id < 1612000 ||
                id > 1863000 && id < 1866000 || id > 1962000 && id < 1964000 ||
                id > 2052000 && id < 2053000;
    }

    public static int getFirstSideNotDemonId() {
        return FIRST_SIDE_NOT_DEMON_ID;
    }

    public static int getFirstSideDemonId() {
        return FIRST_SIDE_DEMON_ID;
    }

    public static double getRelativeCardWidth() {
        return RELATIVE_CARD_WIDTH;
    }

    public static double getRelativeCardHeight() {
        return RELATIVE_CARD_HEIGHT;
    }
}
