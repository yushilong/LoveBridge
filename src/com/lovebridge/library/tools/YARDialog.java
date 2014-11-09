/*
package com.lovebridge.library.tools;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lovebridge.R;

public class YARDialog extends Dialog
{
    private int resId;
    public TextView tv_title;
    public TextView tv_content;
    public Button bt_left;
    public Button bt_right;
    private View view;

    public YARDialog(Context context, int resId)
    {
        super(context, R.style.b5mdialog);
        this.resId = resId;
        init(context);
    }

 *//**
 * 创建一个普通的确认对话框
 * 
 * @param context
 * @return
 */
/*
public static YARDialog createConfirmDialog(Context context)
{
 YARDialog b5mDialog = new YARDialog(context, R.layout.dialog_confirm);
 return b5mDialog;
}

 *//**
 * 创建一个普通的没标题的对话框
 * 
 * @param context
 * @return
 */
/*
 * public static YARDialog createNoTitleConfirmDialog(Context context) {
 * YARDialog b5mDialog = new YARDialog(context, R.layout.dialog_confirm);
 * b5mDialog.tv_title.setVisibility(View.GONE); return b5mDialog; } private void
 * init(Context context) { // TODO Auto-generated method stub view =
 * View.inflate(getContext(), resId, null); tv_title = (TextView)
 * view.findViewById(R.id.tv_dialog_title); tv_content = (TextView)
 * view.findViewById(R.id.tv_dialog_content); bt_left = (Button)
 * view.findViewById(R.id.bt_dialog_left); bt_right = (Button)
 * view.findViewById(R.id.bt_dialog_right); bt_right.setOnClickListener(new
 * View.OnClickListener() {
 * @Override public void onClick(View v) { // TODO Auto-generated method stub
 * dismiss(); } }); } public YARDialog(Context context, boolean cancelable,
 * OnCancelListener cancelListener) { super(context, cancelable,
 * cancelListener); // TODO Auto-generated constructor stub } public
 * YARDialog(Context context) { super(context); // TODO Auto-generated
 * constructor stub }
 * @Override protected void onCreate(Bundle savedInstanceState) { // TODO
 * Auto-generated method stub super.onCreate(savedInstanceState);
 * setContentView(view); setCanceledOnTouchOutside(true); } public void
 * setTitle(String title) { tv_title.setText(title); } public void setTitle(int
 * titleResId) { tv_title.setText(titleResId); } public void setContent(String
 * content) { tv_content.setText(content); } public void setContent(int
 * contentResId) { tv_content.setText(contentResId); } public void
 * setLeftButtonText(String leftButtonText) { bt_left.setText(leftButtonText); }
 * public void setLeftButtonText(int leftButtonTextResId) {
 * bt_left.setText(leftButtonTextResId); } public void setRightButtonText(String
 * RightButtonText) { bt_right.setText(RightButtonText); } public void
 * setRightButtonText(int RightButtonTextResId) {
 * bt_right.setText(RightButtonTextResId); } public void
 * setOnLeftButtonClickListener(android.view.View.OnClickListener
 * onClickListener) { bt_left.setOnClickListener(onClickListener); } public void
 * setOnRightButtonClickListener(android.view.View.OnClickListener
 * onClickListener) { bt_right.setOnClickListener(onClickListener); } }
 */