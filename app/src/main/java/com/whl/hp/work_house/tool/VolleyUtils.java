package com.whl.hp.work_house.tool;

/**
 * Created by hp-whl on 2015/9/28.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

public class VolleyUtils {

    public static RequestQueue mQueue;
    public static ImageLoader mLoader;

    private static LruCache<String,Bitmap> lruCache;//强引用缓存，一级缓存，特点：使用最近最少使用的算法，将旧数据移除，为新数据提供空间
    private static Map<String,SoftReference<Bitmap>> softCache; //软引用缓存，二级缓存
    public static RequestQueue getQueue(Context context){
        if(mQueue==null){
            mQueue= Volley.newRequestQueue(context);
        }

        return mQueue;
    }

    public static ImageLoader getLoader(Context context){
        if(mLoader==null){
            //实例化二级缓存
            softCache=new HashMap<String, SoftReference<Bitmap>>();

            //实例化一级缓存
            lruCache=new LruCache<String, Bitmap>(2*1024*1024){  //缓存的内存空间为2M
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    // TODO 计算存放成员的大小，返回字节大小
                    return value.getRowBytes()*value.getHeight();
                }

                @Override
                protected void entryRemoved(boolean evicted, String key,
                                            Bitmap oldValue, Bitmap newValue) {
                    // TODO 移除旧成员

                    if(evicted)
                        //将移除的成员存放到二级缓存中
                        softCache.put(key, new SoftReference<Bitmap>(oldValue));

                    super.entryRemoved(evicted, key, oldValue, newValue);
                }
            };

            mLoader=new ImageLoader(getQueue(context), new ImageLoader.ImageCache() {

                @Override
                public void putBitmap(String url, Bitmap bitmap) {
                    // TODO 将图片存放在缓存中的方法
                    //将图片存放在一级缓存中
                    lruCache.put(url, bitmap);

                    //将图片也存放在扩展卡
                    try {
                        ImageUtils.saveImg(url, bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public Bitmap getBitmap(String url) {
                    // TODO 从缓存中读取图片的方法
                    //先从一级缓存中获取
                    Bitmap b=lruCache.get(url);
                    if(b==null){
                        //从二级缓存中读取
                        SoftReference<Bitmap> reference=softCache.get(url);
                        if(reference!=null){ //二级缓存中存在
                            b=reference.get();
                            if(b!=null){
                                //将图片对象存放在一级缓存
                                lruCache.put(url, b);

                                //从二级缓存中移除
                                softCache.remove(reference);
                            }
                        }else{ //从三级缓存中读取--扩展卡
                            b= ImageUtils.getImg(url);
                            if(b!=null){
                                //将图片存放到一级缓存中
                                lruCache.put(url, b);
                            }
                        }
                    }
                    return b;
                }
            });
        }

        return mLoader;
    }

}
