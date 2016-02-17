package com.peltashield.inferno.tournament;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.peltashield.inferno.MainActivity;
import com.peltashield.inferno.R;
import com.peltashield.inferno.searchers.SearchersFragment;
import com.peltashield.inferno.utils.Utils;

/**
 * Created by javier on 6/27/15.
 */
public class TournamentStandings extends ListFragment {

    public static TournamentStandings newInstance() {
        TournamentStandings instance = new TournamentStandings();
        return instance;
    }

    public TournamentStandings() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tournament_standings, container, false);
        setListAdapter(new TournamentStandingsAdapter((MainActivity) getActivity()));
        setHasOptionsMenu(true);
        Utils.setAdBannerPadding(getActivity(), (ListView) view.findViewById(android.R.id.list));
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_tournament_standings, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MainActivity activity = (MainActivity) getActivity();
        switch(item.getItemId()) {
            case R.id.action_round:
                activity.replaceFragment(TournamentRound.newInstance(activity));
                break;
            case R.id.action_search:
                activity.replaceFragment(new SearchersFragment());
                break;
            case R.id.action_about:
                activity.openAbout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
