
package com.lovebridge.library.api;

import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.view.View;

import com.lovebridge.library.api.asynchttpclient.AsyncHttpClient;
import com.lovebridge.library.api.asynchttpclient.JsonHttpResponseHandler;
import com.lovebridge.library.api.asynchttpclient.RequestParams;
import com.lovebridge.library.tools.YARCacheManager;
import com.lovebridge.library.tools.YARConstants;
import com.lovebridge.library.tools.YARLogUtils;
import com.lovebridge.library.tools.YARNetUtils;
import com.lovebridge.library.view.pulltorefresh.PullToRefreshBase;

/**
 * @author yushilong
 * @date 2014-9-30
 * @version 1.0
 */
public class YARBaseRequest
{
    private AsyncHttpClient mAsyncHttpClient;
    private static final String TAG = "HTTP";
    private YARApiListenerImpl mYarApiListenerImpl;
    //
    private String url;
    //
    private RequestParams mRequestParams;;
    private boolean isReadCache;

    public YARBaseRequest(String apiMethod, RequestParams requestParams, YARApiListenerImpl yarApiListenerImpl, View mProgressView)
    {
        // TODO Auto-generated constructor stub
        this.url = YARConstants.HOST + apiMethod;
        this.mYarApiListenerImpl = yarApiListenerImpl;
        this.mYarApiListenerImpl.setmProgressView(mProgressView);
        this.mRequestParams = requestParams;
        init();
    }

    @SuppressWarnings("rawtypes")
    public YARBaseRequest(String apiMethod, RequestParams requestParams, YARApiListenerImpl yarApiListenerImpl, View mProgressView, PullToRefreshBase pullToRefreshBase)
    {
        super();
        this.url = YARConstants.HOST + apiMethod;
        this.mYarApiListenerImpl = yarApiListenerImpl;
        this.mYarApiListenerImpl.setmProgressView(mProgressView);
        this.mYarApiListenerImpl.setmPullToRefreshBase(pullToRefreshBase);
        this.mRequestParams = requestParams;
        init();
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    private void init()
    {
        // TODO Auto-generated method stub
        mAsyncHttpClient = new AsyncHttpClient();
    }

    public void start()
    {
        // 是否读缓存数据
        if (isReadCache())
        {
            String cacheStr = YARCacheManager.getInstance().readObject(url);
            if (!TextUtils.isEmpty(cacheStr))
            {
                try
                {
                    // jsonArray
                    if (cacheStr.startsWith("["))
                    {
                        JSONArray jsonArray = new JSONArray(cacheStr);
                        mYarApiListenerImpl.onSuccess(HttpStatus.SC_OK, null, jsonArray);
                    }
                    else
                    // jsonObject
                    {
                        JSONObject jsonObject = new JSONObject(cacheStr);
                        mYarApiListenerImpl.onSuccess(HttpStatus.SC_OK, null, jsonObject);
                    }
                }
                catch (JSONException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    mYarApiListenerImpl.onFailure(HttpStatus.SC_BAD_REQUEST, null, "request error!", null);
                }
            }
        }
        if (!YARNetUtils.isNetAvailable())
        {
            mYarApiListenerImpl.onFailure(HttpStatus.SC_BAD_REQUEST, null, "网络不可用，请检查网络！", null);
            return;
        }
        YARLogUtils.i(TAG, "url--->" + url + "\nbody--->" + mRequestParams.toString());
        mAsyncHttpClient.post(url, mRequestParams, new JsonHttpResponseHandler()
        {
            @Override
            public void onStart()
            {
                // TODO Auto-generated method stub
                super.onStart();
                mYarApiListenerImpl.onStart();
            }

            @Override
            public void onSuccess(int statusCode , Header[] headers , JSONObject response)
            {
                // TODO Auto-generated method stub
                super.onSuccess(statusCode, headers, response);
                if (isResponseSuccess(statusCode, headers, response))
                {
                    mYarApiListenerImpl.onSuccess(statusCode, headers, response);
                }
            }

            @Override
            public void onSuccess(int statusCode , Header[] headers , JSONArray response)
            {
                // TODO Auto-generated method stub
                super.onSuccess(statusCode, headers, response);
                if (isResponseSuccess(statusCode, headers, response))
                {
                    mYarApiListenerImpl.onSuccess(statusCode, headers, response);
                }
            }

            @Override
            public void onFailure(int statusCode , Header[] headers , String responseString , Throwable throwable)
            {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, headers, responseString, throwable);
                mYarApiListenerImpl.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFinish()
            {
                // TODO Auto-generated method stub
                super.onFinish();
                mYarApiListenerImpl.onFinish();
            }

            @Override
            public void onProgress(int bytesWritten , int totalSize)
            {
                // TODO Auto-generated method stub
                super.onProgress(bytesWritten, totalSize);
                mYarApiListenerImpl.onProgress(bytesWritten, totalSize);
            }
        });
    }

    /**
     * 是否需要缓存数据,默认不缓存,true缓存false不缓存
     * 
     * @return
     */
    public boolean isReadCache()
    {
        return isReadCache;
    }

    public void setReadCache(boolean isReadCache)
    {
        this.isReadCache = isReadCache;
    }

    private boolean isResponseSuccess(int statusCode , Header[] headers , Object object)
    {
        if ("null".equals(object) || object == null)
        {
            YARLogUtils.i(TAG, "response--->null");
            return false;
        }
        YARLogUtils.i(TAG, "response--->" + object.toString());
        if (isReadCache())
        {
            YARCacheManager.getInstance().saveObject(object.toString(), url);
        }
        return true;
    }
}
