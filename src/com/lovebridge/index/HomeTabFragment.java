
package com.lovebridge.index;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.RadioGroup;

import com.lovebridge.R;
import com.lovebridge.library.FragmentTabAdapter;
import com.lovebridge.library.YARFragment;

/**
 * @author yushilong
 * @date 2014-10-27 上午10:06:58
 * @version 1.0
 */
public class HomeTabFragment extends YARFragment
{
    private RadioGroup rgs;
    public List<Fragment> fragments = new ArrayList<Fragment>();

    @Override
    public int doGetContentViewId()
    {
        // TODO Auto-generated method stub
        return R.layout.hometab;
    }

    @Override
    public void doInitSubViews(View containerView)
    {
        // TODO Auto-generated method stub
        fragments.add(new IndexFragment());
        fragments.add(new RecommendListFragment());
        fragments.add(new IndexFragment());
        rgs = (RadioGroup) containerView.findViewById(R.id.tabs_rg);
        FragmentTabAdapter tabAdapter = new FragmentTabAdapter((FragmentActivity) mActivity, fragments, R.id.tab_content, rgs);
        tabAdapter.setOnRgsExtraCheckedChangedListener(new FragmentTabAdapter.OnRgsExtraCheckedChangedListener()
        {
            @Override
            public void OnRgsExtraCheckedChanged(RadioGroup radioGroup , int checkedId , int index)
            {
                switch (index)
                {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                }
            }
        });
    }

    @Override
    public void doInitDataes()
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void doAfter()
    {
        // TODO Auto-generated method stub
    }

    @Override
    public Object getNavigationTitle()
    {
        // TODO Auto-generated method stub
        return "HOME TABS";
    }
}
