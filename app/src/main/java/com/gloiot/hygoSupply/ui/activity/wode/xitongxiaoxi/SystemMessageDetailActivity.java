package com.gloiot.hygoSupply.ui.activity.wode.xitongxiaoxi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SystemMessageDetailActivity extends BaseActivity {

    @Bind(R.id.tv_system_message_type)
    TextView tvSystemMessageType;
    @Bind(R.id.tv_system_message_title)
    TextView tvSystemMessageTitle;
    @Bind(R.id.tv_system_message_content)
    TextView tvSystemMessageContent;
    @Bind(R.id.btn_system_message_edit)
    Button btnSystemMessageEdit;
    private String id;
    private SystemMessageModel model;

    @Override
    public int initResource() {
        return R.layout.activity_system_message_detail;
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar(this, true, "系统消息", "");
        model = new SystemMessageModel();
        id = getIntent().getStringExtra("id");

        btnSystemMessageEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SystemMessageDetailActivity.this, SendSystemMessageActivity.class);
                intent.putExtra("model", model);
                intent.putExtra("isEdit", "是");
                startActivity(intent);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        requestHandleArrayList.add(requestAction.shop_wl_mass_detail(SystemMessageDetailActivity.this, phone, id));
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        model.setId(id);
        model.setType(response.getString("消息类型"));
        model.setContent(response.getString("发送内容"));
        model.setTitle(response.getString("消息标题"));
        updata();
    }

    private void updata() {
        tvSystemMessageContent.setText(model.getContent());
        tvSystemMessageTitle.setText(model.getTitle());
        tvSystemMessageType.setText(model.getType());

    }

}
