
package com.lovebridge.chat.fragment;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.lovebridge.R;
import com.lovebridge.chat.utils.EmojiUtils;

public class EmojiRecentPageFragment extends EmojiPageFragment {
    private List<?> emoji;
    private GridView gridView;
    private View recentIntro;

    public EmojiRecentPageFragment() {
        super();
    }

    public static Fragment newInstance() {
        return new EmojiRecentPageFragment();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getEmojiPicker().setRecentPageFragment(this);
        this.emoji = EmojiUtils.getEmojiRecent();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        this.recentIntro = view.findViewById(R.id.emoji_recent_intro);
        this.gridView = (GridView)view.findViewById(R.id.gridview);
        this.updateViewState();
        return view;
    }

    public void onDestroy() {
        super.onDestroy();
        EmojiPickerFragment emojiPickerFragment = this.getEmojiPicker();
        if (emojiPickerFragment != null) {
            emojiPickerFragment.setRecentPageFragment(null);
        }
    }

    public void refreshEmoji() {
        this.emoji = EmojiUtils.getEmojiRecent();
        adapter.notifyDataSetChanged();
        this.updateViewState();
    }

    protected void emojiClicked(int position) {
        this.getEmojiPicker().emojiInserted((Integer)this.getEmoji().get(position), false);
    }

    protected List<?> getEmoji() {
        return this.emoji;
    }

    private void updateViewState() {
        int i = 8;
        int i1 = 1;
        int i2 = 0;
        if (this.emoji.size() >= 1) {
            i1 = 0;
        }

        GridView gridView = this.gridView;
        int i3 = i1 != 0 ? i : 0;
        gridView.setVisibility(i3);
        View view = this.recentIntro;
        if (i1 == 0) {
            i2 = i;
        }

        view.setVisibility(i2);
    }
}
