package com.peltashield.inferno.searchers;

import com.peltashield.inferno.utils.MultiSpinner;

import java.util.Arrays;
import java.util.List;

/**
 * Created by javier on 3/9/15.
 */
public abstract class CardSearcherWithMultiSpinner extends CardSearcher {

    protected void setMultiSpinner(int multiSpinnerId, int arrayResId, String title,
                                   MultiSpinner.MultiSpinnerListener listener,
                                   boolean allPossibleValues) {
        MultiSpinner sp = (MultiSpinner) rootView.findViewById(multiSpinnerId);
        sp.setItems(Arrays.asList(getResources().getStringArray(arrayResId)), title, listener,
                allPossibleValues);
    }

    protected void setMultiSpinnerFilter(String field, int multiSpinnerId, boolean wholeSearch) {
        getDB().addOrFilters(field, getSelected(multiSpinnerId), false, wholeSearch);
    }

    protected void setMultiSpinnerFilter(String field, int multiSpinnerId, boolean parseValues,
                                         boolean wholeSearch) {
        getDB().addOrFilters(field, getSelected(multiSpinnerId), parseValues, wholeSearch);
    }

    protected List<String> getSelected(int multiSpinnerId) {
        MultiSpinner ms = (MultiSpinner) rootView.findViewById(multiSpinnerId);
        return ms.getSelectedItems();
    }

    public void clearMultiSpinners(int... ids) {
        for (int id : ids) {
            ((MultiSpinner) rootView.findViewById(id)).clearSelected();
        }
    }

}
