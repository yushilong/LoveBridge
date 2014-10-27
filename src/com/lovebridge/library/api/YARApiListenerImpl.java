
package com.lovebridge.library.api;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.text.TextUtils;
import android.view.View;

import com.lovebridge.application.MainApplication;
import com.lovebridge.library.tools.YARToast;
import com.lovebridge.library.view.pulltorefresh.PullToRefreshBase;

/**
 * @author yushilong
 * @date 2014-9-29 下午6:09:00
 * @version 1.0
 */
public class YARApiListenerImpl implements YARApiListener
{
    private View mProgressView;
    private PullToRefreshBase<?> mPullToRefreshBase;

    public void setmProgressView(View mProgressView)
    {
        this.mProgressView = mProgressView;
    }

    public void setmPullToRefreshBase(PullToRefreshBase<?> mPullToRefreshBase)
    {
        this.mPullToRefreshBase = mPullToRefreshBase;
    }

    @Override
    public void onSuccess(int statusCode , Header[] headers , JSONObject response)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void onSuccess(int statusCode , Header[] headers , JSONArray response)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void onFailure(int statusCode , Header[] headers , String responseString , Throwable throwable)
    {
        // TODO Auto-generated method stub
        if (!TextUtils.isEmpty(responseString))
        {
            YARToast.showDefault(MainApplication.getInstance(), responseString);
        }
    }

    @Override
    public void onStart()
    {
        // TODO Auto-generated method stub
        if (mProgressView != null)
        {
            mProgressView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFinish()
    {
        // TODO Auto-generated method stub
        if (mProgressView != null)
        {
            mProgressView.setVisibility(View.GONE);
        }
        if (mPullToRefreshBase != null)
        {
            mPullToRefreshBase.onRefreshComplete();
        }
    }

    @Override
    public void onProgress(int bytesWritten , int totalSize)
    {
        // TODO Auto-generated method stub
    }
}
