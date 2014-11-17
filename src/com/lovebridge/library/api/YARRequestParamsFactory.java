package com.lovebridge.library.api;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author yushilong
 * @date 2014-9-29 下午5:34:13
 * @version 1.0
 */
public class YARRequestParamsFactory
{
    public static JSONObject createRequestParams()
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("userId", "123456");
        }
        catch (JSONException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonObject;
    }
}
