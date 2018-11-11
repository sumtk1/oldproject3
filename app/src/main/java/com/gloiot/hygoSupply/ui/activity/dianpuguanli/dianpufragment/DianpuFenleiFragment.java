package com.gloiot.hygoSupply.ui.activity.dianpuguanli.dianpufragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gloiot.hygoSupply.R;
import com.gloiot.hygoSupply.ui.activity.BaseFragment;
import com.gloiot.hygoSupply.ui.adapter.TabFragmentAdapter;
import com.gloiot.hygoSupply.utlis.ConstantUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DianpuFenleiFragment extends BaseFragment {
    @Bind(R.id.tl_fg_dianpu_fenlei)
    TabLayout tl_fg_dianpu_fenlei;
    @Bind(R.id.vp_fg_dianpu_fenlei)
    ViewPager vp_fg_dianpu_fenlei;
    private String title = "分类", phone = "", hengfu = "";
    private String[] titles;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dianpu_fenlei, container, false);
        ButterKnife.bind(this, view);

        if (phone.equals("")) {
            phone = preferences.getString(ConstantUtils.SP_MYID, "");
        }
        requestHandleArrayList.add(requestAction.shop_sj_sy(DianpuFenleiFragment.this, phone, title, hengfu));
        return view;
    }


    @Override
    public void requestSuccess(int requestTag, JSONObject response, int showLoad) throws JSONException {
        super.requestSuccess(requestTag, response, showLoad);
        switch (requestTag) {
            case ConstantUtils.TAG_shop_sj_sy:
                Log.e("分类状态请求成功", response.toString());
                if (!response.getString("分类条数").equals("0")) {
                    JSONArray jsonArray = response.getJSONArray("列表1");
                    List<Fragment> fragments = new ArrayList<>();
                    titles = new String[jsonArray.length()];
                    for (int j = 0; j < jsonArray.length(); j++) {
                        response = (JSONObject) jsonArray.get(j);
                        titles[j] = response.getString("二级分类");
                        Fragment fragment = new DianpuFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("title", titles[j]);
                        fragment.setArguments(bundle);
                        tl_fg_dianpu_fenlei.addTab(tl_fg_dianpu_fenlei.newTab().setText(titles[j]));
                        fragments.add(fragment);
                    }
                    vp_fg_dianpu_fenlei.setAdapter(new TabFragmentAdapter(fragments, titles, this.getActivity().getSupportFragmentManager(), this.getContext()));
                    tl_fg_dianpu_fenlei.setupWithViewPager(vp_fg_dianpu_fenlei);
                }
                break;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
