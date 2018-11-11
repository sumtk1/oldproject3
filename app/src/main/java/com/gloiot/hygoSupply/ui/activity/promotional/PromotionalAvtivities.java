package com.gloiot.hygoSupply.ui.activity.promotional;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.ui.activity.promotional.factory.PasterFactory;
import com.gloiot.hygoSupply.ui.activity.promotional.factory.PointPromotionalFactory;
import com.gloiot.hygoSupply.ui.activity.promotional.model.PromotionalModel;
import com.gloiot.hygoSupply.ui.activity.promotional.paster.Paster;
import com.gloiot.hygoSupply.ui.activity.promotional.paster.PasterImpl;
import com.gloiot.hygoSupply.ui.activity.promotional.paster.ProductPaster;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.MToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;

/**
 * 作者：Ljy on 2017/10/30.
 * 功能：我的——我的资料
 */


public class PromotionalAvtivities extends BaseActivity implements Paster {
    @Bind(R.id.btn_promotional_complete)
    Button btnPromotionalComplete;


    LinearLayout ll_promotional_parentview;
    private PasterFactory pasterFactory;
    private PromotionalModel promotionalModel;
    private Paster paster;
    private String deductionPercent;
    private String activitiesExplain;
    private String isFirst;
    private Intent intent;

    @Override
    public int initResource() {
        return R.layout.activity_promotional_activitys;
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar(this, true, "参与活动", "");
        intent = getIntent();
        ll_promotional_parentview = (LinearLayout) findViewById(R.id.ll_promotional_parentview);
        promotionalModel = new PromotionalModel();
        if ("edit".equals(intent.getStringExtra("type"))) {
            promotionalModel.setProcuctId(intent.getStringExtra("id"));
            promotionalModel.setOldProcuctId(intent.getStringExtra("id"));
            promotionalModel.setChoicedProduct(true);
            promotionalModel.setProductImage(intent.getStringExtra("image"));
            promotionalModel.setProductDescribe(intent.getStringExtra("describe"));
            promotionalModel.setDeductionPercent(intent.getStringExtra("percent"));
            promotionalModel.setOldDeductionPercent(intent.getStringExtra("percent"));
            promotionalModel.setProcuctPrice(intent.getStringExtra("price"));
        }
        deductionPercent = intent.getStringExtra("deductionPercent");
        activitiesExplain = intent.getStringExtra("activitiesExplain");
        promotionalModel.setNormalDeductionPercent(deductionPercent);
        promotionalModel.setPointsExplain(activitiesExplain);
        isFirst = getIntent().getStringExtra("isFirst");
        paster = new PasterImpl(this, this);
        pasterFactory = new PointPromotionalFactory();
        paster = pasterFactory.create(this, paster, promotionalModel);
        if (paster != null) {
            paster.past(ll_promotional_parentview);
        }
        btnPromotionalComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check()) {
                    if ("edit".equals(intent.getStringExtra("type"))) {
                        requestHandleArrayList.add(requestAction.shop_wl_Editorial_activities(PromotionalAvtivities.this, phone, "0", promotionalModel.getOldDeductionPercent(), promotionalModel.getOldProcuctId(), promotionalModel.getDeductionPercent(), promotionalModel.getProcuctId()));
                    } else {
                        requestHandleArrayList.add(requestAction.shop_wl_Create_activity(PromotionalAvtivities.this, phone, promotionalModel.getProcuctId(), promotionalModel.getDeductionPercent()));
                    }

                }
            }
        });
    }

    public boolean check() {
        if (TextUtils.isEmpty(promotionalModel.getProcuctId())) {
            MToastUtils.showToast("请选择参与活动的商品");
            return false;
        } else if (TextUtils.isEmpty(promotionalModel.getDeductionPercent())) {
            MToastUtils.showToast("请输入积分抵扣比例");
            return false;
        } else if (Double.parseDouble(promotionalModel.getDeductionPercent()) > 150) {
            MToastUtils.showToast("积分抵扣比例过大");
            return false;
        }
        return true;
    }


    @Override
    public void past(ViewGroup parent) {

    }

    private void updataPaster() {
        if (null != paster) {
            ll_promotional_parentview.removeAllViews();
            paster.past(ll_promotional_parentview);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult: " + requestCode + resultCode);
        switch (requestCode) {
            case ProductPaster.REQUESTCODE:
                if (resultCode == RESULT_OK) {
                    promotionalModel.setChoicedProduct(true);
                    promotionalModel.setProductImage(data.getStringExtra("url"));
                    promotionalModel.setProcuctId(data.getStringExtra("id"));
                    promotionalModel.setProductDescribe(data.getStringExtra("name"));
                    promotionalModel.setProcuctPrice(data.getStringExtra("price"));
                    updataPaster();
                }
                break;
        }
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        if ("edit".equals(intent.getStringExtra("type"))) {
            MToastUtils.showToast("编辑成功");
        } else {
            MToastUtils.showToast("创建成功");
        }

        if ("yes".equals(isFirst)) {//首次创建活动
            Intent intent = new Intent(PromotionalAvtivities.this, ParticipateActivities.class);
            intent.putExtra("deductionPercent", deductionPercent);
            intent.putExtra("activitiesExplain", activitiesExplain);
            startActivity(intent);
        }
        finish();
    }

}
