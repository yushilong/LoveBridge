package com.lovebridge.library.api;

import com.lovebridge.library.volley.VolleyError;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author yushilong
 * @date 2014-9-30
 * @version 1.0
 */
interface YARApiListener
{
    void onSuccess(JSONObject response);

    void onSuccess(JSONArray response);

    void onFailure(VolleyError volleyError);

    void onStart();

    void onFinish();

    void onProgress(int bytesWritten, int totalSize);
}
