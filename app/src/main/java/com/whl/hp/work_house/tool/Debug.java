package com.whl.hp.work_house.tool;

import android.util.Log;

/**
 * Created by hp-whl on 2015/9/23.
 */
public class Debug {
    public static boolean isDebug=false;
    public static  void startDebug(String context){
        Log.d("debug",context);
    }
}
