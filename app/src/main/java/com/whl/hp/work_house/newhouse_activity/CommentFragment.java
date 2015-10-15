package com.whl.hp.work_house.newhouse_activity;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.whl.hp.work_house.MainActivity;
import com.whl.hp.work_house.R;
import com.whl.hp.work_house.adapter.AbsAdapter;
import com.whl.hp.work_house.been.CommentEntity;
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
 * Created by hp-whl on 2015/9/29.
 */
public class CommentFragment extends Fragment {
    View view;
    ListView listView;
    AbsAdapter<CommentEntitys> adapter;
    List<CommentEntitys> datas;
    String url;
    String fid;
    String cityID;
    String allNum;
    Button commentBtn;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        fid = getArguments().getString("fid");
        commentBtn= (Button) getActivity().findViewById(R.id.commentBtn);
        commentBtn.setText("评论");
        if ("新房".equals(Fragment1.titleName)) {

            url = String.format(Config.NEW_HOUSE_COMMENT, fid);
        } else {
            url = String.format(Config.DISCOUNT_HOUSE_COMMENT, fid, "71");

        }
        loadData();

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.new_house_comment_fragment_layout, container, false);
        listView = (ListView) view.findViewById(R.id.listView);

        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                listView.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        datas = new ArrayList<>();
        adapter = new AbsAdapter<CommentEntitys>(getActivity(), R.layout.loupaninfo_comment_item_layout, datas) {
            @Override
            public void bindView(ViewHolder vHolder, CommentEntitys data) {
                TextView user = (TextView) vHolder.getView(R.id.user);
                TextView summary = (TextView) vHolder.getView(R.id.summary);
                TextView date = (TextView) vHolder.getView(R.id.date);
                user.setText(data.getUser());
                summary.setText("");
                if (data.getAdv() != "") {
                    summary.append("优点：" + data.getAdv() + "\n");
                }
                if (data.getDisadv() != "") {
                    summary.append("缺点：" + data.getDisadv() + "\n");
                }
                if (data.getSummary() != "") {
                    summary.append("总结:" + data.getSummary() + "\n");
                }
                date.setText(data.getDate());


            }
        };
        listView.setAdapter(adapter);


    }

    private void loadData() {
        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Debug.startDebug(response.toString());

                if (getActivity() != null) {

                    Toast.makeText(getActivity(), "获取成功", Toast.LENGTH_SHORT).show();
                }
                try {
                    JSONObject object = response.getJSONObject("data");
                    allNum=object.getString("allnum");
                    commentBtn.setText("评论("+allNum+")");
                    JSONArray array = object.getJSONArray("info");
                    Gson gson = new Gson();
                    TypeToken<List<CommentEntitys>> token = new TypeToken<List<CommentEntitys>>() {
                    };
                    List<CommentEntitys> list = gson.fromJson(array.toString(), token.getType());
                    datas.addAll(list);
                    Debug.startDebug(datas.size() + "评论的长度");
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (getActivity() != null) {

                    Toast.makeText(getActivity(), "获取失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

        VolleyUtils.mQueue.add(request);

    }
}
