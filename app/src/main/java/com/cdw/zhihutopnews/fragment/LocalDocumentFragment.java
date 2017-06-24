package com.cdw.zhihutopnews.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cdw.zhihutopnews.R;
import com.cdw.zhihutopnews.adapter.LocalFragmentAdapter;
import com.cdw.zhihutopnews.config.Config;
import com.cdw.zhihutopnews.localfragment.LocalTabAll;
import com.cdw.zhihutopnews.localfragment.LocalTabDOC;
import com.cdw.zhihutopnews.localfragment.LocalTabOther;
import com.cdw.zhihutopnews.localfragment.LocalTabPDF;
import com.cdw.zhihutopnews.localfragment.LocalTabPPT;
import com.cdw.zhihutopnews.localfragment.LocalTabTXT;
import com.cdw.zhihutopnews.localfragment.LocalTabXLS;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 本地文档Fragment
 */
public class LocalDocumentFragment extends BaseFragment {

    private static final String TAG = LocalDocumentFragment.class.getSimpleName();
    View view;
    //顶部三个LinearLayout
    private LinearLayout mTab01;
    private LinearLayout mTab02;
    private LinearLayout mTab03;
    private LinearLayout mTab04;
    private LinearLayout mTab05;
    private LinearLayout mTab06;
    private LinearLayout mTab07;
    //顶部的三个TextView
    private TextView id_tab01_info;
    private TextView id_tab02_info;
    private TextView id_tab03_info;
    private TextView id_tab04_info;
    private TextView id_tab05_info;
    private TextView id_tab06_info;
    private TextView id_tab07_info;
    //Tab的那个引导线
    private ImageView mTabLine;
    //屏幕的宽度
    private int screenWidth;
    private ViewPager mViewPager;
    private LocalFragmentAdapter mAdapter;
    private List<Fragment> fragments = new ArrayList<Fragment>();
    private Resources res;

    @BindView(R.id.fragmeng_local_doucment)
    LinearLayout fragmengLocalDoucment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_local_doucment, container, false);
        ButterKnife.bind(this, view);
        initView();
        initTabLine();
        FragmentManager fm = getFragmentManager();
        //初始化Adapter
        mAdapter = new LocalFragmentAdapter(fm, fragments);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new TabOnPageChangeListener());


        return view;
    }


    /**
     * 初始化控件，初始化Fragment
     *
     * @return
     */

    private void initView() {
        res = getResources();
        id_tab01_info = (TextView) view.findViewById(R.id.id_tab01_info);
        id_tab02_info = (TextView) view.findViewById(R.id.id_tab02_info);
        id_tab03_info = (TextView) view.findViewById(R.id.id_tab03_info);
        id_tab04_info = (TextView) view.findViewById(R.id.id_tab04_info);
        id_tab05_info = (TextView) view.findViewById(R.id.id_tab05_info);
        id_tab06_info = (TextView) view.findViewById(R.id.id_tab06_info);
        id_tab07_info = (TextView) view.findViewById(R.id.id_tab07_info);

        mTab01 = (LinearLayout) view.findViewById(R.id.id_tab01);
        mTab02 = (LinearLayout) view.findViewById(R.id.id_tab02);
        mTab03 = (LinearLayout) view.findViewById(R.id.id_tab03);
        mTab04 = (LinearLayout) view.findViewById(R.id.id_tab04);
        mTab05 = (LinearLayout) view.findViewById(R.id.id_tab05);
        mTab06 = (LinearLayout) view.findViewById(R.id.id_tab06);
        mTab07 = (LinearLayout) view.findViewById(R.id.id_tab07);
        mTabLine = (ImageView) view.findViewById(R.id.id_tab_line);
        mViewPager = (ViewPager) view.findViewById(R.id.id_viewpager);

        mTab01.setOnClickListener(new TabOnClickListener(0));
        mTab02.setOnClickListener(new TabOnClickListener(1));
        mTab03.setOnClickListener(new TabOnClickListener(2));
        mTab04.setOnClickListener(new TabOnClickListener(3));
        mTab05.setOnClickListener(new TabOnClickListener(4));
        mTab06.setOnClickListener(new TabOnClickListener(5));
        mTab07.setOnClickListener(new TabOnClickListener(6));

        fragments.add(new LocalTabAll());
        fragments.add(new LocalTabDOC());
        fragments.add(new LocalTabPPT());
        fragments.add(new LocalTabXLS());
        fragments.add(new LocalTabPDF());
        fragments.add(new LocalTabTXT());
        fragments.add(new LocalTabOther());


    }

    /**
     * 根据屏幕的宽度，初始化引导线的宽度
     */
    private void initTabLine() {

        //获取屏幕的宽度
        DisplayMetrics outMetrics = new DisplayMetrics();
        getActivity().getWindow().getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        screenWidth = outMetrics.widthPixels;

        //获取控件的LayoutParams参数(注意：一定要用父控件的LayoutParams写LinearLayout.LayoutParams)
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLine.getLayoutParams();
        lp.width = screenWidth / 7;//设置该控件的layoutParams参数
        mTabLine.setLayoutParams(lp);//将修改好的layoutParams设置为该控件的layoutParams
    }


    /**
     * 重置颜色
     */
    private void resetTextView() {
        id_tab01_info.setTextColor(res.getColor(R.color.black));
        id_tab02_info.setTextColor(res.getColor(R.color.black));
        id_tab03_info.setTextColor(res.getColor(R.color.black));
        id_tab04_info.setTextColor(res.getColor(R.color.black));
        id_tab05_info.setTextColor(res.getColor(R.color.black));
        id_tab06_info.setTextColor(res.getColor(R.color.black));
        id_tab07_info.setTextColor(res.getColor(R.color.black));
    }

    /**
     * 功能：点击主页TAB事件
     */
    public class TabOnClickListener implements View.OnClickListener {
        private int index = 0;

        public TabOnClickListener(int i) {
            index = i;
        }

        public void onClick(View v) {
            mViewPager.setCurrentItem(index);//选择某一页
        }

    }

    /**
     * 功能：Fragment页面改变事件
     */
    public class TabOnPageChangeListener implements ViewPager.OnPageChangeListener {

        //当滑动状态改变时调用
        public void onPageScrollStateChanged(int state) {

        }

        //当前页面被滑动时调用
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLine.getLayoutParams();
            //返回组件距离左侧组件的距离
            lp.leftMargin = (int) ((positionOffset + position) * screenWidth / 7);
            mTabLine.setLayoutParams(lp);
        }

        //当新的页面被选中时调用
        public void onPageSelected(int position) {
            //重置所有TextView的字体颜色
            resetTextView();
            switch (position) {
                case 0:
                    id_tab01_info.setTextColor(res.getColor(R.color.colorAccent));
                    break;
                case 1:
                    id_tab02_info.setTextColor(res.getColor(R.color.colorAccent));
                    break;
                case 2:
                    id_tab03_info.setTextColor(res.getColor(R.color.colorAccent));
                    break;
                case 3:
                    id_tab04_info.setTextColor(res.getColor(R.color.colorAccent));
                    break;
                case 4:
                    id_tab05_info.setTextColor(res.getColor(R.color.colorAccent));
                    break;
                case 5:
                    id_tab06_info.setTextColor(res.getColor(R.color.colorAccent));
                    break;
                case 6:
                    id_tab07_info.setTextColor(res.getColor(R.color.colorAccent));
                    break;
            }
        }
    }
    /**
     * 日间夜间模式切换时更新UI
     */
    public void refreshUI() {
        fragmengLocalDoucment.setBackgroundColor(getResources().getColor(Config.isNight ? R.color.background_dark : R.color.background_light));
        mAdapter.notifyDataSetChanged();
    }

}
