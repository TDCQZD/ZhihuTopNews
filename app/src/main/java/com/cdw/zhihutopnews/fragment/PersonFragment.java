package com.cdw.zhihutopnews.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cdw.zhihutopnews.R;
import com.cdw.zhihutopnews.activity.QQLoginActivity;
import com.cdw.zhihutopnews.activity.RegisterActivity;
import com.cdw.zhihutopnews.activity.ResetpwdActivity;
import com.cdw.zhihutopnews.activity.UserActivity;
import com.cdw.zhihutopnews.config.Config;
import com.cdw.zhihutopnews.face.FaceLoginActivity;
import com.cdw.zhihutopnews.personDataBase.UserDataManager;
import com.isnc.facesdk.SuperID;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/26.
 * 个人界面
 */

public class PersonFragment extends BaseFragment {

    @BindView(R.id.login_btn_register)
    Button mRegisterButton;//注册按钮
    @BindView(R.id.login_btn_login)
    Button mLoginButton;//登录按钮
    @BindView(R.id.login_btn_cancle)
    Button mCancleButton;//注销按钮

    @BindView(R.id.login_edit_account)
    EditText mAccount; //用户名编辑
    @BindView(R.id.login_edit_pwd)
    EditText mPwd;  //密码编辑

    @BindView(R.id.Login_Remember)
    CheckBox mRememberCheck;//记住密码按钮
    @BindView(R.id.login_text_change_pwd)
    TextView mChangepwdText;//修改密码

    @BindView(R.id.login_view)
    RelativeLayout loginView;
    @BindView(R.id.login_success_view)
    RelativeLayout loginSuccessView;

    @BindView(R.id.bt_qq)
    Button btQQ;//QQ登陆
    @BindView(R.id.bt_face)
    Button btFace;//刷脸登陆

    @BindView(R.id.fragmeng_person)
    FrameLayout fragmengPerson;
    Fragment fragment;
    private SharedPreferences login_sp;
    private String userNameValue, passwordValue;
    private UserDataManager mUserDataManager;         //用户数据管理类

    View.OnClickListener mListener = new View.OnClickListener() {                  //不同按钮按下的监听事件选择
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.login_btn_register:                            //登录界面的注册按钮
                    Intent intent_Login_to_Register = new Intent(getActivity(), RegisterActivity.class);    //切换Login Activity至User Activity
                    startActivity(intent_Login_to_Register);
//                    finish();
                    break;
                case R.id.login_btn_login:                              //登录界面的登录按钮
                    login();
                    break;
                case R.id.login_btn_cancle:                             //登录界面的注销按钮
                    cancel();
                    break;
                case R.id.login_text_change_pwd:                             //登录界面的修改密码按钮
                    Intent intent_Login_to_reset = new Intent(getActivity(), ResetpwdActivity.class);    //切换Login Activity至User Activity
                    startActivity(intent_Login_to_reset);
//                    finish();
                    break;
                case R.id.bt_qq:                             //QQ登录界面的按钮
                    Toast.makeText(getActivity(), "QQ登陆", Toast.LENGTH_SHORT).show();
                    Intent intent_QQ_to_Login = new Intent(getActivity(), QQLoginActivity.class);    //切换Login Activity至User Activity
                    startActivity(intent_QQ_to_Login);

                    break;
                case R.id.bt_face:                             //s刷脸登录界面的注销按钮
                    Toast.makeText(getActivity(), "刷脸登陆", Toast.LENGTH_SHORT).show();
                    Intent intent_face_to_Login = new Intent(getActivity(), FaceLoginActivity.class);    //切换Login Activity至User Activity
                    startActivity(intent_face_to_Login);
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.person_login, container, false);
        SuperID.initFaceSDK(getActivity());
        SuperID.setDebugMode(true);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        login_sp = getActivity().getSharedPreferences("userInfo", 0);
        String name = login_sp.getString("USER_NAME", "");
        String pwd = login_sp.getString("PASSWORD", "");
        boolean choseRemember = login_sp.getBoolean("mRememberCheck", false);
        boolean choseAutoLogin = login_sp.getBoolean("mAutologinCheck", false);
        //如果上次选了记住密码，那进入登录页面也自动勾选记住密码，并填上用户名和密码
        if (choseRemember) {
            mAccount.setText(name);
            mPwd.setText(pwd);
            mRememberCheck.setChecked(true);
        }

        mRegisterButton.setOnClickListener(mListener);                      //采用OnClickListener方法设置不同按钮按下之后的监听事件
        mLoginButton.setOnClickListener(mListener);
        mCancleButton.setOnClickListener(mListener);
        mChangepwdText.setOnClickListener(mListener);
        btQQ.setOnClickListener(mListener);
        btFace.setOnClickListener(mListener);

        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(getActivity());
            mUserDataManager.openDataBase();                              //建立本地数据库
        }

    }

    /**
     * 登陆
     */
    public void login() {                                              //登录按钮监听事件
        if (isUserNameAndPwdValid()) {
            String userName = mAccount.getText().toString().trim();    //获取当前输入的用户名和密码信息
            String userPwd = mPwd.getText().toString().trim();
            SharedPreferences.Editor editor = login_sp.edit();
            int result = mUserDataManager.findUserByNameAndPwd(userName, userPwd);
            if (result == 1) {                                             //返回1说明用户名和密码均正确
                //保存用户名和密码
                editor.putString("USER_NAME", userName);
                editor.putString("PASSWORD", userPwd);

                //是否记住密码
                if (mRememberCheck.isChecked()) {
                    editor.putBoolean("mRememberCheck", true);
                } else {
                    editor.putBoolean("mRememberCheck", false);
                }
                editor.commit();

                Intent intent = new Intent(getActivity(), UserActivity.class);    //切换Login Activity至User Activity
                Bundle dataBundle = new Bundle();
                dataBundle.putString("name", userName);
                intent.putExtras(dataBundle);
                startActivity(intent);
//                finish();
                Toast.makeText(getActivity(), getString(R.string.login_success), Toast.LENGTH_SHORT).show();//登录成功提示
            } else if (result == 0) {
                Toast.makeText(getActivity(), getString(R.string.login_fail), Toast.LENGTH_SHORT).show();  //登录失败提示
            }
        }
    }

    public void cancel() {           //注销
        if (isUserNameAndPwdValid()) {
            String userName = mAccount.getText().toString().trim();    //获取当前输入的用户名和密码信息
            String userPwd = mPwd.getText().toString().trim();
            int result = mUserDataManager.findUserByNameAndPwd(userName, userPwd);
            if (result == 1) {                                             //返回1说明用户名和密码均正确
//                Intent intent = new Intent(Login.this,User.class) ;    //切换Login Activity至User Activity
//                startActivity(intent);
                Toast.makeText(getActivity(), getString(R.string.cancel_success), Toast.LENGTH_SHORT).show();//登录成功提示
                mPwd.setText("");
                mAccount.setText("");
                mUserDataManager.deleteUserDatabyname(userName);
            } else if (result == 0) {
                Toast.makeText(getActivity(), getString(R.string.cancel_fail), Toast.LENGTH_SHORT).show();  //登录失败提示
            }
        }

    }

    /**
     * 验证用户输入
     * @return
     */
    public boolean isUserNameAndPwdValid() {
        if (mAccount.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), getString(R.string.account_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (mPwd.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), getString(R.string.pwd_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    @Override
    public void onResume() {
        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(getActivity());
            mUserDataManager.openDataBase();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (mUserDataManager != null) {
            mUserDataManager.closeDataBase();
            mUserDataManager = null;
        }
        super.onPause();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    /**
     * 日间夜间模式切换时更新UI
     */
    public void refreshUI() {
        fragmengPerson.setBackgroundColor(getResources().getColor(Config.isNight ? R.color.background_dark : R.color.background_light));

    }

}
