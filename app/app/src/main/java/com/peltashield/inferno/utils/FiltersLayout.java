package com.peltashield.inferno.utils;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.peltashield.inferno.R;
import com.peltashield.inferno.searchers.AdvancedSearcherFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by javier on 5/3/15.
 */
public class FiltersLayout extends LinearLayout {

    protected List<AdvancedSearcherFragment.Filter> filters;

    public FiltersLayout(Context context) {
        super(context);
        filters = new ArrayList<>();
        setId(R.id.filters);
        setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(params);
    }

    public List<AdvancedSearcherFragment.Filter> getFilters() {
        return filters;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        SavedState ss = new SavedState(super.onSaveInstanceState());
        ss.filters = filters;
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        filters = ss.filters;
    }

    static class SavedState extends View.BaseSavedState {

        List<AdvancedSearcherFragment.Filter> filters;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            filters = new ArrayList<>();
            in.readList(filters, null);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeList(filters);
        }

        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }

}
