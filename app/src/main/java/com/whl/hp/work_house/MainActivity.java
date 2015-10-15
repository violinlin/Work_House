package com.whl.hp.work_house;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;

import com.whl.hp.work_house.tool.Debug;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity {
    public static final int REAUEST_CODE = 1;
    //当前显示城市
   public  static String cityID = "1";
    List<Fragment> fragmentList;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        getSupportActionBar().hide();
        fragmentList=new ArrayList();
        fragmentList.add(new NewHouseFragment());
        fragmentList.add(new DiscoverFragment());
        fragmentList.add(new MessageFragment());
        fragmentList.add(new MineFragment());
        changeFragment(fragmentList.get(0));
    }

    public void tabClick(View view){
        switch (view.getId()){
            case R.id.newHouseID:
                changeFragment(fragmentList.get(0));
                break ;
            case R.id.discoverID:
                changeFragment(fragmentList.get(1));
                break ;
            case R.id.messageID:
                changeFragment(fragmentList.get(2));
                break ;
            case R.id.mineID:
                changeFragment(fragmentList.get(3));
                break ;

        }

    }

    public void changeFragment(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frameLayoutID, fragment).commit();

    }
    public void openCityList(View view) {
        Intent intent = new Intent(this, CityListActivity.class);
        startActivityForResult(intent, REAUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REAUEST_CODE && resultCode == 2) {
            String currentCity = data.getStringExtra("city");
            cityID = data.getStringExtra("cityID");
            NewHouseFragment h= (NewHouseFragment) fragmentList.get(0);
            h.currentButton.setText(currentCity);
            h.reLoad(cityID);

        }
    }


}
