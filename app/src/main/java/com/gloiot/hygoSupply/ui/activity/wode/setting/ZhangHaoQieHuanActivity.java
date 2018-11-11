package com.gloiot.hygoSupply.ui.activity.wode.setting;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.gloiot.chatsdk.DataBase.IMDBManager;
import com.gloiot.chatsdk.socket.SocketListener;
import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.ui.activity.MainActivity;
import com.gloiot.hygoSupply.ui.activity.login.LoginActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.utlis.DialogUtlis;
import com.zyd.wlwsdk.adapter.ViewHolder;
import com.zyd.wlwsdk.adapter.abslistview.CommonAdapter;
import com.zyd.wlwsdk.utlis.JSONUtlis;
import com.zyd.wlwsdk.utlis.L;
import com.zyd.wlwsdk.utlis.MD5Utlis;
import com.zyd.wlwsdk.utlis.MToast;
import com.zyd.wlwsdk.utlis.PictureUtlis;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class ZhangHaoQieHuanActivity extends BaseActivity {

    @Bind(R.id.lv_zhanghao_qiehuan)
    ListView lvZhanghaoQiehuan;

    private CommonAdapter commonAdapter;
    private List<ZhangHaoQieHuanBean> listDatas = new ArrayList<>();

    private SQLiteDatabase sqLiteDatabase;
    private String zhanghao;
    private String mima;

    @Override
    public int initResource() {
        return R.layout.activity_zhanghao_qiehuan;
    }

    @Override
    public void initData() {
        //数据库
        sqLiteDatabase = openOrCreateDatabase("shang_cheng.db", Context.MODE_PRIVATE, null);
        sqLiteDatabase.execSQL("CREATE TABLE if not exists qiehuan_zhanghao (zhanghao VARCHAR PRIMARY KEY, mima VARCHAR, imgUrl VARCHAR)");

        //listView脚布局
        View footer = View.inflate(mContext, R.layout.item_zhanghao_footer, null);
        lvZhanghaoQiehuan.addFooterView(footer);
        //换个新账号登录点击事件
        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //listView适配器
        commonAdapter = new CommonAdapter<ZhangHaoQieHuanBean>(mContext, R.layout.item_zhanghao_qiehuan, listDatas) {
            @Override
            public void convert(ViewHolder holder, final ZhangHaoQieHuanBean zhangHaoQieHuanBean) {

                CommonUtils.setDisplayImageOptions((ImageView) holder.getView(R.id.item_zhanghao_qiehuan_touxiang), zhangHaoQieHuanBean.Str_ImageUrl, 1000);

                if (zhangHaoQieHuanBean.Str_ZhangHao.length() <= 5) {
                    holder.setText(R.id.item_zhanghao_qiehuan_zhanghao, zhangHaoQieHuanBean.Str_ZhangHao);
                } else {
                    holder.setText(R.id.item_zhanghao_qiehuan_zhanghao, zhangHaoQieHuanBean.Str_ZhangHao.substring(0, 2) +
                            "****" + zhangHaoQieHuanBean.Str_ZhangHao.substring(zhangHaoQieHuanBean.Str_ZhangHao.length() - 3, zhangHaoQieHuanBean.Str_ZhangHao.length()));
                }

                if (zhangHaoQieHuanBean.isVisible) {
                    holder.getView(R.id.item_zhanghao_qiehuan_delete).setVisibility(View.VISIBLE);
                    holder.getView(R.id.item_zhanghao_qiehuan_currentAccount).setVisibility(View.GONE);
                    holder.getView(R.id.item_zhanghao_qiehuan_touxiang).setVisibility(View.GONE);
                } else {
                    holder.getView(R.id.item_zhanghao_qiehuan_delete).setVisibility(View.GONE);
                    holder.getView(R.id.item_zhanghao_qiehuan_currentAccount).setVisibility(View.VISIBLE);
                    holder.getView(R.id.item_zhanghao_qiehuan_touxiang).setVisibility(View.VISIBLE);
                }
                //是否当前账号
                if (zhangHaoQieHuanBean.isCurrentAccount) {
                    holder.setImageResource(R.id.item_zhanghao_qiehuan_currentAccount, R.mipmap.ic_radio);
                } else {
                    holder.setImageResource(R.id.item_zhanghao_qiehuan_currentAccount, R.mipmap.ic_quan);
                }
                //删除
                holder.getView(R.id.item_zhanghao_qiehuan_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtlis.twoBtnNormal(mContext, "提示", "是否删除该账号？", true, "取消", "确认",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        DialogUtlis.dismissDialogNoAnimator();
                                    }
                                }, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        DialogUtlis.dismissDialogNoAnimator();
                                        sqLiteDatabase.delete("qiehuan_zhanghao", "zhanghao=?",
                                                new String[]{zhangHaoQieHuanBean.Str_ZhangHao});
                                        //查询数据库，更新界面
                                        chaXunSQLite();
                                    }
                                });
                    }
                });

                holder.getView(R.id.item_zhanghao_qiehuan).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (zhangHaoQieHuanBean.isCurrentAccount) {
                            MToast.showToast("当前登录账号已是该账号");
                        } else {
                            zhanghao = zhangHaoQieHuanBean.Str_ZhangHao;
                            mima = zhangHaoQieHuanBean.Str_MiMa;

                            requestHandleArrayList.add(requestAction.userLogin(
                                    ZhangHaoQieHuanActivity.this, zhanghao,
                                    MD5Utlis.Md5(mima)));
                        }
                    }
                });
            }
        };
        lvZhanghaoQiehuan.setAdapter(commonAdapter);

        //这里的顺序谨慎修改，需要从到commonAdapter对象，小心对象报null
        CommonUtils.setTitleBarWithRightClick(this, true, "账号切换", "编辑", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < listDatas.size(); i++) {
                    if (listDatas.get(i).isVisible) {
                        listDatas.get(i).isVisible = false;
                    } else {
                        listDatas.get(i).isVisible = true;
                    }
                }
                commonAdapter.notifyDataSetChanged();
            }
        });

        //查询数据库，更新界面
        chaXunSQLite();
    }

    //查询数据库，更新界面
    private void chaXunSQLite() {
        //查询获得游标
        Cursor cursor = sqLiteDatabase.rawQuery("select * from qiehuan_zhanghao", null);

        listDatas.clear();
        ZhangHaoQieHuanBean zhangHaoQieHuanBean;
        while (cursor.moveToNext()) {
            zhangHaoQieHuanBean = new ZhangHaoQieHuanBean();
            //获得记录
            zhangHaoQieHuanBean.Str_ZhangHao = cursor.getString(0);
            zhangHaoQieHuanBean.Str_MiMa = cursor.getString(1);
            zhangHaoQieHuanBean.Str_ImageUrl = cursor.getString(2);
            if (zhangHaoQieHuanBean.Str_ZhangHao.equals(preferences.getString(ConstantUtils.SP_ZHANGHAO, ""))) {
                zhangHaoQieHuanBean.isCurrentAccount = true;
            } else {
                zhangHaoQieHuanBean.isCurrentAccount = false;
            }
            zhangHaoQieHuanBean.isVisible = false;
            listDatas.add(zhangHaoQieHuanBean);
        }
        cursor.close();
        commonAdapter.notifyDataSetChanged();
    }

    @Override
    public void requestSuccess(int requestTag, JSONObject jsonObject, int showLoad) throws JSONException {
        L.e("requestSuccess :" + requestTag, jsonObject.toString());
        switch (requestTag) {
            case ConstantUtils.TAG_shop_login:
                editor.putString(ConstantUtils.SP_LOGINTYPE, "退出");
                editor.commit();
                SocketListener.getInstance().signoutRenZheng();
                IMDBManager.getInstance(mContext, preferences.getString(ConstantUtils.SP_USERPHONE, "")).ClearnData();


                //存账号
                editor.putString(ConstantUtils.SP_ZHANGHAO, zhanghao);
                editor.putString(ConstantUtils.SP_MYID, jsonObject.getString("账号"));
                editor.putString(ConstantUtils.SP_USERNAME, jsonObject.getString("姓名"));
                editor.putString(ConstantUtils.SP_RANDOMCODE, jsonObject.getString("随机码"));
                editor.putString(ConstantUtils.SP_USERPWD, mima);
                editor.putString(ConstantUtils.SP_USERSCTYPE, jsonObject.getString("商家类别"));
                editor.putString(ConstantUtils.SP_USERIMG, jsonObject.getString("头像"));
                editor.putString(ConstantUtils.SP_USERNICHENG, jsonObject.getString("昵称"));
                editor.putString(ConstantUtils.SP_USERSEX, jsonObject.getString("性别"));
                editor.putString(ConstantUtils.SP_YOUWUDIANPU, jsonObject.getString("店铺状态"));
                editor.putString(ConstantUtils.SP_DIANPUID, jsonObject.getString("店铺id"));
                editor.putString(ConstantUtils.SP_USERPHONE, jsonObject.getString("手机号"));
                editor.putString(ConstantUtils.SP_ZIYING, jsonObject.getString("自营"));
                editor.putString(ConstantUtils.SP_MYPWD, jsonObject.getString("支付密码"));
                editor.putString(ConstantUtils.SP_ONLYID, jsonObject.getString("唯一id"));
                editor.putString(ConstantUtils.SP_SUPERMERCHANT, jsonObject.getString("超级商家"));
                editor.putString(ConstantUtils.SP_DIANPU_ID, jsonObject.getString("店铺id"));
                editor.putString(ConstantUtils.SP_SHIFOUBANGDINGWX, jsonObject.getString("绑定微信"));
                editor.putString(ConstantUtils.SP_LOGINTYPE, "成功");
                editor.commit();
                ConstantUtils.random = jsonObject.getString("随机码");
                ConstantUtils.dianpuId = jsonObject.getString("店铺id");
                try {
                    imDB(jsonObject.getString("账号"));
                    imSocket();
                }catch (Exception e){

                }
                startActivity(new Intent(ZhangHaoQieHuanActivity.this, MainActivity.class));
                this.finish();

                //SQLite
                ContentValues contentValues = new ContentValues();
                contentValues.put("zhanghao", zhanghao);
                contentValues.put("mima", mima);
                contentValues.put("imgUrl", JSONUtlis.getString(jsonObject, "头像"));
                sqLiteDatabase.replace("qiehuan_zhanghao", null, contentValues);

        }
    }


    private void imDB(String account) {
        IMDBManager.getInstance(mContext, account);
    }

    // socket认证
    private void imSocket() {
        if (!TextUtils.isEmpty(preferences.getString(ConstantUtils.SP_USERPHONE, ""))) {
            SocketListener.getInstance().connectionRenZheng(preferences.getString(ConstantUtils.SP_USERPHONE, ""), preferences.getString(ConstantUtils.SP_RANDOMCODE, ""));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sqLiteDatabase.close();
    }

    public class ZhangHaoQieHuanBean {
        //头像图片
        private String Str_ImageUrl = "";
        //账号
        private String Str_ZhangHao = "";
        //密码
        private String Str_MiMa = "";
        //删除按钮是否可见
        private boolean isVisible = false;
        //是否当前账号
        private boolean isCurrentAccount = false;
    }
}
