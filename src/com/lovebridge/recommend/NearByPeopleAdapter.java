package com.lovebridge.recommend;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.lovebridge.R;
import com.lovebridge.application.MainApplication;
import com.lovebridge.library.YARAdapter;
import com.lovebridge.utils.PhotoUtils;
import com.lovebridge.widget.HandyTextView;

public class NearByPeopleAdapter extends YARAdapter {

    public NearByPeopleAdapter(Activity context
    ) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent, YARAdapter.ViewHolder viewHolder) {


        ImageView mIvAvatar = (ImageView) viewHolder.obtainView(convertView, R.id.user_item_iv_avatar);
        ImageView mIvVip = (ImageView) viewHolder.obtainView(convertView, R.id.user_item_iv_icon_vip);
        ImageView mIvGroupRole = (ImageView) viewHolder.obtainView(convertView, R.id.user_item_iv_icon_group_role);
        ImageView mIvIndustry = (ImageView) viewHolder.obtainView(convertView, R.id.user_item_iv_icon_industry);
        ImageView mIvWeibo = (ImageView) viewHolder.obtainView(convertView, R.id.user_item_iv_icon_weibo);
        ImageView mIvTxWeibo = (ImageView) viewHolder.obtainView(convertView, R.id.user_item_iv_icon_txweibo);
        ImageView mIvRenRen = (ImageView) viewHolder.obtainView(convertView, R.id.user_item_iv_icon_renren);
        ImageView mIvDevice = (ImageView) viewHolder.obtainView(convertView, R.id.user_item_iv_icon_device);
        ImageView mIvRelation = (ImageView) viewHolder.obtainView(convertView, R.id.user_item_iv_icon_relation);
        ImageView mIvMultipic = (ImageView) viewHolder.obtainView(convertView, R.id.user_item_iv_icon_multipic);

        HandyTextView mHtvName = (HandyTextView) viewHolder.obtainView(convertView, R.id.user_item_htv_name);
        LinearLayout mLayoutGender = (LinearLayout) viewHolder.obtainView(convertView, R.id.user_item_layout_gender);
        ImageView mIvGender = (ImageView) viewHolder.obtainView(convertView, R.id.user_item_iv_gender);
        HandyTextView mHtvAge = (HandyTextView) viewHolder.obtainView(convertView, R.id.user_item_htv_age);
        HandyTextView mHtvDistance = (HandyTextView) viewHolder.obtainView(convertView, R.id.user_item_htv_distance);
        HandyTextView mHtvTime = (HandyTextView) viewHolder.obtainView(convertView, R.id.user_item_htv_time);
        HandyTextView mHtvSign = (HandyTextView) viewHolder.obtainView(convertView, R.id.user_item_htv_sign);

        NearByPeople people = (NearByPeople) getItem(position);
        mIvAvatar.setImageBitmap(MainApplication.getInstance().getAvatar(people
                .getAvatar()));
        mHtvName.setText(people.getName());
        mLayoutGender.setBackgroundResource(people.getGenderBgId());
        mIvGender.setImageResource(people.getGenderId());
        mHtvAge.setText(people.getAge() + "");
        mHtvDistance.setText(people.getDistance());
        mHtvTime.setText(people.getTime());
        mHtvSign.setText(people.getSign());
        if (people.getIsVip() != 0) {
            mIvVip.setVisibility(View.VISIBLE);
        } else {
            mIvVip.setVisibility(View.GONE);
        }
        if (people.getIsGroupRole() != 0) {
            mIvGroupRole.setVisibility(View.VISIBLE);
            if (people.getIsGroupRole() == 1) {
                mIvGroupRole
                        .setImageResource(R.drawable.ic_userinfo_groupowner);
            }
        } else {
            mIvIndustry.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(people.getIndustry())) {
            mIvIndustry.setVisibility(View.VISIBLE);
            mIvIndustry.setImageBitmap(PhotoUtils.getIndustry(
                    people.getIndustry()));
        } else {
            mIvIndustry.setVisibility(View.GONE);
        }
        if (people.getIsbindWeibo() != 0) {
            mIvWeibo.setVisibility(View.VISIBLE);
            if (people.getIsbindWeibo() == 1) {
                mIvWeibo.setImageResource(R.drawable.ic_userinfo_weibov);
            }
        } else {
            mIvWeibo.setVisibility(View.GONE);
        }

        if (people.getIsbindRenRen() != 0) {
            mIvRenRen.setVisibility(View.VISIBLE);
        } else {
            mIvRenRen.setVisibility(View.GONE);
        }
        if (people.getDevice() != 0) {
            mIvDevice.setVisibility(View.VISIBLE);
            if (people.getDevice() == 1) {
                mIvDevice
                        .setImageResource(R.drawable.ic_userinfo_android);
            }
            if (people.getDevice() == 2) {
                mIvDevice.setImageResource(R.drawable.ic_userinfo_apple);
            }
        } else {
            mIvDevice.setVisibility(View.GONE);
        }
        if (people.getIsRelation() != 0) {
            mIvRelation.setVisibility(View.VISIBLE);
        } else {
            mIvRelation.setVisibility(View.GONE);
        }
        if (people.getIsMultipic() != 0) {
            mIvMultipic.setVisibility(View.VISIBLE);
        } else {
            mIvMultipic.setVisibility(View.GONE);
        }
        return convertView;
    }

    @Override
    public int itemLayoutRes() {
        return R.layout.listitem_user;
    }


}
