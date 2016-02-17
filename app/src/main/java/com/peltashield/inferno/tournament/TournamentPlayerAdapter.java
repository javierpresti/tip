package com.peltashield.inferno.tournament;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.peltashield.inferno.MainActivity;
import com.peltashield.inferno.R;
import com.peltashield.inferno.model.tournament.PlayerLite;
import com.peltashield.inferno.model.tournament.TournamentLite;
import com.peltashield.inferno.utils.Utils;

/**
 * Created by javier on 6/25/15.
 */
public class TournamentPlayerAdapter extends BaseAdapter {

    protected final MainActivity activity;
    protected TournamentLite tournament;

    public TournamentPlayerAdapter(MainActivity activity, TournamentLite tournament) {
        super();
        this.activity = activity;
        this.tournament = tournament;
    }

    public void add(String playerName) {
        if (playerName != null && !playerName.trim().equals("")) {
            if (tournament.containsPlayerName(playerName)) {
                activity.showToastText(playerName + " " +
                        activity.getString(R.string.player_present));
            } else {
                tournament.addPlayer(new PlayerLite(playerName));
                notifyDataSetChanged();
                Utils.saveTournament(activity, tournament);
            }
        }
    }

    public void removeAllPlayers() {
        tournament.clear();
        notifyDataSetChanged();
        Utils.saveTournament(activity, tournament);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView;
        if (convertView == null) { // if it's not recycled
            LayoutInflater inflater = (LayoutInflater)
                    activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.tournament_player_row, parent, false);
        } else {
            rowView = convertView;
        }

        TextView playerView = (TextView) rowView.findViewById(R.id.player_name);
        final String playerName = tournament.getPlayers().get(position).getName();
        playerView.setText(playerName);
        ImageButton removePlayer = (ImageButton) rowView.findViewById(R.id.remove_player);
        removePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage(activity.getString(R.string.delete_player) + playerName + "?");
                builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tournament.removePlayer(position);
                        notifyDataSetChanged();
                        Utils.saveTournament(activity, tournament);
                    }
                });
                builder.setNegativeButton(R.string.cancel, null);
                builder.create().show();
            }
        });

        return rowView;
    }

    @Override
    public int getCount() {
        return tournament.getPlayers().size();
    }

    @Override
    public Object getItem(int position) {
        return tournament.getPlayers().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public TournamentLite getTournament() {
        if (tournament == null) {
            return Utils.getTournament(activity);
        }
        return tournament;
    }

}
