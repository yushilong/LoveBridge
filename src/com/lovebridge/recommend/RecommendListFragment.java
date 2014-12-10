package com.lovebridge.recommend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import com.lovebridge.R;
import com.lovebridge.bean.User;
import com.lovebridge.library.YARFragment;
import com.lovebridge.library.tools.YARActivityUtil;
import com.lovebridge.library.view.pulltorefresh.PullToRefreshBase;
import com.lovebridge.library.view.pulltorefresh.PullToRefreshGridView;

import java.util.ArrayList;

/**
 * @author yushilong
 * @date 2014-10-31 下午5:26:32
 * @version 1.0
 */
public class RecommendListFragment extends YARFragment
{
    private PullToRefreshGridView mPullToRefreshGridView;
    private RecommendListAdapter mRecommendListAdapter;

    @Override
    public int doGetContentViewId()
    {
        // TODO Auto-generated method stub
        return R.layout.recommendlist;
    }

    @Override
    public void doInitSubViews(View containerView)
    {
        // TODO Auto-generated method stub
        mPullToRefreshGridView = (PullToRefreshGridView) containerView.findViewById(R.id.pull_refresh_grid);
        mPullToRefreshGridView.getRefreshableView().setNumColumns(4);
        mPullToRefreshGridView.getRefreshableView().setPadding(10, 10, 10, 10);
        mPullToRefreshGridView.getRefreshableView().setVerticalSpacing(10);
        mPullToRefreshGridView.getRefreshableView().setHorizontalSpacing(10);
        mPullToRefreshGridView.setMode(PullToRefreshBase.Mode.DISABLED);
        mRecommendListAdapter = new RecommendListAdapter(mActivity, new ArrayList<User>());
        mPullToRefreshGridView.setAdapter(mRecommendListAdapter);
        mPullToRefreshGridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                User user = mRecommendListAdapter.getList().get((int) id);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                Intent intent = new Intent(mContext, RecommendDetailActivity.class);
                YARActivityUtil.start(mActivity, intent, bundle);
            }
        });
    }

    @Override
    public void doInitDataes()
    {
        // TODO Auto-generated method stub
        for (int i = 0; i < 22; i++)
        {
            User user = new User();
            user.name = "name" + i;
            user.age = 15 + i;
            user.avatar = "http://www.qqpk.cn/Article/UploadFiles/201112/20111206131612194.jpg";
            mRecommendListAdapter.getList().add(user);
        }
        mRecommendListAdapter.notifyDataSetChanged();
    }

    @Override
    public void doAfter()
    {
        // TODO Auto-generated method stub
    }
}
