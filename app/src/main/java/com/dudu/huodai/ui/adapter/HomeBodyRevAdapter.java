package com.dudu.huodai.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dudu.baselib.http.HttpConstant;
import com.dudu.baselib.utils.MyLog;
import com.dudu.baselib.utils.Utils;
import com.dudu.huodai.LabelActivity;
import com.dudu.huodai.R;
import com.dudu.huodai.widget.CircleImageView;
import com.dudu.model.bean.NewHomeBodyBean;
import com.dudu.model.bean.StoryTable;

import java.util.List;

public class HomeBodyRevAdapter extends RecyclerView.Adapter<HomeBodyRevAdapter.BodyItemHold> implements View.OnClickListener {

    private Context mContext;
    private List<StoryTable> homeBodyBeanList;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    private OnItemClickListener mOnItemClickListener;

    public HomeBodyRevAdapter(Context mContext, List<StoryTable> homeBodyBeanList) {
        this.mContext = mContext;
        this.homeBodyBeanList = homeBodyBeanList;
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(view, (int) view.getTag());
        }
    }


    @NonNull
    @Override
    public BodyItemHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BodyItemHold(LayoutInflater.from(mContext)
                .inflate(R.layout.common_body_item, parent, false), this);
    }

    @Override
    public void onBindViewHolder(@NonNull BodyItemHold holder, int position) {
        StoryTable loanProductBean = homeBodyBeanList.get(position);
        String icon_url = HttpConstant.PIC_BASE_URL + homeBodyBeanList.get(position).getPicture();
        //获取标签
        List<StoryTable.TagsBean> tagsBeans = homeBodyBeanList.get(position).getTags();

        int i = 0;
        for (; i < tagsBeans.size(); i++) {
            TextView lab = new TextView(mContext);

            lab.setText(tagsBeans.get(i).getName());
            lab.setTag(tagsBeans.get(i).getId());
            lab.setBackgroundColor(mContext.getResources().getColor(R.color.label_back_color));
            lab.setTextColor(mContext.getResources().getColor(R.color.label_color));
            lab.setTextSize(Utils.px2dip(mContext.getApplicationContext(), mContext.getResources().getDimension(R.dimen.font_px12)));
            lab.setPadding(3,3,3,3);
            lab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(mContext, LabelActivity.class);
                    intent.putExtra("id", ((int) lab.getTag()));
                    mContext.startActivity(intent);
                }
            });
            holder.info_label_layout.addView(lab);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) lab.getLayoutParams();
            params.setMargins((int) mContext.getResources().getDimension(R.dimen.left_right_marigin), 0, 0, 0);
            lab.setLayoutParams(params);

            if (holder.info_label_layout.getChildCount() == 3) {
                break;
            }

        }


        Glide.with(mContext).load(icon_url).into(holder.icon);

        //标题名字
        holder.title.setText(loanProductBean.getTitle());

        holder.info.setText(loanProductBean.getGuide());

        holder.itemView.setTag(position);
        holder.icon.setOnClickListener(view -> {
            if (mOnItemClickListener != null) {
                //注意这里使用getTag方法获取position
                mOnItemClickListener.onItemClick(view, position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return homeBodyBeanList.size();
    }

    class BodyItemHold extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView title;
        TextView info;

        TextView labelSpecial;
        LinearLayout info_label_layout;

        public BodyItemHold(@NonNull View itemView, View.OnClickListener listener) {
            super(itemView);

            itemView.setOnClickListener(listener);
            icon = itemView.findViewById(R.id.menu_icon);
            title = itemView.findViewById(R.id.tx_menu_title);
            info = itemView.findViewById(R.id.tx_menu_info);


            labelSpecial = itemView.findViewById(R.id.label_special);
            labelSpecial.setVisibility(View.INVISIBLE);

            info_label_layout = itemView.findViewById(R.id.info_label_layout);
        }
    }
}
