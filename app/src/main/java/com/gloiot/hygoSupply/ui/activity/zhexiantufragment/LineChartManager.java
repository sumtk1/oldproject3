package com.gloiot.hygoSupply.ui.activity.zhexiantufragment;

import android.content.Context;
import android.graphics.Color;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.widget.MyMakerView;
import com.jn.chart.animation.Easing;
import com.jn.chart.charts.LineChart;
import com.jn.chart.components.Legend;
import com.jn.chart.components.XAxis;
import com.jn.chart.components.YAxis;
import com.jn.chart.data.Entry;
import com.jn.chart.data.LineData;
import com.jn.chart.data.LineDataSet;
import com.jn.chart.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

/**
 * @Author: mxx
 * @Package:
 * @Description: TODO
 * @Date: 2017/4/1 16:39
 */
public class LineChartManager {

    private static String lineName = null;
    private static String lineName1 = null;

    /**
     * @param context    上下文
     * @param mLineChart 折线图控件
     * @param xValues    折线在x轴的值
     * @param yValue     折线在y轴的值
     * @Description:创建一条折线
     */
    public static void initSingleLineChart(Context context, LineChart mLineChart, ArrayList<String> xValues,
                                           ArrayList<Entry> yValue, int count) {
        initDataStyle(context, mLineChart);

        //设置点击折线点时，显示其数值
        MyMakerView mv = new MyMakerView(context, R.layout.item_mark_layout, count);
        mLineChart.setMarkerView(mv);
        //设置折线的样式
        LineDataSet dataSet = new LineDataSet(yValue, lineName);
        dataSet.setColor(Color.rgb(255, 127, 41));
        dataSet.setCircleColor(Color.rgb(255, 127, 41));
        dataSet.setDrawValues(false);
        //设置原点
        dataSet.setDrawCircles(true);
        //设置线的样式
        dataSet.setDrawCubic(false);
//        dataSet.setCircleRadius(1f);
//        dataSet.setCubicIntensity(0f);

        dataSet.setHighLightColor(Color.rgb(211, 211, 211));  //设置 坐标提示线的颜色
//        LineData data = mLineChart.getData();
//        if(data != null) {
//            data.removeDataSet(dataSet.getDataSetByIndex(dataSet.getDataSetCount() - 1));
        dataSet.setDrawHorizontalHighlightIndicator(false);
        mLineChart.notifyDataSetChanged();
        mLineChart.invalidate();
//        }

//        dataSet.setValueFormatter(new PercentFormatter(new DecimalFormat("%").format()));

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);

        //构建一个LineData  将dataSets放入
        LineData lineData = new LineData(xValues, dataSets);
        //将数据插入
        mLineChart.setData(lineData);
        mLineChart.setNoDataText("没有数据");

        //设置动画效果
        mLineChart.animateY(20, Easing.EasingOption.Linear);
        mLineChart.animateX(20, Easing.EasingOption.Linear);
        mLineChart.invalidate();
        mLineChart.notifyDataSetChanged();
    }

    /**
     * @param context    上下文
     * @param mLineChart 折线图控件
     * @param xValues    折线在x轴的值
     * @param yValue     折线在y轴的值
     * @param yValue1    另一条折线在y轴的值
     * @Description:创建两条折线
     */
    public static void initDoubleLineChart(Context context, LineChart mLineChart, ArrayList<String> xValues,
                                           ArrayList<Entry> yValue, ArrayList<Entry> yValue1) {

        initDataStyle(context, mLineChart);

        LineDataSet dataSet = new LineDataSet(yValue, lineName);
        dataSet.setColor(Color.RED);
        dataSet.setCircleColor(Color.RED);
        dataSet.setDrawValues(false);

        LineDataSet dataSet1 = new LineDataSet(yValue1, lineName1);
        dataSet1.enableDashedLine(10f, 10f, 0f);//将折线设置为曲线
        dataSet1.setColor(Color.parseColor("#66CDAA"));
        dataSet1.setCircleColor(Color.parseColor("#66CDAA"));
        dataSet1.setDrawValues(false);

        //构建一个类型为LineDataSet的ArrayList 用来存放所有 y的LineDataSet   他是构建最终加入LineChart数据集所需要的参数
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        //将数据加入dataSets
        dataSets.add(dataSet);
        dataSets.add(dataSet1);

        //构建一个LineData  将dataSets放入
        LineData lineData = new LineData(xValues, dataSets);
        //将数据插入
        mLineChart.setData(lineData);
        //设置动画效果
        mLineChart.animateY(2000, Easing.EasingOption.Linear);
        mLineChart.animateX(2000, Easing.EasingOption.Linear);
        mLineChart.invalidate();
    }

    /**
     * @param context
     * @param mLineChart
     * @Description:初始化图表的样式
     */
    private static void initDataStyle(Context context, LineChart mLineChart) {
        //设置图表是否支持触控操作
        mLineChart.setTouchEnabled(true);
        mLineChart.setScaleEnabled(false);

        //设置折线的描述的样式（默认在图表的左下角）
        Legend title = mLineChart.getLegend();
        title.setEnabled(false);
        title.setForm(Legend.LegendForm.LINE);
        title.setTextSize(10);
        title.setPosition(Legend.LegendPosition.ABOVE_CHART_LEFT);
        //设置x轴的样式
        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisLineColor(Color.parseColor("#ffffff"));
        xAxis.setAxisLineWidth(1);
        xAxis.setSpaceBetweenLabels(0);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setTextColor(Color.parseColor("#999999"));
        xAxis.setTextSize(10f);
        xAxis.setDrawGridLines(false);
        //设置是否显示x轴
        xAxis.setEnabled(true);

        //设置左边y轴的样式
        YAxis yAxisLeft = mLineChart.getAxisLeft();
//        yAxisLeft.setValueFormatter(new YAxisValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, YAxis yAxis) {
//                return "" + (int) value;//这句是重点!
//            }
//        });
        yAxisLeft.setDrawGridLines(false);
        yAxisLeft.setEnabled(false);
        yAxisLeft.setStartAtZero(true);
//        yAxisLeft.setDrawZeroLine(true);

        //设置右边y轴的样式
        YAxis yAxisRight = mLineChart.getAxisRight();
        yAxisRight.setEnabled(false);
    }

    /**
     * @param name
     * @Description:设置折线的名称
     */
    public static void setLineName(String name) {
        lineName = name;
    }

    /**
     * @param name
     * @Description:设置另一条折线的名称
     */
    public static void setLineName1(String name) {
        lineName1 = name;
    }
}
