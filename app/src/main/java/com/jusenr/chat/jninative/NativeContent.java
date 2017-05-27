package com.jusenr.chat.jninative;

/**
 * Created by T5-Jusenr on 2017/4/3.
 */

public class NativeContent {

    // Used to load the 'native-lib' library on application startup.
    static {
//        System.loadLibrary("native-lib");
        System.loadLibrary("zbar");
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
//    public native String stringFromJNI();

    public native String decode(byte[] data, int width, int height, boolean isCrop, int x, int y, int cwidth, int cheight);

}
