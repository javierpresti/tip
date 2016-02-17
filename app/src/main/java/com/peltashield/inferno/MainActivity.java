package com.peltashield.inferno;

import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.peltashield.inferno.searchers.SearchersFragment;
import com.peltashield.inferno.tournament.TournamentPlayersFragment;
import com.peltashield.inferno.utils.AdManager;

public class MainActivity extends ImageActivity
        implements TournamentPlayersFragment.OnFragmentInteractionListener {

    protected Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_main);

        if (savedInstanceState == null) {
            addFragment(new SearchersFragment());
        }

        if (Configs.isShowingAds()) {
            loadAds();
        }
    }

    public void addFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment)
                .commit();
    }

    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment)
                .commit();
    }

    public void continueToFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment)
                .addToBackStack(String.valueOf(System.currentTimeMillis())).commit();
    }

    public void showToastText(String text) {
        if (text != null) {
            if (toast == null) {
                toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
            } else {
                toast.setText(text);
            }
            toast.show();
        }
    }

    public void showToastText(int stringId) {
        showToastText(getResources().getString(stringId));
    }

    public void openAbout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_about, null);
        TextView textView = (TextView) view.findViewById(R.id.about_text);
        textView.setText(Html.fromHtml(getString(R.string.about_info)));
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        builder.setView(view);
        builder.show();
    }

    protected void loadAds() {
        AdManager.loadAds(this, R.id.adView);
        AdManager.add((AdView) findViewById(R.id.adView), AdSize.SMART_BANNER,
                AdManager.BANNER_SEARCHER_UNIT);

        //AdManager.loadIAd(this, AdManager.INTERSTITIAL_CARD_POOL_UNIT);
    }

    public Toast getToast() {
        return toast;
    }

    @Override
    protected void onStop() {
        if (toast != null) {
            toast.cancel();
        }
        super.onStop();
    }

    public void onFragmentInteraction(String id){}
    public void onFragmentInteraction(Uri uri){}

}
