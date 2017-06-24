package com.cdw.zhihutopnews.face;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cdw.zhihutopnews.R;
import com.google.gson.Gson;
import com.isnc.facesdk.common.Cache;
import com.isnc.facesdk.common.DebugMode;
import com.isnc.facesdk.common.SDKConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by zhangdai on 2017/5/15.
 */

public class GroupFace extends Activity {
    private GroupNetWork mGroupNetWork;

    String[] texts = new String[]{"好", "玩", "的", "刷", "脸", "应", "用", "请", "使", "用", "一", "登", "刷", "脸", "登", "录"};
    Random mRandom = new Random();
    String openId;
    List<GroupData.DataBean.GroupsBean> mList = new ArrayList<GroupData.DataBean.GroupsBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_group_face);
        mGroupNetWork = new GroupNetWork(this);
        openId = Cache.getCached(this, SDKConfig.KEY_OPENID);//openId,
        mGroupNetWork.getGroupList("", new GroupNetWork.OnNetworkCallBack() {
            @Override
            public void onSuccessCallBack(Object data) {

                GroupData groupData = new Gson().fromJson(data.toString(), GroupData.class);
                mList = groupData.getData().getGroups();
            }
        });
    }

    public void btnAddGroup(View view) {
        mGroupNetWork.createGroup(getARandomName(), "", null, new GroupNetWork.OnNetworkCallBack() {
            @Override
            public void onSuccessCallBack(Object data) {
                DebugMode.debug(">onSuccessCallBack>>>" + data.toString());
            }
        });
    }

    public void btnDeleteGroup(View view) {
        if (mList.size() > 0) {
            mGroupNetWork.deleteGroup(mList.get(0).getId(), new GroupNetWork.OnNetworkCallBack() {
                @Override
                public void onSuccessCallBack(Object data) {
                    DebugMode.debug(">>onSuccessCallBack>>" + data.toString());
                }
            });
        }
    }

    public void btnAddUserToGroup(View view) {
        if (mList.size() > 0) {
            mGroupNetWork.addUserToGroup(openId, mList.get(0).getId(), new GroupNetWork.OnNetworkCallBack() {
                @Override
                public void onSuccessCallBack(Object data) {
                    DebugMode.debug(">onSuccessCallBack>>>" + data.toString());
                }
            });
        }
    }

    public void btnDeleteUserFromGroup(View view) {
        if (mList.size() > 0) {
            mGroupNetWork.deleteUserFromGroup(openId, mList.get(0).getId(), new GroupNetWork.OnNetworkCallBack() {
                @Override
                public void onSuccessCallBack(Object data) {
                    DebugMode.debug(">onSuccessCallBack>>>" + data.toString());
                }
            });
        }
    }


    private String getARandomName() {
        int length = 2 + mRandom.nextInt(3);
        String name = "";
        for (int i = 0; i < length; i++) {
            name += texts[mRandom.nextInt(texts.length)];
        }
        return name;
    }

    public void btnFaceGroup(View v){
        if (mList.size()>0) {
            Intent intent = new Intent(this, FaceGroupCompareDemo.class);
            intent.putExtra("groupId", mList.get(0).getId());
            startActivity(intent);
        }
    }

    public void btn_back(View v) {
        finish();
    }
}

