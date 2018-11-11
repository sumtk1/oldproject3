package com.zyd.wlwsdk.utlis;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 创建人： zengming on 2017/10/12.
 * 功能：
 */
public class LocationUtils {

    private static List<String> locationCity;   // 国  省  市
    private static Geocoder geocoder;  //此对象能通过经纬度来获取相应的城市等信息
    private static LocationCallBack locationCallBack;

    public interface LocationCallBack {
        void location(List<String> city);
    }

    //通过地理坐标获取城市名 其中CN分别是city和name的首字母缩写
    public static void getCNBylocation(Context context, LocationCallBack callBack) {
        locationCallBack = callBack;
        locationCity = new ArrayList<>();
        geocoder = new Geocoder(context);
        //用于获取Location对象，以及其他
        LocationManager locationManager;
        String serviceName = Context.LOCATION_SERVICE;
        //实例化一个LocationManager对象
        locationManager = (LocationManager) context.getSystemService(serviceName);
        //provider的类型
        String provider = LocationManager.NETWORK_PROVIDER;

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_LOW);    //低精度   高精度：ACCURACY_FINE
        criteria.setAltitudeRequired(false);       //不要求海拔
        criteria.setBearingRequired(false);       //不要求方位
        criteria.setCostAllowed(false);      //不允许产生资费
        criteria.setPowerRequirement(Criteria.POWER_LOW);   //低功耗

        //通过最后一次的地理位置来获取Location对象
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            locationCity.add("请开启自动定位服务");
            locationCallBack.location(locationCity);
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);

        if (location == null) {
            location = locationManager.getLastKnownLocation(provider);
        }
        updateWithNewLocation(location);
        /*
		第二个参数表示更新的周期，单位为毫秒，
		第三个参数的含义表示最小距离间隔，单位是米，设定每30秒进行一次自动定位
		*/
        locationManager.requestLocationUpdates(provider, 30000, 50, locationListener);
        //移除监听器，在只有一个widget的时候，这个还是适用的
        locationManager.removeUpdates(locationListener);
    }

    //方位改变是触发，进行调用
    private final static LocationListener locationListener = new LocationListener() {
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
        @Override
        public void onProviderEnabled(String provider) {
        }
        @Override
        public void onProviderDisabled(String provider) {
            updateWithNewLocation(null);
        }
        @Override
        public void onLocationChanged(Location location) {
            updateWithNewLocation(location);
        }
    };

    //更新location  return cityName
    private static void updateWithNewLocation(final Location location){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> mCityName = new ArrayList<>();
                double lat = 0;
                double lng = 0;
                List<Address> addList = null;
                if(location != null){
                    lat = location.getLatitude();
                    lng = location.getLongitude();
                }else{
                    mCityName.add("无法获取地理信息");
                }
                try {
                    addList = geocoder.getFromLocation(lat, lng, 1);    //解析经纬度
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(addList != null && addList.size() > 0){
                    Log.e("--addList--", "--" + addList.toString() + "-11-" + addList.get(0).getExtras());
                    for (int i = 0; i < addList.size(); i++) {
                        Address add = addList.get(i);
                        if ("CN".equals(add.getCountryCode())) {
                            mCityName.add("中国");
                        } else {
                            mCityName.add("其他");
                        }
                        mCityName.add(add.getAdminArea());
                        mCityName.add(add.getLocality());

                    }
                }
                locationCallBack.location(mCityName);
            }
        }).start();
    }
}
