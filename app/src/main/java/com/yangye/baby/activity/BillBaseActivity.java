package com.yangye.baby.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.yangye.baby.R;
import com.yangye.baby.adapter.BillAdapter;
import com.yangye.baby.common.CommonTitleHelper;
import com.yangye.baby.support.bean.CommonBean;
import com.yangye.baby.support.bean.ExpendsBean;
import com.yangye.baby.support.bean.IncomeBean;

import java.util.ArrayList;

/**
 * 帐单列表基类
 * @description：
 * @author ldm
 * @date 2015-7-27 下午5:16:42
 */
public abstract class BillBaseActivity extends Activity {
	private CommonTitleHelper mTitleHelper;
	protected ListView bill_lv;
	protected BillAdapter mAdapter;
	protected ArrayList<IncomeBean> incomeList;
	protected ArrayList<ExpendsBean> expendsList;
	private View emptyView;
	protected int type;
	protected int dataType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bill_list);
		mTitleHelper = new CommonTitleHelper(this,-1,R.string.daily_amount, R.string.record);
		bill_lv = (ListView) findViewById(R.id.bill_listview);
		emptyView = findViewById(R.id.no_data_view);
		initListeners();
        incomeList = getIncomeData();
        expendsList = getExpendsData();
		dataType = getDataType();
		type = getType();
        Log.i("ybf","喝奶条数"+incomeList.size());
        Log.i("ybf","排出条数"+expendsList.size());
		if (incomeList.size() > 0 ||expendsList.size()>0 ) {
            Log.i("ybf","ok");
			bill_lv.setVisibility(View.VISIBLE);
			emptyView.setVisibility(View.GONE);
			mAdapter = new BillAdapter(this, incomeList,expendsList, type, dataType);
			bill_lv.setAdapter(mAdapter);
		}
		else {
			bill_lv.setVisibility(View.GONE);
			emptyView.setVisibility(View.VISIBLE);
		}
	}

	protected abstract int getDataType();

	private void initListeners() {
		mTitleHelper.setOnLeftClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		bill_lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				onitemClick(position);
			}
		});
	}

	protected abstract ArrayList<IncomeBean> getIncomeData();
	protected abstract ArrayList<ExpendsBean> getExpendsData();

	protected abstract void onitemClick(int postion);

	protected abstract int getType();
}
