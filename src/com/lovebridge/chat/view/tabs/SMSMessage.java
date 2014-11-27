package com.lovebridge.chat.view.tabs;

import android.content.Context;
import android.net.Uri;

public class SMSMessage extends Message
{
    private final String body;
    private Addresses cachedAddresses;
    private final String number;
    private final Address sender;

    public SMSMessage(long id, String body, int messageType, boolean read, long time, String number, long threadId)
    {
        super(id, messageType, read, time, threadId);
        this.body = body;
        this.number = number;
        this.sender = messageType == 1 ? new Address(number) : null;
    }

    public void delete(Context context)
    {
        Uri uri = Uri.parse("content://sms/" + this.id);
        context.getContentResolver().delete(uri, null, null);
        MessageMutationEvents.broadcast(uri);
    }

    public boolean equals(Object obj)
    {
        boolean bool = true;
        if (this != (((SMSMessage) obj)))
        {
            if (obj == null)
            {
                bool = false;
            }
            else if (this.getClass() != obj.getClass())
            {
                bool = false;
            }
            else if (this.id != ((SMSMessage) obj).id)
            {
                bool = false;
            }
        }
        return bool;
    }

    public Addresses getAddresses()
    {
        if (this.cachedAddresses == null)
        {
            this.cachedAddresses = new Addresses(new Address(this.number));
        }
        return this.cachedAddresses;
    }

    public String getBody()
    {
        return this.body;
    }

    public String getDetailsPreview(Context context)
    {
        return this.getBody();
    }

    public Address getSenderAddress()
    {
        return this.sender;
    }

    public boolean hasImageAttachment()
    {
        return false;
    }

    public boolean hasOtherAttachment()
    {
        return false;
    }

    public int hashCode()
    {
        return (((int) (this.id ^ this.id >>> 0x20))) + 0x1F;
    }

    @Override
    public void resend(Context arg1)
    {
        // TODO Auto-generated method stub
    }
}