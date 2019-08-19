package com.linglong.baselib.utils;

import android.app.Activity;

import com.linglong.baselib.widget.CustomDialog;

import java.lang.ref.WeakReference;

/**
 * createBy ${huanghao}
 * on 2019/7/3
 */
public class LoadDialogUtil {
    private LoadDialogUtil() {
    }

    private WeakReference<Activity> weakReference;
    private CustomDialog loadDialog;

    private static LoadDialogUtil loadDialogUtil;
    private String msg;
    private int theme;//加载展示的

    /**
     *
     * @param mContext 上下文（只能传activity不能传application，不会内存泄漏）
     * @param msg //加载展示的文字内容
     * @param theme //加载图标的主题
     * @return
     */
    public static LoadDialogUtil getInstance(Activity mContext,String msg,int theme) {
        if (loadDialogUtil == null) {
            loadDialogUtil = new LoadDialogUtil();
        }
        loadDialogUtil.weakReference = new WeakReference<>(mContext);
        loadDialogUtil.msg=msg;
        loadDialogUtil.theme=theme;
        return loadDialogUtil;
    }

    public void getLoadDialog() {
        if (loadDialog != null) {
            cancel();
            loadDialog = null;
        }
        CustomDialog.Builder builder = new CustomDialog.Builder(weakReference.get());
        builder.setMessage(msg);
        builder.setTheme(theme);
        loadDialog = builder.create();
        loadDialog.setCanceledOnTouchOutside(false);

    }

    public void show() {
        getLoadDialog();
        if (loadDialog != null) {
            if(weakReference.get()!=null&&!weakReference.get().isFinishing())
            loadDialog.show();
        }
    }

    public void cancel() {
        if (loadDialog != null) {
            loadDialog.dismiss();
            loadDialog = null;
        }
    }

}
