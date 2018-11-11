package com.gloiot.hygoSupply.ui.activity.zhexiantufragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.bean.TiemData;
import com.gloiot.hygoSupply.ui.activity.BaseFragment;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.jn.chart.charts.LineChart;
import com.jn.chart.data.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import butterknife.ButterKnife;

/**
 * @Author: pyz
 * @Package: com.pyz.myproject
 * @Description: TODO
 * @Project: JNChartDemo
 * @Date: 2016/7/4 15:33
 */
public class Fragment2 extends BaseFragment {
    private LineChart mLineChart;
    private Context context;
    //y轴数据集
    private ArrayList<String> yValues;
    //设置y轴的数据
    private ArrayList<Entry> yValue;
    //设置x轴的数据
    private ArrayList<String> xValues;
    private ArrayList<String> x;
    //时间集
    private ArrayList<TiemData> timeListAll;
    private ArrayList<TiemData> timeList;
    private RelativeLayout rl_wushuju;
    private TextView tv_biaoti;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1_layout, container, false);
        ButterKnife.bind(this, view);
        initComponent(view);
        initData();

        return view;
    }

    public void initData() {
        //设置图表的描述
        mLineChart.setDescription("");
        context = getActivity();

        String dianPuZhuantai = preferences.getString(ConstantUtils.SP_YOUWUDIANPU, "");
        if (dianPuZhuantai.equals("是")) {
            String zhanghao = preferences.getString(ConstantUtils.SP_MYID, "");
            requestHandleArrayList.add(requestAction.shop_sta_visitor(this, zhanghao));
        } else {
            rl_wushuju.setVisibility(View.VISIBLE);
        }
    }

    public void initComponent(View view) {
        mLineChart = (LineChart) view.findViewById(R.id.lineChart);
        rl_wushuju = (RelativeLayout) view.findViewById(R.id.rl_wushuju);
        tv_biaoti = (TextView) view.findViewById(R.id.tv_biaoti);
    }

    @Override
    public void onResume() {
        super.onResume();
        String dianPuZhuantai = preferences.getString(ConstantUtils.SP_YOUWUDIANPU, "");
        if (dianPuZhuantai.equals("是")) {
            String zhanghao = preferences.getString(ConstantUtils.SP_MYID, "");
            requestHandleArrayList.add(requestAction.shop_sta_visitor(this, zhanghao));
        } else {
            rl_wushuju.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_sta_visitor:
                Log.e("TAG_shop_sta_visitor", response.toString());
                if (response.getString("状态").equals("成功")) {
                    xValues = new ArrayList<>();
                    yValues = new ArrayList<>();
                    timeListAll = new ArrayList<>();
                    timeList = new ArrayList<>();
                    JSONArray jsonArray = response.getJSONArray("列表");
                    if (jsonArray.length() > 0) {
                        rl_wushuju.setVisibility(View.GONE);
                        tv_biaoti.setText("最近15天的访客统计");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            TiemData tiemData = new TiemData();
                            JSONObject jsonObj = (JSONObject) jsonArray.get(i);
                            String riqi = jsonObj.getString("日期");
                            tiemData.setRiqi(riqi);
                            try {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                Date dt2 = sdf.parse(riqi);
                                int time = (int) (dt2.getTime() / 1000);
                                tiemData.setData(time);
                                tiemData.setNum(jsonObj.getString("值"));
                                timeListAll.add(tiemData);

                                Collections.sort(timeListAll);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        for (int i = 0; i < timeListAll.size(); i++) {
                            if (i > timeListAll.size() - 16) {
                                timeList.add(timeListAll.get(i));
                            }
                        }
                        for (int i = 0; i < timeList.size(); i++) {
                            if (i == timeList.size() - 1) {
                                xValues.add(timeList.get(i).getRiqi().substring(timeList.get(i).getRiqi().length() - 2, timeList.get(i).getRiqi().length()) + "号");
                            } else {
                                xValues.add(timeList.get(i).getRiqi().substring(timeList.get(i).getRiqi().length() - 2, timeList.get(i).getRiqi().length()));
                            }
                            Log.e("096766690", timeList.get(i).getRiqi() + "");
                            yValues.add(timeList.get(i).getNum());
                        }


                        //设置y轴的数据
                        if (yValues.size() > 0) {
                            yValue = new ArrayList<>();
                            for (int i = 0; i < yValues.size(); i++) {
                                yValue.add(new Entry(Float.parseFloat(yValues.get(i)), i));
                            }
                        }
                        mLineChart.setPinchZoom(false);
                        //设置折线的名称
                        //LineChartManager.setLineName("当月值");
//                    mLineChart.zoom(30f / 9f, 1f, 0, 0);
                        //移动到x轴的什么位置
//                    if(xValues.size()>9) {
//                        mLineChart.moveViewToX(xValues.size() - 9);
//                    }
//                    mLineChart.setVisibleXRange(1,17);
                        //创建一条折线的图表
//                        LineChartManager.setLineName("前16天值");
                        LineChartManager.initSingleLineChart(context, mLineChart, xValues, yValue, 1);
                    } else {
                        rl_wushuju.setVisibility(View.VISIBLE);
                    }
                }
                break;
        }
    }
}
