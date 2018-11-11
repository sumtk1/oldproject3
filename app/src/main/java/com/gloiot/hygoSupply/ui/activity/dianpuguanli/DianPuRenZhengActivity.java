package com.gloiot.hygoSupply.ui.activity.dianpuguanli;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseActivity;
import com.gloiot.hygoSupply.utlis.CommonUtils;
import com.gloiot.hygoSupply.utlis.ConstantUtils;
import com.gloiot.hygoSupply.utlis.DialogUtlis;
import com.zyd.wlwsdk.server.AliOss.ChoosePhoto;
import com.zyd.wlwsdk.utlis.MToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hygo03 on 2016/11/30.
 */

public class DianPuRenZhengActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.et_DPRZ_faren_xingming)
    EditText et_DPRZ_faren_xingming;
    @Bind(R.id.iv_DPRZ_shangchuang_fuben01)
    ImageView iv_DPRZ_shangchuang_fuben01;
    @Bind(R.id.gd_DPRZ_pictures_02)
    GridView gd_DPRZ_pictures_02;
    @Bind(R.id.et_DPRZ_xinyong_daima)
    EditText etDPRZXinyongDaima;

    private static final int MAXPICTURES = 8;
    private MyGVAdapter adapter;
    private String phone;
    private String yingYeZhiZhao;
    private List<String> picsUrl_List = new ArrayList<>();
    private List<String> picList = new ArrayList<>();//获取传过来的图片url,最好不要从前面获取，因为如果不退回第一个调接口的地方，从编辑页面再次进来不会更新到最新数据

    @Override
    public int initResource() {
        return R.layout.activity_dianpurenzheng;
    }

    @Override
    public void initData() {
        CommonUtils.setTitleBar((Activity) mContext, true, "资质认证", "");
        phone = preferences.getString(ConstantUtils.SP_MYID, "");
        adapter = new MyGVAdapter(picsUrl_List);
        gd_DPRZ_pictures_02.setAdapter(adapter);
    }

    @OnClick({R.id.iv_DPRZ_shangchuang_fuben01, R.id.iv_DPRZ_shangchuang_fuben02, R.id.btn_DPRZ_tijiao, R.id.tv_DPRZ_xize})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_DPRZ_shangchuang_fuben01:
                setZhiZhao_TuPian();
                break;
            case R.id.iv_DPRZ_shangchuang_fuben02:
                setZiZhi_tupian();
                break;
            case R.id.btn_DPRZ_tijiao:
                if (TextUtils.isEmpty(et_DPRZ_faren_xingming.getText().toString())) {
                    DialogUtlis.oneBtnNormal(mContext, "请输入法人姓名");
                }
                if (TextUtils.isEmpty(etDPRZXinyongDaima.getText().toString())) {
                    DialogUtlis.oneBtnNormal(mContext, "请输入统一社会信用代码");
                } else if (TextUtils.isEmpty(yingYeZhiZhao)) {
                    DialogUtlis.oneBtnNormal(mContext, "请上传营业执照");
                } else {
                    requestHandleArrayList.add(requestAction.dianPuRenZheng(this, phone, et_DPRZ_faren_xingming.getText().toString(), yingYeZhiZhao, picList, etDPRZXinyongDaima.getText().toString()));
                }
                break;
            case R.id.tv_DPRZ_xize:
                Intent intent = new Intent(mContext, RenZhengBiaoZhunXiZeActivity.class);
                startActivity(intent);
                break;
        }

    }

    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_dianpu_renzheng:
                DialogUtlis.oneBtnNormal(mContext, "认证资质已提交成功！请耐心等待审核", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtlis.dismissDialogNoAnimator();
                        finish();
                    }
                });
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    /**
     * gridview适配器
     */
    class MyGVAdapter extends BaseAdapter {
        private boolean isMax;
        private List<String> list = new ArrayList<>();

        public MyGVAdapter(List<String> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            if (list.size() >= MAXPICTURES) {
                isMax = true;
                return MAXPICTURES;
            } else {
                isMax = false;
                return list.size() + 1;
            }
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            View view = View.inflate(DianPuRenZhengActivity.this, R.layout.item_dianpurenzheng_grid_02, null);
            final ImageView mImageView = (ImageView) view.findViewById(R.id.id_item_image);//自定义的imageView，为了更好的放大动画效果
            final ImageView mSelect = (ImageView) view.findViewById(R.id.id_item_select);//选中状态标识
            DianPuRenZhengActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (list.size() == 0) {
                        mImageView.setImageResource(R.mipmap.bg_qitazizhi);
                        mSelect.setVisibility(View.GONE);
                    } else if (position == list.size() && list.size() < MAXPICTURES) {
                        mSelect.setVisibility(View.GONE);
                        mImageView.setImageResource(R.mipmap.bg_qitazizhi);
                    } else {
                        mSelect.setVisibility(View.VISIBLE);
                        CommonUtils.setDisplayImageOptions(mImageView, list.get(position), 0);
                    }
                }
            });
            if (list.size() > 0) {
                mSelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        list.remove(position);
                        picList.remove(position);
                        notifyDataSetChanged();
                    }
                });

                mImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PreviewPictureActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putStringArrayList("imageList", (ArrayList<String>) list);
                        bundle.putString("isLocationPicture", String.valueOf(true));
                        bundle.putInt("position", position);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }

            if (!isMax) {
                if (position == list.size()) {
                    mImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setZiZhi_tupian();
                        }
                    });
                }
            }
            return view;
        }
    }

    private void setZiZhi_tupian() {
        checkPermission(new CheckPermListener() {
            @Override
            public void superPermission() {
                new ChoosePhoto(mContext) {
                    @Override
                    protected void setPicsSuccess(List<String> picsSrc, List<String> picsUrl) {
                        picList.addAll(picsUrl);
                        picsUrl_List.addAll(picsSrc);
                        DianPuRenZhengActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }

                    @Override
                    protected void setPicsFailure(List<String> picsSrc, List<String> picsUrl, List<Boolean> pics_upload_sf) {
                        MToast.showToast("有部分图片上传失败");
                    }
                }.setPics(false, MAXPICTURES - picsUrl_List.size());
            }
        }, R.string.perm_camera, Manifest.permission.CAMERA);
    }


    private void setZhiZhao_TuPian() {
        checkPermission(new CheckPermListener() {
            @Override
            public void superPermission() {
                new ChoosePhoto(mContext) {
                    @Override
                    protected void setPicSuccess(final String myImgSrc, final String myPicUrl) {
                        super.setPicSuccess(myImgSrc, myPicUrl);
                        yingYeZhiZhao = myPicUrl;
                        DianPuRenZhengActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                CommonUtils.setDisplayImageOptions(iv_DPRZ_shangchuang_fuben01, myPicUrl, 0);
                            }
                        });
                    }

                    @Override
                    protected void setPicFailure() {
                        super.setPicFailure();
                        MToast.showToast("图片上传失败");
                    }
                }.setPic(false, false);
            }
        }, R.string.perm_camera, Manifest.permission.CAMERA);
    }

}
