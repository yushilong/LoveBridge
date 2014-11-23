
package com.lovebridge.chat.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.SpannableString;
import android.util.SparseIntArray;

import com.lovebridge.R;
import com.lovebridge.chat.view.ImageUtils;

public class EmojiUtils {
    class EmojiBitmapKey {
        private final int codePoint;
        private final Size size;

        public EmojiBitmapKey(int codePoint, Size size) {
            super();
            this.codePoint = codePoint;
            this.size = size;
        }

        public boolean equals(Object obj) {
            boolean bool = false;
            if (obj != null) {
                if ((((EmojiBitmapKey)obj)) == this) {
                    bool = true;
                } else if ((obj instanceof EmojiBitmapKey)) {
                    Object object = obj;
                    if (this.codePoint == ((EmojiBitmapKey)object).codePoint
                                    && this.size == ((EmojiBitmapKey)object).size) {
                        bool = true;
                    }
                }
            }

            return bool;
        }
    }

    public enum Size {
        SMALL("SMALL", 0, 0xE), NORMAL("NORMAL", 1, 0x15), PICKER("PICKER", 2, 0x23), FULL("FULL", 3, 0);
        private final int value;

        private Size(String arg1, int arg2, int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    private static SparseIntArray EMOJIS = null;
    private static Map<String, List> EMOJI_BY_CATEGORY = null;
    private static final int EMOJI_NOT_FOUND = 0;
    private static final int EMOJI_SINGLE_CODEPOINT = 0xFFFFFFFF;
    private static BitmapLruCache cache;
    private static Context context;

    static {
        EmojiUtils.EMOJIS = new SparseIntArray();
        EmojiUtils.EMOJI_BY_CATEGORY = new HashMap<String, List>();
    }

    public EmojiUtils() {
        super();
    }

    public static String convertCodePointToString(int codePoint) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.appendCodePoint(codePoint);
        int i = EmojiUtils.EMOJIS.get(codePoint);
        if (i != 0 && i != 0xFFFFFFFF) {
            stringBuilder.appendCodePoint(i);
        }

        return stringBuilder.toString();
    }

    public static SpannableString emojify(String s, Size size) {
        SpannableString spannablestring;
        if (s == null) {
            spannablestring = null;
        } else {
            spannablestring = new SpannableString(s);
            int i = 0;
            int k = 0;
            while (i < s.length()) {
                int j = s.codePointAt(i);
                k = Character.charCount(j);
                int l = EMOJIS.get(j);
                if (l != 0) {
                    if (l != -1) {
                        if (i + k >= s.length() || l != s.codePointAt(i + k)) {
                            k += Character.charCount(l);
                        }
                    }
                    spannablestring.setSpan(new EmojiSpan(context, getBitmap(j, size), size), i, i + k, 33);
                }
                i += k;
            }
        }
        return spannablestring;
    }

    public static Bitmap getBitmap(int codePoint, Size size) {
        if (size == Size.FULL) {
            throw new IllegalArgumentException("Full is not a valid size");
        }

        EmojiBitmapKey emojiUtils = new EmojiUtils().new EmojiBitmapKey(codePoint, size);
        Bitmap bitmap = (Bitmap)EmojiUtils.cache.get(emojiUtils);
        if (bitmap == null) {
            bitmap = EmojiUtils.getFullBitmap(codePoint);
            int i = ImageUtils.dpToPx((double)size.getValue());
            if (bitmap != null) {
                if (i != bitmap.getWidth()) {
                    bitmap = Bitmap.createScaledBitmap(bitmap, i, i, true);
                }
                EmojiUtils.cache.put(emojiUtils, bitmap);
            }

        }
        return bitmap;
    }

    public static Bitmap getCachedBitmap(int codePoint, Size size) {
        return (Bitmap)EmojiUtils.cache.get(new EmojiUtils().new EmojiBitmapKey(codePoint, size));
    }

    public static List getEmojiByCategory(String categoryName) {
        return (List)EmojiUtils.EMOJI_BY_CATEGORY.get(categoryName);
    }

    public static List getEmojiRecent() {
        ArrayList arrayList = new ArrayList(EmojiUtils.getRecentEmojiPreferences().getAll().entrySet());
        Collections.sort(((List)arrayList), new Comparator() {
            public int compare(Object arg2, Object arg3) {
                return this.compare(((Map.Entry)arg2), ((Map.Entry)arg3));
            }

            public int compare(Map.Entry arg3, Map.Entry arg4) {
                return ((Enum<Size>)arg4.getValue()).compareTo((Size)arg3.getValue());
            }
        });
        ArrayList arrayList1 = new ArrayList();
        Iterator iterator = ((List)arrayList).iterator();
        while (iterator.hasNext()) {
            // ((List)arrayList1).add(Integer.valueOf(iterator.next().getKey()));
        }

        return arrayList1;
    }

    public static int getEmojiRecentSize() {
        return EmojiUtils.getRecentEmojiPreferences().getAll().size();
    }

    public static boolean init(Context context) {
        XmlPullParser parser = null;
        EmojiUtils.context = context;
        EmojiUtils.cache = new BitmapLruCache(0.125);
        InputStream inputStream = context.getResources().openRawResource(R.raw.emojis);
        try {
            parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(new InputStreamReader(inputStream));
            parser.require(0, null, null);
        } catch (Throwable throwable) {
        }
        List<Integer> list = null;
        try {
            String s = null;
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String name = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:

                        if (name.equals("category")) {
                            s = parser.getAttributeValue(null, "name");
                            list = new ArrayList<Integer>();
                            eventType = parser.next();
                        } else if (!name.equals("e")) {
                            eventType = parser.next();
                        } else {
                            Integer integer = Integer.valueOf(Integer.parseInt(parser.getAttributeValue(null, "cp")));
                            String string2 = parser.getAttributeValue(null, "cp2");
                            SparseIntArray sparseIntArray = EmojiUtils.EMOJIS;
                            int i1 = integer.intValue();
                            int i2 = string2 != null ? Integer.parseInt(string2) : -1;
                            try {
                                sparseIntArray.put(i1, i2);
                                list.add(integer);
                            } catch (Throwable throwable) {

                            }
                        }

                        break;
                    case XmlPullParser.END_TAG:
                        if (!name.equals("category")) {
                            eventType = parser.next();
                        } else {
                            EMOJI_BY_CATEGORY.put(s, list);
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e1) {
            e1.printStackTrace();
            return false;
        } catch (IOException e1) {
            e1.printStackTrace();
            return false;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static void updateRecentEmoji(Integer codePoint) {
        SharedPreferences.Editor sharedPreferences = EmojiUtils.getRecentEmojiPreferences().edit();
        sharedPreferences.putLong(codePoint.toString(), System.currentTimeMillis());
        sharedPreferences.apply();
    }

    private static Bitmap getFullBitmap(int codePoint) {
        InputStream inputStream = null;
        Bitmap bitmap = EmojiUtils.getCachedBitmap(codePoint, Size.FULL);
        if (bitmap == null) {
            try {
                inputStream = EmojiUtils.context.getAssets().open("emoji/emj_" + codePoint + ".png");
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (Throwable throwable) {
                IOUtils.closeQuietly(inputStream);
            }

            IOUtils.closeQuietly(inputStream);
        }

        return bitmap;
    }

    private static SharedPreferences getRecentEmojiPreferences() {
        return EmojiUtils.context.getSharedPreferences("emoji", 0);
    }
}
