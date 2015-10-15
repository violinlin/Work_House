package com.whl.hp.work_house.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbsAdapter<T> extends BaseAdapter {
	
	private Context context; //上下文对象
	private int layoutResId; //item布局资源
	private List<T> datas; //数据源
	
	public AbsAdapter(Context context, int layoutResId, List<T> datas) {
		this.context = context;
		this.layoutResId = layoutResId;
		this.datas = datas;
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vHolder=null;
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(layoutResId, parent,false);
			vHolder=new ViewHolder(convertView);
			
			convertView.setTag(vHolder);
		}else{
			vHolder=(ViewHolder) convertView.getTag();
		}
		
		bindView(vHolder,datas.get(position)); //将数据显示到item布局中
		
		return convertView;
	}
	
	public abstract void bindView(ViewHolder vHolder,T data);

	public static class ViewHolder{
		private Map<Integer,View> cacheViews;
		private View itemView;
		
		public ViewHolder(View itemView){
			this.itemView=itemView;
			cacheViews=new HashMap<Integer,View>();
		}
		
		public View getView(int id){ //查找指定id的item中子控件
			View v=cacheViews.get(id);
			if(v==null){
				v=itemView.findViewById(id);
				if(v!=null){
					cacheViews.put(id, v);
				}
			}
			
			return v;
		}
	}
	
}
