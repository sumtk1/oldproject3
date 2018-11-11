package com.gloiot.hygoSupply.bean;

import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 作者：Ljy on 2017/3/16.
 *
 */

public class GuigeModel implements Cloneable {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    private String color = "";
    private String size = "";
    private String guige = "";
    private String gonghuojia;
    private String jianyijia;
    private String kucun;
    private Map<String, String> allpropertys;
    private EditText editText;
    //列表中是否只有一条数据
    private boolean isOnlyOne;
    //商品主营类别如果不是鞋靴和服装，则统一用规格代表
    private boolean isShowGuige;
    //区分正在输入的是建议价还是供货价
    private boolean isSetJianyijia;
    private boolean allowEdit;
    private boolean isQuickEdit;
    private boolean isIncreaseGonghuojia;
    private boolean isIncreaseJianyijia;
    private double maxGonghuojia;
    private double maxJianyijia;


    public GuigeModel(boolean isShowGuige) {
        this.isShowGuige = isShowGuige;
        allpropertys = new LinkedHashMap<>();
        if (isShowGuige) {
            allpropertys.put("规格", guige);
        } else {
            allpropertys.put("颜色", color);
            allpropertys.put("尺寸", size);
        }
        allpropertys.put("供货价", gonghuojia);
        allpropertys.put("建议价", jianyijia);
        allpropertys.put("库存", kucun);
    }

    public Double getMaxGonghuojia() {
        return maxGonghuojia;
    }

    public void setMaxGonghuojia(Double maxGonghuojia) {
        this.maxGonghuojia = maxGonghuojia;
    }

    public Double getMaxJianyijia() {
        return maxJianyijia;
    }

    public void setMaxJianyijia(Double maxJianyijia) {
        this.maxJianyijia = maxJianyijia;
    }

    public boolean isIncreaseGonghuojia() {
        return isIncreaseGonghuojia;
    }

    public void setIncreaseGonghuojia(boolean increaseGonghuojia) {
        isIncreaseGonghuojia = increaseGonghuojia;
    }

    public boolean isIncreaseJianyijia() {
        return isIncreaseJianyijia;
    }

    public void setIncreaseJianyijia(boolean increaseJianyijia) {
        isIncreaseJianyijia = increaseJianyijia;
    }

    public boolean isAllowEdit() {
        return allowEdit;
    }

    public void setAllowEdit(boolean allowEdit) {
        this.allowEdit = allowEdit;
    }

    public boolean isOnlyOne() {
        return isOnlyOne;
    }

    public void setOnlyOne(boolean onlyOne) {
        isOnlyOne = onlyOne;
    }

    public boolean isShowGuige() {
        return isShowGuige;
    }

    public void setShowGuige(boolean showGuige) {
        isShowGuige = showGuige;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        if (!isShowGuige()) {
            allpropertys.put("颜色", color);
        }
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        if (!isShowGuige()) {
            allpropertys.put("尺寸", size);
        }
        this.size = size;
    }

    public String getGuige() {
        return guige;
    }

    public void setGuige(String guige) {
        if (isShowGuige()) {
            allpropertys.put("规格", guige);
        }
        this.guige = guige;
    }

    public String getGonghuojia() {
        return gonghuojia;
    }

    public void setGonghuojia(String gonghuojia) {
        allpropertys.put("供货价", gonghuojia);

        this.gonghuojia = gonghuojia;
    }

    public String getJianyijia() {
        return jianyijia;
    }

    public void setJianyijia(String jianyijia) {
        allpropertys.put("建议价", jianyijia);
        this.jianyijia = jianyijia;
    }

    public String getKucun() {
        return kucun;
    }

    public void setKucun(String kucun) {
        allpropertys.put("库存", kucun);
        this.kucun = kucun;
    }

    //检查各个属性是否符合要求
    public String checkFormat() {
        for (String key : allpropertys.keySet()) {
//            Log.e("TAG", "checkFormat: " + allpropertys.get(key));
            if (TextUtils.isEmpty(allpropertys.get(key))) {
                return "请填写商品的" + key;
            }
            switch (key) {
                case "供货价":
                case "建议价":
                    if (Double.parseDouble(allpropertys.get(key)) <= 0) {
                        return "商品的" + key + "不能为零";
                    }
                    break;
                case "库存":
                    if (Double.parseDouble(allpropertys.get(key)) <= 0) {
                        return "商品的" + key + "不能为零";
                    }
                    if (allpropertys.get(key).substring(0, 1).equals("0")) {
                        return "库存的第一位不能为零";
                    }
                    if (Double.parseDouble(allpropertys.get(key)) > 10000) {
                        return "库存不能超过10000！";
                    }
                    break;
            }
        }
        if (Double.parseDouble(gonghuojia) >= Double.parseDouble(jianyijia)) {
            return "建议零售价必须大于供货价";
        }
        return "ok";
    }

    public boolean isQuickEdit() {
        return isQuickEdit;
    }

    public void setQuickEdit(boolean quickEdit) {
        isQuickEdit = quickEdit;
    }

    @Override
    public GuigeModel clone() {
        GuigeModel model = null;
        try {
            model = (GuigeModel) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return model;
    }
}


