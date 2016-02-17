package com.peltashield.inferno.model.card.format;

/**
 * Created by javier on 2/15/16.
 */
public class Cdu extends OpenFormat {

    protected static Cdu instance;

    private Cdu() {}

    public static Cdu getInstance() {
        return instance == null ? instance = new Cdu() : instance;
    }

    @Override
    public boolean isEditionAllowed(int cardId) {
        return cardId > 1185000 && cardId < 1620000;
    }

    @Override
    public String toShortString() {
        return "CdU";
    }
}
