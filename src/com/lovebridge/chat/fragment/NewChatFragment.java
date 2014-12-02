package com.lovebridge.chat.fragment;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.lovebridge.R;
import com.lovebridge.chat.utils.MMSSMSUtils;
import com.lovebridge.chat.utils.SoundUtils;
import com.lovebridge.chat.view.RecipientsEditor;
import com.lovebridge.chat.view.tabs.Addresses;
import com.lovebridge.chat.view.tabs.ChatTabEntry;

public class NewChatFragment extends Fragment
{
    private static final String ARG_ADDRESSES = "addresses";
    private static final String ARG_DEFAULT_TEXT = "default_text";
    private static FocusedTextView lastFocusedObject;
    private static Editable recipientsDraft = null;
    private static int selectionEnd = 0;
    private static int selectionStart = 0;
    private final OttStatusChangeEvents.Listener listener = new OttStatusChangeEvents.Listener()
    {
        public void onStatusChanged()
        {
            reloadOttViews();
        }
    };
    private final DataSetObserver recipientsAdapterObserver = new DataSetObserver()
    {
        public void onChanged()
        {
            onAddressesChanged();
        }

        public void onInvalidated()
        {
            onAddressesChanged();
        }
    };
    private ComposerFragment composer;
    private TextView networkIndicator;
    private RecipientsEditor recipientsEditor;

    public NewChatFragment()
    {
    }

    public static NewChatFragment newInstance(String as[], String s)
    {
        NewChatFragment newchatfragment = new NewChatFragment();
        Bundle bundle = new Bundle();
        if (as != null && as.length > 0)
        {
            bundle.putStringArray("addresses", as);
        }
        if (s != null)
        {
            bundle.putString("default_text", s);
        }
        if (!bundle.isEmpty())
        {
            newchatfragment.setArguments(bundle);
        }
        return newchatfragment;
    }

    private void onAddressesChanged()
    {
    }

    private void reloadOttViews()
    {
        updateNetworkIndicator();
    }

    private void retrieveEligibility(Addresses addresses)
    {
    }

    private void updateNetworkIndicator()
    {
    }

    public void onActivityResult(int i, int j, Intent intent)
    {
        super.onActivityResult(i, j, intent);
        composer.handleActivityResult(i, j, intent);
    }

    public View onCreateView(LayoutInflater layoutinflater, ViewGroup viewgroup, Bundle bundle)
    {
        View view = layoutinflater.inflate(R.layout.fragment_new_chat, viewgroup, false);
        networkIndicator = (TextView) view.findViewById(R.id.network_indicator);
        recipientsEditor = (RecipientsEditor) view.findViewById(R.id.recipients);
        recipientsEditor.setListener(new RecipientsEditor.Listener()
        {
            public void onAddressesChanged()
            {
                updateNetworkIndicator();
            }
        });
        Bundle bundle1 = getArguments();
        String s;
        final String prefillAddresses[];
        ListView listview;
        if (bundle1 != null)
        {
            recipientsEditor.setText(null);
            lastFocusedObject = FocusedTextView.COMPOSER;
            prefillAddresses = bundle1.getStringArray("addresses");
            s = bundle1.getString("default_text");
        }
        else
        {
            recipientsEditor.setText(recipientsDraft);
            recipientsEditor.setSelection(selectionStart, selectionEnd);
            s = null;
            prefillAddresses = null;
        }
        recipientsEditor.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            public void onFocusChange(View view1, boolean flag)
            {
                if (view1.equals(recipientsEditor) && flag)
                {
                    NewChatFragment.lastFocusedObject = FocusedTextView.RECIPIENT_EDITOR;
                }
            }
        });
        listview = (ListView) view.findViewById(R.id.suggestions);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView adapterview, View view1, int i, long l)
            {
                //                recipientsEditor.replaceText(adapter.getFilter().convertResultToString(adapter.getItem(i)));
                recipientsEditor.requestFocus();
                ((InputMethodManager) getActivity().getSystemService("input_method")).showSoftInput(recipientsEditor, 1);
            }
        });
        listview.setOnScrollListener(new AbsListView.OnScrollListener()
        {
            public void onScroll(AbsListView abslistview, int i, int j, int k)
            {
            }

            public void onScrollStateChanged(AbsListView abslistview, int i)
            {
                if (i == 1)
                {
                    composer.hideKeyboard();
                    ((EmojiPickerFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.emoji_picker_fragment)).hide();
                }
            }
        });
        composer = (ComposerFragment) getFragmentManager().findFragmentById(R.id.composer);
        composer.getView().setVisibility(View.VISIBLE);
        composer.setChat(0L, s, new ComposerFragment.Listener()
        {
            public Addresses getAddresses()
            {
                return recipientsEditor.getAddresses();
            }

            public void onTextViewFocus()
            {
                NewChatFragment.lastFocusedObject = FocusedTextView.COMPOSER;
            }

            public void sentMessage(long l, boolean flag, ComposerFragment.Listener.PlaceholderType placeholdertype, String s1)
            {
                if (SoundUtils.shouldPlayChatSounds(getActivity()))
                {
                    SoundUtils.playSound(getActivity(), R.raw.send_message);
                }
                if (l == 0L)
                {
                    l = MMSSMSUtils.getOrCreateThreadId(getAddresses().getPhoneNumbers());
                }
                ((ChatTabEntry.Listener) getActivity()).onChatClick(l, true);
                recipientsEditor.setText(null);
                NewChatFragment.lastFocusedObject = FocusedTextView.NO_FOCUS;
            }

            public void startActivityForResultHelper(Intent intent, int i)
            {
                startActivityForResult(intent, i);
            }
        });
        OttStatusChangeEvents.addListener(listener);
        return view;
    }

    public void onDestroyView()
    {
        super.onDestroyView();
        recipientsDraft = recipientsEditor.getText();
        selectionStart = recipientsEditor.getSelectionStart();
        selectionEnd = recipientsEditor.getSelectionEnd();
//        recipientsEditor.getAdapter().unregisterDataSetObserver(recipientsAdapterObserver);
        recipientsEditor.setListener(null);
        OttStatusChangeEvents.removeListener(listener);
    }

    public void onStart()
    {
        super.onStart();
        reloadOttViews();
        if (lastFocusedObject == FocusedTextView.COMPOSER)
        {
            composer.showKeyboard();
            return;
        }
        else
        {
            recipientsEditor.requestFocus();
            ((InputMethodManager) getActivity().getSystemService("input_method")).showSoftInput(recipientsEditor, 1);
            return;
        }
    }

    static
    {
        lastFocusedObject = FocusedTextView.NO_FOCUS;
    }

    public enum FocusedTextView
    {
        NO_FOCUS, RECIPIENT_EDITOR, COMPOSER
    }
}
