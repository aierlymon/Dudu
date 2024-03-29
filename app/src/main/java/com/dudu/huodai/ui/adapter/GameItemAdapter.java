package com.dudu.huodai.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dudu.huodai.R;
import com.dudu.huodai.mvp.model.postbean.GameCaiSelectBean;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class GameItemAdapter extends RecyclerView.Adapter<GameItemAdapter.CameCaiHolder> {

    private List<String> nameList;

    private Context mContext;

    public GameItemAdapter(List<String> nameList, Context mContext) {
        this.nameList = nameList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public CameCaiHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CameCaiHolder(LayoutInflater.from(mContext).inflate(R.layout.activity_game_cai_menuitem, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CameCaiHolder holder, int position) {
        holder.txItem.setText(nameList.get(position));
        holder.txItem.setOnCheckedChangeListener((compoundButton, b) -> {
            if (holder.txItem.isChecked()) {
                holder.txItem.setEnabled(false);
                GameCaiSelectBean gameCaiSelectBean = new GameCaiSelectBean();
                gameCaiSelectBean.setText(nameList.get(position));
                gameCaiSelectBean.setCheckBox(holder.txItem);
                EventBus.getDefault().post(gameCaiSelectBean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return nameList.size();
    }

    class CameCaiHolder extends RecyclerView.ViewHolder {

        CheckBox txItem;

        public CameCaiHolder(@NonNull View itemView) {
            super(itemView);
            txItem = itemView.findViewById(R.id.tx_item);
        }


    }
}
