package com.lovebridge.chat.fragment;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import com.lovebridge.R;
import com.lovebridge.chat.moden.TapMetadata;

public class TapCamFragment extends Fragment
{
    public static interface Listener
    {
        public abstract void onNewTap(Uri uri, TapMetadata tapmetadata);

        public abstract boolean onStartRecording();

        public abstract void onTapCamCancel();

        public abstract void onTapCamError(Throwable throwable);
    }

    private class SurfaceHolderCallback implements android.view.SurfaceHolder.Callback
    {
        public void surfaceChanged(SurfaceHolder surfaceholder, int i, int j, int k)
        {
        }

        public void surfaceCreated(SurfaceHolder surfaceholder)
        {
        }

        public void surfaceDestroyed(SurfaceHolder surfaceholder)
        {
        }

        private SurfaceHolderCallback()
        {
            super();
        }

        SurfaceHolderCallback(SurfaceHolderCallback surfaceholdercallback)
        {
            this();
        }
    }

    private final SurfaceHolderCallback surfaceHolderCallback = new SurfaceHolderCallback(null);
    private SurfaceView viewfinder;

    public TapCamFragment()
    {
    }

    public static TapCamFragment newInstance()
    {
        return new TapCamFragment();
    }

    public void cancelRecording()
    {
        ((Listener) getParentFragment()).onTapCamCancel();
    }

    public void onConfigurationChanged(Configuration configuration)
    {
        super.onConfigurationChanged(configuration);
    }

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
    }

    public View onCreateView(LayoutInflater layoutinflater, ViewGroup viewgroup, Bundle bundle)
    {
        View view = layoutinflater.inflate(R.layout.fragment_tapcam, viewgroup, false);
        viewfinder = (SurfaceView) view.findViewById(R.id.tapcam_viewfinder);
        viewfinder.getHolder().addCallback(surfaceHolderCallback);
        return view;
    }

    public void onDestroyView()
    {
        super.onDestroyView();
        viewfinder.getHolder().removeCallback(surfaceHolderCallback);
        viewfinder = null;
    }

    public void onPause()
    {
        super.onPause();
        cancelRecording();
        viewfinder.setOnClickListener(null);
    }

    public void onResume()
    {
        super.onResume();
    }
}
