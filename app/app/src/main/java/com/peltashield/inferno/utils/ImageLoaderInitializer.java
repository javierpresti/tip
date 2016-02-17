package com.peltashield.inferno.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;
import com.peltashield.inferno.R;
import com.peltashield.inferno.cardsDB.CardDetails;
import com.peltashield.inferno.utils.imageLoader.StreamImageDownloader;

import java.io.InputStream;

/**
 * Created by javier on 1/14/15.
 */
public class ImageLoaderInitializer {

    private ImageLoaderInitializer() {
    }

    public static void init(Context context) {
        ImageLoader iL = ImageLoader.getInstance();
        if (! iL.isInited()) {
            DisplayImageOptions options = getOptions();

            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                    .defaultDisplayImageOptions(options)
                    .imageDownloader(new StreamImageDownloader(context))
                    .build();
            iL.init(config);
            iL.clearMemoryCache();
            iL.clearDiskCache();
        }
    }

    public static ImageLoader getInstance(Context context) {
        ImageLoaderInitializer.init(context);
        return ImageLoader.getInstance();
    }

    public static DisplayImageOptions getOptions(InputStream is, boolean showImageOnLoading) {
        return showImageOnLoading ? getStreamOptions(is) : getStreamOptionsWithNoLoadingImage(is);
    }

    public static DisplayImageOptions getOptions(InputStream is, int cardId, boolean isPortrait,
                                                 boolean thumb) {
        return thumb ? getThumbCircleImageOptions(is, cardId, isPortrait) :
                getStreamPreprocessorOptionsWithNoLoadingImage(is, cardId, isPortrait);
    }

    protected static DisplayImageOptions getOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageOnLoading(R.drawable.zt_back)
                .build();
        return options;
    }

    protected static DisplayImageOptions getStreamOptions(InputStream is) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageOnLoading(R.drawable.zt_back)
                .extraForDownloader(is)
                .build();
        return options;
    }

    protected static DisplayImageOptions getStreamOptionsWithNoLoadingImage(InputStream is) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .extraForDownloader(is)
                .build();
        return options;
    }

    protected static DisplayImageOptions getThumbCircleImageOptions(
            InputStream is, final int cardId, final boolean isPortrait) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(100))
                .showImageOnLoading(R.drawable.empty)
                .extraForDownloader(is)
                .preProcessor(getScalingProcessor(cardId, isPortrait))
                .build();
        return options;
    }

    protected static DisplayImageOptions getStreamPreprocessorOptionsWithNoLoadingImage(
            InputStream is, final int cardId, final boolean isPortrait) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .extraForDownloader(is)
                .preProcessor(getScalingProcessor(cardId, isPortrait))
                .build();
        return options;
    }

    protected static BitmapProcessor getScalingProcessor(
            final int cardId, final boolean isPortrait) {
        return new BitmapProcessor() {
            @Override
            public Bitmap process(Bitmap bitmap) {
                Bitmap newBitMap;
                int[] scales = CardDetails.getScales(cardId, isPortrait,
                        bitmap.getWidth(), bitmap.getHeight());
                if (!isPortrait) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    newBitMap = Bitmap.createBitmap(bitmap,
                            scales[0], scales[1], scales[2], scales[3], matrix, true);
                } else {
                    newBitMap = Bitmap.createBitmap(bitmap,
                            scales[0], scales[1], scales[2], scales[3]);
                }

                return newBitMap;
            }
        };
    }
}
