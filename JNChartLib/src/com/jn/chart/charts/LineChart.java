
package com.jn.chart.charts;

import android.content.Context;
import android.util.AttributeSet;

import com.jn.chart.interfaces.dataprovider.LineDataProvider;
import com.jn.chart.renderer.LineChartRenderer;
import com.jn.chart.charts.BarLineChartBase;
import com.jn.chart.data.LineData;

/**
 * Chart that draws lines, surfaces, circles, ...
 * 
 * @author Philipp Jahoda
 */
public class LineChart extends BarLineChartBase<LineData> implements LineDataProvider {

    public LineChart(Context context) {
        super(context);
    }

    public LineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LineChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();
        mRenderer = new LineChartRenderer(this, mAnimator, mViewPortHandler);
    }

    @Override
    protected void calcMinMax() {
        super.calcMinMax();

        if (mXAxis.mAxisRange == 0 && mData.getYValCount() > 0)
            mXAxis.mAxisRange = 1;
    }
    
    @Override
    public LineData getLineData() {
        return mData;
    }

    @Override
    protected void onDetachedFromWindow() {
        // releases the bitmap in the renderer to avoid oom error
        if(mRenderer != null && mRenderer instanceof LineChartRenderer) {
            ((LineChartRenderer) mRenderer).releaseBitmap();
        }
        super.onDetachedFromWindow();
    }

    @Override
    public void zoom(float scaleX, float scaleY, float x, float y) {
        super.zoom(scaleX, scaleY, x, y);
    }
}
