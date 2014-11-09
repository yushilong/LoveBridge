
package com.lovebridge.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.easemob.chat.EMChatManager;

public class VoiceCallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!intent.getAction().equals(EMChatManager.getInstance().getIncomingVoiceCallBroadcastAction()))
            return;

    }

}
