package com.linglong.baselib.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.linglong.baselib.R;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.style.ChasingDots;
import com.github.ybq.android.spinkit.style.Circle;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.github.ybq.android.spinkit.style.Pulse;
import com.github.ybq.android.spinkit.style.RotatingPlane;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.github.ybq.android.spinkit.style.WanderingCubes;
import com.github.ybq.android.spinkit.style.Wave;

/**
 * createBy ${huanghao}
 * on 2019/7/3
 */
public class CustomDialog extends Dialog {
    public CustomDialog(Context context) {
        super(context);
    }

    public CustomDialog(Context context, int theme) {
        super(context, theme);
    }

    //https://juejin.im/entry/574fe05679bc440052f68c8d(自己看要哪种)
    public static final int RotatingPlane = 0;
    public static final int DoubleBounce = 1;
    public static final int Wave = 2;
    public static final int WanderingCubes = 3;
    public static final int Pulse = 4;
    public static final int ChasingDots = 5;
    public static final int ThreeBounce = 6;
    public static final int Circle = 7;
    public static final int CubeGrid = 8;
    public static final int FadingCircle = 9;
    public static final int FoldingCube = 10;

    public static class Builder {

        private SpinKitView spinKitView;
        private View layout;
        private CustomDialog dialog;

        public Builder(Context context) {
            //这里传入自定义的style，直接影响此Dialog的显示效果。style具体实现见style.xml
            dialog = new CustomDialog(context, R.style.LoadingDialog);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.layout_loading_dialog, null);
            spinKitView = ((SpinKitView) layout.findViewById(R.id.loadingBar));
            spinKitView.setIndeterminateDrawable(new Wave());
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }

        public Builder setMessage(String message) {
            ((TextView) layout.findViewById(R.id.tv_load_msg)).setText(message);
            return this;
        }

        public Builder setTheme(int theme) {
            switch (theme) {
                case RotatingPlane:
                    spinKitView.setIndeterminateDrawable(new RotatingPlane());
                    break;
                case DoubleBounce:
                    spinKitView.setIndeterminateDrawable(new DoubleBounce());
                    break;
                case Wave:
                    spinKitView.setIndeterminateDrawable(new Wave());
                    break;
                case WanderingCubes:
                    spinKitView.setIndeterminateDrawable(new WanderingCubes());
                    break;
                case Pulse:
                    spinKitView.setIndeterminateDrawable(new Pulse());
                    break;
                case ChasingDots:
                    spinKitView.setIndeterminateDrawable(new ChasingDots());
                    break;
                case ThreeBounce:
                    spinKitView.setIndeterminateDrawable(new ThreeBounce());
                    break;
                case Circle:
                    spinKitView.setIndeterminateDrawable(new Circle());
                    break;
                case CubeGrid:
                    spinKitView.setIndeterminateDrawable(new CubeGrid());
                    break;
                case FadingCircle:
                    spinKitView.setIndeterminateDrawable(new FadingCircle());
                    break;
                case FoldingCube:
                    spinKitView.setIndeterminateDrawable(new FoldingCube());
                    break;

            }
            return this;
        }


        public CustomDialog create() {
            return dialog;
        }
    }
}
