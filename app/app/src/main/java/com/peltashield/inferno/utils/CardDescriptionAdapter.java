package com.peltashield.inferno.utils;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.peltashield.inferno.MainActivity;
import com.peltashield.inferno.R;
import com.peltashield.inferno.cardsDB.CardCode;
import com.peltashield.inferno.cardsDB.CardDetails;
import com.peltashield.inferno.cardsDB.CardField;

/**
 * Created by javier on 6/12/15.
 */
public class CardDescriptionAdapter extends BaseAdapter {

    protected final MainActivity context;
    protected int[] ids;

    public CardDescriptionAdapter(Context context, int[] ids) {
        super();
        this.context = (MainActivity) context;
        this.ids = ids;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView;
        if (convertView == null) { // if it's not recycled
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.card_description_row, parent, false);
        } else {
            rowView = convertView;
        }

        setFields(rowView, position);

        return rowView;
    }

    protected void setFields(View rowView, int position) {
        ImageView imageView = (ImageView) rowView.findViewById(R.id.card_image);
        TextView nameView = (TextView) rowView.findViewById(R.id.card_name);
        TextView typeView = (TextView) rowView.findViewById(R.id.card_type);
        TextView editionCodeView = (TextView) rowView.findViewById(R.id.card_edition_code);
        TextView costView = (TextView) rowView.findViewById(R.id.card_cost);
        ImageView pathView = (ImageView) rowView.findViewById(R.id.card_path);
        TextView strView = (TextView) rowView.findViewById(R.id.card_str);
        TextView resView = (TextView) rowView.findViewById(R.id.card_res);

        int id = ids[position];
        CardDetails details = context.getDB().getCardDetails(id);
        float size = rowView.getResources().getDimension(R.dimen.text_size_normal);
        nameView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        typeView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);

        nameView.setText(details.getName());
        typeView.setText(details.getType());

        editionCodeView.setText(CardCommons.getEditionCode(details.getEdition()));
        editionCodeView.setTextColor(CardCommons.getRarityColor(details.getRarity()));

        costView.setText(details.getCost());
        pathView.setImageResource(CardField.getPathId(details.getPath()));
        strView.setText(details.getStr());
        resView.setText(details.getRes());

        setText(rowView, details);

        context.loadCard(imageView, id, details.isPortrait(context), true);
    }

    protected void setText(View rowView, CardDetails details) {
        TextView textView = (TextView) rowView.findViewById(R.id.card_text);
        String text = details.getText().replaceAll("\\\\n", "\\\n").replace("#", "");
        SpannableString ss = new SpannableString(text);
        for (int i = 0; i < text.length(); i++) {
            i = text.indexOf('{', i) + 1;
            if (i == 0) {
                break;
            }
            for (CardCode cardCode : CardCode.values()) {
                String code = cardCode.getCode();
                int j = i + code.length();
                if (text.substring(i, j).equals(code)) {
                    ImageSpan is = new ImageSpan(context, cardCode.getDrawableId());
                    ss.setSpan(is, i - 1, j + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    i = j + 1;
                    break;
                }
            }
        }
        textView.setText(ss);
    }

    @Override
    public int getCount() {
        return ids.length;
    }

    @Override
    public Object getItem(int position) {
        return ids[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void remove(int position) {
        ids = ArrayCommons.remove(ids, position);
        notifyDataSetChanged();
    }

}
