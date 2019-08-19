package com.linglong.baselib.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.KeyEvent;

import androidx.fragment.app.FragmentActivity;

import com.linglong.baselib.R;
import com.linglong.baselib.widget.PermissionDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * createBy ${huanghao}
 * on 2019/7/3
 */
public class RxPermissionUtil {


    private Map<String, String> permissionMap;

    private RxPermissionUtil() {
        permissionMap = new HashMap<>();
        permissionMap.put("android.permission.REQUEST_INSTALL_PACKAGES", "安装应用");
        permissionMap.put("android.permission.READ_EXTERNAL_STORAGE", "读取SD卡");
        permissionMap.put("android.permission.WRITE_EXTERNAL_STORAGE", "写入SD卡");
        permissionMap.put("android.permission.READ_PHONE_STATE", "读取手机状态");
    }


    private static RxPermissionUtil rxPermissionUtil;


    //肯定在主线程，不用同步
    public static RxPermissionUtil getInstance() {
        if (rxPermissionUtil == null) {
            rxPermissionUtil = new RxPermissionUtil();
        }
        return rxPermissionUtil;
    }

    private StringBuilder builder = new StringBuilder();
    ;
    private int leng = 0;

    private PermissionDialog build;

    private WeakReference<Activity> weakReference;

    public void permission(Activity activity, String[] list) {
        weakReference = new WeakReference<>(activity);
        RxPermissions rxPermissions = new RxPermissions((FragmentActivity) weakReference.get());
        rxPermissions.requestEach(list).subscribe(permission -> {

            MyLog.i("leng: " + leng);
            if (permission.granted) {
                ++leng;
                MyLog.i("获取了" + permission.name + "权限");
            } else if (permission.shouldShowRequestPermissionRationale) {
                ++leng;
                //填写弹窗+权限名字
                if (leng == list.length) {
                    builder.append("[" + permissionMap.get(permission.name) + "]权限");
                } else {
                    builder.append("[" + permissionMap.get(permission.name) + "]-");
                }
                MyLog.i("应该需要获取" + permission.name + "权限");
            } else {
                ++leng;
            }

            MyLog.i("builder.tostring: "+builder.toString()+"  len: "+list.length+"  leng: "+leng);

            if (leng != 0 && leng == list.length) {

                if(builder.lastIndexOf("权限")==-1&&builder.lastIndexOf("-")!=-1){
                    builder.replace(builder.length()-1,builder.length(),"权限");
                }
                if (!TextUtils.isEmpty(builder.toString())) {
                    build = PermissionDialog.Builder(weakReference.get())
                            .setTitle(activity.getString(R.string.power_ps))
                            .setMessage("需要获取" + builder.toString())
                            .setOnConfirmClickListener("去设置", view -> {
                                //跳转app权限设置
                                Intent localIntent = new Intent();
                                localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                if (Build.VERSION.SDK_INT >= 9) {
                                    localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                    localIntent.setData(Uri.fromParts("package", activity.getPackageName(), null));
                                } else if (Build.VERSION.SDK_INT <= 8) {
                                    localIntent.setAction(Intent.ACTION_VIEW);
                                    localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
                                    localIntent.putExtra("com.android.settings.ApplicationPkgName", activity.getPackageName());
                                }
                                //把builder为Null
                                PermissionDialog.Builder builder=build.getBuilder();
                                if(builder!=null){
                                    builder.setmContext(null);
                                    builder=null;
                                }
                                build.dismiss();
                                build=null;
                                weakReference.get().startActivity(localIntent);
                            }).build();
                    build.setOnKeyListener((dialog, keyCode, event) -> {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            return true;
                        } else {
                            return false; //默认返回 false
                        }
                    });
                    build.shown();

                }
            }
        });


    }
}