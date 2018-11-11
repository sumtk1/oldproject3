package com.gloiot.hygoSupply.ui.activity.dianpuguanli;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.bean.MiaoShuBean;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.ui.activity.evaluation.UserEvaluationActivity;
import com.gloiot.hygoSupply.ui.activity.evaluation.UserReplyAdapter;
import com.gloiot.hygoSupply.ui.activity.evaluation.model.EvaluationModel;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class ShangpinDianpuActivity extends BaseActivity {
    @Bind(R.id.iv_shangpin_dianpyu_tupian)
    ImageView iv_shangpin_dianpyu_tupian;
    @Bind(R.id.tv_shangpin_dianpyu_mingcheng)
    TextView tv_shangpin_dianpyu_mingcheng;
    @Bind(R.id.tv_shangpin_dianpyu_jiage)
    TextView tv_shangpin_dianpyu_jiage;
    @Bind(R.id.tv_shangpin_dianpyu_kuaidi)
    TextView tv_shangpin_dianpyu_kuaidi;
    @Bind(R.id.tv_shangpin_dianpyu_yuexiao)
    TextView tv_shangpin_dianpyu_yuexiao;
    @Bind(R.id.tv_shangpin_dianpyu_dizhi)
    TextView tv_shangpin_dianpyu_dizhi;
    @Bind(R.id.rl_shangpin_dianbu_neirong)
    RelativeLayout rl_shangpin_dianbu_neirong;
    @Bind(R.id.iv_shangpin_dianpyu_quanqiugou)
    ImageView ivShangpinDianpyuQuanqiugou;
    @Bind(R.id.tv_shangpin_xiangqing_commentSum)
    TextView tvShangpinXiangqingCommentSum;
    @Bind(R.id.rv_shangpin_xiangiqng_pinglun)
    RecyclerView rvShangpinXiangiqngPinglun;
    @Bind(R.id.rl_shangpin_xiangqing_pingjia)
    RelativeLayout rlShangpinXiangqingPingjia;
    @Bind(R.id.tv_shangpin_xinxi_pingjiatxt)
    TextView tvShangpinXinxiPingjiatxt;
    @Bind(R.id.tv_shangpin_dianpyu_jianyijia)
    TextView tvShangpinDianpyuJianyijia;

    private String phone;
    private List<MiaoShuBean> allneirong = new ArrayList<>();   //内容数据
    private WebView neiRongWebView;
    private List<EvaluationModel> evaluationModels;
    private UserReplyAdapter evaluationAdapter;
    private String productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initResource() {
        return R.layout.activity_shangpin_dianpu;
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar((Activity) mContext, true, "商品", "");
        Intent intent = getIntent();
        phone = preferences.getString(ConstantUtils.SP_MYID, "");
        productId = intent.getStringExtra("id");
        requestHandleArrayList.add(requestAction.shop_sj_spxq(ShangpinDianpuActivity.this, phone, intent.getStringExtra("id")));
        neiRongWebView = new WebView(this);
        rl_shangpin_dianbu_neirong.addView(neiRongWebView);
        evaluationModels = new ArrayList<>();
        evaluationAdapter = new UserReplyAdapter(mContext, evaluationModels);
        rvShangpinXiangiqngPinglun.setLayoutManager(new LinearLayoutManager(this));
        rvShangpinXiangiqngPinglun.setAdapter(evaluationAdapter);
        rlShangpinXiangqingPingjia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("暂无评论".equals(tvShangpinXiangqingCommentSum.getText().toString())) {
                    return;
                }
                Intent intent = new Intent(ShangpinDianpuActivity.this, UserEvaluationActivity.class);
                intent.putExtra("productId", productId);
                startActivity(intent);
            }
        });
    }


    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_sj_spxq:
                Log.e("查询店铺商品详情回调", response.toString());
                String yuexiao = response.getString("月销");
                String suoluetu = response.getString("缩略图");
                String dizhi = response.getString("地址");
                String shangpinmincheng = response.getString("商品名称");
//                String shangpintupian = response.getString("商品图片");
                String kuaidifei = response.getString("快递费");
                if ("全球购".equals(response.getString("类型")) || "全球购-自营".equals(response.getString("类型"))) {
                    ivShangpinDianpyuQuanqiugou.setVisibility(View.VISIBLE);
                    String jiesuanjia = response.getString("价格");
                    tv_shangpin_dianpyu_jiage.setText("￥" + jiesuanjia);
                    tvShangpinDianpyuJianyijia.setVisibility(View.GONE);
                } else {
                    ivShangpinDianpyuQuanqiugou.setVisibility(View.GONE);
                    String jiesuanjia = response.getString("价格");
                    tv_shangpin_dianpyu_jiage.setText("￥" + jiesuanjia);
                    tvShangpinDianpyuJianyijia.setVisibility(View.VISIBLE);
                    tvShangpinDianpyuJianyijia.setText(response.getString("建议零售价"));
                    tvShangpinDianpyuJianyijia.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中间横线
                }
                CommonUtils.setDisplayImageOptions(iv_shangpin_dianpyu_tupian, suoluetu, 4);
                tv_shangpin_dianpyu_mingcheng.setText(shangpinmincheng);

                tv_shangpin_dianpyu_kuaidi.setText("快递:" + kuaidifei);
                tv_shangpin_dianpyu_yuexiao.setText("月销:" + yuexiao);
                tv_shangpin_dianpyu_dizhi.setText(dizhi);
                if ("0".equals(response.getString("commentSum"))) {
                    tvShangpinXiangqingCommentSum.setText("暂无评论");
                    tvShangpinXinxiPingjiatxt.setVisibility(View.GONE);
                } else {
                    tvShangpinXiangqingCommentSum.setText("(" + response.getString("commentSum") + ")");
                    tvShangpinXinxiPingjiatxt.setVisibility(View.VISIBLE);
                }

                JSONArray comment = response.getJSONArray("comment");
                if (comment.length() > 0) {
                    for (int i = 0; i < comment.length(); i++) {
                        JSONObject commentObj = comment.getJSONObject(i);
                        EvaluationModel model = new EvaluationModel();
                        model.setName(commentObj.getString("昵称"));
                        model.setImageUrl(commentObj.getString("头像"));
                        model.setContent(commentObj.getString("评论"));
                        model.setTime(commentObj.getString("录入时间"));
                        model.setId(productId);
                        JSONArray images = commentObj.getJSONArray("图片");
                        if (images.length() > 0) {
                            for (int j = 0; j < images.length(); j++) {
                                JSONObject image = images.getJSONObject(j);
                                model.getEvluationImages().add(image.getString("imgUrl"));
                            }
                        }
                        evaluationModels.add(model);
                    }
                    evaluationAdapter.notifyDataSetChanged();
                }


                JSONArray jsonArray = response.getJSONArray("内容");
                for (int j = 0; j < jsonArray.length(); j++) {
                    response = (JSONObject) jsonArray.get(j);
                    JSONArray pictureList = response.getJSONArray("图片列表");
                    String miaoshu = response.getString("描述");
                    MiaoShuBean bean = new MiaoShuBean(miaoshu);
                    for (int x = 0; x < pictureList.length(); x++) {
                        JSONObject picture = (JSONObject) pictureList.get(x);
                        String tupian = picture.getString("图片");
                        bean.getPictures().add(tupian);
                    }
                    allneirong.add(bean);
                }
                convertHtml();
                break;
        }
    }


    private void convertHtml() {
/*        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels - 45;
        float density = metrics.density;
        String imageWidth = (width/density + 0.5)+";}";*/
        StringBuilder content = new StringBuilder("<style type=\"text/css\" > img {width:100%}#func{margin:0}</style><div id=\"func\">");
        String blockLabelBegin = "<p>";
        String blockLabelEnd = "</p>";

        String imgLabelBegin = "<img src=\"";
        String imgLabelEnd = "\"/>";
        for (MiaoShuBean miaoShuBean : allneirong) {
            //显示标题行
            if (!TextUtils.isEmpty(miaoShuBean.getMiaoshu())) {
                content.append(blockLabelBegin + miaoShuBean.getMiaoshu() + blockLabelEnd);
            }
            for (int i = 0; i < miaoShuBean.getPictures().size(); i++) {
                //显示图片
                if (!TextUtils.isEmpty(miaoShuBean.getPictures().get(i))) {
                    content.append(blockLabelBegin + imgLabelBegin + miaoShuBean.getPictures().get(i) + imgLabelEnd + blockLabelEnd);
                }
            }

        }
        content.append("</div>");
        neiRongWebView.getSettings().setJavaScriptEnabled(true);
        neiRongWebView.setWebChromeClient(new WebChromeClient());
        WebSettings settings = neiRongWebView.getSettings();
        //自适应屏幕，但是图片过小时不能满屏显示图片，暂时注释
//        settings.setUseWideViewPort(true);
//        settings.setLoadWithOverviewMode(true);
        neiRongWebView.loadDataWithBaseURL(null, content.toString(), "text/html", "UTF-8", null);
    }

}
