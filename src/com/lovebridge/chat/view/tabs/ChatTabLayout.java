package com.lovebridge.chat.view.tabs;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.view.View;
import android.view.animation.*;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.easemob.chat.EMConversation;
import com.lovebridge.R;

import java.util.HashMap;
import java.util.Map;

public class ChatTabLayout extends RelativeLayout
{
    private static final int ANIMATION_DURATION = 100;
    private static final int AVATAR_BOUNCE_LIMIT = 3;
    private static final int AVATAR_BOUNCE_REPEAT_DELAY = 2000;
    private static final int NUM_OBJECTS_TO_CACHE = 20;
    private static Map<EMConversation, Integer> bounceCount;
    private static LruCache<EMConversation, CachedChatTabInfo> cache;
    private final ImageView avatar;
    private final AnimationSet avatarAnimation;
    private final View avatarWrapper;
    private final TextView title;
    private final TextView unreadIndicator;
    private boolean avatarAnimationIsAnimating;
    private ChatTabEntry chatTabEntry;

    public ChatTabLayout(Context context)
    {
        super(context);
        this.avatarAnimationIsAnimating = false;
        inflate(this.getContext(), R.layout.tab_chat, this);
        this.setClipToPadding(true);
        this.avatar = (ImageView) this.findViewById(R.id.avatar);
        this.avatarWrapper = this.findViewById(R.id.avatar_wrapper);
        this.avatarAnimation = (AnimationSet) AnimationUtils.loadAnimation(context, R.anim.tabs_avatar_bounce);
        this.title = (TextView) this.findViewById(R.id.title);
        this.unreadIndicator = (TextView) this.findViewById(R.id.unread_indicator);
        this.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                chatTabEntry.selectTab(chatTabEntry);
            }
        });
        this.setOnLongClickListener(new View.OnLongClickListener()
        {
            public boolean onLongClick(View v)
            {
                chatTabEntry.deleteChat();
                return true;
            }
        });
        bounceCount = new HashMap<EMConversation, Integer>();
        cache = new LruCache<EMConversation, CachedChatTabInfo>(NUM_OBJECTS_TO_CACHE);
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (bounceCount!=null){
             bounceCount.clear();
        }
    }

    public void loadChatTabContents(ChatTabEntry chatTabEntry)
    {
        this.chatTabEntry = chatTabEntry;
        EMConversation conversation = chatTabEntry.getEMConversation();
        this.avatar.clearAnimation();
        this.title.clearAnimation();
        CachedChatTabInfo info = cache.get(conversation);
        if (info != null)
        {
            this.updateContent(info);
        }
        else
        {

            CachedChatTabInfo result = new CachedChatTabInfo(null, conversation.getUserName());
            this.title.setText(result.title);
            this.title.setVisibility(View.INVISIBLE);
            avatar.setBackgroundResource(R.drawable.barcode_torch_on);
            this.unreadIndicator.setVisibility(GONE);
            cache.put(conversation, result);
            ScaleAnimation scaleAnimation = new ScaleAnimation(0f, 1f, 0f, 1f,
                    ((float) (avatar.getWidth() / 2)),
                    ((float) (avatar.getHeight() / 2)));
            scaleAnimation.setDuration(ANIMATION_DURATION);
            scaleAnimation.setFillAfter(true);
            avatar.startAnimation(scaleAnimation);
            AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
            alphaAnimation.setDuration(100);
            alphaAnimation.setFillAfter(true);
            title.startAnimation(alphaAnimation);
        }
    }

    private void bounceChatTab(boolean withDelay)
    {
        if (!this.avatarAnimationIsAnimating)
        {
            this.incrementBounceCount();
            AnimationSet animationSet = this.avatarAnimation;
            int i = withDelay ? AVATAR_BOUNCE_REPEAT_DELAY : 0;
            animationSet.setStartOffset(i);
            this.avatarAnimation.setAnimationListener(new Animation.AnimationListener()
            {
                public void onAnimationEnd(Animation anim)
                {
                    clearBounceChatTab();
                    if (canChatTabContinueBouncing())
                    {
                        bounceChatTab(true);
                    }
                }

                public void onAnimationRepeat(Animation anim)
                {
                }

                public void onAnimationStart(Animation anim)
                {
                    avatarAnimationIsAnimating = true;
                }
            });
            this.avatarWrapper.startAnimation(this.avatarAnimation);
        }
    }

    private boolean canChatTabContinueBouncing()
    {
        int size = bounceCount.get(this.chatTabEntry.getEMConversation());
        boolean bool = size >= this.avatarAnimation.getAnimations().size()
                * AVATAR_BOUNCE_LIMIT ? false : true;
        return bool;
    }

    private boolean canChatTabStartBouncing()
    {
        return false;
    }

    private void clearBounceChatTab()
    {
        this.avatarAnimationIsAnimating = false;
        this.avatarAnimation.setAnimationListener(null);
        this.avatarWrapper.clearAnimation();
    }

    private void incrementBounceCount()
    {
        EMConversation message = this.chatTabEntry.getEMConversation();
        Integer count = bounceCount.get(message);
        int i = count == null ? 1 : count + 1;
        bounceCount.put(message, i);
    }

    private void updateContent(CachedChatTabInfo info)
    {
        this.avatar.setImageBitmap(info.bitmap);
        this.title.setText(info.title);
        this.title.setVisibility(VISIBLE);
        this.unreadIndicator.setText("message");
        if (this.canChatTabStartBouncing())
        {
            this.bounceChatTab(false);
        }
        else
        {
            this.clearBounceChatTab();
        }
    }

    class CachedChatTabInfo
    {
        public final Bitmap bitmap;
        public final String title;

        private CachedChatTabInfo(Bitmap bitmap, String title)
        {
            super();
            this.bitmap = bitmap;
            this.title = title;
        }
    }
}
