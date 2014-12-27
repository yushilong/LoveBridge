package com.lovebridge.activitys.imagefactory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ViewFlipper;

import com.lovebridge.R;
import com.lovebridge.library.YARActivity;
import com.lovebridge.library.tools.YARDisplayUtil;
import com.lovebridge.utils.PhotoUtils;
import com.lovebridge.widget.Navigation;

public class ImageFactoryActivity extends YARActivity {

    private ViewFlipper mVfFlipper;
    private Button mBtnLeft;
    private Button mBtnRight;

    private ImageFactoryCrop mImageFactoryCrop;
    private ImageFactoryFliter mImageFactoryFliter;
    private String mPath;
    private String mNewPath;
    private int mIndex = 0;
    private String mType;

    public static final String TYPE = "type";
    public static final String CROP = "crop";
    public static final String FLITER = "fliter";
    private Navigation mHeaderLayout;


    @Override
    public void onBackPressed() {
        if (mIndex == 0) {
            setResult(RESULT_CANCELED);
            finish();
        } else {
            if (FLITER.equals(mType)) {
                setResult(RESULT_CANCELED);
                finish();
            } else {
                mIndex = 0;
                initImageFactory();
                mVfFlipper.setInAnimation(ImageFactoryActivity.this,
                        R.anim.slide_in_from_right);
                mVfFlipper.setOutAnimation(ImageFactoryActivity.this,
                        R.anim.slide_out_to_right);
                mVfFlipper.showPrevious();
            }
        }
    }


    @Override
    public int doGetContentViewId() {
        return R.layout.activity_imagefactory;
    }

    @Override
    public void doInitSubViews(View containerView) {
        mHeaderLayout = (Navigation) findViewById(R.id.imagefactory_header);
        mHeaderLayout.iv_navigation_right.setOnClickListener(new OnRightImageButtonClickListener());
        mVfFlipper = (ViewFlipper) findViewById(R.id.imagefactory_vf_viewflipper);
        mBtnLeft = (Button) findViewById(R.id.imagefactory_btn_left);
        mBtnRight = (Button) findViewById(R.id.imagefactory_btn_right);
        mBtnLeft.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mIndex == 0) {
                    setResult(RESULT_CANCELED);
                    finish();
                } else {
                    if (FLITER.equals(mType)) {
                        setResult(RESULT_CANCELED);
                        finish();
                    } else {
                        mIndex = 0;
                        initImageFactory();
                        mVfFlipper.setInAnimation(ImageFactoryActivity.this,
                                R.anim.slide_in_from_left);
                        mVfFlipper.setOutAnimation(ImageFactoryActivity.this,
                                R.anim.slide_out_to_right);
                        mVfFlipper.showPrevious();
                    }
                }
            }
        });
        mBtnRight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mIndex == 1) {
                    mNewPath = PhotoUtils.savePhotoToSDCard(mImageFactoryFliter
                            .getBitmap());
                    Intent intent = new Intent();
                    intent.putExtra("path", mNewPath);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    mNewPath = PhotoUtils.savePhotoToSDCard(mImageFactoryCrop
                            .cropAndSave());
                    mIndex = 1;
                    initImageFactory();
                    mVfFlipper.setInAnimation(ImageFactoryActivity.this,
                            R.anim.slide_in_from_left);
                    mVfFlipper.setOutAnimation(ImageFactoryActivity.this,
                            R.anim.slide_out_to_right);
                    mVfFlipper.showNext();
                }
            }
        });
    }

    @Override
    public void doInitDataes() {
        mPath = getIntent().getStringExtra("path");
        mType = getIntent().getStringExtra(TYPE);
        mNewPath = new String(mPath);
        if (CROP.equals(mType)) {
            mIndex = 0;
        } else if (FLITER.equals(mType)) {
            mIndex = 1;
            mVfFlipper.showPrevious();
        }
        initImageFactory();
    }

    @Override
    public void doAfter() {

    }

    private void initImageFactory() {
        switch (mIndex) {
            case 0:
                if (mImageFactoryCrop == null) {
                    mImageFactoryCrop = new ImageFactoryCrop(this,
                            mVfFlipper.getChildAt(0));
                }
                mImageFactoryCrop.init(mPath, YARDisplayUtil.getScreenWidth(), YARDisplayUtil.getScreenHeight());
                mHeaderLayout.setNavigationTitle("裁切图片");
                mBtnLeft.setText("取    消");
                mBtnRight.setText("确    认");

                break;

            case 1:
                if (mImageFactoryFliter == null) {
                    mImageFactoryFliter = new ImageFactoryFliter(this,
                            mVfFlipper.getChildAt(1));
                }
                mImageFactoryFliter.init(mNewPath);
                mHeaderLayout.setNavigationTitle("图片滤镜");
                mBtnLeft.setText("取    消");
                mBtnRight.setText("完    成");
                break;
        }
    }

    private class OnRightImageButtonClickListener implements
            OnClickListener {
        @Override
        public void onClick(View v) {
            switch (mIndex) {
                case 0:
                    if (mImageFactoryCrop != null) {
                        mImageFactoryCrop.Rotate();
                    }
                    break;

                case 1:
                    if (mImageFactoryFliter != null) {
                        mImageFactoryFliter.Rotate();
                    }
                    break;
            }
        }
    }
}
