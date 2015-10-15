package com.whl.hp.work_house.newhouse_activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.whl.hp.work_house.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by hp-whl on 2015/9/28.
 */
public class NewHouseActivity extends Activity {
    FragmentManager manager;
    boolean isMap = false;
    String cityID;
    String cityName;
    String Name;
    TextView titleName;
    Fragment fragment1,fragment2;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_house_layout);
        titleName= (TextView) findViewById(R.id.titleTV);
        cityID=getIntent().getStringExtra("cityID");
        Name=getIntent().getStringExtra("Name");
        titleName.setText(Name);
        cityName=getIntent().getStringExtra("cityName");
        Fragment2.cName=cityName;
        initFragment();
        manager = getFragmentManager();
        manager.beginTransaction().replace
                (R.id.frameLayout, fragment1)
                .commit();

    }
    public void initFragment(){
        fragment1 = new Fragment1();
        Bundle bundle=new Bundle();
        bundle.putString("cityID", cityID);
        bundle.putString("titleName",Name);
        fragment1.setArguments(bundle);
        fragment2=new Fragment2();
    }
    //切换到地图视图
    public void mapClick(View view) {
        if (!isMap) {
            manager.beginTransaction().replace(R.id.frameLayout, fragment2).commit();
        }else {
            manager.beginTransaction().replace(R.id.frameLayout, fragment1).commit();
        }
        isMap=!isMap;
    }
    //返回事件的处理
    public void cancel(View view) {
        finish();
    }
}
