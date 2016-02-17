package com.peltashield.inferno;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.peltashield.inferno.utils.CardViewAdapter;

public class CardViewFragment extends Fragment {

    public static final String EXTRA_FILTERED_CARDS = "com.peltashield.inferno.FILTERED_CARDS";
    public static final String EXTRA_POS_SELECTED = "com.peltashield.inferno.POS_SELECTED";

    public CardViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.card_view, container, false);

        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.pager);
        CardViewAdapter pageAdapter = new CardViewAdapter(
                getActivity().getSupportFragmentManager(),
                getArguments().getIntArray(EXTRA_FILTERED_CARDS));
        mViewPager.setAdapter(pageAdapter);
        final int extraCurrentItem = getArguments().getInt(EXTRA_POS_SELECTED, -1);
        if (extraCurrentItem != -1) {
            mViewPager.setCurrentItem(extraCurrentItem);
        }
        mViewPager.setOffscreenPageLimit(2);

        ((ImageActivity) getActivity()).hideActionBar();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //TODO Beware that changing orientation in this fragment makes app lag according to the
        // times this fragment has been opened since the activity was created.
        if (Configs.isPortrait(getResources())) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        }
    }

    @Override
    public void onPause() {
        if (Configs.isPortrait(getResources())) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        }
        super.onPause();
    }

    public static void open(int[] filteredCards, int position, FragmentActivity activity) {
        CardViewFragment fragment = new CardViewFragment();
        Bundle args = new Bundle();
        args.putIntArray(CardViewFragment.EXTRA_FILTERED_CARDS, filteredCards);
        args.putInt(CardViewFragment.EXTRA_POS_SELECTED, position);
        fragment.setArguments(args);

        FragmentTransaction transaction = activity.getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
