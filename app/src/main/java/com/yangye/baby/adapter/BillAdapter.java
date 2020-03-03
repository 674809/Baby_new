package com.yangye.baby.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yangye.baby.R;
import com.yangye.baby.support.bean.ExpendsBean;
import com.yangye.baby.support.bean.IncomeBean;

import java.util.ArrayList;

/**
 * 帐单列表数据适配器
 * @description：
 * @author ldm
 * @date 2015-7-16 上午9:40:54
 */
public class BillAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<IncomeBean> incomeList; //喝奶
	private ArrayList<ExpendsBean> expendList; //喝奶
	private int type;// 1 喝奶，2排出，默认是支出
	private int dataType;// 数据类型，1年2月3日

	public BillAdapter(Context context, ArrayList<IncomeBean> incomeList,
                       ArrayList<ExpendsBean> expendsList, int type, int dataType) {
		this.context = context;
		this.incomeList = incomeList;
		this.expendList = expendsList;
		this.type = type;
		this.dataType = dataType;
		Log.i("ybf","type ="+type);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
        if(type ==1){
            return incomeList.size();
        }else {
            return expendList.size();
        }

	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
        if(type ==1){
        return incomeList.get(position);
    }else {
        return expendList.get(position);
    }

	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.bill_list_item, null);
			holder.timeTv = (TextView) convertView.findViewById(R.id.bill_item_time); //时间
			holder.typeTv = (TextView) convertView.findViewById(R.id.bill_item_type);//类型
			holder.contentTv = (TextView) convertView.findViewById(R.id.bill_item_number);//数量
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Log.i("ybf","getView type="+type);
        if(type ==1 ){
            IncomeBean incomeBean = incomeList.get(position);
            holder.timeTv.setText(incomeBean.getYear());
            holder.typeTv.setText(Integer.valueOf(incomeBean.getMamilk()).intValue() >0 ?  "母乳":"牛奶");
            holder.contentTv.setText( incomeBean.getTotal());
        }else {
            ExpendsBean expendsBean = expendList.get(position);
			String type = "";
            holder.timeTv.setText(expendsBean.getYear());
            Log.i("ybf","getShi="+expendsBean.getShi());
            Log.i("ybf","getNiao="+expendsBean.getNiao());
			if(expendsBean.getNiao() ==0 && expendsBean.getShi()==1){
				type ="粪便";
			}else if(expendsBean.getNiao() ==1 && expendsBean.getShi()==0){
				type ="尿";
			}else if(expendsBean.getNiao() ==1 && expendsBean.getShi()==1 ){
				type ="粪便 和 尿";
			}
            holder.typeTv.setText(type);
            holder.contentTv.setText( expendsBean.getTotal());
        }

		return convertView;
	}

	static class ViewHolder {
		TextView timeTv, typeTv, contentTv;// 分别指点时间，类型及对应的金额
	}
}
