package com.whl.hp.work_house.newhouse_activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.whl.hp.work_house.R;

/**
 * Created by hp-whl on 2015/9/29.
 */
public class NewHouseInfoMapActivity extends Activity{
    BaiduMap baiduMap;
    MapView mapView;
    double lat,lng;
    String name;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.new_houseinfo_map_layout);
        mapView= (MapView) findViewById(R.id.map_bmapView);
        lat=getIntent().getDoubleExtra("lat", 0);
        lng=getIntent().getDoubleExtra("lng", 0);
        name=getIntent().getStringExtra("title");
        baiduMap=mapView.getMap();
        LatLng latLng=new LatLng(lat,lng);
        baiduMap.setMapStatus(MapStatusUpdateFactory.newLatLngZoom(latLng,15));
        View view = LayoutInflater.from(this).inflate(R.layout.new_house_fragment2_mapitem_layout, null);

        TextView textView = (TextView) view.findViewById(R.id.textView);
        textView.setText(name);
        BitmapDescriptor bit = BitmapDescriptorFactory.fromView(textView);

        OverlayOptions option = new MarkerOptions()
                .position(latLng)
                .title(name)
                .icon(bit);
        //在地图上添加Marker，并显示
        baiduMap.addOverlay(option);
    }

    public void cancel(View view){
        finish();

    }
}
