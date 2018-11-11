package com.gloiot.hygoSupply.ui.activity.dingdanguanli;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gloiot.chatsdk.DataBase.IMDBManager;
import com.gloiot.chatsdk.bean.UserInfo;
import com.gloiot.chatsdk.chatui.UserInfoCache;
import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.bean.DingdanGuanliBean;
import com.gloiot.hygoSupply.bean.DingdanGuanliShangpinBean;
import com.gloiot.hygoSupply.databinding.ActivityQuerenFahuoBinding;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.ui.activity.message.ConversationActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TuikuanxiangqingActivity extends BaseActivity implements View.OnClickListener {
    ActivityQuerenFahuoBinding binding;
    @Bind(R.id.tv_tkxq_biaoti)
    TextView tv_tkxq_biaoti; //商品标题
    @Bind(R.id.tv_tkxq_jine)
    TextView tv_tkxq_jine; //商品金额¥
    @Bind(R.id.tv_tkxq_color)
    TextView tv_tkxq_color; //颜色分类:白色
    @Bind(R.id.tv_tkxq_shuliang)
    TextView tv_tkxq_shuliang; //商品数量
    @Bind(R.id.tv_tkxq_yuanyin)
    TextView tv_tkxq_yuanyin; //退款原因
    @Bind(R.id.tv_tkxq_tuihuojine)
    TextView tv_tkxq_tuihuojine; //退货金额
    @Bind(R.id.tv_tkxq_tuikuanshuoming)
    TextView tv_tkxq_tuikuanshuoming; //退货说明
    @Bind(R.id.tv_tkxq_shenqingshijian)
    TextView tv_tkxq_shenqingshijian; //申请时间
    @Bind(R.id.iv_tkxq_sptupian)
    ImageView iv_tkxq_sptupian; //商品图片
    @Bind(R.id.iv_tuikuan_success)
    ImageView ivTuikuanSuccess;
    @Bind(R.id.iv_tuikuan_close)
    ImageView ivTuikuanClose;
    @Bind(R.id.tv_tuikuan_status)
    TextView tvTuikuanStatus;
    private String dingdanid, shangpingid;
    private String name;
    private String url;
    private String id;

    @Override
    public int initResource() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tuikuanxiangqing);
        return 0;
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar((Activity) mContext, true, "退款详情", "");
        Intent intent = getIntent();
        DingdanGuanliBean mDingdanGuanliShangpinBean = (DingdanGuanliBean) intent.getSerializableExtra("item");
        DingdanGuanliShangpinBean Dingdanguanli = mDingdanGuanliShangpinBean.get订单管理商品集合().get(0);
        CommonUtils.setDisplayImageOptions(iv_tkxq_sptupian, Dingdanguanli.get缩略图(), 0);
        dingdanid = mDingdanGuanliShangpinBean.get订单id();
        shangpingid = Dingdanguanli.get商品id();
        id = Dingdanguanli.getId();
        tv_tkxq_biaoti.setText(Dingdanguanli.get商品名称());
        tv_tkxq_jine.setText("¥" + Dingdanguanli.get价格());
        tv_tkxq_color.setText(Dingdanguanli.get种类详细());
        tv_tkxq_shuliang.setText("x" + Dingdanguanli.get商品数量());
        if (!TextUtils.isEmpty(mDingdanGuanliShangpinBean.getShouhou_status())) {
            switch (mDingdanGuanliShangpinBean.getShouhou_status()) {
                case "商家未处理":
                    tvTuikuanStatus.setText("超时未处理");
                    ivTuikuanClose.setVisibility(View.VISIBLE);
                    ivTuikuanSuccess.setVisibility(View.GONE);
                    break;
                case "商家拒绝退款申请":
                case "退款关闭":
                case "商家拒绝退货申请":
                    tvTuikuanStatus.setText("退款关闭");
                    ivTuikuanClose.setVisibility(View.VISIBLE);
                    ivTuikuanSuccess.setVisibility(View.GONE);
                    break;
                case "商家已同意退款":
                    ivTuikuanClose.setVisibility(View.GONE);
                    ivTuikuanSuccess.setVisibility(View.VISIBLE);
                    tvTuikuanStatus.setText("退款成功");
                    break;
                default:
                    ivTuikuanClose.setVisibility(View.GONE);
                    ivTuikuanSuccess.setVisibility(View.GONE);
                    tvTuikuanStatus.setText(mDingdanGuanliShangpinBean.getShouhou_status());
            }
        }
        requestHandleArrayList.add(requestAction.shop_wl_SuccessRefund(TuikuanxiangqingActivity.this, preferences.getString(ConstantUtils.SP_MYID, ""), dingdanid, shangpingid, id));
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_wl_SuccessRefund:
                tv_tkxq_yuanyin.setText("退款原因: " + response.getString("退款原因"));
                tv_tkxq_tuihuojine.setText("退货金额: " + response.getString("退款金额"));
                tv_tkxq_tuikuanshuoming.setText("退货说明: " + response.getString("退款说明"));
                tv_tkxq_shenqingshijian.setText("申请时间: " + response.getString("申请时间"));
                id = response.getString("退款账号");
                name = response.getString("退款姓名");
                url = response.getString("头像");
                try {
                    UserInfo bean = new UserInfo();
                    bean.setId(id);
                    bean.setName(name);
                    bean.setUrl(url);
                    UserInfoCache.getInstance(mContext).putData(bean.getId(), bean);//存入缓存
                    IMDBManager.getInstance(mContext).insertUserInfo(bean);//存入数据库
                } catch (Exception e) {

                }

                break;
        }
    }

    @OnClick({R.id.tv_ttxq_lianximaijia, R.id.tv_ttxq_guanfangzixun})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ttxq_lianximaijia://联系买家
                Intent intent = new Intent(new Intent(mContext, ConversationActivity.class));
                intent.putExtra("receiveId", id);
                intent.putExtra("name", name);
                startActivity(intent);
                break;
            case R.id.tv_ttxq_guanfangzixun://官方咨询
                Intent intent1 = new Intent(new Intent(TuikuanxiangqingActivity.this, QingxuanzeKefuActivity.class));
                intent1.putExtra("isAvailable", true);
                startActivity(intent1);
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
