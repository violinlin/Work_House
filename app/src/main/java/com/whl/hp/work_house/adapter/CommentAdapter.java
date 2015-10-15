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
import com.whl.hp.work_house.been.CommentEntity;
import com.whl.hp.work_house.tool.NUtils;

import java.util.List;

/**
 * Created by hp-whl on 2015/9/26.
 */
public class CommentAdapter extends BaseAdapter{
    Context context;
    List<CommentEntity>list;

    public CommentAdapter(Context context,List<CommentEntity>list){
        this.context=context;
        this.list=list;

    }
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
        final ViewHolder holder;
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.comment_item_layout,null);
            holder=new ViewHolder();
            holder.commentTV= (TextView) convertView.findViewById(R.id.commentTV);
            holder.headIV= (ImageView) convertView.findViewById(R.id.headIV);
            holder.nickTV= (TextView) convertView.findViewById(R.id.nickTV);
            holder.timeTV= (TextView) convertView.findViewById(R.id.timeTV);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.nickTV.setText(list.get(position).getNick());
        holder.commentTV.setText(list.get(position).getContent());
        holder.timeTV.setText(list.get(position).getRegion()+"   "+list.get(position).getTime());
        NUtils.get(NUtils.TYPE_IMG,list.get(position).getHead(),new NUtils.AbsCallback(){
            @Override
            public void response(String url, byte[] bytes) {
                super.response(url, bytes);
                Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                holder.headIV.setImageBitmap(bitmap);
            }
        });

        return convertView;
    }

    class ViewHolder{
        TextView nickTV,timeTV,commentTV;
        ImageView headIV;

    }
}
