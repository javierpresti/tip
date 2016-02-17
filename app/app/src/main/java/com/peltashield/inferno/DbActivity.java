package com.peltashield.inferno;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.peltashield.inferno.cardsDB.CardsDB;

/**
 * Created by javier on 2/11/15.
 */
public abstract class DbActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState, int contentLayout) {
        super.onCreate(savedInstanceState);
        setContentView(contentLayout);

        getDB().getReadableDatabase();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getDB().getReadableDatabase();
    }

    @Override
    protected void onStop() {
        super.onStop();
        getDB().close();
    }

    public CardsDB getDB() {
        return CardsDB.getInstance(this);
    }

}
