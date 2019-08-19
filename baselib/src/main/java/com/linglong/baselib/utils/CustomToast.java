package com.linglong.baselib.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.linglong.baselib.R;

/**
 * createBy ${huanghao}
 * on 2019/6/26
 */
public class CustomToast {
    private static Toast mToast;

    /*
    *  Toast toast = new Toast(context);
        //设置Toast显示位置，居中，向 X、Y轴偏移量均为0
        toast.setGravity(Gravity.CENTER, 0, 0);
        //获取自定义视图
        View view = LayoutInflater.from(context).inflate(R.layout.toast, null);
        TextView tvMessage = (TextView) view.findViewById(R.id.tv_toast);
        //设置文本
        tvMessage.setText(msg);
        //设置视图
        toast.setView(view);
        //设置显示时长
        toast.setDuration(Toast.LENGTH_SHORT);
        //显示
        toast.show();
    * */
    public static void showToast(Context mContext, String text, int duration) {

        Toast mToast = new Toast(mContext);
        mToast.setGravity(Gravity.BOTTOM, 0, 80);
        View view = LayoutInflater.from(mContext).inflate(R.layout.toast_custom, null);
        TextView tx = view.findViewById(R.id.tv_prompt);
        tx.setText(text);
        mToast.setView(view);
        mToast.setDuration(duration);

        mToast.show();
    }

    public static void showToast(Context mContext, String text, int duration,int gravity) {

        Toast mToast = new Toast(mContext);
        mToast.setGravity(gravity, 0, 0);
        View view = LayoutInflater.from(mContext).inflate(R.layout.toast_custom, null);
        TextView tx = view.findViewById(R.id.tv_prompt);
        tx.setText(text);
        mToast.setView(view);
        mToast.setDuration(duration);

        mToast.show();
    }

    public static void showToast(Context mContext, int resId, int duration) {
        showToast(mContext, mContext.getResources().getString(resId), duration);
    }

}
