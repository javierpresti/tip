package com.peltashield.inferno.cardsDB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import com.peltashield.inferno.Configs;
import com.peltashield.inferno.R;
import com.peltashield.inferno.utils.StringCommons;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.List;

/**
 * Created by javier on 3/10/15.
 */
public class CardsDB extends SQLiteAssetHelper {

    protected static final String DB_NAME = "cards.db";
    protected static final String TABLE_DATA = "cards_data";
    protected static final String TABLE_EDITIONS = "cards_editions";

    protected String whereQuery = "";
    protected String order = "";

    protected SQLiteDatabase db;
    protected static CardsDB instance;

    private CardsDB(Context context) {
        super(context, DB_NAME, null, Configs.DB_VERSION);
        setForcedUpgrade(); // Any db upgrade will overwrite it. Use on read-only dbs.
        // See README for alternative for read-write dbs.
    }

    public static CardsDB getInstance(Context context) {
        if (instance == null) {
            instance = new CardsDB(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public synchronized SQLiteDatabase getReadableDatabase() {
        db = super.getReadableDatabase();
        setForeignKeyConstraints(db);
        return db;
    }

    @Override
    public synchronized SQLiteDatabase getWritableDatabase() {
        db = super.getWritableDatabase();
        setForeignKeyConstraints(db);
        return db;
    }

    protected void setForeignKeyConstraints(SQLiteDatabase db) {
        if (Build.VERSION.SDK_INT >= 16) {
            db.setForeignKeyConstraintsEnabled(true);
        }
    }

    public void addFilter(String field, String value, boolean parseValues, boolean wholeSearch) {
        String limiter = wholeSearch ? "" : "%";
        addFilter(field, "like", value, false, parseValues, limiter, limiter);
    }

    public void addFilter(String field, boolean not, String value, boolean parseValues,
                          boolean wholeSearch) {
        String limiter = wholeSearch ? "" : "%";
        addFilter(field, not, value, parseValues, limiter, limiter);
    }

    public void addFilter(String field, boolean not, String value, boolean parseValues,
                          String init, String last) {
        addFilter(field, not ? "not like" : "like", value, false, parseValues, init, last);
    }

    public void addFilter(String field, String comparator, String value, boolean xValues,
                          boolean parseValues, boolean wholeSearch) {
        String toAppend = wholeSearch ? "" : "%";
        addFilter(field, comparator, value, xValues, parseValues, toAppend, toAppend);
    }

    public void addFilter(String field, String comparator, String value, boolean xValues,
                          boolean parseValues, String init, String last) {
        StringBuilder sb = new StringBuilder();
        if (!whereQuery.equals("")) {
            sb.append(whereQuery)
                    .append(" and ");
        }
        setFieldValue(sb, "(", field, comparator, value, parseValues, init, last);
        if (comparator.contains("<") || comparator.contains(">")) {
            setFieldValue(sb, " and ", field, "!=", "", false, true);
            if (xValues && comparator.contains(">")) {
                setFieldValue(sb, " or ", CardField.getRawXField(field), "!=", "", false, true);
            }
        }
        sb.append(')');
        whereQuery = sb.toString();
    }

    public void addOrFilters(String field, List<String> values, boolean parseValues,
                             boolean wholeSearch) {
        String toAppend = wholeSearch ? "" : "%";
        addOrFilters(field, values, parseValues, toAppend, toAppend);
    }

    public void addOrFilters(String field, List<String> values, boolean parseValues,
                             String init, String last) {
        if (!values.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            if (!whereQuery.equals("")) {
                sb.append(whereQuery)
                        .append(" and ");
            }
            setFieldValue(sb, "(", field, "like", values.get(0), parseValues, init, last);
            for (int i = 1; i < values.size(); i++) {
                setFieldValue(sb, " or ", field, "like", values.get(i), parseValues, init, last);
            }
            sb.append(')');
            whereQuery = sb.toString();
        }
    }

    public void addOrFilters(List<String> fields, List<String> values,
                             boolean parseValues, boolean wholeSearch) {
        if (!values.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            if (!whereQuery.equals("")) {
                sb.append(whereQuery)
                        .append(" and ");
            }
            setFieldValue(sb, "(", fields.get(0), "like", values.get(0), parseValues, wholeSearch);
            for (int i = 1; i < values.size(); i++) {
                setFieldValue(sb, " or ", fields.get(i), "like", values.get(i), parseValues,
                        wholeSearch);
            }
            sb.append(')');
            whereQuery = sb.toString();
        }
    }

    public void addOrFilters(List<String> fields, List<String> comparators, List<String> values,
                             List<Boolean> parseValues, List<String> inits, List<String> lasts) {
        addOrFilters(fields, comparators, values, null, parseValues, inits, lasts);
    }

    public void addOrFilters(List<String> fields, List<String> comparators, List<String> values,
                             List<Boolean> xValues,
                             List<Boolean> parseValues, List<String> inits, List<String> lasts) {
        if (!values.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            if (!whereQuery.equals("")) {
                sb.append(whereQuery)
                        .append(" and ");
            }
            for (int i = 0; i < values.size(); i++) {
                setFieldValue(sb, i == 0 ? "(" : " or ", fields.get(i), comparators.get(i),
                        values.get(i), parseValues.get(i), inits.get(i), lasts.get(i));
                if (xValues != null) {
                    if (comparators.get(i).contains("<") || comparators.get(i).contains(">")) {
                        setFieldValue(sb, " and ", fields.get(i), "!=", "", false, true);
                        if ((xValues.get(i)) && comparators.get(i).equals(">")) {
                            setFieldValue(sb, " or ", CardField.getRawXField(fields.get(i)),
                                    "!=", "", false, true);
                        }
                    }
                }
            }
            sb.append(')');
            whereQuery = sb.toString();
        }
    }

    protected void setFieldValue(StringBuilder sb, String before, String field, String comparator,
                                 String value, boolean parseValue, boolean wholeSearch) {
        String toAppend = wholeSearch ? "" : "%";
        setFieldValue(sb, before, field, comparator, value, parseValue, toAppend, toAppend);
    }

    protected void setFieldValue(StringBuilder sb, String before, String field, String comparator,
                                 String value, boolean parseValue, String init, String last) {
        String valueToQuery = value.replace("'", "");
        valueToQuery = parseValue ? StringCommons.removeAccents(valueToQuery).toLowerCase() :
                valueToQuery;
        sb.append(before)
                .append(field)
                .append(" ")
                .append(comparator)
                .append(" '")
                .append(init)
                .append(valueToQuery)
                .append(last)
                .append("'");
    }

    public int[] getCardImageIds() {
        String where = "";
        String table = TABLE_DATA + " LEFT JOIN " + TABLE_EDITIONS +
                " USING (" + CardField.INTERNAL_ID + ")";
        String field = TABLE_EDITIONS + "." + CardField.ID;

        if (!whereQuery.equals("")) {
            where = " WHERE " + whereQuery;
            if (order.equals("")) {
                order = " ORDER BY " + CardField.ALL_N;
            }
        }

        whereQuery = "";
        String query = "SELECT " + field + " FROM " + table + where +
                " GROUP BY " + CardField.INTERNAL_ID + order;
        if (!db.isOpen()) {
            getReadableDatabase();
        }
        Cursor cursor = db.rawQuery(query, null);
        order = "";

        int[] ids = new int[cursor.getCount()];
        cursor.moveToFirst();
        for (int i = 0; !cursor.isAfterLast(); i++) {
            ids[i] = cursor.getInt(0);
            cursor.moveToNext();
        }
        cursor.close();
        return ids;
    }

    public String[] getFieldValuesWithFilters(String field) {
        String where = "";
        String table = TABLE_DATA + " LEFT JOIN " + TABLE_EDITIONS +
                " USING (" + CardField.INTERNAL_ID + ")";

        if (!whereQuery.equals("")) {
            where = " WHERE " + whereQuery;
        }

        String query = "SELECT " + field + " FROM " + table + where +
                " GROUP BY " + CardField.INTERNAL_ID;
        if (!db.isOpen()) {
            getReadableDatabase();
        }
        Cursor cursor = db.rawQuery(query, null);

        String[] values = new String[cursor.getCount()];
        cursor.moveToFirst();
        for (int i = 0; !cursor.isAfterLast(); i++) {
            values[i] = cursor.getString(0);
            cursor.moveToNext();
        }
        cursor.close();
        return values;
    }

    public int getRandomCardId(boolean portrait) {
        if (!db.isOpen()) {
            getReadableDatabase();
        }
        Cursor cursor = db.rawQuery("SELECT " + CardField.ID + " FROM " + TABLE_DATA + " WHERE " +
                        CardField.BASIC_TYPE + (portrait ? " IS NOT" : " IS") +
                        " 'Escenario' ORDER BY RANDOM() LIMIT 1", null);
        cursor.moveToFirst();
        int id = cursor.getInt(0);
        cursor.close();
        return id;
    }

    public String[] getFieldValues(String field) {
        String[] values = null;
        if (db.isOpen()) {
            Cursor cursor = db.query(true, TABLE_DATA + " LEFT JOIN " + TABLE_EDITIONS + " USING (" +
                            CardField.INTERNAL_ID + ")", new String[]{field}, null, null, null, null,
                    null, null);
            values = new String[cursor.getCount()];
            cursor.moveToFirst();
            for (int i = 0; !cursor.isAfterLast(); i++) {
                values[i] = cursor.getString(0);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return values;
    }

    public CardDetails getCardDetails(int cardId) {
        String query = CardField.ID + " is " + cardId;
        if (!db.isOpen()) {
            getReadableDatabase();
        }
        Cursor cursor = db.query(TABLE_EDITIONS, new String[]{CardField.INTERNAL_ID,
                        CardField.EDITION, CardField.RARITY}, query, null, null, null, null);
        cursor.moveToFirst();
        int internalId = cursor.getInt(0);
        String edition = cursor.getString(1);
        String rarity = cursor.getString(2);
        cursor.close();

        String[] fields = CardDetails.getDataFields();
        String[] data = new String[fields.length];

        query = CardField.INTERNAL_ID + " is " + internalId;
        if (!db.isOpen()) {
            getReadableDatabase();
        }
        cursor = db.query(TABLE_DATA, fields, query, null, null, null, null);
        cursor.moveToFirst();
        for (int i = 0; i < fields.length; i++) {
            data[i] = cursor.getString(i);
        }
        cursor.close();


        if (!db.isOpen()) {
            getReadableDatabase();
        }
        cursor = db.query(TABLE_EDITIONS, CardDetails.getMultiFields(),
                query, null, null, null, null);
        int[] ids = new int[cursor.getCount()];
        String[] editions = new String[cursor.getCount()];
        String[] rarities = new String[cursor.getCount()];
        String[] artists = new String[cursor.getCount()];
        cursor.moveToFirst();
        for (int i = 0; !cursor.isAfterLast(); i++) {
            ids[i] = cursor.getInt(0);
            editions[i] = cursor.getString(1);
            rarities[i] = cursor.getString(2);
            artists[i] = cursor.getString(3);
            cursor.moveToNext();
        }
        cursor.close();

        CardDetails details = new CardDetails();
        details.setEdition(edition);
        details.setRarity(rarity);
        details.setFields(data);
        details.setMultiFields(ids, editions, rarities, artists);
        return details;
    }

    protected String getFieldOrder(String field) {
        String order;
        if (field.equals(CardField.ES_EDICION)) {
            order = " ORDER BY " + CardField.ALL_N;
        } else if (field.equals(CardField.ES_RAREZA)) {
            StringBuilder sb = new StringBuilder();
            sb.append(" ORDER BY ")
                    .append("CASE ")
                    .append(CardField.getDBField(field));
            String[] rarities = CardField.getEsQuotedRarities();
            for (int i = 0; i < rarities.length; i++) {
                sb.append(" WHEN ")
                        .append(rarities[i])
                        .append(" THEN ")
                        .append(i);
            }
            sb.append(" END");
            order = sb.toString();
        } else {
            order = " ORDER BY " + CardField.getDBField(field);
        }
        return order;
    }

    protected String getOrderDirection(String orderDirection, String ascendingValue) {
        return orderDirection.equals(ascendingValue) ? "ASC" : "DESC";
    }

    public void setOrder(String field, String direction, String ascendingValue) {
        this.order = getFieldOrder(field) + " " + getOrderDirection(direction, ascendingValue);
    }

    public void resetFilters() {
        whereQuery = "";
        order = "";
    }

}
