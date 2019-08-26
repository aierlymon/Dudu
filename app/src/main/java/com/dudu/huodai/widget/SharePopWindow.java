package com.dudu.huodai.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dudu.baselib.utils.MyLog;
import com.dudu.baselib.utils.Utils;
import com.dudu.huodai.R;

public class SharePopWindow extends PopupWindow implements View.OnClickListener {
    private Context mContext;

    private View view;

    private RadioButton txFriend, txQQ, txWx, txWb;
    private RadioGroup mGroup;
    private  TextView txCancel;
    private ImageView backImg;
    public SharePopWindow(Context mContext) {

        this.view = LayoutInflater.from(mContext).inflate(R.layout.popwindow_share, null);

        txFriend = (RadioButton) view.findViewById(R.id.tx_friend_circle);
        txQQ = (RadioButton) view.findViewById(R.id.tx_qq);
        txWx = (RadioButton) view.findViewById(R.id.tx_wx);
        txWb = (RadioButton) view.findViewById(R.id.tx_wb);
        mGroup= ((RadioGroup) view.findViewById(R.id.rg_group));

        txCancel = (TextView) view.findViewById(R.id.tx_cancel);
        backImg= ((ImageView) view.findViewById(R.id.img_back));

        // 设置外部可点击
        this.setOutsideTouchable(true);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框


        //重新定义当前drawable的大小
        for (int i = 0; i < mGroup.getChildCount(); i++) {
            //挨着给每个RadioButton加入drawable限制边距以控制显示大小
            Drawable[] drawables = ((RadioButton) mGroup.getChildAt(i)).getCompoundDrawables();
            //获取drawables
            //给指定的radiobutton设置drawable边界
            Rect r = new Rect(0, 5, (int) mContext.getResources().getDimension(R.dimen.x40), (int) mContext.getResources().getDimension(R.dimen.x40));
            //定义一个Rect边界
            drawables[1].setBounds(r);
            //添加限制给控件
            ((RadioButton) mGroup.getChildAt(i)).setCompoundDrawables(null, drawables[1], null, null);
        }

        /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        int height = (int) (RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setHeight(height);


        // 设置弹出窗体可点击
        this.setFocusable(true);

        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.share_anim);

        txCancel.setOnClickListener(this);
        backImg.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tx_cancel:
            case R.id.img_back:
                dismiss();
                break;
        }
    }
}
