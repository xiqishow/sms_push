package io.xqs.bt.smspush.util;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class UIUtil {
    /**
     * 在当前活动隐藏键盘
     * @param activity
     */
    public static void hideSystemKeyBoard(Activity activity) {
        View current = activity.getCurrentFocus();
        if (current != null) current.clearFocus();
        IBinder windowToken = activity.findViewById(android.R.id.content).getWindowToken();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive()){
            imm.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
