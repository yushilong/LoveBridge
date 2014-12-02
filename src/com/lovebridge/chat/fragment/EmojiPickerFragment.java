package com.lovebridge.chat.fragment;

import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.GridView;
import com.lovebridge.R;
import com.lovebridge.chat.adapter.EmojiTabsAdapter;
import com.lovebridge.chat.adapter.EmojiTabsAdapter.OnTabChangeListener;
import com.lovebridge.chat.utils.EmojiUtils;
import com.lovebridge.chat.utils.ViewTreeObserverUtils;

public class EmojiPickerFragment extends Fragment {
    public interface ComposerReceiver {
        void onBackspacePressed();

        void onInsertEmoji(String arg1);

        void onStateChanged(State arg1);
    }

    public interface Listener {
        void makeRoomForEmojiPicker(int arg1);
    }

    public enum State {
        NONE, EMOJI_PICKER, KEYBOARD;

        private void setState(boolean force, EmojiPickerFragment emojiPickerFragment) {
            if (this != emojiPickerFragment.currentState || (force)) {
                emojiPickerFragment.currentState = this;
                emojiPickerFragment.updateVisibility();
                if (emojiPickerFragment.composerReceiver != null) {
                    emojiPickerFragment.composerReceiver.onStateChanged(this);
                }
            }
        }
    }

    private static final float DEFAULT_KEYBOARD_ASPECT_RATIO_LANDSCAPE = 0.55f;
    private static final float DEFAULT_KEYBOARD_ASPECT_RATIO_PORTRAIT = 0.72f;
    private ComposerReceiver composerReceiver;
    private State currentState;
    ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener;
    private int lastOpenKeyboardHeight;
    private EmojiRecentPageFragment recentPageFragment;
    private View rootView;
    private EmojiTabsAdapter tabsAdapter;

    public EmojiPickerFragment() {
        super();
        this.lastOpenKeyboardHeight = 0;
        this.globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            private int lastBottom = -1;
            private Rect rect = new Rect();

            public void onGlobalLayout() {
                EmojiPickerFragment.this.rootView.getWindowVisibleDisplayFrame(this.rect);
                if (this.lastBottom != this.rect.bottom) {
                    this.lastBottom = this.rect.bottom;
                    int i = EmojiPickerFragment.this.getResources().getDisplayMetrics().heightPixels - this.rect.bottom;
                    if (i > 0) {
                        EmojiPickerFragment.this.lastOpenKeyboardHeight = i;
                        EmojiPickerFragment.this.setState(State.KEYBOARD);
                        return;
                    }
                    if (EmojiPickerFragment.this.currentState != State.EMOJI_PICKER) {
                        EmojiPickerFragment.this.setState(State.NONE);
                    }
                }
            }
        };
    }

    public void emojiInserted(Integer codePoint, boolean shouldRefreshRecent) {
        if (this.composerReceiver != null) {
            this.composerReceiver.onInsertEmoji(EmojiUtils.convertCodePointToString(codePoint.intValue()));
        }
        EmojiUtils.updateRecentEmoji(codePoint);
        if (this.recentPageFragment != null && (shouldRefreshRecent)) {
            this.recentPageFragment.refreshEmoji();
        }
    }

    public void hide() {
        this.setState(State.NONE);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.setDefaultKeyboardSize();
        if (this.isResumed()) {
            this.updateVisibility();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setDefaultKeyboardSize();
        this.getFragmentManager().addOnBackStackChangedListener(new OnBackStackChangedListener() {
            public void onBackStackChanged() {
                if (!EmojiPickerFragment.this.inBackStack()) {
                    EmojiPickerFragment.this.setState(State.NONE);
                }
            }
        });
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int i = 0;
        View view = inflater.inflate(R.layout.fragment_emoji_picker, container, false);
        this.rootView = this.getActivity().findViewById(Window.ID_ANDROID_CONTENT);
        this.tabsAdapter = new EmojiTabsAdapter(this.getActivity(), this.getFragmentManager());
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.emoji_pager);
        viewPager.setAdapter(this.tabsAdapter.getFragmentStatePagerAdapter());
        viewPager.setOnPageChangeListener(this.tabsAdapter.getOnPageChangeListener());
        this.tabsAdapter.setTabSelectedListener(new OnTabChangeListener() {
            public void tabSelected(int position) {
                viewPager.setCurrentItem(position);
            }
        });
        View view2 = view.findViewById(R.id.emoji_tabs_gridview);
        ((GridView) view2).setNumColumns(this.tabsAdapter.getCount());
        ((GridView) view2).setAdapter(this.tabsAdapter.getBaseAdapter());
        if (EmojiUtils.getEmojiRecentSize() < 5) {
            i = 1;
        }
        viewPager.setCurrentItem(i);
        view.findViewById(R.id.backspace).setOnTouchListener(new View.OnTouchListener() {

            private Handler handler;
            private boolean isRepeating;
            private Runnable action = new Runnable()
              {
               public void run() {
                    if (composerReceiver != null) {
                        composerReceiver.onBackspacePressed();
                        int i;
                        if (isRepeating) {
                            i = 100;
                        } else {
                            i = 500;
                        }
                        handler.postDelayed(this, i);
                        isRepeating = true;
                    }
                }
              };

            public boolean onTouch(View v, MotionEvent event) {
                boolean bool = true;
                switch (event.getAction()) {
                    case 0: {
                        if (this.handler != null) {
                            return bool;
                        }
                        this.handler = new Handler();
                        this.handler.post(this.action);
                        v.setPressed(true);
                        break;
                    }
                    case 1: {
                        if (this.handler == null) {
                            return bool;
                        }
                        this.handler.removeCallbacks(this.action);
                        this.handler = null;
                        v.setPressed(false);
                        break;
                    }
                    default: {
                        bool = false;
                        break;
                    }
                }
                return bool;
            }
        });
        return view;
    }

    public void onPause() {
        super.onPause();
        ViewTreeObserverUtils.removeOnGlobalLayoutListener(this.rootView.getViewTreeObserver(),
                this.globalLayoutListener);
    }

    public void onResume() {
        super.onResume();
        State.NONE.setState(true, this);
        this.rootView.getViewTreeObserver().addOnGlobalLayoutListener(this.globalLayoutListener);
    }

    public void setComposerReceiver(ComposerReceiver handler) {
        this.composerReceiver = handler;
        State.NONE.setState(true, this);
    }

    public void setRecentPageFragment(EmojiRecentPageFragment fragment) {
        this.recentPageFragment = fragment;
    }

    public void show() {
        this.setState(State.EMOJI_PICKER);
    }

    private boolean inBackStack() {
        FragmentManager fragmentmanager = getFragmentManager();
        int i = 0;
        while (i < fragmentmanager.getBackStackEntryCount()) {
            if (fragmentmanager.getBackStackEntryAt(i).getName().equals("emoji_picker_back")) {
                return true;
            }
            i++;
        }
        return false;
    }

    private void setDefaultKeyboardSize() {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        float f = displayMetrics.widthPixels < displayMetrics.heightPixels ? DEFAULT_KEYBOARD_ASPECT_RATIO_PORTRAIT
                : DEFAULT_KEYBOARD_ASPECT_RATIO_LANDSCAPE;
        this.lastOpenKeyboardHeight = ((int) ((((float) Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels))) * f));
    }

    private void setState(State state) {
        state.setState(false, this);
    }

    private void updateVisibility() {
        int i;
        if (this.currentState == State.EMOJI_PICKER) {
            if (!this.inBackStack()) {
                this.getFragmentManager().beginTransaction().show(this).addToBackStack("emoji_picker_back")
                        .commit();
            }
            i = this.lastOpenKeyboardHeight;
        } else {
            this.getFragmentManager().popBackStack("emoji_picker_back", 1);
            i = 0;
        }
        ((Listener) this.getActivity()).makeRoomForEmojiPicker(i);
    }


}
