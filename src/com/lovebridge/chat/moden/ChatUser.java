
package com.lovebridge.chat.moden;

import com.easemob.chat.EMContact;

public class ChatUser extends EMContact
{
    private int unreadMsgCount;
    private String header;

    public String getHeader()
    {
        return header;
    }

    public void setHeader(String header)
    {
        this.header = header;
    }

    public int getUnreadMsgCount()
    {
        return unreadMsgCount;
    }

    public void setUnreadMsgCount(int unreadMsgCount)
    {
        this.unreadMsgCount = unreadMsgCount;
    }

    @Override
    public int hashCode()
    {
        return 17 * getUsername().hashCode();
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == null || !(o instanceof ChatUser))
        {
            return false;
        }
        return getUsername().equals(((ChatUser) o).getUsername());
    }

    @Override
    public String toString()
    {
        return nick == null ? username : nick;
    }
}
