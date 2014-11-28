package com.lovebridge.index;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.lovebridge.R;
import com.lovebridge.chat.fragment.FindFragment;
import com.lovebridge.library.FragmentTabAdapter;
import com.lovebridge.library.YARActivity;

import com.lovebridge.library.tools.YARAppManager;
import com.lovebridge.recommend.RecommendListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yushilong
 * @date 2014-10-27 上午10:06:58
 * @version 1.0
 */
public class TabActivity extends YARActivity
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
        fragments.add(new FindFragment());
        rgs = (RadioGroup) findViewById(R.id.tabs_rg);
        FragmentTabAdapter tabAdapter = new FragmentTabAdapter((FragmentActivity) mActivity, fragments,
                R.id.tab_content, rgs);
        tabAdapter.setOnRgsExtraCheckedChangedListener(new FragmentTabAdapter.OnRgsExtraCheckedChangedListener()
        {
            @Override
            public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId, int index)
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

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {
            if (rgs.getCheckedRadioButtonId() != rgs.getChildAt(0).getId())
            {
                rgs.check(rgs.getChildAt(0).getId());
                return true;
            }
            if ((System.currentTimeMillis() - exitTime) > 2000)
            {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            }
            else
            {
                YARAppManager.getInstance().AppExit(getApplicationContext());
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
