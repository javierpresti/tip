package com.peltashield.inferno.cardsDB;

import com.peltashield.inferno.R;

/**
 * Created by javier on 5/6/15.
 */
public enum CardCode {

    PATH_CAOS("C", R.drawable.whiteb_caos), PATH_LOCURA("L", R.drawable.whiteb_locura),
    PATH_MUERTE("M", R.drawable.whiteb_muerte), PATH_PODER("P", R.drawable.whiteb_poder),
    PATH_NEUTRAL("N", R.drawable.path_neutral);

    String code;
    int drawableId;

    CardCode(String code, int drawableId) {
        this.code = code;
        this.drawableId = drawableId;
    }

    public String getCode() {
        return code;
    }

    public int getDrawableId() {
        return drawableId;
    }

}
