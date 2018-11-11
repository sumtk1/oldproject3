package com.gloiot.chatsdk.chatui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.bumptech.glide.Glide;
import com.gloiot.chatsdk.AliOss.AliOss;
import com.gloiot.chatsdk.DataBase.DataBaseCallBack;
import com.gloiot.chatsdk.DataBase.IMDBManager;
import com.gloiot.chatsdk.DataBase.MessageChatCallBack;
import com.gloiot.chatsdk.DataBase.Widget.DataChange;
import com.gloiot.chatsdk.MessageManager;
import com.gloiot.chatsdk.R;
import com.gloiot.chatsdk.bean.ImMsgBean;
import com.gloiot.chatsdk.bean.ImMsgImgBean;
import com.gloiot.chatsdk.bean.ImMsgRecoderBean;
import com.gloiot.chatsdk.bean.ImMsgSPBean;
import com.gloiot.chatsdk.bean.ImMsgTextBean;
import com.gloiot.chatsdk.bean.UserInfo;
import com.gloiot.chatsdk.broadcast.BroadcastManager;
import com.gloiot.chatsdk.chatui.keyboard.XhsEmoticonsKeyBoard;
import com.gloiot.chatsdk.chatui.keyboard.data.EmoticonEntity;
import com.gloiot.chatsdk.chatui.keyboard.emoji.EmojiBean;
import com.gloiot.chatsdk.chatui.keyboard.emoji.SimpleCommonUtils;
import com.gloiot.chatsdk.chatui.keyboard.extend.ExtendBean;
import com.gloiot.chatsdk.chatui.keyboard.interfaces.EmoticonClickListener;
import com.gloiot.chatsdk.chatui.keyboard.recorder.AudioRecorderButton;
import com.gloiot.chatsdk.chatui.keyboard.utils.Base64Utils;
import com.gloiot.chatsdk.chatui.keyboard.widget.EmoticonsEditText;
import com.gloiot.chatsdk.chatui.keyboard.widget.FuncLayout;
import com.gloiot.chatsdk.chatui.ui.adapter.ChattingListAdapter;
import com.gloiot.chatsdk.chatui.ui.viewholder.ChatViewHolder;
import com.gloiot.chatsdk.chatui.ui.widget.SimpleAppsGridView;
import com.gloiot.chatsdk.imagepicker.GlideImageLoader;
import com.gloiot.chatsdk.imagepicker.ImagePicker;
import com.gloiot.chatsdk.imagepicker.bean.ImageItem;
import com.gloiot.chatsdk.imagepicker.ui.ImageGridActivity;
import com.gloiot.chatsdk.socket.CallBackListener;
import com.gloiot.chatsdk.socket.SocketListener;
import com.gloiot.chatsdk.socket.SocketSend;
import com.gloiot.chatsdk.utlis.ChatNotification;
import com.gloiot.chatsdk.utlis.Constant;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileWithBitmapCallback;
import com.zyd.wlwsdk.utlis.ImageCompress;
import com.zyd.wlwsdk.utlis.L;
import com.zyd.wlwsdk.utlis.MToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.gloiot.chatsdk.bean.ImMsgBean.FROM_USER_IMG;
import static com.gloiot.chatsdk.bean.ImMsgBean.TO_USER_IMG;
import static com.gloiot.chatsdk.chatui.ui.widget.SimpleAppsGridView.TGA_APPS_CAMERA;
import static com.gloiot.chatsdk.chatui.ui.widget.SimpleAppsGridView.TGA_APPS_IMAGE;
import static com.gloiot.chatsdk.socket.SocketListener.getUUID;

/**
 * Created by hygo00 on 17/2/28.
 * 聊天管理类
 */

public class ChatUiIM<T extends ImMsgBean> implements FuncLayout.OnFuncKeyBoardListener, ChattingListAdapter.SendFailListener, CallBackListener.SendSingleMessageListener {

    public final String MSG_TEXT = "文本";  // 文字
    public final String MSG_IMAGE = "图片"; // 图片
    public final String MSG_AUDIO = "语音"; // 语音
    public final String MSG_SHANGPIN = "商品";  // 商品

    public static final int REQUEST_CODE_PIC = 0x10;
    public static final int REQUEST_CODE_CAMERA = 0x11;

    private static ChatUiIM instance;
    private FrameLayout fl_content; //顶部View的容器
    private ListView lvChat;
    private XhsEmoticonsKeyBoard ekBar; // 表情键盘
    private AudioRecorderButton mRecorderButton; // 发送语音按钮
    private List<ImMsgBean> mDatas; // 消息集合1 用于存储当前聊天所有消息体，主要用于判断条数是否为20的倍数，用于加载数据判断
    private List<T> onceList2; // 消息集合2 用于存储当前聊天第几页的消息，最多20条（比如当前请求的是第5页，该集合就为第5页的消息）
    private List<T> onceList3; // 消息集合3 用于存储当前聊天所有消息体，区别与mDatas,接收到新消息加入onceList3而不加入mDatas
    private ChattingListAdapter chattingListAdapter;
    private Context context;
    private String receiveid;   // 对方用户id
    private String sendOutid;   // 本人用户id
    private HashMap userMap;    // 上个页面传过来数据 receiveId：对方的用户id、sendOutid：当前的用户id、sendCode：随机码、sendName：昵称
    private Bundle bundle;      // 上个页面传过来顶部View数据，mode为顶部View样式，为null时，顶部View不显示
    private int lastItem;   // 最后一条消息位置
    private SimpleAppsGridView simpleAppsGridView; // 加号扩展键
    private ImagePicker imagePicker; // 图片选择的入口类
    private ArrayList<ImageItem> images = null; // 选择的图片集合
    private ArrayList<ImageItem> imageList = new ArrayList<>();//adapter图片数据
    private HashMap<Integer, Integer> imagePosition = new HashMap<Integer, Integer>();//图片下标位置

    SocketSend socketSend = SocketSend.getInstance();

    public ListView getLvChat() {
        return lvChat;
    }

    public XhsEmoticonsKeyBoard getEkBar() {
        return ekBar;
    }

    public AudioRecorderButton getmRecorderButton() {
        return mRecorderButton;
    }

    public static ChatUiIM getInstance() {
        if (instance == null) {
            instance = new ChatUiIM();
        }
        return instance;
    }

    /**
     * 初始化数据
     */
    private void init(View view) {
        fl_content = (FrameLayout) view.findViewById(R.id.fl_content);
        setTopView();
        lvChat = (ListView) view.findViewById(R.id.lv_chat);
        ekBar = (XhsEmoticonsKeyBoard) view.findViewById(R.id.ek_bar);
        mRecorderButton = (AudioRecorderButton) view.findViewById(R.id.btn_voice);
        mDatas = new ArrayList<>();
        onceList2 = new ArrayList<>();
        onceList3 = new ArrayList<>();
        chattingListAdapter = new ChattingListAdapter(context, sendOutid); // 聊天界面适配器
        chattingListAdapter.setConversationBehaviorListener(conversationBehaviorListener); // 界面点击事件监听
        chattingListAdapter.setUserInfoProvider(userInfoProvider);  // 用户信息提供者
        chattingListAdapter.setSendFailListener(this);  // 发送失败监听
        socketSend.setSendSingleMessageListener(this);  // 发送消息网络请求
        initImagePicker(); // 初始化选择图片、拍照
    }

    /**
     * 设置顶部View及数据
     */
    public void setTopView() {

        if (bundle != null) {
            if (Constant.CHAT_TOP_TYPE.equals(bundle.getString("topType"))) {
                View modeView = View.inflate(context, R.layout.view_im_mode, null);
                fl_content.addView(modeView);

                Glide.with(context).load(bundle.getString("icon")).into((ImageView) modeView.findViewById(R.id.view_im_mode_iv_icon));
                TextView title = (TextView) modeView.findViewById(R.id.view_im_mode_tv_title);
                TextView money = (TextView) modeView.findViewById(R.id.view_im_mode_tv_money);
                title.setText(bundle.getString("title"));
                money.setText("¥" + bundle.getString("money"));

                modeView.findViewById(R.id.view_im_mode_tv_send).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (bundle.getBoolean("single", true)) { // 单件商品是为true
                            sendShangpin(bundle);
                        } else {
                            sendShangPinListener.sendShangPin(context, bundle);
                        }
                    }
                });
            }
        }
    }

    /**
     * 初始化选择图片、拍照
     */
    private void initImagePicker() {
        imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());
        imagePicker.setCrop(false);             // 裁剪
        imagePicker.setMultiMode(true);         // 多选
        imagePicker.setShowCamera(true);        // 显示相机
        imagePicker.setSelectLimit(1);          // 最多1张
        imagePicker.setOutPutX(Integer.valueOf("800"));
        imagePicker.setOutPutY(Integer.valueOf("800"));
    }

    /**
     * 进入聊天界面设置View
     *
     * @param view
     * @param context
     * @param userMap
     */
    public void setView(View view, Context context, final HashMap userMap, Bundle bundle) {
        this.receiveid = (String) userMap.get("receiveId");
        this.sendOutid = (String) userMap.get("sendId");
        this.userMap = userMap;
        this.bundle = bundle;
        this.context = context;
        init(view);
        lvChatSetOnScrollListener(); // 聊天listview滑动监听
        simpleAppsGridView();   // 加号扩展键
        broadcat(); // 监听广播
        getData(true, receiveid); // 获取消息数据
        initEmoticonsKeyBoardBar(); // 初始化聊天键盘
        audioRecorder(); // 发送语音

        ChatNotification.getInstance().setCurrentId(receiveid);
    }

    /**
     * 聊天listview滑动监听
     */
    private void lvChatSetOnScrollListener() {
        lvChat.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_FLING:
                        break;
                    case SCROLL_STATE_IDLE:
                        //判断滚动到顶部
                        if (lvChat.getFirstVisiblePosition() == 0) {
                            //滚到顶部，当条数部位0或者为20的倍数时，加载数据
                            if (mDatas.size() != 0 && mDatas.size() % 20 == 0) {
                                getData(false, receiveid);
                            }
                        }
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL:
                        ekBar.reset(); // 滑动界面，隐藏聊天键盘
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastItem = totalItemCount; // 滑动到底部获取最后一条消息位置
            }
        });
    }

    /**
     * 加号扩展键
     */
    private void simpleAppsGridView() {
        simpleAppsGridView = new SimpleAppsGridView(context);
        simpleAppsGridView.setAppsOnItemClick(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case TGA_APPS_IMAGE: // 选择图片跳转
                        Intent intent = new Intent(context, ImageGridActivity.class);
                        intent.putExtra(ImageGridActivity.EXTRAS_IMAGES, images);
                        intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, false); // 不打开拍照
                        ((Activity) context).startActivityForResult(intent, REQUEST_CODE_PIC);
                        break;
                    case TGA_APPS_CAMERA: // 拍照
                        Intent intent1 = new Intent(context, ImageGridActivity.class);
                        intent1.putExtra(ImageGridActivity.EXTRAS_IMAGES, images);
                        intent1.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 打开拍照
                        ((Activity) context).startActivityForResult(intent1, REQUEST_CODE_CAMERA);
                        break;
                }
                simpleApps.appsOnClick(position);
            }
        });
        if (simpleApps.addApps() != null) {
            simpleAppsGridView.getAddApps().add(simpleApps.addApps());
        }
    }

    BroadcastReceiver broadcastReceiver;

    /**
     * 接收新消息广播监听
     */
    private void broadcat() {
        if (broadcastReceiver == null) { // 为空才新建广播操作
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    ImMsgBean imMsgBean = (ImMsgBean) intent.getExtras().getSerializable("data");
                    if (imMsgBean.getMsgtype().contains("图片")) {
                        // 如果为图片消息message单独从数据库获取
                        imMsgBean.setMessage(IMDBManager.getInstance(context, sendOutid).queryOneMessage(receiveid, imMsgBean.getMsgid()).getMessage());
                    }
                    String reId = imMsgBean.getReceiveid();
                    String seId = imMsgBean.getSendid();
                    // 判断收到的新消息是否在当前页面
                    if ((sendOutid.equals(seId) && receiveid.equals(reId)) || (sendOutid.equals(reId) && receiveid.equals(seId))) {
                        Log.e("broadcat", "------" + imMsgBean.getMsgid());
                        T t = changeData(imMsgBean);
                        chattingListAdapter.addData(t);
                        onceList3.add(onceList3.size(), t);
                        setImageList(onceList3);
                        lvChat.setSelection(lvChat.getCount() - 1);

                        if ("1".equals(imMsgBean.getMsgtype().split("_")[1])) { // 判断是否是发送消息 当为接收消息不用广播回调
                            BroadcastManager.getInstance(context).getBroadcastCallback().callback(imMsgBean.getMsgid());
                        }
                    }
                }
            };
        }
        BroadcastManager.getInstance(context).addAction(MessageManager.NEW_MESSAGE, broadcastReceiver);
    }

    /**
     * 关闭接收新消息广播
     */
    public void destroyBroadcast() {
        BroadcastManager.getInstance(context).destroy(MessageManager.NEW_MESSAGE);
        lastItem = 0; // 消息界面最后一条位置清0

        ChatNotification.getInstance().setCurrentId("");
    }

    /**
     * 清除未读数
     */
    public void cleanNoReadNum() {
        IMDBManager.getInstance(context, sendOutid).NoReadNumClean(receiveid, new DataBaseCallBack() {
            @Override
            public void operationState(boolean flag) {
            }
        });
    }


    /**
     * 消息类型转换
     *
     * @param imMsgBean
     * @return
     */
    private T changeData(ImMsgBean imMsgBean) {
        switch (imMsgBean.getMsgtype().split("_")[0]) {
            case "文本":
                ImMsgTextBean imMsgTextBean = new ImMsgTextBean();
                imMsgTextBean = imMsgTextBean.typeCast(imMsgBean);
                return (T) imMsgTextBean;
            case "图片":
                ImMsgImgBean imMsgImgBean = new ImMsgImgBean();
                imMsgImgBean = imMsgImgBean.typeCast(imMsgBean);
                return (T) imMsgImgBean;
            case "商品":
                ImMsgSPBean imMsgSPBean = new ImMsgSPBean();
                imMsgSPBean = imMsgSPBean.typeCast(imMsgBean);
                return (T) imMsgSPBean;
            case "语音":
                ImMsgRecoderBean imMsgRecoderBean = new ImMsgRecoderBean();
                imMsgRecoderBean = imMsgRecoderBean.typeCast(imMsgBean);

                float recoderTime;
                try {
                    recoderTime = Float.parseFloat(imMsgRecoderBean.getRecoderTime());
                } catch (Exception e) {
                    recoderTime = 0;
                }
                int mMinItemWidth; //最小的item宽度
                int mMaxItemWidth; //最大的item宽度
                //获取屏幕的宽度
                WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                DisplayMetrics outMetrics = new DisplayMetrics();
                wm.getDefaultDisplay().getMetrics(outMetrics);
                mMaxItemWidth = (int) (outMetrics.widthPixels * 0.5f);
                mMinItemWidth = (int) (outMetrics.widthPixels * 0.15f);

                imMsgRecoderBean.setRecoderLenght((int) (mMinItemWidth + (mMaxItemWidth / 60f) * recoderTime));
                imMsgRecoderBean.setRecoderAnim(false);
                return (T) imMsgRecoderBean;
            default:
                return (T) imMsgBean;
        }
    }

    /**
     * 设置数据
     *
     * @param isNewest  true：第一次进来加载的数据、false：加载历史消息数据
     * @param receiveid
     */
    private void getData(boolean isNewest, String receiveid) {
        List<ImMsgBean> onceList;
        onceList2.clear();
        IMDBManager imdbManager = IMDBManager.getInstance(context, sendOutid);
        if (isNewest) { // 判断是否第一次进来
            mDatas.clear();
            onceList = imdbManager.queryChatMsg(receiveid, 0); // 取出第一页的消息
        } else {
            onceList = imdbManager.queryChatMsg(receiveid, onceList3.size()); // 根据onceList3条数取出相应页数
        }

        Collections.reverse(onceList); // 将取出的数据倒序排列
        for (ImMsgBean imMsgBean : onceList) {  // 取出的数据存进onceList2、mDatas集合
            T t = changeData(imMsgBean);
            onceList2.add(t);
            mDatas.add(t);
        }
        onceList2.addAll(onceList3);
        onceList3.clear();
        onceList3.addAll(onceList2);
        if (onceList.size() != 0) {
            chattingListAdapter.addData(onceList3);
        }
        setImageList(onceList3);
        if (isNewest) {
            lvChat.setAdapter(chattingListAdapter);
            scrollToBottom();
        } else {
            // 设置listview的当前位置，如果不设置每次加载完后都会返回到list的第一项。
            lvChat.setSelection(lvChat.getCount() - lastItem);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        for (ImMsgBean imMsgBean : onceList) {
            try {
                if (imMsgBean.getMessageState() == 0) {  //消息状态为发送中时去判断是否发送超时
                    Date nowTime = new Date(System.currentTimeMillis());
                    Date sendTime = sdf.parse(imMsgBean.getSendTime());

                    if ((nowTime.getTime() - sendTime.getTime()) / 1000 > 20) {
                        // 改变数据库消息
                        updataMessage(imMsgBean.getMsgid(), imMsgBean.getMsgid(), imMsgBean.getReceiveid(), 2, imMsgBean.getSendTime());
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置图片集合
     */
    private void setImageList(List<T> chatlist) {
        int key = 0; // 消息位置下标
        int position = 0; // 图片位置下标
        imageList.clear();
        imagePosition.clear();
        for (ImMsgBean imMsgBean : chatlist) {  // 取出图片消息 存进imageList
            if (imMsgBean.getMsgtype().equals(FROM_USER_IMG) || imMsgBean.getMsgtype().equals(TO_USER_IMG)) {
                try {
                    JSONObject jsonObject = new JSONObject(imMsgBean.getMessage());
                    String url = jsonObject.getString("url");
                    String img = jsonObject.getString("img");
                    ImageItem imageItem = new ImageItem();
                    imageItem.path = TextUtils.isEmpty(url) ? img : url;
                    imageList.add(imageItem); // 如果url为空就传缩略图
                    imagePosition.put(key, position);
                    position++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            key++;
        }
        chattingListAdapter.setImageList(imageList); //adapter图片数据
        chattingListAdapter.setImagePosition(imagePosition);//图片下标位置
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
        ekBar.addOnFuncKeyBoardListener(this);
        // 加号弹出的内容
        ekBar.addFuncView(simpleAppsGridView);

        ekBar.getEtChat().setOnSizeChangedListener(new EmoticonsEditText.OnSizeChangedListener() {
            @Override
            public void onSizeChanged(int w, int h, int oldw, int oldh) {
                scrollToBottom();
            }
        });

        // 发送按钮
        ekBar.getBtnSend().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnSendBtnClick(ekBar.getEtChat().getText().toString());
                ekBar.getEtChat().setText("");
            }
        });
//        // 添加表情toolbar左边的加号
//        ekBar.getEmoticonsToolBarView().addFixedToolItemView(false, R.mipmap.icon_add, null, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        // 添加表情toolbar
//        ekBar.getEmoticonsToolBarView().addToolItemView(R.mipmap.icon_setting, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
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

    /**
     * 发送文字
     *
     * @param msg
     */
    private void OnSendBtnClick(final String msg) {
        if (!TextUtils.isEmpty(msg)) {
            try {
                // 消息转译（转译双引号）
                String msg1;
                if (msg.contains("\"")) {
                    msg1 = msg.replaceAll("\"", "\\\"");
                } else {
                    msg1 = msg;
                }
                JSONObject message = new JSONObject();
                message.put("text", msg1);
                insertSendMsg(MSG_TEXT, message);
                scrollToBottom();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送图片
     *
     * @param images
     */
    public void sendImage(ArrayList<ImageItem> images) {
        for (final ImageItem image : images) {
            try {
                compressImage(image.path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 发送语音
     */
    private void audioRecorder() {
        mRecorderButton = (AudioRecorderButton) ekBar.getBtnVoice();
        mRecorderButton.setAudioFinishRecorderListener(new AudioRecorderButton.AudioFinishRecorderListener() {
            @Override
            public void onFinish(float seconds, String filePath) {
                File dir = new File(filePath);
                String recoderData = Base64Utils.fileToBase64(dir);
                try {
                    JSONObject message = new JSONObject();
                    message.put("recoderTime", seconds + "");
                    message.put("recoderData", recoderData);
                    message.put("extra", "");
                    insertSendMsg(MSG_AUDIO, message);
                    scrollToBottom();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                L.e("audio", "--" + filePath + "---" + seconds);
            }
        });
    }

    /**
     * 发送商品消息
     *
     * @param bundle  消息的内容
     */
    public void sendShangpin(final Bundle bundle) {
        if (bundle != null) {
            fl_content.setVisibility(View.GONE);
            try {
                JSONObject message = new JSONObject();
                message.put("spId", bundle.getString("id"));
                message.put("spIcon", bundle.getString("icon"));
                message.put("spTitle", bundle.getString("title"));
                message.put("spMoney", bundle.getString("money"));
                message.put("spUrl", "");
                message.put("extra", bundle.getString("extra"));
                insertSendMsg(MSG_SHANGPIN, message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 消息内容
     *
     * @param msgType 消息类型
     * @param message 子消息内容
     * @return
     */
    private JSONObject msgContent(String msgType, JSONObject message, String uuid) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentTime = sdf.format(new Date());
        JSONObject msgContent = new JSONObject();
        try {
            switch (msgType) {
                case MSG_TEXT:  // 文字消息
                    msgContent.put("pushData", message.getString("text"));
                    break;
                case MSG_IMAGE: // 图片消息
                    msgContent.put("pushData", "[图片]");
                    break;
                case MSG_SHANGPIN:  // 商品消息
                    msgContent.put("pushData", "[商品]");
                    break;
                case MSG_AUDIO:  // 语音消息
                    msgContent.put("pushData", "[语音]");
                    break;
            }
            msgContent.put("msgId", uuid);
            msgContent.put("sendId", sendOutid);
            msgContent.put("receiveId", receiveid);
            msgContent.put("sessionType", "单聊");
            msgContent.put("sendTime", currentTime);
            msgContent.put("msgType", msgType);
            msgContent.put("message", message);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return msgContent;
    }


    /**
     * 发送消息录入数据库
     *
     * @param msgType 消息类型
     * @param message 发送内容
     */
    private void insertSendMsg(final String msgType, final JSONObject message) {
        final String uuid = getUUID();
        IMDBManager.getInstance(context, sendOutid).insertChatMsg(new DataChange().JsonToSystemBean(msgContent(msgType, message, uuid)), 1, new MessageChatCallBack() {
            @Override
            public void DoChatDBSucceed(final ImMsgBean imMsgBean) {
                BroadcastManager.getInstance(context).sendBroadcast(MessageManager.NEW_MESSAGE, imMsgBean, new BroadcastManager.BroadcastCallback() {
                    @Override
                    public void callback(String msgId) {
                        if (!msgId.equals(imMsgBean.getMsgid())) {
                            return;
                        }
                        switch (imMsgBean.getMsgtype().split("_")[0]) {
                            case MSG_TEXT:
                                try {
                                    socketSend.sendMsg(userMap, message.getString("text"), MSG_TEXT, imMsgBean.getMsgid(), imMsgBean.getPushdata());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case MSG_IMAGE:
                                updataToAliOss(imMsgBean.getMsgid(), imMsgBean.getReceiveid(), imMsgBean.getSendTime(), message, imMsgBean.getMsgid());
                                break;
                            case MSG_SHANGPIN:
                                socketSend.sendMsg(userMap, message.toString(), MSG_SHANGPIN, msgId, imMsgBean.getPushdata());
                                break;
                            case MSG_AUDIO:
                                socketSend.sendMsg(userMap, message.toString(), MSG_AUDIO, uuid, imMsgBean.getPushdata());
                                break;
                            default:
                                socketSend.sendMsg(userMap, message.toString(), MSG_IMAGE, uuid, imMsgBean.getPushdata());
                        }
                    }
                });
                scrollToBottom();
            }

            @Override
            public void DoChatDBFault() {

            }
        });
    }

    /**
     * 更新数据库并请求发送消息
     *
     * @param msgType
     * @param msgId
     * @param uuid
     * @param message
     */
    private void updataSendMsg(final String msgType, String msgId, final String uuid, final JSONObject message) {
        IMDBManager.getInstance(context, sendOutid).UpDateMessage(msgId, receiveid, message.toString(), new MessageChatCallBack() {
            @Override
            public void DoChatDBSucceed(ImMsgBean imMsgBean) {
                socketSend.sendMsg(userMap, message.toString(), imMsgBean.getMsgtype().split("_")[0], uuid, imMsgBean.getPushdata());
            }

            @Override
            public void DoChatDBFault() {

            }
        });
    }

    /**
     * 压缩图片并更改数据库 请求发送
     *
     * @param imgPath
     */
    private void compressImage(final String imgPath) {
        try {
            Runtime runtime = Runtime.getRuntime();
            System.gc();
            runtime.runFinalization();
            System.gc();

            final File path = new File(imgPath);
            final Tiny.FileCompressOptions compressOptions = new Tiny.FileCompressOptions();
            compressOptions.config = Bitmap.Config.ARGB_8888;
            Tiny.getInstance().source(path).asFile().withOptions(compressOptions).compress(new FileWithBitmapCallback() {
                @Override
                public void callback(boolean isSuccess, final Bitmap bitmap, final String outfile) {
                    if (!isSuccess) {
                        return;
                    }
                    Log.e("outfile", outfile);

                    try {
                        JSONObject message = new JSONObject();
                        message.put("img", outfile);
                        message.put("url", "");
                        message.put("extra", "");
                        insertSendMsg(MSG_IMAGE, message);
                        scrollToBottom();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updataToAliOss(final String Msgid, final String Receiveid, final String SendTime, final JSONObject message, final String uuid) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    final String imagePath = message.getString("img");

                    new AliOss(context, imagePath) {
                        @Override
                        protected void uploadProgress(long currentSize, long totalSize) {

                        }

                        @Override
                        protected void uploadSuccess(final String myPicUrl) {

                            File outputFile = new File(imagePath);
                            long fileSize = outputFile.length();
                            double scale = Math.sqrt((float) fileSize / 10240);

                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inJustDecodeBounds = true;
                            BitmapFactory.decodeFile(imagePath, options);
                            int height = options.outHeight;
                            int width = options.outWidth;

                            final Tiny.FileCompressOptions compressOptions = new Tiny.FileCompressOptions();
                            compressOptions.width = (int) (width / scale);
                            compressOptions.height = (int) (height / scale);
                            compressOptions.size = 10;
                            compressOptions.config = Bitmap.Config.ARGB_8888;
                            Tiny.getInstance().source(imagePath).asFile().withOptions(compressOptions).compress(new FileWithBitmapCallback() {
                                @Override
                                public void callback(boolean isSuccess, Bitmap bitmap, String outfile) {
                                    File file = new File(outfile);
                                    JSONObject message2 = new JSONObject();
                                    try {
//                                        message2.put("img", ImageCompress.bitmapToBase64(bitmap));
                                        message2.put("img", ImageCompress.fileToBase64(file));
                                        message2.put("url", myPicUrl);
                                        message2.put("extra", "");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    // 得到阿里云图片地址存储数据库
                                    updataSendMsg(MSG_IMAGE, Msgid, uuid, message2);
                                }
                            });

                        }

                        @Override
                        protected void uploadFailure(PutObjectRequest request, ClientException clientException, ServiceException serviceException) {
                            MToast.showToast("消息发送失败");
                            // 改变数据库消息
                            IMDBManager.getInstance(context, sendOutid).UpDateMessageInfo(Msgid, Msgid, Receiveid, 2, SendTime, new MessageChatCallBack() {
                                @Override
                                public void DoChatDBSucceed(ImMsgBean imMsgBean) {
                                    L.e("imMsgBean+uploadFailure   ", imMsgBean.toString());
                                    ImMsgImgBean imMsgImgBean = new ImMsgImgBean();
                                    chattingListAdapter.changeData(imMsgImgBean.typeCast(imMsgBean), imMsgBean.getMsgid());
                                    scrollToBottom();
                                }

                                @Override
                                public void DoChatDBFault() {

                                }
                            });
                        }
                    }.start();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * listview滑动到底部
     */
    private void scrollToBottom() {
        lvChat.requestLayout();
        lvChat.post(new Runnable() {
            @Override
            public void run() {
                lvChat.setSelection(lvChat.getBottom());
            }
        });
    }

    @Override
    public void OnFuncPop(int height) {
        scrollToBottom();
    }

    @Override
    public void OnFuncClose() {

    }

    @Override
    public void resend(final String msgId, String msgContent, String msgType, String receiveid, String sendTime) {
        if (SocketListener.getInstance().getIsAuth()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String currentTime = sdf.format(new Date());
            updataMessage(msgId, msgId, receiveid, 0, currentTime);
        }

        switch (msgType.split("_")[0]) {
            case MSG_TEXT:
                socketSend.sendMsg(userMap, msgContent, msgType.split("_")[0], msgId, msgContent);
                break;
            case MSG_IMAGE:
                try {
                    JSONObject jsonObject = new JSONObject(msgContent);
                    if (jsonObject.getString("url").length() == 0) {
                        updataToAliOss(msgId, receiveid, sendTime, jsonObject, msgId);
                    } else {
                        socketSend.sendMsg(userMap, msgContent, msgType.split("_")[0], msgId, "[图片]");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case MSG_SHANGPIN:
                socketSend.sendMsg(userMap, msgContent, msgType.split("_")[0], msgId, "[商品]");
                break;
            case MSG_AUDIO:
                socketSend.sendMsg(userMap, msgContent, msgType.split("_")[0], msgId, "[语音]");
                break;
        }
    }

    /**
     * 发送消息结果回调
     *
     * @param result
     */
    @Override
    public void sendResult(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            int messageState = -1;
            // 判断是否发送成功记录状态 1：成功、2：失败
            if (jsonObject.getString("msg").equals("发送成功")) {
                messageState = 1;
            } else {
                messageState = 2;
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MToast.showToast("消息发送失败");
                    }
                });
            }
            final String msgIdFse = jsonObject.getString("msgIdFse");
            // 改变数据库消息
            updataMessage(jsonObject.getString("msgId"), msgIdFse, jsonObject.getString("receiveId"), messageState, jsonObject.getString("sendTime"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 改变数据库消息并刷新界面
    private void updataMessage(String msgId, final String msgIdFse, String receiveId, int messageState, String sendTime) {
        IMDBManager.getInstance(context, sendOutid).UpDateMessageInfo(msgId, msgIdFse, receiveId, messageState, sendTime, new MessageChatCallBack() {
            @Override
            public void DoChatDBSucceed(ImMsgBean imMsgBean) {
                switch (imMsgBean.getMsgtype().split("_")[0]) {
                    case MSG_TEXT:
                        chattingListAdapter.changeData(new ImMsgTextBean().typeCast(imMsgBean), msgIdFse);
                        break;
                    case MSG_IMAGE:
                        chattingListAdapter.changeData(new ImMsgImgBean().typeCast(imMsgBean), msgIdFse);
                        break;
                    case MSG_SHANGPIN:
                        chattingListAdapter.changeData(new ImMsgSPBean().typeCast(imMsgBean), msgIdFse);
                        break;
                    case MSG_AUDIO:
                        chattingListAdapter.changeData(new ImMsgRecoderBean().typeCast(imMsgBean), msgIdFse);
                        break;

                }
            }

            @Override
            public void DoChatDBFault() {

            }
        });
    }

    private ConversationBehaviorListener conversationBehaviorListener;
    private UserInfoProvider userInfoProvider;
    private SimpleApps simpleApps;
    private SendShangPinListener sendShangPinListener;

    public void setConversationBehaviorListener(ConversationBehaviorListener conversationBehaviorListener) {
        this.conversationBehaviorListener = conversationBehaviorListener;
    }

    public void setUserInfoProvider(UserInfoProvider userInfoProvider) {
        this.userInfoProvider = userInfoProvider;

    }

    public void setSendShangPinListener(SendShangPinListener sendShangPinListener) {
        this.sendShangPinListener = sendShangPinListener;
    }


    public void setSimpleApps(SimpleApps simpleApps) {
        this.simpleApps = simpleApps;
    }

    public interface ConversationBehaviorListener {
        boolean onUserPortraitClick(ImMsgBean imMsgBean, int position, ChatViewHolder holder);

        boolean onUserPortraitLongClick(ImMsgBean imMsgBean, int position, ChatViewHolder holder);

        boolean onMessageClick(ImMsgBean imMsgBean, int position, ChatViewHolder holder);

        boolean onMessageLongClick(ImMsgBean imMsgBean, int position, ChatViewHolder holder);
    }

    public interface UserInfoProvider {
        UserInfo getUserInfo(String receiveid);
    }

    public interface SendShangPinListener {
        void sendShangPin(Context context, Bundle bundle);
    }

    public interface SimpleApps {
        List<ExtendBean> addApps();

        void appsOnClick(int position);
    }

}

