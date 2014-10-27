
package com.lovebridge.library.tools;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.lovebridge.bean.YARUser;

/**
 * @author yushilong
 * @date 2014-9-29
 * @version 1.0
 */
public class YARUserManager
{
    private YARUser YARUser;
    private boolean isLogined;
    private static YARUserManager _instance;

    /**
     * 初始化用户信息
     * 
     * @param userString
     */
    public void saveUser(String userString)
    {
        if (!TextUtils.isEmpty(userString))
        {
            isLogined = true;
            YARUser = new Gson().fromJson(userString, YARUser.class);
            // B5MPreferenceHelper.saveStringValue(MainApplication.getInstance(),
            // B5MPreferenceKeys.USERNAME, YARUser.username);
            YARCacheManager.getInstance().saveObject(userString, "");
        }
        else
        {
            throw new NullPointerException("YARUser is null");
        }
    }

    /**
     * 清除数据，例如登出操作
     */
    public void clearData()
    {
        YARUser = null;
        isLogined = false;
    }

    public YARUser getUser()
    {
        if (isLogined)
        {
            if (YARUser == null)
            {
                String userString = YARCacheManager.getInstance().readObject("");
                if (!TextUtils.isEmpty(userString))
                {
                    return new Gson().fromJson(userString, YARUser.class);
                }
            }
            else
            {
                return YARUser;
            }
        }
        return null;
    }

    public boolean isLogined()
    {
        return isLogined;
    }

    public static YARUserManager getInstance()
    {
        synchronized (YARUserManager.class)
        {
            if (_instance == null)
            {
                _instance = new YARUserManager();
            }
        }
        return _instance;
    }

    public boolean checkIsLogined(Activity activity , String classForName)
    {
        if (!isLogined)
        {
            Bundle bundle = new Bundle();
            bundle.putString("classForName", classForName);
            //此处跳转到登录界面
            return false;
        }
        return true;
    }
}
