package com.peltashield.inferno.model.card.format;

import com.peltashield.inferno.model.deck.Deck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by javier on 2/5/16.
 */
public abstract class Format {

    protected static List<Format> instances = new ArrayList<Format>(Arrays.asList(
            Vap.getInstance(), Cdu.getInstance(), Base.getInstance(), Eterno.getInstance()));

    public abstract boolean isFormat(Deck deck);

    public abstract String toShortString();

    public static List<Format> getInstances() {
        return instances;
    }

}
