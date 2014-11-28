package com.lovebridge.chat.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.lovebridge.R;
import com.lovebridge.chat.fragment.EmojiPageFragment;
import com.lovebridge.chat.utils.EmojiUtils;

import java.util.List;

public class EmojiCategoryPageFragment extends EmojiPageFragment
{
    public static final String EMOJI_PAGE_KEY = "emoji_page_key";
    private List<?> emoji;

    public EmojiCategoryPageFragment()
    {
        super();
    }

    public static Fragment newInstance(String category)
    {
        EmojiCategoryPageFragment emojiCategoryPageFragment = new EmojiCategoryPageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("emoji_page_key", category);
        ((Fragment) emojiCategoryPageFragment).setArguments(bundle);
        return ((Fragment) emojiCategoryPageFragment);
    }

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.emoji = EmojiUtils.getEmojiByCategory(this.getArguments().getString("emoji_page_key"));
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        view.findViewById(R.id.emoji_recent_intro).setVisibility(View.GONE);
        return view;
    }

    protected void emojiClicked(int position)
    {
        this.getEmojiPicker().emojiInserted((Integer) this.getEmoji().get(position), true);
    }

    protected List<?> getEmoji()
    {
        return this.emoji;
    }
}