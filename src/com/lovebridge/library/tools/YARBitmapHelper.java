/*
package com.lovebridge.library.tools;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

 *//**
 * @author yushilong
 * @date 2014-9-30
 * @version 1.0
 */
/*
public class YARBitmapHelper
{
 public static int DEFAULT_IMG_SIZE = 150;
 public static int DEFAULT_IMG_ROUND = 10;
 public static int DEFAULT_LOAD_ICON_PEOPLE = 0;
 public static int DEFAULT_LOAD_ICON_NOTPEOPLE = 0;

 public static Builder getBaseDefaultBuilder()
 {
     DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.EXACTLY).cacheInMemory(true).cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).resetViewBeforeLoading(true);
     return builder;
 }

 public static Builder getNoCacheBuilder()
 {
     DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.EXACTLY).cacheInMemory(false).cacheOnDisc(false).bitmapConfig(Bitmap.Config.RGB_565).resetViewBeforeLoading(true);
     return builder;
 }

 private static int getDefaultPeopleImgResId()
 {
     return DEFAULT_LOAD_ICON_PEOPLE;
 }

 private static int getDefaultNotPeopleImgResId()
 {
     return DEFAULT_LOAD_ICON_NOTPEOPLE;
 }

 public static BitmapDisplayer getDefaultRoundBitmapDisplayer()
 {
     BitmapDisplayer bitmapDisplayer = new RoundedBitmapDisplayer(DEFAULT_IMG_ROUND);
     return bitmapDisplayer;
 }

 public static DisplayImageOptions getDefaultNotRoundImgOptions(int resId)
 {
     Builder builder = getBaseDefaultBuilder();
     builder.showImageForEmptyUri(resId);
     builder.showImageOnFail(resId);
     builder.showImageOnLoading(resId);
     builder.cacheInMemory(true);
     builder.cacheOnDisc(true);
     DisplayImageOptions displayImageOptions = builder.build();
     return displayImageOptions;
 }

 public static DisplayImageOptions getDefaultNotRoundImgAndNoCacheOptions(int resId)
 {
     Builder builder = getNoCacheBuilder();
     builder.showImageForEmptyUri(resId);
     builder.showImageOnFail(resId);
     builder.showImageOnLoading(resId);
     builder.cacheInMemory(false);
     builder.cacheOnDisc(false);
     DisplayImageOptions displayImageOptions = builder.build();
     return displayImageOptions;
 }

 public static DisplayImageOptions getDefaultRoundImgOptions(int resId)
 {
     Builder builder = new DisplayImageOptions.Builder();
     builder.showImageForEmptyUri(resId);
     builder.showImageOnFail(resId);
     builder.showImageOnLoading(resId);
     builder.cacheInMemory(true);
     builder.cacheOnDisc(true);
     builder.considerExifParams(true);
     builder.displayer(getDefaultRoundBitmapDisplayer());
     DisplayImageOptions displayImageOptions = builder.build();
     return displayImageOptions;
 }

 public static String getImagePath(String imgUrl , int size)
 {
     return imgUrl;
 }

 public static void displayDefaultRoundImage(String imgUrl , ImageView imageView , DisplayImageOptions displayImageOptions)
 {
     ImageLoader.getInstance().displayImage(getImagePath(imgUrl, DEFAULT_IMG_SIZE), imageView, displayImageOptions, new AnimateFirstDisplayListener());
 }

 public static void displayDefaultNotRoundImage(String imgUrl , ImageView imageView , DisplayImageOptions displayImageOptions)
 {
     ImageLoader.getInstance().displayImage(getImagePath(imgUrl, DEFAULT_IMG_SIZE), imageView, displayImageOptions, new AnimateFirstDisplayListener());
 }

 *//**
 * 显示人物的圆角图片
 *
 * @param imgUrl
 * @param imageView
 */
/*
public static void displayDefaultPeopleRoundImage(String imgUrl , ImageView imageView)
{
 displayDefaultRoundImage(imgUrl, imageView, getDefaultRoundImgOptions(getDefaultPeopleImgResId()));
}

 *//**
 * 显示人物的非圆角图片
 *
 * @param imgUrl
 * @param imageView
 */
/*
public static void displayDefaultPeopleNotRoundImage(String imgUrl , ImageView imageView)
{
 displayDefaultNotRoundImage(imgUrl, imageView, getDefaultNotRoundImgOptions(getDefaultPeopleImgResId()));
}

 *//**
 * 显示非人物的圆角图片
 *
 * @param imgUrl
 * @param imageView
 */
/*
public static void displayDefaultNotPeopleRoundImage(String imgUrl , ImageView imageView)
{
 displayDefaultNotRoundImage(imgUrl, imageView, getDefaultRoundImgOptions(getDefaultNotPeopleImgResId()));
}

 *//**
 * 显示非人物的非圆角图片
 *
 * @param imgUrl
 * @param imageView
 */
/*
public static void displayDefaultNotPeopleNotRoundImage(String imgUrl , ImageView imageView)
{
 displayDefaultNotRoundImage(imgUrl, imageView, getDefaultNotRoundImgOptions(getDefaultNotPeopleImgResId()));
}

 *//**
 * 显示非人物的非圆角非缓存图片
 *
 * @param imgUrl
 * @param imageView
 */
/*
 * public static void displayDefaultNotPeopleNotRoundNoCacheImage(String imgUrl
 * , ImageView imageView) { displayDefaultNotRoundImage(imgUrl, imageView,
 * getDefaultNotRoundImgAndNoCacheOptions(getDefaultNotPeopleImgResId())); }
 * public static class AnimateFirstDisplayListener extends
 * SimpleImageLoadingListener { static final List<String> displayedImages =
 * Collections.synchronizedList(new LinkedList<String>());
 * @Override public void onLoadingComplete(String imageUri , View view , Bitmap
 * loadedImage) { if (loadedImage != null) { ImageView imageView = (ImageView)
 * view; boolean firstDisplay = !displayedImages.contains(imageUri); if
 * (firstDisplay) { FadeInBitmapDisplayer.animate(imageView, 500);
 * displayedImages.add(imageUri); } } } } public static Bitmap getBitmap(String
 * url) { URL myFileUrl = null; Bitmap bitmap = null; try { myFileUrl = new
 * URL(url); } catch (MalformedURLException e) { e.printStackTrace(); } try {
 * HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
 * conn.setDoInput(true); conn.connect(); InputStream is =
 * conn.getInputStream(); bitmap = BitmapFactory.decodeStream(is); is.close(); }
 * catch (IOException e) { e.printStackTrace(); } return bitmap; } }
 */