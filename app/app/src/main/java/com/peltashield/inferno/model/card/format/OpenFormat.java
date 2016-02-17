package com.peltashield.inferno.model.card.format;

import com.peltashield.inferno.Configs;
import com.peltashield.inferno.model.deck.Deck;

/**
 * Created by javier on 2/15/16.
 */
public abstract class OpenFormat extends Format {

    @Override
    public boolean isFormat(Deck deck) {
        boolean valid = deck.getSize() == 51;                       // 51 cards
        if (valid) {
            boolean demonFound = false;
            for (Integer key : deck.getCards().keySet()) {
                if (Configs.isDemon(key)) {                         // 1 demon
                    if (demonFound || deck.getCards().get(key) != 1) {
                        valid = false;
                        break;
                    } else {
                        demonFound = true;
                    }
                } else if (!validSize(key, deck.getCards().get(key))) {
                    valid = false;
                    break;
                }
            }
        }
        return valid;
    }

    protected boolean validSize(int cardId, int cardQty) {
        return cardQty <= 4 && cardQty >= 1 && !(Configs.isLimited(cardId) && cardQty > 1) &&
                !Configs.isToken(cardId) && isEditionAllowed(cardId);
    }

    public abstract boolean isEditionAllowed(int cardId);
}
