package com.peltashield.inferno.searchers;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.peltashield.inferno.MainActivity;
import com.peltashield.inferno.R;
import com.peltashield.inferno.cardsDB.CardField;
import com.peltashield.inferno.cardsDB.CardsDB;
import com.peltashield.inferno.utils.FiltersLayout;
import com.peltashield.inferno.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by javier on 3/11/15.
 */
public class AdvancedSearcherFragment extends CardSearcher {

    protected FiltersLayout fl;
    protected static int partnerId = R.array.formats;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = onCreateView(inflater, container, savedInstanceState,
                R.layout.searcher_advanced);
        final Spinner fields = generateSpinner(R.id.fields, R.array.fields);
        generateSpinner(R.id.logicals, R.array.logicals);
        generateSpinner(R.id.fields_order, R.array.fields_order);
        generateSpinner(R.id.order_direction, R.array.order_direction);
        generateSpinner(R.id.logicals_partners, partnerId);

        setViewAs(view);

        view.findViewById(R.id.button_add_filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFilter();
            }
        });

        final ScrollView sv = (ScrollView) view.findViewById(R.id.scrollV);
        fl = new FiltersLayout(getActivity());
        sv.addView(fl);

        fields.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setViews((String) fields.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final Spinner partners = (Spinner) rootView.findViewById(R.id.logicals_partners);
        partners.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CheckBox xFilter = (CheckBox) rootView.findViewById(R.id.x_values_filter);
                switch ((String) partners.getSelectedItem()) {
                    case "=":
                        xFilter.setVisibility(View.GONE);
                        break;
                    case ">":
                        xFilter.setVisibility(View.VISIBLE);
                        break;
                    case "<":
                        xFilter.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Utils.setAdBannerPadding(getActivity(), sv);
        Utils.setCheckBoxLeftPadding(getResources(), view.findViewById(R.id.x_values_filter));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.filters);
        if (layout.getChildCount() == 0) {
            createFiltersTextFromState();
        }
    }

    @Override
    public void clearFilters() {
        clearPathFilters();
        clearEditText(R.id.text_filter);
        setCheckBox(R.id.x_values_filter, true);
        clearSpinners(R.id.fields, R.id.logicals, R.id.logicals_partners, R.id.fields_order,
                R.id.order_direction, R.id.num_filter, R.id.view_as);

        fl.getFilters().clear();
        LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.filters);
        layout.removeAllViews();
    }

    protected void setViews(String selected) {
        EditText editText = (EditText) rootView.findViewById(R.id.text_filter);
        Spinner numFilter = (Spinner) rootView.findViewById(R.id.num_filter);
        CheckBox xFilter = (CheckBox) rootView.findViewById(R.id.x_values_filter);
        int editTextVisibility = View.GONE;
        int numFilterVisibility = View.GONE;
        int optionsId = R.array.logicals_text;
        String autoCompleteField = null;
        int autoCompleteThreshold = 1;
        switch (selected) {
            case CardField.ES_TEXTO:
                editTextVisibility = View.VISIBLE;
                editText.setHint(getResources().getString(R.string.text));
                break;
            case CardField.ES_LEYENDA:
                editTextVisibility = View.VISIBLE;
                editText.setHint(getResources().getString(R.string.legend));
                break;
            case CardField.ES_NOMBRE:
                editTextVisibility = View.VISIBLE;
                autoCompleteField = CardField.NAME_RAW;
                autoCompleteThreshold = 2;
                editText.setHint(getResources().getString(R.string.name));
                break;
            case CardField.ES_ARTISTA:
                editTextVisibility = View.VISIBLE;
                autoCompleteField = CardField.ARTIST_RAW;
                autoCompleteThreshold = 2;
                editText.setHint(getResources().getString(R.string.artist));
                break;
            case CardField.ES_SUBTIPO:
                optionsId = R.array.subtypes;
                break;
            case CardField.ES_HABILIDAD:
                optionsId = R.array.abilities;
                break;
            case CardField.ES_FORMATO:
                optionsId = R.array.formats;
                break;
            case CardField.ES_EDICION:
                optionsId = R.array.editions;
                break;
            case CardField.ES_TIPO_BASICO:
                optionsId = R.array.basic_types_with_token;
                break;
            case CardField.ES_SUPERTIPO:
                optionsId = R.array.supertypes;
                break;
            case CardField.ES_RAREZA:
                optionsId = R.array.rarities;
                break;
            case CardField.ES_COSTE:
                generateSpinner(R.id.num_filter, R.array.cost_values);
                break;
            case CardField.ES_FUE:
                generateSpinner(R.id.num_filter, R.array.str_values);
                break;
            case CardField.ES_RES:
                generateSpinner(R.id.num_filter, R.array.res_values);
                break;
        }
        if (selected.equals(CardField.ES_COSTE) || selected.equals(CardField.ES_FUE) ||
                selected.equals(CardField.ES_RES)) {
            optionsId = R.array.comparators;
            numFilterVisibility = View.VISIBLE;
        } else {
            xFilter.setVisibility(View.GONE);
        }
        editText.setVisibility(editTextVisibility);
        numFilter.setVisibility(numFilterVisibility);
        if (editTextVisibility == View.VISIBLE) {
            setAutoCompleteText(R.id.text_filter, autoCompleteField, autoCompleteThreshold);
        }
        if (partnerId != optionsId) {
            partnerId = optionsId;
            generateSpinner(R.id.logicals_partners, optionsId);
        }
    }

    protected void createFiltersTextFromState() {
        for (Filter f : fl.getFilters()) {
            addFilter(f);
        }
    }

    public void addFilter() {
        EditText filterText = (EditText) rootView.findViewById(R.id.text_filter);
        Spinner filterNum = (Spinner) rootView.findViewById(R.id.num_filter);
        String logical = getSpinnerValue(R.id.logicals);
        String field = getSpinnerValue(R.id.fields);
        String partner = getSpinnerValue(R.id.logicals_partners);
        String num = (String) filterNum.getSelectedItem();

        if (filterNum.getVisibility() != View.VISIBLE ||
                !num.equals(getResources().getString(R.string.no_value))) {
            Filter f;
            if (filterText.getVisibility() == View.VISIBLE) {
                f = new EditFilter(logical, field, partner, filterText.getText().toString());
            } else if (filterNum.getVisibility() == View.VISIBLE) {
                f = new NumFilter(logical, field, partner, num, isChecked(R.id.x_values_filter));
            } else {
                f = new Filter(logical, field, partner);
            }
            fl.getFilters().add(f);
            addFilter(f);

            filterText.setText("");
        }
    }

    protected void addFilter(final Filter filter) {
        final LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.filters);
        final LinearLayout filterLayout = new LinearLayout(getActivity());
        filterLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.addView(filterLayout, 0);
        layout.setTag(filter);

        TextView textV = new TextView(getActivity());
        textV.setText(filter.getLine());
        LinearLayout.LayoutParams tParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0);
        tParams.setMargins(0, 10, 4, 0);
        textV.setLayoutParams(tParams);
        textV.setGravity(Gravity.CENTER_VERTICAL);
        textV.setTextColor(Color.BLACK);
        textV.setTypeface(null, Typeface.BOLD_ITALIC);
        textV.setShadowLayer(5, 0, 0, Color.WHITE);
        float size = getResources().getDimension(R.dimen.text_size_normal);
        textV.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        textV.setBackgroundResource(R.drawable.white_alpha_background);
        textV.setPadding(8, 0, 8, 0);

        ImageButton remover = new ImageButton(getActivity());
        remover.setImageResource(R.drawable.ic_cancel_white_36dp_alpha_40percent);
        remover.setBackgroundColor(Color.TRANSPARENT);
        remover.setPadding(0, 0, 0, 0);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(8, 4, 8, 4);
        remover.setLayoutParams(params);
        remover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout.removeView(filterLayout);
                fl.getFilters().remove(filter);
            }
        });

        filterLayout.addView(remover);
        filterLayout.addView(textV);
    }

    @Override
    protected void setFilters() {
        setPathFilters();
        CardsDB db = getDB();
        ArrayList<Filter> orFilters = new ArrayList<>();
        ArrayList<Filter> andFilters = new ArrayList<>();
        for (Filter filter : fl.getFilters()) {
            if (filter.isOr()) {
                orFilters.add(filter);
            } else {
                andFilters.add(filter);
            }
        }
        for (Filter f : andFilters) {
            f.addToDB(db);
        }
        while (!orFilters.isEmpty()) {
            orFilters.get(0).addToDB(db, orFilters);
        }

        db.setOrder(getSpinnerValue(R.id.fields_order), getSpinnerValue(R.id.order_direction),
                getString(R.string.ascending));
    }

    public static class Filter implements Serializable {

        protected String logical;
        protected String field;
        protected String value;
        protected String init = "%";
        protected String last = "%";

        public Filter(String logical, String field, String value) {
            this.logical = logical;
            this.field = field;
            this.value = value;
        }

        public void addToDB(CardsDB db) {
            db.addFilter(CardField.getDBField(field), isNot(), value, shouldParse(), init, last);
        }

        public void addToDB(CardsDB db, List<Filter> filters) {
            List<String> fields = new ArrayList<>();
            List<String> comparators = new ArrayList<>();
            List<String> values = new ArrayList<>();
            List<Boolean> parseValues = new ArrayList<>();
            List<String> inits = new ArrayList<>();
            List<String> lasts = new ArrayList<>();

            for (int i = 0; i < filters.size(); i++) {
                Filter filter = filters.get(i);
                if (filter.field.equals(this.field)) {
                    filters.remove(i);

                    fields.add(CardField.getDBField(filter.field));
                    comparators.add(filter.getComparator());
                    values.add(filter.value);
                    parseValues.add(filter.shouldParse());
                    inits.add(filter.init);
                    lasts.add(filter.last);

                    i--;
                }
            }
            db.addOrFilters(fields, comparators, values, parseValues, inits, lasts);
        }

        public String getComparator() {
            return (isNot() ? "not " : "") + "like";
        }

        public String getLine() {
            return field + " " + logical + " " + " \"" + value + "\"";
        }

        public boolean isOr() {
            return logical.equals("o");
        }

        public boolean isNot() {
            return logical.equals("no");
        }

        public boolean shouldParse() {
            return field.equals(CardField.ES_SUBTIPO);
        }

    }

    public static class NumFilter extends Filter {

        protected String comparator;
        protected boolean xAllowed;

        public NumFilter(String logical, String field, String comparator, String value,
                         boolean xAllowed) {
            super(logical, field, value);
            this.comparator = comparator;
            this.xAllowed = xAllowed;
            init = "";
            last = "";
        }

        @Override
        public void addToDB(CardsDB db) {
            db.addFilter(CardField.getDBField(field), getComparator(), value, xAllowed,
                    shouldParse(), init, last);
        }

        @Override
        public String getComparator() {
            String comp = comparator;
            if (isNot()) {
                switch (comparator) {
                    case "=":
                        comp = "!=";
                        break;
                    case ">":
                        comp = "<=";
                        break;
                    case "<":
                        comp = ">=";
                        break;
                }
            }
            return comp;
        }

        public void addToDB(CardsDB db, List<Filter> filters) {
            List<String> fields = new ArrayList<>();
            List<String> comparators = new ArrayList<>();
            List<String> values = new ArrayList<>();
            List<Boolean> parseValues = new ArrayList<>();
            List<String> inits = new ArrayList<>();
            List<String> lasts = new ArrayList<>();
            List<Boolean> xAllowed = new ArrayList<>();

            for (int i = 0; i < filters.size(); i++) {
                Filter filter = filters.get(i);
                if (filter.field.equals(CardField.ES_FUE) || filter.field.equals(CardField.ES_RES)
                        || filter.field.equals(CardField.ES_COSTE)) {
                    filters.remove(i);

                    fields.add(CardField.getDBField(filter.field));
                    comparators.add(filter.getComparator());
                    values.add(filter.value);
                    parseValues.add(filter.shouldParse());
                    inits.add(filter.init);
                    lasts.add(filter.last);
                    xAllowed.add(((NumFilter) filter).xAllowed);

                    i--;
                }
            }
            db.addOrFilters(fields, comparators, values, xAllowed, parseValues, inits, lasts);
        }

        protected String getXText() {
            return comparator.equals("=") ? "" : (xAllowed ? " con " : " sin ") + "valores X";
        }

        @Override
        public String getLine() {
            return field + " " + logical + " " + comparator + " " + value + getXText();
        }

    }

    public static class EditFilter extends Filter {

        protected String partner;

        public EditFilter(String logical, String field, String partner, String value) {
            super(logical, field, value);
            this.partner = partner;
            setInitAndLast(partner);
        }

        protected void setInitAndLast(String partner) {
            if (partner.equals("es") || partner.equals("empieza con")) {
                init = "";
                if (partner.equals("es")) {
                    last = "";
                }
            }
        }

        @Override
        public String getLine() {
            return field + " " + logical + " " + partner + " \"" + value + "\"";
        }

        @Override
        public boolean shouldParse() {
            return true;
        }
    }

}
