package com.cdw.zhihutopnews.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cdw.zhihutopnews.R;
import com.cdw.zhihutopnews.fragment.PersonFragment;

public class UserActivity extends AppCompatActivity {
    private TextView tv;
    private Button mReturnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_usercenter);
        tv = (TextView) findViewById(R.id.tv_name);
        Bundle dataBundle = this.getIntent().getExtras();//获得当前Intent内数据Bundle
        String name = dataBundle.getString("name");//从Bundle中获得对应数据
        tv.setText(name);//查找Activity中的View
        mReturnButton = (Button) findViewById(R.id.bt_logout);
//        mReturnButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(UserActivity.this, "退出SQLite登陆", Toast.LENGTH_SHORT).show();
//                FragmentManager fm = getSupportFragmentManager();
//                FragmentTransaction ft = fm.beginTransaction();
//                ft.replace(R.id.fragment_container, new PersonFragment());
//                ft.commit();
//                finish();
//            }
//        });

    }

    public void btn_logout(View view) {
//        Toast.makeText(UserActivity.this, "退出SQLite登陆", Toast.LENGTH_SHORT).show();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, new PersonFragment());
        ft.commit();
        finish();

    }
}
