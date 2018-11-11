package com.zyd.wlwsdk.utlis.xmlanalsis;

import android.content.Context;
import android.content.res.AssetManager;

import com.zyd.wlwsdk.utlis.L;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * 中国城市区域的辅助类
 */
public class ProvinceAreaHelper {

    private static final String TAG = "ProvinceAreaHelper";
    private Context mContext;
    public static ProvinceAreaHelper instance;

    /**
     * 所有省
     */
    protected List<String> mProvinceDatas = new ArrayList<>();
    /**
     * key - 省 value - 市
     */
    protected Map<String, List<String>> mCitisDatasMap = new LinkedHashMap<>();
    /**
     * key - 市 values - 区
     */
    protected Map<String, List<String>> mDistrictDatasMap = new LinkedHashMap<>();

    public static ProvinceAreaHelper getInstance(Context mContext) {
        if (instance == null) {
            instance = new ProvinceAreaHelper(mContext);
        }
        return instance;
    }

    public ProvinceAreaHelper(Context context){
        mContext = context;
        initProvinceData();
    }

    /**
     * 解析省市区的XML数据
     */
    private void initProvinceData(){
        List<ProvinceModel> provinceModelList;
        AssetManager assetManager = mContext.getAssets();
        try {
            InputStream input = assetManager.open("array_data.xml");

            //创建一个解析xml 的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser parser = spf.newSAXParser();
            XmlParserHelper xmlParserHelper = new XmlParserHelper();

            //开始解析xml
            parser.parse(input,xmlParserHelper);
            input.close();

            //获取解析出来的数据
            provinceModelList = xmlParserHelper.getDataList();

            if(provinceModelList != null){
                L.e("--provinceModelList1--", provinceModelList.get(1).getName() + "=" + provinceModelList.size());

                // 遍历所有省的数据
                for (int i = 0; i< provinceModelList.size(); i++) {
                    mProvinceDatas.add(provinceModelList.get(i).getName());
                    List<CityModel> cityList = provinceModelList.get(i).getCityList();
                    List<String> cityNames = new ArrayList<>();

                    L.e("--provinceModelList2--", provinceModelList.get(i).getName() + "" + cityList.size());

                    // 遍历省下面的所有市的数据
                    for (int j = 0; j < cityList.size(); j++) {
                        cityNames.add(cityList.get(j).getName());
                    }

                    // 省-市的数据，保存到mCitisDatasMap
                    mCitisDatasMap.put(provinceModelList.get(i).getName(), cityNames);
                }
            }

        }catch (Exception e){
            L.e(TAG, "解析省市区的XML数据 Exception=" + e.getMessage());
            e.printStackTrace();
        }
    }

    /** 获取省 */
    public List<String> getmProvinceDatas() {
        return mProvinceDatas;
    }


    /** 获取{key - 省 value - 市}集合 */
    public Map<String, List<String>> getmCitisDatasMap() {
        return mCitisDatasMap;
    }

}
