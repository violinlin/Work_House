package com.whl.hp.work_house.newhouse_activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.whl.hp.work_house.R;
import com.whl.hp.work_house.tool.Debug;
import com.whl.hp.work_house.tool.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hp-whl on 2015/9/29.
 */
public class LoupanInfoFragment extends Fragment implements BaiduMap.OnMapClickListener {
    View view;
    MapView mapView;
    BaiduMap baiduMap;
    String url;
    String id;
    TextView title, address;
    double lat;
    double lng;
    String name;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        url = getArguments().getString("url");
        loadData();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getActivity().getApplicationContext());

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.new_house_loupaninfo_fragment_layout, container, false);
        mapView = (MapView) view.findViewById(R.id.map_bmapView);
        title = (TextView) view.findViewById(R.id.titleId);
        address = (TextView) view.findViewById(R.id.address);
        mapView.showZoomControls(false);
        mapView.showScaleControl(false);
        baiduMap = mapView.getMap();
        baiduMap.setOnMapClickListener(this);
        baiduMap.getUiSettings().setAllGesturesEnabled(false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMap();
            }
        });
    }

    private void loadData() {
        final JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                Toast.makeText(getActivity(), "请求成功", Toast.LENGTH_SHORT).show();
                try {

                    String latS = response.getString("lat");
                    lat = Double.parseDouble(latS);
                    String lngS = response.getString("lng");
                    lng = Double.parseDouble(lngS);
                    name = response.getString("name");
                    JSONArray array = response.getJSONArray("info");
                    String a = array.toString();
                    String aa[] = a.split(",");
                    address.setText(aa[0].substring(aa[0].indexOf("\"") + 1, aa[0].lastIndexOf("\"")));
                    title.setText(name);
                    id = response.getString("id");
                    LatLng latlnt = new LatLng(lat, lng);
                    baiduMap.addOverlay(new TextOptions().bgColor(Color.LTGRAY)
                                    .fontSize(20).position(latlnt).text(name)
                    );
                    baiduMap.setMapStatus(MapStatusUpdateFactory.newLatLngZoom(latlnt, 15));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (getActivity() != null) {
                    Toast.makeText(getActivity(), "请求失败", Toast.LENGTH_SHORT).show();

                }
            }
        });

        VolleyUtils.mQueue.add(request);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        showMap();

    }

    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        return false;
    }

    public void showMap() {
        Intent intent = new Intent(getActivity(), NewHouseInfoMapActivity.class);
        intent.putExtra("lat", lat);
        intent.putExtra("lng", lng);
        intent.putExtra("title", name);
        startActivity(intent);
    }
}
