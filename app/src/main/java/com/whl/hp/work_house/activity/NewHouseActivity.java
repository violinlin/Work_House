package com.whl.hp.work_house.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.whl.hp.work_house.R;

/**
 * Created by hp-whl on 2015/9/28.
 */
public class NewHouseActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_house_layout);
    }

    //切换到地图视图
    public void mapClick(View view ){

    }

    //返回事件的处理
    public void cancel(View view ){
        finish();
    }
}
