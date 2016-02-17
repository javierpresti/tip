package com.peltashield.inferno.cardsDB;

import android.content.Context;

import com.peltashield.inferno.Configs;
import com.peltashield.inferno.R;

/**
 * Created by javier on 2/9/15.
 */
public class CardDetails {

    protected String name;
    protected String format;
    protected String path;
    protected String type;
    protected String cost;
    protected String str;
    protected String res;
    protected String text;
    protected String legend;

    protected String rules;
    protected String errata;
    protected String limited;
    protected String banned;

    protected int[] ids;
    protected String edition;
    protected String[] editions;
    protected String[] rarities;
    protected String rarity;
    protected String[] artists;

    public static String[] getDataFields() {
        return new String[] {
                CardField.NAME, CardField.FORMAT, CardField.PATH, CardField.TYPE,
                CardField.COST, CardField.STR, CardField.RES, CardField.TEXT_FULL,CardField.LEGEND,
                CardField.RULES, CardField.ERRATA, CardField.LIMITED, CardField.BANNED};
    }

    public static String[] getMultiFields() {
        return new String[] {CardField.ID, CardField.EDITION, CardField.RARITY, CardField.ARTIST};
    }

    public void setFields(String[] fields) {
        int i = 0;
        name = fields[i++];     format = fields[i++];   path = fields[i++];     type = fields[i++];
        cost = fields[i++];     str = fields[i++];      res = fields[i++];      text = fields[i++];
        legend = fields[i++];
        rules = fields[i++];    errata = fields[i++];   limited = fields[i++];banned = fields[i++];
    }

    public void setMultiFields(int[] ids, String[] editions, String[] rarities, String[] artists) {
        this.ids = ids;
        this.editions = editions;
        this.rarities = rarities;
        this.artists = artists;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public boolean isPortrait(Context context) {
        return  !getType().contains(context.getString(R.string.escenario));
    }

    public String getName() {
        return this.name;
    }
    public String getFormat() {
        return this.format;
    }
    public String getPath() {
        return this.path;
    }
    public String getType() {
        return this.type;
    }
    public String getCost() {
        return this.cost;
    }
    public String getStr() {
        return this.str;
    }
    public String getRes() {
        return this.res;
    }
    public String getText() {
        return this.text;
    }
    public String getLegend() {
        return this.legend;
    }
    public int[] getIds() {
        return this.ids;
    }
    public String getEdition() {
        return this.edition;
    }
    public String[] getEditions() {
        return this.editions;
    }
    public String[] getRarities() {
        return this.rarities;
    }
    public String getRarity() {
        return this.rarity;
    }
    public String[] getArtists() {
        return this.artists;
    }
    public String getRules() {
        return this.rules;
    }
    public String getErrata() {
        return this.errata;
    }
    public String getLimited() {
        return this.limited;
    }
    public String getBanned() {
        return this.banned;
    }

    protected static boolean isSideTitled(int cardId) {
        return cardId >= Configs.getFirstSideDemonId();
    }

    protected static boolean isSideDemon(int cardId) {
        return isSideTitled(cardId) && cardId < 1711000 ||
                cardId > 1874000 && cardId < 1878000 ||
                cardId > 1964000 && cardId < 1969000;
    }

    public static int[] getScales(int cardId, boolean portrait, int cardWidth, int cardHeight) {
        boolean side = isSideTitled(cardId);
        boolean sideDemon = isSideDemon(cardId);
        int x, y, width, height;
        if (portrait) {
            if (side) {
                if (sideDemon) {
                    x = 0;
                    y = (int) (cardHeight / 7.77);
                    height = (int) (cardHeight / 1.7);
                } else {
                    x = (int) (cardWidth / 4.16);
                    y = 0;
                    height = (int) (cardHeight / 1.47);
                }
                width = (int) (cardWidth/1.01) - x;
            } else {
                x = (int)(cardWidth*0.13);
                y = (int)(cardHeight*0.124);
                height = (int)(cardHeight*0.59);
                width = (int)(cardWidth*0.88) - x;
            }
        } else {
            if (side) {
                x = (int)(cardWidth*0.2);
                y = 0;
                height = (int)(cardHeight*0.8);
                width = (int)(cardWidth*0.7) - x;
            } else {
                x = (int)(cardWidth*0.18);
                y = (int)(cardHeight*0.06);
                height = (int)(cardHeight*0.93) - y;
                width = (int)(cardWidth*0.71) - x;
            }
        }
        return new int[] {x, y, width, height};
    }

}
