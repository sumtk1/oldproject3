package com.gloiot.hygoSupply.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.RelativeLayout;

import com.gloiot.chatsdk.DataBase.IMDBManager;
import com.gloiot.chatsdk.socket.SocketListener;
import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.serrver.network.RequestAction;
import com.gloiot.hygoSupply.ui.activity.login.LoginActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.utlis.DialogUtlis;
import com.loopj.android.http.RequestHandle;
import com.zyd.wlwsdk.server.network.OnDataListener;
import com.zyd.wlwsdk.utlis.L;
import com.zyd.wlwsdk.utlis.MToast;
import com.zyd.wlwsdk.utlis.SharedPreferencesUtlis;
import com.zyd.wlwsdk.widge.LoadDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


/**
 * Created by hygo00 on 16/12/5.
 */
public abstract class BaseFragment extends Fragment implements OnDataListener, EasyPermissions.PermissionCallbacks {
    private static final String TAG = "BaseFragment";

    protected Context mContext;

    protected RequestAction requestAction;

    protected ArrayList<RequestHandle> requestHandleArrayList = new ArrayList<>();

    protected SharedPreferences preferences;

    protected SharedPreferences.Editor editor;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = SharedPreferencesUtlis.getInstance().getSharedPreferences();
        editor = preferences.edit();
        mContext = getActivity();
        requestAction = new RequestAction();
    }

    @Override
    public void onPause() {
        super.onPause();
        cancelRequestHandle();
    }

    /**
     * 请求开始
     *
     * @param requestTag 请求标志
     */
    @Override
    public void onStart(int requestTag, int showLoad) {
        L.d(TAG, "onStart: " + requestTag);
        if (showLoad == 0 || showLoad == 1) {
            LoadDialog.show(mContext, requestHandleArrayList);
        }
    }

    /**
     * 请求成功(过滤 状态=成功)
     *
     * @param requestTag 请求标志
     * @param response   请求返回
     * @throws JSONException
     */
    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {

    }

    /**
     * 请求成功
     *
     * @param requestTag 请求标志
     * @param response   请求返回
     */
    @Override
    public void onSuccess(int requestTag, JSONObject response, int showLoad) {
        L.e("HttpRequest", requestTag + "请求成功，返回Json数据为：\n" + response.toString() + "\n");
        if (showLoad == 0 || showLoad == 2) {
            LoadDialog.dismiss(mContext);
        }
        try {
            if (response.getString("状态").equals("成功")) {
                requestSuccess(requestTag, response, showLoad);
            } else if (response.getString("状态").equals("随机码不正确")) {
                DialogUtlis.oneBtnNormal(mContext, "该账号在其他设备登录\n请重新登录", "确定", false, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtlis.dismissDialog();
                        editor.putString(ConstantUtils.SP_LOGINTYPE, "未登录");
                        editor.putString(ConstantUtils.SP_RANDOMCODE, "");
                        editor.commit();
                        SocketListener.getInstance().signoutRenZheng();
                        IMDBManager.getInstance(mContext).ClearnData();
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        mContext.startActivity(intent);


                    }
                });
            } else {
                statusUnusual(response);
            }
        } catch (JSONException e) {
            e.printStackTrace();
//            L.e("HttpRequest1", requestTag + "异常信息：" + e.getMessage());
//            DialogUtlis.oneBtnNormal(mContext, "数据异常-1\n请稍后再试");
        } catch (Exception e) {
            e.printStackTrace();
//            L.e("HttpRequest2", requestTag + "异常信息：" + e.getMessage());
//            DialogUtlis.oneBtnNormal(mContext, "数据异常-2\n请稍后再试");
        }
    }

    //状态异常的情况
    public void statusUnusual(JSONObject response) throws JSONException {
        MToast.showToast(response.getString("状态"));
    }

    /**
     * 请求失败
     *
     * @param requestTag    请求标志
     * @param errorResponse 错误请求返回
     */
    @Override
    public void onFailure(int requestTag, JSONObject errorResponse, int showLoad) {
        L.e(TAG, "onFailure: " + requestTag + " errorResponse: " + errorResponse);
        MToast.showToast("请求超时,请检查你的网络!");
        LoadDialog.dismiss(mContext);
    }

    @Override
    public void onCancel(int requestTag, int showLoad) {
        L.e(TAG, "onCancel: " + requestTag);
    }

    /**
     * 取消网络请求
     */
    public void cancelRequestHandle() {
        if (requestHandleArrayList.size() != 0) {
            for (int i = 0; i < requestHandleArrayList.size(); i++) {
                requestHandleArrayList.get(i).cancel(true);
            }
        }
    }


//    public boolean checkIsSetPwd() {
//        if (preferences.getString(ConstantUtils.SP_MYPWD, "否").equals("否")) {
//            CommonUtils.toSetPwd(this.mContext);
//            return false;
//        } else {
//            return true;
//        }
//    }

    //判断是否登录再跳转
    public boolean check_login_tiaozhuang() {
        L.e("跳转执行：" + this.getClass().getName());
        if (preferences.getString(ConstantUtils.SP_LOGINTYPE, "").equals("成功")) {
            return true;
        } else {
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
            return false;
        }
    }

    public boolean checkIsSetPwd() {
        if (preferences.getString(ConstantUtils.SP_MYPWD, "否").equals("否")) {
            CommonUtils.toSetPwd(this.mContext);
            return false;
        } else {
            return true;
        }
    }

    //设置状态栏高度
    public void setStatusBarHeight(View view) {
        int result = 0;
        int resourceId = getActivity().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        layoutParams.height = result;
        view.setLayoutParams(layoutParams);
    }

    /**
     * ---------------------------------------------------------------------------------------------
     * 权限回调接口
     */
    private CheckPermListener mListener;
    private String[] mPerms;

    public interface CheckPermListener {
        //权限通过后的回调方法
        void superPermission();
    }

    public void checkPermission(CheckPermListener mListener, int resString, String... mPerms) {
        this.mListener = mListener;
        this.mPerms = mPerms;

        if (pub.devrel.easypermissions.EasyPermissions.hasPermissions(mContext, mPerms)) {
            if (mListener != null)
                mListener.superPermission();
        } else {
            pub.devrel.easypermissions.EasyPermissions.requestPermissions(this, getString(resString), 123, mPerms);
        }
    }

    /**
     * 用户权限处理,
     * 如果全部获取, 则直接过.
     * 如果权限缺失, 则提示Dialog.
     *
     * @param requestCode  请求码
     * @param permissions  权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        pub.devrel.easypermissions.EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            //设置返回
            if (pub.devrel.easypermissions.EasyPermissions.hasPermissions(mContext, mPerms)) {
                if (mListener != null)
                    mListener.superPermission();
            }
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        //同意了某些权限可能不是全部
        if (pub.devrel.easypermissions.EasyPermissions.hasPermissions(mContext, mPerms)) {
            if (mListener != null)
                mListener.superPermission();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (pub.devrel.easypermissions.EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this)
                    .setTitle("设置")
                    .setRationale(R.string.perm_tip)
                    .build().show();
        }
    }
}