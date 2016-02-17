package com.peltashield.inferno;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.peltashield.inferno.utils.ArrayCommons;
import com.peltashield.inferno.utils.ImageAdapter;
import com.peltashield.inferno.utils.Utils;

public class CardPoolFragment extends Fragment {

    public static final String EXTRA_CARDS_FILTERED = "com.peltashield.inferno.CARDS_FILTERED";

    public CardPoolFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.card_pool, container, false);
        createImageAdapter(view);

        Activity activity = getActivity();
        ((MainActivity) activity).hideActionBar();

        Utils.setAdBannerPadding(activity, (GridView) view.findViewById(R.id.gridview));

        return view;
    }

    protected void createImageAdapter(View view) {
        GridView gridview = (GridView) view.findViewById(R.id.gridview);
        int[] filteredCards = getArguments().getIntArray(EXTRA_CARDS_FILTERED);
        final ImageAdapter adapter = new ImageAdapter(getActivity(), filteredCards);
        gridview.setAdapter(adapter);

        // getIntArray needs to be called on each anonymous class.
        // Therefore, it can't be used a final variable that stores the int[].
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CardViewActivity.open(getArguments().getIntArray(EXTRA_CARDS_FILTERED),
                        position, getActivity());
            }
        });
        gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.remove(position);
                getArguments().putIntArray(EXTRA_CARDS_FILTERED, ArrayCommons.remove(
                        getArguments().getIntArray(EXTRA_CARDS_FILTERED), position));
                return true;
            }
        });
    }

}
