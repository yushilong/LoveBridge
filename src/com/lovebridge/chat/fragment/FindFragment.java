
package com.lovebridge.chat.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lovebridge.R;
import com.lovebridge.chat.view.TabPagerIndicator;
import com.lovebridge.library.YARBaseFragment;

public class FindFragment extends YARBaseFragment {

    @Override
    public int doGetContentViewId() {
        // TODO Auto-generated method stub
        return R.layout.find_fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (containerView == null) {
            final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.CustomTabPager);
            LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
            containerView = localInflater.inflate(doGetContentViewId(), container, false);
            init(containerView);
            doInitSubViews(containerView);
            doInitDataes();
        }
        ViewGroup parent = (ViewGroup)containerView.getParent();
        if (parent != null) {
            parent.removeView(containerView);
        }

        return containerView;
    }

    @Override
    public void doInitSubViews(View containerView) {
        // TODO Auto-generated method stub
        ViewPager viewPager = (ViewPager)containerView.findViewById(R.id.pager);
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            protected final String[] CONTENT = new String[] { "通讯录", "新朋友", "群聊" };

            @Override
            public Fragment getItem(int i) {
                return new ContactlistFragment();
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return CONTENT[position % CONTENT.length];
            }
        });

        TabPagerIndicator pageIndicator = (TabPagerIndicator)containerView.findViewById(R.id.titles);
        pageIndicator.setViewPager(viewPager);
    }

    @Override
    public void doInitDataes() {
        // TODO Auto-generated method stub

    }

    @Override
    public void doAfter() {
        // TODO Auto-generated method stub

    }

    @Override
    public Object getNavigationTitle() {
        // TODO Auto-generated method stub
        return null;
    }

}
