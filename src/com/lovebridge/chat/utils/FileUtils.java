
package com.lovebridge.chat.utils;

import java.io.File;

import android.content.Context;
import android.os.Environment;

public class FileUtils {
    public FileUtils() {
        super();
    }

    public static File getCacheDir(Context context) {
        File file = Environment.getExternalStorageState().equals("mounted") ? context.getExternalCacheDir() : context
                        .getCacheDir();
        return file;
    }
}