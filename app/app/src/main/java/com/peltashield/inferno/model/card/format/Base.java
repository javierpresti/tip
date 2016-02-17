package com.peltashield.inferno.model.card.format;

/**
 * Created by javier on 2/15/16.
 */
public class Base extends OpenFormat {

    protected static Base instance;

    private Base() {}

    public static Base getInstance() {
        return instance == null ? instance = new Base() : instance;
    }

    @Override
    public boolean isEditionAllowed(int cardId) {
        return cardId > 1621000 && cardId < 2053000;
    }

    @Override
    public String toShortString() {
        return "Bs";
    }
}
