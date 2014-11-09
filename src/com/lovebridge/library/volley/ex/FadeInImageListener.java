/**
 * Copyright 2013 Mani Selvaraj
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lovebridge.library.volley.ex;

import java.lang.ref.WeakReference;

import android.view.animation.AlphaAnimation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.lovebridge.library.volley.VolleyError;
import com.lovebridge.library.volley.toolbox.ImageLoader;
import com.lovebridge.library.volley.toolbox.ImageLoader.ImageContainer;

/**
 * Implements ImageListener interface to perform fade-In animation of image when
 * download completes.
 * 
 * @author Mani Selvaraj
 */
public class FadeInImageListener implements ImageLoader.ImageListener {
    WeakReference<ImageView> mImageView;
    int loadErrorResId;
    private long durationMillis = 500l;

    public FadeInImageListener(ImageView image, int loadErrorResId) {
        mImageView = new WeakReference<ImageView>(image);
        this.loadErrorResId = loadErrorResId;
    }

    @Override
    public void onErrorResponse(VolleyError arg0) {
        if (mImageView.get() != null) {
            mImageView.get().setImageResource(loadErrorResId);
        }
    }

    @Override
    public void onResponse(ImageContainer response, boolean arg1) {
        if (mImageView.get() != null) {
            ImageView image = mImageView.get();
            if (response.getBitmap() != null) {
                AlphaAnimation fadeImage = new AlphaAnimation(0, 1);
                fadeImage.setDuration(durationMillis);
                fadeImage.setInterpolator(new DecelerateInterpolator());
                image.startAnimation(fadeImage);
                image.setImageBitmap(response.getBitmap());
            } else {
                image.setImageResource(loadErrorResId);
            }
        }
    }
}
