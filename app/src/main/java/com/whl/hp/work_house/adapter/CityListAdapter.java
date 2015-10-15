package com.whl.hp.work_house.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.whl.hp.work_house.R;
import com.whl.hp.work_house.been.CityEntitys;

import java.util.List;

/**
 * Created by hp-whl on 2015/9/23.
 */
public class CityListAdapter extends BaseAdapter{
    private Context context;
    private List<CityEntitys>list;
    public CityListAdapter(Context context, List<CityEntitys> list){
        this.context=context;
        this.list=list;

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
    public int getItemViewType(int position) {
        return list.get(position).getType();
    }

    @Override
    public int getViewTypeCount() {
        return CityEntitys.getTypeCount();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean isEnabled(int position) {
        if (list.get(position).getType()==CityEntitys.TYPE_LABEL){
            return false;
        }
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            holder=new ViewHolder();
            if (list.get(position).getType()==CityEntitys.TYPE_LABEL){
                convertView= LayoutInflater.from(context).inflate(R.layout.label_layout,null);
            }else {
                convertView=LayoutInflater.from(context).inflate(R.layout.city_layout,null);
            }
            holder.textView= (TextView) convertView.findViewById(R.id.cityName);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(list.get(position).getCityname());

       return convertView;
    }

    class ViewHolder {
        TextView textView;
    }

}
