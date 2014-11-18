package com.lovebridge.library.api;

import android.view.View;
import com.lovebridge.library.view.pulltorefresh.PullToRefreshBase;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author yushilong
 * @date 2014-10-29 下午6:40:02
 * @version 1.0
 */
public class YARPageRequest extends YARRequest
{
    private int pageNo, pageSize;

    public YARPageRequest(String apiMethod, JSONObject requestParams, YARApiListenerImpl yarApiListenerImpl,
            View mProgressView, PullToRefreshBase pullToRefreshBase)
    {
        super(apiMethod, requestParams, yarApiListenerImpl, mProgressView, pullToRefreshBase);
        initPage(requestParams);
    }

    public YARPageRequest(String apiMethod, JSONObject requestParams, YARApiListenerImpl yarApiListenerImpl,
            View mProgressView)
    {
        super(apiMethod, requestParams, yarApiListenerImpl, mProgressView);
        // TODO Auto-generated constructor stub
        initPage(requestParams);
    }

    public void initPage(JSONObject requestParams)
    {
        try
        {
            requestParams.put("pageNo", pageNo);
            requestParams.put("pageSize", pageSize);
        }
        catch (JSONException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void nextPage()
    {
        pageNo++;
        updateRequestParams();
    }

    public void prePage()
    {
        pageNo--;
        updateRequestParams();
    }

    public void firstPage()
    {
        pageNo = 1;
        updateRequestParams();
    }

    private void updateRequestParams()
    {
        // TODO Auto-generated method stub
        try
        {
            getRequestParams().put("pageNo", pageNo);
        }
        catch (JSONException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
