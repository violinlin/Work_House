package com.whl.hp.work_house.newhouse_activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.whl.hp.work_house.R;
import com.whl.hp.work_house.adapter.AbsAdapter;
import com.whl.hp.work_house.adapter.CommentAdapter;
import com.whl.hp.work_house.tool.Config;
import com.whl.hp.work_house.tool.Debug;
import com.whl.hp.work_house.tool.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp-whl on 2015/9/28.
 */
public class Fragment1 extends Fragment implements View.OnClickListener {

    View view;
    PopupWindow win;
    Button areaB, categoryB, priceB, moreB;
    List<NewHouseEntity> datas;
    AbsAdapter<NewHouseEntity> adapter;
    ListView listView;
    String cityID;
    Gson gson;
    String url;
    int total;
    int rn;
   public static  String titleName;
    LinearLayout new_layout;
    List<NewHouseEntity> list;
    int pageCount = 1;
    boolean isBootom;
    TextView textCount;
    ImageView waitImageViewID;
    AnimationDrawable animation;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        cityID = getArguments().getString("cityID");
        titleName = getArguments().getString("titleName");
        if (titleName.equals("新房")) {
            url = String.format(Config.LOOKING_NEWHOUSE, pageCount, cityID);

        } else {
            url = String.format(Config.NEWEST_HOUSE, pageCount, cityID);

        }

        gson = new Gson();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.new_house_fragment1, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        textCount = (TextView) view.findViewById(R.id.textCount);
        new_layout = (LinearLayout) view.findViewById(R.id.new_layout);
        waitImageViewID = (ImageView) view.findViewById(R.id.waitImageViewID);
        animation = (AnimationDrawable) waitImageViewID.getBackground();
        initButton();

        return view;
    }

    private void initButton() {
        areaB = (Button) view.findViewById(R.id.area);
        areaB.setOnClickListener(this);
        categoryB = (Button) view.findViewById(R.id.category);
        categoryB.setOnClickListener(this);
        priceB = (Button) view.findViewById(R.id.price);
        priceB.setOnClickListener(this);
        moreB = (Button) view.findViewById(R.id.more);
        moreB.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        animation.start();
        downJsonObject(url);
        initListView();
    }

    private void initListView() {
        datas = new ArrayList<NewHouseEntity>();

        Fragment2.datas = new ArrayList<>();
        adapter = new AbsAdapter<NewHouseEntity>(getActivity(), R.layout.new_house_fragment1_item, datas) {
            @Override
            public void bindView(ViewHolder vHolder, NewHouseEntity data) {
                ImageView new_image = (ImageView) vHolder.getView(R.id.new_image);
                TextView new_fname = (TextView) vHolder.getView(R.id.new_fname);
                TextView new_fpricedisplaystr = (TextView) vHolder.getView(R.id.new_fpricedisplaystr);
                TextView new_fregion = (TextView) vHolder.getView(R.id.new_fregion);
                TextView new_address = (TextView) vHolder.getView(R.id.new_address);
                LinearLayout new_layout = (LinearLayout) vHolder.getView(R.id.new_layout);
                new_layout.removeAllViews();
                VolleyUtils.getLoader(getActivity()).get(data.getFcover(), ImageLoader.getImageListener(new_image, R.drawable.ershou, android.R.drawable.ic_menu_delete));
                new_fname.setText(data.getFname());
                new_fpricedisplaystr.setText(data.getFpricedisplaystr());
                new_fregion.setText(data.getFregion());
                new_address.setText(data.getFaddress());
                int color[] = {Color.RED, Color.YELLOW, Color.BLUE, Color.CYAN, Color.DKGRAY};

                if (data.getBookmark() != null) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.leftMargin = 20;
                    Debug.startDebug(data.getBookmark().size() + "标签长度");
                    for (int i = 0; i < data.getBookmark().size(); i++) {
                        TextView textView = new TextView(getActivity());

                        textView.setTextColor(Color.BLACK);
                        textView.setLayoutParams(params);
                        textView.setText(data.getBookmark().get(i).getTag());

                        if (i < color.length) {
                            textView.setBackgroundColor(color[i]);
                        }
                        new_layout.addView(textView);
                    }
                }
            }
        };
        listView.setAdapter(adapter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (isBootom && scrollState == SCROLL_STATE_IDLE) {
                    Toast.makeText(getActivity(), "加载数据...", Toast.LENGTH_SHORT).show();
                    if (pageCount < (total / rn + 1)) {
                        pageCount++;
                        url = String.format(Config.LOOKING_NEWHOUSE, pageCount, cityID);
                        downJsonObject(url);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                isBootom = ((firstVisibleItem + visibleItemCount) == totalItemCount);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = String.format(Config.NEW_HOUSE_INFO, datas.get(position).getFid());
                String picName = datas.get(position).getFcover();
                Intent intent = new Intent(getActivity(), NewHouseInfoActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("fid", datas.get(position).getFid());
                intent.putExtra("picName", picName);
                startActivity(intent);
            }
        });

    }

    public void downJsonObject(String url) {
        //2. 实例化请求对象

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        // TODO 请求成功
                        try {
                            total = Integer.parseInt(response.getString("total"));
                            rn = response.getInt("rn");
                            textCount.setText("共有" + total + "个楼盘");
                            JSONArray array = response.getJSONArray("data");


                            parseJson(array);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                if (getActivity() != null) {

                    Toast.makeText(getActivity(), "请求出错", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //3. 将请求添加到Volley的请求对列中
        VolleyUtils.getQueue(getActivity()).add(request);


    }


    private void parseJson(JSONArray array) {

        TypeToken<List<NewHouseEntity>> token = new TypeToken<List<NewHouseEntity>>() {
        };
        //开始解析
        list = gson.fromJson(array.toString(), token.getType());
        List bookMarks;
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = null;
            bookMarks = new ArrayList();
            try {
                object = array.getJSONObject(i);
                JSONArray arr = object.getJSONArray("bookmark");
                Debug.startDebug(arr.length() + "json" + "长度");
                BookMark bookMark;
                for (int j = 0; j < arr.length(); j++) {
                    JSONObject object1 = arr.getJSONObject(j);
                    String tag = object1.getString("tag");
                    int type = object1.getInt("type");
                    bookMark = new BookMark();
                    bookMark.setTag(tag);
                    bookMark.setType(type);
                    bookMarks.add(bookMark);
                }
                list.get(i).setBookmark(bookMarks);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        datas.addAll(list);
        //将加载完的数据列表传递地图列表
        Fragment2.datas.addAll(list);
        Fragment2.total = total;
        animation.stop();
        waitImageViewID.setVisibility(View.GONE);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        List<String> list = null;

        switch (v.getId()) {
            case R.id.area:
                list = new ArrayList();
                list.add("不限");
                list.add("朝阳");
                list.add("大兴");
                list.add("昌平");
                list.add("房山");
                break;
            case R.id.category:

                list = new ArrayList();
                list.add("不限");
                list.add("住房");
                list.add("别墅");
                list.add("建筑综合体");
                list.add("写字楼");
                list.add("商铺");
                list.add("自住型商品房");
                list.add("限价房");
                break;
            case R.id.price:
                list = new ArrayList();
                list.add("不限");
                list.add("1万以下");
                list.add("1-2万");
                list.add("2-3万");
                list.add("3-4万");
                list.add("4万以上");
                break;
            case R.id.more:
                list = new ArrayList();
                list.add("不限");
                list.add("1万以下");
                list.add("1-2万");
                list.add("2-3万");
                list.add("3-4万");
                list.add("4万以上");
                break;

        }
        if (win != null && win.isShowing()) {
            win.dismiss();
        } else {

            initPopupWindow(list);
        }
    }

    //显示pipupwindow
    public void initPopupWindow(List<String> list) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.popuewindow_layout, null);
        ListView listView = (ListView) view.findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);

        listView.setAdapter(adapter);

        win = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, 300);
        win.setContentView(view);
        win.showAsDropDown(areaB);
        win.setBackgroundDrawable(new BitmapDrawable());
        win.setOutsideTouchable(true);

    }

}
