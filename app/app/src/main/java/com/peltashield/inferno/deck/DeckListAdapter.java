package com.peltashield.inferno.deck;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.peltashield.inferno.MainActivity;
import com.peltashield.inferno.R;
import com.peltashield.inferno.deck.DeckFragment;
import com.peltashield.inferno.model.deck.Deck;
import com.peltashield.inferno.utils.Utils;

import java.util.List;

/**
 * Created by javier on 1/25/16.
 */
public class DeckListAdapter extends BaseAdapter {

    protected final MainActivity activity;
    protected final List<Deck> decks;

    public DeckListAdapter(MainActivity activity, List<Deck> decks) {
        super();
        this.activity = activity;
        this.decks = decks;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView;
        if (convertView == null) { // if it's not recycled
            LayoutInflater inflater = (LayoutInflater)
                    activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.deck_list_row, parent, false);
        } else {
            rowView = convertView;
        }

        TextView deckView = (TextView) rowView.findViewById(R.id.deck_name);
        final Deck deck = decks.get(position);
        final String deckName = deck.getName();
        StringBuilder title = new StringBuilder(deckName + " (" + deck.getSize() + ") -");
        for (String format : deck.getFormats()) {
            title.append(format).append("-");
        }
        title.deleteCharAt(title.length() - 1);
        deckView.setText(title);

        deckView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment;
                Bundle args = new Bundle();
                fragment = new DeckFragment();
                args.putLong(DeckFragment.EXTRA_DECK_ID, deck.getId());

                fragment.setArguments(args);

                activity.continueToFragment(fragment);
            }
        });

        ImageButton removeDeck = (ImageButton) rowView.findViewById(R.id.remove_deck);
        removeDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage(activity.getString(R.string.delete_deck) + deckName + "?");
                builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        decks.get(position).delete();
                        decks.remove(position);
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton(R.string.cancel, null);
                builder.create().show();
            }
        });

        return rowView;
    }

    public void add(String deckName) {
        if (deckName != null && !deckName.trim().equals("")) {
            Deck deck = new Deck(deckName);
            deck.save();
            decks.add(deck);
            notifyDataSetChanged();
            Utils.hideKeyboard(activity, R.id.add_deck);
        }
    }

    @Override
    public int getCount() {
        return decks.size();
    }

    @Override
    public Object getItem(int position) {
        return decks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
