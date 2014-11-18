
package com.lovebridge.chat.view.tabs;

import android.content.Context;

public abstract class Message {
    public final boolean failed;
    public final long id;
    public final int messageType;
    public final long threadId;
    public final long time;
    private int unreadCount;

    protected Message(long id, int messageType, boolean read, long time, long threadId) {
        super();
        this.unreadCount = 0;
        this.id = id;
        this.messageType = messageType;
        this.time = time;
        this.threadId = threadId;
        boolean bool = false;
        if (messageType == 5) {
            bool = true;
        }
        this.failed = bool;
    }

    public abstract void delete(Context arg1);

    public abstract Addresses getAddresses();

    public abstract String getBody();

    public abstract String getDetailsPreview(Context arg1);

    public abstract Address getSenderAddress();

    public int getUnreadCount() {
        return this.unreadCount;
    }

    public abstract boolean hasImageAttachment();

    public abstract boolean hasOtherAttachment();

    public boolean isMediaSms() {
        return false;
    }

    public boolean isPartiallyLoaded() {
        return false;
    }

    public boolean isSent() {
        boolean bool = this.getSenderAddress() == null ? true : false;
        return bool;
    }

    public boolean isTap() {
        return false;
    }

    public abstract void resend(Context arg1);

    public void setUnreadCount(int count) {
        this.unreadCount = count;
    }

    public void showFailureOptions(final Context context, boolean allowRetry) {
        // 删除
    }
}
