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
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.dudu.huodai.R;

public class MoneyDialog extends Dialog {
    private final String TITLE;
    private final String MESSAGE;

    private final onConfirmClickListener ONCONFIRMCLICKLISTENER;
    private final onCancelClickListener ONCANCELCLICKLISTENER;

    public interface onConfirmClickListener {
        void onClick(View view);
    }

    public interface onCancelClickListener {
        void onClick(View view);
    }

    private MoneyDialog(@NonNull Context context, String title, String message,
                        onConfirmClickListener onConfirmClickListener, onCancelClickListener onCancelClickListener) {
        super(context, R.style.UpdateDialog);
        this.TITLE = title;
        this.MESSAGE = message;

        this.ONCONFIRMCLICKLISTENER = onConfirmClickListener;
        this.ONCANCELCLICKLISTENER = onCancelClickListener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_money);
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
        params.width = (int) (point.x );
        params.height = (int) (point.y);
        getWindow().setAttributes(params);

        Button notDoubling = findViewById(R.id.notdoubling);
        Button douBling = findViewById(R.id.doubling);
        TextView txNum = findViewById(R.id.tx_num);
        TextView tvMessage = findViewById(R.id.tx_title);

        if (!TextUtils.isEmpty(TITLE)) {
            txNum.setText(TITLE);
        }
        if (!TextUtils.isEmpty(MESSAGE)) {
            tvMessage.setText(MESSAGE);
        }


        douBling.setOnClickListener(view -> {
            if (ONCONFIRMCLICKLISTENER == null) {
                throw new NullPointerException("clicklistener is not null");
            } else {
                ONCONFIRMCLICKLISTENER.onClick(view);
            }
        });
        notDoubling.setOnClickListener(view -> {
            if (ONCANCELCLICKLISTENER == null) {
                throw new NullPointerException("clicklistener is not null");
            } else {
                ONCANCELCLICKLISTENER.onClick(view);
            }
        });
    }

    public MoneyDialog shown() {
        show();
        return this;
    }

    public static class Builder {
        private String mTitle;
        private String mMessage;

        private onConfirmClickListener mOnConfirmClickListener;
        private onCancelClickListener mOnCcancelClickListener;
        private Context mContext;

        private Builder(Context context) {
            this.mContext = context;
        }

        public Builder setTitle(String title) {
            this.mTitle = title;
            return this;
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

        public MoneyDialog build() {
            return new MoneyDialog(mContext, mTitle, mMessage,
                    mOnConfirmClickListener, mOnCcancelClickListener);
        }
    }

}