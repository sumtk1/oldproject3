package com.gloiot.hygoSupply.ui.activity.zhexiantufragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseFragment;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.jn.chart.charts.PieChart;
import com.jn.chart.components.Legend;
import com.jn.chart.data.Entry;
import com.jn.chart.data.PieData;
import com.jn.chart.data.PieDataSet;
import com.jn.chart.formatter.PercentFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * @Author: pyz
 * @Package: com.pyz.myproject
 * @Description: TODO
 * @Project: JNChartDemo
 * @Date: 2016/7/4 16:05
 */
public class Fragment4 extends BaseFragment {

    private PieChart mChart;
    private ArrayList<Float> shangPingTongjiNum;
    private ArrayList<String> shangPingName;
    private ArrayList<Entry> yValues;  //yVals用来表示封装每个饼块的实际数据
    private ArrayList<String> xValues;  //xVals用来表示每个饼块上的内容
    private ArrayList<Integer> colors = new ArrayList<Integer>();
    private RelativeLayout rl_wushuju;
    private float[] numList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment4_layout, container, false);
        ButterKnife.bind(this, view);
        initComponent(view);
        initData();

        return view;
    }

    public void initData() {
        String dianPuZhuantai = preferences.getString(ConstantUtils.SP_YOUWUDIANPU, "");
        if (dianPuZhuantai.equals("是")) {
            String zhanghao = preferences.getString(ConstantUtils.SP_MYID, "");
            requestHandleArrayList.add(requestAction.shop_sta_shop(this, zhanghao));
        } else {
            rl_wushuju.setVisibility(View.VISIBLE);
        }
        // 饼图颜色
        colors.add(Color.rgb(255, 125, 86));
        colors.add(Color.rgb(255, 170, 86));
        colors.add(Color.rgb(253, 212, 85));
        colors.add(Color.rgb(132, 233, 188));
        colors.add(Color.rgb(147, 210, 255));
        colors.add(Color.rgb(180, 228, 246));
        colors.add(Color.rgb(207, 241, 253));
        colors.add(Color.rgb(238, 238, 238));
    }

    public void initComponent(View view) {
        mChart = (PieChart) view.findViewById(R.id.picChart);
        rl_wushuju = (RelativeLayout) view.findViewById(R.id.rl_wushuju);
    }

    @Override
    public void onResume() {
        super.onResume();
        String dianPuZhuantai = preferences.getString(ConstantUtils.SP_YOUWUDIANPU, "");
        if (dianPuZhuantai.equals("是")) {
            String zhanghao = preferences.getString(ConstantUtils.SP_MYID, "");
            requestHandleArrayList.add(requestAction.shop_sta_shop(this, zhanghao));
        } else {
            rl_wushuju.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_sta_shop:
                if (response.getString("状态").equals("成功")) {
                    shangPingTongjiNum = new ArrayList<>();
                    shangPingName = new ArrayList<>();
                    yValues = new ArrayList<Entry>();
                    xValues = new ArrayList<String>();

                    JSONArray jsonArray = response.getJSONArray("列表");
                    int num = jsonArray.length();
                    numList = new float[num];
                    if (num > 0) {
                        rl_wushuju.setVisibility(View.GONE);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObj = (JSONObject) jsonArray.get(i);
                            String zhi = jsonObj.getString("值");
                            String s = zhi.replace("%", "");
                            float q = Float.parseFloat(s);
                            numList[i] = q;
                            xValues.add(jsonObj.getString("商品名称"));
                        }
//                        if (numList.length == 8 && xValues.size() == 8 && "其他".equals(xValues.get(7))) {
//                            float sum = 0;
//                            for (int i = 0; i < numList.length - 1; i++) {
//                                sum += numList[i];
//                            }
//                            numList[7] = 1f - sum;
//                        }
                        float temp;
                        for (int i = 0; i < numList.length; i++) {
                            for (int j = i + 1; j < numList.length; j++) {
                                if (numList[i] < numList[j]) {
                                    temp = numList[i];
                                    numList[i] = numList[j];
                                    numList[j] = temp;  // 两个数交换位置
                                }
                            }
                        }
                        for (int i = 0; i < numList.length; i++) {
                            yValues.add(new Entry(numList[i], i));
                        }
                        PieData mPieData = getPieData(num, 100);
                        showChart(mChart, mPieData);
                    } else {
                        rl_wushuju.setVisibility(View.VISIBLE);
                    }
                }
                break;
        }
    }

    private void showChart(PieChart pieChart, PieData pieData) {

        pieChart.setHoleRadius(50f);  //半径
//        pieChart.setTransparentCircleRadius(20f); // 半透明圈
        //pieChart.setHoleRadius(0)  //实心圆

        pieChart.setDescription("昨日商品销售分布图");

        // mChart.setDrawYValues(true);
//        pieChart.setDrawCenterText(true);  //饼状图中间可以添加文字

        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);  //显示成百分比

//        pieChart.setRotationAngle(90); // 初始旋转角度

        // draws the corresponding description value into the slice
        // mChart.setDrawXValues(true);

        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true); // 可以手动旋转

        // display percentage values
        pieChart.setUsePercentValues(true);  //显示成百分比
        pieChart.setDrawSliceText(false);
        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
//      mChart.setOnChartValueSelectedListener(this);
        // mChart.setTouchEnabled(false);

//      mChart.setOnAnimationListener(this);

//        pieChart.setCenterText("Quarterly Revenue");  //饼状图中间的文字

        //设置数据
        pieChart.setData(pieData);
        // undo all highlights
        pieChart.highlightValues(null);
        pieChart.invalidate();

        Legend mLegend = pieChart.getLegend();  //设置比例图
//        mLegend.setForm(Legend.LegendForm.CIRCLE);  //设置比例图的形状，默认是方形
        mLegend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
        mLegend.setXEntrySpace(3f);
        mLegend.setYEntrySpace(0f);
//        mLegend.setYOffset(0f);
//        mLegend.setFormSize(7f);
        pieChart.animateXY(1000, 1000);  //设置动画
        // mChart.spin(2000, 0, 360);
    }

    /**
     * @param count 分成几部分
     * @param range
     */
    private PieData getPieData(int count, float range) {

        //y轴的集合
        PieDataSet pieDataSet = new PieDataSet(yValues, "");
        pieDataSet.setSliceSpace(0f); //设置个饼状图之间的距离

        ArrayList<Integer> colorsList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            colorsList.add(colors.get(i));
        }
        pieDataSet.setColors(colorsList);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = 3 * (metrics.densityDpi / 210f);
        pieDataSet.setSelectionShift(px); // 选中态多出的长度
        pieDataSet.setHighlightEnabled(true);

        PieData pieData = new PieData(xValues, pieDataSet);
        // 设置成PercentFormatter将追加%号
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(9f);
        return pieData;
    }


}
