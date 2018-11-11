package com.gloiot.hygoSupply.ui.activity.wode.fadongtai;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.ui.activity.wode.fadongtai.model.ImageTextModel;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.utlis.MToastUtils;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.abslistview.CommonAdapter;
import com.zyd.wlwsdk.server.AliOss.ChoosePhoto;
import com.zyd.wlwsdk.utlis.MToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BianJiNeiRongActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.lv_bianji_neirong)
    ListView lv_bianji_neirong;
    @Bind(R.id.rl_bianji_tianjiatupian)
    RelativeLayout rlBianjiTianjiatupian;
    private String icon = "";
    private List<ImageTextModel> list = new ArrayList<>();
    private View view;
    private ImageView im_bianji_zhanshitupian, imBianJiZhanshi, im_bianji_tianjiatupian;
    private TextView tvTianjiazhanshitupian, tv_suggestion_num, tv_suggestion_sum;
    private CommonAdapter myTianJiaAdapter;
    private EditText et_bianji_biaoti;
    private CharSequence tt;
    private int num = 50;
    private int selectionStart;
    private int selectionEnd;


    @Override
    public int initResource() {
        return R.layout.activity_bianjineirong;
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar((Activity) mContext, true, "编辑内容", "");
        view = View.inflate(mContext, R.layout.item_tianjianeirong_toubu, null);
        lv_bianji_neirong.addHeaderView(view);
        lv_bianji_neirong.setDividerHeight(0);
        lv_bianji_neirong.setAdapter(myTianJiaAdapter);
        et_bianji_biaoti = (EditText) view.findViewById(R.id.et_bianji_biaoti);
        im_bianji_zhanshitupian = (ImageView) view.findViewById(R.id.im_bianji_zhanshitupian);
        im_bianji_tianjiatupian = (ImageView) findViewById(R.id.im_bianji_tianjiatupian);
        imBianJiZhanshi = (ImageView) view.findViewById(R.id.im_bianji_zhanshi);
        tvTianjiazhanshitupian = (TextView) view.findViewById(R.id.tv_tianjiazhanshitupian);
        tv_suggestion_num = (TextView) view.findViewById(R.id.tv_suggestion_num);
        tv_suggestion_sum = (TextView) view.findViewById(R.id.tv_suggestion_sum);
        et_bianji_biaoti.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tt = charSequence;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                tv_suggestion_num.setText(editable.length() + "");
                selectionStart = et_bianji_biaoti.getSelectionStart();
                selectionEnd = et_bianji_biaoti.getSelectionEnd();
                if (tt.length() > num) {
                    editable.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    et_bianji_biaoti.setText(editable);
                    et_bianji_biaoti.setSelection(tempSelection);//设置光标在最后
                }
            }
        });

        //选择展示图片
        view.findViewById(R.id.rl_bianji_zhanshitupian).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission(new CheckPermListener() {
                    @Override
                    public void superPermission() {
                        checkPermission(new CheckPermListener() {
                            @Override
                            public void superPermission() {
                                new ChoosePhoto(mContext) {
                                    @Override
                                    protected void setPicSuccess(final String myImgSrc, final String myPicUrl) {
                                        BianJiNeiRongActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                CommonUtils.setDisplayImageOptions(im_bianji_zhanshitupian, myPicUrl, 0);
                                                icon = myPicUrl;
                                                tvTianjiazhanshitupian.setVisibility(View.GONE);
                                                imBianJiZhanshi.setVisibility(View.GONE);
//                                                Log.e("店招图片添加成功", "图片添加成功" + icon);
                                            }
                                        });
                                    }

                                    @Override
                                    protected void setPicFailure() {
                                        //设置图片失败
                                        MToast.showToast("图片上传失败,请重新设置");
                                    }
                                }.setPic(false, true); //是否是头像

                            }
                        }, R.string.perm_camera, Manifest.permission.CAMERA);
                    }
                }, R.string.perm_WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        });

        myTianJiaAdapter = new CommonAdapter<ImageTextModel>(mContext, R.layout.item_tianjiarenrong_shuoming, list) {
            @Override
            public void convert(ViewHolder holder, ImageTextModel neirongTianJia) {
                ImageView im_tianjianeirong_tupian = holder.getView(R.id.im_tianjianeirong_tupian);
                holder.setText(R.id.tv_tianjianeirong_miaoshu, neirongTianJia.getNeirong());
                CommonUtils.setDisplayImageOptions(im_tianjianeirong_tupian, neirongTianJia.getTupian(), 0);
            }
        };
        lv_bianji_neirong.setAdapter(myTianJiaAdapter);
        lv_bianji_neirong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    ImageTextModel model = list.get(position-1);
                    if (null != model) {
                        Intent intent = new Intent(BianJiNeiRongActivity.this, TianJiaNeiRongActivity.class);
                        intent.putExtra("content", "添加内容");
                        intent.putExtra("position", position);
                        intent.putExtra("describe", model.getNeirong());
                        intent.putExtra("image", model.getTupian());
                        startActivityForResult(intent, 1);
                    }


                }
            }
        });


    }

    @OnClick({R.id.im_bianji_tianjiatupian, R.id.but_bianji_fabu})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.im_bianji_tianjiatupian:
                if (null != list && list.size() > 9) {
                    MToastUtils.showToast("添加内容不能超过9条");
                } else {
                    Intent intent = new Intent(BianJiNeiRongActivity.this, TianJiaNeiRongActivity.class);
                    intent.putExtra("content", "添加内容");
                    startActivityForResult(intent, 1);
                }

                break;
            case R.id.but_bianji_fabu:
                String biaoti = et_bianji_biaoti.getText().toString();
                if (TextUtils.isEmpty(biaoti)) {
                    MToast.showToast("请添加内容");
                } else if (TextUtils.isEmpty(icon)) {
                    MToast.showToast("请添加内容");
                } else {
                    requestHandleArrayList.add(requestAction.shop_wl_releasedynamic(BianJiNeiRongActivity.this
                            , preferences.getString(ConstantUtils.SP_ZHANGHAO, ""), preferences.getString(ConstantUtils.SP_ONLYID, ""), list, "图文", icon, biaoti, "", null));
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (data != null) {
                    String neirong;
                    String tupian;
                    neirong = data.getStringExtra("neirong");
                    tupian = data.getStringExtra("zhaopian");
                    int position = data.getIntExtra("position", 0);
                    final ImageTextModel neirongtianjia;
                    if (position > 0) {
                        neirongtianjia = list.get(position-1);
                        neirongtianjia.setNeirong(data.getStringExtra("neirong"));
                        neirongtianjia.setTupian(data.getStringExtra("zhaopian"));
                    } else {
                        neirongtianjia = new ImageTextModel(neirong, tupian);
                        neirongtianjia.setNeirong(data.getStringExtra("neirong"));
                        neirongtianjia.setTupian(data.getStringExtra("zhaopian"));
                        list.add(neirongtianjia);
                    }
                    myTianJiaAdapter.notifyDataSetChanged();
                    if (list.size() == 9) {
                        rlBianjiTianjiatupian.setVisibility(View.GONE);
                    }
                    lv_bianji_neirong.smoothScrollToPosition(list.size());

                } else {
                    return;
                }
                break;
        }
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_p_add_findTable:
                if ("成功".equals(response.getString("状态"))) {
                    MToast.showToast("发布成功");
//                    setResult(RESULT_OK);
                    finish();
                } else {
                    MToast.showToast(response.getString("状态"));
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
