
package com.lovebridge.library.api;

import com.lovebridge.library.api.asynchttpclient.RequestParams;

/**
 * @author yushilong
 * @date 2014-9-29 下午5:34:13
 * @version 1.0
 */
public class YARRequestParamsFactory
{
    public static RequestParams createRequestParams()
    {
        RequestParams requestParams = new RequestParams();
        return requestParams;
    }
}
