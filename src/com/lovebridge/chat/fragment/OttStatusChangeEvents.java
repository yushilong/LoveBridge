// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.lovebridge.chat.fragment;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OttStatusChangeEvents
{
    private static final List listeners = new ArrayList();
    private static final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    public OttStatusChangeEvents()
    {
    }

    public static void addListener(Listener listener)
    {
        listeners.add(listener);
    }

    public static void broadcast()
    {
        mainThreadHandler.post(new Runnable()
        {
            public void run()
            {
                OttStatusChangeEvents.broadcastSync();
            }
        });
    }

    private static void broadcastSync()
    {
        Iterator iterator = listeners.iterator();
        while (iterator.hasNext())
        {
            ((Listener) iterator.next()).onStatusChanged();
        }
    }

    public static void removeListener(Listener listener)
    {
        listeners.remove(listener);
    }

    public static interface Listener
    {
        public abstract void onStatusChanged();
    }
}
