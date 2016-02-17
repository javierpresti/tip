package com.peltashield.inferno.searchers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.peltashield.inferno.MainActivity;
import com.peltashield.inferno.R;
import com.peltashield.inferno.cardsDB.CardField;
import com.peltashield.inferno.cardsDB.CardsDB;
import com.peltashield.inferno.utils.MultiSpinner;
import com.peltashield.inferno.utils.Utils;

/**
 * Created by javier on 3/9/15.
 */
public class ExtendedSearcherFragment extends CardSearcherWithMultiSpinner
        implements MultiSpinner.MultiSpinnerListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = onCreateView(inflater, container, savedInstanceState,
                R.layout.searcher_extended);
        AutoCompleteTextView filter = setAutoCompleteText(R.id.name_filter, CardField.NAME_RAW, 2);
        generateSpinner(R.id.cost_comp, R.array.cost_comp);
        generateSpinner(R.id.str_comp, R.array.str_comp);
        generateSpinner(R.id.res_comp, R.array.res_comp);
        generateSpinner(R.id.cost_filter, R.array.cost_values);
        generateSpinner(R.id.str_filter, R.array.str_values);
        generateSpinner(R.id.res_filter, R.array.res_values);
        generateSpinner(R.id.fields_order, R.array.fields_order);
        generateSpinner(R.id.order_direction, R.array.order_direction);

        setViewAs(view);

        filter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                search();
            }
        });

        setMultiSpinners();

        EditText textFilter = (EditText) view.findViewById(R.id.text_filter);
        textFilter.setMaxLines(3);
        textFilter.setHorizontallyScrolling(false);

        Utils.setAdBannerPadding(getActivity(), (ScrollView) view.findViewById(R.id.scrollV));

        Utils.setCheckBoxLeftPadding(getResources(), view.findViewById(R.id.tokens_filter));
        Utils.setCheckBoxLeftPadding(getResources(), view.findViewById(R.id.x_values_filter));

        return view;
    }

    @Override
    public void clearFilters() {
        clearSpinners(R.id.cost_comp, R.id.str_comp, R.id.res_comp, R.id.fields_order,
                R.id.order_direction, R.id.cost_filter, R.id.str_filter, R.id.res_filter,
                R.id.view_as);
        clearMultiSpinners(R.id.formats, R.id.editions, R.id.rarities, R.id.abilities,
                R.id.basic_types, R.id.supertypes, R.id.subtypes);

        clearEditText(R.id.name_filter);
        clearEditText(R.id.text_filter);
        setCheckBox(R.id.tokens_filter, false);
        setCheckBox(R.id.x_values_filter, true);

        clearPathFilters();
    }

    protected void setMultiSpinners() {
        setMultiSpinner(R.id.formats, R.array.formats, "Formato", this, true);
        setMultiSpinner(R.id.editions, R.array.editions, "Edición", this, true);
        setMultiSpinner(R.id.rarities, R.array.rarities, "Rareza", this, true);
        setMultiSpinner(R.id.abilities, R.array.abilities, "Habilidad", this, false);
        setMultiSpinner(R.id.basic_types, R.array.basic_types, "Tipo", this, true);
        setMultiSpinner(R.id.supertypes, R.array.supertypes, "Supertipo", this, false);
        setMultiSpinner(R.id.subtypes, R.array.subtypes, "Subtipo", this, false);
    }

    @Override
    protected void setFilters() {
        setTextFilter(R.id.name_filter, CardField.NAME_RAW, true, false);
        setTextFilter(R.id.text_filter, CardField.TEXT_RAW, true, false);

        setPathFilters();

        setMultiSpinnerFilter(CardField.FORMAT, R.id.formats, false);
        setMultiSpinnerFilter(CardField.EDITION, R.id.editions, true);
        setMultiSpinnerFilter(CardField.RARITY, R.id.rarities, true);
        setMultiSpinnerFilter(CardField.ABILITY, R.id.abilities, false);
        setMultiSpinnerFilter(CardField.BASIC_TYPE, R.id.basic_types, false);
        setMultiSpinnerFilter(CardField.SUPERTYPE, R.id.supertypes, false);
        setMultiSpinnerFilter(CardField.SUBTYPE_RAW, R.id.subtypes, true, false);

        boolean xValues = isChecked(R.id.x_values_filter);
        String costComp = getSpinnerValue(R.id.cost_comp);
        String strComp = getSpinnerValue(R.id.str_comp);
        String resComp = getSpinnerValue(R.id.res_comp);

        String costValue = getSpinnerValue(R.id.cost_filter);
        String strValue = getSpinnerValue(R.id.str_filter);
        String resValue = getSpinnerValue(R.id.res_filter);
        if (!costValue.equals(getResources().getString(R.string.no_value))) {
            setSpinnerCompFilter(CardField.COST_RAW, costComp.substring(costComp.length() - 1),
                    costValue, xValues);
        }
        if (!strValue.equals(getResources().getString(R.string.no_value))) {
            setSpinnerCompFilter(CardField.STR_RAW, strComp.substring(strComp.length() - 1),
                    strValue, xValues);
        }
        if (!resValue.equals(getResources().getString(R.string.no_value))) {
            setSpinnerCompFilter(CardField.RES_RAW, resComp.substring(resComp.length() - 1),
                    resValue, xValues);
        }

        CardsDB db = getDB();
        if (! xValues) {
            db.addFilter(CardField.COST_RAWX, "", false, true);
            db.addFilter(CardField.STR_RAWX, "", false, true);
            db.addFilter(CardField.RES_RAWX, "", false, true);
        }
        if (! isChecked(R.id.tokens_filter)) {
            db.addFilter(CardField.BASIC_TYPE, true, "Ficha", false, false);
        }

        db.setOrder(getSpinnerValue(R.id.fields_order), getSpinnerValue(R.id.order_direction),
                getString(R.string.ascending));
    }

    @Override
    public void onItemsSelected(boolean[] selected) {
    }

}
