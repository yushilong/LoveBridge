package com.lovebridge.chat.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class MMSSMSUtils
{
    public static final int MAX_IMAGE_HEIGHT = 1200;
    public static final int MAX_IMAGE_WIDTH = 1600;
    public static final int MESSAGE_TYPE_FAILED = 5;
    public static final int MESSAGE_TYPE_INBOX = 1;
    public static final int MESSAGE_TYPE_OUTBOX = 4;
    public static final int MESSAGE_TYPE_SENT = 2;
    public static final String MMS_SMS_NORMALIZED_DATE = "normalized_date";
    public static final long NEW_THREAD_ID = -1L;
    public static final long NO_THREAD_ID = 0;
    private static final String ID_PROJECTION[] = { "_id" };
    private static final Uri THREAD_ID_URI = Uri.parse("content://mms-sms/threadID");
    private static Context context;

    public MMSSMSUtils()
    {
    }

    public static long getOrCreateThreadId(String s)
    {
        return getOrCreateThreadId(new String[] { s });
    }

    public static long getOrCreateThreadId(String as[])
    {
        if (as.length < 0)
        {
            return 0;
        }
        Uri.Builder builder = THREAD_ID_URI.buildUpon();
        Cursor cursor = context.getContentResolver().query(builder.build(), ID_PROJECTION, null, null, null);
        long l = 0;
        if (cursor != null)
        {
            int i = 0;
            while (!cursor.moveToFirst())
            {
                builder.appendQueryParameter("recipient", as[i]);
                i++;
            }
            l = cursor.getLong(0);
            cursor.close();
        }
        return l;
    }
}
