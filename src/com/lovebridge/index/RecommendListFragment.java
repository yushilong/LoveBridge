package com.lovebridge.index;

import android.view.View;
import com.lovebridge.R;
import com.lovebridge.bean.User;
import com.lovebridge.library.YARBaseFragment;
import com.lovebridge.library.view.pulltorefresh.PullToRefreshGridView;

import java.util.ArrayList;

/**
 * @author yushilong
 * @date 2014-10-31 下午5:26:32
 * @version 1.0
 */
public class RecommendListFragment extends YARBaseFragment
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
        mRecommendListAdapter = new RecommendListAdapter(mActivity, new ArrayList<User>());
        mPullToRefreshGridView.setAdapter(mRecommendListAdapter);
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
