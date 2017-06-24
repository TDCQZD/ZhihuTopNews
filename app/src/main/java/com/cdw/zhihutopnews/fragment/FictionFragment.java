package com.cdw.zhihutopnews.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.cdw.zhihutopnews.R;
import com.cdw.zhihutopnews.activity.BookReadActivity;
import com.cdw.zhihutopnews.adapter.AdapterBookList;
import com.cdw.zhihutopnews.bean.Book;
import com.cdw.zhihutopnews.config.Config;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xfangfang.paperviewlibrary.PaperView;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2017/4/26.
 * 小说界面
 */

public class FictionFragment extends BaseFragment {
    int padding = 16;
    private PaperView paperView;
    private RecyclerView recyclerView;
    private ArrayList<Book> books;
    private TextView textView;

    @BindView(R.id.fragmeng_fiction)
    FrameLayout fragmengFiction;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fiction, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recylerview_book);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(lm);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView();

    }

    /**
     * 添加小说数据
     */
    protected void initData() {
        books = new ArrayList<>();
        books.add(new Book("呼啸山庄", "艾米莉·勃朗特", "http://www.readers365.com/World/001/"));
        books.add(new Book("雾都孤儿", "查尔斯·狄更斯", "http://www.readers365.com/World/004/"));
        books.add(new Book("鲁滨逊漂流记", "丹尼尔·笛福", "http://www.readers365.com/World/005/"));
        books.add(new Book("傲慢与偏见", "简·奥斯汀", "http://www.readers365.com/World/006/"));
        books.add(new Book("老人与海", "海明威", "http://www.readers365.com/World/031/"));
        books.add(new Book("麦田里的守望者", "塞林格", "http://www.readers365.com/World/033/"));
        books.add(new Book("生命中不能承受之轻", "米兰·昆德拉", "http://www.readers365.com/World/044/"));

    }
    protected void initView() {
        AdapterBookList bookadapter = new AdapterBookList(books);
        recyclerView.setAdapter(bookadapter);
        bookadapter.setOnItemClickListener(new AdapterBookList.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Book book = books.get(position);
                Intent intent = new Intent(getActivity(), BookReadActivity.class);
                intent.putExtra("book", book);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Log.e(TAG, "onItemClick: 长按一次");
            }
        });

    }

    public void setText(View view) {
        paperView.setText(getResources().getString(R.string.three_country));
    }

    public void incLine(View view) {
        paperView.setTextLine(paperView.getTextLine() + 1);
    }

    public void decLine(View view) {
        paperView.setTextLine(paperView.getTextLine() - 1);
    }

    public void incTextSize(View view) {
        paperView.setTextSize(paperView.getTextSize() + 1);
    }

    public void decTextSize(View view) {
        paperView.setTextSize(paperView.getTextSize() - 1);
    }

    public void gotoPage(View view) {
        paperView.setPage(5);
    }

    public void setPadding(View view) {
        paperView.setContentPadding(padding);
        padding += 5;
    }
    /**
     * 日间夜间模式切换时更新UI
     */
    public void refreshUI() {
        fragmengFiction.setBackgroundColor(getResources().getColor(Config.isNight ? R.color.background_dark : R.color.background_light));
//        zhihuAdapter.notifyDataSetChanged();
    }
}
