package com.linglong.huodai.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.linglong.baselib.utils.MyLog;
import com.linglong.huodai.R;

import java.util.List;

public class HomeBannerVPAdapyer extends PagerAdapter implements View.OnClickListener {

    private Context mContext;

    List<String> fragmentList;

    public interface OnItemClickListener {
        void onItemClick(View view);
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    private OnItemClickListener mOnItemClickListener;

    public HomeBannerVPAdapyer(Context mContext,List<String> fragmentList) {
        this.fragmentList = fragmentList;
        this.mContext=mContext;
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(view);
        }
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    /**
     * 1.将当前视图添加到container中
     * 2.返回当前View
     *
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate( R.layout.home_banner_item, null,false);
        ImageView image = (ImageView) view.findViewById(R.id.image);
        image.setOnClickListener(this);
        MyLog.i("banner图片地址为: "+fragmentList.get(position));
        Glide.with(mContext).load(fragmentList.get(position)).into(image);
        container.addView(view);
        return view;
    }

    /**
     * 从当前container中删除指定位置（position）的View
     *
     * @param container
     * @param position
     * @param object
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // super.destroyItem(container,position,object); 这一句要删除，否则报错
        container.removeView((View) object);
    }

    /**
     * 确定页视图是否与特定键对象关联
     *
     * @param view
     * @param object
     * @return
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}
