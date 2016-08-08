package per.yrj.movietime.utils;

import android.util.Log;

/**
 * Created by YiRenjie on 2016/6/1.
 */
public class Debug{
    public static final boolean IS_DEBUG = true;

    public static void i(String msg){
        if (IS_DEBUG)
            Log.i("TAG", msg);
    }

    public static void e(String msg){
        if (IS_DEBUG)
            Log.e("TAG", msg);
    }

}
