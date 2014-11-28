package com.lovebridge.library.api;

import android.text.TextUtils;
import android.view.View;
import com.lovebridge.application.MainApplication;
import com.lovebridge.library.tools.YARToast;
import com.lovebridge.library.view.pulltorefresh.PullToRefreshBase;
import com.lovebridge.library.volley.NetworkError;
import com.lovebridge.library.volley.VolleyError;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author yushilong
 * @date 2014-9-29 下午6:09:00
 * @version 1.0
 */
public class YARApiListenerImpl implements YARApiListener
{
    private View progressView;
    private PullToRefreshBase<?> pullToRefreshBase;

    public void setProgressView(View mProgressView)
    {
        this.progressView = mProgressView;
    }

    public void setPullToRefreshBase(PullToRefreshBase<?> mPullToRefreshBase)
    {
        this.pullToRefreshBase = mPullToRefreshBase;
    }

    @Override
    public void onSuccess(JSONObject response)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void onSuccess(JSONArray response)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void onFailure(VolleyError volleyError)
    {
        // TODO Auto-generated method stub
        if (volleyError == null)
        {
            return;
        }
        String errorStr = volleyError.getMessage();
        if (volleyError instanceof NetworkError)
        {
        }
        if (!TextUtils.isEmpty(errorStr))
        {
            YARToast.showDefault(MainApplication.getInstance(), errorStr);
        }
    }

    @Override
    public void onStart()
    {
        // TODO Auto-generated method stub
        if (progressView != null)
        {
            progressView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFinish()
    {
        // TODO Auto-generated method stub
        if (progressView != null)
        {
            progressView.setVisibility(View.GONE);
        }
        if (pullToRefreshBase != null)
        {
            pullToRefreshBase.onRefreshComplete();
        }
    }

    @Override
    public void onProgress(int bytesWritten, int totalSize)
    {
        // TODO Auto-generated method stub
    }
}
