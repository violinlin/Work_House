package com.whl.hp.work_house;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.whl.hp.work_house.adapter.CityListAdapter;
import com.whl.hp.work_house.been.CityEntitys;
import com.whl.hp.work_house.tool.Config;
import com.whl.hp.work_house.tool.Debug;
import com.whl.hp.work_house.tool.NUtils;
import com.whl.hp.work_house.view.SideBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp-whl on 2015/9/23.
 */
public class CityListActivity extends Activity {
    ListView listView;
    List<CityEntitys> cityEntityses;
    SideBar sideBar;
    String GPSCityName;
    SearchView searchView;
    CityListAdapter cityListAdapter;
    List<CityEntitys> searchList;
    boolean isSearch = false;
    TextView localTV;
    public LocationClient mLocationClient;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        listView.setSelected(true);
        localTV = (TextView) findViewById(R.id.localTV);
        sideBar = (SideBar) findViewById(R.id.sideBar);

        loadCityData();
        initSearchView();
        initBaiduDitu();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                if (isSearch) {
                    intent.putExtra("city", searchList.get(position).getCityname());
                    intent.putExtra("cityID", searchList.get(position).getCityid());
                } else {
                    intent.putExtra("city", cityEntityses.get(position).getCityname());
                    intent.putExtra("cityID", cityEntityses.get(position).getCityid());

                }

                setResult(2, intent);
                finish();

            }
        });
    }

    private void initBaiduDitu() {
            mLocationClient = new LocationClient(getApplicationContext());
            //2 设置定位服务的请求参数（定位的参数等行）
            LocationClientOption locationClientOption = new LocationClientOption();
            locationClientOption.setCoorType("bd09ll");//设置坐标系
            locationClientOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
            locationClientOption.setOpenGps(true);
            locationClientOption.setIsNeedAddress(true);
            locationClientOption.setNeedDeviceDirect(true);
            mLocationClient.setLocOption(locationClientOption);
            mLocationClient.registerLocationListener(new BDLocationListener() {
                @Override
                public void onReceiveLocation(BDLocation bdLocation) {
                    GPSCityName = bdLocation.getCity();
                    localTV.setText("GPS定位城市:" + GPSCityName);
                    mLocationClient.stop();


                }
            });
            //开启定位服务
            mLocationClient.start();
    }
    public void backLoackCity(View view){
        if (GPSCityName!=null){
            for (int i=0;i<cityEntityses.size();i++){
                if (GPSCityName.contains(cityEntityses.get(i).getCityname())){
                    Intent intent=new Intent();
                    intent.putExtra("city", cityEntityses.get(i).getCityname());
                    intent.putExtra("cityID", cityEntityses.get(i).getCityid());
                    setResult(2, intent);
                    finish();
                }
            }

        }

    }


    private void initSearchView() {
        searchView = (SearchView) findViewById(R.id.searchCity);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                isSearch = true;
                searchList = new ArrayList<CityEntitys>();
                cityListAdapter = new CityListAdapter(CityListActivity.this, searchList);
                listView.setAdapter(cityListAdapter);
                for (int i = 0; i < cityEntityses.size(); i++) {
                    if (cityEntityses.get(i).getCitypinyin() != null && cityEntityses.get(i).getCitypinyin().contains(newText)) {
                        searchList.add(cityEntityses.get(i));
                    }
                    if (cityEntityses.get(i).getCityname().contains(newText)) {

                        searchList.add(cityEntityses.get(i));

                    }
                }
                cityListAdapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    private void initSideBar() {
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {

                int position = 0;
                if (cityEntityses.size() > 0) {
                    for (int i = 0; i < cityEntityses.size(); i++) {
                        if (cityEntityses.get(position).getType() == CityEntitys.TYPE_LABEL) {
                            if (s.equals(cityEntityses.get(i).getCityname())) {
                                position = i;
                                listView.setSelection(position);
                            }
                        }
                    }
                }
            }
        });
    }

    //结束本Activity
    public void cancel(View view) {
        finish();
    }

    //加载城市列表信息
    public void loadCityData() {
        NUtils.get(NUtils.TYPE_TXT, Config.CHOICE_CITY, new NUtils.AbsCallback() {
            @Override
            public void response(String url, byte[] bytes) {
                super.response(url, bytes);
                try {
                    cityEntityses = new ArrayList<CityEntitys>();
                    String json = new String(bytes, "utf-8");
                    JSONObject obj1 = new JSONObject(json);
                    JSONObject obj2 = obj1.getJSONObject("cities");
                    CityEntitys city;
                    for (char c = 'A'; c <= 'Z'; c++) {
                        if (c == 'I' || c == 'O' || c == 'U' || c == 'V') {
                            continue;
                        }
                        JSONArray array = obj2.getJSONArray(String.valueOf(c));

                        city = new CityEntitys();
                        city.setCategory("lable");
                        city.setCityname(String.valueOf(c));
                        cityEntityses.add(city);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj3 = array.getJSONObject(i);
                            city = new CityEntitys();
                            city.setCategory("name");
                            String cityid = obj3.getString("cityid");
                            String cityname = obj3.getString("cityname");
                            String citypinyin = obj3.getString("citypinyin");

                            city.setCityname(cityname);
                            city.setCityid(cityid);
                            city.setCitypinyin(citypinyin);
                            cityEntityses.add(city);

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                initSideBar();
                cityListAdapter = new CityListAdapter(getApplicationContext(), cityEntityses);
                listView.setAdapter(cityListAdapter);
//                cityListAdapter.notifyDataSetChanged();

            }
        });
    }
}
