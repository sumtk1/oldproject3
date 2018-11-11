package com.gloiot.hygoSupply.utlis;

import com.gloiot.hygoSupply.bean.MiaoShuBean;
import com.gloiot.hygoSupply.bean.ShangpinGuigeBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hygo00 on 2016/7/15.
 */
public class ConstantUtils {
    // TODO: 2017/6/2  更换正式
    // 银联支付mMode参数
    public static String UnionPayMode = "01";
    // 接口主路径测试版2138
    public static final String URL = "http://121.201.67.222:16990/api.post";
    // 超级商家收款地址测试
    public static String SUPERMERCHANT_URL = "http://192.168.0.64:3000/recharge.xhtml";
    // 商品上传须知测试
    public static String UPLOAD_INFORM_URL = "http://183.239.170.178:8007/Explain/release_goods.html";
    // 商品上传细则
    public static String UPLOAD_DETAIL_URL = "http://183.239.170.178:8007/Explain/clearing_rules.html";
    // 店铺等级说明
    public static String SHOPGRADEINSTRUCTION_URL = "http://121.201.67.222:18080/Explain/storeGrade.xhtml";
    // 指导说明正式
    public static String INSTRUCTION_URL = "https://service.glo-iot.com/Explain/guideIndex.html";
    // web js注入服务器地址测试
    public static String WEBJS_URL = "http://121.201.67.222:19990/ajax.post";
    // 缴纳保证金须知
    public static String BAOZHENGJINXUZHI_URL = "http://121.201.67.222:18080/Explain/creditNeeds.html";

//    // 银联支付mMode参数
//    public static String UnionPayMode = "00";
//    // 接口主路径正式版
//    public static final String URL = "https://hygo.zhenxuanzhuangyuan.com:8080/api.post";
//    // 商品上传须知正式
//    public static String UPLOAD_INFORM_URL = "https://hygo.zhenxuanzhuangyuan.com:2018/Explain/release_goods.html";
//    // 超级商家收款正式
//    public static String SUPERMERCHANT_URL = "https://hygo.zhenxuanzhuangyuan.com:2018/Gathering/pay.xhtml";
//    // 发动态服务器地址
//    public static final String DYNAMICURL = "http://183.239.170.178:8007/api.post";
//    // 商品上传细则正式
//    public static String UPLOAD_DETAIL_URL = "https://hygo.zhenxuanzhuangyuan.com:2018/Explain/clearing_rules.html";
//    // 店铺等级说明
//    public static String SHOPGRADEINSTRUCTION_URL = "https://hygo.zhenxuanzhuangyuan.com:2018/Explain/storeGrade.xhtml";
//    // 指导说明正式
//    public static String INSTRUCTION_URL = "https://hygo.zhenxuanzhuangyuan.com:2018/Explain/guideIndex.html";
//    // web js注入服务器地址
//    public static String WEBJS_URL = "https://hygo.zhenxuanzhuangyuan.com:8043/ajax.post";
//    // 缴纳保证金须知
//    public static String BAOZHENGJINXUZHI_URL = "https://service.glo-iot.com/Explain/creditNeeds.html";


    //微信appid
    public static String WXAPPID = "wxc60f32b23a68a0ed";

    //    public static String WXAPPID = "wx4d863985a4dfe452";
    //微信支付状态
    public static String SP_WXSTATE = "false";
    // 支付返回类型
    public static String SP_PAYTYPE = "SP_PAYTYPE";
    // 登录状态
    public static String SP_LOGINTYPE = "SP_LOGINTYPE";
    // 用户ID
    public static String SP_MYID = "SP_MYID";
    // 用户账号
    public static String SP_ZHANGHAO = "SP_ZHANGHAO";
    // 用户随机码
    public static String SP_RANDOMCODE = "SP_RANDOMCODE";
    // 用户头像
    public static String SP_USERIMG = "SP_USERIMG";
    // 用户头像是否更改
    public static String SP_CHANGEUSERIMG = "SP_CHANGEUSERIMG";
    // 用户名字
    public static String SP_USERNAME = "SP_USERNAME";
    // 用户性别
    public static String SP_USERSEX = "SP_USERSEX";
    // 用户昵称
    public static String SP_USERNICHENG = "SP_USERNICHENG";
    // 用户真实名
    public static String SP_TRUENAME = "SP_TRUENAME";
    // 是否绑定微信
    public static String SP_SHIFOUBANGDINGWX = "SP_SHIFOUBANGDINGWX";
    // 是否可以发送系统消息
    public static String SP_SENDMESSAGE = "SP_SENDMESSAGE";

    // 用户手机号
    public static String SP_USERPHONE = "SP_USERPHONE";
    // 用户昵称是否更改
    public static String SP_CHANGEUSERNICHENG = "SP_CHANGEUSERNICHENG";
    // 中油卡类别
    public static String SP_USERZYKTYPE = "SP_USERZYKTYPE";
    // 是否设置过支付密码
    public static String SP_MYPWD = "SP_MYPWD";
    // 商城类别
    public static String SP_USERSCTYPE = "SP_USERSCTYPE";
    // 用户密码
    public static String SP_USERPWD = "SP_USERPWD";
    // 用户密码
    public static String SP_TOKEN = "SP_TOKEN";
    // 融云被挤，登录状态
    public static Boolean SP_RONGCLOUD_LOGINTYPE = false;
    //店铺主营类别
    public static String SP_ZHUYINGLEIBIE = "SP_ZHUYINGLEIBIE";
    //店铺主营类别id
    public static String SP_ZHUYINGLEIBIEID = "SP_ZHUYINGLEIBIEID";
    //用户实名验证状态
    public static String SP_SHIMINGYANZHENG = "SP_SHIMINGYANZHENG";

    //判断是否有店铺
    public static String SP_YOUWUDIANPU = "SP_YOUWUDIANPU";
    //店铺id
    public static String SP_DIANPUID = "SP_DIANPUID";

    //指引图状态
    public static String SP_ZHIYIINGZHUANTAI = "SP_ZHIYIINGZHUANTAI";
    //所有商品规格
    public static List<ShangpinGuigeBean> SP_ALLSHANGPINGUIGE = new ArrayList<>();
    //所有商品描述
    public static List<MiaoShuBean> SP_ALLSHANGPINMIAOSHU = new ArrayList<>();
    //店铺是否修改
    public static Boolean SP_CHANGEDIANPU = false;
    //是否第一次打开商品详情
    public static String SP_SHANGPIN_XIANGQING = "0";

    //是否为超级商家
    public static String SP_SUPERMERCHANT = "SP_SUPERMERCHANT";
    //是否为自营账号
    public static String SP_ZIYING = "SP_ZIYING";
    //模版
    public static String SP_MOBAN = "SP_MOBAN";
    //通知
    public static String SP_INFORM = "SP_INFORM";
    // 用户绑定手机号码
    public static String SP_BANGDINGPHONE = "SP_BANGDINGPHONE";
    // 唯一id
    public static String SP_ONLYID = "SP_ONLYID";
    // 随机码
    public static String SP_RANDROM = "SP_RANDROM";


    //登陆接口
    public final static int TAG_shop_login = 0;
    //    public final static String FUNC_shop_login = "shop_sp_login";
//    public final static String FUNC_shop_login = "shop_wl_logins";
    public final static String FUNC_shop_login = "shop_new_loginA";

    //忘记密码接口
    public final static int TAG_test_password = 1;
    public final static String FUNC_test_password = "shop_wl_resspd";
    //验证码接口
    public final static int TAG_sms = 2;
    public final static String FUNC_sms = "shop_wl_sms";
    //商家注册接口
    public final static int TAG_shop_sj_reg = 3;
    public final static String FUNC_shop_sj_reg = "shop_sp_reg";

    //商家注册推荐人接口
    public final static int TAG_shangjia_info = 4;
    public final static String FUNC_shangjia_info = "shangjia_sp_info";
    //商家注册三商人接口
    public final static int TAG_shop_select = 5;
    public final static String FUNC_shop_select = "shop_select";
    //支付接口
    public final static int TAG_shop_paylist = 6;
    public final static String FUNC_shop_paylist = "shop_paylist";
    //商家推荐人信息接口
    public final static int TAG_shop_sj_tuijian = 7;
    public final static String FUNC_shop_sj_tuijian = "shop_sj_tuijian";
    //设置个人信息接口
    public final static int TAG_shop_set = 8;
    //    public final static String FUNC_shop_set = "shop_set";
    public final static String FUNC_shop_set = "shop_wl_set";
    //我的人脉接口
    public final static int TAG_shop_woderenmai_list = 9;
    //    public final static String FUNC_shop_woderenmai_list = "shop_woderenmai";
    public final static String FUNC_shop_woderenmai_list = "shop_woderenmai";
    //查询发货地址接口
    public final static int TAG_shop_fahuochaxun = 10;
    public final static String FUNC_shop_fahuochaxun = "shop_fahuochaxun";
    //默认发货地址接口
    public final static int TAG_shop_fahuomorendizhi = 11;
    public final static String FUNC_shop_fahuomorendizhi = "shop_fahuomorendizhi";
    //编辑发货地址接口
    public final static int TAG_shop_fahuobianji = 12;
    public final static String FUNC_shop_fahuobianji = "shop_fahuobianji";
    //删除发货地址接口
    public final static int TAG_shop_shanchu = 13;
    public final static String FUNC_shop_shanchu = "shop_shanchu";

    //订单详情接口
    public final static int TAG_shop_sy_ddxq_day = 14;
    //    public final static String FUNC_shop_sy_ddxq_day = "shop_sy_ddxq_day";
    public final static String FUNC_shop_sy_ddxq_day = "shop_wl_ddxq_day";
//    public final static String FUNC_shop_sy_ddxq_day = "shop_wl_ddxq_day";

    //    public final static String FUNC_shop_sy_ddxq_day = "shop_s_ddgl_Info";
    //今日收益接口
    public final static int TAG_shop_sy_today = 15;
    //    public final static String FUNC_shop_sy_today = "shop_sy_todayA";
    public final static String FUNC_shop_sy_today = "shop_wl_toady";
    //查询俱乐部名称接口
    public final static int TAG_shop_sj_jlb = 16;
    public final static String FUNC_shop_sj_jlb = "shop_sj_jlb";
    //上传商品类别接口
    public final static int TAG_shop_sanji_leibie = 17;
    public final static String FUNC_shop_sanji_leibie = "shop_sanjiliebiaochaxun";
    //订单管理--显示买家账号（无昵称）
    public final static int TAG_shop_ddgli_maijiazh = 19;
    public final static String FUNC_shop_ddgli_maijiazh = "shop_ddgli_maijiazh";
    //给前台传值物流公司数据(确认发货)
    public final static int TAG_shop_ddgl_fahuo = 20;
    //    public final static String FUNC_shop_ddgl_fahuo = "shop_wl_fahuoA";
//    public final static String FUNC_shop_ddgl_fahuo = "shop_wl_delivery";
    public final static String FUNC_shop_ddgl_fahuo = "shop_new_fahuo";

    //获取发货信息（公司/快递单号）
    public final static int TAG_shop_ddgl_kd = 21;
    public final static String FUNC_shop_ddgl_kd = "shop_wl_kdgs";
    //查看物流shop_kuaidi_query
    public final static int TAG_shop_kuaidi_query = 22;
    //    public final static String FUNC_shop_kuaidi_query = "shop_wl_kd";
    public final static String FUNC_shop_kuaidi_query = "shop_new_kd";

    //取消待付款订单shop_ddgli_quxiaodd
    public final static int TAG_shop_ddgli_quxiaodd = 23;
    public final static String FUNC_shop_ddgli_quxiaodd = "shop_ddgli_quxiaodd";

    //订单管理shop_ddgli
    public final static int TAG_shop_wl_refund = 24;
    //public final static String FUNC_shop_ddgli = "shop_ddgli";
//    public final static String FUNC_shop_ddgli = "shop_wl_ddgli";
//    public final static String FUNC_shop_ddgli = "shop_wl_manageA";
//    public final static String FUNC_shop_wl_refund = "shop_wl_manageB";
//    public final static String FUNC_shop_wl_refund = "shop_wl_refund";
//    public final static String FUNC_shop_wl_refund = "shop_wl_RefundTimeA";
    public final static String FUNC_shop_wl_refund = "shop_wl_RefundTimeB";

    //商品上传
    public final static int TAG_shop_shangpinshangchuan = 25;
    public final static String FUNC_shop_shangpinshangchuan = "shop_shangpinshangchuan";

    //商品上传（旧版）
    public final static int TAG_shop_sj_shangchuang = 25;
    //public final static String FUNC_shop_sj_shangchuang = "shop_sj_shangchuang";
    public final static String FUNC_shop_sj_shangchuang = "shop_wl_upload";
    //我的店铺
    public final static int TAG_shop_sj_dp = 26;
    public final static String FUNC_shop_sj_dp = "shop_wl_Mydp";
//    public final static String FUNC_shop_sj_dp = "shop_sp_Mydp";

    //编辑店铺
    public final static int TAG_shop_sj_dianpu = 27;
    public final static String FUNC_shop_sj_dianpu = "shop_wl_setshop";
    //修改
    public final static int TAG_shop_sj_bianji = 28;
    //    public final static String FUNC_shop_sj_bianji = "shop_sj_bianji";
    public final static String FUNC_shop_sj_bianji = "shop_wl_setshop";
    //店铺首页 全部 分类 分类二级列表
    public final static int TAG_shop_sj_sy = 29;
    //public final static String FUNC_shop_sj_sy = "shop_sj_syA";
//    public final static String FUNC_shop_sj_sy = "shop_wl_shopSyA";
    public final static String FUNC_shop_sj_sy = "shop_home_categories";//国外、国内
    //新版旧接口
//    public final static String FUNC_shop_sj_sy = "shop_sj_syA";

    //实名认证判断是否认证过了shop_smyzpanduan
    public final static int TAG_shop_shimingrenzhengpanduan = 30;
    public final static String FUNC_shop_shimingrenzhengpanduan = "shop_shimingrenzhengpanduan";
    public final static int TAG_shop_sj_sz = 30;
    public final static String FUNC_shop_sj_sz = "shop_wl_sz";
    //商品下架shop_sx_manages_out
    public final static int TAG_shop_sx_manages_out = 31;
    public final static String FUNC_shop_sx_manages_out = "shop_wl_manageo_out";
    //商品管理
    public final static int TAG_shop_sx_manages = 32;
    //    public final static String FUNC_shop_sx_manages = "shop_wl_spgl";
//    public final static String FUNC_shop_sx_manages = "shop_commodity_manage";//增加海外与国内商品的判断
    public final static String FUNC_shop_sx_manages = "shop_new_commodity_manage";//增加海外与国内商品的判断
    //实名认证
    public final static int TAG_shop_smrztj = 33;
    public final static String FUNC_shop_smrztj = "shop_wl_smlz";
    //实名认证（无照片版）
    public final static int TAG_shopo_sj_smlz = 33;
    public final static String FUNC_shopo_sj_smlz = "shop_wl_smlz";
    //修改密码
    public final static int TAG_update_pwd = 34;
    //    public final static String FUNC_update_pwd = "update_pwd";
    public final static String FUNC_update_pwd = "shop_wl_pwd";
    //商品详情 shop_sj_spxq
    public final static int TAG_shop_sj_spxq = 35;
    //public final static String FUNC_shop_sj_spxq = "shop_sj_spxq";
    //public final static String FUNC_shop_sj_spxq = "shop_wl_spxq";
//    public final static String FUNC_shop_sj_spxq = "shop_commodity_details";
//    public final static String FUNC_shop_sj_spxq = "shop_wl_detailsE_newA";
    public final static String FUNC_shop_sj_spxq = "shop_wl_detailsE_newB";


    //商品收益详情shop_sj_ddxq
    public final static int TAG_shop_sj_ddxq = 36;
    //    public final static String FUNC_shop_sj_ddxq = "shop_sj_ddxq";
    public final static String FUNC_shop_sj_ddxq = "shop_wl_ddxq";

    //推荐收益详情shop_sj_tjxq
    public final static int TAG_shop_sj_tjxq = 37;
    //    public final static String FUNC_shop_sj_tjxq = "shop_sj_tjxq";
    public final static String FUNC_shop_sj_tjxq = "shop_sj_tjxq";

    //商家总收益shop_sj_zong
    public final static int TAG_shop_sj_dzong = 38;
    //    public final static String FUNC_shop_sj_dzong = "shop_sj_dzong";
//    public final static String FUNC_shop_sj_dzong = "shop_wl_zong";
    public final static String FUNC_shop_sj_dzong = "shop_wl_earnings";

    //商家红利提取shop_sj_sy
    public final static int TAG_shop_sj_sjtq = 40;
    //    public final static String FUNC_shop_sj_sjtq = "shop_sj_sjtq";
//    public final static String FUNC_shop_sj_sjtq = "shop_wl_Extract";
    public final static String FUNC_shop_sj_sjtq = "shop_wl_ExtractA";

    //店铺搜索商品
    public final static int TAG_shop_sj_sou = 41;
    //    public final static String FUNC_shop_sj_sou = "shop_wl_souA";
    public final static String FUNC_shop_sj_sou = "shop_commodity_search";


    //shop_mhonglis我的红利
    public final static int TAG_shop_myhl = 42;
    public final static String FUNC_shop_myhl = "shop_sp_myhl";

    //根据倍数反类目
    public final static int TAG_shop_lm = 43;
    public final static String FUNC_shop_lm = "shop_lm";  //根据倍数反类目
    //得到省
    public final static int TAG_shop_sj_sheng = 44;
    public final static String FUNC_shop_sj_sheng = "shop_wl_sheng";
    //得到市
    public final static int TAG_shop_sj_shi = 45;
    public final static String FUNC_shop_sj_shi = "shop_wl_shi";
    //得到区
    public final static int TAG_shop_sj_qu = 46;
    public final static String FUNC_shop_sj_qu = "shop_wl_qu";
    //得到供货商
    public final static int TAG_shop_sj_supplier = 47;
    public final static String FUNC_shop_sj_supplier = "shop_sp_supplier";
    //得到一级类目
    public final static int TAG_shop_s_uplm = 48;
    public final static int TAG_shop_s_uplm2 = 187;
    public final static String FUNC_shop_s_uplm = "shop_wl_uplmA";
    //得到三级类目
    public final static int TAG_shop_s_uplm3 = 188;
    //创建店铺
    public final static int TAG_shop_wl_newshop = 49;
    //    public final static String FUNC_shop_wl_newshop = "shop_wl_newshop";
    public final static String FUNC_shop_wl_newshop = "shop_wl_foundshop";

    //店铺管理新
    public final static int TAG_shop_s_storeM = 50;
    public final static String FUNC_shop_s_storeM = "shop_wl_Storm";
    //保证金保障
    public final static int TAG_shop_sj_money = 51;
    public final static String FUNC_shop_sj_money = "shop_sj_money";

    //保证金保障下单
    public final static int TAG_shop_sj_payment = 52;
    public final static String FUNC_shop_sj_payment = "shop_sj_payment";

    //供货商推荐
    public final static int TAG_shop_sj_newsj = 53;
    public final static String FUNC_shop_sj_newsj = "shop_sj_newsj";

    //商品上传按钮判断
    public final static int TAG_shop_sj_spec = 54;
    public final static String FUNC_shop_sj_spec = "shop_wl_spec";
    //店铺认证
    public final static int TAG_shop_dianpu_renzheng = 55;
    //    public final static String FUNC_shop_dianpu_renzheng = "shop_wl_dprzA";
    public final static String FUNC_shop_dianpu_renzheng = "shop_wl_ShopOne";
    //认证细则
    public final static int TAG_shop_dianpu_renzheng_xize = 56;
    public final static String FUNC_shop_dianpu_renzheng_xize = "shop_wl_stand";
    //注册下单
    public final static int TAG_shop_sj_regA = 57;
    public final static String FUNC_shop_sj_regA = "shop_sp_tj";
    //得到二级类目
    public final static int TAG_shop_sj_sclm = 58;
    public final static String FUNC_shop_sj_sclm = "shop_sj_sclm";
    //得到订单详情
    public final static int TAG_shop_s_ddgl_Info = 59;
    //    public final static String FUNC_shop_s_ddgl_Info = "shop_wl_ddxq_day";
//    public final static String FUNC_shop_s_ddgl_Info = "shop_wl_orderInfo";
//    public final static String FUNC_shop_s_ddgl_Info = "shop_wl_orderInfoA";
    public final static String FUNC_shop_s_ddgl_Info = "shop_wl_orderInfoB";
    //商家服务
    public final static int TAG_shop_sj_fw = 60;
    public final static String FUNC_shop_sj_fw = "shop_sj_fw";
    //商家服务立即享有按钮
    public final static int TAG_shop_sj_ljxyA = 61;
    public final static String FUNC_shop_sj_ljxyA = "shop_sp_ljxyA";

    //获取ConversationList用户的信息
    public final static int TAG_shop_t_Info = 62;
    public final static String FUNC_shop_t_Info = "shop_sp_Info";
    //无需物流(电子卡卷)
    public final static int TAG_shop_sj_fahuoB = 63;
    //    public final static String FUNC_shop_sj_fahuoB = "shop_wl_fhb";
    public final static String FUNC_shop_sj_fahuoB = "shop_new_deliver";

    //更改账号
    public final static int TAG_shop_set_zhanghao = 64;
    public final static String FUNC_shop_set_zhanghao = "shop_set_zhanghao";

    //验证密码
    public final static int TAG_shop_sp_wd = 65;
    public final static String FUNC_shop_sp_wd = "shop_wl_wd";

    //修改手机号
    public final static int TAG_shop_set_tel = 66;
    public final static String FUNC_shop_set_tel = "shop_wl_tel";

    //服务商信息
    public final static int TAG_shangjia_sp_info = 67;
    public final static String FUNC_shangjia_sp_info = "shangjia_sp_info";
    //重置服务商
    public final static int TAG_shop_sp_chongzhi = 68;
    public final static String FUNC_shop_sp_chongzhi = "shop_sp_chongzhi";
    //商品上传
    public final static int TAG_shop_sp_uploadA = 69;
    //    public final static String FUNC_shop_sp_uploadA = "shop_wl_upshop";
    public final static String FUNC_shop_sp_uploadA = "shop_wl_shop_upload";
    //商品上传添加规格
    public final static int TAG_shop_sp_addSpec = 70;
    public final static String FUNC_shop_sp_addSpec = "shop_wl_addspec";
    //今日头条
    public final static int TAG_shop_theadlines = 71;
    public final static String FUNC_shop_theadlines = "shop_notification_content";
    //首页改版
    public final static int TAG_shop_wl_top = 72;
    public final static String FUNC_shop_wl_top = "shop_wl_topA";

    //商品统计
    public final static int TAG_shop_sta_shop = 73;
    public final static String FUNC_shop_sta_shop = "shop_wl_shopA";

    //收益统计
    public final static int TAG_shop_sta_benefit = 74;
    public final static String FUNC_shop_sta_benefit = "shop_wl_benefit";

    //访客统计
    public final static int TAG_shop_sta_visitor = 75;
    public final static String FUNC_shop_sta_visitor = "shop_wl_visitor";

    //店铺统计
    public final static int TAG_shop_sta_order = 76;
    public final static String FUNC_shop_sta_order = "shop_wl_order";

    //判断随机码
    public final static int TAG_shop_sp_sjm = 77;
    public final static String FUNC_shop_sp_sjm = "shop_wl_sjm";


    //商品收益，红利记录
    public final static int TAG_shop_wl_Erecord = 78;
    //    public final static String FUNC_shop_wl_Erecord = "shop_wl_sp";
//    public final static String FUNC_shop_wl_Erecord = "shop_wl_Erecord";
    public final static String FUNC_shop_wl_Erecord = "shop_gain_recording";


    //实名认证
    public final static int TAG_real_name = 79;
    public final static String FUNC_real_name = "shop_wl_smlzA";

    //运费模板
    public final static int TAG_shop_wl_exFeeModel = 80;
    public final static int TAG_shop_wl_exFeeModel_delete = 81;
    public final static String FUNC_shop_wl_exFeeModel = "shop_wl_exFeeModel";


    //热门资讯
    public final static int TAG_shop_wl_theadlinesA = 82;
    public final static String FUNC_shop_wl_theadlinesA = "shop_wl_theadlinesA";

    //关于我们
    public final static int TAG_shop_wl_my = 83;
    public final static String FUNC_shop_wl_my = "shop_wl_my";

    //商品下架
    public final static int TAG_shop_wl_shelevs = 84;
    public final static String FUNC_shop_wl_shelevs = "shop_wl_shelevs";
    //商品上架
    public final static int TAG_shop_wl_shelf = 85;
    public final static String FUNC_shop_wl_shelf = "shop_wl_shelf";

    //通知
    public final static int TAG_shop_wl_notice = 86;
    public final static String FUNC_shop_wl_notice = "shop_wl_notice";
    //生活模块
    public final static int TAG_GETLIFEDATA = 87;
    public final static String FUNC_things_hp_lifeB = "things_hp_lifeB";


    //支付验证码
    public final static int TAG_shop_wl_payment = 87;
    public final static String FUNC_shop_wl_payment = "shop_wl_payment";

    //支付验证码
    public final static int TAG_shop_wl_setpay = 88;
    public final static String FUNC_shop_wl_setpay = "shop_wl_setpayA";
    //退货地址
    public final static int TAG_shop_wl_send = 89;
    public final static String FUNC_shop_wl_send = "shop_wl_send";
    //超级商家收款
    public final static int TAG_shop_wl_address = 90;
    public final static String FUNC_shop_wl_address = "shop_wl_Receivables";

    //商品上架(商品上架绑定运费模版)  后台这里的命名不规范  带A和不带A的都在用 自求多福吧
    public final static int TAG_shop_wl_shelfA = 91;
    public final static String FUNC_shop_wl_shelfA = "shop_wl_shelfA";


    public final static int TAG_shop_hp_Info = 92;
    public final static String FUNC_shop_hp_Info = "shop_hp_Info";

    public final static int TAG_p_return_address = 93;
    public final static String FUNC_p_return_address = "p_return_address";


    public final static int TAG_shop_wl_query = 94;
    //    public final static String FUNC_shop_wl_query = "shop_wl_queryA";
//    public final static String FUNC_shop_wl_query = "shop_wl_queryB";
    public final static String FUNC_shop_wl_query = "shop_wl_details";
    public final static int TAG_shop_wl_editshop = 95;
    public final static String FUNC_shop_wl_editshop = "shop_wl_editshop";

    //满额包邮(李家豪)
    public final static int TAG_shop_wl_Freeship = 96;
    public final static String FUNC_shop_wl_Freeship = "shop_wl_Freeship";
    //满额包邮查询(李家豪)
    public final static int TAG_shop_wl_Freeship_query = 97;
    public final static String FUNC_shop_wl_Freeship_query = "shop_wl_Freeship_query";
    //优惠券管理(李家豪)
    public final static int TAG_shop_wl_Coupon = 98;
    public final static String FUNC_shop_wl_Coupon = "shop_wl_CouponA";
    //停止优惠券(李家豪)
    public final static int TAG_shop_wl_stopcoupon = 99;
    public final static String FUNC_shop_wl_stopcoupon = "shop_wl_stopcoupon";
    //创建优惠券(李家豪)
    public final static int TAG_shop_establish_coupon = 100;
    public final static String FUNC_shop_establish_coupon = "shop_establish_couponB";
    //商品分润(徐超)
    public final static int TAG_shop_wl_Profit = 103;
    public final static String FUNC_shop_wl_Profit = "shop_wl_Profit";
    //超级商家收款信息
    public final static int TAG_CHAOJISHANGJIAINFO = 104;
    public final static String FUNC_CHAOJISHANGJIAINFO = "p_super_merchant_collectionInfo_three";
    //超级商家付款
    public final static int TAG_PAYCHAOJISHANGJIA = 105;
    public final static String FUNC_PAYCHAOJISHANGJIA = "p_super_merchant_collection_three";
    //发布内容(服务号)
    public final static int TAG_p_add_findTable = 106;
    public final static String FUNC_p_add_findTable = "p_add_findTable";
    //拒绝原因
    public final static int TAG_shop_wl_reason = 107;
    public final static String FUNC_shop_wl_reason = "shop_wl_reason";
    //同意退货(徐超)
    public final static int TAG_shop_wl_tyth = 108;
    public final static String FUNC_shop_wl_tyth = "shop_rf_tythA";
//    public final static String FUNC_shop_wl_tyth = "Return_wl_goodsA";

    //拒绝原因提交
    public final static int TAG_shop_wl_Refuse = 109;
    //    public final static String FUNC_shop_wl_Refuse = "shop_wl_Refuse";
    public final static String FUNC_shop_wl_Refuse = "shop_rf_RefuseA";

    //退货退款(徐超)
    public final static int TAG_Return_wl_goods = 110;
    //    public final static String FUNC_Return_wl_goods = "Return_wl_goods";
//    public final static String FUNC_Return_wl_goods = "Return_wl_goodsA";
    public final static String FUNC_Return_wl_goods = "Return_wl_goodsB";


    //协商退货退款
    public final static int TAG_shop_wl_record = 111;
    public final static String FUNC_shop_wl_record = "shop_wl_record";//协商退货退款
    //绑定银行卡
    public final static int TAG_p_bindingCard_three = 112;
    public final static String FUNC_p_bindingCard_three = "p_bindingCard_three";
    //短信验证 绑定银行卡
    public final static int TAG_shop_wl_monsms = 113;
    public final static String FUNC_shop_wl_monsms = "shop_wl_monsms";
    //银行卡类型
    public final static int TAG_p_bank_type_two = 114;
    public final static String FUNC_p_bank_type_two = "shop_wl_Cardname";

    //查询银行卡信息
    public final static int TAG_shop_wl_Bankcard = 115;
    public final static String FUNC_shop_wl_Bankcard = "shop_wl_Bankcard";
    //解绑银行卡
    public final static int TAG_p_bindbankcard_two = 116;
    public final static String FUNC_p_bindbankcard_two = "p_bindbankcard_two";
    //二维码
    public final static int TAG_p_get_QR = 117;
    public final static String FUNC_p_get_QR = "p_get_QR";

    //手动退款
    public final static int TAG_Manual_wl_refund = 118;
    //    public final static String FUNC_Manual_wl_refund = "Manual_wl_refund";
//    public final static String FUNC_Manual_wl_refund = "Manual_wl_refundA";
    public final static String FUNC_Manual_wl_refund = "Manual_wl_refundB";
    //客服
    public final static int TAG_sel_shop_kefu = 119;
    public final static String FUNC_sel_shop_kefu = "sel_shop_kefu_2";  //客服
//    public final static int TAG_sel_shop_kefu_2 = 124;
//    public final static String FUNC_sel_shop_kefu_2 = "sel_shop_kefu_2";//客服 增加姓名


    //退款详情
    public final static int TAG_shop_wl_SuccessRefund = 120;
    public final static String FUNC_shop_wl_SuccessRefund = "shop_wl_SuccessRefund";
    //发商品 商品列表
    public final static int TAG_shop_wl_choiceshop = 121;
    public final static String FUNC_shop_wl_choiceshop = "shop_wl_choiceshop";
    public final static String FUNC_shop_wl_shoplist = "shop_wl_shoplist";
    public final static String FUNC_shop_wl_promotion = "shop_wl_promotion";
    //    订单时间 shop_wl_OrderTime(徐超)
    public final static int TAG_shop_wl_OrderTime = 122;
    public final static String FUNC_shop_wl_OrderTime = "shop_wl_OrderTime";
    //订单管理晒选时间shop_wl_RefundTime(徐超)
    public final static int TAG_shop_wl_RefundTime = 123;
    public final static String FUNC_shop_wl_RefundTime = "shop_wl_RefundTimeB";
    //审核失败
    public final static int TAG_shop_wl_FailureReason = 124;
    public final static String FUNC_shop_wl_FailureReason = "shop_wl_FailureReason";
    //评论列表
    public final static int TAG_shop_wl_recomments = 125;
    public final static String FUNC_shop_wl_recomments = "shop_wl_recomments";
    //商品评论
    public final static int TAG_shop_sh_recomments = 126;
    public final static String FUNC_shop_sh_recomments = "shop_wl_commentlist";
    //商品评论
    public final static int TAG_shop_comment_reply = 127;
    public final static String FUNC_shop_comment_reply = "shop_comment_reply";
    //删除商品
    public final static int TAG_shop_wl_delshops = 128;
    public final static String FUNC_shop_wl_delshops = "shop_wl_delshops";
    //商品收益详情
    public final static int TAG_shop_gain_recording_xq = 129;
    public final static String FUNC_shop_gain_recording_xq = "shop_gain_recording_xq";
    //退款详情
    public final static int TAG_shop_wl_rf_goodsDetail = 130;
    public final static String FUNC_shop_wl_rf_goodsDetail = "shop_wl_rf_goodsDetail";


    //退款退货订单列表
    public final static int TAG_shop_wl_Reservice = 131;
    public final static String FUNC_shop_wl_Reservice = "shop_wl_Reservice";
    //未处理订单数量
    public final static int TAG_shop_wl_prompt = 132;
    public final static String FUNC_shop_wl_prompt = "shop_wl_prompt";
    //退款详情
    public final static int TAG_shop_wl_rf_details = 133;
    public final static String FUNC_shop_wl_rf_details = "shop_wl_rf_details";
    //快速编辑
    public final static int TAG_shop_edit_shopPropety = 134;
    public final static String FUNC_shop_edit_shopPropety = "shop_edit_shopPropety";
    //店铺等级
    public final static int TAG_shop_sp_grade = 135;
    public final static String FUNC_shop_sp_grade = "shop_sp_grade";
    //荣誉分详情
    public final static int TAG_shop_sp_gradeDetail = 136;
    public final static String FUNC_shop_sp_gradeDetail = "shop_sp_gradeDetail";
    //店铺等级查询
    public final static int TAG_shop_sh_honnr = 137;
    public final static String FUNC_shop_sh_honnr = "shop_sh_honnr";


    // 客服中心
    public final static int TAG_KEHU_ZHONGXIN = 138;
    public final static String FUNC_KEHU_ZHONGXIN = "kefu_shop_jiem";
    // 选择客服位置
    public final static int TAG_KEHU_SELECT_LOCATION = 139;
    public final static String FUNC_KEHU_SELECT_LOCATION = "kefu_city_list";
    // 选择客服
    public final static int TAG_KEHU_SELECT = 140;
    public final static String FUNC_KEHU_SELECT = "sel_shop_kefu_2";
    // 线下交易说明
    public final static int TAG_shop_busExplain = 141;
    public final static String FUNC_shop_busExplain = "shop_busExplain";
    // 商家线下发货
    public final static int TAG_shop_wl_Linegoods = 142;
    public final static String FUNC_shop_wl_Linegoods = "shop_wl_Linegoods";


    // 收益提取界面信息
    public final static int TAG_shop_scsy_tx_Version = 143;
    public final static String FUNC_shop_scsy_tx_Version = "shop_silver-branch";
    // 收益提取界面信息
    public final static int TAG_shop_profitExchange_Version = 144;
    public final static String FUNC_shop_profitExchange_Version = "shop_profitExchange_Version1.0.0";
    public final static String FUNC_shop_profitExchange_Version_zhifubao = "shop_profitExchange_Zfb";
    // 收益提取界面信息
    public final static int TAG_shop_wl_Create_activity = 145;
    public final static String FUNC_shop_wl_Create_activity = "shop_wl_Create_activity";
    // 提现到银行卡详情
    public final static int TAG_shop_gain_recording_bank_xq = 146;
    public final static String FUNC_shop_gain_recording_bank_xq = "shop_gain_recording_bank_xq";
    // 账单通知转银行卡详情
    public final static int TAG_shop_billNotice_goBank_xq = 147;
    public final static String FUNC_shop_billNotice_goBank_xq = "shop_billNotice_goBank_xq";

    // 信誉保证金介绍
    public final static int TAG_shop_bzj_jieshao = 148;
    public final static String FUNC_shop_bzj_jieshao = "shop_wl_Bond";
    // 信誉保证金金额
    public final static int TAG_shop_bzj_jine = 149;
    public final static String FUNC_shop_bzj_jine = "shop_wl_paid";
    // 信誉保证金缴纳
    public final static int TAG_shop_bzj_jiaona = 150;
    public final static String FUNC_shop_bzj_jiaona = "shop_wl_Depositlist";
    // 充值
    public final static int TAG_shop_chongzhi = 151;
    public final static String FUNC_shop_chongzhi = "shop_wl_Margin_recharge";
    // 缴纳记录
    public final static int TAG_shop_jiaonajilu = 152;
    public final static String FUNC_shop_jiaonajilu = "shop_wl_Marginrecord";
    // 账单通知 体现到支付宝详情
    public final static int TAG_shop_gain_recording_ali_xq = 153;
    public final static String FUNC_shop_gain_recording_ali_xq = "shop_gain_recording_ali_xq";
    // 活动列表
    public final static int TAG_shop_wl_activity = 153;
    public final static String FUNC_shop_wl_activity = "shop_wl_activity";
    // 编辑活动
    public final static int TAG_shop_wl_Editorial_activities = 154;
    public final static String FUNC_shop_wl_Editorial_activities = "shop_wl_Editorial_activities";
    // 商品上传添加属性（添加规格判断颜色尺寸或规格）
    public final static int TAG_shop_wl_attribute = 155;
    public final static String FUNC_shop_wl_attribute = "shop_wl_attribute";
    //商品推广基本信息
    public final static int TAG_shop_wl_extension = 156;
    public final static String FUNC_shop_wl_extension = "shop_wl_extension";
    //商品推广下单
    public final static int TAG_shop_wl_extension_order = 157;
    public final static String FUNC_shop_wl_extension_order = "shop_wl_extension_order";
    //商品推广记录
    public final static int TAG_shop_wl_extension_record = 158;
    public final static String FUNC_shop_wl_extension_record = "shop_wl_extension_record";
    //添加删除支付宝
    public final static int TAG_shop_aliacc_edit = 159;
    public final static String FUNC_shop_aliacc_edit = "shop_aliacc_edit";
    //支付宝列表
    public final static int TAG_shop_aliacc_list = 160;
    public final static String FUNC_shop_aliacc_list = "shop_aliacc_list";
    //银行卡支付宝列表
    public final static int TAG_shop_wl_Bankcard_Version110 = 161;
    public final static String FUNC_shop_wl_Bankcard_Version110 = "shop_wl_Bankcard_Version1.3.0";
    // 账单通知转支付宝详情
    public final static int TAG_shop_wl_billDetail = 162;
    public final static String FUNC_shop_wl_billDetail = "shop_wl_billDetail";
    // 获取提现手续费
    public final static int TAG_shop_getShouXuFei = 163;
    public final static String FUNC_shop_getShouXuFei = "shop_sp_showList";

    //发送系统消息
    public final static int TAG_shop_wl_mass = 162;
    public final static String FUNC_shop_wl_mass = "shop_wl_mass";
    //系统消息列表
    public final static int TAG_shop_wl_mass_List = 163;
    public final static String FUNC_shop_wl_mass_List = "shop_wl_mass_List";
    //系统消息详情
    public final static int TAG_shop_wl_mass_detail = 164;
    public final static String FUNC_shop_wl_mass_detail = "shop_wl_mass_detail";
    //系统消息详情
    public final static int TAG_shop_wl_Uploading_goods = 165;
    public final static String FUNC_shop_wl_Uploading_goods = "shop_wl_Uploading_goods";


    //随机码
    public static String random = "";
    //店铺id
    public static String dianpuId = "";
    // 相机权限
    public static int PERMISSION_CAMERA = 0;
    // SharedPreferences存储空间
    public static String MYSP = "myinfo";
    // 手机高度
    public static String SP_PHONEHEIGHT = "SP_PHONEHEIGHT";
    // 手机信息json格式
    public static String SP_PHONEINFO_JSON = "SP_PHONEINFO_JSON";
    // 手机信息kv格式
    public static String SP_PHONEINFO_KV = "SP_PHONEINFO_KV";
    // 手机ID
    public static String SP_PHENEID = "SP_PHENEID";
    // 用户信息json格式
    public static String SP_MYINFO_JSON = "SP_MYINFO_JSON";
    // 用户信息kv格式
    public static String SP_MYINFO_KV = "SP_MYINFO_KV";
    //用户默认发货地址
    public static String SP_MOREN_FAHUODI = "SP_MOREN_FAHUODI";
    //生活模块
    public static final String CACHE_LIFE = "CACHE_LIFE";
    //店铺id
    public static final String SP_DIANPU_ID = "SP_DIANPU_ID";
    // 当前版本号
    public static String VERSION = "1.7.1";


    // 存储网页注入缓存
    public static final String WEB_JS = "WEB_JS";
    // web支付服务器地址
    public static String WEBPAY_URL = "WEBPAY_URL";
    // web收银台地址
    public static String WEBCASHIER_URL = "WEBCASHIER_URL";
}
