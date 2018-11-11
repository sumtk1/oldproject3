package com.zyd.wlwsdk.utlis;

import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * 日期弹出框工具类
 */
public class DatePrickerUtli {

    /**
     *
     * 设置时间选择器的分割线颜色
     * @param datePicker
     */
    public static void setDatePickerDividerColor(DatePicker datePicker){
        // Divider changing:
        // 获取 mSpinners
        LinearLayout llFirst = (LinearLayout) datePicker.getChildAt(0);
        // 获取 NumberPicker
        LinearLayout mSpinners  = (LinearLayout) llFirst.getChildAt(0);
        for (int i = 0; i < mSpinners.getChildCount(); i++) {
            NumberPicker picker = (NumberPicker) mSpinners.getChildAt(i);

            Field[] pickerFields = NumberPicker.class.getDeclaredFields();
            for (Field pf : pickerFields) {
                if (pf.getName().equals("mSelectionDivider")) {
                    pf.setAccessible(true);
                    try {
                        pf.set(picker, new ColorDrawable(0xff));
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (Resources.NotFoundException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }

    /**
     * 得到DatePicker控件里面的numberpicker组件
     *
     * @param datePicker
     */
    public static void findNumberPicker(DatePicker datePicker) {
        LinearLayout llFirst = (LinearLayout) datePicker.getChildAt(0);
        LinearLayout mSpinners = (LinearLayout) llFirst.getChildAt(0);

        for (int i = 0; i < mSpinners.getChildCount(); i++) {
            NumberPicker picker = (NumberPicker) mSpinners.getChildAt(i);
            setNumberPickerTextColor(picker, 0xff2b9ced);
        }
    }


    /**
     * 设置NumberPicker字色
     *
     * @param numberPicker：NumberPicker
     * @param color：int
     */
    public static void setNumberPickerTextColor(NumberPicker numberPicker, int color) {
        final int count = numberPicker.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = numberPicker.getChildAt(i);
            if (child instanceof EditText) {
                Field selectorWheelPaintField;
                try {
                    selectorWheelPaintField = numberPicker.getClass().getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    try {
                        ((Paint) selectorWheelPaintField.get(numberPicker)).setColor(color);
                        ((Paint) selectorWheelPaintField.get(numberPicker)).setTextSize(48);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    ((EditText) child).setTextColor(color);
                    ((EditText) child).setTextSize(18);
                    numberPicker.invalidate();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }

                numberPicker.invalidate();
            }
        }
    }

    /**
     * 获取时区当前时间(str)
     */
    public static String getDate() {
        SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        dff.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        String str = dff.format(Calendar.getInstance().getTime());
        L.e("---1---", str + "");
        return str;
    }
    /**
     * 获取时区最大时间
     */
    public static String getMaxDate(int month) {
        SimpleDateFormat dialog = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        dialog.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, month);
        String Maxstr = dialog.format(calendar.getTime());
        L.e("---2---", Maxstr + "");
        return Maxstr;
    }

}
