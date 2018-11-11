package com.gloiot.hygoSupply.ui.activity.evaluation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.gloiot.chatsdk.chatui.keyboard.XhsEmoticonsKeyBoard;
import com.gloiot.chatsdk.chatui.keyboard.data.EmoticonEntity;
import com.gloiot.chatsdk.chatui.keyboard.emoji.EmojiBean;
import com.gloiot.chatsdk.chatui.keyboard.emoji.SimpleCommonUtils;
import com.gloiot.chatsdk.chatui.keyboard.interfaces.EmoticonClickListener;
import com.gloiot.chatsdk.chatui.keyboard.widget.EmoticonsEditText;
import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.ui.activity.dianpuguanli.ShangpinDianpuActivity;
import com.gloiot.hygoSupply.ui.activity.evaluation.model.EvaluationModel;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.utlis.MToastUtils;
import com.gloiot.hygoSupply.widget.RefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 作者：Ljy on 2017/8/28.
 * 功能：我的——我的资料
 */


public class EvaluationActivity extends BaseActivity implements RefreshLayout.OnLoadListener, SwipeRefreshLayout.OnRefreshListener, EvaluationAdapter.OnItemClickListener {

    private final String WAITREPLY = "待回复";
    private final String REPLIED = "已回复";
    @Bind(R.id.iv_twotitle_back)
    ImageView ivTwotitleBack;
    @Bind(R.id.tv_twotile_1)
    TextView tv_twotile_1;
    @Bind(R.id.tv_twotile_2)
    TextView tv_twotile_2;
    @Bind(R.id.rv_evaluation)
    RecyclerView rvEvaluation;
    @Bind(R.id.tv_twotile_3)
    TextView tv_twotile_3;
    @Bind(R.id.refreshlayout_evaluation)
    RefreshLayout refreshlayoutEvaluation;
    @Bind(R.id.iv_no_evaluation)
    ImageView ivNoEvaluation;


    private String currentType;
    private List<EvaluationModel> evaluationModels;
    private boolean onLoad;
    private EvaluationAdapter evaluationAdapter;
    private int page;
    private int num;
    private XhsEmoticonsKeyBoard ekBar;
    private String replyTypeId = "";
    private boolean isShowKeyBord;
    private int height;

    @Override
    public int initResource() {
        return R.layout.activity_evaluation;
    }

    @Override
    public void initData() {
        WindowManager wm = this.getWindowManager();
        height = wm.getDefaultDisplay().getHeight();
        ekBar = (XhsEmoticonsKeyBoard) findViewById(R.id.evaluation_ek_bar);
        initEmoticonsKeyBoardBar();
        ekBar.getRl_keybord().setVisibility(View.GONE);
        tv_twotile_1.setText("待回复");
        tv_twotile_3.setText("已回复");
        currentType = WAITREPLY;
        tv_twotile_2.setVisibility(View.GONE);
        tv_twotile_1.setTextColor(ContextCompat.getColor(this, R.color.cl_white));
        tv_twotile_3.setTextColor(ContextCompat.getColor(this, R.color.cl_ff7f29));
        evaluationModels = new ArrayList<>();
        evaluationAdapter = new EvaluationAdapter(mContext, evaluationModels);
        evaluationAdapter.setListener(this);
        rvEvaluation.setLayoutManager(new LinearLayoutManager(this));
        rvEvaluation.setAdapter(evaluationAdapter);
        refreshlayoutEvaluation.setOnRefreshListener(this);
        refreshlayoutEvaluation.setOnLoadListener(this);
        requestHandleArrayList.add(requestAction.shop_wl_recomments(this, phone, currentType, "0"));

    }

    @Override
    public void onResume() {
        super.onResume();
        if (isShowKeyBord) {
            hideKeyBord();
        }
    }

    //点击title变色样式
    public void setTitleStyle() {
        if (WAITREPLY.equals(currentType)) {
            tv_twotile_1.setTextColor(ContextCompat.getColor(this, R.color.cl_white));
            tv_twotile_3.setTextColor(ContextCompat.getColor(this, R.color.cl_ff7f29));
            tv_twotile_1.setBackgroundResource(R.drawable.bg_tv_3_shape_selected);
            tv_twotile_3.setBackgroundResource(R.drawable.bg_tv_3_shape_right);
        } else {
            tv_twotile_1.setTextColor(ContextCompat.getColor(this, R.color.cl_ff7f29));
            tv_twotile_3.setTextColor(ContextCompat.getColor(this, R.color.cl_white));
            tv_twotile_1.setBackgroundResource(R.drawable.bg_tv_3_shape);
            tv_twotile_3.setBackgroundResource(R.drawable.bg_tv_3_shape_right_selected);
        }
        onRefresh();
    }

    /**
     * 初始化聊天键盘
     */
    private void initEmoticonsKeyBoardBar() {
        // 初始化输入框
        SimpleCommonUtils.initEmoticonsEditText(ekBar.getEtChat());
        // 设置表情布局adapter
        ekBar.setAdapter(SimpleCommonUtils.getCommonAdapter(emoticonClickListener));
        // 功能布局监听（OnFuncPop功能布局弹起，OnFuncClose功能布局关闭）
        ekBar.getEtChat().setOnSizeChangedListener(new EmoticonsEditText.OnSizeChangedListener() {
            @Override
            public void onSizeChanged(int w, int h, int oldw, int oldh) {

            }
        });

        // 发送按钮
        ekBar.getBtnSend().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reply = ekBar.getEtChat().getText().toString();
                if (TextUtils.isEmpty(reply)) {
                    MToastUtils.showToast("回复内容不能为空");
                } else if (reply.length() > 100) {
                    MToastUtils.showToast("回复内容不能超过100个字");
                } else if (!TextUtils.isEmpty(replyTypeId)) {
                    requestHandleArrayList.add(requestAction.shop_comment_reply(EvaluationActivity.this, phone, replyTypeId, ekBar.getEtChat().getText().toString()));
                }
            }
        });
        ekBar.getEtChat().removeTextChangedListener(ekBar.getWatcher());
        ekBar.getBtnSend().setVisibility(View.VISIBLE);
        ekBar.getBtnMultimedia().setVisibility(View.GONE);
        ekBar.getBtnSend().setBackgroundResource(com.gloiot.chatsdk.R.drawable.btn_send_bg);
        ekBar.getmBtnVoiceOrText().setVisibility(View.GONE);
    }

    public void showKeyBord(String hint) {
        isShowKeyBord = true;
        ekBar.getRl_keybord().setVisibility(View.VISIBLE);
        ekBar.getEtChat().setHint(hint);
        ekBar.getEtChat().setFocusable(true);
        ekBar.getEtChat().setFocusableInTouchMode(true);
        ekBar.getEtChat().requestFocus();
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void hideKeyBord() {
        isShowKeyBord = false;
        ekBar.getEtChat().setText("");
        ekBar.getEtChat().clearFocus();
        if (ekBar.isShowExpression()) {
            ekBar.reset();
        }
        InputMethodManager manager = (InputMethodManager) ekBar.getEtChat().getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(ekBar.getEtChat().getWindowToken(), 0);
        ekBar.getRl_keybord().setVisibility(View.GONE);
    }

    /**
     * 表情点击监听
     */
    EmoticonClickListener emoticonClickListener = new EmoticonClickListener() {
        @Override
        public void onEmoticonClick(Object o, int actionType, boolean isDelBtn) {
            if (isDelBtn) {
                SimpleCommonUtils.delClick(ekBar.getEtChat());
            } else {
                if (o == null) {
                    return;
                }
                String content = null;
                if (o instanceof EmojiBean) {
                    content = ((EmojiBean) o).emoji;
                } else if (o instanceof EmoticonEntity) {
                    content = ((EmoticonEntity) o).getContent();
                }

                if (TextUtils.isEmpty(content)) {
                    return;
                }
                int index = ekBar.getEtChat().getSelectionStart();
                Editable editable = ekBar.getEtChat().getText();
                editable.insert(index, content);
            }
        }
    };


    @OnClick({R.id.iv_twotitle_back, R.id.tv_twotile_1, R.id.tv_twotile_3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_twotitle_back:
                finish();
                break;
            case R.id.tv_twotile_1:
                if (!currentType.equals(WAITREPLY)) {
                    currentType = WAITREPLY;
                    setTitleStyle();
                }

                break;
            case R.id.tv_twotile_3:
                if (!currentType.equals(REPLIED)) {
                    currentType = REPLIED;
                    setTitleStyle();
                }
                break;
        }
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_wl_recomments:
                if (onLoad) {
                    refreshlayoutEvaluation.setLoading(false);  ////取消加载动画
                } else {
                    evaluationModels.clear();
                    refreshlayoutEvaluation.setRefreshing(false); //取消刷新动画
                }
                JSONArray array = response.getJSONArray("列表");
                page = Integer.parseInt(response.getString("页数"));
                if (array.length() > 0) {
                    refreshlayoutEvaluation.setVisibility(View.VISIBLE);
                    ivNoEvaluation.setVisibility(View.GONE);
                    num = array.length();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        EvaluationModel model = new EvaluationModel();
                        model.setName(object.getString("商品名称"));
                        model.setDescribe("购买了：" + model.getName() + "|" + object.getString("商品规格"));
                        model.setTime(object.getString("录入时间"));
                        model.setImageUrl(object.getString("缩略图"));
                        model.setContent(object.getString("评论"));
                        model.setNickName(object.getString("昵称"));
                        model.setId(object.getString("商品id"));
                        model.setReplyid(object.getString("评论id"));
                        try {
                            model.setReplyaddId(object.getString("追加评论id"));
                        } catch (Exception e) {

                        }

                        JSONArray images = object.getJSONArray("评论图片");
                        if (images.length() > 0) {
                            for (int j = 0; j < images.length(); j++) {
                                JSONObject image = images.getJSONObject(j);
                                model.getEvluationImages().add(image.getString("imgUrl"));
                            }
                        }
                        JSONArray additionalImages = object.getJSONArray("追加图片");
                        if (additionalImages.length() > 0) {
                            for (int j = 0; j < additionalImages.length(); j++) {
                                JSONObject image1 = additionalImages.getJSONObject(j);
                                model.getAdditionalImages().add(image1.getString("imgUrl"));
                            }
                        }
//                        if (TextUtils.isEmpty(object.getString("回复内容"))) {
//                            model.setReply("天气不错");
//                        }
//                        if (TextUtils.isEmpty(object.getString("追加评论"))) {
//
//                            model.setAdd("不错不错");
//
//                        }
//                        if (TextUtils.isEmpty(object.getString("追加回复"))) {
//
//                            model.setReplyadd("很不错很不错");
//
//                        }

                        model.setReply(object.getString("回复内容"));
                        model.setAdd(object.getString("追加评论"));
                        model.setReplyadd(object.getString("追加回复"));
                        evaluationModels.add(model);
                    }
                    evaluationAdapter.notifyDataSetChanged();
                } else {
                    num = 0;
                    evaluationAdapter.notifyDataSetChanged();
                    if (!onLoad) {
                        refreshlayoutEvaluation.setVisibility(View.GONE);
                        ivNoEvaluation.setVisibility(View.VISIBLE);
                    }
                }
                if (onLoad) {
                    onLoad = false;
                }
                break;
            case ConstantUtils.TAG_shop_comment_reply:
                replyTypeId = "";
                MToastUtils.showToast("回复成功");
                hideKeyBord();
                onRefresh();
                break;

        }


    }

    @Override
    public void onRefresh() {
        page = 0;
        onLoad = false;
        requestHandleArrayList.add(requestAction.shop_wl_recomments(this, phone, currentType, "0"));
    }

    @Override
    public void onLoad() {
        onLoad = true;
        try {
            if (num >= 10) {
                page++;
                requestHandleArrayList.add(requestAction.shop_wl_recomments(this, phone, currentType, String.valueOf(page)));
            } else if (num > 0) {
                refreshlayoutEvaluation.setLoading(false);
                MToastUtils.showToast("已无数据");
            } else {
                refreshlayoutEvaluation.setLoading(false);
                MToastUtils.showToast("已无数据");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onItemClick(View view, int position) {
        switch (view.getId()) {
            case R.id.tv_evaluation_content:
                if (isShowKeyBord) {
                    hideKeyBord();
                    break;
                }
                if (currentType.equals("待回复") && TextUtils.isEmpty(evaluationModels.get(position).getReply())) {
                    showKeyBord("回复:" + evaluationModels.get(position).getNickName());
                    replyTypeId = evaluationModels.get(position).getReplyid();
                }
                break;
            case R.id.tv_item_evaluation_addcontent:

                if (isShowKeyBord) {
                    hideKeyBord();
                    break;
                }

                if (currentType.equals("待回复") && TextUtils.isEmpty(evaluationModels.get(position).getReplyadd())) {
                    showKeyBord("回复:" + evaluationModels.get(position).getNickName() + "的追评");
                    replyTypeId = evaluationModels.get(position).getReplyaddId();
                }
                break;
            case R.id.rl_evaluation_manager:
                if (isShowKeyBord) {
                    hideKeyBord();
                    break;
                }
                if (currentType.equals("待回复") && TextUtils.isEmpty(evaluationModels.get(position).getContent()) && TextUtils.isEmpty(evaluationModels.get(position).getReply())) {
                    showKeyBord("回复:" + evaluationModels.get(position).getNickName());
                    replyTypeId = evaluationModels.get(position).getReplyid();
                    break;
                }
                if (currentType.equals("待回复") && !TextUtils.isEmpty(evaluationModels.get(position).getReply()) && TextUtils.isEmpty(evaluationModels.get(position).getAdd()) && TextUtils.isEmpty(evaluationModels.get(position).getReplyadd())) {
                    showKeyBord("回复:" + evaluationModels.get(position).getNickName() + "的追评");
                    replyTypeId = evaluationModels.get(position).getReplyaddId();
                    break;
                }
                break;
            case R.id.iv_evaluation_product:
                Intent intent = new Intent(mContext, ShangpinDianpuActivity.class);
                intent.putExtra("id", evaluationModels.get(position).getId());
                startActivity(intent);
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
