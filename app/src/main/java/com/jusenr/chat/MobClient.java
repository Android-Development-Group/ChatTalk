package com.jusenr.chat;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

/**
 * Created by Jusenr on 2017/8/16.
 */

public class MobClient {

    public static void onEvent(Context context, String eventId) {
        onEvent(context, eventId, null);
    }

    public static void onEvent(Context context, String eventId, HashMap<String, String> map) {
        if (BuildConfig.IS_TEST)
            return;
        if (map != null)
            MobclickAgent.onEvent(context, eventId, map);
        else
            MobclickAgent.onEvent(context, eventId);
    }
}
