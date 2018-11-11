package com.gloiot.hygoSupply.ui.activity.wode.fadongtai;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.ui.activity.wode.fadongtai.adapter.DynamicProductAdapter;
import com.gloiot.hygoSupply.ui.activity.wode.fadongtai.model.DynamicProductModel;
import com.gloiot.hygoSupply.ui.activity.wode.fadongtai.model.ProductModel;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.utlis.MToastUtils;
import com.zyd.wlwsdk.server.AliOss.ChoosePhoto;
import com.zyd.wlwsdk.utlis.MToast;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 发状态—商品
 */

public class FaShangPingZhuangTaiActivity extends BaseActivity {


    @Bind(R.id.btn_dynamic_product_publish)
    Button btnDynamicProductPublish;
    @Bind(R.id.rv_dynamic_product)
    RecyclerView rvDynamicProduct;
    private DynamicProductModel dynamicProductModel;
    private DynamicProductAdapter adapter;
    private String num = "0", name = "", price = "";

    @Override
    public int initResource() {
        return R.layout.activity_dynamin_product;
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar((Activity) mContext, true, "发布商品", "");
        dynamicProductModel = new DynamicProductModel();
        adapter = new DynamicProductAdapter(this, dynamicProductModel) {

            @Override
            public void setPicture() {
                checkPermission(new CheckPermListener() {
                    @Override
                    public void superPermission() {
                        checkPermission(new CheckPermListener() {
                            @Override
                            public void superPermission() {
                                new ChoosePhoto(mContext) {
                                    @Override
                                    protected void setPicSuccess(final String myImgSrc, final String myPicUrl) {
                                        FaShangPingZhuangTaiActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                dynamicProductModel.setTitleImg(myPicUrl);
                                                adapter.notifyDataSetChanged();
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
        };
        rvDynamicProduct.setLayoutManager(new LinearLayoutManager(this));
        rvDynamicProduct.setAdapter(adapter);

        btnDynamicProductPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = dynamicProductModel.check();
                if ("ok".equals(message)) {
                    //上传商品
                    requestHandleArrayList.add(requestAction.shop_wl_releasedynamic(FaShangPingZhuangTaiActivity.this
                            , preferences.getString(ConstantUtils.SP_ZHANGHAO, ""), preferences.getString(ConstantUtils.SP_ONLYID, ""), null, "商品", "", "", "", dynamicProductModel));
                } else {
                    MToastUtils.showToast(message);
                }
            }
        });
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        MToastUtils.showToast("发布成功");
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult: " + requestCode + resultCode);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    dynamicProductModel.setLink(data.getStringExtra("url"));
                    dynamicProductModel.setId(data.getStringExtra("id"));
                    name = data.getStringExtra("name");
                    price = data.getStringExtra("price");
                    num = data.getStringExtra("num");
                    adapter.notifyDataSetChanged();
                }
                break;
            case 1://单款商品图文
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        ProductModel model = new ProductModel();
                        model.setDescribe(data.getStringExtra("neirong"));
                        model.setImgUrl(data.getStringExtra("zhaopian"));
                        model.setLink(data.getStringExtra("zhaopian"));
                        model.setTitle(name);
                        model.setId(dynamicProductModel.getId());
                        model.setNum(num);
                        model.setPrice(price);
                        dynamicProductModel.getProductModelsSingle().add(model);
                        adapter.notifyDataSetChanged();
                    }
                }
                break;
            case 2://多款商品图文
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        int position;
                        ProductModel model;
                        if ((position = data.getIntExtra("position", -1)) > -1) {
                            model = dynamicProductModel.getProductModelsMulit().get(position);
                            Log.e(TAG, "onViewClicked: "+position );
                        } else {
                            model = new ProductModel();
                        }
                        model.setTitle(data.getStringExtra("name"));
                        model.setDescribe(data.getStringExtra("detail"));
                        model.setImgUrl(data.getStringExtra("url"));
                        model.setId(data.getStringExtra("id"));
                        model.setLink(data.getStringExtra("url"));
                        model.setPrice(data.getStringExtra("price"));
                        model.setNum(data.getStringExtra("num"));
                        if (position <0) {
                            dynamicProductModel.getProductModelsMulit().add(model);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
                break;
        }
    }

}
