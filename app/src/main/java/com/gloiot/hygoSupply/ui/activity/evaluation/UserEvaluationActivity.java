package com.gloiot.hygoSupply.ui.activity.evaluation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

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
import com.gloiot.hygoSupply.utlis.CommonUtils;
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

/**
 * 作者：Ljy on 2017/8/29
 * 功能：我的——我的资料
 */


public class UserEvaluationActivity extends BaseActivity implements RefreshLayout.OnLoadListener, SwipeRefreshLayout.OnRefreshListener, EvaluationAdapter.OnItemClickListener {
    @Bind(R.id.rv_user_evaluation)
    RecyclerView rvUserEvaluation;
    @Bind(R.id.refreshlayout_evaluation)
    RefreshLayout refreshlayoutEvaluation;

    private List<EvaluationModel> evaluationModels;
    private EvaluationAdapter evaluationAdapter;
    private boolean onLoad;
    private String productId;
    int page = 0;
    int num;
    private XhsEmoticonsKeyBoard ekBar;
    private String replyTypeId = "";
    private boolean isShowKeyBord;

    @Override
    public int initResource() {
        return R.layout.activity_user_evaluation;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isShowKeyBord) {
            hideKeyBord();
        }
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar((Activity) mContext, true, "用户评价", "");
        ekBar = (XhsEmoticonsKeyBoard) findViewById(R.id.evaluation_ek_bar);
        initEmoticonsKeyBoardBar();
        ekBar.getRl_keybord().setVisibility(View.GONE);
        evaluationModels = new ArrayList<>();
        evaluationAdapter = new EvaluationAdapter(mContext, evaluationModels);
        evaluationAdapter.setListener(this);
        rvUserEvaluation.setLayoutManager(new LinearLayoutManager(this));
        rvUserEvaluation.setAdapter(evaluationAdapter);
        productId = getIntent().getStringExtra("productId");
        refreshlayoutEvaluation.setOnRefreshListener(this);
        refreshlayoutEvaluation.setOnLoadListener(this);
        requestHandleArrayList.add(requestAction.shop_sh_recomments(UserEvaluationActivity.this, phone, productId, "0"));
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_sh_recomments:
                if (onLoad) {
                    onLoad = false;
                    refreshlayoutEvaluation.setLoading(false);  ////取消加载动画
                } else {
                    evaluationModels.clear();
                    refreshlayoutEvaluation.setRefreshing(false); //取消刷新动画
                }
                JSONArray array = response.getJSONArray("列表");
                page = Integer.parseInt(response.getString("页数"));
                if (array.length() > 0) {
                    num = array.length();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        EvaluationModel model = new EvaluationModel();
                        model.setName(object.getString("昵称"));
                        model.setNickName(object.getString("昵称"));
                        model.setDescribe(object.getString("商品规格"));
                        model.setTime(object.getString("录入时间"));
                        model.setImageUrl(object.getString("头像"));
                        model.setContent(object.getString("评论"));
                        model.setAddTime(object.getString("追加时间"));
                        JSONArray images = object.getJSONArray("图片");
                        if (images.length() > 0) {
                            for (int j = 0; j < images.length(); j++) {
                                JSONObject image = images.getJSONObject(j);
                                model.getEvluationImages().add(image.getString("imgUrl"));
                            }
                        }
                        try {
                            JSONArray additionalImages = object.getJSONArray("追加评论图片");
                            if (additionalImages.length() > 0) {
                                for (int j = 0; j < additionalImages.length(); j++) {
                                    JSONObject image = additionalImages.getJSONObject(j);
                                    model.getAdditionalImages().add(image.getString("imgUrl"));
                                }
                            }
                        } catch (Exception e) {

                        }
                        model.setReply(object.getString("回复内容"));
                        model.setAdd(object.getString("追加评论"));
                        model.setReplyadd(object.getString("追加回复"));
                        evaluationModels.add(model);
                    }
                    evaluationAdapter.notifyDataSetChanged();
                } else {
                    evaluationAdapter.notifyDataSetChanged();
                    MToastUtils.showToast("暂无数据");
                }
                break;


        }
    }

    @Override
    public void onRefresh() {
        page = 0;
        onLoad = false;
        requestHandleArrayList.add(requestAction.shop_sh_recomments(UserEvaluationActivity.this, phone, productId, "0"));
        try {
            refreshlayoutEvaluation.removeFoot();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onLoad() {
        onLoad = true;
        try {
            if (evaluationModels.size() >= 10) {
                page++;
                requestHandleArrayList.add(requestAction.shop_sh_recomments(UserEvaluationActivity.this, phone, productId, String.valueOf(page)));
            } else if (evaluationModels.size() > 0) {
                refreshlayoutEvaluation.setLoading(false);
                refreshlayoutEvaluation.isNoData();
            } else {
                refreshlayoutEvaluation.setLoading(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                    requestHandleArrayList.add(requestAction.shop_comment_reply(UserEvaluationActivity.this, phone, replyTypeId, ekBar.getEtChat().getText().toString()));
                }
            }
        });
        ekBar.getEtChat().removeTextChangedListener(ekBar.getWatcher());
        ekBar.getBtnSend().setVisibility(View.VISIBLE);
        ekBar.getBtnMultimedia().setVisibility(View.GONE);
        ekBar.getEtChat().setMaxEms(100);
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
        if (ekBar.isShowExpression()) {
            ekBar.reset();
        }
        ekBar.getEtChat().clearFocus();
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

    @Override
    public void onItemClick(View view, int position) {
        switch (view.getId()) {
            case R.id.tv_evaluation_content:
                if (isShowKeyBord) {
                    hideKeyBord();
                    break;
                }
                if (TextUtils.isEmpty(evaluationModels.get(position).getReply())) {
                    showKeyBord("回复:" + evaluationModels.get(position).getNickName());
                    replyTypeId = evaluationModels.get(position).getReplyid();
                }
                break;
            case R.id.tv_item_evaluation_addcontent:
            case R.id.ll_evaluation_addTime:

                if (isShowKeyBord) {
                    hideKeyBord();
                    break;
                }

                if (TextUtils.isEmpty(evaluationModels.get(position).getReplyadd())) {
                    showKeyBord("回复:" + evaluationModels.get(position).getNickName() + "的追评");
                    replyTypeId = evaluationModels.get(position).getReplyaddId();
                }
                break;
            case R.id.rl_evaluation_manager:
                if (isShowKeyBord) {
                    hideKeyBord();
                    break;
                }
                if (TextUtils.isEmpty(evaluationModels.get(position).getContent()) && TextUtils.isEmpty(evaluationModels.get(position).getReply())) {
                    showKeyBord("回复:" + evaluationModels.get(position).getNickName());
                    replyTypeId = evaluationModels.get(position).getReplyid();
                    break;
                }
                if (!TextUtils.isEmpty(evaluationModels.get(position).getReply()) && TextUtils.isEmpty(evaluationModels.get(position).getAdd()) && TextUtils.isEmpty(evaluationModels.get(position).getReplyadd())) {
                    showKeyBord("回复:" + evaluationModels.get(position).getNickName() + "的追评");
                    replyTypeId = evaluationModels.get(position).getReplyaddId();
                    break;
                }
                break;
            case R.id.iv_evaluation_product:
//                Intent intent = new Intent(mContext, ShangpinDianpuActivity.class);
//                intent.putExtra("id", evaluationModels.get(position).getId());
//                startActivity(intent);
                break;
        }
    }

}
