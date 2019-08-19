package com.linglong.huodai.widget.jingewenku.abrahamcaijin.loopviewpagers;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.linglong.baselib.R;
import com.linglong.baselib.utils.MyLog;
import com.linglong.huodai.widget.jingewenku.abrahamcaijin.loopviewpagers.adapter.LoopFragmentPagerAdapter;
import com.linglong.huodai.widget.jingewenku.abrahamcaijin.loopviewpagers.adapter.LoopViewPagerAdapter;
import com.linglong.huodai.widget.jingewenku.abrahamcaijin.loopviewpagers.interfaces.CreateView;
import com.linglong.huodai.widget.jingewenku.abrahamcaijin.loopviewpagers.interfaces.IndicatorAnimator;
import com.linglong.huodai.widget.jingewenku.abrahamcaijin.loopviewpagers.interfaces.OnPageClickListener;
import com.linglong.huodai.widget.jingewenku.abrahamcaijin.loopviewpagers.interfaces.UpdateImage;
import com.linglong.huodai.widget.jingewenku.abrahamcaijin.loopviewpagers.util.DensityUtils;
import com.linglong.huodai.widget.jingewenku.abrahamcaijin.loopviewpagers.util.LoopViewPagerScroller;

import java.util.List;

/**
 * 主要功能:
 *
 * @Prject: LoopViewPager
 * @Package: com.example.huodai.widget.jingewenku.abrahamcaijin.loopviewpagers
 * @author: Abraham
 * @date: 2019年04月20日 14:07
 * @Copyright: 个人版权所有
 * @Company:
 * @version: 1.0.0
 */


public class LoopViewPager<T> extends FrameLayout {
    private LoopFragmentPagerAdapter loopFragmentPagerAdapter;
    private LoopViewPagerScroller loopViewPagerScroller;
    private int gravity = Gravity.BOTTOM | Gravity.CENTER;
    private IndicatiorCanvasView indicatorCanvasView;
    private IndicatorView indicatorView;
    private ViewPager.OnPageChangeListener onPageChangeListener;
    private OnPageClickListener onClickListener;
    private IndicatorAnimator indicatorAnimator;

    private ViewPager viewPager;
    private Runnable loopRunnable;
    private Handler mHandler;
    private long delayTime = 3000;
    private int currentItem = 0;
    private int viewNumber = 0;

    private int loopNowIndicatorImg = R.drawable.ic_check;
    private int loopIndicatorImg = R.drawable.ic_uncheck;
    private Activity mConttext;


    public Handler getmHandler() {
        return mHandler;
    }


    public enum IndicatorGravity {
        LEFT,
        RIGHT,
        CENTER,
    }

    public LoopViewPager(@NonNull Context context) {
        this(context, null);
    }

    public LoopViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoopViewPager(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.LoopViewPage, defStyleAttr, 0);
        int numCount = typedArray.getIndexCount();
        for (int i = 0; i < numCount; i++) {
            int attr = typedArray.getIndex(i);
            if (attr == R.styleable.LoopViewPage_loop_now_indicator_img) {
                loopNowIndicatorImg = typedArray.getResourceId(attr, R.mipmap.ic_origin);
            } else if (attr == R.styleable.LoopViewPage_loop_indicator_img) {
                loopIndicatorImg = typedArray.getResourceId(attr, R.mipmap.ic_un_origin);
            } else if (attr == R.styleable.LoopViewPage_loop_gravity) {
                String tempGravity = typedArray.getString(attr);
                if (tempGravity.contains("center")) {
                    gravity = Gravity.BOTTOM | Gravity.CENTER;
                } else if (tempGravity.contains("left")) {
                    gravity = Gravity.BOTTOM;
                } else if (tempGravity.contains("right")) {
                    gravity = Gravity.BOTTOM | Gravity.RIGHT;
                }
            }
        }
        typedArray.recycle();
        setClipChildren(true);
        initViewPage(context);
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener l) {
        this.onPageChangeListener = l;
    }

    public void setOnPageClickListener(OnPageClickListener l) {
        this.onClickListener = l;
    }

    public void setIndicatorAnimator(IndicatorAnimator l) {
        this.indicatorAnimator = l;
    }

    public LoopViewPager setIndicatorType(Object type) {
        if (type == IndicatorView.class) {
            indicatorView = new IndicatorView(getContext(), loopNowIndicatorImg, loopIndicatorImg, indicatorAnimator);
        } else if (type == IndicatiorCanvasView.class) {
            indicatorCanvasView = new IndicatiorCanvasView(getContext(), loopNowIndicatorImg, loopIndicatorImg);
        }
        return this;
    }

    public void initViewPage(Context context) {
        mHandler = new Handler();
        this.viewPager = new ViewPager(context);
        this.viewPager.setOffscreenPageLimit(2);
        loopViewPagerScroller = new LoopViewPagerScroller(context);
        loopViewPagerScroller.setScrollDuration(900);
        loopViewPagerScroller.initViewPagerScroll(viewPager);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            viewPager.setId(viewPager.hashCode());
        } else {
            viewPager.setId(View.generateViewId());
        }
        loopRunnable = () -> {

            MyLog.i("执行了定时操作: mContext: "+mConttext);
            if (mConttext != null && mConttext.isFinishing()) {
                if (mHandler == null || mConttext == null) {
                    return;
                }
                mHandler.removeCallbacksAndMessages(null);
                try {
                    MyLog.i("销毁了定时操作: mContext: "+mConttext);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        mHandler.getLooper().quitSafely();
                    } else {
                        mHandler.getLooper().quit();
                    }
                    mHandler = null;
                }catch (Exception e){}

            } else {
                viewPager.setCurrentItem(currentItem);
                currentItem++;
                mHandler.postDelayed(loopRunnable, delayTime);
            }
        };
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }
                if (indicatorCanvasView != null) {
                    indicatorCanvasView.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {
                currentItem = position;
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageSelected(position);
                }
                if (indicatorView != null) {
                    indicatorView.changeIndicator(position % viewNumber);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageScrollStateChanged(state);
                }
            }
        });
        this.addView(this.viewPager);
    }

    public void setAnimation(boolean reverseDrawingOrder, ViewPager.PageTransformer transformer) {
        viewPager.setPageTransformer(reverseDrawingOrder, transformer);
    }

    public ViewPager getViewPager() {
        return this.viewPager;
    }

    public void setData(FragmentManager fragmentManager, List<Fragment> listFragment) {
        viewNumber = listFragment.size();
        initIndicator(getContext());
        this.loopFragmentPagerAdapter = new LoopFragmentPagerAdapter(fragmentManager, listFragment);
        this.viewPager.setAdapter(this.loopFragmentPagerAdapter);
    }

    public void setData(Context context, List<T> mData, CreateView mCreatView) {
        viewNumber = mData.size();
        initIndicator(getContext());
        LoopViewPagerAdapter loopViewPagerAdapter = new LoopViewPagerAdapter(context, mData, mCreatView, onClickListener);
        viewPager.setAdapter(loopViewPagerAdapter);
    }

    public void setData(Activity context, List<T> mData, UpdateImage updateImage) {
        mConttext = context;
        setData(mData, updateImage);
    }


    public void setData(List<T> mData, final UpdateImage updateImage) {
        viewNumber = mData.size();
        initIndicator(getContext());
        LoopViewPagerAdapter loopViewPagerAdapter = new LoopViewPagerAdapter(getContext(), mData, new CreateView() {
            @Override
            public View createView(int position) {
                return new ImageView(getContext());
            }

            @Override
            public void updateView(View view, int position, Object item) {
                if (updateImage != null) {
                    updateImage.loadImage(((ImageView) view), position, item);
                }
            }

            @Override
            public void deleteView(int position) {
            }
        }, onClickListener);
        viewPager.setAdapter(loopViewPagerAdapter);
    }

    public void initIndicator(Context context) {
        if (indicatorCanvasView == null && indicatorView == null) {
            indicatorView = new IndicatorView(context, loopNowIndicatorImg, loopIndicatorImg, indicatorAnimator);
        }
        LayoutParams layoutParams = new LayoutParams(-2, -2);
        layoutParams.gravity = gravity;
        int margins = DensityUtils.dp2px(context, 8.0F);
        layoutParams.setMargins(margins, 0, margins, margins);


     /*   if (indicatorCanvasView != null) {
            addView(indicatorCanvasView, layoutParams);
            indicatorCanvasView.initView(viewNumber);
        }*/
        if (indicatorView != null) {
            ViewParent vp = indicatorView.getParent();
            if (vp != null) {
                ViewGroup parent = (ViewGroup) vp;
                parent.removeView(indicatorView);
            }
            addView(indicatorView, layoutParams);
            MyLog.i("initIndicator执行了多少次呢!!");

            indicatorView.initView(viewNumber);
        }
    }




    public void setDelayTime(long delayTime) {
        this.delayTime = delayTime;
    }

    public void startBanner() {
        mHandler.postDelayed(loopRunnable, delayTime);
    }

    public void setCurrentItem(int currentItem) {
        this.currentItem = currentItem;
        viewPager.setCurrentItem(currentItem);
    }

    public void startBanner(long delayTime) {
        this.delayTime = delayTime;
        mHandler.postDelayed(loopRunnable, delayTime);
    }

    public void showIndicator(boolean show) {
        if (indicatorView != null) {
            indicatorView.setVisibility(show == true ? View.VISIBLE : View.GONE);
        }
        if (indicatorCanvasView != null) {
            indicatorCanvasView.setVisibility(show == true ? View.VISIBLE : View.GONE);
        }
    }

    public void setIndicatorGravity(IndicatorGravity indicatorGravity) {
        LayoutParams layoutParams = new LayoutParams(-2, -2);
        layoutParams.gravity = gravity;
        int margins = DensityUtils.dp2px(getContext(), 8.0F);
        layoutParams.setMargins(margins, 0, margins, margins);
        if (indicatorGravity == IndicatorGravity.LEFT) {
            gravity = Gravity.BOTTOM;
        } else if (indicatorGravity == IndicatorGravity.CENTER) {
            gravity = Gravity.BOTTOM | Gravity.CENTER;
        } else if (indicatorGravity == IndicatorGravity.RIGHT) {
            gravity = Gravity.BOTTOM | Gravity.RIGHT;
        }
        if (indicatorView != null) {
            indicatorView.setLayoutParams(layoutParams);
        }
        if (indicatorCanvasView != null) {
            indicatorCanvasView.setLayoutParams(layoutParams);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        MyLog.i("执行了onAttachedToWindow");
    }




    public void cancelLoop() {
        if (mConttext != null && mConttext.isFinishing()) {
            if (mHandler == null || mConttext == null) {
                return;
            }
            mHandler.removeCallbacksAndMessages(null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mHandler.getLooper().quitSafely();
            } else {
                mHandler.getLooper().quit();
            }
        }
    }


}
