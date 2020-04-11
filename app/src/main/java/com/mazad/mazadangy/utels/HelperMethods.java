package com.mazad.mazadangy.utels;

/**
 * Created by A.taher on 4/8/2018.
 */

import android.app.Activity;
import android.app.ProgressDialog;

public class HelperMethods {


    public static ProgressDialog blg ;

    public static void showDialog(Activity currentActivity, String title, String msg) {
        blg = new ProgressDialog(currentActivity);
        blg.setTitle(title);
        blg.setCanceledOnTouchOutside(false);
        blg.setMessage(msg);
        blg.setCancelable(false);
        blg.show();

    }



    public static void hideDialog(Activity currentActivity) {
        blg.cancel();
    }
}
