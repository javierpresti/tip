package com.peltashield.inferno.searchers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.peltashield.inferno.CardListFragment;
import com.peltashield.inferno.CardPoolFragment;
import com.peltashield.inferno.CardViewActivity;
import com.peltashield.inferno.Configs;
import com.peltashield.inferno.MainActivity;
import com.peltashield.inferno.R;
import com.peltashield.inferno.cardsDB.CardField;
import com.peltashield.inferno.cardsDB.CardsDB;
import com.peltashield.inferno.utils.AdManager;
import com.peltashield.inferno.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by javier on 3/2/15.
 */
public abstract class CardSearcher extends Fragment {

    protected View rootView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState, int layout) {
        rootView = inflater.inflate(layout, container, false);

        rootView.findViewById(R.id.button_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    search();
            }
        });

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        AutoCompleteTextView nameFilter = (AutoCompleteTextView)
                rootView.findViewById(R.id.name_filter);
        if (nameFilter != null) {
            nameFilter.clearFocus();
        }
    }

    protected AutoCompleteTextView setAutoCompleteText(int autoCompleteTextViewId,
                                                       final String field, int threshold) {
        final AutoCompleteTextView textView = (AutoCompleteTextView)
                rootView.findViewById(autoCompleteTextViewId);
        if (field == null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    getActivity(), android.R.layout.simple_list_item_1, new String[]{});
            textView.setAdapter(adapter);
        } else {
            textView.setThreshold(threshold);
            new AsyncTask<Void, Void, String[]>() {
                @Override
                protected String[] doInBackground(Void... voids) {
                    return getDB().getFieldValues(field);
                }

                @Override
                protected void onPostExecute(String[] strings) {
                    FragmentActivity activity = getActivity();
                    if (activity != null && strings != null) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                activity, android.R.layout.simple_list_item_1, strings);
                        textView.setAdapter(adapter);
                    }
                }

            }.execute();
        }
        return textView;
    }

    protected AutoCompleteTextView updateAutoCompleteText(final AutoCompleteTextView textView,
                                                          final String field) {
        new AsyncTask<Void, Void, String[]>() {
            @Override
            protected String[] doInBackground(Void... voids) {
                return getDB().getFieldValuesWithFilters(field);
            }

            @Override
            protected void onPostExecute(String[] strings) {
                FragmentActivity activity = getActivity();
                if (activity != null && strings != null) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            activity, android.R.layout.simple_list_item_1, strings);
                    textView.setAdapter(adapter);
                }
            }

        }.execute();
        return textView;
    }

    protected Spinner generateSpinner(int spinnerID, int choicesID) {
        Spinner spinner = (Spinner) rootView.findViewById(spinnerID);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity(), choicesID, R.layout.spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        return spinner;
    }

    protected abstract void setFilters();
    public abstract void clearFilters();

    public void search() {
        setFilters();
        int[] cards = getDB().getCardImageIds();
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            String toastText = null;
            if (cards.length > 0) {
                Utils.hideKeyboard(activity, R.id.button_search);
                Fragment fragment;
                Bundle args = new Bundle();
                boolean canDisplayImage = activity.canDisplayImage();
                if (cards.length > 1) {
                    Spinner viewAs = (Spinner) rootView.findViewById(R.id.view_as);
                    if (viewAs != null && canDisplayImage &&
                            viewAs.getSelectedItem().equals(getString(R.string.grid))) {
                        fragment = new CardPoolFragment();
                        args.putIntArray(CardPoolFragment.EXTRA_CARDS_FILTERED, cards);
                    } else {
                        fragment = new CardListFragment();
                        args.putIntArray(CardListFragment.EXTRA_CARDS_FILTERED, cards);
                    }
                    toastText = cards.length + getString(R.string.card_found);
                } else {
//                    fragment = new CardViewFragment();
//                    args.putIntArray(CardViewFragment.EXTRA_FILTERED_CARDS, cards);
                    CardViewActivity.open(cards, 0, getActivity());
                    return;
                }
                activity.showToastText(toastText);
                fragment.setArguments(args);
                FragmentTransaction transaction =
                        activity.getSupportFragmentManager().beginTransaction();
                if (Build.VERSION.SDK_INT > 15 && canDisplayImage) {
                    transaction.setCustomAnimations(R.anim.frag_slide_in, R.anim.frag_slide_out);
                }
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(String.valueOf(System.currentTimeMillis()));
                transaction.commit();
            } else {
                activity.showToastText(getResources().getString(R.string.no_card_found));
            }
        }
    }

    protected void setSpinnerFilter(int filter, String type,
                                    boolean parseValues, boolean wholeSearch) {
        Spinner sp = (Spinner) rootView.findViewById(filter);
        String value = (String) sp.getSelectedItem();
        if (!value.equals((String) sp.getItemAtPosition(0))) {
            getDB().addFilter(type, value, parseValues, wholeSearch);
        }
    }

    protected void setSpinnerCompFilter(String field, int compId, int filter, boolean xValues) {
        String value = getSpinnerValue(compId);
        setTextFilter(filter, field, value.substring(value.length() - 1), xValues, false, true);
    }

    protected void setSpinnerCompFilter(String field, String comp, String filter, boolean xValues) {
        setTextFilter(filter, field, comp, xValues, false, true);
    }

    protected void setTextFilter(int filter, String field,
                                 boolean parseValues, boolean wholeSearch) {
        String value = getEditTextValue(filter);
        if (!value.equals("")) {
            getDB().addFilter(field, value, parseValues, wholeSearch);
        }
    }

    protected void setTextFilter(int filter, String field, String comparator, boolean xValues,
                                 boolean parseValues, boolean wholeSearch) {
        String value = getEditTextValue(filter);
        setTextFilter(value, field, comparator, xValues, parseValues, wholeSearch);
    }

    protected void setTextFilter(String filter, String field, String comparator, boolean xValues,
                                 boolean parseValues, boolean wholeSearch) {
        if (!filter.equals("")) {
            getDB().addFilter(field, comparator, filter, xValues, parseValues, wholeSearch);
        }
    }

    protected boolean isChecked(int checkBoxId) {
        return ((CheckBox) rootView.findViewById(checkBoxId)).isChecked();
    }

    protected String getEditTextValue(int editTextId) {
        return ((EditText) rootView.findViewById(editTextId)).getText().toString();
    }

    protected String getSpinnerValue(int spinnerId) {
        Spinner sp = (Spinner) rootView.findViewById(spinnerId);
        return (String) sp.getSelectedItem();
    }

    protected void setPathFilters() {
        List<String> values = new ArrayList<>();
        if (this.isChecked(R.id.caos_filter)) {
            values.add(getString(R.string.caos));
        }
        if (this.isChecked(R.id.locura_filter)) {
            values.add(getString(R.string.locura));
        }
        if (this.isChecked(R.id.muerte_filter)) {
            values.add(getString(R.string.muerte));
        }
        if (this.isChecked(R.id.poder_filter)) {
            values.add(getString(R.string.poder));
        }
        if (this.isChecked(R.id.neutral_filter)) {
            values.add(getString(R.string.neutral));
        }
        getDB().addOrFilters(CardField.PATH, values, false, true);
    }

    protected void clearEditText(int id) {
        View view = getView();
        if (view != null) {
            ((EditText) view.findViewById(id)).setText(null);
        }
    }

    protected void setCheckBox(int id, boolean checked) {
        View view = getView();
        if (view != null) {
            ((CheckBox) view.findViewById(id)).setChecked(checked);
        }
    }

    protected void clearPathFilters() {
        setCheckBox(R.id.caos_filter, false);
        setCheckBox(R.id.locura_filter, false);
        setCheckBox(R.id.muerte_filter, false);
        setCheckBox(R.id.poder_filter, false);
        setCheckBox(R.id.neutral_filter, false);
    }

    protected void clearSpinners(int... ids) {
        View view = getView();
        if (view != null) {
            for (int id : ids) {
                ((Spinner) view.findViewById(id)).setSelection(0);
            }
        }
    }

    protected void setViewAs(View view) {
        if (((MainActivity) getActivity()).canDisplayImage()) {
            Spinner viewAs = generateSpinner(R.id.view_as, R.array.view_as);
            final SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
            final String viewPrefKey = getString(R.string.saved_view_as);
            viewAs.setSelection(preferences.getInt(viewPrefKey, 0));
            viewAs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    preferences.edit().putInt(viewPrefKey, position).apply();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
        } else {
            view.findViewById(R.id.view_as).setVisibility(View.GONE);
            view.findViewById(R.id.view_as_text).setVisibility(View.GONE);
        }
    }

    protected CardsDB getDB() {
        return ((MainActivity) getActivity()).getDB();
    }

}