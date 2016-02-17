package com.peltashield.inferno.utils;

import android.net.Uri;

import com.android.vending.expansion.zipfile.APEZProvider;

import java.io.File;

/**
 * Created by javier on 6/4/15.
 */
public class CustomAPEZProvider extends APEZProvider {

    private static final String AUTHORITY = "com.peltashield.inferno.provider";

    @Override
    public String getAuthority() {
        return AUTHORITY;
    }

    public static Uri buildUri(String path) {
        StringBuilder contentPath = new StringBuilder("content://");

        contentPath.append(AUTHORITY);
        contentPath.append(File.separator);
        contentPath.append(path);

        return Uri.parse(contentPath.toString());
    }
}
