package com.peltashield.inferno.deck;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.peltashield.inferno.MainActivity;
import com.peltashield.inferno.R;
import com.peltashield.inferno.cardsDB.CardDetails;
import com.peltashield.inferno.cardsDB.CardField;
import com.peltashield.inferno.utils.ArrayCommons;
import com.peltashield.inferno.utils.CardCommons;

/**
 * Created by javier on 1/25/16.
 */
public class DeckDescriptionAdapter extends BaseAdapter {

    protected final MainActivity context;
    protected int[] ids;
    protected int[] qtys;

    public DeckDescriptionAdapter(Context context, int[] ids, int[] qtys) {
        super();
        this.context = (MainActivity) context;
        this.ids = ids;
        this.qtys = qtys;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView;
        if (convertView == null) { // if it's not recycled
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.deck_description_row, parent, false);
        } else {
            rowView = convertView;
        }

        setFields(rowView, position);

        return rowView;
    }

    protected void setFields(View rowView, int position) {
        ImageView imageView = (ImageView) rowView.findViewById(R.id.card_image);
        TextView cardQty = (TextView) rowView.findViewById(R.id.card_qty);
        TextView nameView = (TextView) rowView.findViewById(R.id.card_name);
        TextView typeView = (TextView) rowView.findViewById(R.id.card_type);
        TextView editionCodeView = (TextView) rowView.findViewById(R.id.card_edition_code);
        TextView costView = (TextView) rowView.findViewById(R.id.card_cost);
        ImageView pathView = (ImageView) rowView.findViewById(R.id.card_path);
        TextView strView = (TextView) rowView.findViewById(R.id.card_str);
        TextView resView = (TextView) rowView.findViewById(R.id.card_res);

        int id = ids[position];
        int qty = qtys[position];
        cardQty.setText(String.valueOf(qty));
        float size = rowView.getResources().getDimension(R.dimen.text_size_normal);
        nameView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        typeView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);

        CardDetails details = context.getDB().getCardDetails(id);
        nameView.setText(details.getName());
        typeView.setText(details.getType());

        editionCodeView.setText(CardCommons.getEditionCode(details.getEdition()));
        editionCodeView.setTextColor(CardCommons.getRarityColor(details.getRarity()));

        costView.setText(details.getCost());
        pathView.setImageResource(CardField.getPathId(details.getPath()));
        strView.setText(details.getStr());
        resView.setText(details.getRes());

        context.loadCard(imageView, id, details.isPortrait(context), true);
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

    public void set(int[] ids, int[] qtys) {
        this.ids = ids;
        this.qtys = qtys;
        notifyDataSetChanged();
    }
}
