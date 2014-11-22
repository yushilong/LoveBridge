
package com.lovebridge.chat.utils;

import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;

import com.lovebridge.chat.utils.EmojiUtils.Size;

public class EmojiFilter implements InputFilter {
    public EmojiFilter() {
        super();
    }

    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        SpannableString spannableString = EmojiUtils.emojify(source.toString(), Size.NORMAL);
        if ((source instanceof Spanned)) {
            TextUtils.copySpansFrom((Spanned)source, start, end, null, ((Spannable)spannableString), 0);
        }

        return ((CharSequence)spannableString);
    }
}