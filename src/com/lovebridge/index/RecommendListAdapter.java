
package com.lovebridge.index;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lovebridge.R;
import com.lovebridge.bean.User;
import com.lovebridge.library.YARAdapter;
import com.lovebridge.library.api.YARImageLoader;

/**
 * @author yushilong
 * @date 2014-10-31 下午5:38:56
 * @version 1.0
 */
public class RecommendListAdapter extends YARAdapter<User> {
    public RecommendListAdapter(Activity activity, List<User> mList) {
        super(activity, mList);
        // TODO Auto-generated constructor stub
    }

    @Override
    public int itemLayoutRes() {
        // TODO Auto-generated method stub
        return R.layout.recommendlist_item;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent, ViewHolder viewHolder) {
        // TODO Auto-generated method stub
        ImageView iv_avatar = viewHolder.obtainView(convertView, R.id.iv_avatar);
        TextView tv_age = viewHolder.obtainView(convertView, R.id.tv_age);
        User user = list.get(position);
        YARImageLoader.newInstance().get(iv_avatar, user.avatar);
        tv_age.setText(user.age + "");
        return convertView;
    }
}
