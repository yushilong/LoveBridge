
package com.lovebridge.chat.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.lovebridge.R;
import com.lovebridge.chat.moden.ChatUser;


public class ActionsAdapter extends BaseAdapter {

    private static final int VIEW_TYPE_CATEGORY = 0;
    private static final int VIEW_TYPE_SETTINGS = 1;
    private static final int VIEW_TYPE_SITES = 2;
    private static final int VIEW_TYPES_COUNT = 3;

    private Context context;

    public ActionsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public ChatUser getItem(int position) {
        return new ChatUser();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int type = getItemViewType(position);

        final ViewHolder holder;
        if (convertView == null) {
            if (type == VIEW_TYPE_CATEGORY)
                convertView = LayoutInflater.from(context).inflate(R.layout.category_list_item, parent, false);
            else
                convertView = LayoutInflater.from(context).inflate(R.layout.action_list_item, parent, false);

            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (type != VIEW_TYPE_CATEGORY) {
            final Drawable icon = convertView.getContext().getResources().getDrawable(R.drawable.app_pref_bg);
            icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
            holder.text.setCompoundDrawables(icon, null, null, null);
            holder.text.setText("XXX");
        } else {
            holder.text.setText("AAA");
        }

        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPES_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        final String scheme = "";
        if ("category".equals(scheme))
            return VIEW_TYPE_CATEGORY;
        else if ("settings".equals(scheme))
            return VIEW_TYPE_SETTINGS;
        return VIEW_TYPE_SITES;
    }

    @Override
    public boolean isEnabled(int position) {
        return getItemViewType(position) != VIEW_TYPE_CATEGORY;
    }

    private static class ViewHolder {
        TextView text;
    }
}
