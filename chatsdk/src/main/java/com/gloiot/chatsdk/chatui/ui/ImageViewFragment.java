package com.gloiot.chatsdk.chatui.ui;

import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.gloiot.chatsdk.R;
import com.polites.android.GestureImageView;

import java.io.File;

/**
 * 聊天点击图片Fragment
 */
public class ImageViewFragment extends Fragment {
    private String imageUrl;
    private ProgressBar loadBar;
    private GestureImageView imageGiv;

    public View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container, android.os.Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_images_view_item, container, false);
        init(view);
        loadImage(imageUrl);
        return view;
    }

    private void init(View mView) {
        loadBar = (ProgressBar) mView.findViewById(R.id.imageView_loading_pb);
        imageGiv = (GestureImageView) mView.findViewById(R.id.imageView_item_giv);
        imageGiv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }


    public void loadImage(String url) {
        Log.d("loadImage", "-" + url);
        try {
            if (new File(url).exists() || url.startsWith("http://")) {
                // 图片文件或网络地址
                Glide.with(this)
                        .asBitmap()
                        .load(url)
                        .into(imageGiv);
            } else {
                // base64编码
                Glide.with(this)
                        .asBitmap()
                        .load(Base64.decode(url, Base64.DEFAULT))
                        .into(imageGiv);
            }
            loadBar.setVisibility(View.GONE);
            imageGiv.setVisibility(View.VISIBLE);
        } catch (Exception e){

        }
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
