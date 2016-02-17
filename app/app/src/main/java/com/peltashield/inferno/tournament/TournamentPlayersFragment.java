package com.peltashield.inferno.tournament;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.peltashield.inferno.Configs;
import com.peltashield.inferno.MainActivity;
import com.peltashield.inferno.R;
import com.peltashield.inferno.searchers.SearchersFragment;
import com.peltashield.inferno.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TournamentPlayersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TournamentPlayersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TournamentPlayersFragment extends ListFragment {

    protected TournamentPlayerAdapter adapter;

    private OnFragmentInteractionListener mListener;

    public static TournamentPlayersFragment newInstance() {
        TournamentPlayersFragment fragment = new TournamentPlayersFragment();
        return fragment;
    }

    public TournamentPlayersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new TournamentPlayerAdapter((MainActivity) getActivity(),
                Utils.getTournament(getActivity()));
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tournament_players, container, false);

        setAddPlayer(view);
        setReset(view);
        setContinue(view);
        setPlace(view);
        setBackground(view);

        setHasOptionsMenu(true);
        Utils.setAdBannerPadding(getActivity(), (ViewGroup) view.findViewById(android.R.id.list));
        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_tournament, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_search:
                SearchersFragment fragment = new SearchersFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment).commit();
                break;
            case R.id.action_about:
                ((MainActivity) getActivity()).openAbout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        View view = getView();
        if (view != null) {
            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            EditText placeText = (EditText) view.findViewById(R.id.place);
            editor.putString(getString(R.string.saved_place), placeText.getText().toString());
            editor.apply();
        }
        super.onStop();
    }

    protected void setAddPlayer(final View view) {
        final EditText newPlayer = (EditText) view.findViewById(R.id.new_player);
        newPlayer.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addPlayer(newPlayer);
                    return true;
                }
                return false;
            }
        });

        view.findViewById(R.id.add_player).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPlayer(newPlayer);
            }
        });
    }

    protected void setReset(View view) {
        Button reset = (Button) view.findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.reset_tournament);
                builder.setMessage(R.string.reset_about);
                builder.setPositiveButton(R.string.do_reset,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                adapter.removeAllPlayers();
                            }
                        });
                builder.setNegativeButton(R.string.cancel, null);
                builder.create().show();
            }
        });
    }

    protected void setContinue(View view) {
        Button cont = (Button) view.findViewById(R.id.to_continue);
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.getCount() < 4) {
                    ((MainActivity) getActivity()).showToastText(
                            getString(R.string.players_required));
                } else {
                    Activity activity = getActivity();
                    Utils.hideKeyboard(activity, R.id.to_continue);
                    ((MainActivity) activity).replaceFragment(TournamentRound.newInstance(activity));
                }
            }
        });
    }

    protected void setPlace(View view) {
        EditText placeText = (EditText) view.findViewById(R.id.place);
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        placeText.setText(sharedPref.getString(getString(R.string.saved_place), null));
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

    protected void addPlayer(EditText newPlayer) {
        adapter.add(newPlayer.getText().toString());
        newPlayer.setText("");
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
