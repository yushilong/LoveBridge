package com.lovebridge.chat.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import com.lovebridge.R;
import com.lovebridge.chat.fragment.ComposerFragment.Listener.PlaceholderType;
import com.lovebridge.chat.fragment.EmojiPickerFragment.ComposerReceiver;
import com.lovebridge.chat.fragment.EmojiPickerFragment.State;
import com.lovebridge.chat.moden.TapMetadata;
import com.lovebridge.chat.utils.EmojiFilter;
import com.lovebridge.chat.utils.SoundUtils;
import com.lovebridge.chat.view.tabs.Address;
import com.lovebridge.chat.view.tabs.Addresses;

import java.util.HashMap;
import java.util.Map;

public class ComposerFragment extends Fragment implements com.lovebridge.chat.fragment.TapCamFragment.Listener {
    public interface Listener {
        public enum PlaceholderType {
            IMAGE, TEXT
        }

        Addresses getAddresses();

        void onTextViewFocus();

        void sentMessage(long arg1, boolean arg2, PlaceholderType arg3, String arg4);

        void startActivityForResultHelper(Intent arg1, int arg2);
    }

    private static final int MAX_LINES_LAND = 2;
    private static final int MAX_LINES_PORT = 8;
    private static final int REQUEST_CAMERA_PICKER = 0x64;
    private static final int REQUEST_CONFIRM_TAP = 0x65;
    private View cameraButton;
    private View.OnClickListener cameraGalleryOnClickListener;
    private static Map<Long, Parcelable> drafts = new HashMap<Long, Parcelable>();
    private EditText editText;
    private View emojiButton;
    private EmojiPickerFragment emojiPickerFragment;
    private View keyboardButton;
    private Listener listener;
    private View sendButton;
    private boolean tapCamCurrentlyOn;
    private TapCamFragment tapCamFragment;
    private View tapCamFragmentContainer;
    private long threadId;
    private boolean viewsEnabled;
    private String toChatUsername;

    public String getToChatUsername() {
        return toChatUsername;
    }

    public void setToChatUsername(String toChatUsername) {
        this.toChatUsername = toChatUsername;
    }

    static {
        ComposerFragment.drafts = new HashMap<Long, Parcelable>();
    }

    public ComposerFragment() {
        super();
        this.viewsEnabled = false;
        this.tapCamCurrentlyOn = false;
        this.cameraGalleryOnClickListener = new View.OnClickListener() {
            public void onClick(View v) {
            }
        };
    }

    public void handleActivityResult(int requestCode, int resultCode, Intent intent) {
        if ((requestCode == REQUEST_CAMERA_PICKER || requestCode == REQUEST_CONFIRM_TAP) && resultCode == 0xFFFFFFFF) {
            final FragmentActivity fragmentActivity = this.getActivity();
            SharedPreferences sharedPreferences = PreferenceManager
                    .getDefaultSharedPreferences(fragmentActivity);
            if (sharedPreferences.contains("ott_auth_dialog_after_broadcast")) {
                return;
            }
            sharedPreferences.edit().putBoolean("ott_auth_dialog_after_broadcast", true).apply();
        }
    }

    public boolean hasText() {
        boolean bool = TextUtils.isEmpty(this.editText.getText().toString()) ? false : true;
        return bool;
    }

    public void hideKeyboard() {
        hideInputKeyboard(getActivity(), editText);
    }

    public static void hideInputKeyboard(Context context, View view) {
        if (null != view) {
            InputMethodManager inputMethodManager = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.emojiPickerFragment = (EmojiPickerFragment) this.getFragmentManager().findFragmentById(
                R.id.emoji_picker_fragment);
        this.emojiPickerFragment.setComposerReceiver(new ComposerReceiver() {
            public void onBackspacePressed() {
                ComposerFragment.this.editText.dispatchKeyEvent(new KeyEvent(0, 0x43));
            }

            public void onInsertEmoji(String string) {
                ComposerFragment.this.editText.getText().replace(ComposerFragment.this.editText.getSelectionStart(),
                        ComposerFragment.this.editText.getSelectionEnd(), string);
            }

            @Override
            public void onStateChanged(State state) {
                int i = 8;
                int i1 = 0;
                View view = ComposerFragment.this.emojiButton;
                int i2 = state == State.EMOJI_PICKER ? i : 0;
                view.setVisibility(i2);
                View view1 = ComposerFragment.this.keyboardButton;
                if (state != State.EMOJI_PICKER) {
                    i1 = i;
                }
                view1.setVisibility(i1);
            }
        });
    }

    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        this.setComposerMaxLines(config.orientation);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_composer, container, false);
        this.cameraButton = view.findViewById(R.id.camera_button);
        this.sendButton = view.findViewById(R.id.send_button);
        this.emojiButton = view.findViewById(R.id.emoji_button);
        this.keyboardButton = view.findViewById(R.id.keyboard_button);
        this.editText = (EditText) view.findViewById(R.id.edit_text);
        this.editText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                int i = 8;
                boolean bool = TextUtils.isEmpty(ComposerFragment.this.editText.getText());
                View view = ComposerFragment.this.cameraButton;
                int i1 = bool ? 0 : i;
                view.setVisibility(i1);
                View view1 = ComposerFragment.this.sendButton;
                if (!bool) {
                    i = 0;
                }
                view1.setVisibility(i);
                ComposerFragment.this.updateTapCamVisibility();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        this.editText.setFilters(new InputFilter[]{new EmojiFilter()});
        View view1 = this.sendButton;
        boolean bool = SoundUtils.shouldPlayChatSounds(this.getActivity()) ? false : true;
        view1.setSoundEffectsEnabled(bool);
        this.setComposerMaxLines(this.getResources().getConfiguration().orientation);
        this.tapCamFragmentContainer = view.findViewById(R.id.tapcam_fragment_container);
        return view;
    }

    public void onDestroyView() {
        super.onDestroyView();
        ComposerFragment.drafts.put(Long.valueOf(this.threadId), this.editText.onSaveInstanceState());
    }

    public void onNewTap(Uri uri, TapMetadata tapMetadata) {
    }

    public void onStart() {
        super.onStart();
        this.updateTapCamVisibility();
    }

    public boolean onStartRecording() {
        View.OnClickListener view = null;
        boolean bool = true;
        Addresses addresses = this.listener.getAddresses();
        if (addresses.size() != 1) {
            bool = false;
        }
        if (bool) {
            this.cameraButton.setOnClickListener(view);
        }
        return bool;
    }

    public void onTapCamCancel() {
        this.cameraButton.setOnClickListener(this.cameraGalleryOnClickListener);
    }

    public void onTapCamError(Throwable t) {
        this.updateTapCamVisibility();
        this.cameraButton.setOnClickListener(this.cameraGalleryOnClickListener);
    }

    public void setChat(long threadId, String defaultText, ComposerFragment.Listener listener) {
        Address address = null;
        if (this.tapCamFragment != null) {
            this.tapCamFragment.cancelRecording();
        }
        ComposerFragment.drafts.put(Long.valueOf(this.threadId), this.editText.onSaveInstanceState());
        this.threadId = threadId;
        this.listener = listener;
        if (!TextUtils.isEmpty(defaultText)) {
            this.editText.setText(defaultText);
            if (!this.hasText()) {
                this.emojiPickerFragment.hide();
            }
        }
        if (ComposerFragment.drafts.containsKey(Long.valueOf(threadId))) {
            this.editText.onRestoreInstanceState(ComposerFragment.drafts.get(Long.valueOf(threadId)));
            ComposerFragment.drafts.remove(Long.valueOf(threadId));
        } else {
            this.editText.setText(((CharSequence) address));
        }
        this.enableViews();
    }

    public void setEligibleForTapCamDefaultModeTest() {
        SharedPreferences.Editor sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity())
                .edit();
        sharedPreferences.putBoolean("tapcam_default_mode_eligible", true);
        sharedPreferences.apply();
        this.updateTapCamVisibility();
    }

    public void setTapCamAllowed(boolean tapCamAllowed) {
        this.updateTapCamVisibility();
    }

    public void showKeyboard() {
        this.editText.requestFocus();
        ((InputMethodManager) this.getActivity().getSystemService("input_method")).showSoftInput(this.editText, 1);
    }

    private void enableViews() {
        if (!this.viewsEnabled) {
            this.viewsEnabled = true;
            this.editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if ((v.equals(ComposerFragment.this.editText)) && (hasFocus)) {
                        ComposerFragment.this.listener.onTextViewFocus();
                    }
                }
            });
            this.cameraButton.setOnClickListener(this.cameraGalleryOnClickListener);
            this.sendButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Addresses addresses = ComposerFragment.this.listener.getAddresses();
                    if (addresses.size() == 0) {
                        ComposerFragment.this.handleNoAddresses();
                    } else {
                        String string = ComposerFragment.this.editText.getText().toString();
                        if (TextUtils.isEmpty(string)) {
                            return;
                        }
                        ComposerFragment.this.editText.setText("");
                        ComposerFragment.this.listener.sentMessage(ComposerFragment.this.threadId, true,
                                PlaceholderType.TEXT, string);
                    }
                }
            });
            this.emojiButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ComposerFragment.this.emojiPickerFragment.show();
                    ComposerFragment.this.editText.requestFocus();
                    ComposerFragment.this.hideKeyboard();
                }
            });
            this.keyboardButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ComposerFragment.this.showKeyboard();
                }
            });
        }
    }

    private void handleNoAddresses() {
        Toast.makeText(this.getActivity(), this.getResources().getString(R.string.compose_error_no_addresses), 0)
                .show();
    }

    private void setComposerMaxLines(int orientation) {
        if (this.editText != null) {
            EditText editText = this.editText;
            int i = orientation == 1 ? MAX_LINES_PORT : MAX_LINES_LAND;
            editText.setMaxLines(i);
        }
    }

    private boolean shouldShowTapCam() {
        return false;
    }

    private void updateTapCamVisibility() {
        boolean bool = this.shouldShowTapCam();
        if (bool != this.tapCamCurrentlyOn) {
            this.tapCamCurrentlyOn = bool;
            View view = this.tapCamFragmentContainer;
            int i = bool ? 0 : 8;
            view.setVisibility(i);
            if (!bool) {
                return;
            }
            FragmentManager fragmentManager = this.getChildFragmentManager();
            this.tapCamFragment = (TapCamFragment) fragmentManager.findFragmentById(R.id.tapcam_fragment_container);
            if (this.tapCamFragment != null) {
                return;
            }
            this.tapCamFragment = TapCamFragment.newInstance();
            fragmentManager.beginTransaction().replace(R.id.tapcam_fragment_container, this.tapCamFragment).commit();
        }
    }
}
