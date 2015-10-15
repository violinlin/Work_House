package com.whl.hp.work_house.zixun;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.search.poi.PoiBoundSearchOption;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.whl.hp.work_house.MainActivity;
import com.whl.hp.work_house.R;
import com.whl.hp.work_house.ZiXunActivity;
import com.whl.hp.work_house.adapter.ListWebAdapter;
import com.whl.hp.work_house.been.ListWebEntitys;
import com.whl.hp.work_house.tool.Config;
import com.whl.hp.work_house.tool.Debug;
import com.whl.hp.work_house.tool.ImageUtils;
import com.whl.hp.work_house.tool.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by hp-whl on 2015/10/5.
 */
public class ZiXun2Activity extends Activity {
    PullToRefreshListView pull_listView;
    String url;
    List<ListWebEntitys> datas;
    ListWebAdapter adapterListWeb;
    ImageView waitImageView;
    AnimationDrawable animation;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zixun2_layout);
        waitImageView = (ImageView) findViewById(R.id.waitImageViewID);
        animation = (AnimationDrawable) waitImageView.getBackground();
        animation.start();
        url = String.format(Config.ZIXUN_URL, MainActivity.cityID);
        pull_listView = (PullToRefreshListView) findViewById(R.id.pull_listView);
        initAdapter();
        loadData();
    }

    private void initAdapter() {
//        pull_listView.setMode(PullToRefreshBase.Mode.BOTH);
        pull_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ZiXun2Activity.this, ZiXunActivity.class);
                intent.putExtra("newId", datas.get(position - 1).getId());
                Debug.startDebug(datas.get(position - 1).getImagecount());
                intent.putExtra("type", datas.get(position - 1).getType());
                intent.putExtra("commentcount", datas.get(position - 1).getCommentcount());
                intent.putExtra("commentid", datas.get(position - 1).getCommentid());
                startActivity(intent);

            }
        });
//        pull_listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
//            @Override
//            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//
//            }
//
//            @Override
//            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//
//            }
//        });
        adapterListWeb = new ListWebAdapter(this);
        pull_listView.setAdapter(adapterListWeb);
    }

    private void loadData() {
        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONArray array = null;
                try {
                    array = response.getJSONArray("data");

                    TypeToken<List<ListWebEntitys>> typeToken = new TypeToken<List<ListWebEntitys>>() {
                    };
                    Gson gson = new Gson();
                    datas = gson.fromJson(array.toString(), typeToken.getType());
                    ListWebEntitys data = datas.get(0);
                    firstItem(data);

                    datas.remove(datas.get(0));

                    adapterListWeb.add(datas);
                    datas.add(0,data);
                    adapterListWeb.notifyDataSetChanged();
//                    pull_listView.onRefreshComplete();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                animation.stop();
                waitImageView.setVisibility(View.GONE);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ZiXun2Activity.this, "加载失败", Toast.LENGTH_SHORT).show();

            }
        });

        VolleyUtils.getQueue(this).add(request);
    }

    private void firstItem(ListWebEntitys data) {
        View view = getLayoutInflater().inflate(R.layout.zixun2first_layout, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        Bitmap bitmap = ImageUtils.getImg(data.getThumbnail());
        if (bitmap == null) {
            bitmap = ImageUtils.getImg(data.getGroupthumbnail());
        }
        if (bitmap == null) {

            VolleyUtils.getLoader(this).get(data.getThumbnail(),ImageLoader.getImageListener(imageView,
                    R.drawable.ershou, android.R.drawable.ic_menu_delete));
        } else {
            imageView.setImageBitmap(bitmap);
        }


        TextView textView = (TextView) view.findViewById(R.id.text);
        textView.setText(data.getTitle());
        pull_listView.getRefreshableView().addHeaderView(view);

    }

    public void cancel(View view) {
        finish();
    }
}
