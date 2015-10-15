package com.whl.hp.work_house.LookHouse;

import android.app.Activity;
import android.content.ComponentCallbacks;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.whl.hp.work_house.MainActivity;
import com.whl.hp.work_house.R;
import com.whl.hp.work_house.adapter.AbsAdapter;
import com.whl.hp.work_house.tool.Config;
import com.whl.hp.work_house.tool.Debug;
import com.whl.hp.work_house.tool.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp-whl on 2015/10/5.
 */
public class LookHouseActivity extends Activity {

    ListView listView;
    ImageView waitImageView;
    String url;
    List<LookHouseEntitys> datas;
    AnimationDrawable animation;
    AbsAdapter<LookHouseEntitys> adapter;
    BaiduMap baidumap;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.lookhouse_layout);
        listView = (ListView) findViewById(R.id.listView);
        waitImageView = (ImageView) findViewById(R.id.waitImageViewID);
        url = String.format(Config.TEAM_HOUSE, MainActivity.cityID);
        Debug.startDebug(url + "看房团");
        animation = (AnimationDrawable) waitImageView.getBackground();
        animation.start();
        initAdapter();

        loadDatas();

    }

    public void cancel(View view) {
        finish();
    }

    private void initAdapter() {
        datas = new ArrayList<LookHouseEntitys>();
        adapter = new AbsAdapter<LookHouseEntitys>(this, R.layout.lookhouse_item_layout, datas) {
            @Override
            public void bindView(ViewHolder vHolder, LookHouseEntitys data) {
                TextView name = (TextView) vHolder.getView(R.id.name);
                TextView alias = (TextView) vHolder.getView(R.id.alias);
                name.setText(data.getName());
                alias.setText(data.getAlias());
                LinearLayout linear = (LinearLayout) vHolder.getView(R.id.linearlayout);
                linear.removeAllViews();
                MapView mapView = (MapView) vHolder.getView(R.id.baiduMap);

                baidumap = mapView.getMap();
                mapView.showZoomControls(false);
                mapView.showScaleControl(false);
                baidumap.getUiSettings().setAllGesturesEnabled(false);

                LatLng center = null;
                baidumap.clear();
                for (int i = 0; i < data.getHouses().size(); i++) {
                    TextView text = new TextView(LookHouseActivity.this);
                    Houses houses = data.getHouses().get(i);
                    text.setTextColor(Color.RED);
                    text.setText(houses.getName() + "           " + houses.getPrice_pre() + houses.getPrice_value() + houses.getPrice_unit());
                    linear.addView(text);

                    //定义文字所显示的坐标点
                    LatLng llText = new LatLng(Double.parseDouble(houses.getLat()), Double.parseDouble(houses.getLng()));
                    if (i == 0) {
                        center = llText;
                    }
//构建文字Option对象，用于在地图上添加文字
                    OverlayOptions textOption = new TextOptions()
                            .bgColor(Color.BLACK)
                            .fontSize(15)
                            .fontColor(Color.WHITE)
                            .text(houses.getName())
                            .rotate(0)
                            .position(llText);
//在地图上添加该文字对象并显示
                    baidumap.addOverlay(textOption);
                }
                if (center != null) {
                    baidumap.setMapStatus(MapStatusUpdateFactory.newLatLngZoom(center, 10));

                }
            }
        };

        listView.setAdapter(adapter);
    }

    private void loadDatas() {
        final JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray array = response.getJSONArray("routes");
                    List<LookHouseEntitys> l = new ArrayList<LookHouseEntitys>();
                    List<Houses> houseList;
                    LookHouseEntitys look = null;
                    for (int i = 0; i < array.length(); i++) {

                        look = new LookHouseEntitys();
                        JSONObject obj1 = array.getJSONObject(i);
                        String rid = obj1.getString("rid");
                        String name = obj1.getString("name");
                        String alias = obj1.getString("alias");
                        String deadtime = obj1.getString("deadtime");
                        look.setName(name);
                        look.setAlias(alias);
                        look.setDeadtime(deadtime);
                        look.setRid(rid);
                        JSONArray array2 = obj1.getJSONArray("houses");
                        Houses houses;
                        houseList = new ArrayList<Houses>();
                        for (int j = 0; j < array2.length(); j++) {
                            houses = new Houses();
                            JSONObject obj2 = array2.getJSONObject(j);
                            String id = obj2.getString("id");
                            String name2 = obj2.getString("name");
                            String lat = obj2.getString("lat");
                            String lng = obj2.getString("lng");
                            String fcover = obj2.getString("fcover");
                            String price_pre = obj2.getString("price_pre");
                            String price_value = obj2.getString("price_value");
                            String price_unit = obj2.getString("price_unit");
                            houses.setFcover(fcover);
                            houses.setId(id);
                            houses.setLat(lat);
                            houses.setLng(lng);
                            houses.setName(name2);
                            houses.setPrice_pre(price_pre);
                            houses.setPrice_unit(price_unit);
                            houses.setPrice_value(price_value);
                            houseList.add(houses);
                        }
                        look.setHouses(houseList);
                        l.add(look);
                    }
                    datas.addAll(l);
                    adapter.notifyDataSetChanged();
                    animation.stop();
                    waitImageView.setVisibility(View.GONE);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LookHouseActivity.this, "加载失败", Toast.LENGTH_SHORT).show();

            }
        });

        VolleyUtils.getQueue(this).add(request);

    }


}
