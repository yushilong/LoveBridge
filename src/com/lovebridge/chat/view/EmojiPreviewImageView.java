package com.lovebridge.chat.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.lovebridge.chat.utils.CancelableAsyncTask;
import com.lovebridge.chat.utils.EmojiUtils;
import com.lovebridge.chat.utils.EmojiUtils.Size;

public class EmojiPreviewImageView extends ImageView {
    private volatile CancelableAsyncTask task;

    public EmojiPreviewImageView(Context context) {
        super(context);
    }

    public EmojiPreviewImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmojiPreviewImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setEmojiCodePoint(final Integer codePoint) {

        if (task != null) {
            task.cancel(true);
            task = null;
        }
        Bitmap bitmap = EmojiUtils.getCachedBitmap(codePoint.intValue(), Size.PICKER);
        this.setImageBitmap(bitmap);
        if (bitmap == null) {
            task = (new CancelableAsyncTask() {


                protected Bitmap doInBackground(Void avoid[]) {
                    return EmojiUtils.getBitmap(codePoint, EmojiUtils.Size.PICKER);
                }

                protected Object doInBackground(Object aobj[]) {
                    return doInBackground((Void[]) aobj);
                }

                protected void onPostExecute(Bitmap bitmap1) {
                    if (bitmap1 != null) {
                        setImageBitmap(bitmap1);
                    }
                }

                protected void onPostExecute(Object obj) {
                    onPostExecute((Bitmap) obj);
                }


            }).execute(new Void[0]);
        }
    }
}
