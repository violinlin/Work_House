package com.whl.hp.work_house;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.whl.hp.work_house.adapter.CommentAdapter;
import com.whl.hp.work_house.been.CommentEntity;
import com.whl.hp.work_house.tool.Config;
import com.whl.hp.work_house.tool.Debug;
import com.whl.hp.work_house.tool.NUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp-whl on 2015/9/25.
 */
public class ZiXunActivity extends Activity implements ViewPager.OnPageChangeListener {

    WebView webView;
    String newId;
    //评论的id
    String commentid;
    List<View> pagerList;
    String type = "0";
    ViewPager viewPager;
    List<String> imageUrls;
    List<CommentEntity> commentEntities;
    ListView commentListView;
    String commentcount="10";
    String websurl;
    String text;
    //web内容的id
    String id;
    AnimationDrawable animationDrawable;
    boolean dataComplete = false;
    RelativeLayout relativeLayout;
    LinearLayout commentLayout,titleLayout;
    ImageButton imageButton,shareID;
    TextView titleTV;
    ImageView waitImageView;
    EditText editText;
    Button commentBtn;
    TextView titleTv,timeTv;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zixun_layout);




        imageUrls = new ArrayList();
        viewPager = (ViewPager) findViewById(R.id.ViewPagerID);
        viewPager.setOnPageChangeListener(this);
        waitImageView = (ImageView) findViewById(R.id.waitImageViewID);
        animationDrawable = (AnimationDrawable) waitImageView.getBackground();
        animationDrawable.start();
        commentid = getIntent().getStringExtra("commentid");
        commentcount=getIntent().getStringExtra("commentcount");
        newId = getIntent().getStringExtra("newId");
        type = getIntent().getStringExtra("type");
        initFindByid();
        loadData();


    }

    private void initFindByid() {
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        commentLayout= (LinearLayout) findViewById(R.id.commentLayoutID);
        imageButton= (ImageButton) findViewById(R.id.imageButtonID);
        shareID= (ImageButton) findViewById(R.id.shareID);
        titleTV = (TextView) findViewById(R.id.titleTV);
        editText= (EditText) findViewById(R.id.editTextID);
        commentBtn= (Button) findViewById(R.id.commentBtnID);
        commentBtn.setText("评论："+commentcount);

        titleLayout= (LinearLayout) getLayoutInflater().inflate(R.layout.commenttitle_layout,null);
        titleTV= (TextView) titleLayout.findViewById(R.id.titleTvId);
        timeTv= (TextView) titleLayout.findViewById(R.id.timeTvId);
    }

    private void initViewPager() {
        pagerList = new ArrayList();
        if (dataComplete) {
            if (type.equals("0")) {
                View view1 = getLayoutInflater().inflate(R.layout.viewpater_0layout, null);
                View view2 = getLayoutInflater().inflate(R.layout.viewpatercomment_layout, null);
                pagerList.add(view1);
                pagerList.add(view2);
                viewPager.setAdapter(new ViewAdapter());
                commentListView = (ListView) view2.findViewById(R.id.listView);

                webView = (WebView) view1.findViewById(R.id.webView);
                webView.loadUrl(websurl + id);

                loadComment();

            } else if (type.equals("1")) {
                relativeLayout.setBackgroundColor(Color.BLACK);
                imageButton.setBackgroundColor(Color.BLACK);
                shareID.setBackgroundColor(Color.BLACK);
                commentLayout.setBackgroundColor(Color.BLACK);
                titleTV.setText(1+"/"+imageUrls.size());
                for (int i = 0; i < imageUrls.size(); i++) {
                    View view = getLayoutInflater().inflate(R.layout.viewpater_1layout, null);
                    ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
                    TextView textView = (TextView) view.findViewById(R.id.textView);
                    textView.setText(text);
                    pagerList.add(view);
                    loadImage(imageUrls.get(i), imageView);


                }


                View view2 = getLayoutInflater().inflate(R.layout.viewpatercomment_layout, null);
                pagerList.add(view2);
                viewPager.setAdapter(new ViewAdapter());
                commentListView = (ListView) view2.findViewById(R.id.listView);
                loadComment();

            }
        }


    }

    private void loadData() {
        NUtils.get(NUtils.TYPE_TXT, String.format(Config.NEWS_DETAIL, newId), new NUtils.AbsCallback() {
            @Override
            public void response(String url, byte[] bytes) {
                super.response(url, bytes);
                try {
                    String json = new String(bytes, "utf-8");
                    JSONObject obj1 = new JSONObject(json);
                    id = obj1.getString("id");
                    Debug.startDebug(url + "图片");
                    websurl = obj1.getString("surl");

                    if (type.equals("1")) {



                        JSONArray array = obj1.getJSONArray("content");

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj2 = array.getJSONObject(i);
                            if (obj2.getInt("type") == 1) {
                                text = obj2.getString("value");
                            } else if (obj2.getInt("type") == 2) {
                                imageUrls.add(obj2.getString("value"));

                            }
                        }
                    }

                    dataComplete = true;
                    waitImageView.setVisibility(View.GONE);
                    initViewPager();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void loadImage(String url, final ImageView imageView) {
        NUtils.get(NUtils.TYPE_IMG, url, new NUtils.AbsCallback() {
            @Override
            public void response(String url, byte[] bytes) {

                super.response(url, bytes);
                Debug.startDebug(url + "图片类表");
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageView.setImageBitmap(bitmap);

            }
        });

    }

    public void loadComment() {
        NUtils.get(NUtils.TYPE_TXT, String.format(Config.NEWS_COMMENT, commentid), new NUtils.AbsCallback() {
            @Override
            public void response(String url, byte[] bytes) {
                super.response(url, bytes);
                try {
                    String json = new String(bytes, "utf-8");
                    JSONObject obj1 = new JSONObject(json);
                    JSONObject obj2 = obj1.getJSONObject("data");
                    JSONArray array = obj2.getJSONArray("comments");
                    TypeToken<List<CommentEntity>> typeToken = new TypeToken<List<CommentEntity>>() {
                    };
                    Gson gson = new Gson();
                    commentEntities = gson.fromJson(array.toString(), typeToken.getType());

                    CommentAdapter adapter = new CommentAdapter(ZiXunActivity.this, commentEntities);
                    commentListView.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == pagerList.size() - 1) {
            titleTV.setText("评论");
            commentBtn.setText("原文");
            commentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(0);
                }
            });
        }
        if (type.equals("0")){
           if (position<pagerList.size()-1){
                titleTV.setText("房产资讯");
           }
        }

        if (type.equals("1")) {

            if (position < pagerList.size() - 1) {
                titleTV.setText((position+1) + "/" + imageUrls.size());

            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    class ViewAdapter extends PagerAdapter {


        @Override
        public int getCount() {
            return pagerList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(pagerList.get(position));
            return pagerList.get(position); //作为当前页面的数据返回
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            container.removeView(pagerList.get(position));
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    public void cancel(View view) {
        finish();
    }

}
