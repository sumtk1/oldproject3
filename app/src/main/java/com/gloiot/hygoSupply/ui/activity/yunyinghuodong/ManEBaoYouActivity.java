package com.gloiot.hygoSupply.ui.activity.yunyinghuodong;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.utlis.DialogUtlis;
import com.gloiot.hygoSupply.utlis.MToastUtils;
import com.gloiot.hygoSupply.utlis.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zxl on 2017/6/20.
 */

public class ManEBaoYouActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.tv_meby_shixiaoriqi) TextView tv_meby_shixiaoriqi;
    @Bind(R.id.tv_meby_shengxiaoshijian) TextView tv_meby_shengxiaoshijian;
    @Bind(R.id.tv_meby_bubaoyoudiqu) TextView tv_meby_bubaoyoudiqu;
    @Bind(R.id.et_meby_standard) EditText et_meby_standard;
    @Bind(R.id.iv_meby_wutiaojian_xuanze) ImageView iv_meby_wutiaojian_xuanze;
    @Bind(R.id.iv_meby_mebyxuanze) ImageView iv_meby_mebyxuanze;
    @Bind(R.id.iv_meby_guanbicigongneng) ImageView iv_meby_guanbicigongneng;
    @Bind(R.id.swh_meby_guanbicigongneng) Switch swh_meby_guanbicigongneng;

    private StringBuffer sb_bubaoyouquyu = null;
    private List<XuanZeQuYuBean> selected_list = null;
    HashMap<String, Object> data_map = new HashMap<>();
    private String shengxiao_shijian = "";
    private String shixiao_shijian = "";
    private String wutiaojianshiyong = "否";
    private String guanbicigongneng = "否";
    private String phone = "";
    private String bubaoyoudiqu;

    @Override
    public int initResource() {
        return R.layout.activity_manebaoyou;
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar(this, true, "满额包邮", "");
        sb_bubaoyouquyu = new StringBuffer();
        phone = SharedPreferencesUtils.getInstance().getSharedPreferences().getString(ConstantUtils.SP_ZHANGHAO, "");
        data_map.put("是否关闭","是");
        data_map.put("账号", phone);
        data_map.put("随机码", ConstantUtils.random);
        data_map.put("使用条件", "0");
        addMEBYTextChangedListener();
        wuTiaoJianShiYongSetStatus();
        getSwitchStatus();
        zanShiGuanBiCiGoNengSetStatus();
        requestHandleArrayList.add(requestAction.shop_wl_Freeship_query(this
                , SharedPreferencesUtils.getInstance().getSharedPreferences().getString(ConstantUtils.SP_ZHANGHAO, "")));
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_wl_Freeship:
                MToastUtils.showToast("运费 ");
                finish();
                break;
            case ConstantUtils.TAG_shop_wl_Freeship_query:
                if (response != null) {
                        String shiyongtiaojian = response.getString("使用条件");
                        bubaoyoudiqu = response.getString("不包邮地区");
//                        bubaoyoudiqu = bubaoyoudiqu.replace("\"","");
//                        bubaoyoudiqu = bubaoyoudiqu.replace("[","");
//                        bubaoyoudiqu = bubaoyoudiqu.replace("]","");
                        bubaoyoudiqu = bubaoyoudiqu.replace(" ","");
                        shengxiao_shijian = response.getString("生效时间");
                        shixiao_shijian = response.getString("失效时间");
                        guanbicigongneng = response.getString("是否关闭");
                        if (!TextUtils.isEmpty(guanbicigongneng)) {
                            zanShiGuanBiCiGoNengSetStatus();
                        }
                        tv_meby_bubaoyoudiqu.setText(bubaoyoudiqu);
                        tv_meby_shengxiaoshijian.setText(shengxiao_shijian);
                        tv_meby_shixiaoriqi.setText(shixiao_shijian);
                        if (TextUtils.isEmpty(shiyongtiaojian) || "0.00".equals(shiyongtiaojian) || "0".equals(shiyongtiaojian)) {
                            data_map.put("使用条件", "0");
                            wutiaojianshiyong = "是";
                        } else {
                            et_meby_standard.setText(shiyongtiaojian);
                            data_map.put("使用条件", shiyongtiaojian);
                            wutiaojianshiyong = "否";
                        }
                        wuTiaoJianShiYongSetStatus();
                        data_map.put("不包邮地区", bubaoyoudiqu);
                        data_map.put("生效时间", shengxiao_shijian);
                        data_map.put("失效时间", shixiao_shijian);
                        data_map.put("是否关闭", guanbicigongneng);
                }
                break;

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @OnClick({R.id.rl_meby_bubaoyoudiqu, R.id.btn_meby_queding, R.id.rl_meby_shengxiaoshijian, R.id.rl_meby_shixiao, R.id.iv_meby_wutiaojian_xuanze
            , R.id.iv_meby_mebyxuanze,})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_meby_bubaoyoudiqu ://不要运费地区
                Intent intent = new Intent(this,XuanZeQuYuActivity.class);
                intent.putExtra("selected_list", (Serializable) selected_list);
                startActivityForResult(intent, 0);
                break;
            case R.id.btn_meby_queding:
                if ("否".equals(wutiaojianshiyong)) {
                    data_map.put("使用条件", et_meby_standard.getText().toString().trim());
                } else {
                    data_map.put("使用条件", "0");
                }
                data_map.put("是否关闭", guanbicigongneng);
                requestHandleArrayList.add(requestAction.shop_wl_Freeship(this,data_map));
                break;
            case R.id.rl_meby_shengxiaoshijian :
                DialogUtlis.towBtnDate(mContext, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePicker datePicker = (DatePicker) DialogUtlis.myDialogBuilder.getCustomView().findViewById(R.id.datePicker);
                        int month = datePicker.getMonth() + 1;
                        shengxiao_shijian = datePicker.getYear() + "-" + month + "-" + datePicker.getDayOfMonth();
                        tv_meby_shengxiaoshijian.setText(shengxiao_shijian);
                        data_map.put("生效时间",shengxiao_shijian);
                        DialogUtlis.myDialogBuilder.dismiss();
                    }
                });
                break;
            case R.id.rl_meby_shixiao :
                DialogUtlis.towBtnDate(mContext, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePicker datePicker = (DatePicker) DialogUtlis.myDialogBuilder.getCustomView().findViewById(R.id.datePicker);
                        int month = datePicker.getMonth() + 1;
                        try {
                            String time = String.valueOf(datePicker.getYear()+"" + month + "" + datePicker.getDayOfMonth());
                            if (biaojiShijian(time)) {
                                shixiao_shijian = datePicker.getYear() + "-" + month + "-" + datePicker.getDayOfMonth();
                                tv_meby_shixiaoriqi.setText(shixiao_shijian);
                                data_map.put("失效时间",shixiao_shijian);
                            }
                            DialogUtlis.myDialogBuilder.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
            case R.id.iv_meby_mebyxuanze :
                updateWuTiaoJianStatus();
                break;
            case R.id.iv_meby_wutiaojian_xuanze :
                updateWuTiaoJianStatus();
                break;
        }
    }

    private boolean biaojiShijian(String shixiaotime) {
        int shixiao_time = Integer.parseInt(shixiaotime);
        if (shixiao_time - Integer.parseInt(shengxiao_shijian.replace("-","")) > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void updateWuTiaoJianStatus () {
        if ("否".equals(wutiaojianshiyong))
            wutiaojianshiyong = "是";
        else
            wutiaojianshiyong = "否";
        wuTiaoJianShiYongSetStatus();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode) {
            case 0:
                if (data == null) return;
                if (selected_list == null) {
                    selected_list = new ArrayList<>();
                }
                selected_list.clear();
                List<XuanZeQuYuBean> list = (List<XuanZeQuYuBean>) data.getSerializableExtra("selected_list") ;
                if (list != null) {
                    selected_list.addAll(list);
                }
                String bubaoyoudiqu = sbBbydqToString();
                data_map.put("不包邮地区", "[" + bubaoyoudiqu + "]");
                if (TextUtils.isEmpty(bubaoyoudiqu)) {
                    tv_meby_bubaoyoudiqu.setText(""); //bubaoyoudiqu
                } else {
                    tv_meby_bubaoyoudiqu.setText("已选择"); //bubaoyoudiqu
                }
                break;

        }
    }

    //满额包邮的Stringbuffer 转String
    public String sbBbydqToString () {
        try {
            sb_bubaoyouquyu.setLength(0);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        if (selected_list.size() == 0) {
            return "";
        }
        for (int i = 0 ; i < selected_list.size() ; i ++ ) {
            if (i == (selected_list.size() - 1)) {
                sb_bubaoyouquyu.append(selected_list.get(i).getDiming());
            } else {
                sb_bubaoyouquyu.append(selected_list.get(i).getDiming()).append(",");
            }
        }
        return sb_bubaoyouquyu.toString().trim();
    }

    public void addMEBYTextChangedListener () {
        et_meby_standard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                data_map.put("使用条件", et_meby_standard.getText().toString().trim());
            }
        });
    }

    public void getSwitchStatus () {
        swh_meby_guanbicigongneng.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    guanbicigongneng = "是";
                } else {
                    guanbicigongneng = "否";
                }
            }
        });

    }

    public void zanShiGuanBiCiGoNengSetStatus () {
        if ("否".equals(guanbicigongneng))
            swh_meby_guanbicigongneng.setChecked(false);
        else
            swh_meby_guanbicigongneng.setChecked(true);
    }

    public void wuTiaoJianShiYongSetStatus () {
        if ("否".equals(wutiaojianshiyong)) {
            iv_meby_wutiaojian_xuanze.setImageResource(R.mipmap.ic_baoyou_wendianji);
            iv_meby_mebyxuanze.setImageResource(R.mipmap.dianji);
            et_meby_standard.setEnabled(true);
        } else {
            iv_meby_wutiaojian_xuanze.setImageResource(R.mipmap.dianji);
            iv_meby_mebyxuanze.setImageResource(R.mipmap.ic_baoyou_wendianji);
            et_meby_standard.setEnabled(false);
            et_meby_standard.getText().clear();
        }
    }

}

