package com.peltashield.inferno;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.peltashield.inferno.cardsDB.CardField;
import com.peltashield.inferno.model.deck.Deck;
import com.peltashield.inferno.utils.AdManager;
import com.peltashield.inferno.utils.CardViewAdapter;
import com.peltashield.inferno.utils.CustomAPEZProvider;

import java.util.List;

public class CardViewActivity extends ImageActivity {

    public static final String EXTRA_FILTERED_CARDS = "com.peltashield.inferno.FILTERED_CARDS";
    public static final String EXTRA_POS_SELECTED = "com.peltashield.inferno.POS_SELECTED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_view);

        setNavigationDrawer();

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new CardViewAdapter(getSupportFragmentManager(),
                getIntent().getIntArrayExtra(EXTRA_FILTERED_CARDS)));
        final int extraCurrentItem = getIntent().getIntExtra(EXTRA_POS_SELECTED, -1);
        if (extraCurrentItem != -1) {
            viewPager.setCurrentItem(extraCurrentItem);
        }
        viewPager.setOffscreenPageLimit(2);

        if (Configs.isShowingAds()) {
            AdManager.loadAds(this, R.id.adView);
            AdManager.add((AdView) findViewById(R.id.adView), AdSize.SMART_BANNER,
                    AdManager.BANNER_CARD_VIEW_UNIT);
        }

        hideActionBar();
    }

    protected void setNavigationDrawer() {
        ListView drawerList = (ListView) findViewById(R.id.left_drawer);

        drawerList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.options_card_view)));

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int[] cardIds = getIntent().getIntArrayExtra(EXTRA_FILTERED_CARDS);
                ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
                int[] cIds = getDB().getCardDetails(cardIds[viewPager.getCurrentItem()]).getIds();
                ImageView imageView = (ImageView) viewPager.findViewById(android.R.id.icon1);
                final int cardId = cIds[(int) imageView.getTag(R.id.reedit)];//cardIds[viewPager.getCurrentItem()];
                switch (position) {
                    case 0:
                        boolean isFlipped = (boolean) imageView.getTag();
                        String card = CardField.getCardImageName(cardId, false, isFlipped);
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_STREAM, CustomAPEZProvider.buildUri(card));
                        intent.setType("image/jpg");
                        startActivity(Intent.createChooser(intent,
                                getResources().getText(R.string.share_with)));
                        break;
                    case 1:
                        AlertDialog.Builder builder = new AlertDialog.Builder(CardViewActivity.this);
                        builder.setTitle(R.string.add_to);
                        List<Deck> decks = Deck.listAll(Deck.class);
                        CharSequence[] deckNames = new CharSequence[decks.size()];
                        for (int i = 0; i < decks.size(); i++) {
                            deckNames[i] = decks.get(i).getName();
                        }
                        builder.setItems(deckNames, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Deck deck = Deck.listAll(Deck.class).get(which);
                                deck.addCard(cardId);
                                deck.save();
                                Toast.makeText(CardViewActivity.this, R.string.card_added,
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                        builder.show();
                        break;
                }
            }
        });
    }

    public static void open(int[] filteredCards, int position, FragmentActivity activity) {
        Intent intent = new Intent(activity, CardViewActivity.class);
        intent.putExtra(CardViewActivity.EXTRA_FILTERED_CARDS, filteredCards);
        intent.putExtra(CardViewActivity.EXTRA_POS_SELECTED, position);
        activity.startActivity(intent);
    }

}
