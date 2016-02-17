package com.peltashield.inferno.searchers;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.peltashield.inferno.BuildConfig;
import com.peltashield.inferno.Configs;
import com.peltashield.inferno.R;
import com.peltashield.inferno.MainActivity;
import com.peltashield.inferno.deck.DeckListFragment;
import com.peltashield.inferno.tournament.TournamentPlayersFragment;

public class SearchersFragment extends Fragment {

    protected final static int QTY_VIEWS = 1;
    protected CardSearcherPagerAdapter pageAdapter;

    public SearchersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.searchers, container, false);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);
        pageAdapter = new CardSearcherPagerAdapter(getChildFragmentManager(),
                CardSearcherPagerAdapter.BASIC_SEARCHER);
        viewPager.setAdapter(pageAdapter);
//        viewPager.setOffscreenPageLimit(QTY_VIEWS - 1);

        MainActivity activity = (MainActivity) getActivity();
        boolean isPortait = Configs.isPortrait(getResources());
        activity.loadCard((ImageView) view.findViewById(R.id.back),
                activity.getDB().getRandomCardId(isPortait), isPortait, false);

        setHasOptionsMenu(true);
        activity.getSupportActionBar().show();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(
                BuildConfig.DEBUG ? R.menu.debug_menu_searchers : R.menu.menu_searchers, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager)
                    getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
        String searcher = null;
        boolean searcherSelected = true;
        switch(item.getItemId()) {
            case R.id.action_basic_search:
                searcher = CardSearcherPagerAdapter.BASIC_SEARCHER;
                break;
            case R.id.action_extended_search:
                searcher = CardSearcherPagerAdapter.EXTENDED_SEARCHER;
                break;
            case R.id.action_advanced_search:
                searcher = CardSearcherPagerAdapter.ADVANCED_SEARCHER;
                break;
            case R.id.action_lists:
                searcherSelected = false;
                openListManager();
                break;
            case R.id.action_tournament:
                searcherSelected = false;
                openTournamentManager();
                break;
            case R.id.action_about:
                searcherSelected = false;
                ((MainActivity) getActivity()).openAbout();
                break;
        }
        if (searcherSelected) {
            ViewPager viewPager = (ViewPager) getView().findViewById(R.id.pager);
            pageAdapter = new CardSearcherPagerAdapter(getChildFragmentManager(), searcher);
            viewPager.setAdapter(pageAdapter);
        }
        return super.onOptionsItemSelected(item);
    }

    protected void openListManager() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(
                R.id.fragment_container, DeckListFragment.newInstance())
                .addToBackStack(String.valueOf(System.currentTimeMillis())).commit();
    }

    protected void openTournamentManager() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(
                R.id.fragment_container, TournamentPlayersFragment.newInstance()).commit();
    }

    public static class CardSearcherPagerAdapter extends FragmentStatePagerAdapter {

        public static final String BASIC_SEARCHER = "basic";
        public static final String EXTENDED_SEARCHER = "extended";
        public static final String ADVANCED_SEARCHER = "advanced";

        protected String searcher;

        public CardSearcherPagerAdapter(FragmentManager fm, String searcher) {
            super(fm);
            this.searcher = searcher;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch(searcher) {
                case EXTENDED_SEARCHER: return new ExtendedSearcherFragment();
                case ADVANCED_SEARCHER: return new AdvancedSearcherFragment();
                default: return new BasicSearcherFragment();
            }
        }

        @Override
        public int getCount() {
            return QTY_VIEWS;
        }

    }

}
