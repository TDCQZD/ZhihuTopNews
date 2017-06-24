package com.cdw.zhihutopnews.presenter.impleView;

import com.cdw.zhihutopnews.bean.ZhihuDaily;



public interface IZhihuFragment extends IBaseFragment {
    void updateList(ZhihuDaily zhihuDaily);//获取最新新闻
    void getTopStory(ZhihuDaily zhihuDaily);//获取头条新闻
}
