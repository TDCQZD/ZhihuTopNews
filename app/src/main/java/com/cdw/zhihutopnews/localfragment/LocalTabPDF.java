package com.cdw.zhihutopnews.localfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cdw.zhihutopnews.R;


/**
 * Created by Administrator on 2017/4/18.
 */

public class LocalTabPDF extends LazyFragment {

    private View view;
    // 标志位，标志已经初始化完成。
    private boolean isPrepared;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_local, container, false);

        initUI();//实例化控件
        isPrepared = true;
        lazyLoad();//加载数据

        return view;
    }

    /**
     * 实例化组件
     */
    private void initUI() {

    }

    /**
     * 实现懒加载,当屏幕显示这个界面的时候才会触发次方法
     */
    @Override
    protected void lazyLoad() {
        if (isPrepared && isVisible) {
            //TODO 在这里处理逻辑
            Toast.makeText(getContext(), "LocalTabPDF", Toast.LENGTH_SHORT).show();
        }
    }
}