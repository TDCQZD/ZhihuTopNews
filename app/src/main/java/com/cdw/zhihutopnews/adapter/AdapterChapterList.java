package com.cdw.zhihutopnews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cdw.zhihutopnews.R;
import com.cdw.zhihutopnews.bean.Chapter;

import java.util.ArrayList;


/**
 * 小说界面书籍的章节适配器
 */


public class AdapterChapterList extends RecyclerView.Adapter<AdapterChapterList.ViewHolder> {


    private ArrayList<Chapter> chapters;
    private OnItemClickListener listener;
    private Context context;


    public AdapterChapterList(ArrayList<Chapter> chapters) {
        this.chapters = chapters;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_toc_item, parent, false);
        final ViewHolder holder = new ViewHolder(view, listener);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Chapter chapter = chapters.get(position);
        holder.item.setText(chapter.getName());
    }

    @Override
    public int getItemCount() {
        return chapters.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        View mView;
        TextView item;
        OnItemClickListener listener;

        ViewHolder(View itemView, OnItemClickListener l) {
            super(itemView);
            listener = l;
            mView = itemView;
            item = (TextView) itemView.findViewById(R.id.tocItem);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClick(v, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (listener != null) {
                listener.onItemLongClick(v, getAdapterPosition());
                return true;
            }
            return false;
        }
    }
}
