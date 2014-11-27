package com.lovebridge.chat.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.lovebridge.R;
import com.lovebridge.chat.adapter.ActionsAdapter;
import com.lovebridge.chat.fragment.ChatFragment;
import com.lovebridge.chat.view.chats.ActionsContentView;
import com.lovebridge.library.YARActivity;

public class ExamplesActivity extends YARActivity
{
    private static final String STATE_URI = "state:uri";
    private static final String STATE_FRAGMENT_TAG = "state:fragment_tag";
    private ActionsContentView viewActionsContentView;
    private int currposition;
    private String currentContentFragmentTag = null;

    @Override
    public int doGetContentViewId()
    {
        return R.layout.example;
    }

    @Override
    public void doInitSubViews(View containerView)
    {
        viewActionsContentView = (ActionsContentView) findViewById(R.id.actionsContentView);
    }

    @Override
    public void doInitDataes()
    {
        viewActionsContentView.setSwipingType(ActionsContentView.SWIPING_EDGE);
        final ListView viewActionsList = (ListView) findViewById(R.id.actions);
        final ActionsAdapter actionsAdapter = new ActionsAdapter(this);
        viewActionsList.setAdapter(actionsAdapter);
        viewActionsList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                    long flags)
            {
                updateContent(position);
                viewActionsContentView.showContent();
            }
        });
        updateContent(currposition);
    }

    @Override
    public void doAfter()
    {
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

    public void onActionsButtonClick(View view)
    {
        if (viewActionsContentView.isActionsShown())
            viewActionsContentView.showContent();
        else
            viewActionsContentView.showActions();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        outState.putInt(STATE_URI, currposition);
        outState.putString(STATE_FRAGMENT_TAG, currentContentFragmentTag);
        super.onSaveInstanceState(outState);
    }

    public void onSourceCodeClick(View view)
    {
    }

    public void updateContent(int position)
    {
        final Fragment fragment;
        final String tag;
        final FragmentManager fm = getSupportFragmentManager();
        final FragmentTransaction tr = fm.beginTransaction();
        if (currposition != position)
        {
            final Fragment currentFragment = fm.findFragmentByTag(currentContentFragmentTag);
            if (currentFragment != null)
                tr.hide(currentFragment);
        }
        tag = "ChatFragment";
        final Fragment foundFragment = fm.findFragmentByTag(tag);
        if (foundFragment != null)
        {
            fragment = foundFragment;
        }
        else
        {
            fragment = new ChatFragment();
        }
        if (fragment.isAdded())
        {
            tr.show(fragment);
        }
        else
        {
            tr.replace(R.id.content, fragment, tag);
        }
        tr.commit();
        currposition = position;
        currentContentFragmentTag = tag;
    }
}
