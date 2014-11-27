package com.lovebridge.chat.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

public class SoundUtils
{
    public SoundUtils()
    {
        super();
    }

    public static Uri getSoundUri(Context context, int soundId)
    {
        return Uri.parse("android.resource://" + context.getPackageName() + "/raw/" + soundId);
    }

    public static void playSound(Context context, int soundId)
    {
        Ringtone ringtone = RingtoneManager.getRingtone(context, SoundUtils.getSoundUri(context, soundId));
        if (ringtone != null)
        {
            ringtone.play();
        }
    }

    public static boolean shouldPlayChatSounds(Context context)
    {
        Object object = context.getSystemService("audio");
        boolean bool = !Settings.soundChat.getValue(context) || (((AudioManager) object).isMusicActive()) ? false : true;
        return bool;
    }
}