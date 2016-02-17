package com.peltashield.inferno.tournament;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.peltashield.inferno.MainActivity;
import com.peltashield.inferno.R;
import com.peltashield.inferno.model.tournament.TournamentLite;
import com.peltashield.inferno.searchers.SearchersFragment;
import com.peltashield.inferno.utils.Utils;

/**
 * Created by javier on 7/2/15.
 */
public class TournamentRound extends ListFragment {

    public static TournamentRound newInstance(Activity activity) {
        TournamentRound instance = new TournamentRound();
        TournamentLite tournament = Utils.getTournament(activity);
        tournament.startTournament(); //TODO
        tournament.getLastRound();
        Utils.saveTournament(activity, tournament);
        return instance;
    }

    public TournamentRound() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tournament_round, container, false);

        setListAdapter(new TournamentRoundAdapter((MainActivity) getActivity()));

        TextView roundText = (TextView) view.findViewById(R.id.round);
        roundText.setText(getString(R.string.round) + ": " + 1);//TODO

        setHasOptionsMenu(true);
        Utils.setAdBannerPadding(getActivity(), (ListView) view.findViewById(android.R.id.list));
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_tournament_round, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MainActivity activity = (MainActivity) getActivity();
        switch(item.getItemId()) {
            case R.id.action_standings:
                activity.replaceFragment(TournamentStandings.newInstance());
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

    public static class TournamentRoundAdapter extends BaseAdapter {

        protected final MainActivity activity;
        protected String[][] round;

        public TournamentRoundAdapter(MainActivity activity) {
            super();
            this.activity = activity;
            this.round = Utils.getTournament(activity).getRounds()[0];
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            String[][] round = Utils.getTournament(activity).getRounds()[0];
            String[] match = round[position];

            final View rowView;
            if (convertView == null) { // if it's not recycled
                LayoutInflater inflater = (LayoutInflater)
                        activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.tournament_round_row, parent, false);
            } else {
                rowView = convertView;
            }

            final ImageButton doneButton = (ImageButton) rowView.findViewById(R.id.action_done);
            final RadioGroup playerOneGroup = (RadioGroup) rowView.findViewById(R.id.po_score);
            final RadioGroup playerTwoGroup = (RadioGroup) rowView.findViewById(R.id.pt_score);
            setEnd(round, TournamentLite.hasEnded(match), playerOneGroup, playerTwoGroup,
                    doneButton);

            TextView playerOne = (TextView) rowView.findViewById(R.id.player_one);
            TextView playerTwo = (TextView) rowView.findViewById(R.id.player_two);

            playerOne.setText(TournamentLite.getPlayer0Name(match));
            playerTwo.setText(TournamentLite.getPlayer1Name(match));

            doneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean setPoints = playerOneGroup.getChildAt(0).isEnabled();

                    TournamentLite tournament = Utils.getTournament(activity);
                    String[][] round = tournament.getRounds()[0];
                    String[] match = round[position];
                    if (setPoints) {
                        RadioButton playerOneScore = (RadioButton)
                                rowView.findViewById(playerOneGroup.getCheckedRadioButtonId());
                        RadioButton playerTwoScore = (RadioButton)
                                rowView.findViewById(playerTwoGroup.getCheckedRadioButtonId());
                        tournament.setResult(match, playerOneScore.getText().toString(),
                                playerTwoScore.getText().toString());
                    } else {
                        tournament.unsetResult(match);
                    }
                    setEnd(round, setPoints, playerOneGroup, playerTwoGroup, doneButton);

                    Utils.saveTournament(activity, tournament);
                }
            });

            return rowView;
        }

        public void setEnd(String[][] round, boolean end, RadioGroup playerOneGroup,
                           RadioGroup playerTwoGroup, ImageButton doneButton) {
            doneButton.setImageResource(end ? R.drawable.ic_cancel_black_36dp :
                    R.drawable.ic_add_circle_black_36dp);

            for (int i = 0; i < playerOneGroup.getChildCount(); i++) {
                playerOneGroup.getChildAt(i).setEnabled(!end);
                playerTwoGroup.getChildAt(i).setEnabled(!end);
            }
            activity.findViewById(R.id.end_round).setEnabled(TournamentLite.hasEnded(round));
        }

        @Override
        public int getCount() {
            return round.length;
        }

        @Override
        public Object getItem(int position) {
            return round[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

    }
}
