package com.peltashield.inferno.utils;

import android.app.Activity;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.peltashield.inferno.BuildConfig;
import com.peltashield.inferno.Configs;
import com.peltashield.inferno.R;

/**
 * Created by javier on 4/9/15.
 */
public class AdManager {

    protected static AdRequest.Builder adBuilder;
    protected static InterstitialAd iAd;
    protected static long lastIAdAppearanceTime = System.currentTimeMillis() +
            Configs.INIT_IAD_APPEARANCE_TIME;

    public static final String BANNER_SEARCHER_UNIT = "ca-app-pub-7777351944943506/3395633471";
    public static final String BANNER_SEARCHER_BASIC_UNIT =
            "ca-app-pub-7777351944943506/1220896272";
    public static final String INTERSTITIAL_CARD_POOL_UNIT =
            "ca-app-pub-7777351944943506/3256032679";
    public static final String BANNER_CARD_VIEW_UNIT = "ca-app-pub-7777351944943506/9302566271";


    protected static final String BANNER_TEST_UNIT = "ca-app-pub-3940256099942544/6300978111";
    protected static final String INTERSTITIAL_TEST_UNIT =
            "ca-app-pub-3940256099942544/1033173712";

    private AdManager() {
    }

    public static void resetTime() {
        lastIAdAppearanceTime = System.currentTimeMillis();
    }

    public static void showIAdIfReady() {
        if (Configs.isShowingAds() && isIAdReady()) {
            iAd.show();
            resetTime();
        }
    }

    public static boolean isIAdReady() {
        boolean timeUp = System.currentTimeMillis() - lastIAdAppearanceTime > Configs.IAD_INTERVAL;
        return timeUp && iAd.isLoaded();
    }

    public static void loadIAd(Activity activity, String adUnit) {
        String unit = BuildConfig.DEBUG ? getTestInterstitialId() : adUnit;
        iAd = new InterstitialAd(activity);
        iAd.setAdUnitId(unit);
        add(iAd);

        iAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                add(iAd);
            }
        });
}

    protected static void add(InterstitialAd iAd) {
        AdRequest adRequest = getAdBuilder().build();
        iAd.loadAd(adRequest);
    }

    public static void add(AdView adView, AdSize adSize, String adUnit) {
        String unit = BuildConfig.DEBUG ? getTestBannerId() : adUnit;
        adView.setAdUnitId(unit);
        adView.setAdSize(adSize);
        AdRequest adRequest = getAdBuilder().build();
        adView.loadAd(adRequest);
    }

    protected static AdRequest.Builder getAdBuilder() {
        return adBuilder == null ? new AdRequest.Builder() : adBuilder;
    }

    public static void loadAds(Activity activity, int adId) {
        AdView adView = new AdView(activity);
        adView.setId(adId);
        RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        adView.setLayoutParams(adParams);
        RelativeLayout layout = (RelativeLayout) activity.findViewById(R.id.layout);
        layout.addView(adView);
    }

    protected static String getTestBannerId() {
        return BANNER_TEST_UNIT;
    }

    protected static String getTestInterstitialId() {
        return INTERSTITIAL_TEST_UNIT;
    }

}
