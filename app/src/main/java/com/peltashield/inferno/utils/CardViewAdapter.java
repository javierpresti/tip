package com.peltashield.inferno.utils;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.peltashield.inferno.Configs;
import com.peltashield.inferno.ImageActivity;
import com.peltashield.inferno.R;
import com.peltashield.inferno.cardsDB.CardCode;
import com.peltashield.inferno.cardsDB.CardDetails;
import com.peltashield.inferno.cardsDB.CardField;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by javier on 7/21/15.
 */
public class CardViewAdapter extends FragmentStatePagerAdapter {

    private int[] cardIds;

    public CardViewAdapter(FragmentManager fm, int[] cardIds) {
        super(fm);
        this.cardIds = cardIds;
    }

    public void changeCardQuantity() {

    }

    @Override
    public Fragment getItem(int position) {
        return CardImageFragment.newInstance(cardIds[position]);
    }

    @Override
    public int getCount() {
        return cardIds.length;
    }

    public static class CardImageFragment extends Fragment {

        public static final String CARD_ID = "cardId";

        public static CardImageFragment newInstance(int cardId) {
            CardImageFragment fragment = new CardImageFragment();
            Bundle args = new Bundle();
            args.putInt(CARD_ID, cardId);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final int cardId = getArguments().getInt(CARD_ID);
            final View rootView = inflater.inflate(R.layout.card, container, false);
            final ImageView imageView = (ImageView) rootView.findViewById(android.R.id.icon1);
            ((ImageActivity) getActivity()).displayCardImage(cardId, imageView, false, false);

            final TextView artistView = (TextView) rootView.findViewById(R.id.artist_view);

            new AsyncTask<Void, Void, CardDetails>() {
                @Override
                protected CardDetails doInBackground(Void... voids) {
                    return ((ImageActivity) getActivity()).getDB().getCardDetails(cardId);
                }

                @Override
                protected void onPostExecute(CardDetails details) {
                    if (getActivity() != null) {
                        setFields(rootView, details, imageView);
                        setTextHints(rootView, details.getText());
                        setEditionsText(rootView, details, imageView, artistView);
                        addEditionsButton(rootView, imageView, details, artistView);
                        setFlipToCard(imageView, details, cardId);
                        addSharing(imageView, details.getIds());
                        setTextAdditionals(rootView, details);
                    }
                }
            }.execute();

            final ScrollView sv = (ScrollView) rootView.findViewById(R.id.scrollView);
            if (Configs.isPortrait(getResources()) && Configs.isShowingAds()) {
                DisplayMetrics metrics = rootView.getResources().getDisplayMetrics();
                if ((double) metrics.heightPixels / metrics.widthPixels <
                        Configs.getScrollingVerticalRelative()) {
                    sv.post(new Runnable() {
                        @Override
                        public void run() {
                            sv.scrollBy(0, 40);
                        }
                    });
                }
            }
            Utils.setAdBannerPadding(getActivity(),
                    (RelativeLayout) rootView.findViewById(R.id.layout));

            return rootView;
        }

        protected void setTextAdditionals(View rootView, CardDetails details) {
            String[] fields = {getString(R.string.errata), getString(R.string.limited_in),
                    getString(R.string.banned_in), getString(R.string.rules)};
            String[] values = {details.getErrata(), details.getLimited(), details.getBanned(),
                    details.getRules()};
            for(int i = 0; i < values.length; i++) {
                if (values[i] != null && !values[i].equals("")) {
                    TextView textView = new TextView(getActivity());
                    textView.setText(fields[i] + ": " + unescape(values[i]));
                    float size = getResources().getDimension(R.dimen.text_size_small);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
                    textView.setTextColor(Color.WHITE);

                    LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.additionals);
                    layout.addView(textView);
                }
            }
        }

        protected void addSharing(final ImageView imageView, final int[] cardIds) {
            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    String card = CardField.getCardImageName(cardIds[getReeditTag(imageView)],
                            false, isFlipped(imageView));
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_STREAM, CustomAPEZProvider.buildUri(card));
                    intent.setType("image/jpg");
                    startActivity(Intent.createChooser(intent,
                            getResources().getText(R.string.share_with)));
                    return true;
                }
            });
        }

        protected void setTextHints(View rootView, String text) {
            String[] texts = unescape(text).split("#");
            StringBuilder sb = new StringBuilder();
            for (String s : texts) {
                sb.append(s);
            }
            int l = 0;
            String newText = sb.toString();
            SpannableString ss = new SpannableString(newText);
            for (int i = 1; i < texts.length; i+=2) {
                l += texts[i-1].length();
                final String ability = texts[i];
                final String description = CardCommons.getAbilityDescription(ability);
                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        showAbilityDescription(ability, description);
                    }
                };
                int abLength = ability.length();
                ss.setSpan(clickableSpan, l, l + abLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                l += abLength;
            }
            for (int i = 0; i < newText.length(); i++) {
                i = newText.indexOf('{', i) + 1;
                if (i == 0) {
                    break;
                }
                for (CardCode cardCode : CardCode.values()) {
                    String code = cardCode.getCode();
                    int j = i + code.length();
                    if (newText.substring(i, j).equals(code)) {
                        ImageSpan is = new ImageSpan(getActivity(), cardCode.getDrawableId());
                        ss.setSpan(is, i - 1, j + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        i = j + 1;
                        break;
                    }
                }
            }
            TextView textView = (TextView) rootView.findViewById(R.id.text_view);
            textView.setText(ss);
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        }

        protected void showAbilityDescription(String ability, String description) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(ability);
            builder.setMessage(description);
            builder.show();
        }

        protected void setFlipToCard(final ImageView imageView, final CardDetails details,
                                     final int imageId) {
            try {
                InputStream is = ((ImageActivity) getActivity())
                        .getCardImageStream(imageId, true);
                if (is != null) { //flip card image exists
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            boolean isFlipped = isFlipped(imageView);
                            int cardId = details.getIds().length == 1 ? imageId :
                                    details.getIds()[getReeditTag(imageView)];
                            setFlipped(imageView, !isFlipped);
                            ((ImageActivity) getActivity())
                                    .displayCardImage(cardId, imageView, !isFlipped, false);
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        protected void addEditionsButton(View rootView, final ImageView imageView,
                                         final CardDetails details, final TextView artistView) {
            final int qtyEditions = details.getIds().length;
            if (qtyEditions > 1) {
                Button flipButton = new Button(getActivity());
                flipButton.setBackgroundResource(android.R.color.transparent);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                params.addRule(RelativeLayout.ALIGN_BOTTOM, android.R.id.icon1);
                params.addRule(RelativeLayout.ALIGN_RIGHT, android.R.id.icon1);
                params.addRule(RelativeLayout.ALIGN_LEFT, android.R.id.icon1);
                flipButton.setLayoutParams(params);
                DisplayMetrics displayMetrics = rootView.getResources().getDisplayMetrics();
                int height = (int) (displayMetrics.heightPixels * 0.07);
                flipButton.setMinimumHeight(height);
                flipButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int nextEd = (getReeditTag(imageView) - 1 + qtyEditions) % qtyEditions;
                        setImage(imageView, artistView, details, nextEd);
                    }
                });
                RelativeLayout layout = (RelativeLayout) rootView.findViewById(R.id.layout);
                layout.addView(flipButton);
            }
        }

        protected void setFields(View rootView, CardDetails details, ImageView imageView) {
            int qtyEditions = details.getIds().length;
            int position = qtyEditions - 1;
            while (position > 0 && getArguments().getInt(CARD_ID) != details.getIds()[position]) {
                position--;
            }
            setReeditTag(imageView, position);
            setFlipped(imageView, false);

            TextView nameView = (TextView) rootView.findViewById(R.id.name_view);
            TextView typeView = (TextView) rootView.findViewById(R.id.type_view);
            TextView costView = (TextView) rootView.findViewById(R.id.cost_view);
            TextView strView = (TextView) rootView.findViewById(R.id.str_view);
            TextView resView = (TextView) rootView.findViewById(R.id.res_view);
            ImageView pathView = (ImageView) rootView.findViewById(R.id.path_view);
            TextView legendView = (TextView) rootView.findViewById(R.id.legend_view);
            TextView artistView = (TextView) rootView.findViewById(R.id.artist_view);
            TextView formatsView = (TextView) rootView.findViewById(R.id.formats_view);
            nameView.setText(details.getName());
            typeView.setText(details.getType());
            costView.setText(details.getCost());
            strView.setText(details.getStr());
            resView.setText(details.getRes());
            pathView.setImageResource(CardField.getPathId(details.getPath()));
            legendView.setText(unescape(details.getLegend()));
            formatsView.setText(getString(R.string.formats) + ": " + details.getFormat());
            String artist = details.getArtists()[position];
            artistView.setText(getArtText() + artist);
        }

        protected String unescape(String text) {
            return text.replaceAll("\\\\n", "\\\n");
        }

        protected void setEditionsText(View rootView, final CardDetails details,
                                       final ImageView imageView, final TextView artistView) {
            StringBuilder sb = new StringBuilder();
            sb.append(details.getEditions()[0]);
            String editionSeparator = " - ";
            for (int i = 1; i < details.getEditions().length; i++) {
                sb.append(editionSeparator);
                sb.append(details.getEditions()[i]);
            }
            SpannableString ss = new SpannableString(sb.toString());
            int pos = 0;
            for (int i = 0; i < details.getEditions().length; i++) {
                String edition = details.getEditions()[i];
                final int detailN = i;
                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        setImage(imageView, artistView, details, detailN);
                    }
                };
                ss.setSpan(clickableSpan, pos, pos + edition.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ss.setSpan(new ForegroundColorSpan(
                                CardCommons.getRarityColor(details.getRarities()[i])), pos,
                        pos + edition.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                pos += edition.length() + editionSeparator.length();
            }

            TextView editionView = (TextView) rootView.findViewById(R.id.edition_view);
            editionView.setText(ss);
            editionView.setMovementMethod(LinkMovementMethod.getInstance());
            editionView.setSaveEnabled(false);
        }

        protected void setImage(ImageView imageView, TextView artistView, CardDetails details,
                                int cardPos) {
            ((ImageActivity) getActivity()).displayCardImage(details.getIds()[cardPos],
                    imageView, isFlipped(imageView), false);
            setReeditTag(imageView, cardPos);
            artistView.setText(getArtText() + details.getArtists()[cardPos]);
        }

        protected boolean isFlipped(ImageView imageView) {
            return (boolean) imageView.getTag();
        }

        protected void setFlipped(ImageView imageView, boolean isFlipped) {
            imageView.setTag(isFlipped);
        }

        protected int getReeditTag(ImageView imageView) {
            return (int) imageView.getTag(getReeditTagId());
        }

        protected int getReeditTagId() {
            return R.id.reedit;
        }

        protected void setReeditTag(ImageView imageView, int reeditPos) {
            imageView.setTag(getReeditTagId(), reeditPos);
        }

        protected String getArtText() {
            return getString(R.string.art) + ": ";
        }

        protected ImageButton createButtonOver(int buttonDrawableId, View rootView, int overViewId,
                                               double xRelative, double yRelative) {
            ImageButton flipButton = new ImageButton(getActivity());
            flipButton.setImageResource(buttonDrawableId);
            flipButton.setBackgroundResource(R.drawable.round_button);
            flipButton.setPadding(0, 0, 0, 0);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            flipButton.setLayoutParams(params);
            DisplayMetrics displayMetrics = rootView.getResources().getDisplayMetrics();
            int width = (int) (displayMetrics.widthPixels * xRelative);
            int height = (int) (displayMetrics.heightPixels * yRelative);

            params.setMargins(0, 0, width, height);
            params.addRule(RelativeLayout.ALIGN_BOTTOM, overViewId);
            params.addRule(RelativeLayout.ALIGN_RIGHT, overViewId);
            flipButton.setLayoutParams(params);
            return flipButton;
        }

    }
}
