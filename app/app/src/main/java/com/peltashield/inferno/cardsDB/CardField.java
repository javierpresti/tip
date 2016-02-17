package com.peltashield.inferno.cardsDB;

import com.peltashield.inferno.Configs;
import com.peltashield.inferno.R;

/**
 * Created by javier on 1/28/15.
 */
public class CardField {

    public static final String ID = "_id";
    public static final String INTERNAL_ID = "internal_id";

    public static final String NAME_RAW = "name_raw";
    public static final String NAME = "name";
    public static final String FORMAT = "format";
    public static final String PATH = "path";
    public static final String TYPE = "type";
    public static final String BASIC_TYPE = "basic_type";
    public static final String SUPERTYPE = "supertype";
    public static final String SUBTYPE_RAW = "subtype_raw";
    public static final String SUBTYPE = "subtype";
    public static final String COST_RAW = "cost_raw";
    public static final String STR_RAW = "str_raw";
    public static final String RES_RAW = "res_raw";
    public static final String COST_RAWX = "cost_rawx";
    public static final String STR_RAWX = "str_rawx";
    public static final String RES_RAWX = "res_rawx";
    public static final String COST = "cost";
    public static final String STR = "str";
    public static final String RES = "res";
    public static final String TEXT_RAW = "text_raw";
    public static final String TEXT_FULL = "text_full";
    public static final String LEGEND_RAW = "legend_raw";
    public static final String LEGEND = "legend";
    public static final String ABILITY = "ability";
    public static final String RULES = "rules";
    public static final String ERRATA = "errata";
    public static final String LIMITED = "limited";
    public static final String BANNED = "banned";

    public static final String ALL_N = "all_n";
    public static final String EDITION = "edition";
    public static final String RARITY = "rarity";
    public static final String ARTIST = "artist";
    public static final String ARTIST_RAW = "artist_raw";

    public static final String FLAG_EDITION_ID = "flag_edition_id";
    public static final String ED_N = "ed_n";

    private static final String SIZE_FULL = "zf";
    private static final String SIZE_THUMB = "zt";
    private static final String FLIP_SIDE = "r";

    public static final String ES_NOMBRE = "Nombre";
    public static final String ES_TEXTO = "Texto";
    public static final String ES_FORMATO = "Formato";
    public static final String ES_EDICION = "Edición";
    public static final String ES_TIPO_BASICO = "Tipo básico";
    public static final String ES_TIPO = "Tipo";
    public static final String ES_SUPERTIPO = "Supertipo";
    public static final String ES_SUBTIPO = "Subtipo";
    public static final String ES_RAREZA = "Rareza";
    public static final String ES_HABILIDAD = "Habilidad";
    public static final String ES_COSTE = "Coste";
    public static final String ES_FUE = "FUE";
    public static final String ES_RES = "RES";
    public static final String ES_ARTISTA = "Artista";
    public static final String ES_LEYENDA = "Leyenda";

    public static final String ES_R_COMUN = "Común";
    public static final String ES_R_INFRECUENTE = "Infrecuente";
    public static final String ES_R_RARA = "Rara";
    public static final String ES_R_EPICA = "Épica";
    public static final String ES_R_PROMOCIONAL = "Promocional";

    public static String[] getEsQuotedRarities() {
        return new String[]{"'" + ES_R_COMUN + "'", "'" + ES_R_INFRECUENTE + "'",
                "'" + ES_R_RARA + "'", "'" + ES_R_EPICA + "'", "'" + ES_R_PROMOCIONAL + "'"};
    }

    public static String getRawXField(String rawField) {
        return rawField + "x";
    }

    public static String getCardImageName(int cardId, boolean thumbImage, boolean flip) {
        String size = thumbImage && Configs.isUsingThumbImages() ? SIZE_THUMB : SIZE_FULL;
        return size + cardId + (flip ? FLIP_SIDE : "") + ".jpg";
    }

    public static int getPathId(String path) {
        int pathId;
        switch (path) {
            case "Caos":
                pathId = R.drawable.path_caos;
                break;
            case "Locura":
                pathId = R.drawable.path_locura;
                break;
            case "Muerte":
                pathId = R.drawable.path_muerte;
                break;
            case "Poder":
                pathId = R.drawable.path_poder;
                break;
            default:
                pathId = R.drawable.path_neutral;
        }
        return pathId;
    }

    public static int getWhitePathId(String path) {
        int pathId;
        switch (path) {
            case "Caos":
                pathId = R.drawable.whiteb_caos;
                break;
            case "Locura":
                pathId = R.drawable.whiteb_locura;
                break;
            case "Muerte":
                pathId = R.drawable.whiteb_muerte;
                break;
            case "Poder":
                pathId = R.drawable.whiteb_poder;
                break;
            default:
                pathId = R.drawable.path_neutral;
        }
        return pathId;
    }

    public static String getDBField(String esField) {
        String dbField = null;
        switch (esField) {
            case ES_NOMBRE:     dbField = CardField.NAME_RAW; break;
            case ES_TEXTO:      dbField = CardField.TEXT_RAW; break;
            case ES_FORMATO:    dbField = CardField.FORMAT; break;
            case ES_EDICION:    dbField = CardField.EDITION; break;
            case ES_TIPO:       dbField = CardField.TYPE; break;
            case ES_TIPO_BASICO: dbField = CardField.BASIC_TYPE; break;
            case ES_SUPERTIPO:  dbField = CardField.SUPERTYPE; break;
            case ES_SUBTIPO:    dbField = CardField.SUBTYPE_RAW; break;
            case ES_RAREZA:     dbField = CardField.RARITY; break;
            case ES_HABILIDAD:  dbField = CardField.ABILITY; break;
            case ES_COSTE:      dbField = CardField.COST_RAW; break;
            case ES_FUE:        dbField = CardField.STR_RAW; break;
            case ES_RES:        dbField = CardField.RES_RAW; break;
            case ES_ARTISTA:    dbField = CardField.ARTIST_RAW; break;
            case ES_LEYENDA:    dbField = CardField.LEGEND_RAW; break;
        }
        return dbField;
    }

}
