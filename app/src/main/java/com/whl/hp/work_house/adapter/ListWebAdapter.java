package com.whl.hp.work_house.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.whl.hp.work_house.R;
import com.whl.hp.work_house.been.ListWebEntitys;
import com.whl.hp.work_house.tool.NUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp-whl on 2015/9/24.
 */
public class ListWebAdapter extends BaseAdapter {
    Context context;
    private List<ListWebEntitys>list;



    public ListWebAdapter(Context context){
        this.context=context;
        list=new ArrayList();

    }

    public void add(List<ListWebEntitys>l){
        list.addAll(l);

    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


//        if (convertView==null){
            if (list.get(position).getType().equals("0")){
                convertView=LayoutInflater.from(context).inflate(R.layout.listweb0_layout,null);
                final ImageView imageView= (ImageView) convertView.findViewById(R.id.webIV);
                TextView webTitle= (TextView) convertView.findViewById(R.id.webTitle);
                TextView webcomment= (TextView) convertView.findViewById(R.id.webcomment);
                TextView websummary= (TextView) convertView.findViewById(R.id.websummary);
                webTitle.setText(list.get(position).getTitle());
                webcomment.setText(list.get(position).getCommentcount()+"评");
                websummary.setText(list.get(position).getSummary());
                NUtils.get(NUtils.TYPE_IMG,list.get(position).getThumbnail(),new NUtils.AbsCallback(){
                    @Override
                    public void response(String url, byte[] bytes) {
                        super.response(url, bytes);
                        Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                        imageView.setImageBitmap(bitmap);
                    }
                });


            }else if (list.get(position).getType().equals("1")){
                convertView=LayoutInflater.from(context).inflate(R.layout.listweb1_layout,null);
               final ImageView imageView= (ImageView) convertView.findViewById(R.id.webIV);
                TextView webTitle= (TextView) convertView.findViewById(R.id.webTitle);
                TextView webcomment= (TextView) convertView.findViewById(R.id.webcomment);
                webTitle.setText(list.get(position).getTitle());
                webcomment.setText(list.get(position).getCommentcount()+"评");
                NUtils.get(NUtils.TYPE_IMG, list.get(position).getGroupthumbnail(), new NUtils.AbsCallback() {
                    @Override
                    public void response(String url, byte[] bytes) {
                        super.response(url, bytes);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        imageView.setImageBitmap(bitmap);
                    }
                });

            }



//        }
        return convertView;
    }


}
