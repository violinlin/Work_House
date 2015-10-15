package com.whl.hp.work_house;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.whl.hp.work_house.LookHouse.LookHouseActivity;
import com.whl.hp.work_house.adapter.ListWebAdapter;
import com.whl.hp.work_house.been.ListWebEntitys;
import com.whl.hp.work_house.ershou.ErShouHouseActivity;
import com.whl.hp.work_house.newhouse_activity.NewHouseActivity;
import com.whl.hp.work_house.tool.Config;
import com.whl.hp.work_house.tool.Debug;
import com.whl.hp.work_house.tool.NUtils;
import com.whl.hp.work_house.view.PagerView;
import com.whl.hp.work_house.youhui.YouHuiActivity;
import com.whl.hp.work_house.zixun.ZiXun2Activity;
import com.whl.hp.work_house.zufang.ZuFangActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hp-whl on 2015/9/26.
 */
public class NewHouseFragment extends Fragment implements View.OnClickListener {
    ViewPager viewPager;
    //当前城市的id
    static Button currentButton;
    String cityID = "1";
    int headCount = 3;
    //第三方listview
    PullToRefreshListView listViewWed;
    //城市的信息列表
    List<ListWebEntitys> listWebEntityses;
    //城市的信息列表的适配器
    ListWebAdapter adapterListWeb;
    //Viewpager传值的类
    PagerView pagetView;
    //图标布局
    View tubiaoLayout;
    View view;
    //广告布局
    View viewPagerLayout;
    View moretubiaoLayout;
    //是够显示更过图标
    boolean more = false;
    boolean isclear = false;
    int btnId[] = {R.id.xinfangBtn, R.id.ershouBtn, R.id.zufangBtn,
            R.id.zixunBtn, R.id.youhuiBtn, R.id.kaipanBtn, R.id.jisuanBtn, R.id.moreBtn};
    ImageButton tubiaoBtn[] = new ImageButton[btnId.length];

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.newhouse_layout, container, false);
        currentButton = (Button) view.findViewById(R.id.currentCity);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initLayout();
    }
    //实例化布局文件，
    public void initLayout() {
        listViewWed = (PullToRefreshListView) view.findViewById(R.id.listViewWeb);
        tubiaoLayout = LayoutInflater.from(getActivity()).inflate(R.layout.tubiao_layout, null);
        moretubiaoLayout = LayoutInflater.from(getActivity()).inflate(R.layout.moretubiao_layout, null);
        initMoreTubiao(moretubiaoLayout);
        viewPagerLayout = LayoutInflater.from(getActivity()).inflate(R.layout.viewpager_layout, null);

        initTubiaoButton();
        initListViewWeb();


    }

    private void initMoreTubiao(View view) {
        ImageButton qImage= (ImageButton) view.findViewById(R.id.qImage);
        ImageButton lookImage= (ImageButton) view.findViewById(R.id.lookImage);
        ImageButton packImage= (ImageButton) view.findViewById(R.id.packImage);
        lookImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), LookHouseActivity.class);
                startActivity(in);

            }
        });
    }

    //加载list数据，并为list添加头布局
    private void initListViewWeb() {
        listViewWed.getRefreshableView().addHeaderView(viewPagerLayout);
        listViewWed.getRefreshableView().addHeaderView(tubiaoLayout);
        intiViewPager();
        adapterListWeb = new ListWebAdapter(getActivity());
        listViewWed.setAdapter(adapterListWeb);
        String url = String.format(Config.FIRST_PAGE_LISTVIEW, 10, 0, 0, cityID);
        loadListWebData(url);
        listViewWed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ZiXunActivity.class);
                intent.putExtra("newId", listWebEntityses.get(position - headCount).getId());
                Debug.startDebug(listWebEntityses.get(position - headCount).getImagecount());
                intent.putExtra("type", listWebEntityses.get(position - headCount).getType());
                intent.putExtra("commentcount", listWebEntityses.get(position - headCount).getCommentcount());
                intent.putExtra("commentid", listWebEntityses.get(position - headCount).getCommentid());
                startActivity(intent);

            }
        });
        listViewWed.setMode(PullToRefreshBase.Mode.BOTH);
        listViewWed.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            //            下拉刷新
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String url = String.format(Config.FIRST_PAGE_LISTVIEW, 20, 1, 1, cityID);
                isclear = true;
                loadListWebData(url);
            }

            //          上拉加载
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                String url = String.format(Config.FIRST_PAGE_LISTVIEW, 20, 0, 1, cityID);
                isclear = false;
                loadListWebData(url);
            }
        });


    }
    private void intiViewPager() {
        viewPager = (ViewPager) viewPagerLayout.findViewById(R.id.viewPagerId);
        final TextView viewpagerTV = (TextView) viewPagerLayout.findViewById(R.id.titleId);
        final LinearLayout nav = (LinearLayout) viewPagerLayout.findViewById(R.id.navLayoutId);
        pagetView = new PagerView(viewPager, viewpagerTV, nav, getActivity());
        String url = String.format(Config.FIRST_PAGE_WEBVIEW, "1");
        loadViewPager(url);
    }

    //加载广告数据
    public void loadViewPager(String url) {
        NUtils.get(NUtils.TYPE_TXT, url, new NUtils.AbsCallback() {
            @Override
            public void response(String url, byte[] bytes) {
                super.response(url, bytes);
                try {
                    String json = new String(bytes, "utf-8");
                    JSONObject obj1 = new JSONObject(json);
                    JSONArray array = obj1.getJSONArray("data");
                    List<Map<String, String>> datas = new ArrayList<Map<String, String>>();
                    Map<String, String> map = null;
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj2 = array.getJSONObject(i);
                        String title = obj2.getString("title");
                        String picurl = obj2.getString("picurl");

                        map = new HashMap<String, String>();
                        map.put(PagerView.KEY_TITLE, title);
                        map.put(PagerView.KEY_URL, picurl);
                        datas.add(map);
                    }
                    pagetView.setDatas(datas);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }




    public void loadListWebData(String url) {
        NUtils.get(NUtils.TYPE_TXT, url, new NUtils.AbsCallback() {
            @Override
            public void response(String url, byte[] bytes) {
                super.response(url, bytes);
                try {
                    String json = new String(bytes, "utf-8");
                    JSONObject obj1 = new JSONObject(json);
                    JSONArray array = obj1.getJSONArray("data");
                    TypeToken<List<ListWebEntitys>> typeToken = new TypeToken<List<ListWebEntitys>>() {
                    };
                    Gson gson = new Gson();
                    listWebEntityses = gson.fromJson(array.toString(), typeToken.getType());
                    if (isclear) {
                        adapterListWeb = new ListWebAdapter(getActivity());
                        listViewWed.setAdapter(adapterListWeb);
                    }
                    adapterListWeb.add(listWebEntityses);
                    adapterListWeb.notifyDataSetChanged();
                    listViewWed.onRefreshComplete();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }



    public void reLoad(String cityID) {
        this.cityID = cityID;
        isclear = true;
        String url = String.format(Config.FIRST_PAGE_LISTVIEW, 10, 0, 0, cityID);
        loadListWebData(url);
        url = String.format(Config.FIRST_PAGE_WEBVIEW, cityID);
        loadViewPager(url);
    }

    //初始化图标并为其添加监听
    private void initTubiaoButton() {
        for (int i = 0; i < btnId.length; i++) {
            tubiaoBtn[i] = (ImageButton) tubiaoLayout.findViewById(btnId[i]);
            tubiaoBtn[i].setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.xinfangBtn:
                Intent intent=new Intent(getActivity(), NewHouseActivity.class);
                intent.putExtra("cityID",cityID);
                intent.putExtra("Name","新房");
                intent.putExtra("cityName",currentButton.getText().toString());
                startActivity(intent);
                break;
            case R.id.ershouBtn:
                Intent intent3=new Intent(getActivity(), ErShouHouseActivity.class);
                startActivity(intent3);
                break;
            case R.id.zufangBtn:
                Intent intent4=new Intent(getActivity(), ZuFangActivity.class);
                startActivity(intent4);
                break;
            case R.id.zixunBtn:

                Intent intent5=new Intent(getActivity(), ZiXun2Activity.class);
                startActivity(intent5);
                break;
            case R.id.youhuiBtn:
                Intent intent1=new Intent(getActivity(), YouHuiActivity.class);
                startActivity(intent1);
                break;

            case R.id.kaipanBtn:
                Intent intent2=new Intent(getActivity(), NewHouseActivity.class);
                intent2.putExtra("cityID",cityID);
                intent2.putExtra("Name","最新开盘");
                intent2.putExtra("cityName",currentButton.getText().toString());
                startActivity(intent2);
                break;
            case R.id.jisuanBtn:
                break;
            case R.id.moreBtn:
                more = !more;
                if (more) {
                    headCount = 4;
                    listViewWed.getRefreshableView().addHeaderView(moretubiaoLayout);

                } else {
                    listViewWed.getRefreshableView().removeHeaderView(moretubiaoLayout);
                    headCount = 3;
                }
                break;
        }

    }
}
