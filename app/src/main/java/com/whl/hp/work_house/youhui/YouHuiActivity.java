package com.whl.hp.work_house.youhui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.whl.hp.work_house.R;
import com.whl.hp.work_house.adapter.AbsAdapter;
import com.whl.hp.work_house.newhouse_activity.NewHouseInfoActivity;
import com.whl.hp.work_house.tool.Config;
import com.whl.hp.work_house.tool.ImageUtils;
import com.whl.hp.work_house.tool.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp-whl on 2015/9/30.
 */
public class YouHuiActivity extends Activity {
    ListView listView;
    PopupWindow win;
    ImageView waitImageViewID;
    AnimationDrawable animation;
    String url;
    boolean isBootom = false;
    List<YouHuiEntitys> datas;
    AbsAdapter<YouHuiEntitys> adapter;
    int pageCount = 1;
    Button area;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youhui_layout);
        initListView();

        waitImageViewID = (ImageView) findViewById(R.id.waitImageViewID);
        area= (Button) findViewById(R.id.area);
        datas = new ArrayList();
        url = String.format(Config.DISCOUNT_SEAL, pageCount);
        animation = (AnimationDrawable) waitImageViewID.getBackground();
        animation.start();
        initAdapter();
        loadData();

    }

    private void initListView() {
        listView = (ListView) findViewById(R.id.listView);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                String url = String.format(Config.NEW_HOUSE_INFO, datas.get(position).getHid());
                String picName = datas.get(position).getCover();
                Intent intent = new Intent(YouHuiActivity.this, NewHouseInfoActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("fid",datas.get(position).getHid());
                intent.putExtra("picName", picName);
                startActivity(intent);


            }
        });


        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if (isBootom && scrollState == SCROLL_STATE_IDLE) {
                    Toast.makeText(YouHuiActivity.this, "加载新的数据", Toast.LENGTH_SHORT).show();
                    pageCount++;
                    if (pageCount < 10) {
                        url = String.format(Config.DISCOUNT_SEAL, pageCount);
                        loadData();
                        adapter.notifyDataSetChanged();
                    }

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                isBootom = (firstVisibleItem + visibleItemCount == totalItemCount);

            }
        });
    }

    private void loadData() {
        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray array = response.getJSONArray("data");
                    TypeToken<List<YouHuiEntitys>> token = new TypeToken<List<YouHuiEntitys>>() {
                    };
                    Gson gson = new Gson();
                    List<YouHuiEntitys> list
                            = gson.fromJson(array.toString(), token.getType());
                    datas.addAll(list);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                animation.stop();
                waitImageViewID.setVisibility(View.GONE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(YouHuiActivity.this, "请求出错", Toast.LENGTH_SHORT).show();
            }
        });


        VolleyUtils.getQueue(this).add(request);

    }

    public void initAdapter() {
        adapter = new AbsAdapter<YouHuiEntitys>(this, R.layout.youhui_item_layout, datas) {
            @Override
            public void bindView(ViewHolder vHolder, final YouHuiEntitys data) {
                TextView title = (TextView) vHolder.getView(R.id.title);
                ImageView imageView = (ImageView) vHolder.getView(R.id.imageView);
                TextView address = (TextView) vHolder.getView(R.id.address);
                TextView price = (TextView) vHolder.getView(R.id.price);
                TextView youhui = (TextView) vHolder.getView(R.id.youhui);
                Button youhuiBtn = (Button) vHolder.getView(R.id.youhuiID);
                Button telbtn = (Button) vHolder.getView(R.id.telID);

                youhuiBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(YouHuiActivity.this, "获取优惠", Toast.LENGTH_SHORT).show();

                    }
                });
                telbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(YouHuiActivity.this);
                        builder.setTitle("确定拨打电话")
                                .setMessage(data.getTel())
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Uri uri = Uri.parse("tel:" + data.getTel());
                                        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                                        startActivity(intent);

                                    }
                                })
                                .setNegativeButton("取消", null)
                                .show()
                        ;

                    }
                });

                title.setText(data.getName());
                address.setText(data.getAddress());
                price.setText(data.getPrice_pre() + data.getPrice_value() + data.getPrice_unit());
                youhui.setText(data.getDiscount());
                Bitmap bitmap = ImageUtils.getImg(data.getCover());
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    VolleyUtils.getLoader(YouHuiActivity.this).get(data.getCover(),
                            ImageLoader.getImageListener(imageView, R.drawable.ershou, android.R.drawable.ic_menu_delete));
                }

            }
        };
        listView.setAdapter(adapter);

    }

    public void cancel(View view) {
        finish();

    }

    public void clickBtn(View view) {


        List<String> list = null;

        switch (view.getId()) {
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
        View view = LayoutInflater.from(YouHuiActivity.this).inflate(R.layout.popuewindow_layout, null);
        ListView listView = (ListView) view.findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(YouHuiActivity.this, android.R.layout.simple_list_item_1, list);

        listView.setAdapter(adapter);

        win = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, 300);
        win.setContentView(view);
        win.showAsDropDown(area);
        win.setBackgroundDrawable(new BitmapDrawable());
        win.setOutsideTouchable(true);

    }
}
