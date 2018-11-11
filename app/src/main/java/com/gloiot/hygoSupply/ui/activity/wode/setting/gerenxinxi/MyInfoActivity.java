package com.gloiot.hygoSupply.ui.activity.wode.setting.gerenxinxi;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.chatsdk.DataBase.IMDBManager;
import com.gloiot.chatsdk.bean.UserInfo;
import com.gloiot.chatsdk.chatui.UserInfoCache;
import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.utlis.DialogUtlis;
import com.zyd.wlwsdk.autolayout.AutoRelativeLayout;
import com.zyd.wlwsdk.server.AliOss.ChoosePhoto;
import com.zyd.wlwsdk.utlis.MToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;

public class MyInfoActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.iv_my_info_touxiang)
    ImageView iv_my_info_touxiang;
    @Bind(R.id.rl_my_info_touxiang)
    RelativeLayout rl_my_info_touxiang;
    @Bind(R.id.tv_my_info_nicheng)
    TextView tv_my_info_nicheng;
    @Bind(R.id.rl_my_info_nicheng)
    RelativeLayout rl_my_info_nicheng;
    @Bind(R.id.tv_my_info_xingbie)
    TextView tv_my_info_xingbie;
    @Bind(R.id.rl_my_info_xingbie)
    RelativeLayout rl_my_info_xingbie;
    @Bind(R.id.tv_my_info_phone)
    TextView tv_my_info_phone;
    @Bind(R.id.rl_my_info_phone)
    RelativeLayout rl_my_info_phone;

    private SharedPreferences.Editor editor;
    private String phone, xingbie = "", nicheng = "", myUserImage = "";
    private String phoneNum="";//绑定的手机号

    @Override
    public int initResource() {
        return R.layout.activity_my_info;
    }

    @Override
    public void initData() {
        editor = preferences.edit();
        CommonUtils.setTitleBar((Activity) mContext, true, "个人信息", "");
        myUserImage = preferences.getString(ConstantUtils.SP_USERIMG, "");
        nicheng = preferences.getString(ConstantUtils.SP_USERNICHENG, "");
        xingbie = preferences.getString(ConstantUtils.SP_USERSEX, "");
        phoneNum = preferences.getString(ConstantUtils.SP_USERPHONE, "");
        if (!myUserImage.equals("")) {
            CommonUtils.setDisplayImageOptions(iv_my_info_touxiang, myUserImage, 8);
            myUserImage = "";
        }
        if (nicheng != null && !nicheng.equals("")) {
            Log.e("11", "nicheng不空" + nicheng);
            tv_my_info_nicheng.setText(nicheng);
            nicheng = "";
        }
        if (!xingbie.equals("")) {
            tv_my_info_xingbie.setText(xingbie);
            xingbie = "";
        }


        rl_my_info_touxiang.setOnClickListener(this);
        rl_my_info_nicheng.setOnClickListener(this);
        rl_my_info_xingbie.setOnClickListener(this);
        rl_my_info_phone.setOnClickListener(this);
        phone = preferences.getString(ConstantUtils.SP_MYID, "");
        tv_my_info_phone.setText(phoneNum);
    }

    @Override
    public void onResume() {
        super.onResume();
        String nicheng = preferences.getString(ConstantUtils.SP_USERNICHENG, "");
//        requestHandleArrayList.add(requestAction.shop_set(MyInfoActivity.this, phone, "", "", ""));
        if (!nicheng.equals("")) {
            tv_my_info_nicheng.setText(nicheng);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_my_info_touxiang: //设置头像
                touxiang_shangchuan();
                break;
            case R.id.rl_my_info_nicheng: //设置昵称
                if (!tv_my_info_nicheng.getText().toString().equals("")) {
                    return;
                }
                startActivity(new Intent(this, MyNichengActivity.class));
                break;
            case R.id.rl_my_info_xingbie: //设置性别
                settingXingbie();
                break;
            case R.id.rl_my_info_phone:
                startActivity(new Intent(this, GengHuanShouJiActivity.class).putExtra("手机号", tv_my_info_phone.getText().toString()));
                break;
        }
    }

    //设置性别
    private void settingXingbie() {

        DialogUtlis.towBtnSex(mContext, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int denger = DialogUtlis.getSex();
                DialogUtlis.myDialogBuilder.dismiss();
                HashMap<String, Object> hashMap = new HashMap<>();
                if (denger == -1) {
                    return;
                }
//                hashMap.put("性别", denger == 0 ? "男" : "女");
                updateMyData(tv_my_info_xingbie, denger == 0 ? "男" : "女");
            }
        });
//        final MyDialogBuilder myDialogBuilder;
//        myDialogBuilder = MyDialogBuilder.getInstance(mContext);
//        myDialogBuilder
//                .withTitie("请选择性别")
//                .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
//                .setBtnClick("男", MyDialogBuilder.BtnNormal, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        xingbie = "男";
//                        progressDialogUilis.show();
//                        requestHandleArrayList.add(requestAction.shop_set(MyInfoActivity.this, phone, xingbie, "", ""));
//                        myDialogBuilder.dismiss();
//
//                    }
//                })
//                .setBtnClick("女", MyDialogBuilder.BtnNormal, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        progressDialogUilis.show();
//
//                        myDialogBuilder.dismiss();
//                    }
//                })
//                .setBtnClick("取消", MyDialogBuilder.BtnCancel, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        myDialogBuilder.dismiss();
//                    }
//                })
//                .show();

    }

    /**
     * 修改资料
     */
    private void updateMyData(final TextView textview, final String content) {
        xingbie = content;
        requestHandleArrayList.add(requestAction.shop_set(MyInfoActivity.this, phone, xingbie, "", ""));
        tv_my_info_xingbie = textview;
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject jsonObject, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, jsonObject, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_set:     //个人信息请求成功
                String shoujihao = jsonObject.getString("手机号");
                if (!shoujihao.equals("")) {
                    tv_my_info_phone.setText(shoujihao);
                }
                if (!myUserImage.equals("")) {
                    Log.e("设置头像", myUserImage);
                    editor.putString(ConstantUtils.SP_USERIMG, myUserImage);
                    editor.putBoolean(ConstantUtils.SP_CHANGEUSERIMG, true);
                    CommonUtils.setDisplayImageOptions(iv_my_info_touxiang, myUserImage, 8);
                    myUserImage = "";
                    editor.commit();
                    UserInfo userInfo = new UserInfo();
                    userInfo.setName(preferences.getString(ConstantUtils.SP_USERNICHENG, ""));
                    userInfo.setId(preferences.getString(ConstantUtils.SP_MYID, "") + "_商家");
                    userInfo.setUrl(preferences.getString(ConstantUtils.SP_USERIMG, ""));
//                    UserInfoCache.getInstance(mContext).upData(userInfo.getId(), userInfo);
                    UserInfoCache.getInstance(mContext).upData(userInfo.getId(), userInfo, preferences.getString(ConstantUtils.SP_MYID, ""));
                    MToast.showToast("修改成功");

                } else if (!xingbie.equals("")) {
                    editor.putString(ConstantUtils.SP_USERSEX, xingbie);
                    tv_my_info_xingbie.setText(xingbie);
                    xingbie = "";
                    MToast.showToast("修改成功");
                    editor.commit();
                }

                break;

        }
    }

    //上传头像
    private void touxiang_shangchuan() {
        checkPermission(new CheckPermListener() {
            @Override
            public void superPermission() {
                checkPermission(new CheckPermListener() {
                    @Override
                    public void superPermission() {
                        new ChoosePhoto(mContext) {
                            @Override
                            protected void setPicSuccess(final String imgSrc, final String myPicUrl) {
                                MyInfoActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        myUserImage = imgSrc;
                                        requestHandleArrayList.add(requestAction.shop_set(MyInfoActivity.this, phone, "", "", myPicUrl));
                                    }
                                });
                            }

                            @Override
                            protected void setPicFailure() {
                                //设置头像失败
                                MToast.showToast("图片上传失败,请重新设置");
                            }
                        }.setPic(true, false);
                    }
                }, R.string.perm_camera, Manifest.permission.CAMERA);
            }
        }, R.string.perm_WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);

    }


    @Override
    protected void onDestroy() {
        xingbie = "";
        super.onDestroy();
    }
}
