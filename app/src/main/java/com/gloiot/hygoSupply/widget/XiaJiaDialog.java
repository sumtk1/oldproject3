package com.gloiot.hygoSupply.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.utlis.ScreenUtil;
import com.gloiot.hygoSupply.utlis.ToolUtil;

/**
 * Created by Ad on 2017/5/11.
 */

public class XiaJiaDialog extends Dialog {

    private Context mContext;
    private ClickListenerInterface clickListenerInterface;

    public XiaJiaDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }
    public XiaJiaDialog(@NonNull Context context, int theme) {
        super(context,theme);
        this.mContext = context;
    }
    public XiaJiaDialog(@NonNull Context context, int theme, ClickListenerInterface clickListenerInterface) {
        super(context,theme);
        this.mContext = context;
        this.clickListenerInterface = clickListenerInterface;
    }

    public interface ClickListenerInterface {
         void cancle();

         void doConfirm(String xiajia_yuanyin);

         void addEditContentListten(String content, int length);
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

    }
    EditText ed = null;
    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_xiajia, null);
        setContentView(view);
        final TextView zishu = (TextView) view.findViewById(R.id.xiajia_zishu_txt);
        view.findViewById(R.id.xiajia_cancle_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListenerInterface.cancle();
            }
        });
        view.findViewById(R.id.xiajia_sure_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListenerInterface.doConfirm(ed.getText().toString());
            }
        });
        view.findViewById(R.id.xiajia_sure_txt);
        ed = ((EditText)view.findViewById(R.id.xiajia_input_ed));
        ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                zishu.setText(s.length() + "/50" );
                clickListenerInterface.addEditContentListten(String.valueOf(s),s.length());
            }
        });


//        Window dialogWindow = getWindow();
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        DisplayMetrics d = mContext.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
//        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        lp.alpha = 0.7f; // 透明度
//        dialogWindow.setGravity(Gravity.CENTER);
//        dialogWindow.setAttributes(lp);

        // 设置dialog 宽度
        WindowManager.LayoutParams wlp = this.getWindow().getAttributes();
        ToolUtil.setParams(wlp, (Activity) mContext);
        ScreenUtil.setDialogMetrics(this, mContext);
    }


    public void setClicklistener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }

    public void clearEdidtext () {
        if (ed != null) {
            ed.getText().clear();
        }
    }

}
