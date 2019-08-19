package com.linglong.huodai.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.linglong.baselib.utils.MyLog;
import com.linglong.huodai.R;
import com.linglong.huodai.mvp.model.postbean.LoanFraTypeBean;
import com.linglong.huodai.mvp.model.postbean.LoanMoneyBean;
import com.linglong.huodai.ui.adapter.LoanSpinnerRevAdapter;
import com.linglong.huodai.ui.adapter.base.BaseMulDataModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.WINDOW_SERVICE;

public class LoanFraPopWindow extends PopupWindow {
    public static final int TYPE = 1;
    public static final int LOAN = 2;
    private int currentItem;

    public interface CancelListener {
        void cancel();
    }

    CancelListener cancelListener;

    public void setCancelListener(CancelListener cancelListener) {
        this.cancelListener = cancelListener;
    }

    @BindView(R.id.spinner_recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.spinner_back_img)
    ImageView imageView_back;

    List<BaseMulDataModel> loadNums;

    private LoanSpinnerRevAdapter loanSpinnerRevAdapter;


    public int getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(int currentItem) {
        this.currentItem = currentItem;
    }

    public LoanFraPopWindow(Context context) {
        super(context);
        loadNums = new ArrayList<>();
        String[] loanNumArray = context.getResources().getStringArray(R.array.loan_num);
        for (int i = 0; i < loanNumArray.length; i++) {
            LoanMoneyBean loanMoneyBean = new LoanMoneyBean();
            loanMoneyBean.setName(loanNumArray[i]);
            switch (i) {
                case 1:
                    loanMoneyBean.setLimit(0);
                    loanMoneyBean.setMax(5000);
                    break;
                case 2:
                    loanMoneyBean.setLimit(5000);
                    loanMoneyBean.setMax(10000);
                    break;
                case 3:
                    loanMoneyBean.setLimit(10000);
                    loanMoneyBean.setMax(20000);
                    break;
                case 4:
                    loanMoneyBean.setLimit(20000);
                    loanMoneyBean.setMax(30000);
                    break;
                case 5:
                    loanMoneyBean.setLimit(30000);
                    loanMoneyBean.setMax(100000000);
                    break;
            }

            loadNums.add(loanMoneyBean);
        }

        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        setFocusable(false);
        setOutsideTouchable(false);
        setTouchable(true);

        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View contentView = LayoutInflater.from(context).inflate(R.layout.fra_loan_spinner_item,
                null);
        setContentView(contentView);
        ButterKnife.bind(this, contentView);


        loanSpinnerRevAdapter = new LoanSpinnerRevAdapter(context);
        loanSpinnerRevAdapter.setOnItemClickListener(new LoanSpinnerRevAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, BaseMulDataModel model) {

                if (model instanceof LoanFraTypeBean) {
                    MyLog.i("数据: " + model.toString());
                    EventBus.getDefault().post((LoanFraTypeBean) model);
                } else {
                    MyLog.i("数据: " + model.toString());
                    EventBus.getDefault().post((LoanMoneyBean) model);
                }
                dismiss();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(loanSpinnerRevAdapter);
    }


    @OnClick({R.id.spinner_back_img})
    public void click() {
        if (isShowing()) {
            //全部checkbox取消选择
            cancelListener.cancel();
            dismiss();
        }
    }

    int anchorLoc[] = new int[2];

    public void showAsDropDown(View anchor, Context context) {
        super.showAsDropDown(anchor);
    }

    public int[] getAnchorLoc() {
        return anchorLoc;
    }

    public void selectType(int type, List<BaseMulDataModel> contentList) {
        switch (type) {
            case TYPE:
                loanSpinnerRevAdapter.setInfoList(contentList);
                break;
            case LOAN:
                loanSpinnerRevAdapter.setInfoList(loadNums);
                break;
        }
        loanSpinnerRevAdapter.notifyDataSetChanged();
    }


    /**
     * 判断底部navigator是否已经显示
     *
     * @param windowManager
     * @return
     */
    private WindowManager wm;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private boolean hasSoftKeys(Context context) {
        if (wm == null)
            wm = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        Display d = wm.getDefaultDisplay();


        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        d.getRealMetrics(realDisplayMetrics);


        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;


        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);


        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;


        return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
    }

}
