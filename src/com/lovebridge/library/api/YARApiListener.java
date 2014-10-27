
package com.lovebridge.library.api;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author yushilong
 * @date 2014-9-30
 * @version 1.0
 */
interface YARApiListener
{
    void onSuccess(int statusCode , Header[] headers , JSONObject response);

    void onSuccess(int statusCode , Header[] headers , JSONArray response);

    void onFailure(int statusCode , Header[] headers , String responseString , Throwable throwable);

    void onStart();

    void onFinish();

    void onProgress(int bytesWritten , int totalSize);
}
