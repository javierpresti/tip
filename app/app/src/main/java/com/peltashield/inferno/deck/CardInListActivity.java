package com.peltashield.inferno.deck;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.peltashield.inferno.Configs;
import com.peltashield.inferno.ImageActivity;
import com.peltashield.inferno.R;
import com.peltashield.inferno.model.deck.Deck;
import com.peltashield.inferno.utils.AdManager;
import com.peltashield.inferno.utils.CardViewAdapter;

/**
 * Created by javier on 2/3/16.
 */
public class CardInListActivity extends ImageActivity {
    public static final String EXTRA_DECK_ID = "com.peltashield.inferno.DECK_ID";
    public static final String EXTRA_POS_SELECTED = "com.peltashield.inferno.POS_SELECTED";

    protected ViewPager viewPager;
    protected Deck deck;
    protected MenuItem changeQtyItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_view);

        deck = Deck.findById(Deck.class, getIntent().getLongExtra(EXTRA_DECK_ID, -1));
        viewPager = (ViewPager) findViewById(R.id.pager);
        CardViewAdapter adapter = new CardViewAdapter(getSupportFragmentManager(), deck.getIds());
        viewPager.setAdapter(adapter);
        final int extraCurrentItem = getIntent().getIntExtra(EXTRA_POS_SELECTED, -1);
        if (extraCurrentItem != -1) {
            viewPager.setCurrentItem(extraCurrentItem);
        }
        viewPager.setOffscreenPageLimit(2);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateActionBar(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (Configs.isShowingAds()) {
            AdManager.loadAds(this, R.id.adView);
            AdManager.add((AdView) findViewById(R.id.adView), AdSize.SMART_BANNER,
                    AdManager.BANNER_CARD_VIEW_UNIT);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_card_list_view, menu);
        changeQtyItem = menu.findItem(R.id.card_qty);
        int pos = getIntent().getIntExtra(EXTRA_POS_SELECTED, -1);
        if (pos >= 0) {
            updateActionBar(pos);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.card_qty:
                changeCardQuantity();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void updateActionBar(int position) {
        changeQtyItem.setTitle(String.valueOf(deck.getQuantities()[position]));
    }

    protected void changeCardQuantity() {
        final int pos = viewPager.getCurrentItem();
        final int cardId = deck.getIds()[pos];
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_card_qty);
        dialog.setTitle(R.string.edit);

        final TextView qtyView = (TextView) dialog.findViewById(R.id.qty);
        qtyView.setText(String.valueOf(deck.getQuantities()[pos]));

        ImageButton lessButton = (ImageButton) dialog.findViewById(R.id.qty_less);
        ImageButton moreButton = (ImageButton) dialog.findViewById(R.id.qty_more);
        Button cancelButton = (Button) dialog.findViewById(R.id.button_cancel);
        Button okButton = (Button) dialog.findViewById(R.id.button_ok);
        Button deleteButton = (Button) dialog.findViewById(R.id.button_delete);

        lessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt(qtyView.getText().toString()) - 1;
                qtyView.setText(String.valueOf(Math.max(qty, 0)));
            }
        });

        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qtyView.setText(String.valueOf(Integer.parseInt(qtyView.getText().toString()) + 1));
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt(qtyView.getText().toString());
                deck.addCard(cardId, qty);
                deck.save();
                dialog.dismiss();
                if (qty > 0) {
                    updateActionBar(pos);
                } else {
                    finish();
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deck.removeAllOfCard(cardId);
                deck.save();
                dialog.dismiss();
                finish();
            }
        });

        dialog.show();
    }

    public static void open(long deckId, int position, FragmentActivity activity) {
        Intent intent = new Intent(activity, CardInListActivity.class);
        intent.putExtra(CardInListActivity.EXTRA_DECK_ID, deckId);
        intent.putExtra(CardInListActivity.EXTRA_POS_SELECTED, position);
        activity.startActivity(intent);
    }

}
