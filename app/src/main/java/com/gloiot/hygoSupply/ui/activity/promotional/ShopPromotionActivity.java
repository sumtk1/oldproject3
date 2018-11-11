package com.gloiot.hygoSupply.ui.activity.promotional;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.ui.activity.promotional.factory.PasterFactory;
import com.gloiot.hygoSupply.ui.activity.promotional.factory.ShopPromotionalFactory;
import com.gloiot.hygoSupply.ui.activity.promotional.model.PromotionalModel;
import com.gloiot.hygoSupply.ui.activity.promotional.model.ShopPromotionModel;
import com.gloiot.hygoSupply.ui.activity.promotional.paster.Paster;
import com.gloiot.hygoSupply.ui.activity.promotional.paster.PasterImpl;
import com.gloiot.hygoSupply.ui.activity.promotional.paster.ProductPaster;
import com.gloiot.hygoSupply.ui.activity.promotional.paster.UpdataPage;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.utlis.DialogUtlis;
import com.gloiot.hygoSupply.utlis.MToastUtils;
import com.zyd.wlwsdk.server.network.HttpManager;
import com.zyd.wlwsdk.utlis.JSONUtlis;
import com.zyd.wlwsdk.utlis.MD5Utlis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShopPromotionActivity extends BaseActivity implements UpdataPage, Paster {


    @Bind(R.id.ll_promotional_parentview)
    LinearLayout llPromotionalParentview;
    @Bind(R.id.tv_shop_promotion_totalPrice)
    TextView tvShopPromotionTotalPrice;


    private PasterFactory pasterFactory;
    private ShopPromotionModel promotionalModel;
    private Paster paster;
    private Map<String, Double> priceInfo;


    @Override
    public int initResource() {
        return R.layout.activity_shop_promotion;
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBarWithRightClick(this, true, "商品推广", "推广记录", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShopPromotionActivity.this, ShopPromotionRecordActivity.class));
            }
        });
        pasterFactory = new ShopPromotionalFactory();
        priceInfo = new HashMap<>();
        promotionalModel = new ShopPromotionModel();
        promotionalModel.setPriceInfo(priceInfo);
        promotionalModel.setUpdataPage(this);
        paster = new PasterImpl(this, this);
        paster = pasterFactory.create(this, paster, promotionalModel);
        if (paster != null) {
            paster.past(llPromotionalParentview);
        }

        requestHandleArrayList.add(requestAction.shop_wl_extension(ShopPromotionActivity.this, phone));
    }

    @Override
    public void updataPage(String tag) {
        switch (tag) {
            case "":
                if (null != paster) {
                    llPromotionalParentview.removeAllViews();
                    paster.past(llPromotionalParentview);
                }
                tvShopPromotionTotalPrice.setText(promotionalModel.getTotal() + "");
                break;
            case "onlyTotal":
                tvShopPromotionTotalPrice.setText(promotionalModel.getTotal() + "");
                break;
        }

    }

    @OnClick(R.id.tv_shop_promotion_action)
    public void onViewClicked() {

        if (check()) {
            DialogUtlis.oneBtnPwd(mContext, promotionalModel.getTotal() + "", new DialogUtlis.PasswordCallback() {
                @Override
                public void callback(String data) {
                    requestHandleArrayList.add(requestAction.shop_wl_extension_order(ShopPromotionActivity.this, phone, promotionalModel, CommonUtils.Md5(data)));
                }
            });
        }
    }

    public boolean check() {
        if (TextUtils.isEmpty(promotionalModel.getProcuctId())) {
            MToastUtils.showToast("请选择商品");
            return false;
        } else if (TextUtils.isEmpty(promotionalModel.getType())) {
            MToastUtils.showToast("请选择推广类别");
            return false;
        } else if (TextUtils.isEmpty(promotionalModel.getSecondType())) {
            MToastUtils.showToast("请选择" + promotionalModel.getType() + "位置");
            return false;
        } else if (promotionalModel.getTime() <= 0) {
            MToastUtils.showToast("请输入天数");
            return false;
        } else if (promotionalModel.getTime() == 0) {
            MToastUtils.showToast("天数不能为零");
            return false;
        } else if (promotionalModel.getTime() > 7) {
            MToastUtils.showToast("天数不能超过七天");
            return false;
        } else if (promotionalModel.getTotal() > promotionalModel.getEarnings()) {
            MToastUtils.showToast("账户余额不足");
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void past(ViewGroup parent) {

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
                    updataPage("");
                }
                break;
        }
    }


    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_wl_extension:
                JSONArray list = response.getJSONArray("列表");
                String[] type1 = new String[list.length()];
                for (int i = 0; i < list.length(); i++) {
                    JSONObject object = list.getJSONObject(i);
                    type1[i] = object.getString("页数");
                    priceInfo.put(object.getString("页数"), Double.parseDouble(object.getString("价格")));
                }
                JSONArray picture = response.getJSONArray("轮播");
                String[] type2 = new String[picture.length()];
                for (int i = 0; i < picture.length(); i++) {
                    JSONObject object = picture.getJSONObject(i);
                    type2[i] = object.getString("页数");
                    priceInfo.put(object.getString("页数"), Double.parseDouble(object.getString("价格")));
                }
                promotionalModel.setPriceInfo(priceInfo);
                promotionalModel.setType1(type1);
                promotionalModel.setType2(type2);
                promotionalModel.setRemark(response.getString("备注").replaceAll("\\/n", "\n"));
                promotionalModel.setEarnings(Double.parseDouble(response.getString("商家收益")));
                updataPage("");
                break;
            case ConstantUtils.TAG_shop_wl_extension_order:
                MToastUtils.showToast("推广成功");
                finish();
                break;
        }
    }
}
