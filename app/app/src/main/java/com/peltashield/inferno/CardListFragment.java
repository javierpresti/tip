package com.peltashield.inferno;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.peltashield.inferno.utils.ArrayCommons;
import com.peltashield.inferno.utils.CardDescriptionAdapter;
import com.peltashield.inferno.utils.Utils;

public class CardListFragment extends ListFragment {

    public static final String EXTRA_CARDS_FILTERED = "com.peltashield.inferno.CARDS_FILTERED";

    CardDescriptionAdapter adapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        int[] ids = getArguments().getIntArray(EXTRA_CARDS_FILTERED);
        adapter = new CardDescriptionAdapter(getActivity(), ids);
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //View view = super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.card_list, container, false);
        Activity activity = getActivity();
        ((MainActivity) activity).hideActionBar();
        ListView list = (ListView) view.findViewById(android.R.id.list);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.remove(position);
                getArguments().putIntArray(EXTRA_CARDS_FILTERED, ArrayCommons.remove(
                        getArguments().getIntArray(EXTRA_CARDS_FILTERED), position));
                return true;
            }
        });
        Utils.setAdBannerPadding(activity, list);
        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        CardViewActivity.open(getArguments().getIntArray(EXTRA_CARDS_FILTERED), position,
                getActivity());
    }

}
