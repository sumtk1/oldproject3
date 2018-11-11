package com.gloiot.hygoSupply.ui.activity.wode.xitongxiaoxi;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.ui.activity.promotional.ShopPromotionRecordActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.DialogUtlis;
import com.gloiot.hygoSupply.utlis.MToastUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zyd.wlwsdk.utlis.MToast;
import com.zyd.wlwsdk.widge.MyDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者：Ljy on 2018/1/2.
 * 功能：
 */


public class SendSystemMessageActivity extends BaseActivity {
    @Bind(R.id.et_system_message_title)
    EditText etSystemMessageTitle;
    @Bind(R.id.tv_system_message_titlenum)
    TextView tvSystemMessageTitlenum;
    @Bind(R.id.et_system_message_content)
    EditText etSystemMessageContent;
    @Bind(R.id.tv_system_message_contentnum)
    TextView tvSystemMessageContentnum;
    @Bind(R.id.tv_system_message_type_choice)
    TextView tvSystemMessageTypeChoice;
    private String typeCreate[] = new String[]{"系统维护", "重要通知", "通用通知", "紧急通知", "最新通知", "临时通知"};
    private String typeEdit[] = new String[]{"系统维护", "重要通知", "通用通知", "紧急通知", "最新通知", "临时通知", "无效消息"};
    private String[] currentType;
    @Bind(R.id.btn_system_message_send)
    Button btnSystemMessageSend;
    private int position = 1;
    private boolean isEdit;
    private SystemMessageModel model;

    @Override
    public int initResource() {
        return R.layout.activity_send_system_message;
    }

    @Override
    public void initData() {
        isEdit = "是".equals(getIntent().getStringExtra("isEdit"));//判断是否是编辑模式
        if (isEdit) {
            model = getIntent().getParcelableExtra("model");
            model.setEdit(true);
            tvSystemMessageTypeChoice.setText(model.getType());
            etSystemMessageTitle.setText(model.getTitle());
            etSystemMessageContent.setText(model.getContent());
            tvSystemMessageTitlenum.setText(model.getTitle().length() + "/30");
            tvSystemMessageContentnum.setText(model.getContent().length() + "/500");
            etSystemMessageTitle.setFocusable(false);
            etSystemMessageTitle.setFocusableInTouchMode(false);
            currentType = typeEdit;
            CommonUtils.setTitleBar(this, true, "编辑系统消息", "");
        } else {
            model = new SystemMessageModel();
            model.setEdit(false);
            currentType = typeCreate;
            CommonUtils.setTitleBarWithRightClick(this, true, "发送系统消息", "消息记录", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(SendSystemMessageActivity.this, SystemMessageRecordActivity.class));
                }
            });
        }


        btnSystemMessageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check()) {
                    requestHandleArrayList.add(requestAction.shop_wl_mass(SendSystemMessageActivity.this, phone, model));
                }
            }
        });


        etSystemMessageTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tvSystemMessageTitlenum.setText(s.length() + "/30");
                model.setTitle(s.toString());
            }
        });
        etSystemMessageContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tvSystemMessageContentnum.setText(s.length() + "/500");
                model.setContent(s.toString());
            }
        });

    }

    @OnClick(R.id.tv_system_message_type_choice)
    public void onViewClicked() {
        DialogUtlis.towBtnLoopView(mContext, "请选择消息类型", currentType, position, new MyDialogBuilder.LoopViewCallBack() {
            @Override
            public void callBack(int data) {
                position = data;
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvSystemMessageTypeChoice.setText(currentType[position]);
                model.setType(currentType[position]);
                DialogUtlis.dismissDialog();
            }
        });

    }

    private boolean check() {
        if (TextUtils.isEmpty(model.getType())) {
            MToastUtils.showToast("请选择消息类型");
            return false;
        } else if (TextUtils.isEmpty(model.getTitle())) {
            MToastUtils.showToast("消息标题不能为空");
            return false;
        } else if (TextUtils.isEmpty(model.getContent())) {
            MToastUtils.showToast("消息内容不能为空");
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        if (isEdit) {
            MToastUtils.showToast("编辑成功");
            ImageLoader.getInstance().clearDiskCache();
            ImageLoader.getInstance().clearMemoryCache();
        } else {
            MToastUtils.showToast("发送成功");
        }
        finish();
    }
}
