package com.cdw.zhihutopnews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cdw.zhihutopnews.R;
import com.cdw.zhihutopnews.fragment.PersonFragment;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zhangdai on 2017/5/24.
 * QQ登陆界面
 */

public class QQLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Tencent mTencent;
    private IUiListener loginListener;
    private Button btLogout;
    private ImageView avatarimg;
    private CircleImageView icon_image;
    private TextView tvUsername;
    private Button btLogin;
//    private Context context;
PersonFragment pesonFragment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_usercenter);

        mTencent = Tencent.createInstance("101403682", this.getApplicationContext());
        findViews();
        qqLogin();
//        FragmentManager fm
    }


    private void findViews() {
//        btLogin = (Button) findViewById(R.id.bt_login);
        btLogout = (Button) findViewById(R.id.bt_logout);
        avatarimg = (ImageView) findViewById(R.id.avatarimg);
        tvUsername = (TextView) findViewById(R.id.tv_name);

//        btLogin.setOnClickListener(this);
        btLogout.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == btLogout) {
            qqLogout();
        }
    }

    private void qqLogout() {
        Toast.makeText(QQLoginActivity.this, "退出登陆", Toast.LENGTH_SHORT).show();
        mTencent.logout(QQLoginActivity.this);

        FragmentManager fm =getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, new PersonFragment());
        ft.commit();
        finish();

    }

    private void qqLogin() {
        mTencent.login(this, "all", loginListener);
        loginListener = new IUiListener() {
            @Override
            public void onComplete(Object o) { //登录成功后回调该方法,可以跳转相关的页面
                Toast.makeText(QQLoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();

                JSONObject object = (JSONObject) o;

                try {

                    String accessToken = object.getString("access_token");

                    String expires = object.getString("expires_in");

                    String openID = object.getString("openid");

                    mTencent.setAccessToken(accessToken, expires);

                    mTencent.setOpenId(openID);

                } catch (JSONException e) {

                    e.printStackTrace();

                }
            }

            @Override
            public void onError(UiError uiError) {
                Toast.makeText(QQLoginActivity.this, "授权出错！", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(QQLoginActivity.this, "授权取消！", Toast.LENGTH_LONG).show();
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_LOGIN) {

            if (resultCode == -1) {
                Tencent.onActivityResultData(requestCode, resultCode, data, loginListener);

                Tencent.handleResultData(data, loginListener);

                UserInfo info = new UserInfo(this, mTencent.getQQToken());
                info.getUserInfo(new IUiListener() {
                    @Override
                    public void onComplete(Object o) {

                        try {
                            JSONObject info = (JSONObject) o;
                            String nickName = info.getString("nickname");//获取用户昵称
                            String iconUrl = info.getString("figureurl_qq_2");//获取用户头像的url

                            Log.i("TAG", "Username" + nickName);
                            tvUsername.setText(nickName);
                            Glide.with(QQLoginActivity.this).load(iconUrl).into(avatarimg);//Glide解析获取用户头像
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(UiError uiError) {

                        Log.e("GET_QQ_INFO_ERROR", "获取qq用户信息错误");
                        Toast.makeText(QQLoginActivity.this, "获取qq用户信息错误", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {

                        Log.e("GET_QQ_INFO_CANCEL", "获取qq用户信息取消");
                        Toast.makeText(QQLoginActivity.this, "获取qq用户信息取消", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
