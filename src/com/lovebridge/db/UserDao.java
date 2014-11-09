
package com.lovebridge.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.easemob.util.HanziToPinyin;
import com.lovebridge.chat.moden.ChatUser;
import com.lovebridge.library.tools.YARConstants;

public class UserDao {
    public static final String TABLE_NAME = "uers";
    public static final String COLUMN_NAME_ID = "username";
    public static final String COLUMN_NAME_NICK = "nick";
    public static final String COLUMN_NAME_IS_STRANGER = "is_stranger";

    private DbOpenHelper dbHelper;

    public UserDao(Context context) {
        dbHelper = DbOpenHelper.getInstance(context);
    }

    /**
     * 保存好友list
     * 
     * @param contactList
     */
    public void saveContactList(List<ChatUser> contactList) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(TABLE_NAME, null, null);
            for (ChatUser user : contactList) {
                ContentValues values = new ContentValues();
                values.put(COLUMN_NAME_ID, user.getUsername());
                if (user.getNick() != null)
                    values.put(COLUMN_NAME_NICK, user.getNick());
                db.replace(TABLE_NAME, null, values);
            }
        }
    }

    /**
     * 获取好友list
     * 
     * @return
     */
    public Map<String, ChatUser> getContactList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Map<String, ChatUser> users = new HashMap<String, ChatUser>();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + TABLE_NAME /*
                                                                       * +
                                                                       * " desc"
                                                                       */, null);
            while (cursor.moveToNext()) {
                String username = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ID));
                String nick = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NICK));
                ChatUser user = new ChatUser();
                user.setUsername(username);
                user.setNick(nick);
                String headerName = null;
                if (!TextUtils.isEmpty(user.getNick())) {
                    headerName = user.getNick();
                } else {
                    headerName = user.getUsername();
                }

                if (username.equals(YARConstants.NEW_FRIENDS_USERNAME) || username.equals(YARConstants.GROUP_USERNAME)) {
                    user.setHeader("");
                } else if (Character.isDigit(headerName.charAt(0))) {
                    user.setHeader("#");
                } else {
                    user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target.substring(
                                    0, 1).toUpperCase());
                    char header = user.getHeader().toLowerCase().charAt(0);
                    if (header < 'a' || header > 'z') {
                        user.setHeader("#");
                    }
                }
                users.put(username, user);
            }
            cursor.close();
        }
        return users;
    }

    /**
     * 删除一个联系人
     * 
     * @param username
     */
    public void deleteContact(String username) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(TABLE_NAME, COLUMN_NAME_ID + " = ?", new String[] { username });
        }
    }

    /**
     * 保存一个联系人
     * 
     * @param user
     */
    public void saveContact(ChatUser user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_ID, user.getUsername());
        if (user.getNick() != null)
            values.put(COLUMN_NAME_NICK, user.getNick());
        if (db.isOpen()) {
            db.replace(TABLE_NAME, null, values);
        }
    }

}
