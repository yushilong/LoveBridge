package com.lovebridge.chat.view;

import android.content.Context;
import android.text.Editable;
import android.text.SpannableString;
import android.text.method.QwertyKeyListener;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.MultiAutoCompleteTextView;
import com.lovebridge.chat.view.tabs.Address;
import com.lovebridge.chat.view.tabs.Addresses;

import java.util.HashSet;

public class RecipientsEditor extends MultiAutoCompleteTextView
{
    private Listener listener;
    private android.widget.MultiAutoCompleteTextView.Tokenizer tokenizer;

    public RecipientsEditor(Context context)
    {
        super(context);
        setup();
    }

    public RecipientsEditor(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
        setup();
    }

    public RecipientsEditor(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
        setup();
    }

    private void autoAddEnteredText()
    {
        Address address = getUncompletedAddress();
        if (address != null)
        {
            addTextWithAddress(address);
        }
    }

    private Address getUncompletedAddress()
    {
        return new Address("");
    }

    private void setup()
    {
    }

    public void addTextWithAddress(Address address)
    {
        SpannableString spannablestring = new SpannableString(String.valueOf('\0'));
        spannablestring.setSpan(address, 0, spannablestring.length(), 33);
        replaceText(spannablestring);
    }

    public Addresses getAddresses()
    {
        HashSet hashset = new HashSet();
        Address address = getUncompletedAddress();
        if (address != null)
        {
            hashset.add(address);
        }
        //        hashset.addAll(getCompletedAddresses().getAddresses());
        return new Addresses(address);
    }

    public Addresses getCompletedAddresses()
    {
        int i = 0;
        HashSet hashset = new HashSet();
        RecipientSpan arecipientspan[] = getText().getSpans(0, length(), RecipientSpan.class);
        int j = arecipientspan.length;
        do
        {
            if (i >= j)
            {
                return new Addresses(hashset);
            }
            hashset.add(arecipientspan[i].getAddress());
            i++;
        }
        while (true);
    }

    public InputConnection onCreateInputConnection(EditorInfo editorinfo)
    {
        InputConnection inputconnection = super.onCreateInputConnection(editorinfo);
        editorinfo.imeOptions = 0xbfffffff & editorinfo.imeOptions;
        return inputconnection;
    }

    public void refilter()
    {
        performFiltering(getText(), 0);
    }

    public void replaceText(CharSequence charsequence)
    {
        super.replaceText(charsequence);
        Editable editable = getText();
        Object aobj[] = editable.getSpans(0, length(), Object.class);
        int i = 0;
        do
        {
            if (i >= aobj.length)
            {
                return;
            }
            Object obj = aobj[i];
            if (QwertyKeyListener.class.equals(obj.getClass().getEnclosingClass()))
            {
                editable.removeSpan(obj);
            }
            i++;
        }
        while (true);
    }

    public void setListener(Listener listener1)
    {
        listener = listener1;
    }

    public void showDropDown()
    {
    }

    public static interface Listener
    {
        public abstract void onAddressesChanged();
    }
}
