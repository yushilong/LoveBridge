
package com.lovebridge.chat.fragment;

import java.util.List;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.lovebridge.R;
import com.lovebridge.chat.view.EmojiPreviewImageView;

public abstract class EmojiPageFragment extends Fragment {
    protected BaseAdapter adapter;
    private GridView gridView;

    public EmojiPageFragment() {
        super();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.setGridViewColumnCount();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.adapter = new BaseAdapter() {

            public int getCount() {
                return EmojiPageFragment.this.getEmoji().size();
            }

            public Object getItem(int position) {
                return EmojiPageFragment.this.getEmoji().get(position);
            }

            public long getItemId(int position) {
                return 0;
            }

            public View getView(final int position, View convertView, ViewGroup parent) {
                View view = convertView;
                if (view == null) {
                    view = LayoutInflater.from(EmojiPageFragment.this.getActivity()).inflate(
                                    R.layout.emoji_picker_cell, parent, false);
                    view.setLayoutParams(new AbsListView.LayoutParams(50, 50));
                }

                ((EmojiPreviewImageView)view).setOnClickListener(new View.OnClickListener() {

                    public void onClick(View view) {
                        emojiClicked(position);
                    }
                });
                ((EmojiPreviewImageView)view).setEmojiCodePoint((Integer)EmojiPageFragment.this.getEmoji()
                                .get(position));
                return view;
            }
        };
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_emoji_page, container, false);
        this.gridView = (GridView)view.findViewById(R.id.gridview);
        this.gridView.setAdapter(this.adapter);
        this.setGridViewColumnCount();
        return view;
    }

    protected abstract void emojiClicked(int arg1);

    protected abstract List<?> getEmoji();

    protected EmojiPickerFragment getEmojiPicker() {
        return (EmojiPickerFragment)this.getFragmentManager().findFragmentById(R.id.emoji_picker_fragment);
    }

    private void setGridViewColumnCount() {
        if (this.gridView != null) {
            this.gridView.setNumColumns(((int)((((float)(this.getResources().getDisplayMetrics().widthPixels
                            - this.gridView.getPaddingLeft() - this.gridView.getPaddingRight()))) / this.getResources()
                            .getDimension(R.dimen.emoji_picker_cell_size))));
        }
    }
}
