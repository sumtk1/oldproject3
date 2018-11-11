package com.gloiot.hygoSupply.ui.activity.shangpinshangchuan;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.bean.GuigeModel;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.ui.adapter.GuigeAdapter;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.utlis.MToastUtils;
import com.zyd.wlwsdk.utlis.MToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jp.wasabeef.recyclerview.animators.ScaleInRightAnimator;


public class TianjiaShangpinGuigeActivity_new extends BaseActivity implements View.OnClickListener {
    //判断店铺是否属于服装类
    private boolean isClothes;
    private RecyclerView recyclerView;
    private List<GuigeModel> guigeModelList;
    private GuigeAdapter adapter;
    private Button btn_tianjia_shangpin_guige_queding;
    private ImageView iv_shangpin_guige_back;
    private String zhuyingleimu, phone, productId;
    private TextView tv_shangpin_guige_fabu;
    private TextView tv_toptitle_title;
    private boolean isEdit;//是否从商品编辑页面跳过来

    @Override
    public int initResource() {
        return R.layout.activity_tianjia_shangpin_guige_new;
    }

    @Override
    public void initData() {
        initComponent();
        if ("edit".equals(getIntent().getStringExtra("type"))) {
            isEdit = true;
            tv_shangpin_guige_fabu.setText("保存");
            tv_toptitle_title.setText("修改规格");
        } else {
            tv_shangpin_guige_fabu.setText("发布");
        }
        guigeModelList = new ArrayList<>();
        adapter = new GuigeAdapter(this, guigeModelList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new ScaleInRightAnimator());
        recyclerView.getItemAnimator().setAddDuration(1000);
        iv_shangpin_guige_back.setOnClickListener(this);
        btn_tianjia_shangpin_guige_queding.setOnClickListener(this);
        tv_shangpin_guige_fabu.setOnClickListener(this);
        phone = preferences.getString(ConstantUtils.SP_MYID, "");
        zhuyingleimu = getIntent().getStringExtra("zhuyingleimu");
        productId = getIntent().getStringExtra("productId");
        if (TextUtils.isEmpty(zhuyingleimu)) {
            zhuyingleimu = preferences.getString(ConstantUtils.SP_ZHUYINGLEIBIE, "");
        }
        if (null != zhuyingleimu && (zhuyingleimu.equals("时尚服装") || zhuyingleimu.equals("鞋靴箱包"))) {
            isClothes = true;
        }
        //判断是否有缓存的规格
        if (ShangPinShangChuanActivity.guigeModels.size() > 0) {
            Iterator<GuigeModel> iterator = ShangPinShangChuanActivity.guigeModels.iterator();
            while (iterator.hasNext()) {
                guigeModelList.add(iterator.next().clone());
            }
//             List<GuigeModel> guigeModelList1=new ArrayList<>(ShangPinShangChuanActivity.guigeModels);
//            guigeModelList.addAll(ShangPinShangChuanActivity.guigeModels);
        } else {
            GuigeModel model = new GuigeModel(!isClothes);
            model.setOnlyOne(true);
            guigeModelList.add(model);
        }
        recyclerView.setAdapter(adapter);
    }

    public void initComponent() {
        recyclerView = (RecyclerView) findViewById(R.id.rclv_tianjia_shangpin_guige);
        tv_shangpin_guige_fabu = (TextView) findViewById(R.id.tv_shangpin_guige_fabu);
        iv_shangpin_guige_back = (ImageView) findViewById(R.id.iv_shangpin_guige_back);
        tv_toptitle_title = (TextView) findViewById(R.id.tv_toptitle_title);
        btn_tianjia_shangpin_guige_queding = (Button) findViewById(R.id.btn_tianjia_shangpin_guige_queding);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_tianjia_shangpin_guige_queding:
                //检查所填规格属性是否符合要求
                if (checkFormat()) {
                    GuigeModel model = new GuigeModel(!isClothes);
                    model.setOnlyOne(false);
                    adapter.clearFouces();
                    if (guigeModelList.size() == 1) {//如果添加数据之前只有一条数据，则新增数据的时候要将第一条数据的删除按钮显示出来
                        guigeModelList.get(0).setOnlyOne(false);
                        guigeModelList.add(0, model);
                        adapter.notifyItemInserted(0);
                        adapter.notifyItemChanged(1);
                    } else {
                        guigeModelList.add(0, model);
                        adapter.notifyItemInserted(0);
                        recyclerView.smoothScrollToPosition(0);
                    }
                } else {
                    MToast.showToast(tag);
                    recyclerView.smoothScrollToPosition(position);
                }
                break;
            case R.id.iv_shangpin_guige_back:
                onBackPressed();
                break;

            case R.id.tv_shangpin_guige_fabu:
                if (checkFormat()) {
                    if (isEdit) {
                        isEdit = false;
//                        Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    } else {
                        requestHandleArrayList.add(requestAction.shop_sp_addSpec(this, phone, guigeModelList, productId));
                    }

                } else {
                    MToast.showToast(tag);
                    recyclerView.smoothScrollToPosition(position);
                }
                break;
        }
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        MToastUtils.showToast("商品上传成功！");
        setResult(RESULT_OK);
        finish();
    }


    //为空的规格属性的名称
    String tag = "";
    //为空属性所在的条目在列表中的位置
    int position = 0;

    //检查所填各规格是否符合要求
    public boolean checkFormat() {
        for (int i = guigeModelList.size() - 1; i > -1; i--) {//倒序遍历
            if (!(tag = guigeModelList.get(i).checkFormat()).equals("ok")) {//规格model内的各项属性是否符合
                position = i;
                return false;
            }
            for (int j = i - 1; j > -1; j--) {//检查规格或颜色的名称是否有重复的情况
                if (guigeModelList.get(i).isShowGuige()) {
                    if (guigeModelList.get(i).getGuige().equals(guigeModelList.get(j).getGuige())) {
                        tag = "产品规格不能重复";
                        position = j;
                        return false;
                    }
                } else {
//                    if (guigeModelList.get(i).getColor().equals(guigeModelList.get(j).getColor())) {
//                        tag = "产品颜色不能重复";
//                        position = j;
//                        return false;
//                    }
                    return true;
                }
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.e("234", "onBackPressed: " + isEdit);
        if (!isEdit) {
            setResult(RESULT_CANCELED);
            if (guigeModelList.size() > 0) {
                if (!isEdit) {
                    ShangPinShangChuanActivity.guigeModels = guigeModelList;
                }
            }
        }
    }

}

