
package com.lovebridge.chat.utils;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.ViewTreeObserver;

public class ViewTreeObserverUtils {
    public ViewTreeObserverUtils() {
        super();
    }

    @TargetApi(value = 0x10)
    public static void removeOnGlobalLayoutListener(ViewTreeObserver observer,
                                                    ViewTreeObserver.OnGlobalLayoutListener listener) {
        if (Build.VERSION.SDK_INT < 0x10) {
            observer.removeGlobalOnLayoutListener(listener);
        } else {
            observer.removeOnGlobalLayoutListener(listener);
        }
    }
}
