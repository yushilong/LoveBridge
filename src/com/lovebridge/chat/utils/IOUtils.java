package com.lovebridge.chat.utils;

import java.io.*;

public class IOUtils
{
    private static final int EOF = 0xFFFFFFFF;

    public IOUtils()
    {
        super();
    }

    public static void closeQuietly(Closeable closable)
    {
        if (closable != null)
        {
            try
            {
                closable.close();
            }
            catch (IOException iOException)
            {
            }
        }
    }

    public static void copy(InputStream in, OutputStream out, int bufferSize) throws IOException
    {
        byte[] array_b = new byte[bufferSize];
        while (true)
        {
            int i = in.read(array_b);
            if (EOF == i)
            {
                return;
            }
            out.write(array_b, 0, i);
        }
    }

    public static void copy(InputStream in, Writer out, int bufferSize) throws IOException
    {
        InputStreamReader inputStreamReader = new InputStreamReader(in);
        char[] array_ch = new char[bufferSize];
        while (true)
        {
            int i = inputStreamReader.read(array_ch);
            if (EOF == i)
            {
                return;
            }
            out.write(array_ch, 0, i);
        }
    }
}
