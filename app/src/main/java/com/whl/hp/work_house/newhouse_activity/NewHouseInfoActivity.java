package com.whl.hp.work_house.newhouse_activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Debug;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.whl.hp.work_house.R;
import com.whl.hp.work_house.tool.ImageUtils;
import com.whl.hp.work_house.tool.VolleyUtils;

/**
 * Created by hp-whl on 2015/9/29.
 */
public class NewHouseInfoActivity extends Activity {
    FragmentManager manager;
    ImageView imageView;
    String url;
    String picName;
    Bitmap bitmap;
    String fid;
    Fragment fragment1, fragment2;
    Bundle bundle;
    Button commentBtn,button;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_house_info_layout);
        url = getIntent().getStringExtra("url");
        fid= getIntent().getStringExtra("fid");
        commentBtn= (Button) findViewById(R.id.commentBtn);
        button= (Button) findViewById(R.id.button);
        picName = getIntent().getStringExtra("picName");
        imageView = (ImageView) findViewById(R.id.imageView);

        fragment1 = new LoupanInfoFragment();
        bundle=new Bundle();
        bundle.putString("url", url);

        fragment1.setArguments(bundle);

        bitmap = ImageUtils.getImg(picName);
        if (bitmap != null) {
            imageView.setImageBitmap(ImageUtils.getImg(picName));
        }

        manager = getFragmentManager();
        manager.beginTransaction().replace(R.id.framelayout_info, fragment1).commit();
    }


    public void btnClock(View view) {
        switch (view.getId()) {
            case R.id.loupanInfoBtn:
                bundle=new Bundle();
                bundle.putString("url",url);
                button.setVisibility(View.GONE);
                fragment1 = new LoupanInfoFragment();
                fragment1.setArguments(bundle);
                manager.beginTransaction().replace(R.id.framelayout_info, fragment1).commit();
                break;
            case R.id.commentBtn:
                button.setVisibility(View.VISIBLE);
                bundle=new Bundle();
                bundle.putString("fid",fid);
                fragment2 = new CommentFragment();
                fragment2.setArguments(bundle);
                manager.beginTransaction().replace(R.id.framelayout_info, fragment2).commit();
                break;
            case R.id.back:
                finish();
                break;
            case R.id.favorite:
                break;
            case R.id.share:
                break;
            case R.id.num:
                break;

        }

    }
}
