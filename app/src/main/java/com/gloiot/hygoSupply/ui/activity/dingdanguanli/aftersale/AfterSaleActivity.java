package com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.gloiot.chatsdk.utlis.Constant;
import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.bean.DingdanGuanliShangpinBean;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.JuJueShenQingActivity;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.TongYiTuiHuoActivity;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale.attacher.BuyerAttcher;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale.attacher.ExpressAttcher;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale.attacher.NoticeAttcher;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale.attacher.ProductAttcher;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale.attacher.ProgressAttcher;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale.attacher.RefuseAttcher;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale.attacher.ResultAttcher;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale.attacher.StatusAttcher;
import com.gloiot.hygoSupply.ui.activity.dingdanguanli.aftersale.model.AfterSaleModel;
import com.gloiot.hygoSupply.ui.activity.message.ConversationActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.utlis.DialogUtlis;
import com.gloiot.hygoSupply.utlis.MToastUtils;
import com.zyd.wlwsdk.utlis.NoDoubleClickUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

public class AfterSaleActivity extends BaseActivity implements AttachView {
    @Bind(R.id.btn_after_sale_refuse)
    Button btnAfterSaleRefuse;
    @Bind(R.id.btn_after_sale_contact)
    Button btnAfterSaleContact;
    @Bind(R.id.btn_after_sale_agree)
    Button btnAfterSaleAgree;
    private AfterSaleModel afterSaleModel;//售后model
    private LinearLayout parentView;//界面附着的父view
    private String id;
    private String orderId;
    private String afterSaleType;
    private String account;
    private String productId;
    private String status;
    private String nickName;

    @Override
    public int initResource() {
        return R.layout.activity_after_sale;
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar(this, true, "申请详情", "");
        id = getIntent().getStringExtra("id");
        orderId = getIntent().getStringExtra("orderId");
        afterSaleType = getIntent().getStringExtra("afterSaleType");
        account = getIntent().getStringExtra("account");
        productId = getIntent().getStringExtra("productId");
        status = getIntent().getStringExtra("status");
        nickName = getIntent().getStringExtra("nickName");
        parentView = (LinearLayout) findViewById(R.id.activity_after_sale);
    }

    @Override
    public void onResume() {
        super.onResume();
        if ("仅退款".equals(afterSaleType)) {
            requestHandleArrayList.add(requestAction.shop_wl_rf_details(this, phone, id, orderId));
        } else if ("退款退货".equals(afterSaleType)) {
            requestHandleArrayList.add(requestAction.shop_wl_rf_goodsDetail(this, phone, id, orderId));
        } else {
            Log.e(TAG, "initData: " + afterSaleType);
        }
    }

    @Override
    public void attach(ViewGroup parent) {

    }


    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_wl_rf_goodsDetail:
            case ConstantUtils.TAG_shop_wl_rf_details:
                getData(response);
                break;
            case ConstantUtils.TAG_Manual_wl_refund:
                MToastUtils.showToast("已同意退款申请");
                parentView.removeAllViews();
                requestHandleArrayList.add(requestAction.shop_wl_rf_details(this, phone, id, orderId));
                break;
            case ConstantUtils.TAG_shop_wl_tyth:
                MToastUtils.showToast("已同意退货申请");
                parentView.removeAllViews();
                requestHandleArrayList.add(requestAction.shop_wl_rf_goodsDetail(this, phone, id, orderId));
                break;
            case ConstantUtils.TAG_Return_wl_goods:
                MToastUtils.showToast("退款成功");
                parentView.removeAllViews();
                requestHandleArrayList.add(requestAction.shop_wl_rf_goodsDetail(this, phone, id, orderId));
                break;
        }
    }

    public void getData(JSONObject response) throws JSONException {


//        { '状态': '成功',
//                0|apibus   |   '须知': '1.如您同意申请，将直接退款给买家。/n2.如您拒绝申请，买家可以要求官方介入协助处理，如核实是您的责任，将会影响您店铺的信誉度。/n3.如逾期未处理，买家可以申请官方介入协助处理。/n4.买家发货后，请在7天内确认收货，如逾期未处理，买家有权申请客服介入协助处理。',
//                0|apibus   |   title: '请尽快处理申请\n还剩2天22时11分',
//                0|apibus   |   '收货人': '魏芸',
//                0|apibus   |   '退款说明': '买的耳机不能用，打开蓝牙之后收不到，我用苹果手机和安卓手机都试了，现要求退货',
//                0|apibus   |   '收货地址': '浙江省金华市兰溪市\r\n黄大仙路99-1号嘉宝·桃源欣城1栋4单元201室',
//                0|apibus   |   '收货人手机号': '15171584881',
//                0|apibus   |   shop:
//            0|apibus   |    [ { '商品名称': 'A921BL 运动式蓝牙耳机',
//                0|apibus   |        '商品规格': '规格:玫瑰金',
//                0|apibus   |        '商品数量': '1.00',
//                0|apibus   |        '缩略图': 'http://qqwlw.oss-cn-shenzhen.aliyuncs.com/sj-test/C3F01ABF-145A-4A3E-A382-0379BEEAE67B3622513622.jpg' } ],
//            0|apibus   |   '凭证': [ { 'imgUrl': 'http://qqwlw.oss-cn-shenzhen.aliyuncs.com/qqwlw/qqwlw2017102718501021.jpg' } ],
//            0|apibus   |   '支付方式': '支付宝',
//                0|apibus   |   '物流状态': null,
//                0|apibus   |   '录入时间': '2017-11-28 16:04:01',
//                0|apibus   |   '店铺名': '老张的店铺',
//                0|apibus   |   '退款金额': '128.00',
//                0|apibus   |   '退款类型': '退款退货',
//                0|apibus   |   '售后状态': '请处理',
//                0|apibus   |   '退款原因': '商品质量问题',
//                0|apibus   |   '订单id': '13207228088_20171025074602840' }


        parentView.removeAllViews();
        afterSaleModel = new AfterSaleModel();
        afterSaleModel.setId(id);
        afterSaleModel.setProductId(productId);
        afterSaleModel.setStatus(response.getString("title"));
        afterSaleModel.setBuyerInfo(response.getString("收货人") + "    " + response.getString("收货人手机号"));
        afterSaleModel.setBuyerAddress("收货地址:" + response.getString("收货地址"));
        afterSaleModel.setAfterSaleStatus(response.getString("售后状态"));
        JSONArray shop = response.getJSONArray("shop");
        if (shop.length() > 0) {
            JSONObject product = shop.getJSONObject(0);
            afterSaleModel.setName(product.getString("商品名称"));
            afterSaleModel.setNum("×" + product.getString("商品数量"));
            afterSaleModel.setSize(product.getString("商品规格"));
            afterSaleModel.setProductImg(product.getString("缩略图"));
        }
        afterSaleModel.setReason("退款原因:    " + response.getString("退款原因"));
        afterSaleModel.setMoney(response.getString("退款金额"));
        afterSaleModel.setTime("申请退款时间:    " + response.getString("录入时间"));
        afterSaleModel.setOrderId(response.getString("订单id"));
        afterSaleModel.setExplain(response.getString("退款说明"));
        afterSaleModel.setNotice(response.getString("须知").replaceAll("\\/n", "\n"));
        afterSaleModel.setAfterSaleType(response.getString("退款类型"));
        try {
            afterSaleModel.setResultTime(response.getString("resultTime"));
        } catch (Exception e) {
        }

        Log.e(TAG, "getData: " + afterSaleModel.getAfterSaleStatus());
        try {
            afterSaleModel.setRefuseReason(response.getString("拒绝原因"));
            afterSaleModel.setRefuseExplain(response.getString("拒绝说明"));
        } catch (Exception e) {

        }
        try {
            afterSaleModel.setExpressCompany(response.getString("快递公司"));
            afterSaleModel.setExpressId(response.getString("快递单号"));
            afterSaleModel.setExpressTime(response.getString("发货时间"));
            afterSaleModel.setExpressStatus(response.getString("售后物流状态"));
        } catch (Exception e) {

        }
        try {
            JSONArray record = response.getJSONArray("record");
            if (record.length() > 0) {
                for (int i = 0; i < record.length(); i++) {
                    String[] info = new String[3];
                    JSONObject object = record.getJSONObject(i);
                    info[0] = object.getString("状态");
                    try {
                        String time = object.getString("时间");
                        int position = time.indexOf(" ");
                        info[1] = time.substring(0, position);
                        info[2] = time.substring(position + 1, time.length());
                    } catch (Exception e) {

                    }

                    afterSaleModel.getProgressInfo().add(info);
                }
            }
        } catch (Exception e) {

        }

        JSONArray voucher = response.getJSONArray("凭证");
        if (voucher.length() > 0) {
            for (int i = 0; i < voucher.length(); i++) {
                JSONObject voucherObj = voucher.getJSONObject(i);
                afterSaleModel.getVoucherImages().add(voucherObj.getString("imgUrl"));
            }
        }
        attachView();
        if ("请退款".equals(afterSaleModel.getAfterSaleStatus()) || "请处理".equals(afterSaleModel.getAfterSaleStatus()) || afterSaleModel.getStatus().contains("待确认收货")) {
            btnAfterSaleAgree.setVisibility(View.VISIBLE);
            btnAfterSaleRefuse.setVisibility(View.VISIBLE);
        } else {
            btnAfterSaleAgree.setVisibility(View.GONE);
            btnAfterSaleRefuse.setVisibility(View.GONE);
        }
//        afterSaleModel.getVoucherImages().add("http://qqwlw.oss-cn-shenzhen.aliyuncs.com/manage/2017080417315769209.jpg");
//        afterSaleModel.getVoucherImages().add("http://qqwlw.oss-cn-shenzhen.aliyuncs.com/manage/2017080417315769209.jpg");
//        afterSaleModel.getVoucherImages().add("http://qqwlw.oss-cn-shenzhen.aliyuncs.com/manage/2017080417315769209.jpg");
//        afterSaleModel.setStatus("今天天气不错\n出去玩啊出去玩啊");
//        afterSaleModel.setBuyerInfo("萨达     135303392665");
//        afterSaleModel.setBuyerAddress("收货地址：广东深圳市罗湖区发展中心大厦1545楼 广东深圳市罗湖区发展中心大厦1545楼 广东深圳市罗湖区发展中心大厦1545楼 广东深圳市罗湖区发展中心大厦1545楼");
//        afterSaleModel.setName("我是一个商品");
//        afterSaleModel.setNum("×3");
//        afterSaleModel.setSize("规格：白色");
//        afterSaleModel.setReason("退款原因:    无理由，不想要了");
//        afterSaleModel.setMoney("退款金额:    ￥500.00");
//        afterSaleModel.setTime("申请退款时间:    2017-05-05 17:10");
//        afterSaleModel.setOrderId("订单编号:    13530390365");
//        afterSaleModel.setExplain("不好用，无理由阿斯达大大大大所大所按时打算打算的撒奥奥奥奥奥奥奥奥奥奥奥奥");
//        afterSaleModel.setNotice("1.如您同意申请，将直接退款给买家。\n2.如您拒绝申请，买家可以要求官方介入协助处理，如核实是您的责任，将会影响您店铺的信誉度。\n3.如逾期未处理，买家可以申请官方介入协助处理。4.买家发货后，请在7天内确认收货，如逾期未处理，买家有权申请客服介入协助处理。");
//        afterSaleModel.setProductImg("http://qqwlw.oss-cn-shenzhen.aliyuncs.com/manage/2017080417315769209.jpg");
//        afterSaleModel.getVoucherImages().add("http://qqwlw.oss-cn-shenzhen.aliyuncs.com/manage/2017080417315769209.jpg");
//        afterSaleModel.getVoucherImages().add("http://qqwlw.oss-cn-shenzhen.aliyuncs.com/manage/2017080417315769209.jpg");
//        afterSaleModel.getVoucherImages().add("http://qqwlw.oss-cn-shenzhen.aliyuncs.com/manage/2017080417315769209.jpg");
//        attachView();
    }


    public void attachView() {
        AttachView attacher = new Attacher(this, this);
        attacher = new StatusAttcher(attacher, this, afterSaleModel);
        attacher = new ProgressAttcher(attacher, this, afterSaleModel);
        attacher = new BuyerAttcher(attacher, this, afterSaleModel);
        attacher = new ProductAttcher(attacher, this, afterSaleModel);
        attacher = new NoticeAttcher(attacher, this, afterSaleModel);
        attacher = new ExpressAttcher(attacher, this, afterSaleModel);
        attacher = new RefuseAttcher(attacher, this, afterSaleModel);
        attacher = new ResultAttcher(attacher, this, afterSaleModel);
        attacher.attach(parentView);
    }

    @OnClick({R.id.btn_after_sale_refuse, R.id.btn_after_sale_contact, R.id.btn_after_sale_agree})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_after_sale_refuse:
                if (afterSaleModel != null) {
                    Intent intent = new Intent(AfterSaleActivity.this, JuJueShenQingActivity.class);
                    intent.putExtra("type", afterSaleModel.getAfterSaleType());
                    intent.putExtra("dingdanID", orderId);
                    intent.putExtra("id", id);
                    startActivityForResult(intent, 1);
                }
                break;
            case R.id.btn_after_sale_contact:
                if (afterSaleModel != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("topType", Constant.CHAT_TOP_TYPE);// 是否有悬浮，""代表没有悬浮，悬浮类别:shangpin = 商品
                    bundle.putString("id", productId);
                    bundle.putString("icon", afterSaleModel.getProductImg());
                    bundle.putString("title", afterSaleModel.getName());
                    bundle.putString("money", afterSaleModel.getMoney());
                    bundle.putBoolean("single", true); // 订单包含两种商品及以上值为false
                    bundle.putString("extra", "");
                    bundle.putString("orderId", orderId);
                    bundle.putString("orderState", status);
                    Intent conversation = new Intent(AfterSaleActivity.this, ConversationActivity.class);
                    conversation.putExtra("receiveId", account);
                    conversation.putExtra("name", nickName);
                    conversation.putExtra("data", bundle);
                    startActivity(conversation);
                }
                break;
            case R.id.btn_after_sale_agree:
                if (afterSaleModel != null) {
                    if ("仅退款".equals(afterSaleType)) {
                        DialogUtlis.twoBtnNormal(mContext, "是否同意退款申请", "提示", false, "取消", "确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DialogUtlis.dismissDialog();
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (NoDoubleClickUtils.isDoubleClick()) return; // 防止连续点击
                                requestHandleArrayList.add(requestAction.Manual_wl_refund(AfterSaleActivity.this, phone, account, orderId, "", id));
                                DialogUtlis.dismissDialog();
                            }
                        });
                    } else if (afterSaleModel.getStatus().contains("待确认收货")) {
                        //确认退款 最后一步
                        DialogUtlis.twoBtnNormal(mContext, "一经确认,系统将退还款项给买家", "确认收货", false, "取消", "确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DialogUtlis.dismissDialog();

                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DialogUtlis.dismissDialog();
                                requestHandleArrayList.add(requestAction.Return_wl_goods(AfterSaleActivity.this, phone, account, orderId, "", id));

                            }
                        });
                    } else if ("退款退货".equals(afterSaleType)) {
                        DialogUtlis.twoBtnNormal(mContext, "是否确认退货", "提示", false, "取消", "确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DialogUtlis.dismissDialog();
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (NoDoubleClickUtils.isDoubleClick()) return; // 防止连续点击
                                requestHandleArrayList.add(requestAction.shop_wl_tyth(AfterSaleActivity.this, phone, orderId, account, id));
                                DialogUtlis.dismissDialog();
                            }
                        });
                    }
                }
        }
    }
}
