
package com.lovebridge;

import android.annotation.SuppressLint;
import android.app.Instrumentation;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lovebridge.index.HomeTabFragment;
import com.lovebridge.library.YARBaseActivity;
import com.lovebridge.library.YARBaseFragment;
import com.lovebridge.library.tools.YARAppManager;

/**
 * @author yushilong
 * @date 2014-9-30 下午4:06:23
 * @version 1.0
 */
@SuppressLint("InflateParams")
public class MainActivity extends YARBaseActivity
{
    private ViewGroup navigationViewGroup;//导航栏容器
    private View defaultNavigationView;//默认的导航栏
    private FragmentManager mFragmentManager;
    private Fragment currentFragment;

    @Override
    public int doGetContentViewId()
    {
        // TODO Auto-generated method stub
        return R.layout.main;
    }

    @Override
    public void doInitSubViews(View containerView)
    {
        // TODO Auto-generated method stub
        navigationViewGroup = (ViewGroup) findViewById(R.id.fl_navigation);
        defaultNavigationView = LayoutInflater.from(mContext).inflate(R.layout.yar_navigation, null);
    }

    /**
     * 获取默认的导航栏
     * @return
     */
    public View getDefaultNavigationView()
    {
        return defaultNavigationView;
    }

    /**
     * 替换导航栏
     */
    public void replaceNavigationView()
    {
        navigationViewGroup.removeAllViews();
        View navigationView = ((YARBaseFragment) currentFragment).getNavigationView();
        navigationViewGroup.addView(navigationView == null ? defaultNavigationView : navigationView);
        changeNavigationTitle(0);
    }

    /**
     * 改变导航栏标题，默认只需重写getNavigationTitle方法即可
     * @param resId
     */
    public void changeNavigationTitle(int resId)
    {
        TextView titleTextView = (TextView) navigationViewGroup.findViewById(R.id.tv_navigation_title);
        if (resId == 0)
        {
            YARBaseFragment yarBaseFragment = (YARBaseFragment) currentFragment;
            if (yarBaseFragment.getNavigationTitle() instanceof String)
            {
                titleTextView.setText(yarBaseFragment.getNavigationTitle() == null ? null : yarBaseFragment.getNavigationTitle().toString());
            }
            else if (yarBaseFragment.getNavigationTitle() instanceof Integer)
            {
                int titleResId = Integer.valueOf(yarBaseFragment.getNavigationTitle().toString());
                titleTextView.setText(titleResId == 0 ? null : titleResId);
            }
        }
        else
        {
            titleTextView.setText(resId);
        }
    }

    /**
     * 启动不带参数的fragment
     * @param clazz
     */
    public void startFragment(Class<?> clazz)
    {
        startFragment(clazz, null);
    }

    /**
     * 启动带参数的fragment
     * @param clazz
     */
    public void startFragment(Class<?> clazz , Bundle bundle)
    {
        Fragment fragment = null;
        try
        {
            fragment = (Fragment) clazz.newInstance();
        }
        catch (InstantiationException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (bundle != null && !bundle.isEmpty())
        {
            fragment.setArguments(bundle);
        }
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        String tag = clazz.hashCode() + "";
        fragmentTransaction.replace(R.id.fl_body, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
        currentFragment = fragment;
        replaceNavigationView();
    }

    @Override
    public void doInitDataes()
    {
        // TODO Auto-generated method stub
        mFragmentManager = getSupportFragmentManager();
        startFragment(HomeTabFragment.class);
    }

    @Override
    public void doAfter()
    {
        // TODO Auto-generated method stub
    }

    /**
     * 模拟BACK按键
     */
    public void inmitateBack()
    {
        // TODO Auto-generated method stub
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                Instrumentation inst = new Instrumentation();
                inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK); //传入不同的keycode就ok了
            }
        }).start();
    }

    public void onFragmentResume(Fragment fragment)
    {
        // TODO Auto-generated method stub
        if (currentFragment == null || currentFragment != fragment)
        {
            currentFragment = fragment;
            replaceNavigationView();
        }
    }
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode , KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            if (mFragmentManager.getBackStackEntryCount() == 1)
            {
                if ((System.currentTimeMillis() - exitTime) > 2000)
                {
                    Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    exitTime = System.currentTimeMillis();
                    return true;
                }
                else
                {
                    YARAppManager.getInstance().AppExit(MainActivity.this);
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
