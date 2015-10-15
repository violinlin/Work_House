package com.whl.hp.work_house.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whl.hp.work_house.tool.Debug;
import com.whl.hp.work_house.tool.NUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by hp-whl on 2015/9/24.
 */
public class PagerView implements NUtils.Callback{
    ViewPager viewPager;
    TextView textView;
    LinearLayout linearLayout;
    private List<ImageView> imgViews; //ViewPager中显示的所有图片控件
    private List<Map<String,String>> datas; //存储广告图片的数据源
    private Handler mHandler=new Handler();
    public static final String KEY_TITLE="title";
    public static final String KEY_URL="url";
    Context context;
    public PagerView( ViewPager viewPager,TextView textView,LinearLayout linearLayout,Context context){
        this.viewPager=viewPager;
        this.textView=textView;
        this.linearLayout=linearLayout;
        this.context=context;

    }

    public void setDatas(List<Map<String,String>> datas){
        this.datas=datas;
        textView.setText(datas.get(0).get(KEY_TITLE)); //默认显示第一个广告标题

        createViews();

    }

    private void createViews() {
        // TODO 根据数据源创建ViewPager中显示的UI
        imgViews=new ArrayList<ImageView>();
        ImageView imgView=null;
        for(Map<String,String> map:datas){
            imgView=new ImageView(context);
            imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imgView.setTag(map.get(KEY_URL));

            imgViews.add(imgView);
        }
        //设置ViewPager的适配器
        viewPager.setAdapter(new ImageAdapter());

        loadImgs();

    }

    private void loadImgs() {
        // TODO 加载网络图片
        for(Map<String,String> map:datas){
            NUtils.get(NUtils.TYPE_IMG,map.get(KEY_URL), this);
            Debug.startDebug(map.get(KEY_URL));


        }

        new PlayImgThread().start();//开始播放
    }

    @Override
    public boolean isCancelled(String url) {
        return false;
    }

    @Override
    public void response(String url, byte[] bytes) {
        // TODO 网络下载完成的回调方法
        Bitmap bitmap= BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        for(ImageView imgView:imgViews){
            if(imgView.getTag().equals(url)){
                imgView.setImageBitmap(bitmap);
            }
        }
    }

    class ImageAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return imgViews.size();
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(imgViews.get(position));
            return imgViews.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imgViews.get(position));
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view==obj;
        }
    }

    private int curPosition=0; //当前ViewPager页面的位置
    class PlayImgThread extends Thread{
        @Override
        public void run() {
            try {
                while(PagerView.this!=null){

                    if(curPosition==imgViews.size()){
                        curPosition=0;
                    }

                    Thread.sleep(2000);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() { //在主线程中执行的
                            if(curPosition==imgViews.size()){
                                curPosition=0;
                            }

                            textView.setText(datas.get(curPosition).get(KEY_TITLE));

                            if(curPosition==0)
                                viewPager.setCurrentItem(curPosition++,false);//false不带有动画效果地切换
                            else
                                viewPager.setCurrentItem(curPosition++);

                        }
                    });

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



}
