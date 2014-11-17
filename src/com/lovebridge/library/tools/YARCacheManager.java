package com.lovebridge.library.tools;

import android.content.Context;
import com.lovebridge.application.MainApplication;

import java.io.*;

/**
 * @author yushilong
 * @date 2014-9-30
 * @version 1.0
 */
public class YARCacheManager
{
    private Context context;
    private static final int CACHE_TIME = 60 * 60 * 1000;// 缓存失效时间
    private static YARCacheManager _instance;

    // key为用户ID+url
    public YARCacheManager()
    {
        super();
        // TODO Auto-generated constructor stub
        context = MainApplication.getInstance();
    }

    public static synchronized YARCacheManager getInstance()
    {
        if (_instance == null)
        {
            return new YARCacheManager();
        }
        return _instance;
    }

    /**
     * 保存对象
     *
     * @param ser
     * @param file
     * @throws IOException
     */
    public boolean saveObject(String object, String url)
    {
        deleteObject(getKeyFile(url));
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try
        {
            fos = context.openFileOutput(getKeyFile(url), Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            oos.flush();
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        finally
        {
            try
            {
                oos.close();
            }
            catch (Exception e)
            {
            }
            try
            {
                fos.close();
            }
            catch (Exception e)
            {
            }
        }
    }

    public String getKeyFile(String url)
    {
        return YARStringUtils.getMD5Str(getUid() + url);
    }

    /**
     * 读取对象
     *
     * @param file
     * @return
     * @throws IOException
     */
    public String readObject(String url)
    {
        url = getKeyFile(url);
        if (!isExistDataCache(url) || isCacheDataFailure(url))
        {
            return null;
        }
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try
        {
            fis = context.openFileInput(url);
            ois = new ObjectInputStream(fis);
            return (String) ois.readObject();
        }
        catch (FileNotFoundException e)
        {
        }
        catch (Exception e)
        {
            e.printStackTrace();
            // 反序列化失败 - 删除缓存文件
            if (e instanceof InvalidClassException)
            {
                File data = context.getFileStreamPath(url);
                data.delete();
            }
        }
        finally
        {
            try
            {
                ois.close();
            }
            catch (Exception e)
            {
            }
            try
            {
                fis.close();
            }
            catch (Exception e)
            {
            }
        }
        return null;
    }

    public void deleteObject(String url)
    {
        File data = context.getFileStreamPath(url);
        if (data.exists())
            data.delete();
    }

    /**
     * 判断缓存是否存在
     *
     * @param cachefile
     * @return
     */
    private boolean isExistDataCache(String url)
    {
        boolean exist = false;
        File data = context.getFileStreamPath(url);
        if (data.exists())
            exist = true;
        return exist;
    }
    /**
     * 判断缓存数据是否可读
     *
     * @param cachefile
     * @return
     */
    /*
     * private boolean isReadDataCache(String cachefile) { return
     * readObject(cachefile) != null; }
     */

    /**
     * 判断缓存是否失效
     *
     * @param cachefile
     * @return
     */
    public boolean isCacheDataFailure(String cachefile)
    {
        boolean failure = false;
        File data = context.getFileStreamPath(cachefile);
        if (!data.exists() || !((System.currentTimeMillis() - data.lastModified()) < CACHE_TIME))
            failure = true;
        return failure;
    }

    public static String getUid()
    {
        if (YARUserManager.getInstance().isLogined())
        {
            String userId = YARPreferenceUtils.readStringValue(MainApplication.getInstance(),
                    YARPreferenceUtils.PreferenceKeys.USER_ID);
            return userId == null ? "" : userId;
        }
        else
        {
            return "";
        }
    }

    /**
     * 清除缓存目录
     *
     * @param dir 目录
     * @param numDays 当前系统时间
     * @return
     */
    private int clearCacheFolder(File dir, long curTime)
    {
        int deletedFiles = 0;
        if (dir != null && dir.isDirectory())
        {
            try
            {
                for (File child : dir.listFiles())
                {
                    if (child.isDirectory())
                    {
                        deletedFiles += clearCacheFolder(child, curTime);
                    }
                    if (child.lastModified() < curTime)
                    {
                        if (child.delete())
                        {
                            deletedFiles++;
                        }
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return deletedFiles;
    }

    /**
     * 清除app缓存
     */
    public void clearAppCache()
    {
        // 清除webview缓存
        File file = context.getCacheDir();
        if (file != null && file.exists() && file.isDirectory())
        {
            for (File item : file.listFiles())
            {
                item.delete();
            }
            file.delete();
        }
        // 清除数据缓存
        clearCacheFolder(context.getFilesDir(), System.currentTimeMillis());
        clearCacheFolder(context.getCacheDir(), System.currentTimeMillis());
        // 2.2版本才有将应用缓存转移到sd卡的功能
        if (isMethodsCompat(android.os.Build.VERSION_CODES.FROYO))
        {
            clearCacheFolder(context.getExternalCacheDir(), System.currentTimeMillis());
        }
    }

    /**
     * 判断当前版本是否兼容目标版本的方法
     *
     * @param VersionCode
     * @return
     */
    public static boolean isMethodsCompat(int VersionCode)
    {
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        return currentVersion >= VersionCode;
    }
}
