
package com.lovebridge.library.api;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.lovebridge.library.view.pulltorefresh.PullToRefreshBase;
import com.lovebridge.library.view.pulltorefresh.PullToRefreshBase.Mode;

/**
 * @author yushilong
 * @date 2014-9-30 下午1:46:58
 * @version 1.0
 */
public class YARResponseParser {
    private static Gson gson = new Gson();

    /**
     * 单个实体类解析
     * 
     * @param jsonObject
     * @param clazz
     * @return
     */
    public synchronized static <T> T parseModel(JSONObject jsonObject, Class<T> clazz) {
        return gson.fromJson(jsonObject.toString(), clazz);
    }

    /**
     * 列表解析
     * 
     * @param jsonArray
     * @param clazz
     * @return
     */
    public synchronized static <T> List<T> parseList(JSONArray jsonArray, Class<T> clazz) {
        List<T> list = new ArrayList<T>();
        int length = jsonArray.length();
        for (int i = 0; i < length; i++) {
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            list.add(parseModel(jsonObject, clazz));
        }
        return list;
    }

    /**
     * 分页列表解析
     * 
     * @param jsonObject
     * @param arrayKey
     * @param clazz
     * @param pullToRefreshBase
     * @return
     */
    public synchronized static <T> List<T> parsePagingList(JSONObject jsonObject, String arrayKey, Class<T> clazz,
                                                           PullToRefreshBase<?> pullToRefreshBase) {
        boolean isLastPage = jsonObject.optBoolean("lastPage");
        if (isLastPage) {
            pullToRefreshBase.setMode(Mode.DISABLED);
        } else {
            pullToRefreshBase.setMode(Mode.BOTH);
        }
        JSONArray jsonArray = jsonObject.optJSONArray(arrayKey);
        return parseList(jsonArray, clazz);
    }
}
