package com.peltashield.inferno.model.deck;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import com.peltashield.inferno.model.card.format.Format;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by javier on 10/30/15.
 */
public class Deck extends SugarRecord {

    protected String name;
    protected String cardsJson;

    @Ignore
    protected HashMap<Integer, Integer> cards; //cardId, qtyCard


    public Deck(String name) {
        this.name = name;
    }

    public Deck() {}

    public int[] getIds() {
        Integer[] arr = getCards().keySet().toArray(new Integer[getCards().size()]);
        int[] array = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            array[i] = arr[i];
        }
        return array;
    }

    public int[] getQuantities() {
        Integer[] arr = getCards().values().toArray(new Integer[getCards().size()]);
        int[] array = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            array[i] = arr[i];
        }
        return array;
    }

    public void addCard(int cardId) {
        Integer qtyCards = getCards().get(cardId);
        if (qtyCards == null) {
            qtyCards = 0;
        }
        addCard(cardId, qtyCards + 1);
    }

    public void addCard(int cardId, int qty) {
        if (qty > 0) {
            getCards().put(cardId, qty);
        } else {
            removeAllOfCard(cardId);
        }
    }

    public void removeCard(int cardId) {
        Integer qtyCards = getCards().get(cardId);
        if (qtyCards != null) {
            if (qtyCards == 1) {
                getCards().remove(cardId);
            } else {
                getCards().put(cardId, qtyCards - 1);
            }
        }
    }

    public void removeAllOfCard(int cardId) {
        getCards().remove(cardId);
    }

    public List<String> getFormats() {
        List<String> validFormats = new ArrayList<>();
        for (Format format : Format.getInstances()) {
            if (format.isFormat(this)) {
                validFormats.add(format.toShortString());
            }
        }
        return validFormats;
    }

    public int getSize() {
        int size = 0;
        for (Integer value : getCards().values()) {
            size += value;
        }
        return size;
    }

    public HashMap<Integer, Integer> getCards() {
        if (cards == null) {
            if (cardsJson == null || "null".equals(cardsJson)) {
                cards = new HashMap<>();
            } else {
                cards = new Gson().fromJson(cardsJson,
                        new TypeToken<HashMap<Integer, Integer>>() {}.getType());
            }
        }
        return cards;
    }

    @Override
    public long save() {
        cardsJson = new Gson().toJson(cards);
        return super.save();
    }

    public String getName() {
        return name;
    }
    public String getCardsJson() {
        return cardsJson;
    }

}