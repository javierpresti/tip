package com.peltashield.inferno.deck;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.peltashield.inferno.R;
import com.peltashield.inferno.model.deck.Deck;
import com.peltashield.inferno.utils.Utils;

/**
 * Created by javier on 1/25/16.
 */
public class DeckFragment extends ListFragment {

    public static final String EXTRA_DECK_ID = "com.peltashield.inferno.DECK_ID";

    protected DeckDescriptionAdapter adapter;
    protected Deck deck;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //View view = super.onCreateView(inflater, container, savedInstanceState);
        deck = Deck.findById(Deck.class, getArguments().getLong(EXTRA_DECK_ID));
        int[] ids = deck.getIds();
        int[] qtys = deck.getQuantities();
        adapter = new DeckDescriptionAdapter(getActivity(), ids, qtys);
        setListAdapter(adapter);

        View view = inflater.inflate(R.layout.card_list, container, false);
        ListView list = (ListView) view.findViewById(android.R.id.list);
        Utils.setAdBannerPadding(getActivity(), list);

        return view;
    }

    @Override
    public void onResume() {
        deck = Deck.findById(Deck.class, getArguments().getLong(EXTRA_DECK_ID));
        adapter.set(deck.getIds(), deck.getQuantities());
        super.onResume();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        CardInListActivity.open(deck.getId(), position, getActivity());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_card_view, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}
