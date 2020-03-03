package com.yangye.baby.activity;

import android.os.Bundle;
import android.util.Log;

import com.yangye.baby.common.Constant;
import com.yangye.baby.support.bean.ExpendsBean;
import com.yangye.baby.support.bean.IncomeBean;
import com.yangye.baby.support.db.DBTool;
import com.yangye.baby.support.tool.DataTools;

import java.util.ArrayList;


/**
 *月度对应的每一天帐单数据列表
 * @description：
 * @author ldm
 * @date 2015-7-17 下午1:36:47
 */
public class DayActivity extends BillBaseActivity {
	private int type;
	private int month;
	private String findType="";
	private String findValue="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void onitemClick(int postion) {

	}


	@Override
	protected int getDataType() {
		// TODO Auto-generated method stub
		return 3;
	}

    @Override
    protected ArrayList<IncomeBean> getIncomeData() {
        type = getIntent().getIntExtra(Constant.BILL_TYPE, Constant.INCOME_TYPE);
        month = getIntent().getIntExtra(Constant.MONTH, DataTools.getInstance().getNowMonth());
        findType = getIntent().getStringExtra(Constant.FIND_TYPE);
        findValue = getIntent().getStringExtra(Constant.FIND_VALUE);
        incomeList = new DBTool(this).findAllIncome(findType, String.valueOf(findValue));
        Log.e("ybf","incomeList findType = "+findType);
        Log.e("ybf","income ListfindValue = "+findValue);
        Log.e("ybf","incomeList = "+incomeList.size());
        return incomeList;
    }

    @Override
    protected ArrayList<ExpendsBean> getExpendsData() {
        type = getIntent().getIntExtra(Constant.BILL_TYPE, Constant.EXPEND_TYPE);
        findType = getIntent().getStringExtra(Constant.FIND_TYPE);
        findValue = getIntent().getStringExtra(Constant.FIND_VALUE);
        month = getIntent().getIntExtra(Constant.MONTH, DataTools.getInstance().getNowMonth());
        expendsList = new DBTool(this).findAllExpend(findType, String.valueOf(findValue));
        Log.e("ybf","expendsList findType = "+findType);
        Log.e("ybf","expendsList findValue = "+findValue);
        Log.e("ybf","expendsList = "+expendsList.size());
        return expendsList;
    }

    @Override
	protected int getType() {
		// TODO Auto-generated method stub
		return type;
	}
}
