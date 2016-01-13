package com.example.mixas.exoplayerwrapper;

import android.annotation.TargetApi;
import android.media.MediaDrm;
import android.os.Build;
import android.text.TextUtils;

import com.google.android.exoplayer.drm.MediaDrmCallback;
import com.google.android.exoplayer.util.Util;

import java.util.UUID;

/**
 * Created by mixas on 13.01.16.
 */
public class SimpleMediaDrmCallback implements MediaDrmCallback {
    private String vidoUrl;
    private String defaultUri;

    public SimpleMediaDrmCallback(String URL)
    {
        vidoUrl = URL;
    }
    @Override
    public byte[] executeProvisionRequest(UUID uuid, MediaDrm.ProvisionRequest provisionRequest) throws Exception {
        return Util.executePost(vidoUrl, null, null);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public byte[] executeKeyRequest(UUID uuid, MediaDrm.KeyRequest keyRequest) throws Exception {
        String url = keyRequest.getDefaultUrl();
        if (TextUtils.isEmpty(url)) {
            url = defaultUri;
        }
        return Util.executePost(url, keyRequest.getData(), null);
    }

}
