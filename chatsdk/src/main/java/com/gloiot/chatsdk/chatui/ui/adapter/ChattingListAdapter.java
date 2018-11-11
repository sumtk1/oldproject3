package com.gloiot.chatsdk.chatui.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.gloiot.chatsdk.DataBase.IMDBManager;
import com.gloiot.chatsdk.R;
import com.gloiot.chatsdk.bean.ImMsgBean;
import com.gloiot.chatsdk.bean.ImMsgImgBean;
import com.gloiot.chatsdk.bean.ImMsgRecoderBean;
import com.gloiot.chatsdk.bean.ImMsgSPBean;
import com.gloiot.chatsdk.bean.ImMsgTextBean;
import com.gloiot.chatsdk.bean.UserInfo;
import com.gloiot.chatsdk.chatui.ChatUiIM;
import com.gloiot.chatsdk.chatui.UserInfoCache;
import com.gloiot.chatsdk.chatui.keyboard.emoji.SimpleCommonUtils;
import com.gloiot.chatsdk.chatui.keyboard.recorder.MediaManager;
import com.gloiot.chatsdk.chatui.keyboard.utils.Base64Utils;
import com.gloiot.chatsdk.chatui.ui.viewholder.ChatViewHolder;
import com.gloiot.chatsdk.chatui.ui.viewholder.FromUserImgViewHolder;
import com.gloiot.chatsdk.chatui.ui.viewholder.FromUserMsgViewHolder;
import com.gloiot.chatsdk.chatui.ui.viewholder.FromUserSpViewHolder;
import com.gloiot.chatsdk.chatui.ui.viewholder.FromUserVoiceViewHolder;
import com.gloiot.chatsdk.chatui.ui.viewholder.ToUserImgViewHolder;
import com.gloiot.chatsdk.chatui.ui.viewholder.ToUserMsgViewHolder;
import com.gloiot.chatsdk.chatui.ui.viewholder.ToUserSpViewHolder;
import com.gloiot.chatsdk.chatui.ui.viewholder.ToUserVoiceViewHolder;
import com.gloiot.chatsdk.chatui.ui.widget.BubbleImageView;
import com.gloiot.chatsdk.imagepicker.ImagePicker;
import com.gloiot.chatsdk.imagepicker.ui.ImagePreviewSeeActivity;
import com.gloiot.chatsdk.utlis.TimeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.gloiot.chatsdk.bean.ImMsgBean.FROM_USER_IMG;
import static com.gloiot.chatsdk.bean.ImMsgBean.FROM_USER_MSG;
import static com.gloiot.chatsdk.bean.ImMsgBean.FROM_USER_SHANGPIN;
import static com.gloiot.chatsdk.bean.ImMsgBean.FROM_USER_VOICE;
import static com.gloiot.chatsdk.bean.ImMsgBean.TO_USER_IMG;
import static com.gloiot.chatsdk.bean.ImMsgBean.TO_USER_MSG;
import static com.gloiot.chatsdk.bean.ImMsgBean.TO_USER_SHANGPIN;
import static com.gloiot.chatsdk.bean.ImMsgBean.TO_USER_VOICE;


public class ChattingListAdapter<T extends ImMsgBean, U extends ChatViewHolder> extends BaseAdapter {

    private final int VIEW_TYPE_COUNT = 9 ;// 消息总类型
    private final int FROM_MSG = 0;      //接收文字消息
    private final int TO_MSG = 1;        //发送文字消息
    private final int FROM_IMG = 2;      //接收图片消息
    private final int TO_IMG = 3;        //发送图片消息
    private final int FROM_SP = 4;      //接收商品消息
    private final int TO_SP = 5;        //发送商品消息
    private final int FROM_VOID = 6;      //接收语音消息
    private final int TO_VOID = 7;        //发送语音消息

    private LayoutInflater mInflater;
    private List<T> mData;
    private Context mContext;
    private ArrayList<String> imageList = new ArrayList<String>();  // 图片集合
    private HashMap<Integer, Integer> imagePosition = new HashMap<Integer, Integer>();

    private String sendOutid = "";

    public ChattingListAdapter(Context context, String sendOutid) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.sendOutid = sendOutid;
    }

    public void setImageList(ArrayList<String> imageList) {
        this.imageList = imageList;
    }

    public void setImagePosition(HashMap<Integer, Integer> imagePosition) {
        this.imagePosition = imagePosition;
    }

    public void addData(List<T> bean) {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData.clear();
        mData.addAll(bean);
        this.notifyDataSetChanged();
    }

    public void addData(T bean) {
        addData(bean, true, false);
    }

    public void addData(T bean, boolean isNotifyDataSetChanged, boolean isFromHead) {
        if (bean == null) {
            return;
        }
        if (mData == null) {
            mData = new ArrayList<>();
        }
        if (isFromHead) {
            mData.add(0, bean);
        } else {
            mData.add(bean);
        }
        if (isNotifyDataSetChanged) {
            this.notifyDataSetChanged();
        }
    }

    public void changeData(T bean, String msgIdFse) {
        if (bean.getMessageState() == 2 || bean.getMessageState() == 0) {  // 消息发送失败
            for (int i = mData.size() - 1; i >= 0; i--) {
                if (mData.get(i).getMsgid().equals(msgIdFse)) {
                    mData.get(i).setMsgid(bean.getMsgid());
                    mData.get(i).setSendTime(bean.getSendTime());
                    mData.get(i).setMessageState(bean.getMessageState());
                    break;
                }
            }
        } else if (bean.getMessageState() == 1) {
            ImMsgBean imMsgBean = new ImMsgBean();

            //true为重发消息， false为非重发消息
            boolean flag = false;
            int index = -1;

            for (int i = mData.size() - 1; i >= 0; i--) {
                if (mData.get(i).getMsgid().equals(msgIdFse)) {
                    imMsgBean = mData.get(i);
                    index = i;
                    //这里是判断时候为发送失败后重发的消息
                    if (mData.get(i).getMessageState() == 2)
                        flag = true;
                    break;
                }
            }

            if (index != -1) {
                if (flag) {
                    mData.remove(imMsgBean);
                    mData.add(bean);
                } else {
                    mData.get(index).setMsgid(bean.getMsgid());
                    mData.get(index).setSendTime(bean.getSendTime());
                    mData.get(index).setMessageState(bean.getMessageState());
                }
            }
        }

        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        int msgType = -1;
        switch (mData.get(position).getMsgtype()) {
            case FROM_USER_MSG:
                msgType = 0;
                break;
            case TO_USER_MSG:
                msgType = 1;
                break;
            case FROM_USER_IMG:
                msgType = 2;
                break;
            case TO_USER_IMG:
                msgType = 3;
                break;
            case FROM_USER_SHANGPIN:
                msgType = 4;
                break;
            case TO_USER_SHANGPIN:
                msgType = 5;
                break;
            case FROM_USER_VOICE:
                msgType = 6;
                break;
            case TO_USER_VOICE:
                msgType = 7;
                break;
        }
        return msgType;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final T bean = mData.get(position);
        Log.e("getView", bean.toString() + "-");
        int type = getItemViewType(position);
        View holderView;
        switch (type) {
            case FROM_MSG:  // 接收文本消息
                FromUserMsgViewHolder fromUserMsgViewHolder;
                if (view == null) {
                    fromUserMsgViewHolder = new FromUserMsgViewHolder();
                    holderView = mInflater.inflate(R.layout.layout_msgfrom_list_item, null);
                    fromUserMsgViewHolder.headIcon = (ImageView) holderView.findViewById(R.id.iv_chat_icon);
                    fromUserMsgViewHolder.chatContent = (RelativeLayout) holderView.findViewById(R.id.rl_chat_content);
                    fromUserMsgViewHolder.msgContent = (TextView) holderView.findViewById(R.id.tv_chat_content);
                    fromUserMsgViewHolder.chatName = (TextView) holderView.findViewById(R.id.tv_chat_name);
                    fromUserMsgViewHolder.chatTime = (TextView) holderView.findViewById(R.id.tv_chat_time);
                    holderView.setTag(fromUserMsgViewHolder);
                    view = holderView;
                } else {
                    fromUserMsgViewHolder = (FromUserMsgViewHolder) view.getTag();
                }
                try {
                    fromMsgUserLayout(fromUserMsgViewHolder, (ImMsgTextBean) bean, position);
                } catch (Exception e) {
                    Log.e("chatException", e + "");
                    view = mInflater.inflate(R.layout.layout_no_list_item, null);
                }
                break;
            case TO_MSG:  // 发送文本消息
                ToUserMsgViewHolder toUserMsgViewHolder;
                if (view == null) {
                    toUserMsgViewHolder = new ToUserMsgViewHolder();
                    holderView = mInflater.inflate(R.layout.layout_msgto_list_item, null);
                    toUserMsgViewHolder.headIcon = (ImageView) holderView.findViewById(R.id.iv_chat_icon);
                    toUserMsgViewHolder.chatContent = (RelativeLayout) holderView.findViewById(R.id.rl_chat_content);
                    toUserMsgViewHolder.msgContent = (TextView) holderView.findViewById(R.id.tv_chat_content);
                    toUserMsgViewHolder.chatName = (TextView) holderView.findViewById(R.id.tv_chat_name);
                    toUserMsgViewHolder.sendFail = (ImageView) holderView.findViewById(R.id.mysend_fail_img);
                    toUserMsgViewHolder.chatTime = (TextView) holderView.findViewById(R.id.tv_chat_time);
                    holderView.setTag(toUserMsgViewHolder);
                    view = holderView;
                } else {
                    toUserMsgViewHolder = (ToUserMsgViewHolder) view.getTag();
                }
                try {
                    toMsgUserLayout(toUserMsgViewHolder, (ImMsgTextBean) bean, position);
                } catch (Exception e) {
                    Log.e("chatException", e + "");
                    view = mInflater.inflate(R.layout.layout_no_list_item, null);
                }
                break;
            case FROM_VOID:  // 接收语音消息
                FromUserVoiceViewHolder fromUserVoiceViewHolder;
                if (view == null) {
                    fromUserVoiceViewHolder = new FromUserVoiceViewHolder();
                    holderView = mInflater.inflate(R.layout.layout_audiofrom_list_item, null);
                    fromUserVoiceViewHolder.headIcon = (ImageView) holderView.findViewById(R.id.iv_chat_icon);
                    fromUserVoiceViewHolder.chatContent = (RelativeLayout) holderView.findViewById(R.id.rl_chat_content);
                    fromUserVoiceViewHolder.chatName = (TextView) holderView.findViewById(R.id.tv_chat_name);
                    fromUserVoiceViewHolder.chatTime = (TextView) holderView.findViewById(R.id.tv_chat_time);
                    fromUserVoiceViewHolder.chatAnim = holderView.findViewById(R.id.v_chat_anim);
                    fromUserVoiceViewHolder.chatLenght = holderView.findViewById(R.id.fl_chat_lenght);
                    fromUserVoiceViewHolder.chatRecoderTime = (TextView) holderView.findViewById(R.id.tv_chat_recodertime);
                    holderView.setTag(fromUserVoiceViewHolder);
                    view = holderView;
                } else {
                    fromUserVoiceViewHolder = (FromUserVoiceViewHolder) view.getTag();
                }
                try {
                    fromVoidUserLayout(fromUserVoiceViewHolder, (ImMsgRecoderBean) bean, position);
                } catch (Exception e) {
                    Log.e("chatException", e + "");
                    view = mInflater.inflate(R.layout.layout_no_list_item, null);
                }
                break;
            case TO_VOID:  // 发送语音消息
                ToUserVoiceViewHolder toUserVoiceViewHolder;
                if (view == null) {
                    toUserVoiceViewHolder = new ToUserVoiceViewHolder();
                    holderView = mInflater.inflate(R.layout.layout_audioto_list_item, null);
                    toUserVoiceViewHolder.headIcon = (ImageView) holderView.findViewById(R.id.iv_chat_icon);
                    toUserVoiceViewHolder.chatContent = (RelativeLayout) holderView.findViewById(R.id.rl_chat_content);
                    toUserVoiceViewHolder.sendFail = (ImageView) holderView.findViewById(R.id.mysend_fail_img);
                    toUserVoiceViewHolder.chatName = (TextView) holderView.findViewById(R.id.tv_chat_name);
                    toUserVoiceViewHolder.chatTime = (TextView) holderView.findViewById(R.id.tv_chat_time);
                    toUserVoiceViewHolder.chatAnim = holderView.findViewById(R.id.v_chat_anim);
                    toUserVoiceViewHolder.chatLenght = holderView.findViewById(R.id.fl_chat_lenght);
                    toUserVoiceViewHolder.chatRecoderTime = (TextView) holderView.findViewById(R.id.tv_chat_recodertime);
                    holderView.setTag(toUserVoiceViewHolder);
                    view = holderView;
                } else {
                    toUserVoiceViewHolder = (ToUserVoiceViewHolder) view.getTag();
                }
                try {
                    toVoidUserLayout(toUserVoiceViewHolder, (ImMsgRecoderBean) bean, position);
                } catch (Exception e) {
                    Log.e("chatException", e + "");
                    view = mInflater.inflate(R.layout.layout_no_list_item, null);
                }
                break;
            case FROM_IMG: // 接收图片消息
                FromUserImgViewHolder fromUserImgViewHolder;
                if (view == null) {
                    fromUserImgViewHolder = new FromUserImgViewHolder();
                    view = mInflater.inflate(R.layout.layout_imagefrom_list_item, null);
                    fromUserImgViewHolder.headIcon = (ImageView) view.findViewById(R.id.iv_chat_icon);
                    fromUserImgViewHolder.chatContent = (RelativeLayout) view.findViewById(R.id.rl_chat_content);
                    fromUserImgViewHolder.chatName = (TextView) view.findViewById(R.id.tv_chat_name);
                    fromUserImgViewHolder.chatImage = (BubbleImageView) view.findViewById(R.id.image_message);
                    fromUserImgViewHolder.chatTime = (TextView) view.findViewById(R.id.tv_chat_time);
                    view.setTag(fromUserImgViewHolder);
                } else {
                    fromUserImgViewHolder = (FromUserImgViewHolder) view.getTag();
                }
                try {
                    fromImgUserLayout(fromUserImgViewHolder, (ImMsgImgBean) bean, position);
                } catch (Exception e) {
                    Log.e("chatException", e + "");
                    view = mInflater.inflate(R.layout.layout_no_list_item, null);
                }
                break;
            case TO_IMG: // 发送图片消息
                ToUserImgViewHolder toUserImgViewHolder;
                if (view == null) {
                    toUserImgViewHolder = new ToUserImgViewHolder();
                    view = mInflater.inflate(R.layout.layout_imageto_list_item, null);
                    toUserImgViewHolder.headIcon = (ImageView) view.findViewById(R.id.iv_chat_icon);
                    toUserImgViewHolder.chatContent = (RelativeLayout) view.findViewById(R.id.rl_chat_content);
                    toUserImgViewHolder.chatName = (TextView) view.findViewById(R.id.tv_chat_name);
                    toUserImgViewHolder.chatImage = (BubbleImageView) view.findViewById(R.id.image_message);
                    toUserImgViewHolder.sendFail = (ImageView) view.findViewById(R.id.mysend_fail_img);
                    toUserImgViewHolder.chatTime = (TextView) view.findViewById(R.id.tv_chat_time);
                    view.setTag(toUserImgViewHolder);
                } else {
                    toUserImgViewHolder = (ToUserImgViewHolder) view.getTag();
                }
                try {
                    toImgUserLayout(toUserImgViewHolder, (ImMsgImgBean) bean, position);
                } catch (Exception e) {
                    Log.e("chatException", e + "");
                    view = mInflater.inflate(R.layout.layout_no_list_item, null);
                }
                break;
            case FROM_SP:  // 接收商品消息
                FromUserSpViewHolder fromUserSpViewHolder;
                if (view == null) {
                    fromUserSpViewHolder = new FromUserSpViewHolder();
                    holderView = mInflater.inflate(R.layout.layout_spfrom_list_item, null);
                    fromUserSpViewHolder.headIcon = (ImageView) holderView.findViewById(R.id.iv_chat_icon);
                    fromUserSpViewHolder.chatContent = (RelativeLayout) holderView.findViewById(R.id.rl_chat_content);
                    fromUserSpViewHolder.chatName = (TextView) holderView.findViewById(R.id.tv_chat_name);
                    fromUserSpViewHolder.chatSpIcon = (ImageView) holderView.findViewById(R.id.iv_chat_sp_icon);
                    fromUserSpViewHolder.chatSpTitle = (TextView) holderView.findViewById(R.id.tv_chat_sp_title);
                    fromUserSpViewHolder.chatSpMoney = (TextView) holderView.findViewById(R.id.tv_chat_sp_money);
                    fromUserSpViewHolder.chatTime = (TextView) holderView.findViewById(R.id.tv_chat_time);
                    holderView.setTag(fromUserSpViewHolder);
                    view = holderView;
                } else {
                    fromUserSpViewHolder = (FromUserSpViewHolder) view.getTag();
                }
                try {
                    fromSpUserLayout(fromUserSpViewHolder, (ImMsgSPBean) bean, position);
                } catch (Exception e) {
                    view = mInflater.inflate(R.layout.layout_no_list_item, null);
                }
                break;
            case TO_SP:  // 发送商品消息
                ToUserSpViewHolder toUserSpViewHolder;
                if (view == null) {
                    toUserSpViewHolder = new ToUserSpViewHolder();
                    holderView = mInflater.inflate(R.layout.layout_spto_list_item, null);
                    toUserSpViewHolder.headIcon = (ImageView) holderView.findViewById(R.id.iv_chat_icon);
                    toUserSpViewHolder.chatContent = (RelativeLayout) holderView.findViewById(R.id.rl_chat_content);
                    toUserSpViewHolder.chatName = (TextView) holderView.findViewById(R.id.tv_chat_name);
                    toUserSpViewHolder.sendFail = (ImageView) holderView.findViewById(R.id.mysend_fail_img);
                    toUserSpViewHolder.chatSpIcon = (ImageView) holderView.findViewById(R.id.iv_chat_sp_icon);
                    toUserSpViewHolder.chatSpTitle = (TextView) holderView.findViewById(R.id.tv_chat_sp_title);
                    toUserSpViewHolder.chatSpMoney = (TextView) holderView.findViewById(R.id.tv_chat_sp_money);
                    toUserSpViewHolder.chatTime = (TextView) holderView.findViewById(R.id.tv_chat_time);
                    holderView.setTag(toUserSpViewHolder);
                    view = holderView;
                } else {
                    toUserSpViewHolder = (ToUserSpViewHolder) view.getTag();
                }
                try {
                    toSpUserLayout(toUserSpViewHolder, (ImMsgSPBean) bean, position);
                } catch (Exception e) {
                    view = mInflater.inflate(R.layout.layout_no_list_item, null);
                }
                break;
            case -1:
                if (view == null) {
                    view = mInflater.inflate(R.layout.layout_no_list_item, null);
                }
                break;
        }
        return view;
    }

    // 接收文字消息
    private void fromMsgUserLayout(final FromUserMsgViewHolder holder, final ImMsgTextBean bean, final int position) {
        setUserInfo((T) bean, (U) holder);
        setContent(holder.msgContent, bean.getPushdata());
        chatClickListener(position, holder, bean);
        setTime(holder, bean, position);
    }

    // 发送文字消息
    private void toMsgUserLayout(final ToUserMsgViewHolder holder, final ImMsgTextBean bean, final int position) {
        setUserInfo((T) bean, (U) holder);
        setContent(holder.msgContent, bean.getPushdata());
        chatClickListener(position, holder, bean);
        isSendFail((T) bean, (U) holder); // 重新发送按钮
        setTime(holder, bean, position);
    }

    // 接收图片消息
    private void fromImgUserLayout(final FromUserImgViewHolder holder, final ImMsgImgBean bean, final int position) {
        setUserInfo((T) bean, (U) holder);
        chatClickListener(position, holder, bean); // 点击事件
        setTime(holder, bean, position);
        try {
            ImageView imageView = holder.chatImage;
            String tag = (String) imageView.getTag(R.id.imageloader_uri);
            String img = null;
            if (!bean.getMsgid().equals(tag)) {
                img = new JSONObject(bean.getMessage()).getString("img");
                byte[] decode = Base64.decode(img, Base64.DEFAULT);
                RequestOptions options = new RequestOptions()
//                        .transform(new CustomShapeTransformation(mContext, R.drawable.common_chat_left))   // 自定义图片形状
                        .error(R.mipmap.ip_default_image)
                        .placeholder(R.mipmap.ip_default_image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL);
                Glide.with(mContext)
                        .asBitmap() // bitmap转化，如果是gif，则会显示第一帧
                        .load(decode)
                        .apply(options)
                        .into(imageView); // 显示图片
                imageView.setTag(R.id.imageloader_uri, bean.getMsgid());
            }
            imageView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent intentPreview = new Intent(mContext, ImagePreviewSeeActivity.class);
                    intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, imageList);
                    intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, imagePosition.get(position));
                    intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
                    mContext.startActivity(intentPreview);
                }
            });
        } catch (Exception e) {
            Log.e("exception", e + "--");
        }
    }

    // 发送图片消息
    private void toImgUserLayout(final ToUserImgViewHolder holder, final ImMsgImgBean bean, final int position) {
        setUserInfo((T) bean, (U) holder);
        chatClickListener(position, holder, bean); // 点击事件
        isSendFail((T) bean, (U) holder); // 重新发送按钮
        setTime(holder, bean, position);

        try {
            ImageView imageView = holder.chatImage;
            String tag = (String) imageView.getTag(R.id.imageloader_uri);
            int res = R.drawable.common_chat_right;
            String img = null;
            if (!bean.getMsgid().equals(tag)) {
                img = new JSONObject(bean.getMessage()).getString("img");
                RequestOptions options = new RequestOptions()
//                        .transform(new CustomShapeTransformation(mContext, res))   // 自定义图片形状  界面固定死宽高，后期优化修改
                        .error(R.mipmap.ip_default_image)   // 加载错误图片
                        .placeholder(R.mipmap.ip_default_image) // 加载中图片
//                        .override(150, 200) //设置最终显示的图片像素   界面固定死宽高，后期优化修改
                        .diskCacheStrategy(DiskCacheStrategy.ALL); // 缓存所有版本的图片,默认模式
                if (new File(img).exists()) {
                    Glide.with(mContext)
                            .asBitmap() // bitmap转化，如果是gif，则会显示第一帧
                            .load(img)
                            .apply(options)
//                            .thumbnail(0.1f)//10%的原图大小
                            .into(imageView); // 显示图片
                } else {
                    byte[] decode = Base64.decode(img, Base64.DEFAULT);
                    Glide.with(mContext)
                            .asBitmap() // bitmap转化，如果是gif，则会显示第一帧
                            .load(decode)
                            .apply(options)
//                            .thumbnail(0.1f)//10%的原图大小
                            .into(imageView); // 显示图片
                }
                imageView.setTag(R.id.imageloader_uri, bean.getMsgid());
            }
            imageView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent intentPreview = new Intent(mContext, ImagePreviewSeeActivity.class);
                    intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, imageList);
                    intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, imagePosition.get(position));
                    intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
                    mContext.startActivity(intentPreview);
                }
            });
        } catch (JSONException e) {
            Log.e("exception", e + "--");
        }
    }

    // 接收商品消息
    private void fromSpUserLayout(final FromUserSpViewHolder holder, final ImMsgSPBean bean, final int position) {
        setUserInfo((T) bean, (U) holder);
        chatClickListener(position, holder, bean);
        setTime(holder, bean, position);

        Glide.with(mContext).load(bean.getSpIcon()).into(holder.chatSpIcon);
        holder.chatSpTitle.setText(bean.getSpTitle());
        holder.chatSpMoney.setText("¥" + bean.getSpMoney());
    }

    // 发送商品消息
    private void toSpUserLayout(final ToUserSpViewHolder holder, final ImMsgSPBean bean, final int position) {
        setUserInfo((T) bean, (U) holder);
        chatClickListener(position, holder, bean);
        isSendFail((T) bean, (U) holder); // 重新发送按钮
        setTime(holder, bean, position);

        Glide.with(mContext).load(bean.getSpIcon()).into(holder.chatSpIcon);
        holder.chatSpTitle.setText(bean.getSpTitle());
        holder.chatSpMoney.setText("¥" + bean.getSpMoney());
    }

    // 接收语音消息
    private void fromVoidUserLayout(final FromUserVoiceViewHolder holder, final ImMsgRecoderBean bean, final int position) {
        setUserInfo((T) bean, (U) holder);
        chatClickListener(position, holder, bean);
        setTime(holder, bean, position);

        manageVoiceMessage((U) holder, bean, "from", position);
    }

    // 发送语音消息
    private void toVoidUserLayout(final ToUserVoiceViewHolder holder, final ImMsgRecoderBean bean, final int position) {
        setUserInfo((T) bean, (U) holder);
        chatClickListener(position, holder, bean);
        isSendFail((T) bean, (U) holder); // 重新发送按钮
        setTime(holder, bean, position);

        manageVoiceMessage((U) holder, bean, "to", position);
    }

    /**
     * 处理语音消息逻辑
     *
     * @param holder
     * @param bean
     * @param type   判断是接收消息还是发送消息
     */
    private void manageVoiceMessage(final U holder, final ImMsgRecoderBean bean, final String type, final int position) {
        final boolean isForm = "from".equals(type); // 是否是接收消息
        TextView tv_recoderTime;
        View v_lenght;
        final View v_anim;
        if (isForm) {
            tv_recoderTime = ((FromUserVoiceViewHolder) holder).chatRecoderTime;
            v_lenght = ((FromUserVoiceViewHolder) holder).chatLenght;
            v_anim = ((FromUserVoiceViewHolder) holder).chatAnim;
        } else {
            tv_recoderTime = ((ToUserVoiceViewHolder) holder).chatRecoderTime;
            v_lenght = ((ToUserVoiceViewHolder) holder).chatLenght;
            v_anim = ((ToUserVoiceViewHolder) holder).chatAnim;
        }

        float recoderTime;
        try {
            recoderTime = Float.parseFloat(bean.getRecoderTime());
        } catch (Exception e) {
            recoderTime = 0;
        }
        tv_recoderTime.setText(Math.round(recoderTime) + "\"");

        ViewGroup.LayoutParams lp = v_lenght.getLayoutParams();
        lp.width = bean.getRecoderLenght();
        v_lenght.setLayoutParams(lp);


        v_lenght.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bean.setRecoderAnim(true);

                /** ------------------------------------------------------------
                 * 解析语音数据 */
//                File dir = new File(bean.getRecoderData());
//                String a1 = Base64Utils.fileToBase64(dir);
                File b1 = Base64Utils.base64ToFile(bean.getRecoderData());
                /** ------------------------------------------------------------ */

                // 播放录音
                MediaManager.getInstance().playSound(v_anim, isForm, b1.getAbsolutePath(), new MediaPlayer.OnCompletionListener() {

                    public void onCompletion(MediaPlayer mp) {
                        v_anim.setBackgroundResource(isForm ? R.mipmap.voice_left : R.mipmap.voice_right);
                        bean.setRecoderAnim(false);
                    }
                }, new MediaManager.onInterruptAnimListener() {
                    @Override
                    public void interruptAnim() {
                        bean.setRecoderAnim(false);
                    }
                });
            }
        });

        if (bean.isRecoderAnim()) {
            v_anim.setBackgroundResource(isForm ? R.drawable.play_anim_left : R.drawable.play_anim_right);
            AnimationDrawable animation = (AnimationDrawable) v_anim.getBackground();
            animation.start();
        } else {
            v_anim.setBackgroundResource(isForm ? R.mipmap.voice_left : R.mipmap.voice_right);
        }
    }

    /* time */
    private void setTime(ChatViewHolder holder, ImMsgBean bean, int position) {
        if (position != 0) {
            String showTime = TimeUtils.getTime(bean.getSendTime(), mData.get(position - 1).getSendTime());
            if (showTime != null) {
                holder.chatTime.setVisibility(View.VISIBLE);
                holder.chatTime.setText(showTime);
            } else {
                holder.chatTime.setVisibility(View.GONE);
            }
        } else {
            String showTime = TimeUtils.getTime(bean.getSendTime(), null);
            holder.chatTime.setVisibility(View.VISIBLE);
            holder.chatTime.setText(showTime);
        }
    }


    /**
     * 重新发送按钮显示与隐藏
     *
     * @param bean
     * @param holder
     */
    private void isSendFail(final T bean, U holder) {
        ImageView sendFail;
        // holder转换
        switch (bean.getMsgtype()) {
            case TO_USER_MSG:
                sendFail = ((ToUserMsgViewHolder) holder).sendFail; // 文字
                break;
            case TO_USER_IMG:
                sendFail = ((ToUserImgViewHolder) holder).sendFail; // 图片
                break;
            case TO_USER_SHANGPIN:
                sendFail = ((ToUserSpViewHolder) holder).sendFail; // 商品
                break;
            case TO_USER_VOICE:
                sendFail = ((ToUserVoiceViewHolder) holder).sendFail; // 语音
                break;
            default:
                sendFail = null;
                break;
        }
        switch (bean.getMessageState()) {
            case 0:
                // 发送中
                sendFail.setVisibility(View.VISIBLE); //TODO 暂时隐藏
                sendFail.setBackgroundResource(R.mipmap.loading_chat);
                Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.sending_animotion);
                sendFail.startAnimation(animation);
                break;
            case 1:
                // 发送成功
                sendFail.setVisibility(View.INVISIBLE);
                sendFail.clearAnimation();
                break;
            case 2:
                // 发送失败
                sendFail.setVisibility(View.VISIBLE);
                sendFail.setBackgroundResource(R.mipmap.msg_state_fail_resend_pressed);
                sendFail.clearAnimation();
                sendFail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onMoreClick(v)) {
                            return;
                        }
                        switch (bean.getMsgtype().split("_")[0]) {
                            case "文本":
                                sendFailListener.resend(bean.getMsgid(), bean.getPushdata(), bean.getMsgtype(), bean.getReceiveid(), bean.getSendTime());
                                break;
                            case "图片":
                                sendFailListener.resend(bean.getMsgid(), bean.getMessage(), bean.getMsgtype(), bean.getReceiveid(), bean.getSendTime());
                                break;
                            case "商品":
                                sendFailListener.resend(bean.getMsgid(), bean.getMessage(), bean.getMsgtype(), bean.getReceiveid(), bean.getSendTime());
                                break;
                        }
                    }
                });
                break;
            default:
                break;
        }
    }

    /**
     * 设置用户信息
     *
     * @param bean
     * @param holder
     */
    private void setUserInfo(T bean, U holder) {
        UserInfo userInfo = UserInfoCache.getInstance(mContext).getUserInfo(bean.getSendid(), sendOutid);
        if (null == userInfo) {//缓存没有数据，从数据库获取信息
            userInfo = IMDBManager.getInstance(mContext, sendOutid).queryUserInfo(bean.getSendid());
        }
        if (null != userInfo) { //缓存或数据库中有数据
            UserInfoCache.getInstance(mContext).putData(bean.getSendid(), userInfo);//再存储一次（缓存为空，向其中存数据库数据）
            holder.chatName.setText(userInfo.getName());
            if (!TextUtils.isEmpty(userInfo.getUrl())) {
                if ("客服".equals(userInfo.getUrl())) {
                    Glide.with(mContext).load(R.mipmap.ic_kefu).into(holder.headIcon);
                } else {
                Glide.with(mContext).load(userInfo.getUrl()).into(holder.headIcon);
                }

            }
        } else {//本地不存在，需要联网更新（异步）
            userInfo(bean.getSendid());//开启联网，返回值为null
        }
    }

    /**
     * 设置文字内容包含表情
     */
    public void setContent(TextView tv_content, String content) {
        SimpleCommonUtils.spannableEmoticonFilter(tv_content, content);
    }


    private UserInfo userInfo(String userId) {
        return userInfoProvider.getUserInfo(userId);
    }

    private void chatClickListener(final int position, final ChatViewHolder holder, final ImMsgBean bean) {
        holder.headIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conversationBehaviorListener.onUserPortraitClick(bean, position, holder);
            }
        });

        holder.chatContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conversationBehaviorListener.onMessageClick(bean, position, holder);
            }
        });
    }

    private ChatUiIM.ConversationBehaviorListener conversationBehaviorListener;
    private ChatUiIM.UserInfoProvider userInfoProvider;
    private SendFailListener sendFailListener;

    public void setConversationBehaviorListener(ChatUiIM.ConversationBehaviorListener conversationBehaviorListener) {
        this.conversationBehaviorListener = conversationBehaviorListener;
    }

    public void setUserInfoProvider(ChatUiIM.UserInfoProvider userInfoProvider) {
        this.userInfoProvider = userInfoProvider;
    }

    public void setSendFailListener(SendFailListener sendFailListener) {
        this.sendFailListener = sendFailListener;
    }

    public interface SendFailListener {
        void resend(String msgId, String msgContent, String msgType, String Receiveid, String SendTime);
    }

    /**
     * 防止重复点击
     *
     * @param v
     * @return
     */
    private long lastTime; // 上一次点击时间
    private long delay = 1000; // 默认1秒

    public boolean onMoreClick(View v) {
        boolean flag = false;
        long time = System.currentTimeMillis() - lastTime;
        if (time < delay) {
            flag = true;
        }
        lastTime = System.currentTimeMillis();
        return flag;
    }
}