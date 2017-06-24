package com.cdw.zhihutopnews.localfragment;

import android.support.v4.app.Fragment;

/**
 * 实现懒加载的BaseFragment
 */
public abstract class LazyFragment extends Fragment {

    protected boolean isVisible;

    /**
     * 在这里实现Fragment数据的缓加载.
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    protected void onVisible() {
        lazyLoad();
    }

    protected abstract void lazyLoad();

    protected void onInvisible() {
    }
}
