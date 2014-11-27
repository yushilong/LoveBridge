package com.lovebridge.chat.utils;

abstract class Setting
{
    private final int key;

    Setting(int key)
    {
        super();
        this.key = key;
    }

    protected int getKey()
    {
        return this.key;
    }
}