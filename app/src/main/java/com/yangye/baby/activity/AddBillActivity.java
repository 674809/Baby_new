package com.yangye.baby.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.yangye.baby.R;
import com.yangye.baby.common.CommonTitleHelper;
import com.yangye.baby.common.Constant;
import com.yangye.baby.support.SpUtil.SharedPreferencesUtils;
import com.yangye.baby.support.bean.IncomeBean;
import com.yangye.baby.support.db.DBHelper;
import com.yangye.baby.support.email.SendEmailTool;
import com.yangye.baby.support.tool.DataTools;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class AddBillActivity extends Activity implements OnClickListener, CompoundButton.OnCheckedChangeListener {
    /* 添加支出或收入切换* */
    private RadioGroup billRg;
    private RadioButton expendRb; //排出
    private RadioButton incomeRb; //吃奶
    /**************支出部分*****************/
    private CheckBox typ1;
    private CheckBox typ2;
    private View expendView;
    private String[] expendTitle;
    /*************收入部分************************************/
    private View incomeView;
    private EditText mMamilk;//母乳
    private EditText mMilke; //牛奶
    /*******共用部分*****************************/
    private File billFile;
    private DBHelper mDbHelper;
    private EditText mRemarkEdt;//备注
    private Button mSaveBtn;
    private String[] saveData;
    private ArrayList<ArrayList<String>> bill2List;
    private int billType = Constant.EXPEND_TYPE;// 2为排出，1吃奶，默认是支出
    private CommonTitleHelper mtTitleHelper;
    /**
     * 用于计算当天的输入的数据合计
     */
    private int year = DataTools.getInstance().getNowYear();
    private int month = DataTools.getInstance().getNowMonth();
    private int day = DataTools.getInstance().getDay();
    private long time = DataTools.getInstance().getNowSeconds();
    private ArrayList<String> calArray = new ArrayList<String>();
    private IncomeBean incomeBean;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private int niao = 0;
    private int shi = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_one_bill);
        findViewsById();
        initDetailData();
        initListeners();
        mDbHelper = new DBHelper(this);
        mDbHelper.open();
        bill2List = new ArrayList<ArrayList<String>>();
    }

    /**
     * 编辑帐单时初始化数据
     *
     * @description：
     * @author ldm
     * @date 2015-8-11 下午5:18:18
     */
    private void initDetailData() {
        if (null != getIntent().getSerializableExtra(Constant.EXPEND_BEAN)) {
            billType = Constant.EXPEND_TYPE;
            expendView.setVisibility(View.VISIBLE);
            incomeView.setVisibility(View.GONE);

        }
        if (null != getIntent().getSerializableExtra(Constant.INCOME_BEAN)) {
            billType = Constant.INCOME_TYPE;
            expendView.setVisibility(View.GONE);
            incomeView.setVisibility(View.VISIBLE);
            incomeBean = (IncomeBean) getIntent().getSerializableExtra(Constant.INCOME_BEAN);
            mMamilk.setText(incomeBean.getMamilk());
            mMilke.setText(incomeBean.getMilk());
            mRemarkEdt.setText(incomeBean.getOther());
        }
    }


    private void findViewsById() {
        /****************共用部分***********************************/
        mtTitleHelper = new CommonTitleHelper(this, R.string.add_info);
        billRg = (RadioGroup) findViewById(R.id.bill_rg);
        expendRb = (RadioButton) findViewById(R.id.add_one_expend);
        incomeRb = (RadioButton) findViewById(R.id.add_one_income);
        mRemarkEdt = (EditText) findViewById(R.id.bill_remark_edt);
        mSaveBtn = (Button) findViewById(R.id.family_bill_save);
        /*****************支出部分*********************************/
        expendView = findViewById(R.id.add_expend_layout);
        typ1 = findViewById(R.id.type1);
        typ2 = findViewById(R.id.type2);
        typ1.setOnCheckedChangeListener(this);
        typ2.setOnCheckedChangeListener(this);


        /*****************收入部分**************************************/
        mMamilk = (EditText) findViewById(R.id.income_salary_edt); //mamilk
        mMilke = (EditText) findViewById(R.id.income_salary_milk); //milk

        incomeView = findViewById(R.id.add_income_layout);

    }

    private void initListeners() {
        mSaveBtn.setOnClickListener(this);
        mtTitleHelper.setOnLeftClickListener(this);
        billRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.add_one_expend:
                        billType = Constant.EXPEND_TYPE;
                        expendView.setVisibility(View.VISIBLE);
                        incomeView.setVisibility(View.GONE);
                        break;
                    case R.id.add_one_income:
                        billType = Constant.INCOME_TYPE;
                        expendView.setVisibility(View.GONE);
                        incomeView.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left:
                finish();
                break;
            case R.id.family_bill_save:
                calArray.clear();
                if (billType == Constant.EXPEND_TYPE) {
                    calArray.add(String.valueOf(shi));
                    calArray.add(String.valueOf(niao));
                    calTotal(calArray);
                    saveExpendsData();
                    finish();
                } else if (billType == Constant.INCOME_TYPE) {
                    calArray.add(dealSaveData(mMamilk));
                    calArray.add(dealSaveData(mMilke));
                    calTotal(calArray);
                    saveIncomeData();
                    finish();
                }

                break;
        }
    }

    private ArrayList<ArrayList<String>> getBillData() {
        bill2List.clear();
        Cursor mCrusor = mDbHelper.exeSql("select * from expend");
        while (mCrusor.moveToNext()) {// { "shi", "niao", "合计" };
            ArrayList<String> beanList = new ArrayList<String>();
            beanList.add(mCrusor.getString(3));
            beanList.add(mCrusor.getString(4));
            bill2List.add(beanList);
        }
        mCrusor.close();
        return bill2List;
    }


    private boolean canSave(String[] data) {
        boolean isOk = false;
        for (int i = 0; i < data.length; i++) {
            if (i > 0 && i < data.length) {
                if (!TextUtils.isEmpty(data[i])) {
                    isOk = true;
                }
            }
        }
        return isOk;
    }

    /**
     * 排出
     *
     * @description：
     * @author ldm
     * @date 2015-7-17 上午9:54:20
     */
    @SuppressLint("SimpleDateFormat")
    private void saveExpendsData() {
        saveData = new String[]{new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()),
                String.valueOf(shi), String.valueOf(niao), mRemarkEdt.getText().toString().trim()};
        if (shi != 0 || niao != 0) {
            ContentValues values = new ContentValues();
            // year month time food live use clothes traffic doctor laiwang eduation travel other remark total
            values.put("year", saveData[0]);
            values.put("month", month);
            values.put("time", sdf.format(new Date()));
            Log.i("ybf", "year =" + year + "\n" + "month=" + month + "\n" + "time=" + sdf.format(new Date()));
            values.put("shi", shi);
            values.put("niao", niao);
            values.put("other", dealSaveData(mRemarkEdt));
            values.put("total", calTotal(calArray));
            long insert = mDbHelper.insert("expend", values);
            Log.i("ybf", "insert expend =" + insert);
            final String body = "日期：" + saveData[0] + "\n" + "干:" + shi + "\n" + "湿：" + niao;
            try {
                int sms = (int) SharedPreferencesUtils.getParam(this, "sms", 1);
                Log.i("ybf", "sms =" + sms);
                if (sms == 1) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            initEmail(body);
                        }
                    }).start();
                    //SMSMethod.getInstance(this).SendMessage("13991394862",saveData[0]+"奶粉量:"+mMilke.getText().toString().trim());
                }
            } catch (Exception e) {
            }
        } else {
            Toast.makeText(this, "请至少输入一项数据！", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 保存喝奶数据
     *
     * @description：
     * @author ldm
     * @date 2015-7-17 上午9:54:47
     */
    private void saveIncomeData() {
        saveData = new String[]{new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()),
                mMamilk.getText().toString().trim(), mMilke.getText().toString().trim(),
                mRemarkEdt.getText().toString().trim()};
        if (canSave(saveData)) {
            ContentValues values = new ContentValues();
            // year monty time salary prize manager invest other remark total
            values.put("year", saveData[0]);
            values.put("month", month);
            values.put("time", sdf.format(new Date()));
            values.put("mamilk", dealSaveData(mMamilk));
            values.put("milk", dealSaveData(mMilke));
            values.put("other", dealSaveData(mRemarkEdt));
            values.put("total", calTotal(calArray));
            if (incomeBean != null) {
                String sql = "delete from income where time= " + "'" + String.valueOf(incomeBean.getTime()) + "'";
                mDbHelper.delete(sql);
            }
            long insert = mDbHelper.insert("income", values);
            Log.i("ybf", "insert income =" + insert);
            Log.i("ybf", "SMSMethod");
            final String body = "日期：" + saveData[0] + "\n" + "奶粉量:" + dealSaveData(mMilke) + "\n" + "母乳量：" + dealSaveData(mMamilk);
            try {
                int sms = (int) SharedPreferencesUtils.getParam(this, "sms", 1);
                Log.i("ybf", "sms =" + sms);
                if (sms == 1) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            initEmail(body);
                        }
                    }).start();
                    //SMSMethod.getInstance(this).SendMessage("13991394862",saveData[0]+"奶粉量:"+mMilke.getText().toString().trim());
                }
            } catch (Exception e) {
            }

        } else {
            Toast.makeText(this, "请填写任意一项内容", Toast.LENGTH_SHORT).show();
        }

    }

    private void initEmail(String body) {
        SendEmailTool.getInstance().SendEmailConfigure("smtp.163.com", 25, "xxx@163.com", "xxx");
        SendEmailTool.getInstance().sendEmail("", "统计", body, null);
    }


    /**
     * 存数据处理，如果没有填写该项，则默认存到数据库为0,方便计算
     *
     * @description：
     * @author ldm
     * @date 2015-7-22 下午3:16:57
     */
    private String dealSaveData(EditText edt) {
        String data = "0";
        if (!TextUtils.isEmpty(edt.getText().toString().trim())) {
            data = edt.getText().toString().trim();
        }
        return data;
    }

    /**
     * 计算当天的数据和
     *
     * @description：
     * @author ldm
     * @date 2015-7-22 下午3:23:42
     */
    private double calTotal(ArrayList<String> dataList) {
        double total = 0;
        for (String string : dataList) {
            total += Double.valueOf(string);
        }
        return total;
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.type1:
                Log.i("ybf", "niao");
                niao = 1;
                break;
            case R.id.type2:
                shi = 1;
                Log.i("ybf", "shi");
                break;
        }
    }
}
