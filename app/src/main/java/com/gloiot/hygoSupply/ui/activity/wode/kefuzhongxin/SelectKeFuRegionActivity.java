package com.gloiot.hygoSupply.ui.activity.wode.kefuzhongxin;

import android.Manifest;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.zyd.wlwsdk.utlis.LocationUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 创建者 zengming.
 * 功能：选择客服区域
 */
public class SelectKeFuRegionActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.ll_location)
    LinearLayout llLocation;
    @Bind(R.id.tv_location_city)
    TextView tvLocationCity;
    @Bind(R.id.exListView)
    ExpandableListView exListView;
    private List<String> cityList = new ArrayList<>();
    private boolean isLocation = false; // 定位是否成功
    private boolean isPositioning = true; // 是否正在定位
    private List<ProvinceBean> mData;
    private MyAdapter myAdapter;

    @Override
    public int initResource() {
        return R.layout.activity_kefuregion_select;
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar(this, true, "位置", "");
        setLocation();
        setAdapter();
        requestHandleArrayList.add(requestAction.SelectKeFuLocation(this, preferences.getString(ConstantUtils.SP_MYID, ""),
                getIntent().getIntExtra("regionID", 1)));
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        Log.e("SelectKeFuRegion：" + requestTag, response.toString());
        switch (requestTag) {
            case ConstantUtils.TAG_KEHU_SELECT_LOCATION:
                mData.clear();
                //列表
                JSONArray jsonArray = response.getJSONArray("列表");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject province = jsonArray.getJSONObject(i);
                    ProvinceBean bean = new ProvinceBean();
                    bean.setProvince(province.getString("省"));

                    List<CityBean> list = new ArrayList<>();
                    JSONArray cityArray = province.getJSONArray("市");
                    for (int j = 0; j < cityArray.length(); j++) {
                        JSONObject city = cityArray.getJSONObject(i);
                        CityBean cityBean = new CityBean();
                        cityBean.setCity(city.getString("市"));
                        cityBean.setCityNum(city.getString("编号"));
                        list.add(cityBean);
                    }

                    bean.setCityList(list);

                    mData.add(bean);
                }
                myAdapter.notifyDataSetChanged();
                exListView.expandGroup(0);
                break;
        }
    }

    private void setAdapter() {
        mData = new ArrayList<>();
        exListView.setAdapter(myAdapter = new MyAdapter());

//        //设置 属性 GroupIndicator 去掉默认向下的箭头  
//        exListView.setGroupIndicator(null);
        exListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Log.e(TAG, "group " + groupPosition + "---child " + childPosition + "---id " + id);
                Intent intent = new Intent(SelectKeFuRegionActivity.this, SelectKeFuActivity.class);
                intent.putExtra("cityID", mData.get(groupPosition).getCityList().get(childPosition).getCityNum());
                startActivity(intent);
                return false;
            }
        });
    }

    @OnClick({R.id.ll_location})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_location:
                if (!isPositioning) {
                    if (isLocation) {
                        Intent intent = new Intent(SelectKeFuRegionActivity.this, SelectKeFuActivity.class);
                        intent.putExtra("cityName", cityList.get(cityList.size() - 1));
                        startActivity(intent);
                    } else {
                        isPositioning = true;
                        tvLocationCity.setText("定位中");
                        setLocation();
                    }
                }
                break;
        }
    }

    private void setLocation() {
        checkPermission(new CheckPermListener() {
            @Override
            public void superPermission() {
                LocationUtils.getCNBylocation(mContext, new LocationUtils.LocationCallBack() {
                    @Override
                    public void location(List<String> city) {
                        isPositioning = false;
                        cityList = city;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("--location--", "--1232131");
                                if (cityList.size() == 1) {
                                    isLocation = false;
                                    tvLocationCity.setText(cityList.get(0));
                                } else {
                                    isLocation = true;
                                    String cityName = "";
                                    for (int i = 0; i < cityList.size(); i++) {
                                        cityName = cityName + cityList.get(i) + " ";
                                    }
                                    tvLocationCity.setText(cityName);
                                }
                            }
                        });
                    }
                });
            }
        }, R.string.perm_location, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    class MyAdapter extends BaseExpandableListAdapter {

        @Override
        public int getGroupCount() {
            return mData.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return mData.get(groupPosition).getCityList().size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return mData.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return mData.get(groupPosition).getCityList();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            View view = convertView;
            GroupHolder holder = null;
            if(view == null){
                holder = new GroupHolder();
                view = LayoutInflater.from(mContext).inflate(R.layout.item_kefuregion_select01, null);
                holder.groupName = (TextView)view.findViewById(R.id.tv_group_name);
                holder.arrow = (ImageView)view.findViewById(R.id.iv_arrow);
                view.setTag(holder);
            }else{
                holder = (GroupHolder)view.getTag();
            }

            //判断是否已经打开列表
            if(isExpanded){
                holder.arrow.setBackgroundResource(R.mipmap.ic_arrow_down);
            }else{
                holder.arrow.setBackgroundResource(R.mipmap.ic_arrow_up);
            }

            holder.groupName.setText(mData.get(groupPosition).getProvince());
            return view;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {        View view = convertView;
            ChildHolder holder = null;
            if(view == null){
                holder = new ChildHolder();
                view = LayoutInflater.from(mContext).inflate(R.layout.item_kefuregion_select02, null);
                holder.childName = (TextView)view.findViewById(R.id.tv_child_name);
                view.setTag(holder);
            }else{
                holder = (ChildHolder)view.getTag();
            }

            holder.childName.setText(mData.get(groupPosition).getCityList().get(childPosition).getCity());

            return view;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        class GroupHolder{
            public TextView groupName;
            public ImageView arrow;
        }

        class ChildHolder{
            public TextView childName;
        }
    }

    // 省Bean
    class ProvinceBean {
        private String province;
        private List<CityBean> cityList;

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public List<CityBean> getCityList() {
            return cityList;
        }

        public void setCityList(List<CityBean> cityList) {
            this.cityList = cityList;
        }
    }

    class CityBean {
        private String city;
        private String cityNum;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCityNum() {
            return cityNum;
        }

        public void setCityNum(String cityNum) {
            this.cityNum = cityNum;
        }
    }

}
