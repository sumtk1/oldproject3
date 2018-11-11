//package com.zyd.wlwsdk.widge;
//
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.os.Handler;
//import android.support.v4.view.PagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.youth.banner.BannerConfig;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by RudyJun on 2016/12/3.
// */
//
///**
// * 禁止轮播图自动播放下一张
// */
//public class NoAutoPlayBanner extends FrameLayout implements ViewPager.OnPageChangeListener{
//
//        /**
//         * 常量已经移入BannerConfig类里
//         */
//        @Deprecated
//        public static final int NOT_INDICATOR=0;
//        @Deprecated
//        public static final int CIRCLE_INDICATOR=1;
//        @Deprecated
//        public static final int NUM_INDICATOR=2;
//        @Deprecated
//        public static final int NUM_INDICATOR_TITLE=3;
//        @Deprecated
//        public static final int CIRCLE_INDICATOR_TITLE=4;
//        @Deprecated
//        public static final int LEFT=5;
//        @Deprecated
//        public static final int CENTER=6;
//        @Deprecated
//        public static final int RIGHT=7;
//
//        public String tag="banner";
//        private int mIndicatorMargin = BannerConfig.PADDING_SIZE;
//        private int mIndicatorWidth = BannerConfig.INDICATOR_SIZE;
//        private int mIndicatorHeight = BannerConfig.INDICATOR_SIZE;
//        private int bannerStyle= BannerConfig.NOT_INDICATOR;
//        private int delayTime= BannerConfig.TIME;
//        private boolean isAutoPlay= BannerConfig.IS_AUTO_PLAY;
//        private int mIndicatorSelectedResId = com.youth.banner.R.drawable.gray_radius;
//        private int mIndicatorUnselectedResId = com.youth.banner.R.drawable.white_radius;
//        private int defaultImage = com.youth.banner.R.drawable.default_image;
//        private int count;
//        private int currentItem;
//        private int gravity=-1;
//        private List<ImageView> imageViews;
//        private List<ImageView> indicatorImages;
//        private Context context;
//        private ViewPager viewPager;
//        private LinearLayout indicator;
//        private Handler handler = new Handler();
//        private com.youth.banner.Banner.OnBannerClickListener listener;
//        private com.youth.banner.Banner.OnLoadImageListener imageListener;
//        private String[] titles;
//        private TextView bannerTitle , numIndicator;
//        private int lastPosition=0;
//
//        public NoAutoPlayBanner(Context context) {
//            this(context, null);
//        }
//        public NoAutoPlayBanner(Context context, AttributeSet attrs) {
//            this(context, attrs, 0);
//        }
//
//        public NoAutoPlayBanner(Context context, AttributeSet attrs, int defStyle) {
//            super(context, attrs, defStyle);
//            this.context = context;
//            imageViews = new ArrayList<ImageView>();
//            indicatorImages = new ArrayList<ImageView>();
//            initView(context, attrs);
//        }
//        private void handleTypedArray(Context context, AttributeSet attrs) {
//            if (attrs == null) {
//                return;
//            }
//            TypedArray typedArray = context.obtainStyledAttributes(attrs, com.youth.banner.R.styleable.Banner);
//            mIndicatorWidth =typedArray.getDimensionPixelSize(com.youth.banner.R.styleable.Banner_indicator_width, BannerConfig.INDICATOR_SIZE);
//            mIndicatorHeight =typedArray.getDimensionPixelSize(com.youth.banner.R.styleable.Banner_indicator_height, BannerConfig.INDICATOR_SIZE);
//            mIndicatorMargin =typedArray.getDimensionPixelSize(com.youth.banner.R.styleable.Banner_indicator_margin, BannerConfig.PADDING_SIZE);
//            mIndicatorSelectedResId =typedArray.getResourceId(com.youth.banner.R.styleable.Banner_indicator_drawable_selected, com.youth.banner.R.drawable.gray_radius);
//            mIndicatorUnselectedResId =typedArray.getResourceId(com.youth.banner.R.styleable.Banner_indicator_drawable_unselected, com.youth.banner.R.drawable.white_radius);
//            defaultImage =typedArray.getResourceId(com.youth.banner.R.styleable.Banner_default_image, com.youth.banner.R.drawable.default_image);
//            delayTime =typedArray.getDimensionPixelSize(com.youth.banner.R.styleable.Banner_delay_time, BannerConfig.TIME);
//            isAutoPlay =typedArray.getBoolean(com.youth.banner.R.styleable.Banner_is_auto_play, BannerConfig.IS_AUTO_PLAY);
//            typedArray.recycle();
//        }
//        private void initView(Context context, AttributeSet attrs) {
//            imageViews.clear();
//            View view = LayoutInflater.from(context).inflate(com.youth.banner.R.layout.banner, this, true);
//            viewPager = (ViewPager) view.findViewById(com.youth.banner.R.id.viewpager);
//            indicator = (LinearLayout) view.findViewById(com.youth.banner.R.id.indicator);
//            bannerTitle = (TextView) view.findViewById(com.youth.banner.R.id.bannerTitle);
//            numIndicator = (TextView) view.findViewById(com.youth.banner.R.id.numIndicator);
//            handleTypedArray(context, attrs);
//        }
//        public void setDelayTime(int delayTime) {
//            this.delayTime=delayTime;
//        }
//        public void setIndicatorGravity(int type) {
//            switch (type){
//                case BannerConfig.LEFT:
//                    this.gravity= Gravity.LEFT|Gravity.CENTER_VERTICAL;
//                    break;
//                case BannerConfig.CENTER:
//                    this.gravity=Gravity.CENTER;
//                    break;
//                case BannerConfig.RIGHT:
//                    this.gravity=Gravity.RIGHT|Gravity.CENTER_VERTICAL;
//                    break;
//            }
//        }
//        public void setBannerTitle(String[] titles) {
//            this.titles=titles;
//            if (bannerStyle== BannerConfig.CIRCLE_INDICATOR_TITLE||
//                    bannerStyle== BannerConfig.NUM_INDICATOR_TITLE) {
//                if (titles != null && titles.length > 0) {
//                    bannerTitle.setVisibility(View.VISIBLE);
//                    indicator.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
//                }else{
//                    numIndicator.setBackgroundResource(com.youth.banner.R.drawable.black_background);
//                }
//            }
//        }
//        public void setBannerStyle(int bannerStyle) {
//            this.bannerStyle=bannerStyle;
//            switch (bannerStyle){
//                case BannerConfig.CIRCLE_INDICATOR:
//                    indicator.setVisibility(View.VISIBLE);
//                    break;
//                case BannerConfig.NUM_INDICATOR:
//                    numIndicator.setVisibility(View.VISIBLE);
//                    numIndicator.setBackgroundResource(com.youth.banner.R.drawable.black_background);
//                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                    layoutParams.setMargins(0,0,10,10);
//                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//                    numIndicator.setLayoutParams(layoutParams);
//                    numIndicator.setPadding(5,6,5,6);
//                    break;
//                case BannerConfig.NUM_INDICATOR_TITLE:
//                    numIndicator.setVisibility(View.VISIBLE);
//                    break;
//                case BannerConfig.CIRCLE_INDICATOR_TITLE:
//                    indicator.setVisibility(View.VISIBLE);
//                    break;
//            }
//        }
//        public void setImages(Object[] imagesUrl) {
//            setImageArray(imagesUrl, null);
//        }
//        public void setImages(Object[] imagesUrl, com.youth.banner.Banner.OnLoadImageListener imageListener) {
//            setImageArray(imagesUrl, imageListener);
//        }
//        public void setImages(List<?> imagesUrl){
//            setImageList(imagesUrl, null);
//        }
//        public void setImages(List<?> imagesUrl, com.youth.banner.Banner.OnLoadImageListener imageListener) {
//            setImageList(imagesUrl, imageListener);
//        }
//        private void setImageArray(Object[] imagesUrl, com.youth.banner.Banner.OnLoadImageListener imageListener) {
//            if (imagesUrl==null||imagesUrl.length<=0) {
//                Log.e(tag,"Please set the images data.");
//                return;
//            }
//            count = imagesUrl.length;
//            if(count>1) {
//                createIndicator();
//            }
//            imageViews.clear();
//            for (int i = 0; i <= count-1; i++) {
//                ImageView iv = new ImageView(context);
//                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                Object url=null;
//                    url=imagesUrl[i];
//                imageViews.add(iv);
//                if(imageListener!=null){
//                    imageListener.OnLoadImage(iv,url);
//                }else{
//                    Glide.with(context)
//                            .load(url)
//                            .placeholder(defaultImage)
//                            .into(iv);
//                }
//            }
//            setData();
//        }
//        private void setImageList(List<?> imagesUrl, com.youth.banner.Banner.OnLoadImageListener imageListener) {
//            if (imagesUrl==null||imagesUrl.size()<=0) {
//                Log.e(tag,"Please set the images data.");
//                return;
//            }
//            count = imagesUrl.size();
//            if(count>1) {
//                createIndicator();
//            }
//            imageViews.clear();
//            if(count==1){
//                ImageView iv = new ImageView(context);
//                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                Object url=null;
//                    url=imagesUrl.get(0);
//                imageViews.add(iv);
//                if(imageListener!=null){
//                    imageListener.OnLoadImage(iv,url);
//                }else{
//                    Glide.with(context)
//                            .load(url)
//                            .placeholder(defaultImage)
//                            .into(iv);
//                }
//            }else {
//                Log.e("imageViewsCount" , "imageViewsCount = "+count);
//                for (int i = 0; i <= count-1; i++) {
//                    ImageView iv = new ImageView(context);
//                    iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                    Object url = null;
//                        url = imagesUrl.get(i);
//                    imageViews.add(iv);
//                    if (imageListener != null) {
//                        imageListener.OnLoadImage(iv, url);
//                    } else {
//                        Glide.with(context)
//                                .load(url)
//                                .placeholder(defaultImage)
//                                .into(iv);
//                    }
//                }
//            }
//            setData();
//        }
//        private void createIndicator() {
//            indicatorImages.clear();
//            indicator.removeAllViews();
//            for (int i = 0; i < count; i++) {
//                ImageView imageView = new ImageView(context);
//                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mIndicatorWidth,mIndicatorHeight);
//                params.leftMargin = mIndicatorMargin;
//                params.rightMargin = mIndicatorMargin;
//                if(i==0){
//                    imageView.setImageResource(mIndicatorSelectedResId);
//                }else{
//                    imageView.setImageResource(mIndicatorUnselectedResId);
//                }
//                indicator.addView(imageView, params);
//                indicatorImages.add(imageView);
//            }
//        }
//
//
//        private void setData() {
//            currentItem = 0;
//            viewPager.setAdapter(new BannerPagerAdapter());
//            viewPager.setFocusable(true);
//            viewPager.setCurrentItem(currentItem);
//            viewPager.addOnPageChangeListener(this);
//            if (gravity!=-1)
//                indicator.setGravity(gravity);
//            if (isAutoPlay)
//                startAutoPlay();
//        }
//        public void isAutoPlay(boolean isAutoPlay) {
//            this.isAutoPlay=isAutoPlay;
//        }
//        private void startAutoPlay() {
//            handler.removeCallbacks(task);
//            handler.postDelayed(task, delayTime);
//        }
//
//        private final Runnable task = new Runnable() {
//
//            @Override
//            public void run() {
//                if (isAutoPlay) {
//                    currentItem = currentItem % (count + 1) + 1;
//                    if (currentItem == 1) {
//                        viewPager.setCurrentItem(currentItem, false);
//                    } else {
//                        viewPager.setCurrentItem(currentItem);
//                    }
//                }
//                handler.postDelayed(task, delayTime);
//            }
//        };
//
//
//        class BannerPagerAdapter extends PagerAdapter {
//
//            @Override
//            public int getCount() {
//                return imageViews.size();
//            }
//
//            @Override
//            public boolean isViewFromObject(View arg0, Object arg1) {
//                return arg0 == arg1;
//            }
//
//            @Override
//            public Object instantiateItem(ViewGroup container, final int position) {
//                container.addView(imageViews.get(position));
//                final ImageView view=imageViews.get(position);
//                view.setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if(listener!=null){
//                            listener.OnBannerClick(v,position);
//                        }
//                    }
//                });
//                return view;
//            }
//
//            @Override
//            public void destroyItem(ViewGroup container, int position, Object object) {
//                container.removeView(imageViews.get(position));
//            }
//
//        }
//
//        @Override
//        public void onPageScrollStateChanged(int state) {
//            switch (state) {
//                case 1:
//                    isAutoPlay = false;
//                    break;
//                case 2:
//                    isAutoPlay = false;
//                    break;
//                case 0:
//                    isAutoPlay = false;
//                        Log.e("imageViews" , "imageViews = "+imageViews.size());
//                    break;
//            }
//        }
//
//        @Override
//        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        }
//
//        @Override
//        public void onPageSelected(int position) {
//            if(count>1) {
//                indicatorImages.get((lastPosition + count) % count).setImageResource(mIndicatorUnselectedResId);
//                indicatorImages.get((position + count) % count).setImageResource(mIndicatorSelectedResId);
//            }
//            lastPosition=position;
//            switch (bannerStyle){
//                case BannerConfig.CIRCLE_INDICATOR:
//                    break;
//                case BannerConfig.NUM_INDICATOR:
//                    if (position>count) position=count;
//                    numIndicator.setText(position+"/"+count);
//                    break;
//                case BannerConfig.NUM_INDICATOR_TITLE:
//                    if (position>count) position=count;
//                    numIndicator.setText(position+"/"+count);
//                    if (titles!=null&&titles.length>0){
//                        if (position>titles.length) position=titles.length;
//                        bannerTitle.setText(titles[position-1]);
//                    }
//                    break;
//                case BannerConfig.CIRCLE_INDICATOR_TITLE:
//                    if (titles!=null&&titles.length>0){
//                        if (position>titles.length) position=titles.length;
//                        bannerTitle.setText(titles[position-1]);
//                    }
//                    break;
//            }
//
//        }
//
//
//        public void setOnBannerClickListener(com.youth.banner.Banner.OnBannerClickListener listener) {
//            this.listener = listener;
//        }
//
//        public void setOnBannerImageListener(com.youth.banner.Banner.OnLoadImageListener imageListener) {
//            this.imageListener = imageListener;
//        }
//
//        public interface OnBannerClickListener {
//            void OnBannerClick(View view, int position);
//        }
//        public interface OnLoadImageListener {
//            void OnLoadImage(ImageView view, Object url);
//        }
//    }
//
//
