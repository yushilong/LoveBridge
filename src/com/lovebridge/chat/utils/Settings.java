
package com.lovebridge.chat.utils;

import com.lovebridge.R;

public class Settings {
    private static String DEFAULT_NOTIFICATION_SOUND = "content://settings/system/notification_sound";
    public static SettingBoolean defaultSmsMmsClient;
    public static SettingBoolean displayNotification;
    public static SettingBoolean inviteReminders;
    public static SettingBoolean mediaSmsSend;
    public static SettingString nightMode;
    public static SettingBoolean popoverNotification;
    public static SettingBoolean readReceipts;
    public static SettingString soundNotification;
    public static SettingBoolean vibrate;
    public static SettingBoolean soundChat;
    public static SettingBoolean ott;

    static {
        Settings.defaultSmsMmsClient = new SettingBoolean(R.string.pref_default_sms_mms_client_key, true);
        Settings.soundChat = new SettingBoolean(R.string.pref_sound_chat_key, true);
        Settings.displayNotification = new SettingBoolean(R.string.pref_display_notification_key, true);
        Settings.soundNotification = new SettingString(R.string.pref_sound_notification_key, DEFAULT_NOTIFICATION_SOUND);
        Settings.vibrate = new SettingBoolean(R.string.pref_vibrate_key, true);
        Settings.popoverNotification = new SettingBoolean(R.string.pref_popover_notification_key, true);
        Settings.nightMode = new SettingString(R.string.pref_nightmode_key, "auto");
        Settings.inviteReminders = new SettingBoolean(R.string.pref_invite_reminders_key, true);
        Settings.mediaSmsSend = new SettingBoolean(R.string.pref_media_sms_send_key, false);
        Settings.ott = new SettingBoolean(R.string.pref_ott_key, false);
        Settings.readReceipts = new SettingBoolean(R.string.pref_read_receipts_key, true);
    }

    public Settings() {
        super();
    }
}