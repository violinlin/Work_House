package com.whl.hp.work_house.newhouse_activity;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.whl.hp.work_house.R;
import com.whl.hp.work_house.tool.Debug;
import com.whl.hp.work_house.tool.VolleyUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp-whl on 2015/9/28.
 */
public class Fragment2 extends Fragment implements BaiduMap.OnMarkerClickListener, BaiduMap.OnMapClickListener {

    View view;
    MapView bmapView;
    BaiduMap baiduMap;
    InfoWindow window;
    TextView cityName, currentPageID;
    ImageButton nextBtn, preBtn;
    static String cName;
    static int pageCount = 1;
    static int total;
    public static List<NewHouseEntity> datas;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getActivity().getApplicationContext());
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.new_house_fragment2, container, false);
        bmapView = (MapView) view.findViewById(R.id.bmapView);
        //设置缩放按键不显示
        bmapView.showZoomControls(false);
        cityName = (TextView) view.findViewById(R.id.cityNameID);
        currentPageID = (TextView) view.findViewById(R.id.currentPageID);
        currentPageID.setText(((pageCount - 1) * 10 + 1) + "-" + (pageCount * 10) + "（" + "共有" + total + "个楼盘" + ")");
        nextBtn = (ImageButton) view.findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pageCount < datas.size() / 10 - 1) {
                    pageCount++;
                    makeMark();
                }
            }
        });
        preBtn = (ImageButton) view.findViewById(R.id.preBtn);
        preBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pageCount > 1) {
                    pageCount--;

                    makeMark();
                }
            }
        });
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        cityName.setText("显示" + cName + "的所有楼盘");
        initBaiduMap();
    }
    private void initBaiduMap() {
        baiduMap = bmapView.getMap();
        baiduMap.setOnMapClickListener(this);
        baiduMap.setOnMarkerClickListener(this);

        makeMark();
    }

    private void makeMark() {
        if (datas != null) {
            baiduMap.clear();
            currentPageID.setText(((pageCount - 1) * 10 + 1) + "-" +
                    (pageCount * 10) + "（" + "共有" + total + "个楼盘" + ")");
            int showIndex = (pageCount - 1) * 10;
            for (int i = showIndex; i < showIndex + 10; i++) {
                if (i >= 0 && i < datas.size()) {
                    //定义Maker坐标点
                    LatLng pt = new LatLng(Double.parseDouble(datas.get(i).getLat()), Double.parseDouble(datas.get(i).getLng()));
                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.new_house_fragment2_mapitem_layout, null);

                    TextView textView = (TextView) view.findViewById(R.id.textView);
                    textView.setText(datas.get(i).getFname());
                    BitmapDescriptor bit = BitmapDescriptorFactory.fromView(textView);

                    OverlayOptions option = new MarkerOptions()
                            .position(pt)
                            .title(datas.get(i).getFname())
                            .icon(bit);
                    //在地图上添加Marker，并显示
                    baiduMap.addOverlay(option);

                }

            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bmapView.onDestroy();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
//        Toast.makeText(getActivity(), marker.getTitle(), Toast.LENGTH_SHORT).show();

        intitInfoWindow(marker);

        return true;
    }


    public void intitInfoWindow(Marker marker) {
        NewHouseEntity data = null;
        View view = null;
        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i).getFname().equals(marker.getTitle())) {
                data = datas.get(i);
            }
        }

        if (data != null) {

            view = LayoutInflater.from(getActivity()).inflate(R.layout.new_house_fragment2_item, null);
            view.setAlpha(0.8f);
            ImageView new_image = (ImageView) view.findViewById(R.id.new_image);
            TextView new_fpricedisplaystr = (TextView) view.findViewById(R.id.new_fpricedisplaystr);
            TextView new_price = (TextView) view.findViewById(R.id.new_price);
            VolleyUtils.getLoader(getActivity()).get(data.getFcover(), ImageLoader.getImageListener(new_image, R.drawable.ershou, android.R.drawable.ic_menu_delete));
            new_fpricedisplaystr.setText(data.getFpricedisplaystr());
            new_price.setText(data.getFregion());

            window = new InfoWindow(view, marker.getPosition(), -40);

            baiduMap.showInfoWindow(window);
        }

    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (window != null) {
            baiduMap.hideInfoWindow();

        }
    }

    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        return false;
    }
}
