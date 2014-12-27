package com.lovebridge.utils;


import com.lovebridge.application.MainApplication;
import com.lovebridge.recommend.NearByPeople;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class JsonResolveUtils {
    // 附近个人的json文件名称
    private static final String NEARBY_PEOPLE = "nearby_people.json";
    // 附近个人的json文件名称
    private static final String NEARBY_GROUP = "nearby_group.json";
    // 用户资料文件夹
    private static final String PROFILE = "profile/";
    // 用户状态文件夹
    private static final String STATUS = "status/";
    // 后缀名
    private static final String SUFFIX = ".json";
    // 状态评论
    private static final String FEEDCOMMENT = "feedcomment.json";

    /**
     * 解析附近个人Json数据
     *
     * @return
     */
    public static List<NearByPeople> resolveNearbyPeople() {
        MainApplication application = MainApplication.getInstance();
        if (application.mNearByPeoples != null
                && application.mNearByPeoples.isEmpty()) {
            String json = TextUtils.getJson(
                    application.getApplicationContext(), NEARBY_PEOPLE);
            if (json != null) {
                try {
                    JSONArray array = new JSONArray(json);
                    NearByPeople people = null;
                    JSONObject object = null;
                    for (int i = 0; i < array.length(); i++) {
                        object = array.getJSONObject(i);
                        String uid = object.getString(NearByPeople.UID);
                        String avatar = object.getString(NearByPeople.AVATAR);
                        int isVip = object.getInt(NearByPeople.VIP);
                        int isGroupRole = object
                                .getInt(NearByPeople.GROUP_ROLE);
                        String industry = object
                                .getString(NearByPeople.INDUSTRY);
                        int isbindWeibo = object.getInt(NearByPeople.WEIBO);
                        int isbindTxWeibo = object
                                .getInt(NearByPeople.TX_WEIBO);
                        int isbindRenRen = object.getInt(NearByPeople.RENREN);
                        int device = object.getInt(NearByPeople.DEVICE);
                        int isRelation = object.getInt(NearByPeople.RELATION);
                        int isMultipic = object.getInt(NearByPeople.MULTIPIC);
                        String name = object.getString(NearByPeople.NAME);
                        int gender = object.getInt(NearByPeople.GENDER);
                        int age = object.getInt(NearByPeople.AGE);
                        String distance = object
                                .getString(NearByPeople.DISTANCE);
                        String time = object.getString(NearByPeople.TIME);
                        String sign = object.getString(NearByPeople.SIGN);

                        people = new NearByPeople(uid, avatar, isVip,
                                isGroupRole, industry, isbindWeibo,
                                isbindTxWeibo, isbindRenRen, device,
                                isRelation, isMultipic, name, gender, age,
                                distance, time, sign);
                        application.mNearByPeoples.add(people);
                    }
                } catch (JSONException e) {
                    application.mNearByPeoples.clear();
                }
            }
        }
        return application.mNearByPeoples;
    }


}
