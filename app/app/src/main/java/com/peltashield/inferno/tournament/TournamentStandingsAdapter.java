package com.peltashield.inferno.tournament;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.peltashield.inferno.MainActivity;
import com.peltashield.inferno.R;
import com.peltashield.inferno.model.tournament.PlayerLite;
import com.peltashield.inferno.utils.Utils;

import java.util.List;

/**
 * Created by javier on 7/2/15.
 */
public class TournamentStandingsAdapter extends BaseAdapter {

    protected final MainActivity activity;
    protected List<PlayerLite> players;

    public TournamentStandingsAdapter(MainActivity activity) {
        this.activity = activity;
        this.players = Utils.getTournament(activity).getSortedPlayers();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView;
        if (convertView == null) { // if it's not recycled
            LayoutInflater inflater = (LayoutInflater)
                    activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.tournament_standings_row, parent, false);
        } else {
            rowView = convertView;
        }

        TextView pos = (TextView) rowView.findViewById(R.id.player_pos);
        TextView name = (TextView) rowView.findViewById(R.id.player_name);
        TextView score = (TextView) rowView.findViewById(R.id.player_score);
        TextView won = (TextView) rowView.findViewById(R.id.player_won);
        TextView drew = (TextView) rowView.findViewById(R.id.player_drew);
        TextView lost = (TextView) rowView.findViewById(R.id.player_lost);
        TextView wins = (TextView) rowView.findViewById(R.id.player_wins);
        TextView looses = (TextView) rowView.findViewById(R.id.player_looses);

        pos.setText(String.valueOf(position + 1));
        //List<Player> players = Utils.getTournamentPlayers(activity);
        name.setText(players.get(position).getName());
        score.setText(String.valueOf(players.get(position).getScore()));
        won.setText(String.valueOf(players.get(position).getWon()));
        drew.setText(String.valueOf(players.get(position).getDrew()));
        lost.setText(String.valueOf(players.get(position).getLost()));
        wins.setText(String.valueOf(players.get(position).getWins()));
        looses.setText(String.valueOf(players.get(position).getLooses()));

        return rowView;
    }

    @Override
    public int getCount() {
        return players.size();
    }

    @Override
    public Object getItem(int position) {
        return players.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
