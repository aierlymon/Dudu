package com.dudu.huodai.ui.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dudu.baselib.utils.Utils;
import com.dudu.huodai.AdvertisementActivity;
import com.dudu.huodai.GameTreeActivity;
import com.dudu.huodai.R;
import com.dudu.huodai.mvp.model.postbean.GameSmashBean;
import com.dudu.huodai.widget.GameWinDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class GameSmashAdapter extends RecyclerView.Adapter<GameSmashAdapter.GameSmashHodler> implements View.OnClickListener {


    private OnItemClickListener mOnItemClickListener;

    private Context mContext;
    List<String> list;

    public GameSmashAdapter(Context mContext, List<String> list) {
        this.mContext = mContext;
        this.list = list;
        Glide.with(mContext).load(R.mipmap.badegg).preload();
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(view, (int) view.getTag());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }


    @NonNull
    @Override
    public GameSmashHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GameSmashHodler(LayoutInflater.from(mContext)
                .inflate(R.layout.activity_game_smash_item, parent, false), this);
    }

    @Override
    public void onBindViewHolder(@NonNull GameSmashHodler holder, int position) {
        RequestOptions options = new RequestOptions();
        int size = (int) mContext.getResources().getDimension(R.dimen.x72);
        size = Utils.px2dip(mContext.getApplicationContext(), size);
        options.override(size, size); //设置加载的图片大小
        Glide.with(mContext).load(R.mipmap.gold_egg).apply(options).into(holder.glodImage);
        Glide.with(mContext).load(R.mipmap.hammer).apply(options).into(holder.hammer);
        holder.hammer.setVisibility(View.GONE);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class GameSmashHodler extends RecyclerView.ViewHolder {
        ImageView glodImage;
        ImageView hammer;

        public GameSmashHodler(View itemView, View.OnClickListener listener) {
            super(itemView);
            itemView.setOnClickListener(listener);
            glodImage = itemView.findViewById(R.id.gold_egg);
            hammer = itemView.findViewById(R.id.hammer);
            glodImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hammer.setVisibility(View.VISIBLE);
                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(hammer, "rotation", 45f, -45f, 0);
                    objectAnimator.setInterpolator(new AccelerateInterpolator());
                    objectAnimator.start();
                    objectAnimator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            //弹窗通知
                            // hammer.setVisibility(View.GONE);
                            RequestOptions options = new RequestOptions();
                            int size = (int) mContext.getResources().getDimension(R.dimen.x72);
                            size = Utils.px2dip(mContext.getApplicationContext(), size);
                            options.override(size, size); //设置加载的图片大小
                            Glide.with(mContext).load(R.mipmap.badegg).apply(options).into(glodImage);

                            //弹窗
                            GameWinDialog.Builder(mContext)
                                    .setMessage("恭喜您，奖励您")
                                    .setTitle("+80")
                                    .setIconId(R.mipmap.win)
                                    .setLeftButtonText("继续砸")
                                    .setRightButtonText("金豆翻倍")
                                    .setOnCancelClickListener(new GameWinDialog.onCancelClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            notifyDataSetChanged();
                                        }
                                    })
                                    .setOnConfirmClickListener(new GameWinDialog.onConfirmClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            EventBus.getDefault().post(new GameSmashBean());
                                        }
                                    })
                                    .build().shown();
                        }
                    });
                }
            });
        }

    }
}
