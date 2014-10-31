
package com.lovebridge.library.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.view.View;

import com.lovebridge.library.tools.YARConstants;
import com.lovebridge.library.tools.YARLogUtils;
import com.lovebridge.library.tools.YARNetUtils;
import com.lovebridge.library.view.pulltorefresh.PullToRefreshBase;
import com.lovebridge.library.volley.Cache.Entry;
import com.lovebridge.library.volley.NetworkError;
import com.lovebridge.library.volley.ParseError;
import com.lovebridge.library.volley.Request.Priority;
import com.lovebridge.library.volley.Response;
import com.lovebridge.library.volley.RetryPolicy;
import com.lovebridge.library.volley.VolleyError;
import com.lovebridge.library.volley.ex.YARJsonRequest;

/**
 * @author yushilong
 * @date 2014-9-30
 * @version 1.0
 */
public class YARRequest
{
    private static final String TAG = "Volley";
    private YARJsonRequest mYarJsonRequest;
    private YARApiListenerImpl mYarApiListenerImpl;
    //
    private String url;
    //
    private JSONObject requestParams;

    public YARRequest(String apiMethod, JSONObject requestParams, YARApiListenerImpl yarApiListenerImpl, View mProgressView)
    {
        // TODO Auto-generated constructor stub
        this.url = YARConstants.HOST + apiMethod;
        this.mYarApiListenerImpl = yarApiListenerImpl;
        this.mYarApiListenerImpl.setmProgressView(mProgressView);
        this.requestParams = requestParams;
        initRequest();
    }

    @SuppressWarnings("rawtypes")
    public YARRequest(String apiMethod, JSONObject requestParams, YARApiListenerImpl yarApiListenerImpl, View mProgressView, PullToRefreshBase pullToRefreshBase)
    {
        super();
        this.url = YARConstants.HOST + apiMethod;
        this.mYarApiListenerImpl = yarApiListenerImpl;
        this.mYarApiListenerImpl.setmProgressView(mProgressView);
        this.mYarApiListenerImpl.setmPullToRefreshBase(pullToRefreshBase);
        this.requestParams = requestParams;
        initRequest();
    }

    private void initRequest()
    {
        // TODO Auto-generated method stub
        mYarJsonRequest = new YARJsonRequest(url, requestParams, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                // TODO Auto-generated method stub
                if (isResponseSuccess(response))
                {
                    mYarApiListenerImpl.onSuccess(response);
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                // TODO Auto-generated method stub
                mYarApiListenerImpl.onFailure(error);
            }
        });
        mYarJsonRequest.setTag(TAG);
        mYarJsonRequest.setHeaders();
    }

    public void start()
    {
        // 是否读缓存数据
        if (shouldCache())
        {
            Entry cacheEntry = mYarJsonRequest.getCacheEntry();
            if (cacheEntry != null)
            {
                String cacheStr = new String(cacheEntry.data);
                if (TextUtils.isEmpty(cacheStr.trim()))
                {
                    return;
                }
                try
                {
                    // jsonArray
                    if (cacheStr.startsWith("["))
                    {
                        JSONArray jsonArray = new JSONArray(cacheStr);
                        mYarApiListenerImpl.onSuccess(jsonArray);
                    }
                    else
                    // jsonObject
                    {
                        JSONObject jsonObject = new JSONObject(cacheStr);
                        mYarApiListenerImpl.onSuccess(jsonObject);
                    }
                }
                catch (JSONException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    mYarApiListenerImpl.onFailure(new ParseError());
                }
            }
        }
        if (!YARNetUtils.isNetAvailable())
        {
            mYarApiListenerImpl.onFailure(new NetworkError());
            return;
        }
        YARLogUtils.i(TAG, "url--->" + url + "\nbody--->" + requestParams.toString());
        YARVolley.getRequestQueue().add(mYarJsonRequest);
    }

    private boolean isResponseSuccess(Object object)
    {
        if ("null".equals(object) || object == null)
        {
            YARLogUtils.i(TAG, "response--->null");
            return false;
        }
        YARLogUtils.i(TAG, "response--->" + object.toString());
        if (shouldCache())
        {
            Entry cacheEntry = new Entry();
            cacheEntry.data = object.toString().getBytes();
            mYarJsonRequest.setCacheEntry(cacheEntry);
        }
        return true;
    }

    public JSONObject getRequestParams()
    {
        return requestParams;
    }

    public void setRequestParams(JSONObject requestParams)
    {
        this.requestParams = requestParams;
    }

    public void setShouldCache(boolean shouldCache)
    {
        mYarJsonRequest.setShouldCache(shouldCache);
    }

    public boolean shouldCache()
    {
        return mYarJsonRequest.shouldCache();
    }

    public void setPriority(Priority priority)
    {
        mYarJsonRequest.setPriority(priority);
    }

    public void setRetryPolicy(RetryPolicy retryPolicy)
    {
        mYarJsonRequest.setRetryPolicy(retryPolicy);
    }

    public void setTag(Object tag)
    {
        mYarJsonRequest.setTag(tag);
    }

    public void getTag(Object tag)
    {
        mYarJsonRequest.getTag();
    }
}
