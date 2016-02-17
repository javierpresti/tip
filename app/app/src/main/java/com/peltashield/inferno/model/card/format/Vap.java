package com.peltashield.inferno.model.card.format;

/**
 * Created by javier on 2/15/16.
 */
public class Vap extends OpenFormat {

    protected static Vap instance;

    private Vap() {}

    public static Vap getInstance() {
        return instance == null ? instance = new Vap() : instance;
    }

    @Override
    public boolean isEditionAllowed(int cardId) {
        return cardId > 1703000 && cardId < 2053000;
    }

    @Override
    public String toShortString() {
        return "VaP";
    }
}
