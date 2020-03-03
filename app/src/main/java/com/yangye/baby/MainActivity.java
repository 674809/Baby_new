package com.yangye.baby;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yangye.baby.activity.AddBillActivity;
import com.yangye.baby.activity.DayActivity;
import com.yangye.baby.activity.MyDialog;
import com.yangye.baby.common.CommonTitleHelper;
import com.yangye.baby.common.Constant;
import com.yangye.baby.support.SpUtil.SharedPreferencesUtils;
import com.yangye.baby.support.db.DBTool;
import com.yangye.baby.support.permissioin.Permissions;
import com.yangye.baby.support.tool.DataTools;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;

public class MainActivity extends Activity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private CommonTitleHelper mTitleHelper;
    private String month;
    private TextView timeTv;// 时间展示
    private TextView incomeTv;// 吃奶
    private TextView expendTv1;// 排出1 尿 2 粪
    private TextView expendTv2;// 排出1 尿 2 粪
    private TextView right;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private TextView addBillTv;// 记一笔
    private TextView tv_anmen;// 记一笔
    private RadioButton chMonth;
    private RadioButton chDay;
    private RadioGroup radioGroup;
    private DatePicker chDate;
    private Button btEnter;
    private String find_type = "month";//查询条件默认为月
    private String find_value = DataTools.getInstance().getNowMonth() + "";//查询条值

    private RelativeLayout incomeDetailTv;// 查看收入详情
    private RelativeLayout expendDetailTv;// 查看支出详情

    private MyDialog myDialog;
    private int width;
    private int height;
    private int DiaWidth;
    private long lastClickTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        month = String.valueOf(DataTools.getInstance().getNowMonth());
        initView();
        Permissions.requestPermissionAll(this);


    }



    private void initView() {
        mTitleHelper = new CommonTitleHelper(this, -1, R.string.bill, R.string.main);
        timeTv = (TextView) findViewById(R.id.main_time_tv);
        incomeTv = (TextView) findViewById(R.id.main_income_content_tv);
        expendTv1 = (TextView) findViewById(R.id.main_expend_content_tv1);
        addBillTv = (TextView) findViewById(R.id.main_add_tv);
        incomeDetailTv = (RelativeLayout) findViewById(R.id.main_income);
        expendDetailTv = (RelativeLayout) findViewById(R.id.main_expend);
        tv_anmen = (TextView) findViewById(R.id.tv_anmen);
        right = findViewById(R.id.right);
        initListeners();
        initDate();
    }

    private void initListeners() {
        incomeDetailTv.setOnClickListener(this);
        expendDetailTv.setOnClickListener(this);
        addBillTv.setOnClickListener(this);
        tv_anmen.setOnClickListener(this);
        right.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUiDatas();
    }

    private void setUiDatas() {
        timeTv.setText(sdf.format(new Date()));
        incomeTv.setText(String.valueOf(new DBTool(this).calIncomeBill("month", month)) + "ml");
        expendTv1.setText(String.valueOf(new DBTool(this).calExpendBill("month", month)) + "次");
    }

    @Override
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.main_income:// 本月收入帐单
                bundle.clear();
                bundle.putInt(Constant.BILL_TYPE, Constant.INCOME_TYPE);
                bundle.putInt(Constant.MONTH, DataTools.getInstance().getNowMonth());
                bundle.putString(Constant.FIND_TYPE, find_type);
                bundle.putString(Constant.FIND_VALUE, find_value);
                Intent mIntent = new Intent(this, DayActivity.class);
                mIntent.putExtras(bundle);
                startActivity(mIntent);
                break;
            case R.id.main_expend:// 本月支出帐单
                bundle.clear();
                bundle.putInt(Constant.BILL_TYPE, Constant.EXPEND_TYPE);
                bundle.putInt(Constant.MONTH, DataTools.getInstance().getNowMonth());
                bundle.putString(Constant.FIND_TYPE, find_type); //yue ri
                bundle.putString(Constant.FIND_VALUE, find_value);
                Intent tet = new Intent(this, DayActivity.class);
                tet.putExtras(bundle);
                startActivity(tet);
                break;
            case R.id.main_add_tv:// 记一笔
                startActivity(new Intent(this, AddBillActivity.class));
                break;
            case R.id.right:// 跳转到总帐单中
                showDialog();
                break;
            case R.id.tv_anmen:
                onDisplaySettingButton();
                break;


        }
    }


    public void showDialog() {
        myDialog = new MyDialog(MainActivity.this);
        myDialog.setCancelable(true);//禁止Back 键
        View view = LayoutInflater.from(MainActivity.this)
                .inflate(R.layout.dialog_layout, null);
        myDialog.setLayoutView(view);
        // myDialog.setLayoutXY(width / 2, height / 2);
        myDialog.show();

        radioGroup = view.findViewById(R.id.radioGroup);
        chMonth = view.findViewById(R.id.month);
        chDay = view.findViewById(R.id.day);
        chDate = view.findViewById(R.id.date);

        chDate.setVisibility(View.GONE);
        radioGroup.setOnCheckedChangeListener(this);
        btEnter = view.findViewById(R.id.bt_enter);

        int yyyy = chDate.getYear();
        int m = chDate.getMonth() + 1;
        int d = chDate.getDayOfMonth();
        chDate.init(yyyy, m, d, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int yyyy = chDate.getYear();
                int m = chDate.getMonth() + 1;
                int d = chDate.getDayOfMonth();
                String mm = String.format("%02d", m);
                String dd = String.format("%02d", d);
                find_value = (yyyy + "-" + mm + "-" + dd).trim();
                Log.i("ybf", "find_type=" + find_type.trim());
                Log.i("ybf", "find_value=" + find_value.trim());
            }
        });
   /*     chDate.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int yyyy = chDate.getYear();
                int m = chDate.getMonth() + 1;
                int d = chDate.getDayOfMonth();
                String mm = String.format("%02d", m);
                String dd = String.format("%02d", d);
                find_value = (yyyy + "-" + mm + "-" + dd).trim();
                Log.i("ybf", "find_type=" + find_type.trim());
                Log.i("ybf", "find_value=" + find_value.trim());
            }
        });*/
        //  setOnTimeChangedListener
        btEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                Bundle bundle = new Bundle();
                bundle.clear();
                bundle.putInt(Constant.BILL_TYPE, Constant.EXPEND_TYPE);
                bundle.putInt(Constant.MONTH, DataTools.getInstance().getNowMonth());
                bundle.putString(Constant.FIND_TYPE, find_type); //yue ri
                bundle.putString(Constant.FIND_VALUE, find_value);
                Intent tet = new Intent(MainActivity.this, DayActivity.class);
                tet.putExtras(bundle);
                startActivity(tet);
            }
        });
    }

    public void initDate() {
        Display d = MainActivity.this.getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;    //得到宽度
        height = dm.heightPixels;  //得到高度
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        Log.i("ybf", "checkedId=" + checkedId);
        switch (checkedId) {
            case R.id.month:
                find_type = "month";
                find_value = DataTools.getInstance().getNowMonth() + "";
                chDate.setVisibility(View.GONE);
                break;
            case R.id.day:
                find_type = "time";
                chDate.setVisibility(View.VISIBLE);
                int yyyy = chDate.getYear();
                int m = chDate.getMonth() + 1;
                int d = chDate.getDayOfMonth();
                String mm = String.format("%02d", m);
                String dd = String.format("%02d", d);
                find_value = (yyyy + "-" + mm + "-" + dd).trim();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Permissions.changePermissionState(this, permissions[0], true);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // 需要点击几次 就设置几
    long [] mHits = null;
    public void onDisplaySettingButton() {
        if (mHits == null) {
            mHits = new long[5];
        }
        System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);//把从第二位至最后一位之间的数字复制到第一位至倒数第一位
        mHits[mHits.length - 1] = SystemClock.uptimeMillis();//记录一个时间
        if (SystemClock.uptimeMillis() - mHits[0] <= 1000) {//一秒内连续点击。
            mHits = null;	//这里说明一下，我们在进来以后需要还原状态，否则如果点击过快，第六次，第七次 都会不断进来触发该效果。重新开始计数即可
            Log.i("ybf","进入");
            showTwo();
        }
    }

    private AlertDialog.Builder builder;
    /**
     * 两个按钮的 dialog
     */
    private void showTwo() {

        builder = new AlertDialog.Builder(this).setTitle("是否发送短信").setPositiveButton("发送", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //ToDo: 你想做的事情
                        SharedPreferencesUtils.setParam(MainActivity.this,"sms",1);
                        Toast.makeText(MainActivity.this, "发送", Toast.LENGTH_LONG).show();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //ToDo: 你想做的事情
                        SharedPreferencesUtils.setParam(MainActivity.this,"sms",0);
                        Toast.makeText(MainActivity.this, "取消", Toast.LENGTH_LONG).show();
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }
}
