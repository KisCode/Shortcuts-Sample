package com.keno.shortcuts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.keno.shortcuts.R;
import com.keno.shortcuts.pojo.ShortcutModel;

import java.util.ArrayList;
import java.util.List;

public class ShortcutSettingAdapter extends RecyclerView.Adapter<ShortcutSettingAdapter.ShortCutViewHolder> {
    private List<ShortcutModel> mDatas;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    public ShortcutSettingAdapter(Context mContext, List<ShortcutModel> mDatas) {
        this.mDatas = mDatas;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ShortcutSettingAdapter.ShortCutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_shortcut_setting, parent, false);
        return new ShortCutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ShortcutSettingAdapter.ShortCutViewHolder holder, final int position) {
        ShortcutModel shortcutModel = mDatas.get(position);
        holder.ivIcon.setImageResource(shortcutModel.getIconRes());
        holder.tvTitle.setText(shortcutModel.getTitle());

        if (shortcutModel.isAdd()) {
            holder.tvAdd.setText(R.string.remove);
        } else {
            holder.tvAdd.setText(R.string.add);
        }

        if (mOnItemClickListener != null) {
            holder.tvAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemChildViewClick(holder.tvAdd, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public ShortcutModel getItem(int pos) {
        return mDatas.get(pos);
    }

    public List<ShortcutModel> getDatas() {
        return mDatas;
    }

    public void remove(int pos) {
        mDatas.remove(pos);
        notifyDataSetChanged();
    }

    public void add(ShortcutModel shortcutModel) {
        mDatas.add(shortcutModel);
        notifyDataSetChanged();
    }

    public void setNewData(List<ShortcutModel> newData) {
        this.mDatas = newData == null ? new ArrayList<ShortcutModel>() : newData;
        notifyDataSetChanged();
    }

    public void setOnClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemChildViewClick(View view, int pos);
    }

    class ShortCutViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvTitle;
        TextView tvAdd;

        public ShortCutViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_icon_shortcut_setting);
            tvTitle = itemView.findViewById(R.id.tv_title_shortcut_setting);
            tvAdd = itemView.findViewById(R.id.tv_add_shortcut_setting);
        }
    }
}
