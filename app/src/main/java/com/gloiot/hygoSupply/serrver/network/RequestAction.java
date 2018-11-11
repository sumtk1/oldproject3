package com.gloiot.hygoSupply.serrver.network;

import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;

import com.gloiot.hygoSupply.bean.FreightTemplateModel;
import com.gloiot.hygoSupply.bean.GuigeModel;
import com.gloiot.hygoSupply.bean.MiaoShuBean;
import com.gloiot.hygoSupply.bean.ShangpinGuanliBean;
import com.gloiot.hygoSupply.bean.ShangpinGuigeBean;
import com.gloiot.hygoSupply.ui.activity.postproduct.model.ProductDetailsModel;
import com.gloiot.hygoSupply.ui.activity.postproduct.model.SpecificationModel;
import com.gloiot.hygoSupply.ui.activity.promotional.model.ShopPromotionModel;
import com.gloiot.hygoSupply.ui.activity.wode.fadongtai.model.DynamicProductModel;
import com.gloiot.hygoSupply.ui.activity.wode.fadongtai.model.ImageTextModel;
import com.gloiot.hygoSupply.ui.activity.wode.fadongtai.model.ProductModel;
import com.gloiot.hygoSupply.ui.activity.wode.tuihuodizhi.ShouhuoAddress;
import com.gloiot.hygoSupply.ui.activity.wode.xitongxiaoxi.SystemMessageModel;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.utlis.DialogUtlis;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.zyd.wlwsdk.server.network.OnDataListener;
import com.zyd.wlwsdk.server.network.HttpManager;
import com.zyd.wlwsdk.server.network.utlis.JsonUtils;
import com.zyd.wlwsdk.utlis.MD5Utlis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by hygo00 on 2016/7/15.
 */
public class RequestAction {

    private RequestHandle doPost_New(OnDataListener onDataListener, HashMap<String, Object> hashMap, String FUNC, int TAG) {
        //将信息加入map
        hashMap.putAll(CommonUtils.loadMap(ConstantUtils.SP_PHONEINFO_JSON));
        //打印接口请求信息
        Log.e("HttpRequest", "此次请求的FUNC为：" + FUNC + "，请求信息如下：");
        String requestContent = "";
//        for (Map.Entry<String, Object> e : entry) {
//            e.getValue().toString().replaceAll("'","’");
//            requestContent += e.getKey() + "=" + e.getValue() + "\n";
//
//        }
        Iterator<Map.Entry<String, Object>> iterator = hashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> itEntry = iterator.next();
            Object value = itEntry.getValue();
            if (value != null && !TextUtils.isEmpty(value.toString())) {
                itEntry.setValue(value.toString().replaceAll("'", "’"));
            }
            requestContent += itEntry.getKey() + "=" + itEntry.getValue() + "\n";
        }
        Log.e("HttpRequest", requestContent);
        RequestParams params = new RequestParams();
        params.add("func", FUNC);
        params.add("words", ConstantUtils.random + CommonUtils.aesEncrypt(JsonUtils.createJSON(hashMap).toString(), ConstantUtils.random));
        if (TAG == 24) {
            return HttpManager.doPost(TAG, params, onDataListener, 2);
        } else {
            return HttpManager.doPost(TAG, params, onDataListener, 0);
        }
    }


    private static RequestParams getParams(String func, HashMap<String, Object> hashMap) {
        String json = "";
        hashMap.putAll(CommonUtils.loadMap(ConstantUtils.SP_PHONEINFO_JSON));
        RequestParams params = new RequestParams();
        params.add("func", func);
        json = JsonUtils.createJSON(hashMap).toString();
        params.add("words", ConstantUtils.random + CommonUtils.aesEncrypt(json, ConstantUtils.random));
        Log.e("TAG", "getParams:json数据 " + json);
        return params;
    }


    // 获取支付地址
    public final static int TAG_PAYWEBURL = 10086;

    public RequestHandle getPayWebUrl(OnDataListener onDataListener, int showLoad, int time) {
        RequestParams params = new RequestParams();
        params.add("func", "p_jssdk");
        params.add("category", "android");
        return HttpManager.doPost(ConstantUtils.WEBJS_URL, TAG_PAYWEBURL, params, onDataListener, showLoad, time);
    }

    //登录
    public RequestHandle userLogin(OnDataListener onDataListener, String phone, String pwd) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", phone);
        hashMap.put("随机码", ConstantUtils.random = CommonUtils.Md5(ConstantUtils.FUNC_shop_login));
        hashMap.put("密码", pwd);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_login, ConstantUtils.TAG_shop_login);
    }

    //登录
    public RequestHandle userLogin(OnDataListener onDataListener, String phone, String pwd, String suijima) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", phone);
        hashMap.put("随机码", suijima);
        hashMap.put("密码", pwd);
        hashMap.put("_版本", "商家");
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_login, ConstantUtils.TAG_shop_login);
    }

    //忘记密码
    public RequestHandle forgetPassword(OnDataListener onDataListener, String forget_password_phone, String forget_password_yanzhengma, String forget_password_newpassword) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", forget_password_phone);
        hashMap.put("随机码", ConstantUtils.random = CommonUtils.Md5(ConstantUtils.FUNC_test_password));
        hashMap.put("验证码", forget_password_yanzhengma);
        hashMap.put("新密码", forget_password_newpassword);
        hashMap.put("确认密码", forget_password_newpassword);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_test_password, ConstantUtils.TAG_test_password);
    }

    //获取验证码
    public RequestHandle sms(OnDataListener onDataListener, String forget_password_phone) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("手机号", forget_password_phone);
        hashMap.put("随机码", ConstantUtils.random = CommonUtils.Md5(ConstantUtils.FUNC_sms));
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_sms, ConstantUtils.TAG_sms);
    }

    //商家注册
    public RequestHandle shop_sj_reg(OnDataListener onDataListener, String et_register_phone, String et_register_yanzhengma, String et_register_password, String name, String touxiang) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", et_register_phone);
        hashMap.put("随机码", ConstantUtils.random = CommonUtils.Md5(ConstantUtils.FUNC_shop_sj_reg));
        hashMap.put("验证码", et_register_yanzhengma);
        hashMap.put("密码", CommonUtils.Md5(et_register_password));
        hashMap.put("确认密码", CommonUtils.Md5(et_register_password));
        hashMap.put("姓名", name);
        hashMap.put("头像", touxiang);
        hashMap.put("_版本", "商家");
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_sj_reg, ConstantUtils.TAG_shop_sj_reg);
    }

    //获取供货商列表
    public RequestHandle shop_sj_supplier(OnDataListener onDataListener, String phone, String zhongyou_leibie, String shangcheng_leibie) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", phone);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("中油类别", zhongyou_leibie);
        hashMap.put("商城类别", shangcheng_leibie);
        hashMap.put("_版本", "商家");
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_sj_supplier, ConstantUtils.TAG_shop_sj_supplier);
    }

    //获取推荐人姓名
    public RequestHandle shangjia_info(OnDataListener onDataListener, String phone, String tuijianren_phone, String id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", phone);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("服务商手机号", tuijianren_phone);
        hashMap.put("供货商id", id);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shangjia_info, ConstantUtils.TAG_shangjia_info);
    }   //获取推荐人姓名

    public RequestHandle shangjia_info(OnDataListener onDataListener, String phone, String tuijianren_phone) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", phone);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("服务商手机号", tuijianren_phone);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shangjia_info, ConstantUtils.TAG_shangjia_info);
    }


    //设置个人信息
    public RequestHandle shop_set(OnDataListener onDataListener, String phone, String xingbie, String nicheng, String touxiang) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", phone);
        hashMap.put("随机码", ConstantUtils.random);
        if (xingbie == null || xingbie.equals("")) {
//            hashMap.put("性别", "");
        } else {
            hashMap.put("性别", xingbie);
        }
        if (nicheng == null || nicheng.equals("")) {
//            hashMap.put("昵称", "");
        } else {
            hashMap.put("昵称", nicheng);
        }
        if (touxiang == null || touxiang.equals("")) {
//            hashMap.put("头像", null);
        } else {
            hashMap.put("头像", touxiang);
        }
        hashMap.put("设置", "个人信息");
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_set, ConstantUtils.TAG_shop_set);
    }


    //我的人脉显示列表
    public RequestHandle shop_woderenmai_list(OnDataListener onDataListener, String phone, String pagenum, String sousuo) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", phone);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("页数", pagenum);
        hashMap.put("搜索", sousuo);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_woderenmai_list, ConstantUtils.TAG_shop_woderenmai_list);
    }


    //今日订单详情
    public RequestHandle shop_sy_ddxq_day(OnDataListener onDataListener, String phone, String num) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", phone);
        hashMap.put("页数", num);
        hashMap.put("随机码", ConstantUtils.random);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_sy_ddxq_day, ConstantUtils.TAG_shop_sy_ddxq_day);
    }

    //无需物流发货 快递单号 id
    public RequestHandle shop_ddgl_fahuoB(OnDataListener onDataListener, String phone, String id, List<String> shangpinID) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", phone);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("_版本", "商家");
        hashMap.put("id", id);
        JSONArray array = new JSONArray();
        for (String s : shangpinID) {
            JSONObject object = new JSONObject();
            try {
                object.put("订单销售id", s);
                array.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            hashMap.put("订单销售id", URLEncoder.encode(array.toString(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_sj_fahuoB, ConstantUtils.TAG_shop_sj_fahuoB);
    }


    //今日收益
    public RequestHandle shop_sy_today(OnDataListener onDataListener, String phone) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", phone);
        hashMap.put("随机码", ConstantUtils.random);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_sy_today, ConstantUtils.TAG_shop_sy_today);
    }

    //商品上传 账号  商品名称  缩略图地址  发货地址id   商品描述  商品属性
//    public RequestHandle shop_shangpinshangchuan(OnDataListener onDataListener,
//                                                 String phone, String shangpinmingchen,
//                                                 String suoluetu, String fahuodizhiID, String yunfei,
//                                                 List<MiaoShuBean> allMiaoshu, String leimu,
//                                                 List<ShangpinGuigeBean> allShangpinGuige) {
//        HashMap<String, Object> hashMap = new HashMap<>();
//        JSONObject jsonMiaoshu = new JSONObject();
//        JSONArray jsonArrayMiaoshu = new JSONArray();
//        JSONObject jsonGuige = new JSONObject();
//        JSONArray jsonArrayGuige = new JSONArray();
//        //商品描述重组
//        for (int i = 0; i < allMiaoshu.size(); i++) {
//            JSONObject jsonobj = new JSONObject();
//            try {
//                //描述jsonArray组建
//                jsonobj.put("描述", allMiaoshu.get(i).getMiaoshu());
//                jsonobj.put("图片", allMiaoshu.get(i).getTupian());
//                jsonArrayMiaoshu.put(jsonobj);
//
//                jsonMiaoshu.put("列表", jsonArrayMiaoshu);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        //商品规格重组
//        for (int i = 0; i < allShangpinGuige.size(); i++) {
//            JSONObject jsonobj2 = new JSONObject();
//            try {
//                //规格jsonArray组建
//                jsonobj2.put("颜色", allShangpinGuige.get(i).getYanse());
//                jsonobj2.put("尺寸", allShangpinGuige.get(i).getChicun());
//                jsonobj2.put("结算价", allShangpinGuige.get(i).getGonghuojia());
//                jsonobj2.put("单价", allShangpinGuige.get(i).getJianyijia());
//                jsonobj2.put("库存", allShangpinGuige.get(i).getKucun());
//                jsonArrayGuige.put(jsonobj2);
//                jsonGuige.put("列表", jsonArrayGuige);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        try {
//            hashMap.put("账号", phone);
//            hashMap.put("随机码", ConstantUtils.random);
//            hashMap.put("商品名称", shangpinmingchen);
//            hashMap.put("缩略图", suoluetu);
//            hashMap.put("发货地址id", fahuodizhiID);
//            hashMap.put("快递费", yunfei);
//            hashMap.put("商品描述", URLEncoder.encode(jsonMiaoshu.toString(), "UTF-8"));
//            hashMap.put("分类", leimu);
//            hashMap.put("商品属性", URLEncoder.encode(jsonGuige.toString(), "UTF-8"));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return doPost(onDataListener, hashMap, ConstantUtils.FUNC_shop_shangpinshangchuan, ConstantUtils.TAG_shop_shangpinshangchuan);
//    }

    //商品上传 账号  商品名称   发货地址id  运费 商品描述  类目   商品规格   主营类目  商品类型 商城类别  图片列表
    public RequestHandle shop_sj_shangchuang(OnDataListener onDataListener,
                                             String phone, String shangpinmingchen,
                                             String fahuodizhiID, String yunfei,
                                             List<MiaoShuBean> allMiaoshu, String leimu,
                                             List<ShangpinGuigeBean> allShangpinGuige, String zhuyingleimu, String shangpinleixing, String shangchengleibie, List<String> picUrlList, String dizhi) {
        HashMap<String, Object> hashMap = new HashMap<>();
        JSONObject jsonMiaoshu = new JSONObject();
        JSONArray jsonArrayMiaoshu = new JSONArray();
        JSONObject jsonGuige = new JSONObject();
        JSONArray jsonArrayGuige = new JSONArray();
        JSONObject jsonPicture = new JSONObject();
        JSONArray jsonArrayPictures = new JSONArray();
        //商品描述重组
        for (int i = 0; i < allMiaoshu.size(); i++) {
            JSONObject jsonobj = new JSONObject();
            try {
                //描述jsonArray组建
                jsonobj.put("描述", allMiaoshu.get(i).getMiaoshu());
                jsonobj.put("图片", allMiaoshu.get(i).getTupian());
                jsonArrayMiaoshu.put(jsonobj);
                jsonMiaoshu.put("列表", jsonArrayMiaoshu);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //商品规格重组
        for (int i = 0; i < allShangpinGuige.size(); i++) {
            JSONObject jsonobj2 = new JSONObject();
            try {
                //规格jsonArray组建
                jsonobj2.put("颜色", allShangpinGuige.get(i).getYanse());
                jsonobj2.put("尺寸", allShangpinGuige.get(i).getChicun());
                jsonobj2.put("供货价", allShangpinGuige.get(i).getGonghuojia());
                jsonobj2.put("建议零售价", allShangpinGuige.get(i).getJianyijia());
                jsonobj2.put("市场价", allShangpinGuige.get(i).getJianyijia());
                jsonobj2.put("规格", allShangpinGuige.get(i).getXiangxi());
                jsonobj2.put("库存", allShangpinGuige.get(i).getKucun());
                jsonArrayGuige.put(jsonobj2);
                jsonGuige.put("列表", jsonArrayGuige);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //商城图片重组
        for (String url : picUrlList) {
            JSONObject object = new JSONObject();
            try {
                object.put("图片", url);
                jsonArrayPictures.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            hashMap.put("账号", phone);
            hashMap.put("随机码", ConstantUtils.random);
            hashMap.put("商品名称", shangpinmingchen);
            hashMap.put("缩略图", URLEncoder.encode(jsonArrayPictures.toString(), "UTF-8"));
            //Log.e("TAG", "shop_sj_shangchuang:缩略图格式" + URLEncoder.encode(jsonArrayPictures.toString()));
            hashMap.put("发货地址id", fahuodizhiID);
            hashMap.put("地址", dizhi);
            hashMap.put("快递费", yunfei);
            hashMap.put("商品描述", URLEncoder.encode(jsonMiaoshu.toString(), "UTF-8"));
            hashMap.put("分类", leimu);
            hashMap.put("类别名称", zhuyingleimu);
            hashMap.put("商品类型", shangpinleixing);
            hashMap.put("名称", shangchengleibie);
            hashMap.put("商品属性", URLEncoder.encode(jsonGuige.toString(), "UTF-8"));
            // Log.e("TAG", "shop_sj_shangchuang:wwwwwww "+JsonUtils.createJSON(hashMap).toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_sj_shangchuang, ConstantUtils.TAG_shop_sj_shangchuang);
    }


    //    //订单管理 func=shop_ddgli
//    public RequestHandle shop_ddgli(OnDataListener onDataListener, String phone, String dingdanzhuangtai, String yeshu) {
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("账号", phone);
//        hashMap.put("随机码", ConstantUtils.random);
//        hashMap.put("订单状态", dingdanzhuangtai);
//        hashMap.put("页数", yeshu);
//        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_ddgli, ConstantUtils.TAG_shop_ddgli);
//    }
    //订单管理 func=shop_ddgli
    public RequestHandle shop_wl_refund(OnDataListener onDataListener, String phone, String dingdanzhuangtai, String yeshu) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", phone);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("订单状态", dingdanzhuangtai);
        hashMap.put("页数", yeshu);
        hashMap.put("version", "1.3.0");
        hashMap.put("订单时间", "");
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_refund, ConstantUtils.TAG_shop_wl_refund);
    }

    //售后列表详情
    public RequestHandle shop_wl_Reservice(OnDataListener onDataListener, String phone, String dingdanzhuangtai, String yeshu) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", phone);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("订单状态", dingdanzhuangtai);
        hashMap.put("页数", yeshu);
        hashMap.put("version", "1.3.0");
        hashMap.put("订单时间", "");
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_Reservice, ConstantUtils.TAG_shop_wl_Reservice);
    }

    //给前台传值物流公司数据   快递公司 快递单号 id
    public RequestHandle shop_ddgl_fahuo(OnDataListener onDataListener, String phone, String kuaidigongsi, String kuaididanhao, String id, List<String> shangpinID, List<String> repairId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", phone);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("快递公司", kuaidigongsi);
        hashMap.put("快递单号", kuaididanhao);
        hashMap.put("_版本", "商家");
        hashMap.put("id", id);
        JSONArray array = new JSONArray();
        for (int i = 0; i < repairId.size(); i++) {
            JSONObject object = new JSONObject();
            try {
//                object.put("商品id", shangpinID.get(i));
                object.put("订单销售id", repairId.get(i));
                array.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            hashMap.put("订单销售id", URLEncoder.encode(array.toString(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_ddgl_fahuo, ConstantUtils.TAG_shop_ddgl_fahuo);
    }

    // 获取发货信息（公司/快递单号）shop_ddgl_kd
    public RequestHandle shop_ddgl_kd(OnDataListener onDataListener, String phone, String id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", phone);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("id", id);
        hashMap.put("_版本", "商家");
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_ddgl_kd, ConstantUtils.TAG_shop_ddgl_kd);
    }

    // 查看物流shop_ddgl_ckwl
    public RequestHandle shop_ddgl_ckwl(OnDataListener onDataListener, String phone, String id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", phone);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("id", id);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_kuaidi_query, ConstantUtils.TAG_shop_kuaidi_query);
    }


    //我的店铺
    public RequestHandle shop_sj_dp(OnDataListener onDataListener, String phone) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", phone);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("_版本", "商家");
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_sj_dp, ConstantUtils.TAG_shop_sj_dp);
    }

    //    //修改店铺
//    public RequestHandle shop_sj_bianji(OnDataListener onDataListener, String phone, String dianpumingcheng, String dianputubiao, String dianzhaotupian) {
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("账号", phone);
//        hashMap.put("随机码", ConstantUtils.random);
//        hashMap.put("店铺名称", dianpumingcheng);
//        hashMap.put("店铺图标", dianputubiao);
//        hashMap.put("店招图片", dianzhaotupian);
//        return doPost(onDataListener, hashMap, ConstantUtils.FUNC_shop_sj_bianji, ConstantUtils.TAG_shop_sj_bianji);
//    }
    //修改店铺
    public RequestHandle shop_sj_bianji(OnDataListener onDataListener, String phone, String dianpumingcheng, String dianputubiao, String dianzhaotupian, String leimu, String jieshao, String dianpuId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", phone);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("店铺名称", dianpumingcheng);
        hashMap.put("店铺图标", dianputubiao);
        hashMap.put("店招图片", dianzhaotupian);
        hashMap.put("主营类别", leimu);
        hashMap.put("店铺介绍", jieshao);
        hashMap.put("店铺id", dianpuId);

        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_sj_bianji, ConstantUtils.TAG_shop_sj_bianji);
    }


    //店铺首页 全部 分类 分类二级接口
    public RequestHandle shop_sj_sy(OnDataListener onDataListener, String phone, String fenlei, String hengfu) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", phone);
        hashMap.put("随机码", ConstantUtils.random);
        if (fenlei.equals("首页") || fenlei.equals("全部") || fenlei.equals("分类")) {
            hashMap.put("类别", fenlei);
        } else {
            hashMap.put("分类", "横幅列表");
        }
        if (!hengfu.equals("")) {
            hashMap.put("横幅列表", hengfu);
        }
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_sj_sy, ConstantUtils.TAG_shop_sj_sy);
    }

    //实名认证
//    public RequestHandle shop_smrztj(OnDataListener onDataListener, String phone, String zhenshimingzi, String shenfenzhenghao, String shouchi, String zhengmian, String fanmian) {
//        HashMap<String, Object> hashMap = new HashMap<>();
//        JSONObject obj = new JSONObject();
//        try {
//            obj.put("手持身份证照片", shouchi);
//            obj.put("身份证正面", zhengmian);
//            obj.put("身份证背面", fanmian);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        hashMap.put("账号", phone);
//        hashMap.put("随机码", ConstantUtils.random);
//        hashMap.put("真实名", zhenshimingzi);
//        hashMap.put("身份证号", shenfenzhenghao);
//        try {
//            hashMap.put("身份证照片", URLEncoder.encode(obj.toString(), "UTF-8"));
//            Log.e("身份证照片", URLEncoder.encode(obj.toString(), "UTF-8"));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return doPost(onDataListener, hashMap, ConstantUtils.FUNC_shop_smrztj, ConstantUtils.TAG_shop_smrztj);
//    }

    //实名认证(无照片版)
    public RequestHandle shopo_sj_smlz(OnDataListener onDataListener, String phone, String zhenshimingzi, String shenfenzhenghao) {
        HashMap<String, Object> hashMap = new HashMap<>();
        JSONObject obj = new JSONObject();
        hashMap.put("账号", phone);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("姓名", zhenshimingzi);
        hashMap.put("_版本", "商家");
        hashMap.put("身份证号", shenfenzhenghao);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shopo_sj_smlz, ConstantUtils.TAG_shopo_sj_smlz);
    }

    //shop_smyzpanduan实名认证 判断是否认证过了
    public RequestHandle shop_sj_sz(OnDataListener onDataListener, String phone) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", phone);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("_版本", "商家");
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_sj_sz, ConstantUtils.TAG_shop_sj_sz);
    }
//    //实名认证判断
//    public RequestHandle shop_shimingrenzhengpanduan (OnDataListener onDataListener, String phone) {
//        HashMap<String, Object> hashMap = new HashMap<>();
//
//        hashMap.put("账号", phone);
//        hashMap.put("随机码", ConstantUtils.random);
//        return doPost(onDataListener,hashMap,ConstantUtils.FUNC_shop_shimingrenzhengpanduan,ConstantUtils.TAG_shop_shimingrenzhengpanduan);
//    }


    //商品管理
    public RequestHandle shop_sx_manages(OnDataListener onDataListener, String phone, String shenqingyeshu, String xiaoshouyeshu, String xiajiayeshu) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", phone);
        hashMap.put("type", "1");
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("申请页数", shenqingyeshu);
        hashMap.put("销售页数", xiaoshouyeshu);
        hashMap.put("下架页数", xiajiayeshu);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_sx_manages, ConstantUtils.TAG_shop_sx_manages);
    }

    //商品下架
    public RequestHandle shop_sx_manages_out(OnDataListener onDataListener, String phone, String id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", phone);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("id", id);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_sx_manages_out, ConstantUtils.TAG_shop_sx_manages_out);
    }

    //修改密码
    public RequestHandle update_pwd(OnDataListener onDataListener, String phone, String oldpwd, String newpwd, String renewpwd) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", phone);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("旧密码", oldpwd);
        hashMap.put("新密码", newpwd);
        hashMap.put("确认密码", renewpwd);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_update_pwd, ConstantUtils.TAG_update_pwd);
    }

    //店铺商品详情 shop_sj_spxq
    public RequestHandle shop_sj_spxq(OnDataListener onDataListener, String phone, String id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", phone);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("id", id);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_sj_spxq, ConstantUtils.TAG_shop_sj_spxq);
    }

    //shop_sj_ddxq 商品收益详情
    public RequestHandle shop_sj_ddxq(OnDataListener onDataListener, String phone, String yue, String yeshu) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", phone);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("月", yue);
        hashMap.put("页数", yeshu);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_sj_ddxq, ConstantUtils.TAG_shop_sj_ddxq);

    }

    //shop_sj_ddxq推荐收益详情
    public RequestHandle shop_sj_tjxq(OnDataListener onDataListener, String phone, String yeshu) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", phone);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("页数", yeshu);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_sj_tjxq, ConstantUtils.TAG_shop_sj_tjxq);
    }

    //商家总收益
    public RequestHandle shop_sj_zong(OnDataListener onDataListener, String phone) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", phone);
        hashMap.put("随机码", ConstantUtils.random);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_sj_dzong, ConstantUtils.TAG_shop_sj_dzong);
    }

    //提取
    public RequestHandle shop_sj_sjtq(OnDataListener onDataListener, String phone, String jifen, String mima) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String str = dateFormat.format(new Date()) + phone;
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", phone);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("积分", jifen);
        hashMap.put("密码", CommonUtils.Md5(mima));
        hashMap.put("说明", "收益转红利");
        hashMap.put("兑换id", str);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_sj_sjtq, ConstantUtils.TAG_shop_sj_sjtq);
    }

    //搜索
    public RequestHandle shop_sj_sou(OnDataListener onDataListener, String phone, String sousuo) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", phone);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("搜索", sousuo);
        hashMap.put("页数", "0");
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_sj_sou, ConstantUtils.TAG_shop_sj_sou);
    }

    //我的红利ijilu
    public RequestHandle shop_myhl(OnDataListener onDataListener, String phone) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", phone);
        hashMap.put("随机码", ConstantUtils.random);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_myhl, ConstantUtils.TAG_shop_myhl);
    }

    // 查看物流shop_ddgl_ckwl
    public RequestHandle shop_kuaidi_query(OnDataListener onDataListener, String phone, String id, String shangpinId, String saleId, String state) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", phone);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("id", id);
        hashMap.put("商品id", shangpinId);
        hashMap.put("订单销售id", saleId);
        hashMap.put("物流类型", state);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_kuaidi_query, ConstantUtils.TAG_shop_kuaidi_query);
    }

    //获取省
    public RequestHandle getSheng(OnDataListener onDataListener, String phone) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", phone);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("_版本", "商家");
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_sj_sheng, ConstantUtils.TAG_shop_sj_sheng);
    }

    //获取市
    public RequestHandle getShi(OnDataListener onDataListener, String phone, String shengId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", phone);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("_版本", "商家");
        hashMap.put("省id", shengId);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_sj_shi, ConstantUtils.TAG_shop_sj_shi);
    }

    //获取区
    public RequestHandle getQu(OnDataListener onDataListener, String phone, String shiId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", phone);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("_版本", "商家");
        hashMap.put("市id", shiId);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_sj_qu, ConstantUtils.TAG_shop_sj_qu);
    }

    //获得主营类目
    public RequestHandle getLeimu(OnDataListener onDataListener) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("类别", "一级");
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_s_uplm, ConstantUtils.TAG_shop_s_uplm);
    }

    //获得二级类目
    public RequestHandle getLeimuEr(OnDataListener onDataListener, String id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("类别", "二级");
        hashMap.put("一级id", id);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_s_uplm, ConstantUtils.TAG_shop_s_uplm2);
    }

    //获得三级类目
    public RequestHandle getLeimuSan(OnDataListener onDataListener, String id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("类别", "三级");
        hashMap.put("二级id", id);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_s_uplm, ConstantUtils.TAG_shop_s_uplm3);
    }


    //创建店铺（新）
    public RequestHandle shop_wl_newshop(OnDataListener onDataListener, String phone, String dianpumingcheng, String dianzhaoicon, String dianpuicon, String leibie, String jieshao) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", phone);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("_版本", "商家");
        hashMap.put("店铺图标", dianpuicon);
        hashMap.put("店招图片", dianzhaoicon);
        hashMap.put("店铺名称", dianpumingcheng);
        hashMap.put("主营类别", leibie);
        hashMap.put("店铺介绍", jieshao);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_newshop, ConstantUtils.TAG_shop_wl_newshop);
    }

    //新店铺管理
    public RequestHandle shop_s_storeM(OnDataListener onDataListener, String phone) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", phone);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("_版本", "商家");
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_s_storeM, ConstantUtils.TAG_shop_s_storeM);
    }


    //商家推荐人信息(1)
    public RequestHandle shop_sj_regA(OnDataListener onDataListener, String phone, String tuijianren_phone, String tuijianren_name, String gonghuoshang_id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", phone);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("服务商手机号", tuijianren_phone);
//      hashMap.put("服务商姓名", tuijianren_name);
        hashMap.put("供货商id", gonghuoshang_id);
        hashMap.put("_版本", "商家");
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_sj_regA, ConstantUtils.TAG_shop_sj_regA);
    }

    //商品上传判断
    public RequestHandle shop_sj_spec(OnDataListener onDataListener, String phone) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", phone);
        hashMap.put("随机码", ConstantUtils.random);
//        hashMap.put("供货商", gonghuoshang);
        hashMap.put("_版本", "商家");
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_sj_spec, ConstantUtils.TAG_shop_sj_spec);
    }

    //    //店铺认证
//    public RequestHandle dianPuRenZheng(OnDataListener onDataListener, String phone, String farenxingming, String yingyezhizhao, List<String> qitazizhilist) {
//        HashMap<String, Object> hashMap = new HashMap<>();
//        JSONObject YYJson = new JSONObject();
//        JSONObject QTJson = new JSONObject();
//        JSONArray YYtupian_jsonArr = new JSONArray();
//        JSONArray QTtupian_jsonArr = new JSONArray();
//        for (int i = 0; i < qitazizhilist.size(); i++) {
//            JSONObject jsonObject = new JSONObject();
//            try {
//                jsonObject.put("图片", qitazizhilist.get(i));
//                QTtupian_jsonArr.put(jsonObject);
//                QTJson.put("", QTtupian_jsonArr);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        try {
//            JSONObject jsonObj = new JSONObject();
//            jsonObj.put("图片", yingyezhizhao);
//            YYtupian_jsonArr.put(jsonObj);
//            YYJson.put("", YYtupian_jsonArr);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        try {
//            hashMap.put("账号", phone);
//            hashMap.put("随机码", ConstantUtils.random);
//            hashMap.put("_版本", "商家");
//            hashMap.put("法人姓名", farenxingming);
//            hashMap.put("营业执照", URLEncoder.encode(YYJson.toString(), "UTF-8"));
//            hashMap.put("其它资质", URLEncoder.encode(QTJson.toString(), "UTF-8"));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return doPost(onDataListener, hashMap, ConstantUtils.FUNC_shop_dianpu_renzheng, ConstantUtils.TAG_shop_dianpu_renzheng);
//    }
    //店铺认证
    public RequestHandle dianPuRenZheng(OnDataListener onDataListener, String phone, String farenxingming, String yingyezhizhao, List<String> qitazizhilist, String pingzheng) {
        HashMap<String, Object> hashMap = new HashMap<>();
        JSONArray YYtupian_jsonArr = new JSONArray();
        JSONArray QTtupian_jsonArr = new JSONArray();
        if (qitazizhilist.size() > 0) {
            for (int i = 0; i < qitazizhilist.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("图片", qitazizhilist.get(i));
                    QTtupian_jsonArr.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("图片", yingyezhizhao);
            YYtupian_jsonArr.put(jsonObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            hashMap.put("账号", phone);
            hashMap.put("随机码", ConstantUtils.random);
            hashMap.put("_版本", "商家");
            hashMap.put("法人姓名", farenxingming);
            hashMap.put("信用凭证", pingzheng);
            hashMap.put("营业执照", URLEncoder.encode(YYtupian_jsonArr.toString(), "UTF-8"));
            if (qitazizhilist.size() > 0) {
                hashMap.put("其它资质", URLEncoder.encode(QTtupian_jsonArr.toString(), "UTF-8"));
            } else {
                hashMap.put("其它资质", "[]");
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_dianpu_renzheng, ConstantUtils.TAG_shop_dianpu_renzheng);
    }

    //认证标准
    public RequestHandle renZhengBiaoZhun(OnDataListener onDataListener, String phone, String biaoti) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", phone);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("_版本", "商家");
        hashMap.put("标题", biaoti);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_dianpu_renzheng_xize, ConstantUtils.TAG_shop_dianpu_renzheng_xize);
    }

    //    //获得订单
//    public RequestHandle shop_s_ddgl_Info(OnDataListener onDataListener, String id) {
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("订单id", id);
////        hashMap.put("随机码", ConstantUtils.random);
//        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_s_ddgl_Info, ConstantUtils.TAG_shop_s_ddgl_Info);
//    }
    //获得订单详情
    public RequestHandle shop_s_ddgl_Info(OnDataListener onDataListener, String phone, String id, String dinagdanzhuangtai) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", phone);
        hashMap.put("订单id", id);
        hashMap.put("version", "1.3.0");
        hashMap.put("订单状态", dinagdanzhuangtai);
        hashMap.put("随机码", ConstantUtils.random);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_s_ddgl_Info, ConstantUtils.TAG_shop_s_ddgl_Info);
    }

    //商家服务
    public RequestHandle shop_sj_fw(OnDataListener onDataListener, String phone, String mingchen) {

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", phone);
        hashMap.put("名称", mingchen);
        hashMap.put("_版本", "商家");
        hashMap.put("随机码", ConstantUtils.random);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_sj_fw, ConstantUtils.TAG_shop_sj_fw);
    }

    //商家服务立即享有按钮
    public RequestHandle shop_sj_ljxyA(OnDataListener onDataListener, String phone, String taocan) {

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", phone);
        hashMap.put("升级套餐", taocan);
        hashMap.put("_版本", "商家");
        hashMap.put("随机码", ConstantUtils.random);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_sj_ljxyA, ConstantUtils.TAG_shop_sj_ljxyA);
    }


    //获取客户信息
    public RequestHandle shop_t_Info(OnDataListener onDataListener, String phone, ArrayList list) {
        HashMap<String, Object> hashMap = new HashMap<>();
        JSONArray user_jsonArr = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("id", list.get(i));
                user_jsonArr.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            hashMap.put("账号", phone);
            hashMap.put("随机码", ConstantUtils.random);
            hashMap.put("user", URLEncoder.encode(user_jsonArr.toString(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_t_Info, ConstantUtils.TAG_shop_t_Info);
    }


    //    //设置账号
//    public RequestHandle shop_set_zhanghao(OnDataListener onDataListener,String phone, String shezhiZhanghao) {
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("账号", phone);
//        hashMap.put("随机码", ConstantUtils.random);
//        hashMap.put("更改账号", shezhiZhanghao);
//        hashMap.putAll(CommonUtils.loadMap(ConstantUtils.SP_PHONEINFO_JSON));
//        for(Map.Entry entry : hashMap.entrySet()){
//            Log.e(entry.getKey().toString() , entry.getValue().toString());
//        }
//        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_set_zhanghao, ConstantUtils.TAG_shop_set_zhanghao);
//    }
    //验证密码
    public RequestHandle shop_sp_wd(OnDataListener onDataListener, String phone, String verifyPassword) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", phone);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("verifyPassword", verifyPassword);
        hashMap.putAll(CommonUtils.loadMap(ConstantUtils.SP_PHONEINFO_JSON));
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_sp_wd, ConstantUtils.TAG_shop_sp_wd);
    }

    //设置手机号
    public RequestHandle shop_set_tel(OnDataListener onDataListener, String phone, String newPhone) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", phone);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("更改手机号", newPhone);
        hashMap.putAll(CommonUtils.loadMap(ConstantUtils.SP_PHONEINFO_JSON));
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_set_tel, ConstantUtils.TAG_shop_set_tel);
    }

    //获取服务商姓名
    public RequestHandle shangjia_sp_info(OnDataListener onDataListener, String phone, String fuwushangPhone) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", phone);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("服务商手机号", fuwushangPhone);
        hashMap.putAll(CommonUtils.loadMap(ConstantUtils.SP_PHONEINFO_JSON));
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shangjia_sp_info, ConstantUtils.TAG_shangjia_sp_info);
    }

    //重置服务商
    public RequestHandle shop_sp_chongzhi(OnDataListener onDataListener, String phone, String fuwushang_phone) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", phone);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("_版本", "商家");
        hashMap.put("服务商手机号", fuwushang_phone);
        hashMap.putAll(CommonUtils.loadMap(ConstantUtils.SP_PHONEINFO_JSON));
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_sp_chongzhi, ConstantUtils.TAG_shop_sp_chongzhi);
    }

    //商品上传 账号  商品名称   发货地址id  运费 商品描述  类目     主营类目  商品类型 商城类别  图片列表
    public RequestHandle shop_sp_uploadA(OnDataListener onDataListener,
                                         String phone, String shangpinmingchen,
                                         String fahuodizhiID, String yunfei,
                                         List<MiaoShuBean> allMiaoshu, String leimu,
                                         String zhuyingleimu, String shangpinleixing, String shangchengleibie, List<String> picUrlList, String dizhi, String shangpinxiangqingid, String[] dizhiId, String yiji, String erji, String sanji, String yunfeiId, String productId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        JSONObject jsonMiaoshu = new JSONObject();
        JSONArray jsonArrayMiaoshu = new JSONArray();
        JSONObject jsonPicture = new JSONObject();
        JSONArray jsonArrayPictures = new JSONArray();
        //商品描述重组
        for (int i = 0; i < allMiaoshu.size(); i++) {
            JSONObject jsonobj = new JSONObject();
            try {
                //描述jsonArray组建
                jsonobj.put("描述", allMiaoshu.get(i).getMiaoshu().replaceAll("'", "’"));
                jsonobj.put("图片", allMiaoshu.get(i).getTupian());
                jsonArrayMiaoshu.put(jsonobj);
                jsonMiaoshu.put("列表", jsonArrayMiaoshu);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //商城图片重组
        for (String url : picUrlList) {
            JSONObject object = new JSONObject();
            try {
                object.put("图片", url);
                jsonArrayPictures.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        try {
            hashMap.put("账号", phone);
            hashMap.put("随机码", ConstantUtils.random);
            hashMap.put("商品名称", shangpinmingchen);
            hashMap.put("缩略图", URLEncoder.encode(jsonArrayPictures.toString(), "UTF-8"));
            //Log.e("TAG", "shop_sj_shangchuang:缩略图格式" + URLEncoder.encode(jsonArrayPictures.toString()));
//            hashMap.put("发货地址id", fahuodizhiID);
            hashMap.put("地址", dizhi);
            hashMap.put("快递费", yunfei);
            hashMap.put("内容", URLEncoder.encode(jsonMiaoshu.toString(), "UTF-8"));
            hashMap.put("分类", leimu);
            hashMap.put("类别名称", zhuyingleimu);
            hashMap.put("商品类型", shangpinleixing);
            hashMap.put("名称", shangchengleibie);
            hashMap.put("商品详情id", shangpinxiangqingid);
            hashMap.put("省id", dizhiId[1]);
            hashMap.put("市id", dizhiId[2]);
            hashMap.put("区id", dizhiId[3]);
            hashMap.put("一级id", yiji);
            hashMap.put("二级id", erji);
            hashMap.put("三级id", sanji);
            hashMap.put("运费id", yunfeiId);
            hashMap.put("商品id", productId);
            // Log.e("TAG", "shop_sj_shangchuang:wwwwwww "+JsonUtils.createJSON(hashMap).toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_sp_uploadA, ConstantUtils.TAG_shop_sp_uploadA);
    }

    //商品上传规格
    public RequestHandle shop_sp_addSpec(OnDataListener onDataListener, String phone, List<GuigeModel> guigeModels, String productId) {

        JSONArray jsonArrayGuige = new JSONArray();
        //商品规格重组
        for (int i = 0; i < guigeModels.size(); i++) {
            JSONObject jsonobj2 = new JSONObject();
            try {
                //规格jsonArray组建
                jsonobj2.put("颜色", guigeModels.get(i).getColor().replaceAll("'", "’"));
                jsonobj2.put("尺寸", guigeModels.get(i).getSize().replaceAll("'", "’"));
                jsonobj2.put("供货价", guigeModels.get(i).getGonghuojia());
                jsonobj2.put("建议零售价", guigeModels.get(i).getJianyijia());
                jsonobj2.put("市场价", guigeModels.get(i).getJianyijia());
                jsonobj2.put("规格", guigeModels.get(i).getGuige().replaceAll("'", "’"));
                jsonobj2.put("库存", guigeModels.get(i).getKucun());
                jsonArrayGuige.put(jsonobj2);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", phone);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("_版本", "商家");
        try {
            hashMap.put("商品属性", URLEncoder.encode(jsonArrayGuige.toString(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        hashMap.put("商品id", productId);
        hashMap.putAll(CommonUtils.loadMap(ConstantUtils.SP_PHONEINFO_JSON));
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_sp_addSpec, ConstantUtils.TAG_shop_sp_addSpec);
    }


    //今日头条
    public RequestHandle shop_theadlines(OnDataListener onDataListener, String zhanghao) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_theadlines, ConstantUtils.TAG_shop_theadlines);
    }


    //首页改版
    public RequestHandle shop_wl_top(OnDataListener onDataListener, String zhanghao) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
//        hashMap.put("店铺id", dianPuId);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_top, ConstantUtils.TAG_shop_wl_top);
    }

    //商品统计
    public RequestHandle shop_sta_shop(OnDataListener onDataListener, String zhanghao) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_sta_shop, ConstantUtils.TAG_shop_sta_shop);
    }

    //商品统计
    public RequestHandle shop_sta_benefit(OnDataListener onDataListener, String zhanghao) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_sta_benefit, ConstantUtils.TAG_shop_sta_benefit);
    }

    //访客统计
    public RequestHandle shop_sta_visitor(OnDataListener onDataListener, String zhanghao) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_sta_visitor, ConstantUtils.TAG_shop_sta_visitor);
    }


    //订单统计
    public RequestHandle shop_sta_order(OnDataListener onDataListener, String zhanghao) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_sta_order, ConstantUtils.TAG_shop_sta_order);
    }

    //判断随机码
    public RequestHandle shop_sp_sjm(OnDataListener onDataListener, String zhanghao, String suijima) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("商随机码", suijima);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_sp_sjm, ConstantUtils.TAG_shop_sp_sjm);
    }

    //商品收益，红利记录(徐超)
    public RequestHandle shop_wl_sp(OnDataListener onDataListener, String zhanghao, int yeshu) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("页数", yeshu);
        hashMap.put("version", "2.4.0");
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_Erecord, ConstantUtils.TAG_shop_wl_Erecord);
    }

    //实名认证
    public RequestHandle real_name(OnDataListener onDataListener, String zhanghao, String shoujihao, String num, String name) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("手机号", shoujihao);
        hashMap.put("身份证号", num);
        hashMap.put("真实姓名", name);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_real_name, ConstantUtils.TAG_real_name);
    }

    //运费模板(查询)
    public RequestHandle shop_wl_exFeeModel(OnDataListener onDataListener, String zhanghao) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("pick", "locating");
        hashMap.put("随机码", ConstantUtils.random);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_exFeeModel, ConstantUtils.TAG_shop_wl_exFeeModel);
    }

    //运费模板(删除)
    public RequestHandle shop_wl_exFeeModel(OnDataListener onDataListener, String zhanghao, String id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("pick", "remove");
        hashMap.put("id", id);
        hashMap.put("随机码", ConstantUtils.random);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_exFeeModel, ConstantUtils.TAG_shop_wl_exFeeModel_delete);
    }

    //运费模板(修改，上传)
    public RequestHandle shop_wl_exFeeModel(OnDataListener onDataListener, String zhanghao, String pick, FreightTemplateModel model) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("pick", pick);
        JSONObject modelobj = new JSONObject();
        try {
            modelobj.put("名称", model.getName().replaceAll("'", "’"));
            modelobj.put("包邮", model.getFreeExpress().get() ? "是" : "否");
            if (!model.getFreeExpress().get()) {
                modelobj.put("类型", model.getPriceType().substring(1, 3));
                modelobj.put("运费", model.getFreight());
                modelobj.put("单位", model.getUnit());
                modelobj.put("加添", model.getAdd());
                modelobj.put("加额", model.getAddNum());
                modelobj.put("限定类型", model.getLimitersType());
                modelobj.put("限定条件", model.getLimiters());
                if ("按件数".equals(model.getPriceType())) {
                    modelobj.put("规格", "0");
                } else {
                    modelobj.put("规格", model.getStrandard());
                }
            } else {
                modelobj.put("类型", "");
                modelobj.put("运费", "0");
                modelobj.put("单位", "0");
                modelobj.put("加添", "0");
                modelobj.put("加额", "0");
                modelobj.put("规格", "0");
                modelobj.put("限定类型", "");
                modelobj.put("限定条件", "0");
            }
            modelobj.put("店铺id", ConstantUtils.dianpuId);
            hashMap.put("model", URLEncoder.encode(modelobj.toString(), "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if ("edit".equals(pick)) {
            hashMap.put("id", model.getId());
        }
        hashMap.put("随机码", ConstantUtils.random);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_exFeeModel, ConstantUtils.TAG_shop_wl_exFeeModel);
    }

    //运费模板(修改，上传)（传入商品id，为特定商品绑定运费模板）
    public RequestHandle shop_wl_exFeeModel(OnDataListener onDataListener, String zhanghao, String pick, String tradeId, FreightTemplateModel model) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("pick", pick);
        JSONObject modelobj = new JSONObject();
        try {
            modelobj.put("名称", model.getName().replaceAll("'", "’"));
            modelobj.put("包邮", model.getFreeExpress().get() ? "是" : "否");
            if (!model.getFreeExpress().get()) {
                modelobj.put("类型", model.getPriceType().substring(1, 3));
                modelobj.put("运费", model.getFreight());
                modelobj.put("单位", model.getUnit());
                modelobj.put("加添", model.getAdd());
                modelobj.put("加额", model.getAddNum());
                modelobj.put("限定类型", model.getLimitersType());
                modelobj.put("限定条件", model.getLimiters());
                if ("按件数".equals(model.getPriceType())) {
                    modelobj.put("规格", "0");
                } else {
                    modelobj.put("规格", model.getStrandard());
                }
            } else {
                modelobj.put("类型", "");
                modelobj.put("运费", "0");
                modelobj.put("单位", "0");
                modelobj.put("加添", "0");
                modelobj.put("加额", "0");
                modelobj.put("规格", "0");
                modelobj.put("限定类型", "");
                modelobj.put("限定条件", "");
            }
            if (TextUtils.isEmpty(ConstantUtils.dianpuId)) {
                modelobj.put("店铺id", "0");
            } else {
                modelobj.put("店铺id", ConstantUtils.dianpuId);
            }

            hashMap.put("model", URLEncoder.encode(modelobj.toString(), "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(tradeId)) {
            hashMap.put("商品id", tradeId);
        }
        hashMap.put("随机码", ConstantUtils.random);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_exFeeModel, ConstantUtils.TAG_shop_wl_exFeeModel);
    }

    //热门资讯
    public RequestHandle shop_wl_theadlinesA(OnDataListener onDataListener, String zhanghao) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_theadlinesA, ConstantUtils.TAG_shop_wl_theadlinesA);
    }

    //关于我们
    public RequestHandle shop_wl_my(OnDataListener onDataListener, String zhanghao) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_my, ConstantUtils.TAG_shop_wl_my);
    }

    //商品下架
    public RequestHandle shop_wl_shelevs(OnDataListener onDataListener, String zhanghao, List<ShangpinGuanliBean> spgl, String xiajia_yuanyin) {
        HashMap<String, Object> hashMap = new HashMap<>();
        JSONArray jsonArray = new JSONArray();
        String id = "";
        for (int i = 0; i < spgl.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            ShangpinGuanliBean spBean = spgl.get(i);
            try {
                jsonObject.put("id", spBean.getId());
                jsonObject.put("商品名称", spBean.getShangpinmingcheng());
                jsonArray.put(jsonObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            hashMap.put("id", URLEncoder.encode(jsonArray.toString(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        hashMap.put("下架原因", xiajia_yuanyin);
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_shelevs, ConstantUtils.TAG_shop_wl_shelevs);
    }

    //商品上架
    public RequestHandle shop_wl_shelf(OnDataListener onDataListener, String zhanghao, List<ShangpinGuanliBean> spgl) {
        HashMap<String, Object> hashMap = new HashMap<>();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < spgl.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            ShangpinGuanliBean spBean = spgl.get(i);
            try {
                jsonObject.put("id", spBean.getId());
                jsonObject.put("商品名称", spBean.getShangpinmingcheng());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            hashMap.put("id", URLEncoder.encode(jsonArray.toString(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_shelf, ConstantUtils.TAG_shop_wl_shelf);
    }

    //商品上架(单个)
    public RequestHandle shop_wl_shelf(OnDataListener onDataListener, String zhanghao, String id, String name) {
        HashMap<String, Object> hashMap = new HashMap<>();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("商品名称", name);
            jsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            hashMap.put("id", URLEncoder.encode(jsonArray.toString(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_shelf, ConstantUtils.TAG_shop_wl_shelf);
    }

    //商品上架(单个)
    public RequestHandle shop_wl_shelfA(OnDataListener onDataListener, String zhanghao, String id, String freightId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", id);
        hashMap.put("运费id", freightId);
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_shelfA, ConstantUtils.TAG_shop_wl_shelfA);
    }


    //通知
    public RequestHandle shop_wl_notice(OnDataListener onDataListener, String zhanghao) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_notice, ConstantUtils.TAG_shop_wl_notice);
    }


    //支付验证码
    public RequestHandle shop_wl_payment(OnDataListener onDataListener, String zhanghao) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("手机号", zhanghao);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_payment, ConstantUtils.TAG_shop_wl_payment);
    }

    //设置支付密码
    public RequestHandle shop_wl_setpay(OnDataListener onDataListener, String zhanghao, String mima, String queren_mima, String yzm, String phone) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("支付密码", CommonUtils.Md5(mima));
        hashMap.put("确认密码", CommonUtils.Md5(queren_mima));
        hashMap.put("手机号", phone);
        hashMap.put("验证码", yzm);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_setpay, ConstantUtils.TAG_shop_wl_setpay);
    }

    //生活模块
    public RequestHandle getLifeData(OnDataListener onDataListener) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("radio", "生活");
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_things_hp_lifeB, ConstantUtils.TAG_GETLIFEDATA);
    }

    //退货地址
    public RequestHandle getTuiHuoDiZhi(OnDataListener onDataListener, String zhanghao, ShouhuoAddress address, String type) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("type", type);
        switch (type) {
            case "show":
                break;
            case "del":
                hashMap.put("id", address.getId());
                break;
            case "add":
                hashMap.put("省", address.getProvince());
                hashMap.put("市", address.getCity());
                hashMap.put("区", address.getDistrict());
                hashMap.put("收货人", address.getShouhuoren());
                hashMap.put("默认地址", address.getDefaultAddress());
                hashMap.put("详细地址", address.getDetailedAddress());
                hashMap.put("手机号", address.getPhoneNum());
                break;
            case "edit":
                hashMap.put("id", address.getId());
                hashMap.put("省", address.getProvince());
                hashMap.put("市", address.getCity());
                hashMap.put("区", address.getDistrict());
                hashMap.put("收货人", address.getShouhuoren());
                hashMap.put("默认地址", address.getDefaultAddress());
                hashMap.put("详细地址", address.getDetailedAddress());
                hashMap.put("手机号", address.getPhoneNum());
                break;
        }
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_send, ConstantUtils.TAG_shop_wl_send);
    }

    //超级商家收款记录
    public RequestHandle shop_wl_address(OnDataListener onDataListener, String zhanghao, String id, int yeshu) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("唯一id", id);
        hashMap.put("页数", yeshu);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_address, ConstantUtils.TAG_shop_wl_address);
    }

    public RequestHandle GetUserInfo(OnDataListener onDataListener, String userId, String type, String zhanghao) {
        HashMap<String, Object> hashMap = new HashMap<>();
        if (userId.length() > 11) {
            userId = userId.substring(0, 11);
        }
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("user", userId);
        hashMap.put("type", type);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_hp_Info, ConstantUtils.TAG_shop_hp_Info);
    }

    public RequestHandle ReturnAddress(OnDataListener onDataListener, String zhanghao) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_p_return_address, ConstantUtils.TAG_p_return_address);
    }

    //查询商品信息
    public RequestHandle shop_wl_query(OnDataListener onDataListener, String zhanghao, String id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("商品id", id);
        hashMap.put("随机码", ConstantUtils.random);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_query, ConstantUtils.TAG_shop_wl_query);
    }

    //商品上传 账号  商品名称   发货地址id  运费 商品描述  类目     主营类目  商品类型 商城类别  图片列表
    public RequestHandle shop_wl_editshop(OnDataListener onDataListener,
                                          String phone, String shangpinmingchen,
                                          String fahuodizhiID, String yunfei,
                                          List<MiaoShuBean> allMiaoshu, String leimu,
                                          String zhuyingleimu, String shangpinleixing, String shangchengleibie, List<String> picUrlList, String dizhi, String shangpinxiangqingid, String[] dizhiId, String yiji, String erji, String sanji, String yunfeiId, String productId, List<GuigeModel> guigeModels) {
        HashMap<String, Object> hashMap = new HashMap<>();
        JSONObject jsonMiaoshu = new JSONObject();
        JSONArray jsonArrayMiaoshu = new JSONArray();
        JSONObject jsonPicture = new JSONObject();
        JSONArray jsonArrayPictures = new JSONArray();
        //商品描述重组
        for (int i = 0; i < allMiaoshu.size(); i++) {
            JSONObject jsonobj = new JSONObject();
            try {
                //描述jsonArray组建
                jsonobj.put("描述", allMiaoshu.get(i).getMiaoshu().replaceAll("'", "’"));
                jsonobj.put("图片", allMiaoshu.get(i).getTupian());
                jsonArrayMiaoshu.put(jsonobj);
                jsonMiaoshu.put("列表", jsonArrayMiaoshu);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //商城图片重组
        for (String url : picUrlList) {
            JSONObject object = new JSONObject();
            try {
                object.put("图片", url);
                jsonArrayPictures.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        JSONArray jsonArrayGuige = new JSONArray();
        //商品规格重组
        for (int i = 0; i < guigeModels.size(); i++) {
            JSONObject jsonobj2 = new JSONObject();
            try {
                //规格jsonArray组建
                jsonobj2.put("颜色", guigeModels.get(i).getColor().replaceAll("'", "’"));
                jsonobj2.put("尺寸", guigeModels.get(i).getSize().replaceAll("'", "’"));
                jsonobj2.put("供货价", guigeModels.get(i).getGonghuojia());
                jsonobj2.put("建议零售价", guigeModels.get(i).getJianyijia());
                jsonobj2.put("市场价", guigeModels.get(i).getJianyijia());
                jsonobj2.put("规格", guigeModels.get(i).getGuige().replaceAll("'", "’"));
                jsonobj2.put("库存", guigeModels.get(i).getKucun());
                jsonobj2.put("id", guigeModels.get(i).getId());
                jsonArrayGuige.put(jsonobj2);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            hashMap.put("账号", phone);
            hashMap.put("随机码", ConstantUtils.random);
            hashMap.put("商品名称", shangpinmingchen);
            hashMap.put("缩略图", URLEncoder.encode(jsonArrayPictures.toString(), "UTF-8"));
            //Log.e("TAG", "shop_sj_shangchuang:缩略图格式" + URLEncoder.encode(jsonArrayPictures.toString()));
//            hashMap.put("发货地址id", fahuodizhiID);
//            hashMap.put("地址", dizhi);
            hashMap.put("快递费", yunfei);
            hashMap.put("内容", URLEncoder.encode(jsonMiaoshu.toString(), "UTF-8"));
//            hashMap.put("分类", leimu);
//            hashMap.put("类别名称", zhuyingleimu);
//            hashMap.put("商品类型", shangpinleixing);
//            hashMap.put("名称", shangchengleibie);
//            hashMap.put("商品详情id", shangpinxiangqingid);
//            hashMap.put("省id", dizhiId[1]);
//            hashMap.put("市id", dizhiId[2]);
//            hashMap.put("区id", dizhiId[3]);
//            hashMap.put("一级id", yiji);
//            hashMap.put("二级id", erji);
//            hashMap.put("三级id", sanji);
            hashMap.put("运费id", yunfeiId);
            hashMap.put("商品id", productId);
            hashMap.put("商品属性", URLEncoder.encode(jsonArrayGuige.toString(), "UTF-8"));
            // Log.e("TAG", "shop_sj_shangchuang:wwwwwww "+JsonUtils.createJSON(hashMap).toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_editshop, ConstantUtils.TAG_shop_wl_editshop);
    }


    //商品分润(李嘉豪)
    public RequestHandle shop_wl_Profit(OnDataListener onDataListener, String zhanghao, int yeshu) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("页数", yeshu);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_Profit, ConstantUtils.TAG_shop_wl_Profit);
    }


    // 获取超级商家信息
    public RequestHandle ChaoJIShangJiaInfo(OnDataListener onDataListener, String onlyID, String zhanghao) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("onlyId", onlyID);
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_CHAOJISHANGJIAINFO, ConstantUtils.TAG_CHAOJISHANGJIAINFO);
    }

    // 向超级商家付款
    public final static int TAG_PAYCHAOJISHANGJIA = 203;

    public RequestHandle PayChaoJIShangJia(OnDataListener onDataListener, String onlyID, String zhanghu, String money, String orderNum, String pwd, String beizhu, String zhanghao) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("onlyId", onlyID);
        hashMap.put("付款账户", zhanghu);
        hashMap.put("转账金额", money);
        hashMap.put("转账订单号", orderNum);
        hashMap.put("支付密码", MD5Utlis.Md5(pwd));
        hashMap.put("备注", beizhu);
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_PAYCHAOJISHANGJIA, ConstantUtils.TAG_PAYCHAOJISHANGJIA);
    }

    //创建优惠券(李家豪)
    public RequestHandle shop_establish_coupon(OnDataListener onDataListener, String zhanghao, String type, String couponname, String miane, String faxingliang,
                                               String tiaojian, String shengxiaotime, String shixiaotime, String id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("种类", type);
        hashMap.put("优惠券名称", couponname);
        hashMap.put("面额", miane);
        hashMap.put("发行量", faxingliang);
        hashMap.put("限领", "1");
        hashMap.put("使用条件", tiaojian);
        hashMap.put("生效时间", shengxiaotime);
        hashMap.put("失效时间", shixiaotime);
        hashMap.put("商品id", id);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_establish_coupon, ConstantUtils.TAG_shop_establish_coupon);
    }


    //满额包邮查询(李家豪)
    public RequestHandle shop_wl_Freeship_query(OnDataListener onDataListener, String zhanghao) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_Freeship_query, ConstantUtils.TAG_shop_wl_Freeship_query);
    }

    //满额包邮(李家豪)
    public RequestHandle shop_wl_Freeship(OnDataListener onDataListener, HashMap<String, Object> hashMap) {
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_Freeship, ConstantUtils.TAG_shop_wl_Freeship);
    }


    //优惠券管理(李家豪)
    public RequestHandle shop_wl_Coupon(OnDataListener onDataListener, String zhanghao, String page, String type) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("类型", type);
        hashMap.put("页数", page);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_Coupon, ConstantUtils.TAG_shop_wl_Coupon);
    }

    //停止优惠券(李家豪)
    public RequestHandle shop_wl_stopcoupon(OnDataListener onDataListener, String zhanghao, String id, String status) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("优惠券id", id);
        hashMap.put("类型", status);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_stopcoupon, ConstantUtils.TAG_shop_wl_stopcoupon);
    }

    //发布内容(余光宝)
    public RequestHandle shop_wl_releasedynamic(OnDataListener onDataListener, String zhanghao, String sole, List<ImageTextModel> list, String type, String zhanshitu, String biaoti, String shiping, DynamicProductModel model) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("唯一id", sole);
        hashMap.put("随机码", ConstantUtils.random);
        if (type.equals("图文")) {
            hashMap.put("发布标识", type);
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < list.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                ImageTextModel spBean = list.get(i);
                try {
                    jsonObject.put("内容", spBean.getNeirong());
                    jsonObject.put("图片", spBean.getTupian());
                    jsonObject.put("展示图", zhanshitu);
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            hashMap.put("图文数据", jsonArray.toString());
            hashMap.put("视频地址", "");
            hashMap.put("展示图", zhanshitu);
            hashMap.put("标题", biaoti);
        } else if ("视频".equals(type)) {
            hashMap.put("发布标识", type);
            hashMap.put("视频地址", shiping);
            hashMap.put("展示图", "");
            hashMap.put("视频图片", zhanshitu);
            hashMap.put("标题", biaoti);
        } else {
            if (null != model) {
                hashMap.put("标题", model.getTitle());
                JSONArray array = new JSONArray();
                List<ProductModel> productModelList;
                if (model.isMulit()) {
                    hashMap.put("发布标识", "多商");
                    productModelList = model.getProductModelsMulit();
                } else {
                    productModelList = model.getProductModelsSingle();
                    hashMap.put("发布标识", "单商");
                }
                for (ProductModel productModel : productModelList) {
                    try {
                        JSONObject object = new JSONObject();
                        if (!model.isMulit()) {
                            object.put("展示图", model.getTitleImg());
                        }
                        object.put("商品价格", productModel.getPrice());
                        object.put("内容", productModel.getDescribe());
                        object.put("商品名称", productModel.getTitle());
//                        JSONArray address = new JSONArray();
                        HashMap<String, Object> product = new HashMap<>();
//                        JSONObject product = new JSONObject();
                        product.put("商品id", productModel.getId());
                        product.put("图片数量", productModel.getNum());

//                        address.put(product);
                        object.put("图片地址", URLEncoder.encode(JsonUtils.createJSON(product).toString(), "UTF-8"));
                        object.put("图片", productModel.getLink());
                        array.put(object);
                    } catch (JSONException e) {
                        Log.e("TAG", "shop_wl_releasedynamic: " + e.getMessage());
                    } catch (Exception e) {
                        Log.e("TAG", "shop_wl_releasedynamic: " + e.getMessage());
                    }
                }
                try {
                    hashMap.put("商品数据", URLEncoder.encode(array.toString(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }

        //特殊服务器地址（http://183.239.170.178:8007/api.post）

        //打印接口请求信息
        Log.e("HttpRequest", "此次请求的FUNC为：" + ConstantUtils.FUNC_p_add_findTable + "，请求信息如下：");
        String requestContent = "";
        Iterator<Map.Entry<String, Object>> iterator = hashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> itEntry = iterator.next();
            Object value = itEntry.getValue();
            if (value != null && !TextUtils.isEmpty(value.toString())) {
                itEntry.setValue(value.toString().replaceAll("'", "’"));
            }
            requestContent += itEntry.getKey() + "=" + itEntry.getValue() + "\n";
        }
        Log.e("HttpRequest", requestContent);
        return HttpManager.doPost(ConstantUtils.TAG_p_add_findTable, getParams(ConstantUtils.FUNC_p_add_findTable, hashMap), onDataListener, 0);
    }

    //拒绝原因
    public RequestHandle DDoS(OnDataListener onDataListener, String zhanghao) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_reason, ConstantUtils.TAG_shop_wl_reason);
    }

    //拒绝原因提交
    public RequestHandle JuJueYuanYinTiJiao(OnDataListener onDataListener, String zhanghao, String shuoming, String cause, String id, String type, String shangpinId, String repairId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("拒绝说明", shuoming);
        hashMap.put("拒绝原因", cause);
        hashMap.put("订单id", id);
        hashMap.put("商品id", shangpinId);
        hashMap.put("申请类型", type);
        hashMap.put("id", repairId);
        hashMap.put("随机码", ConstantUtils.random);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_Refuse, ConstantUtils.TAG_shop_wl_Refuse);
    }


    //同意退货(李嘉豪)
    public RequestHandle shop_wl_tyth(OnDataListener onDataListener, String zhanghao, String Dingdanid, String tuikuanzhanghao, String id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("订单id", Dingdanid);
        hashMap.put("退货人账号", tuikuanzhanghao);
        hashMap.put("id", id);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_tyth, ConstantUtils.TAG_shop_wl_tyth);
    }

    //退货退款(徐超)
    public RequestHandle Return_wl_goods(OnDataListener onDataListener, String zhanghao, String tuihuorenzhanghao, String Dingdanid, String ShangPingID, String id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("退货人账号", tuihuorenzhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("订单id", Dingdanid);
        hashMap.put("商品id", ShangPingID);
        hashMap.put("id", id);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_Return_wl_goods, ConstantUtils.TAG_Return_wl_goods);
    }


    //退款申请
    public RequestHandle TuiKuanShenQing(OnDataListener onDataListener, String zhanghao, String applyfor, String id, String shangpingID, String tradeId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("申请", applyfor);
        hashMap.put("订单id", id);
        hashMap.put("id", tradeId);
        hashMap.put("商品id", shangpingID);
        hashMap.put("随机码", ConstantUtils.random);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_record, ConstantUtils.TAG_shop_wl_record);
    }


    //添加银行卡
    public RequestHandle TianJiaYinHangKa(OnDataListener onDataListener, String zhanghao, String kahao, String name, String yanzhengma, String shoujihao) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("银行名", name);
        hashMap.put("银行卡号", kahao);
        hashMap.put("验证码", yanzhengma);
        hashMap.put("手机号", shoujihao);
        hashMap.put("随机码", ConstantUtils.random);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_p_bindingCard_three, ConstantUtils.TAG_p_bindingCard_three);
    }

    //获取验证码
    public RequestHandle shop_wl_monsms(OnDataListener onDataListener, String forget_password_phone) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("手机号", forget_password_phone);
        hashMap.put("随机码", ConstantUtils.random);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_monsms, ConstantUtils.TAG_shop_wl_monsms);
    }

    //选择银行卡类型
    public RequestHandle XuanZeYinHangKaLeiXing(OnDataListener onDataListener, String zhanghao) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_p_bank_type_two, ConstantUtils.TAG_p_bank_type_two);
    }

    //获取银行卡
    public RequestHandle HuoQvYinHangKa(OnDataListener onDataListener, String zhanghao) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_Bankcard, ConstantUtils.TAG_shop_wl_Bankcard);
    }

    //手动退款
    public RequestHandle Manual_wl_refund(OnDataListener onDataListener, String phone, String zhanghao, String id, String shangpingID, String repairID) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", phone);
        hashMap.put("退款人账号", zhanghao);
        hashMap.put("订单id", id);
        hashMap.put("商品id", shangpingID);
        hashMap.put("id", repairID);
        hashMap.put("随机码", ConstantUtils.random);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_Manual_wl_refund, ConstantUtils.TAG_Manual_wl_refund);
    }

    public RequestHandle JieBangYinHangKa(OnDataListener onDataListener, String id, String paypwd, String zhanghao) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("银行id", id);
        hashMap.put("支付密码", MD5Utlis.Md5(paypwd));
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_p_bindbankcard_two, ConstantUtils.TAG_p_bindbankcard_two);
    }

    public RequestHandle GetMyQRcode(OnDataListener onDataListener, String zhanghao) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_p_get_QR, ConstantUtils.TAG_p_get_QR);
    }

    public RequestHandle sel_shop_kefu(OnDataListener onDataListener, String zhanghao) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_sel_shop_kefu, ConstantUtils.TAG_sel_shop_kefu);
    }

    //退款详情
    public RequestHandle shop_wl_SuccessRefund(OnDataListener onDataListener, String zhanghao, String id, String shangpingID, String repairId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("订单id", id);
        hashMap.put("商品id", shangpingID);
        hashMap.put("id", repairId);
        hashMap.put("随机码", ConstantUtils.random);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_SuccessRefund, ConstantUtils.TAG_shop_wl_SuccessRefund);
    }

    //发商品 商品列表
    public RequestHandle shop_wl_choiceshop(OnDataListener onDataListener, String zhanghao, String yeshu, String condition, String search, String type) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("页数", yeshu);
        hashMap.put("condition", condition);
        hashMap.put("搜索", search);
        hashMap.put("随机码", ConstantUtils.random);
        if ("activitys".equals(type)) {//从参与活动过来请求商品列表
            return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_shoplist, ConstantUtils.TAG_shop_wl_choiceshop);
        } else if ("shopPromotion".equals(type)) {
            return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_promotion, ConstantUtils.TAG_shop_wl_choiceshop);
        } else {
            return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_choiceshop, ConstantUtils.TAG_shop_wl_choiceshop);
        }
    }

    //订单管理  订单时间
    public RequestHandle shop_wl_OrderTime(OnDataListener onDataListener, String zhanghao) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("订单状态", "待发货");
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_OrderTime, ConstantUtils.TAG_shop_wl_OrderTime);
    }

    //订单管理晒选时间shop_wl_RefundTime(徐超)
    public RequestHandle shop_wl_RefundTime(OnDataListener onDataListener, String zhanghao, String time) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("订单状态", "待发货");
        hashMap.put("订单时间", time);
        hashMap.put("页数", "0");
        hashMap.put("随机码", ConstantUtils.random);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_RefundTime, ConstantUtils.TAG_shop_wl_RefundTime);
    }

    //shop_wl_FailureReason(李家豪)商品作废原因
    public RequestHandle shop_wl_FailureReason(OnDataListener onDataListener, String zhanghao, String id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("商品id", id);
        hashMap.put("随机码", ConstantUtils.random);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_FailureReason, ConstantUtils.TAG_shop_wl_FailureReason);
    }
    //评论列表

    public RequestHandle shop_wl_recomments(OnDataListener onDataListener, String zhanghao, String type, String yeshu) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("商家账号", zhanghao);
        hashMap.put("type", "待回复".equals(type) ? "0" : "1");
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("页数", yeshu);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_recomments, ConstantUtils.TAG_shop_wl_recomments);
    }

    //商品评论列表
    public RequestHandle shop_sh_recomments(OnDataListener onDataListener, String zhanghao, String shangpinId, String yeshu) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("商品id", shangpinId);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("页数", yeshu);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_sh_recomments, ConstantUtils.TAG_shop_sh_recomments);
    }

    //回复评论
    public RequestHandle shop_comment_reply(OnDataListener onDataListener, String zhanghao, String id, String reply) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("id", id);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("reply", reply);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_comment_reply, ConstantUtils.TAG_shop_comment_reply);
    }

    //回复评论
    public RequestHandle shop_wl_delshops(OnDataListener onDataListener, String zhanghao, String id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("商品id", id);
        hashMap.put("随机码", ConstantUtils.random);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_delshops, ConstantUtils.TAG_shop_wl_delshops);
    }

    //商品收益详情
    public RequestHandle shop_gain_recording_xq(OnDataListener onDataListener, String zhanghao, String id, String explain) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("id", id);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("version", "10000");
        hashMap.put("explain", explain);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_gain_recording_xq, ConstantUtils.TAG_shop_gain_recording_xq);
    }

    //退款退货详情
    public RequestHandle shop_wl_rf_goodsDetail(OnDataListener onDataListener, String zhanghao, String id, String orderId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("id", id);
        hashMap.put("订单id", orderId);
        hashMap.put("随机码", ConstantUtils.random);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_rf_goodsDetail, ConstantUtils.TAG_shop_wl_rf_goodsDetail);
    }

    //退款详情
    public RequestHandle shop_wl_rf_details(OnDataListener onDataListener, String zhanghao, String id, String orderId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("id", id);
        hashMap.put("订单id", orderId);
        hashMap.put("随机码", ConstantUtils.random);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_rf_details, ConstantUtils.TAG_shop_wl_rf_details);
    }

    //未处理订单详情
    public RequestHandle shop_wl_prompt(OnDataListener onDataListener, String zhanghao) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_prompt, ConstantUtils.TAG_shop_wl_prompt);
    }

    //快速编辑
    public RequestHandle shop_edit_shopPropety(OnDataListener onDataListener, String zhanghao, String productId, List<GuigeModel> guigeModels) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("商品id", productId);
        JSONArray jsonArrayGuige = new JSONArray();
        //商品规格重组
        for (int i = 0; i < guigeModels.size(); i++) {
            JSONObject jsonobj2 = new JSONObject();
            try {
                //规格jsonArray组建
                jsonobj2.put("颜色", guigeModels.get(i).getColor().replaceAll("'", "’"));
                jsonobj2.put("尺寸", guigeModels.get(i).getSize().replaceAll("'", "’"));
                jsonobj2.put("供货价", guigeModels.get(i).getGonghuojia());
                jsonobj2.put("建议零售价", guigeModels.get(i).getJianyijia());
                jsonobj2.put("市场价", guigeModels.get(i).getJianyijia());
                jsonobj2.put("规格", guigeModels.get(i).getGuige().replaceAll("'", "’"));
                jsonobj2.put("库存", guigeModels.get(i).getKucun());
                jsonobj2.put("id", guigeModels.get(i).getId());
                jsonArrayGuige.put(jsonobj2);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            hashMap.put("商品属性", URLEncoder.encode(jsonArrayGuige.toString(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_edit_shopPropety, ConstantUtils.TAG_shop_edit_shopPropety);
    }

    //快速编辑
    public RequestHandle shop_edit_shopPropety1(OnDataListener onDataListener, String zhanghao, String productId, List<SpecificationModel> guigeModels) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("商品id", productId);
        JSONArray jsonArrayGuige = new JSONArray();
        //商品规格重组
        for (int i = 0; i < guigeModels.size(); i++) {
            JSONObject jsonobj2 = new JSONObject();
            try {
                //规格jsonArray组建
                jsonobj2.put("颜色", guigeModels.get(i).getColor().replaceAll("'", "’"));
                jsonobj2.put("尺寸", guigeModels.get(i).getSize().replaceAll("'", "’"));
                jsonobj2.put("供货价", guigeModels.get(i).getSupplyPrice());
                jsonobj2.put("建议零售价", guigeModels.get(i).getRetailPrice());
                jsonobj2.put("市场价", guigeModels.get(i).getRetailPrice());
                jsonobj2.put("规格", guigeModels.get(i).getSpecification().replaceAll("'", "’"));
                jsonobj2.put("库存", guigeModels.get(i).getStock());
                jsonobj2.put("id", guigeModels.get(i).getId());
                jsonArrayGuige.put(jsonobj2);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            hashMap.put("商品属性", URLEncoder.encode(jsonArrayGuige.toString(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_edit_shopPropety, ConstantUtils.TAG_shop_edit_shopPropety);
    }


    //未处理订单详情
    public RequestHandle shop_sp_grade(OnDataListener onDataListener, String zhanghao, String shopGrade, String page) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("店铺等级", shopGrade);
        hashMap.put("页数", page);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_sp_grade, ConstantUtils.TAG_shop_sp_grade);
    }

    //荣誉分详情
    public RequestHandle shop_sp_gradeDetail(OnDataListener onDataListener, String zhanghao, String id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("订单销售id", id);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_sp_gradeDetail, ConstantUtils.TAG_shop_sp_gradeDetail);
    }

    //店铺等级查询
    public RequestHandle shop_sh_honnr(OnDataListener onDataListener, String zhanghao) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("商品id", "");
        hashMap.put("店铺id", "");
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_sh_honnr, ConstantUtils.TAG_shop_sh_honnr);
    }

    // 客服中心
    public RequestHandle KeHuZhongXin(OnDataListener onDataListener, String zhanghao) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_KEHU_ZHONGXIN, ConstantUtils.TAG_KEHU_ZHONGXIN);
    }

    // 选择客服位置
    public RequestHandle SelectKeFuLocation(OnDataListener onDataListener, String zhanghao, int regionID) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("区域id", regionID);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_KEHU_SELECT_LOCATION, ConstantUtils.TAG_KEHU_SELECT_LOCATION);
    }

    // 选择客服
    public RequestHandle SelectKeFu(OnDataListener onDataListener, String zhanghao, String cityID, String cityName, String systemKeFu) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        if (!TextUtils.isEmpty(cityID)) {
            hashMap.put("市id", cityID);
        } else if (!TextUtils.isEmpty(cityName)) {
            hashMap.put("客服组", cityName);
        } else if (!TextUtils.isEmpty(systemKeFu)) {
            hashMap.put("客服组", systemKeFu);
        }
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_KEHU_SELECT, ConstantUtils.TAG_KEHU_SELECT);
    }

    // 线下交易说明
    public RequestHandle shop_busExplain(OnDataListener onDataListener, String zhanghao) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_busExplain, ConstantUtils.TAG_shop_busExplain);
    }

    // 商家线下交易发货
    public RequestHandle shop_wl_Linegoods(OnDataListener onDataListener, String zhanghao, List<String> repairId, String code) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        JSONArray array = new JSONArray();
        for (int i = 0; i < repairId.size(); i++) {
            JSONObject object = new JSONObject();
            try {
//                object.put("商品id", shangpinID.get(i));
                object.put("订单销售id", repairId.get(i));
                array.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            hashMap.put("订单销售id", URLEncoder.encode(array.toString(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        hashMap.put("收货码", code);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_Linegoods, ConstantUtils.TAG_shop_wl_Linegoods);
    }

    // 创建积分折扣活动
    public RequestHandle shop_wl_Create_activity(OnDataListener onDataListener, String zhanghao, String id, String deductionPercent) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("商品id", id);
        hashMap.put("积分抵扣", deductionPercent);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_Create_activity, ConstantUtils.TAG_shop_wl_Create_activity);
    }

    // 编辑活动
    public RequestHandle shop_wl_Editorial_activities(OnDataListener onDataListener, String zhanghao, String type, String oldPercent, String oldId, String newPercent, String newId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("type", type);
        hashMap.put("OldId", oldId);
        if (!oldId.equals(newId)) {
            hashMap.put("NewId", newId);
            hashMap.put("积分抵扣1", newPercent);
            hashMap.put("积分抵扣", oldPercent);
        } else {
            hashMap.put("NewId", "");
            hashMap.put("积分抵扣", newPercent);
        }
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_Editorial_activities, ConstantUtils.TAG_shop_wl_Editorial_activities);
    }

    // 积分活动列表
    public RequestHandle shop_wl_activity(OnDataListener onDataListener, String zhanghao, String page) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("页数", page);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_activity, ConstantUtils.TAG_shop_wl_activity);
    }

    // 提取收益界面信息
    public RequestHandle shop_scsy_tx_Version(OnDataListener onDataListener, String zhanghao) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("version", "1.7.1");
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_scsy_tx_Version, ConstantUtils.TAG_shop_scsy_tx_Version);
    }

    // 提取收益
    public RequestHandle shop_profitExchange_Version(OnDataListener onDataListener, String zhanghao, String bankid, String jifen, String onlyid, String zhifumima) {

        return shop_profitExchange_Version(onDataListener, zhanghao, bankid, jifen, onlyid, zhifumima, false);
    }


    // 提取收益
    public RequestHandle shop_profitExchange_Version(OnDataListener onDataListener, String zhanghao, String bankid, String jifen, String onlyid, String zhifumima, boolean isZhiFuBao) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("积分", jifen);
        hashMap.put("onlyId", onlyid);
        hashMap.put("支付密码", zhifumima);
        hashMap.put("version", "1.7.1");
        hashMap.put("到账类别", "支付宝");
        String func;
        if (isZhiFuBao) {
            hashMap.put("支付宝id", bankid);
            func = ConstantUtils.FUNC_shop_profitExchange_Version_zhifubao;
        } else {
            hashMap.put("银行卡id", bankid);
            func = ConstantUtils.FUNC_shop_profitExchange_Version;
        }
        return doPost_New(onDataListener, hashMap, func, ConstantUtils.TAG_shop_profitExchange_Version);
    }


    // 缴纳信誉保证金详情
    public RequestHandle shop_gain_recording_bank_xq(OnDataListener onDataListener, String zhanghao, String id, String type) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("id", id);
        hashMap.put("type", type);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_gain_recording_bank_xq, ConstantUtils.TAG_shop_gain_recording_bank_xq);
    }


    // 提现到银行卡详情
    public RequestHandle shop_billNotice_goBank_xq(OnDataListener onDataListener, String zhanghao, String id, String type) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("id", id);
        hashMap.put("type", type);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_billNotice_goBank_xq, ConstantUtils.TAG_shop_billNotice_goBank_xq);
    }

    // 支付宝账单详情
    public RequestHandle shop_wl_billDetail(OnDataListener onDataListener, String zhanghao, String id, String type) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("id", id);
        hashMap.put("title", type);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_billDetail, ConstantUtils.TAG_shop_wl_billDetail);
    }

    // 提现到银行卡详情
    public RequestHandle shop_wl_attribute(OnDataListener onDataListener, String zhanghao, String type) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("类型名称", type);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_attribute, ConstantUtils.TAG_shop_wl_attribute);
    }


    // 信誉保证金介绍
    public RequestHandle shop_bzj_jieshao(OnDataListener onDataListener, String zhanghao) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_bzj_jieshao, ConstantUtils.TAG_shop_bzj_jieshao);
    }

    // 信誉保证金金额
    public RequestHandle shop_bzj_jine(OnDataListener onDataListener, String zhanghao) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_bzj_jine, ConstantUtils.TAG_shop_bzj_jine);
    }

    // 缴纳信誉保证金
    public RequestHandle shop_bzj_jiaona(OnDataListener onDataListener, String zhanghao, String money1, String money2, String pwd) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("缴纳金额", money1);
        hashMap.put("待缴纳金额", money2);
        hashMap.put("密码", pwd);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_bzj_jiaona, ConstantUtils.TAG_shop_bzj_jiaona);
    }


    // 充值
    public RequestHandle shop_chongzhi(OnDataListener onDataListener, String zhanghao, String money) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("充值金额", money);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_chongzhi, ConstantUtils.TAG_shop_chongzhi);
    }

    // 缴纳记录
    public RequestHandle shop_jiaonajilu(OnDataListener onDataListener, String zhanghao, String yeshu) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("页数", yeshu);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_jiaonajilu, ConstantUtils.TAG_shop_jiaonajilu);
    }

    // 商品推广基本信息
    public RequestHandle shop_wl_extension(OnDataListener onDataListener, String zhanghao) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_extension, ConstantUtils.TAG_shop_wl_extension);
    }

    // 商品推广下单
    public RequestHandle shop_wl_extension_order(OnDataListener onDataListener, String zhanghao, ShopPromotionModel model, String pwd) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("商品id", model.getProcuctId());
        hashMap.put("类别", model.getType());
        hashMap.put("位置", model.getSecondType());
        hashMap.put("金额", model.getTotal());
        hashMap.put("单价", model.getPrice());
        hashMap.put("天数", model.getTime());
        hashMap.put("支付密码", pwd);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_extension_order, ConstantUtils.TAG_shop_wl_extension_order);
    }

    // 商品推广记录
    public RequestHandle shop_wl_extension_record(OnDataListener onDataListener, String zhanghao, String page) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("页数", page);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_extension_record, ConstantUtils.TAG_shop_wl_extension_record);
    }


    // 提现到支付宝详情
    public RequestHandle shop_gain_recording_ali_xq(OnDataListener onDataListener, String zhanghao, String id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("id", id);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_gain_recording_ali_xq, ConstantUtils.TAG_shop_gain_recording_ali_xq);
    }

    //我的支付宝天添加/删除
    public RequestHandle shop_aliacc_edit(OnDataListener onDataListener, String zhanghao, String addOrDel,
                                          String id, String aliaccout, String name, String paypwd, String onlyId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("version", "10100");
        hashMap.put("edit", addOrDel);
        hashMap.put("id", id);
        hashMap.put("支付密码", MD5Utlis.Md5(paypwd));
        hashMap.put("唯一id", onlyId);
        hashMap.put("aliaccout", aliaccout);
        hashMap.put("name", name);

        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_aliacc_edit, ConstantUtils.TAG_shop_aliacc_edit);
    }

    //我的支付宝列表
    public RequestHandle shop_aliacc_list(OnDataListener onDataListener, String zhanghao) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("version", "10100");

        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_aliacc_list, ConstantUtils.TAG_shop_aliacc_list);
    }

    //获取银行卡加支付宝
    public RequestHandle HuoQvYinHangKa110(OnDataListener onDataListener, String zhanghao, String cardType) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("version", "2.5.0");
        hashMap.put("平台", "商家");
        hashMap.put("type", cardType);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_Bankcard_Version110, ConstantUtils.TAG_shop_wl_Bankcard_Version110);
    }

    // 获取提现手续费
    public RequestHandle getTiXianShouXuFei(OnDataListener onDataListener, String zhanghao) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("平台", "商家");
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_getShouXuFei, ConstantUtils.TAG_shop_getShouXuFei);
    }

    //发送系统消息
    public RequestHandle shop_wl_mass(OnDataListener onDataListener, String zhanghao, SystemMessageModel model) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("文章标题", model.getTitle());
        hashMap.put("发送内容", model.getContent());
        hashMap.put("消息类型", model.getType());
        if (model.isEdit()) {
            hashMap.put("type", "edit");
            hashMap.put("编辑id", model.getId());
        } else {
            hashMap.put("type", "add");
            hashMap.put("编辑id", "");
        }
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_mass, ConstantUtils.TAG_shop_wl_mass);
    }


    //系统消息列表
    public RequestHandle shop_wl_mass_List(OnDataListener onDataListener, String zhanghao, String page) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("页数", page);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_mass_List, ConstantUtils.TAG_shop_wl_mass_List);
    }

    //系统消息详情
    public RequestHandle shop_wl_mass_detail(OnDataListener onDataListener, String zhanghao, String id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);
        hashMap.put("id", id);
        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_mass_detail, ConstantUtils.TAG_shop_wl_mass_detail);
    }

    //商品上传新
    public RequestHandle shop_wl_Uploading_goods(OnDataListener onDataListener, String zhanghao, com.gloiot.hygoSupply.ui.activity.postproduct.model.ProductModel model) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("账号", zhanghao);
        hashMap.put("随机码", ConstantUtils.random);

        JSONArray imageUrl = new JSONArray();
        for (String image : model.getImageUrl()) {
            JSONObject object = new JSONObject();
            try {
                object.put("图片", image);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            imageUrl.put(object);
        }
        try {
            hashMap.put("缩略图", URLEncoder.encode(imageUrl.toString(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (!TextUtils.isEmpty(model.getId())) {
            hashMap.put("商品id", model.getId());
        }
        hashMap.put("商品名称", model.getName());
        hashMap.put("商品类型", model.getType());
        hashMap.put("一级id", model.getFirstId());
        hashMap.put("二级id", model.getSecondId());
        hashMap.put("三级id", model.getThirdId());
        hashMap.put("分类", model.getFirstName() + ">" + model.getSecondName() + ">" + model.getThirdName());
        hashMap.put("省id", model.getProvinceId());
        hashMap.put("市id", model.getCityId());
        hashMap.put("区id", model.getCountyId());
        hashMap.put("运费id", model.getFreightTemplateModel().getId());

        JSONArray specification = new JSONArray();
        for (SpecificationModel specificationModel : model.getSpecificationModels()) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("颜色", specificationModel.getColor());
                jsonObject.put("尺寸", specificationModel.getSize());
                jsonObject.put("规格", specificationModel.getSpecification());
                jsonObject.put("库存", specificationModel.getStock());
                jsonObject.put("建议零售价", specificationModel.getRetailPrice());
                jsonObject.put("市场价", specificationModel.getRetailPrice());
                jsonObject.put("供货价", specificationModel.getSupplyPrice());
                specification.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            hashMap.put("商品属性", URLEncoder.encode(specification.toString(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONArray content = new JSONArray();

        for (ProductDetailsModel productDetailsModel : model.getProductDetailsModels()) {
            JSONObject productDetail = new JSONObject();
            JSONArray images = new JSONArray();
            for (String image : productDetailsModel.getImages()) {
                JSONObject objImage = new JSONObject();
                try {
                    objImage.put("图片", image);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                images.put(objImage);
            }
            try {
                productDetail.put("图片列表", images);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                productDetail.put("描述", productDetailsModel.getDescribe());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            content.put(productDetail);
        }
        try {
            hashMap.put("内容", URLEncoder.encode(content.toString(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (model.isPointSetting()) {
            hashMap.put("积分抵扣", model.getPointSetting());
        } else {
            hashMap.put("积分抵扣", "");
        }


        return doPost_New(onDataListener, hashMap, ConstantUtils.FUNC_shop_wl_Uploading_goods, ConstantUtils.TAG_shop_wl_Uploading_goods);
    }


}