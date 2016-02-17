package com.peltashield.inferno.searchers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

import com.peltashield.inferno.R;
import com.peltashield.inferno.cardsDB.CardField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by javier on 3/9/15.
 */
public class BasicSearcherFragment extends CardSearcher {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = onCreateView(inflater, container, savedInstanceState,
                R.layout.searcher_basic);
        AutoCompleteTextView filter = setAutoCompleteText(R.id.name_filter, CardField.NAME_RAW, 2);
        generateSpinner(R.id.formats, R.array.formats_with_title);
        generateSpinner(R.id.types, R.array.types_with_title);

/*        final AutoCompleteTextView filter = (AutoCompleteTextView) rootView.findViewById(R.id.name_filter);
        filter.setThreshold(2);
        filter.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                resetFilters();
                updateAutoCompleteText(filter, CardField.NAME_RAW);
                return false;
            }
        });
*/
        filter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                search();
            }
        });

        return view;
    }

    public void resetFilters() {
        getDB().resetFilters();
        setPathFilters();
        setSpinnerFilter(R.id.types, CardField.BASIC_TYPE, false, true);
        setSpinnerFilter(R.id.formats, CardField.FORMAT, false, false);
        getDB().addFilter(CardField.BASIC_TYPE, true, "Ficha", false, false);
    }

    @Override
    protected void setFilters() {
        setTextFilters();
        setPathFilters();
        setSpinnerFilter(R.id.types, CardField.BASIC_TYPE, false, true);
        setSpinnerFilter(R.id.formats, CardField.FORMAT, false, false);
        getDB().addFilter(CardField.BASIC_TYPE, true, "Ficha", false, false);
    }

    @Override
    public void clearFilters() {
        clearEditText(R.id.name_filter);

        clearPathFilters();

        clearSpinners(R.id.formats, R.id.types);
    }

    protected void setTextFilters() {
        String value = getEditTextValue(R.id.name_filter);
        if (!value.equals("")) {
            List<String> fields = new ArrayList<>(2);
            List<String> values = new ArrayList<>(2);
            fields.add(CardField.NAME_RAW);
            fields.add(CardField.TEXT_RAW);
            values.add(value);
            values.add(value);
            getDB().addOrFilters(fields, values, true, false);
        }
    }
}
