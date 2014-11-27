package com.lovebridge.chat.view.tabs;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

public class MessageMutationEvents
{
    public interface Listener
    {
        void onMessageMutation(Uri arg1);
    }

    private static Listener listener;
    private static Handler mainThreadHandler;

    static
    {
        MessageMutationEvents.mainThreadHandler = new Handler(Looper.getMainLooper());
    }

    public MessageMutationEvents()
    {
        super();
    }

    public static void broadcast(final Uri uri)
    {
        MessageMutationEvents.mainThreadHandler.post(new Runnable()
        {
            public void run()
            {
                if (MessageMutationEvents.listener != null)
                {
                    MessageMutationEvents.listener.onMessageMutation(uri);
                }
            }
        });
    }

    public static void clearListener()
    {
        MessageMutationEvents.listener = null;
    }

    public static void setListener(Listener newListener)
    {
        MessageMutationEvents.listener = newListener;
    }

    static Listener access$0()
    {
        return MessageMutationEvents.listener;
    }
}
