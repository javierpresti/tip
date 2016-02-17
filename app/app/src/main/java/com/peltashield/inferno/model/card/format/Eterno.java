package com.peltashield.inferno.model.card.format;

/**
 * Created by javier on 2/15/16.
 */
public class Eterno extends OpenFormat {

    protected static Eterno instance;

    private Eterno() {}

    public static Eterno getInstance() {
        return instance == null ? instance = new Eterno() : instance;
    }

    @Override
    public boolean isEditionAllowed(int cardId) {
        return cardId != 1620112 && cardId != 2053124;
    }

    @Override
    public String toShortString() {
        return "Et";
    }
}
