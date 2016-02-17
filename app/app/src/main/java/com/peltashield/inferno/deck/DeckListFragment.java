package com.peltashield.inferno.deck;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.peltashield.inferno.Configs;
import com.peltashield.inferno.MainActivity;
import com.peltashield.inferno.R;
import com.peltashield.inferno.model.deck.Deck;
import com.peltashield.inferno.searchers.SearchersFragment;
import com.peltashield.inferno.utils.Utils;

import java.util.List;

/**
 * Created by javier on 1/25/16.
 */
public class DeckListFragment extends ListFragment {

    protected DeckListAdapter adapter;

    public static DeckListFragment newInstance() {
        DeckListFragment fragment = new DeckListFragment();
        return fragment;
    }

    public DeckListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

/*        List<Deck> decks = new ArrayList<>();//Utils.getDecks(getActivity());
        Deck deck = new Deck("lista");
        deck.addCard(1874120);
        deck.addCard(1878120);
        deck.addCard(1885120);
        deck.addCard(1878120);
        deck.addCard(1894120);
        deck.addCard(1882120);

        decks.add(deck);
        decks.add(new Deck("lista2"));
        decks.add(new Deck("lista3"));
*/
        List<Deck> decks = Deck.listAll(Deck.class);
        adapter = new DeckListAdapter((MainActivity) getActivity(), decks);
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.deck_list, container, false);

        setAddDeck(view);
        setBackground(view);

        setHasOptionsMenu(true);
        Utils.setAdBannerPadding(getActivity(), (ViewGroup) view.findViewById(android.R.id.list));
        return view;
    }

    protected void setAddDeck(final View view) {
        final EditText newDeck = (EditText) view.findViewById(R.id.new_deck);
        newDeck.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addDeck(newDeck);
                    return true;
                }
                return false;
            }
        });

        view.findViewById(R.id.add_deck).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDeck(newDeck);
            }
        });
    }

    protected void addDeck(EditText newDeck) {
        adapter.add(newDeck.getText().toString());
        newDeck.setText("");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_tournament, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                SearchersFragment fragment = new SearchersFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit();
                break;
            case R.id.action_about:
                ((MainActivity) getActivity()).openAbout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void setBackground(View view) {
        if (Build.VERSION.SDK_INT > 15) {
            MainActivity activity = (MainActivity) getActivity();
            ImageView imageView = (ImageView) view.findViewById(R.id.back);
            boolean isPortait = Configs.isPortrait(getResources());
            activity.loadCard(imageView, activity.getDB().getRandomCardId(isPortait), isPortait,
                    false);
            imageView.setImageAlpha(25);
        }
    }

}
