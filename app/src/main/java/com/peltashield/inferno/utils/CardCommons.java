package com.peltashield.inferno.utils;

import android.graphics.Color;

/**
 * Created by javier on 4/8/15.
 */
public class CardCommons {
    //TODO this class is hardcored. Review

    public static String getAbilityDescription(String ability) {
        String dc;
        switch (ability) {
            case "Abismal": dc = "Las cartas que fueran a ir a un inferno por el daño de esta " +
                    "carta, se exilian";
                break;
            case "Acuático": dc = "Al comienzo de tu restauración, si no hay Acqua, pon un " +
                    "contador de daño sobre este aliado";
                break;
            case "Agresor": dc = "Tu enemigo debe proponer un aliado con Agresor como defensor";
                break;
            case "Bendición de Ubanna": dc = "Cuesta 2 menos si hay Thanatos"; break;
            case "Estocada": dc = "Cuando haga daño de combate a un demonio"; break;
            case "Evasivo": dc = "No puede ser atacado"; break;
            case "Furia": dc = "Puede atacar inmediatamente"; break;
            case "Golpe de gracia": dc = "Cuando destruya a un aliado"; break;
            case "Halo": dc = "No puede ser objetivo de cartas o habilidades enemigas"; break;
            case "Impulso": dc = "Cuando juegues una carta neutral desde tu mano.\n" +
                    "Múltiples instancias de Impulso son independientes"; break;
            case "Indestructible": dc = "No puede ser destruido por efectos ni para pagar " +
                    "costes, no puede recibir daño ni colocar contadores de daño sobre él"; break;
            case "Inmerso": dc = "Juega esta carta sólo si hay Acqua"; break;
            case "Profundidad": dc = "Cartas con Acqua en juego"; break;
            case "Protector": dc = "Puedes girar el aliado y hacer que defienda al atacante, " +
                    "en vez del defensor propuesto"; break;
            case "Recobrar": dc = "Puedes jugar esta carta desde tu inferno, pagando su coste " +
                    "de Recobrar. Si lo haces, exíliala"; break;
            case "Réplica": dc = "Cuando juegues esta carta, puedes jugar las copias de tu " +
                    "inferno pagando su Réplica"; break;
            case "Sangre débil": dc = "Juega esta carta sólo si hay Thanatos"; break;
            case "Vínculo": dc = "Cuando sea puesta en el inferno desde el mazo, puedes pagar " +
                    "su coste de Vínculo y exiliarla"; break;
            case "Thanatos": dc = "Entorno"; break;
            case "Acqua": dc = "Entorno"; break;
            default: dc = null;
        }
        return dc;
    }

    public static String getEditionCode(String edition) {
        String code;
        switch (edition) {
            case "Despertar": code = "DSP"; break;
            case "Insania": code = "INS"; break;
            case "Conquista": code = "CNQ"; break;
            case "Destino": code = "DST"; break;
            case "Hadouken": code = "HDK"; break;
            case "Origen": code = "ORI"; break;
            case "Descenso": code = "DSC"; break;
            case "Encuentro": code = "ENC"; break;
            case "Abismo": code = "ABI"; break;
            default: code = null;
        }
        if (code == null) {
            if (edition.contains("Despertar")) {
                code = "DSP";
            } else if (edition.contains("Insania")) {
                code = "INS";
            } else if (edition.contains("Conquista")) {
                code = "CNQ";
            } else if (edition.contains("Destino")) {
                code = "DST";
            } else if (edition.contains("Origen")) {
                code = "ORI";
            } else if (edition.contains("Descenso")) {
                code = "DSC";
            } else if (edition.contains("Encuentro")) {
                code = "ENC";
            } else if (edition.contains("Abismo")) {
                code = "ABI";
            }
        }
        return code;
    }

    public static int getRarityColor(String rarity) {
        int colorId;
        switch (rarity) {
            case "Común":
                colorId = Color.LTGRAY;
                break;
            case "Infrecuente":
                colorId = Color.RED;
                break;
            case "Rara":
                colorId = Color.YELLOW;
                break;
            case "Épica":
                colorId = Color.CYAN;
                break;
            case "Promocional":
                colorId = Color.GREEN;
                break;
            default:
                colorId = Color.WHITE;
        }
        return colorId;
    }

}
