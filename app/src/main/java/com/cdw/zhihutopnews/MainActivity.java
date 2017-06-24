package com.cdw.zhihutopnews;


import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.cdw.zhihutopnews.activity.BaseActivity;
import com.cdw.zhihutopnews.config.Config;
import com.cdw.zhihutopnews.fragment.BaseFragment;
import com.cdw.zhihutopnews.fragment.FictionFragment;
import com.cdw.zhihutopnews.fragment.LocalDocumentFragment;
import com.cdw.zhihutopnews.fragment.PersonFragment;
import com.cdw.zhihutopnews.fragment.ZhihuFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 主程序
 */
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    // 打电话权限申请的请求码
    private static final int CALL_PHONE_REQUEST_CODE = 0x0011;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer)
    DrawerLayout drawer;
    @BindView(R.id.rg_main)
    RadioGroup mRg_main;
    @BindView(R.id.sr)
    SwipeRefreshLayout sr;

    private long exitTime = 0;
    private List<BaseFragment> mBaseFragment;
    private Fragment currentFragment;
    /**
     * 选中的Fragment的对应的位置
     */
    private int position;

    /**
     * 上次切换的Fragment
     */
    private Fragment mContent;
    private View.OnClickListener navigationOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            drawer.openDrawer(GravityCompat.START);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeTheme(Config.isNight);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(navigationOnClickListener);
        navView.setNavigationItemSelectedListener(this);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);//去掉默认显示的Title
        initView();
        //初始化Fragment
        initFragment();
        //设置RadioGroup的监听
        setListener();

        initLisneter();


    }

    private void initView() {

        int[][] state = new int[][]{
                new int[]{-android.R.attr.state_checked}, // unchecked
                new int[]{android.R.attr.state_checked}  // pressed
        };
        int[] color = new int[]{
                Color.BLACK, Color.BLACK};
        int[] iconcolor = new int[]{
                Color.GRAY, Color.BLACK};
        navView.setItemTextColor(new ColorStateList(state, color));
        navView.setItemIconTintList(new ColorStateList(state, iconcolor));

        /**
         * 暂未实现下拉刷新功能
         */
        sr.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void initLisneter() {

        sr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sr.setRefreshing(false);
            }
        });
    }

    /**
     * 滑动菜单的监听
     *
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        String msg = "";
        switch (item.getItemId()) {
            case R.id.nav_call:
                msg += "电话";
                callPhonePermission();
                break;
            case R.id.nav_friends:
                msg += "联系人";

                break;
            case R.id.nav_location:
                msg += "地址";
                break;
            case R.id.nav_mail:
                msg += "邮箱";
                break;
            case R.id.nav_task:
                msg += "日程";
                break;
        }

        if (!msg.equals("")) {
            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
        drawer.closeDrawers();//关闭滑动菜单
        return true;
    }

    private void callPhonePermission() {
        if (ContextCompat.checkSelfPermission(this,//检测某个权限是否已经被授予
                Manifest.permission.CALL_PHONE)//权限
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                // 用户拒绝过这个权限了，应该提示用户，为什么需要这个权限。
                Toast.makeText(this, "你曾经拒绝过此权限,需要重新获取", Toast.LENGTH_SHORT).show();

            } else {
                // 没有权限，申请权限。
                Toast.makeText(this, "申请权限", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE},//申请权限，可申请多个权限
                        CALL_PHONE_REQUEST_CODE);//权限请求码
            }

        } else {
            //权限已被授予，执行操作
            callPhone();
        }
    }

    private void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + "10086");
        intent.setData(data);
        startActivity(intent);
    }

    /**
     * 处理权限申请回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            case CALL_PHONE_REQUEST_CODE: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 用户同意申请权限，执行操作
                    Toast.makeText(this, "用户同意申请权限", Toast.LENGTH_SHORT).show();
                    callPhone();
                } else {
                    // 用户拒绝申请权限
                    Toast.makeText(this, "用户拒绝申请权限", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    /**
     * 切换白天黑夜主题模式,此种方式需要重启Activity
     * MainActivity.this.recreate();
     */
    private void changeTheme(boolean display_model) {
        if (display_model) {
            setTheme(R.style.AppTheme_Night);//夜间模式
        } else {
            setTheme(R.style.AppTheme_Light);//白天模式
        }

    }

    /**
     * 日间夜间模式切换，刷新界面
     */
    private void refreshUI() {
        toolbar.setBackgroundColor(getResources().getColor(Config.isNight ? R.color.toolbar_background_dark : R.color.toolbar_background_light));
        navView.setBackgroundColor(getResources().getColor(Config.isNight ? R.color.background_dark : R.color.background_light));
//        ((ZhihuFragment) mContent).refreshUI();
//        ((FictionFragment) currentFragment).refreshUI();
//        ((LocalDocumentFragment) currentFragment).refreshUI();
//        ((PersonFragment) currentFragment).refreshUI();
    }

    /**
     * 菜单项
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.getItem(0).setTitle(Config.isNight ? getResources().getString(R.string.display_model_light) : getResources().getString(R.string.display_model_night));
        return true;
    }

    /**
     * Toolbar菜单的监听
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_display_model:
                Config.isNight = !Config.isNight;
                showAnimation();
                refreshUI();
                item.setTitle(Config.isNight ? getResources().getString(R.string.display_model_light) : getResources().getString(R.string.display_model_night));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setListener() {
        mRg_main.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        //设置默认选中常用框架
        mRg_main.check(R.id.rb_local_document);
    }

    /**
     * @param from 刚显示的Fragment,马上就要被隐藏了
     * @param to   马上要切换到的Fragment，一会要显示
     */
    private void switchFrament(Fragment from, Fragment to) {

        if (from != to) {
            mContent = to;
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            //才切换
            //判断有没有被添加
            if (!to.isAdded()) {
                //to没有被添加
                //from隐藏
                if (from != null) {
                    ft.hide(from);
                }
                //添加to
                if (to != null) {
                    ft.add(R.id.fragment_container, to).commit();
                }
            } else {
                //to已经被添加
                // from隐藏
                if (from != null) {
                    ft.hide(from);
                }
                //显示to
                if (to != null) {
                    ft.show(to).commit();
                }
            }
        }

    }

    /**
     * 根据位置得到对应的Fragment
     *
     * @return
     */
    private BaseFragment getFragment() {
        BaseFragment fragment = mBaseFragment.get(position);
        return fragment;
    }

    private void initFragment() {
        mBaseFragment = new ArrayList<>();
        mBaseFragment.add(new LocalDocumentFragment());//本地文档
        mBaseFragment.add(new FictionFragment());//小说
        mBaseFragment.add(new ZhihuFragment());//新闻
        mBaseFragment.add(new PersonFragment());//个人
    }

    /**
     * 退出
     */
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(MainActivity.this, R.string.app_exit_tip, Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                super.onBackPressed();
            }
        }
    }

    /**
     * 展示一个切换动画
     */
    private void showAnimation() {
        final View decorView = getWindow().getDecorView();
        Bitmap cacheBitmap = getCacheBitmapFromView(decorView);
        if (decorView instanceof ViewGroup && cacheBitmap != null) {
            final View view = new View(this);
            view.setBackgroundDrawable(new BitmapDrawable(getResources(), cacheBitmap));
            ViewGroup.LayoutParams layoutParam = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            ((ViewGroup) decorView).addView(view, layoutParam);
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
            objectAnimator.setDuration(300);
            objectAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    ((ViewGroup) decorView).removeView(view);
                }
            });
            objectAnimator.start();
        }
    }

    /**
     * 获取一个 View 的缓存视图
     *
     * @param view
     * @return
     */
    private Bitmap getCacheBitmapFromView(View view) {
        final boolean drawingCacheEnabled = true;
        view.setDrawingCacheEnabled(drawingCacheEnabled);
        view.buildDrawingCache(drawingCacheEnabled);
        final Bitmap drawingCache = view.getDrawingCache();
        Bitmap bitmap;
        if (drawingCache != null) {
            bitmap = Bitmap.createBitmap(drawingCache);
            view.setDrawingCacheEnabled(false);
        } else {
            bitmap = null;
        }
        return bitmap;
    }



    //当Recycle滑动到底部时，加载更多新闻
    public interface LoadingMore {
        void loadingStart();

        void loadingFinish();
    }

    /**
     * RadioGroup监听
     * 切换底部导航按钮
     */
    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_local_document://本地文档
                    position = 0;
                    toolbar.setTitle("本地文档");
                    break;
                case R.id.rb_fiction://小说
                    position = 1;
                    toolbar.setTitle("小说界面");
                    break;
                case R.id.rb_news://新闻
                    position = 2;
                    toolbar.setTitle("知乎新闻");
                    break;
                case R.id.rb_person://个人
                    position = 3;
                    toolbar.setTitle("个人界面");
                    break;
                default:
                    position = 0;
                    break;
            }

            //根据位置得到对应的Fragment
            BaseFragment to = getFragment();
            //替换
            switchFrament(mContent, to);

        }
    }

    /**
     * 检查是否拥有权限
     *
     * @param thisActivity
     * @param permission
     * @param requestCode
     * @param errorText
     */
    protected void checkPermission(Activity thisActivity, String permission, int requestCode, String errorText) {
        //判断当前Activity是否已经获得了该权限
        if (ContextCompat.checkSelfPermission(thisActivity, permission) != PackageManager.PERMISSION_GRANTED) {
            //如果App的权限申请曾经被用户拒绝过，就需要在这里跟用户做出解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity,
                    permission)) {
                Toast.makeText(this, errorText, Toast.LENGTH_SHORT).show();
                //进行权限请求
                ActivityCompat.requestPermissions(thisActivity,
                        new String[]{permission},
                        requestCode);
            } else {
                //进行权限请求
                ActivityCompat.requestPermissions(thisActivity,
                        new String[]{permission},
                        requestCode);
            }
        } else {

        }
    }
}
