package com.peltashield.inferno.utils;

import com.google.android.vending.expansion.downloader.impl.DownloaderService;

import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * Created by javier on 5/20/15.
 */
public class InfernoDownloaderService extends DownloaderService {

    public static final String BASE64_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AM" +
            "IIBCgKCAQEAloexOGISuGeSeBRWcsdzt69oxNXadMjmm6LY18QtpGf1bIo5oZjbyzymN2qEq" +
            "EjHQViiZSY2b0QR932Q3Flqd2lfMZHqvHw4dM7/KvsSjDfN8m4InX0sTC4IgNvpJV4p9rRWM" +
            "q7w7RcQ8BnMPqrvObH24C68ZmhCEfd9c3tmiUJoB7ZEPy//R3o5JujFq+HhmQDjQccPbxdZZ" +
            "BT281wI/kYb7KHZ7REATL0/FcxSBDkcN76Z8T70ZgAJyhC/XfEIWzYqYI50HqlPXL77iAPpQ" +
            "+HcJ18e8rn85iMORGTIwx5I4ASRniXs5tF/sX1zrmHImzoTJZ+IV1n94aX9TiITNQIDAQAB";

    //TODO change this! http://android-developers.blogspot.com.ar/2013/02/using-cryptography-to-store-credentials.html
    public static final byte[] SALT = new byte[] { 8, 42, -12, -1, 54, 98,
            -110, -12, 23, 2, -8, -4, 9, 5, -106, -107, -33, 45, -1, 83
    };

    @Override
    public String getPublicKey() {
        return BASE64_PUBLIC_KEY;
    }

    @Override
    public byte[] getSALT() {
/*        final int outputKeyLength = 256;
        SecureRandom secureRandom = new SecureRandom();
        // Do *not* seed secureRandom! Automatically seeded from system entropy.
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(outputKeyLength, secureRandom);
        SecretKey key = keyGenerator.generateKey();
        return key.getEncoded();
*/        return SALT;
    }

    @Override
    public String getAlarmReceiverClassName() {
        return InfernoAlarmReceiver.class.getName();
    }
}
