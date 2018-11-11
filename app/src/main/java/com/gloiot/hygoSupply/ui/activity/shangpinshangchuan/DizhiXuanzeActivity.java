package com.gloiot.hygoSupply.ui.activity.shangpinshangchuan;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.abslistview.CommonAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DizhiXuanzeActivity extends BaseActivity implements View.OnClickListener {
    //一级区域（例：省）
    private TextView tv_firstfield;
    //二级区域（例：市）
    private TextView tv_secondfield;
    //显示三级地址的列表
    private ListView lv_dizhileibiao;
    //当前所在的区域级别(0:省,1:市，2：区)
    private int currentField;
    //返回按钮
    private ImageView iv_back;
    private CommonAdapter dizhiAdapter;
    private List<String[]> dizhiList;
    private List<String[]> shengList;
    private List<String[]> shiList;
    private String phone;
    private String[] dizhiInfoFinal;
    private String[] dizhiInfoBack;

    @Override
    public int initResource() {
        return R.layout.activity_dizhi_xuanze;
    }

    @Override
    public void initData() {
        tv_firstfield = (TextView) findViewById(R.id.tv_dizhi_xuanze_firstfield);
        tv_secondfield = (TextView) findViewById(R.id.tv_dizhi_xuanze_secondfield);
        lv_dizhileibiao = (ListView) findViewById(R.id.lv_dizhi_liebiao);
        iv_back = (ImageView) findViewById(R.id.iv_dizhi_liebiao_back);

        dizhiInfoBack = new String[4];
        shiList = new ArrayList<>();
        shengList = new ArrayList<>();
        dizhiList = new ArrayList<>();
        iv_back.setOnClickListener(this);
        phone = preferences.getString(ConstantUtils.SP_MYID, "");
        dizhiAdapter = new CommonAdapter<String[]>(DizhiXuanzeActivity.this, R.layout.item_dizhi_xuanze_liebiao, dizhiList) {
            @Override
            public void convert(final ViewHolder holder, String[] strings) {
                holder.setText(R.id.tv_dizhi_item, strings[1]);
            }
        };
        lv_dizhileibiao.setAdapter(dizhiAdapter);
        lv_dizhileibiao.setDividerHeight(1);
        lv_dizhileibiao.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentField == 0) {//省级
                    dizhiInfoFinal = dizhiList.get(position);
                    dizhiInfoBack[1] = dizhiInfoFinal[0];
                    requestHandleArrayList.add(requestAction.getShi(DizhiXuanzeActivity.this, phone, dizhiList.get(position)[0]));
                } else if (currentField == 1) {//市级
                    dizhiInfoFinal = dizhiList.get(position);
                    dizhiInfoBack[2] = dizhiInfoFinal[0];
                    requestHandleArrayList.add(requestAction.getQu(DizhiXuanzeActivity.this, phone, dizhiList.get(position)[0]));
                } else {//区级
                    dizhiInfoFinal = dizhiList.get(position);
                    dizhiInfoBack[0] = dizhiInfoFinal[1];
                    dizhiInfoBack[3] = dizhiInfoFinal[0];
                    Intent intent = new Intent();
                    intent.putExtra("dizhiInfoFinal", dizhiInfoBack);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
        requestHandleArrayList.add(requestAction.getSheng(this, phone));
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        dizhiList.clear();
        switch (requestTag) {
            case ConstantUtils.TAG_shop_sj_sheng:
                shengList.clear();
                tv_secondfield.setText("省");
                tv_firstfield.setText("");
                JSONArray array = response.getJSONArray("省列表");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject sheng = array.getJSONObject(i);
                    String[] shengInfo = new String[2];
                    shengInfo[0] = sheng.getString("id");
                    shengInfo[1] = sheng.getString("省份");
                    dizhiList.add(shengInfo);

                }
                shengList.addAll(dizhiList);
                dizhiAdapter.notifyDataSetChanged();
                currentField = 0;

                break;
            case ConstantUtils.TAG_shop_sj_shi:
                shiList.clear();
                tv_secondfield.setText("市");
                tv_firstfield.setText("省");
                JSONArray array1 = response.getJSONArray("市列表");
                for (int i = 0; i < array1.length(); i++) {
                    JSONObject sheng = array1.getJSONObject(i);
                    String[] shiInfo = new String[2];
                    shiInfo[0] = sheng.getString("id");
                    shiInfo[1] = sheng.getString("城市");
                    dizhiList.add(shiInfo);
                }
                shiList.addAll(dizhiList);
                dizhiAdapter.notifyDataSetChanged();
                currentField = 1;

                break;
            case ConstantUtils.TAG_shop_sj_qu:
                try {
                    JSONArray array2 = response.getJSONArray("区列表");

                    for (int i = 0; i < array2.length(); i++) {
                        JSONObject sheng = array2.getJSONObject(i);
                        String[] quInfo = new String[2];
                        quInfo[0] = sheng.getString("id");
                        quInfo[1] = sheng.getString("区");
                        dizhiList.add(quInfo);
                        tv_secondfield.setText("区");
                        tv_firstfield.setText("市");
                    }
                } catch (JSONException e) {
                    Intent intent = new Intent();
                    dizhiInfoBack[0] = dizhiInfoFinal[1];
                    intent.putExtra("dizhiInfoFinal", dizhiInfoBack);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                dizhiAdapter.notifyDataSetChanged();
                currentField = 2;
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_dizhi_liebiao_back:
                if (currentField == 0) {//省级
                    finish();
                } else if (currentField == 1) {//市级
                    tv_secondfield.setText("省");
                    tv_firstfield.setText("");
                    dizhiList.clear();
                    dizhiList.addAll(shengList);
                    dizhiAdapter.notifyDataSetChanged();
                    currentField--;
                } else {//区级
                    tv_secondfield.setText("市");
                    tv_firstfield.setText("省");
                    dizhiList.clear();
                    dizhiList.addAll(shiList);
                    dizhiAdapter.notifyDataSetChanged();
                    currentField--;
                }
                break;
        }
    }
}
