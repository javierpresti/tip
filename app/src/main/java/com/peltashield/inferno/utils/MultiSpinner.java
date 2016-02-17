package com.peltashield.inferno.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.peltashield.inferno.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by javier on 1/23/15.
 */
public class MultiSpinner extends Spinner implements
        DialogInterface.OnMultiChoiceClickListener, DialogInterface.OnCancelListener {
    private List<String> items;
    private boolean[] selected;
    private String defaultText;
    private MultiSpinnerListener listener;

    private boolean allPossibleValues = true;

    public MultiSpinner(Context context) {
        super(context);
    }

    public MultiSpinner(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
    }

    public MultiSpinner(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
    }

    @NonNull
    @Override
    public Parcelable onSaveInstanceState() {
        SavedState ss = new SavedState(super.onSaveInstanceState());
        ss.checked = selected;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState)state;
        super.onRestoreInstanceState(ss.getSuperState());
        setSelected(ss.checked);
    }

    static class SavedState extends BaseSavedState {

        boolean[] checked;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            checked = new boolean[in.readInt()];
            in.readBooleanArray(checked);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(checked.length);
            out.writeBooleanArray(checked);
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

    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        selected[which] = isChecked;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        refreshText();
        listener.onItemsSelected(selected);
    }

    protected void refreshText() {
        StringBuilder spinnerBuilder = new StringBuilder();
        boolean someUnselected = false;
        boolean someSelected = false;
        for (int i = 0; i < items.size(); i++) {
            if (selected[i]) {
                spinnerBuilder.append(items.get(i));
                spinnerBuilder.append(", ");
                someSelected = true;
            } else {
                someUnselected = true;
            }
        }
        String spinnerText;
        if (someSelected && (someUnselected || !allPossibleValues)) {
            spinnerText = spinnerBuilder.toString();
            if (spinnerText.length() > 2) {
                spinnerText = spinnerText.substring(0, spinnerText.length() - 2);
            }
        } else {
            spinnerText = defaultText;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                R.layout.spinner,
                new String[]{spinnerText});
        setAdapter(adapter);
    }

    @Override
    public boolean performClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMultiChoiceItems(
                items.toArray(new CharSequence[items.size()]), selected, this);
        builder.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.setOnCancelListener(this);
        builder.show();
        return true;
    }

    public void setItems(List<String> items, String allText, MultiSpinnerListener listener,
                         boolean allPossibleValues) {
        this.items = items;
        this.defaultText = allText;
        this.listener = listener;

        this.allPossibleValues = allPossibleValues;

        // all not selected by default
        selected = new boolean[items.size()];
        for (int i = 0; i < selected.length; i++)
            selected[i] = false;

        // all text on the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                R.layout.spinner, new String[]{allText});
        setAdapter(adapter);
    }

    public List<String> getSelectedItems() {
        List<String> selectedItems = new ArrayList<String>();
        for (int i = 0; i < selected.length; i++) {
            if (selected[i]) {
                selectedItems.add(items.get(i));
            }
        }
        return selectedItems;
    }

    public boolean[] getSelected() {
        return selected;
    }

    public void setSelected(boolean[] selected) {
        this.selected = selected;
        refreshText();
    }

    public void clearSelected() {
        setSelected(new boolean[selected.length]);
    }

    public interface MultiSpinnerListener {
        public void onItemsSelected(boolean[] selected);
    }

}
