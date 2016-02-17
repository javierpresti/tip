package com.peltashield.inferno.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.peltashield.inferno.Configs;
import com.peltashield.inferno.MainActivity;
import com.peltashield.inferno.R;
import com.peltashield.inferno.model.tournament.TournamentLite;

/**
 * Created by javier on 5/4/15.
 */
public class Utils {

    private Utils() {}

    public static void setAdBannerPadding(final Activity activity, final ViewGroup viewGroup) {
        if (Configs.isShowingAds()) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    viewGroup.setClipToPadding(false);
                    viewGroup.setPadding(viewGroup.getPaddingLeft(), viewGroup.getPaddingTop(),
                            viewGroup.getPaddingRight(),
                            activity.findViewById(R.id.adView).getHeight());
                }
            });
        }
    }

    public static void setCheckBoxLeftPadding(Resources res, View checkBox) {
        if (Build.VERSION.SDK_INT < 17) {
            final float scale = res.getDisplayMetrics().density;
            checkBox.setPadding(checkBox.getPaddingLeft() + (int) (30.0f * scale + 0.5f),
                    checkBox.getPaddingTop(),
                    checkBox.getPaddingRight(),
                    checkBox.getPaddingBottom());
        }
    }

    public static TournamentLite getTournament(Activity activity) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        String json = sharedPref.getString(activity.getString(R.string.saved_tournament), null);
        if (json == null) {
            return new TournamentLite();
        } else {
            return new Gson().fromJson(json, TournamentLite.class);
        }
    }

    public static void saveTournament(Activity activity, TournamentLite tournament) {
        SharedPreferences.Editor editor = activity.getPreferences(Context.MODE_PRIVATE).edit();
        editor.putString(activity.getString(R.string.saved_tournament),
                new Gson().toJson(tournament));
        editor.apply();
    }

    public static String[][][] getRounds(Activity activity) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        String json = sharedPref.getString(activity.getString(R.string.saved_tournament) + "round", null);
        if (json == null) {
            return getTournament(activity).getRounds();
        } else {
            return new Gson().fromJson(json, new TypeToken<String[][][]>() {}.getType());
        }
    }

    public static void saveRounds(Activity activity, String[][][] rounds) {
        SharedPreferences.Editor editor = activity.getPreferences(Context.MODE_PRIVATE).edit();
        editor.putString(activity.getString(R.string.saved_tournament) + "round",
                new Gson().toJson(rounds));
        editor.apply();
    }




    //    public static List<Player> getTournamentPlayers(Activity activity) {
/*        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        String json = sharedPref.getString(activity.getString(R.string.saved_players), null);
        if (json == null) {
            return new ArrayList<>();
        } else {
            return new Gson().fromJson(json, new TypeToken<List<Player>>(){}.getType());
        }
*/
/*        Round round = getTournamentRound(activity);
        if (round == null) {
            round = new Round();
            saveRound(activity, round);
        }
        return round.getPlayers();
    }

    public static Round getTournamentRound(Activity activity) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        String json = sharedPref.getString(activity.getString(R.string.saved_round), null);
        return new Gson().fromJson(json, Round.class);
    }

    public static void saveTournamentPlayers(Activity activity, List<Player> players) {
        Round round = getTournamentRound(activity);
        round.setPlayers(players);
        saveRound(activity, round);
//        SharedPreferences.Editor editor = activity.getPreferences(Context.MODE_PRIVATE).edit();
//        editor.putString(activity.getString(R.string.saved_players), new Gson().toJson(players));
//        editor.apply();
    }

    public static void saveRound(Activity activity, Round round) {
        SharedPreferences.Editor editor = activity.getPreferences(Context.MODE_PRIVATE).edit();
        editor.putString(activity.getString(R.string.saved_round), new Gson().toJson(round));
        editor.apply();
    }
*/
    public static void hideKeyboard(Activity activity, int buttonId) {
        InputMethodManager imm = (InputMethodManager)
                activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.findViewById(buttonId)
                .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static AutoCompleteTextView setAutoCompleteText(Fragment fragment,
                                                           int autoCompleteTextViewId,
                                                       final String field, int threshold) {
        View view = fragment.getView();
        if (view == null) return null;
        final MainActivity activity = (MainActivity) fragment.getActivity();
        final AutoCompleteTextView textView = (AutoCompleteTextView)
                view.findViewById(autoCompleteTextViewId);
        if (field == null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    activity, android.R.layout.simple_list_item_1, new String[]{});
            textView.setAdapter(adapter);
        } else {
            textView.setThreshold(threshold);
            new AsyncTask<Void, Void, String[]>() {
                @Override
                protected String[] doInBackground(Void... voids) {
                    return activity.getDB().getFieldValues(field);
                }

                @Override
                protected void onPostExecute(String[] strings) {
                    if (activity != null && strings != null) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                activity, android.R.layout.simple_list_item_1, strings);
                        textView.setAdapter(adapter);
                    }
                }

            }.execute();
        }
        return textView;
    }

}
