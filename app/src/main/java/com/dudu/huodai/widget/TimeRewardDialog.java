package com.dudu.huodai.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.dudu.baselib.utils.Utils;
import com.dudu.huodai.R;

public class TimeRewardDialog extends Dialog {
    private final String MESSAGE;

    private final onConfirmClickListener ONCONFIRMCLICKLISTENER;
    private final onCancelClickListener ONCANCELCLICKLISTENER;

    private TextView tvMessage;
    private RelativeLayout timerRewardParent;
    private Button close;

    public Button getClose() {
        return close;
    }

    public RelativeLayout getTimerRewardParent() {
        return timerRewardParent;
    }

    public interface onConfirmClickListener {
        void onClick(View view);
    }

    public interface onCancelClickListener {
        void onClick(View view);
    }

    private TimeRewardDialog(@NonNull Context context, String message,
                             onConfirmClickListener onConfirmClickListener, onCancelClickListener onCancelClickListener) {
        super(context, R.style.UpdateDialog);
        this.MESSAGE = message;

        this.ONCONFIRMCLICKLISTENER = onConfirmClickListener;
        this.ONCANCELCLICKLISTENER = onCancelClickListener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_time_reward);
        // setCanceledOnTouchOutside(false);
        initView();
    }

    public static Builder Builder(Context context) {
        return new Builder(context);
    }

    private void initView() {

//如果对话框宽度异常，可以通过下方代码根据设备的宽度来设置弹窗宽度


        WindowManager windowManager = getWindow().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        Point point = new Point();
        display.getSize(point);
        params.width = (int) (point.x);
        params.height = (int) (point.y);
        getWindow().setAttributes(params);


        close = (Button) findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        tvMessage = ((TextView) findViewById(R.id.tx_time_reward));
        timerRewardParent = ((RelativeLayout) findViewById(R.id.time_reward_parent));


        if (!TextUtils.isEmpty(MESSAGE)) {
            tvMessage.setText(MESSAGE);
        }

        TextView tx_reward = (TextView) findViewById(R.id.tx_getit);


      /*  tx_reward.setOnClickListener(view -> {
            if (ONCONFIRMCLICKLISTENER == null) {
                throw new NullPointerException("clicklistener is not null");
            } else {
                ONCONFIRMCLICKLISTENER.onClick(view);
                dismiss();
            }
        });*/
      /*  notDoubling.setOnClickListener(view -> {
            if (ONCANCELCLICKLISTENER == null) {
                throw new NullPointerException("clicklistener is not null");
            } else {
                ONCANCELCLICKLISTENER.onClick(view);
                dismiss();
            }
        });*/
    }

    private void refreshGifeLocation(ImageView gife, LinearLayout dialog_parent) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        dialog_parent.measure(w, h);
        int height = dialog_parent.getMeasuredHeight();
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) gife.getLayoutParams();
        layoutParams.topMargin = (int) (Utils.getScreenWH(getContext().getApplicationContext())[1] / 2 - height / 2 - gife.getLayoutParams().height / 2);
        gife.setLayoutParams(layoutParams);
    }

    public TimeRewardDialog shown() {
        show();
        return this;
    }

    public static class Builder {
        private String mMessage;


        private onConfirmClickListener mOnConfirmClickListener;
        private onCancelClickListener mOnCcancelClickListener;
        private Context mContext;

        private Builder(Context context) {
            this.mContext = context;
        }


        public Builder setMessage(String message) {
            this.mMessage = message;
            return this;
        }

        public Builder setOnConfirmClickListener(onConfirmClickListener confirmclickListener) {
            this.mOnConfirmClickListener = confirmclickListener;
            return this;
        }

        public Builder setOnCancelClickListener(onCancelClickListener onCancelclickListener) {
            this.mOnCcancelClickListener = onCancelclickListener;
            return this;
        }

        public TimeRewardDialog build() {
            return new TimeRewardDialog(mContext, mMessage,
                    mOnConfirmClickListener, mOnCcancelClickListener);
        }


    }

}
