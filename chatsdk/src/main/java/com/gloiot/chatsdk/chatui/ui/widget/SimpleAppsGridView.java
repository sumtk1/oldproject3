package com.gloiot.chatsdk.chatui.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.gloiot.chatsdk.R;
import com.gloiot.chatsdk.chatui.keyboard.extend.ExtendAdapter;
import com.gloiot.chatsdk.chatui.keyboard.extend.ExtendBean;

import java.util.ArrayList;
import java.util.List;


/**
 * 加号弹出的内容
 */
public class SimpleAppsGridView extends RelativeLayout {

    protected View view;
    private GridView gv_apps;
    private ArrayList<ExtendBean> mExtendBeanList;
    public static final int TGA_APPS_IMAGE = 0;     // 图片
    public static final int TGA_APPS_CAMERA = 1;    // 拍照

    public SimpleAppsGridView(Context context) {
        this(context, null);
    }

    public SimpleAppsGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.view_apps, this);
        init();
    }

    protected void init() {
        gv_apps = (GridView) view.findViewById(R.id.gv_apps);
        mExtendBeanList = new ArrayList<>();
        mExtendBeanList.add(new ExtendBean(R.drawable.chat_image_bg, "图片"));
        mExtendBeanList.add(new ExtendBean(R.drawable.chat_camera_bg, "拍照"));
//        mExtendBeanList.add(new ExtendBean(R.drawable.chat_transferpoints_bg, "转让积分"));
//        mExtendBeanList.add(new ExtendBean(R.drawable.chat_redpacket_bg, "红包"));
//        mExtendBeanList.add(new ExtendBean(R.drawable.chat_location_bg, "位置"));
        mExtendBeanList.add(null);
        mExtendBeanList.add(null);
        mExtendBeanList.add(null);
        ExtendAdapter adapter = new ExtendAdapter(getContext(), mExtendBeanList);
        gv_apps.setAdapter(adapter);


        addApps = new AddApps() {
            @Override
            public void add(List<ExtendBean> list) {
                mExtendBeanList.clear();
                mExtendBeanList.add(new ExtendBean(R.drawable.chat_image_bg, "图片"));
                mExtendBeanList.add(new ExtendBean(R.drawable.chat_camera_bg, "拍照"));
                for (int i = 0; i < list.size(); i++) {
                    mExtendBeanList.add(list.get(i));
                }
                do {
                    mExtendBeanList.add(null);
                } while (mExtendBeanList.size() <= 5);
                ExtendAdapter adapter = new ExtendAdapter(getContext(), mExtendBeanList);
                gv_apps.setAdapter(adapter);
            }
        };
    }

    public void setAppsOnItemClick(AdapterView.OnItemClickListener onItemClick) {
        gv_apps.setOnItemClickListener(onItemClick);
    }

    private AddApps addApps;

    public AddApps getAddApps() {
        return addApps;
    }

    public interface AddApps {
        void add(List<ExtendBean> list);
    }
}
