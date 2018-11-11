package com.gloiot.hygoSupply.utlis;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.widge.MyDialogBuilder;
import com.zyd.wlwsdk.widge.PwdInputView;

import cn.qqtheme.framework.picker.AddressPicker;


/**
 * Created by hygo00 on 16/10/28.
 * Dialog工具类
 */

public class DialogUtlis {


    public static MyDialogBuilder myDialogBuilder;

    public static MyDialogBuilder getMyDialogBuilder() {
        return myDialogBuilder;
    }

    public static void dismissDialog() {
        myDialogBuilder.dismiss();
    }

    public static void dismissDialogNoAnimator() {
        myDialogBuilder.dismissNoAnimator();
    }

    public static void oneBtnNormal(final Context mContext, String Content) {
        oneBtnNormal(mContext, Content, null, true, null);
    }

    public static void oneBtnNormal(final Context mContext, String Content, String btntext) {
        oneBtnNormal(mContext, Content, btntext, true, null);
    }

    public static void oneBtnNormal(final Context mContext, String Content, String btntext, View.OnClickListener onClickListener) {
        oneBtnNormal(mContext, Content, btntext, true, onClickListener);
    }

    public static void oneBtnNormal(final Context mContext, String Content, View.OnClickListener onClickListener) {
        oneBtnNormal(mContext, Content, null, false, onClickListener);
    }

    public static void oneBtnNormal(final Context mContext, String Content, boolean cancelable, View.OnClickListener onClickListener) {
        oneBtnNormal(mContext, Content, null, cancelable, onClickListener);
    }

    /**
     * 普通提示1个按钮
     *
     * @param mContext
     * @param Content
     */
    public static void oneBtnNormal(final Context mContext, String Content, String btnText, boolean cancelable, View.OnClickListener onClickListener) {
        myDialogBuilder = MyDialogBuilder.getInstance(mContext);
        myDialogBuilder.setCancelable(cancelable);
        myDialogBuilder.withTitie("提示")
                .withContene(Content)
                .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss);
        if (onClickListener == null) {
            myDialogBuilder.setBtn1(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialogBuilder.dismiss();
                }
            });
        } else {
            myDialogBuilder.setBtn1(onClickListener);
        }
        if (btnText != null)
            myDialogBuilder.setBtn1Text(btnText);
        myDialogBuilder.show();
    }

    /**
     * 普通提示1个按钮
     *
     * @param mContext
     * @param Content
     * @param title
     * @param btnText
     * @param cancelable
     * @param onClickListener
     */
    public static void oneBtnNormal(final Context mContext, String title, String Content, String btnText, boolean cancelable, View.OnClickListener onClickListener) {
        myDialogBuilder = MyDialogBuilder.getInstance(mContext);
        myDialogBuilder.setCancelable(cancelable);
        myDialogBuilder.withTitie(title)
                .withContene(Content)
                .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss);
        if (onClickListener == null) {
            myDialogBuilder.setBtn1(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialogBuilder.dismiss();
                }
            });
        } else {
            myDialogBuilder.setBtn1(onClickListener);
        }
        if (btnText != null)
            myDialogBuilder.setBtn1Text(btnText);
        myDialogBuilder.show();
    }

    /**
     * 普通提示2个按钮
     *
     * @param mContext
     * @param Content
     */
    public static void twoBtnNormal(final Context mContext, String Content, View.OnClickListener onClickListener) {
        myDialogBuilder = MyDialogBuilder.getInstance(mContext);
        myDialogBuilder.withTitie("提示")
                .withContene(Content)
                .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                .setBtn2(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialogBuilder.dismiss();
                    }
                }, onClickListener).show();
    }

    /**
     * 特殊提示2个按钮
     *
     * @param mContext
     * @param Content
     */
    public static void twoBtnNormal(final Context mContext, String Content, String title, boolean cancelable, String btn1, String btn2, View.OnClickListener onClickListener1, View.OnClickListener onClickListener2) {
        myDialogBuilder = MyDialogBuilder.getInstance(mContext);
        myDialogBuilder.setCancelable(cancelable);
        myDialogBuilder.withTitie(title)
                .withContene(Content)
                .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                .setBtn2Text(btn1, btn2)
                .setBtn2(onClickListener1, onClickListener2).show();
    }


    /**
     * 单个输入框
     *
     * @param mContext
     * @param title
     */
    static EditText towBtnEditText;

    public static void towBtnEdit(final Context mContext, String title, View.OnClickListener onClickListener) {
        myDialogBuilder = MyDialogBuilder.getInstance(mContext);
        myDialogBuilder.withTitie(title)
                .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                .setCustomView(R.layout.dialog_son_oneedit)
                .setBtn2(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialogBuilder.dismiss();
                    }
                }, onClickListener).show();

        towBtnEditText = (EditText) (myDialogBuilder.getCustomView()).findViewById(R.id.dialog_et);
    }

    public static EditText getEditText() {
        return towBtnEditText;
    }

    /**
     * 文字居中单选
     *
     * @param mContext
     * @param title               标题
     * @param datas               数据 Stirng[]
     * @param singleChoiceOnClick item点击监听
     */
    public static void oneBtnSingleChoice(final Context mContext, String title, String[] datas, MyDialogBuilder.SingleChoiceOnItemClick singleChoiceOnClick) {
        myDialogBuilder = MyDialogBuilder.getInstance(mContext);
        myDialogBuilder.withTitie(title)
                .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                .setBtn1Bg(R.drawable.btn_dialog_left)
                .setBtn1Text("取消")
                .setSingleChoice(mContext, datas, R.layout.item_textcenter, new MyDialogBuilder.SingleChoiceConvert() {
                    @Override
                    public void convert(ViewHolder holder, String s) {
                        holder.setText(R.id.tv_item_center, s);
                    }
                }, singleChoiceOnClick)
                .setBtn1(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialogBuilder.dismiss();
                    }
                }).show();
    }

    /**
     * 文字居中单选
     *
     * @param mContext
     * @param title               标题
     * @param datas               数据 Stirng[]
     * @param singleChoiceOnClick item点击监听
     */
    public static void oneBtnSingleChoice(final Context mContext, String title, String[] datas, MyDialogBuilder.SingleChoiceOnItemClickWithPosition singleChoiceOnClick) {
        myDialogBuilder = MyDialogBuilder.getInstance(mContext);
        myDialogBuilder.withTitie(title)
                .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                .setSingleChoice(mContext, datas, R.layout.item_textcenter, new MyDialogBuilder.SingleChoiceConvert() {
                    @Override
                    public void convert(ViewHolder holder, String s) {
                        holder.setText(R.id.tv_item_center, s);
                    }
                }, singleChoiceOnClick)
                .setBtn1(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialogBuilder.dismiss();
                    }
                }).show();
    }


    public static void twoBtnNormal(final Context mContext, String Content, String btn1, String btn2, View.OnClickListener onClickListener) {
        twoBtnNormal(mContext, Content, -100, "提示", true, btn1, btn2, onClickListener, null);
    }

    /**
     * @param mContext         上下文
     * @param Content          内容
     * @param gravity          内容重心
     * @param title            标题
     * @param cancelable       是否点返回键销毁
     * @param btn1             按钮1文字
     * @param btn2             按钮2文字
     * @param onClickListener1 按钮1点击事件
     * @param onClickListener2 按钮2点击事件
     */
    public static void twoBtnNormal(final Context mContext, String Content, int gravity, String title, boolean cancelable, String btn1, String btn2, View.OnClickListener onClickListener1, View.OnClickListener onClickListener2) {
        myDialogBuilder = MyDialogBuilder.getInstance(mContext);
        myDialogBuilder.setCancelable(cancelable);
        myDialogBuilder.withTitie(title)
                .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss);
        if (gravity != -100) {
            myDialogBuilder.withContene(Content, gravity);
        } else {
            myDialogBuilder.withContene(Content);
        }
        if (btn1 != null && btn2 != null) {
            myDialogBuilder.setBtn2Text(btn1, btn2);
        }
        if (onClickListener1 != null && onClickListener2 != null) {
            myDialogBuilder.setBtn2(onClickListener1, onClickListener2);
        }
        if (onClickListener1 != null && onClickListener2 == null) {
            myDialogBuilder.setBtn2(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialogBuilder.dismiss();
                }
            }, onClickListener1);
        }
        myDialogBuilder.show();
    }

    /**
     * 滚桶单选
     *
     * @param mContext
     * @param title
     * @param datas
     * @param loopViewCallBack
     * @param onClickListener
     */
    public static void towBtnLoopView(final Context mContext, String title, String[] datas, int position, MyDialogBuilder.LoopViewCallBack loopViewCallBack, View.OnClickListener onClickListener) {
        myDialogBuilder = MyDialogBuilder.getInstance(mContext);
        myDialogBuilder.withTitie(title)
                .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                .setListViewSingle(mContext, datas, loopViewCallBack, position)
                .setBtn2(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialogBuilder.dismiss();
                    }
                }, onClickListener).show();
    }


    /**
     * 输入支付密码对话框
     *
     * @param mContext
     * @param money
     * @param passwordCallback
     */
    public static void oneBtnPwd(final Context mContext, String money, final PasswordCallback passwordCallback) {
        myDialogBuilder = MyDialogBuilder.getInstance(mContext);
        myDialogBuilder.withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                .setCustomView(R.layout.dialog_son_paypwd).show();

        final PwdInputView pv_paypwd = (PwdInputView) (myDialogBuilder.getCustomView()).findViewById(R.id.pv_paypwd);
        final TextView tv_title = (TextView) (myDialogBuilder.getCustomView()).findViewById(R.id.tv_title);
        final ImageView iv_close = (ImageView) (myDialogBuilder.getCustomView()).findViewById(R.id.iv_close);
        final TextView tv_paymoney = (TextView) (myDialogBuilder.getCustomView()).findViewById(R.id.tv_pay_money);

        tv_title.setText("请输入支付密码");
        tv_paymoney.setText(money);
        if (TextUtils.isEmpty(money))
            tv_paymoney.setVisibility(View.GONE);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialogBuilder.dismiss();
            }
        });

        //设置是否显示密码
        pv_paypwd.setDisplayPasswords(false);
        //设置方框的圆角度数
        pv_paypwd.setRadiusBg(0);

        //设置监听内部输入的字符
        pv_paypwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 6) {
                    passwordCallback.callback(s.toString());
                    myDialogBuilder.dismiss();
                }
            }
        });
    }

    // 输入支付密码对话框 回调
    public interface PasswordCallback {
        void callback(String data);
    }


    /**
     * 日期选择
     *
     * @param mContext
     * @param onClickListener
     */
    public static void towBtnDate(final Context mContext, View.OnClickListener onClickListener) {
        myDialogBuilder = MyDialogBuilder.getInstance(mContext);
        myDialogBuilder.withTitie("日期")
                .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                .setCustomDateView(R.layout.dialog_son_datepricker)
                .setBtn2(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialogBuilder.dismiss();
                    }
                }, onClickListener).show();
    }

    /**
     * 日期选择    附带最大时间
     *
     * @param mContext
     * @param onClickListener
     */
    public static void towBtnDate(final Context mContext, View.OnClickListener onClickListener, int month) {
        myDialogBuilder = MyDialogBuilder.getInstance(mContext);
        myDialogBuilder.withTitie("日期")
                .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                .setCustomDateView(R.layout.dialog_son_datepricker, month)
                .setBtn2(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialogBuilder.dismiss();
                    }
                }, onClickListener).show();
    }

    /**
     *
     * @param mContext
     * @param onClickListener
     * @param picker
     * @param title
     */
    public static void towBtnList(final Context mContext, View.OnClickListener onClickListener, AddressPicker picker, String title) {
        myDialogBuilder = MyDialogBuilder.getInstance(mContext);
        myDialogBuilder.withTitie(title)
                .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                .setCustomView(picker.getContentView())
                .setBtn2(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialogBuilder.dismiss();
                    }
                }, onClickListener).show();
    }


    /**
     * 我的二维码
     *
     * @param mContext
     */
    public static void myErWeiMa(final Context mContext, MyERWeiMaCallback myERWeiMaCallback) {
        myDialogBuilder = MyDialogBuilder.getInstance(mContext);
        myDialogBuilder.setDismissClickEveryWhere(true);
        myDialogBuilder
                .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                .setCustomView(R.layout.dialog_son_myerweima)
                .show();


        final TextView tv_name = (TextView) (myDialogBuilder.getCustomView()).findViewById(R.id.tv_name);
        final TextView tv_num = (TextView) (myDialogBuilder.getCustomView()).findViewById(R.id.tv_num);
        final ImageView iv_touxiang = (ImageView) (myDialogBuilder.getCustomView()).findViewById(R.id.iv_touxiang);
        final ImageView iv_ewm = (ImageView) (myDialogBuilder.getCustomView()).findViewById(R.id.iv_ewm);
        final ImageView iv_sex = (ImageView) (myDialogBuilder.getCustomView()).findViewById(R.id.iv_sex);

        myERWeiMaCallback.callback(tv_name, tv_num, iv_touxiang, iv_ewm, iv_sex);

    }

    // 我的二维码控件回调
    public interface MyERWeiMaCallback {
        void callback(TextView tvName, TextView tvNum, ImageView pic, ImageView ewm, ImageView sex);
    }


    //联动选择省市（WheelView）
    public static void towBtnPC(final Context context, View.OnClickListener onClickListener, String provinceStr, String cityStr) {
        myDialogBuilder = MyDialogBuilder.getInstance(context);
        myDialogBuilder.withTitie("选择省市")
                .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                .setPCWheelView(context, provinceStr, cityStr)
                .setBtn2(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialogBuilder.dismiss();
                    }
                }, onClickListener).show();
    }

    /**
     * 性别选择
     *
     * @param mContext
     * @param Content
     */
    static int sex = -1;

    public static void towBtnSex(final Context mContext, View.OnClickListener onClickListener) {
        myDialogBuilder = MyDialogBuilder.getInstance(mContext);
        myDialogBuilder.withTitie("性别")
                .withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                .setCustomView(R.layout.dialog_son_sex)
                .setBtn2(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialogBuilder.dismiss();
                        sex = -1;
                    }
                }, onClickListener).show();

        final TextView tvNan = (TextView) (myDialogBuilder.getCustomView()).findViewById(R.id.dialog_nan);
        final TextView tvNv = (TextView) (myDialogBuilder.getCustomView()).findViewById(R.id.dialog_nv);

        tvNan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvNan.setTextColor(Color.parseColor("#2b9ced"));
                tvNv.setTextColor(Color.parseColor("#333333"));
                tvNan.setBackgroundResource(R.drawable.sytle_dialog_sex2);
                tvNv.setBackgroundResource(R.drawable.sytle_dialog_sex1);
                sex = 0;
            }
        });


        tvNv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvNan.setTextColor(Color.parseColor("#333333"));
                tvNv.setTextColor(Color.parseColor("#2b9ced"));
                tvNan.setBackgroundResource(R.drawable.sytle_dialog_sex1);
                tvNv.setBackgroundResource(R.drawable.sytle_dialog_sex2);
                sex = 1;
            }
        });
    }

    // 返回选择的性别 @return 0-男，1-女
    public static int getSex() {
        return sex;
    }

    /**
     * 通知栏
     */

    public static void InForm(final Context mContext, String Content, String btnText, boolean cancelable, View.OnClickListener onClickListener) {
        myDialogBuilder = MyDialogBuilder.getInstance(mContext);
        myDialogBuilder.setCancelable(cancelable);
        myDialogBuilder.setDismissClickEveryWhere(true);
        myDialogBuilder.withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                .setCustomView(R.layout.dialog_inform).show();
        if (onClickListener == null) {
            myDialogBuilder.setBtn1(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialogBuilder.dismiss();
                }
            });
        } else {
            myDialogBuilder.setBtn1(onClickListener);
        }
        if (btnText != null)
            myDialogBuilder.setBtn1Text(btnText);
        myDialogBuilder.show();
        final TextView tv_inform_content = (TextView) (myDialogBuilder.getCustomView()).findViewById(R.id.tv_inform_content);
        final TextView tv_inform_title = (TextView) (myDialogBuilder.getCustomView()).findViewById(R.id.tv_inform_title);
        final ImageView im_inform_cancel = (ImageView) (myDialogBuilder.getCustomView()).findViewById(R.id.im_inform_cancel);
        im_inform_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialogBuilder.dismiss();
            }
        });
        tv_inform_content.setText(Content);
        tv_inform_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                return;
            }
        });
        tv_inform_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                return;
            }
        });
    }


    /**
     * 输入支付密码对话框(用于身份验证，中间的提示信息字体较小一些)
     *
     * @param mContext
     * @param message
     * @param passwordCallback
     */
    public static void oneBtnPwd1(final Context mContext, String message, final PasswordCallback passwordCallback) {
        myDialogBuilder = MyDialogBuilder.getInstance(mContext);
        myDialogBuilder.withEffects(MyDialogBuilder.SlideTop, MyDialogBuilder.SlideTopDismiss)
                .setCustomView(R.layout.dialog_son_paypwd_yanzheng).show();

        final PwdInputView pv_paypwd = (PwdInputView) (myDialogBuilder.getCustomView()).findViewById(R.id.pv_paypwd);
        final TextView tv_title = (TextView) (myDialogBuilder.getCustomView()).findViewById(R.id.tv_title);
        final ImageView iv_close = (ImageView) (myDialogBuilder.getCustomView()).findViewById(R.id.iv_close);
        final TextView tv_message = (TextView) (myDialogBuilder.getCustomView()).findViewById(R.id.tv_message);

        tv_title.setText("请输入支付密码");
        tv_message.setText(message);
        if (TextUtils.isEmpty(message))
            tv_message.setVisibility(View.GONE);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialogBuilder.dismiss();
            }
        });

        //设置是否显示密码
        pv_paypwd.setDisplayPasswords(false);
        //设置方框的圆角度数
        pv_paypwd.setRadiusBg(0);

        //设置监听内部输入的字符
        pv_paypwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 6) {
                    passwordCallback.callback(s.toString());
                    myDialogBuilder.dismiss();
                }
            }
        });
    }
}
