package com.dateview;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static android.widget.Toast.makeText;

/**
 * 选择时间不能超过当天
 */
public class MainActivity extends AppCompatActivity {

    /**
     * 获取屏幕宽高，设置弹窗宽高
     */
    private Dialog mDialog;

    public String mData;
    public int Year, Month, Day,//用于存储实时选中的年月日
            MaxYear, MaxMonth, MaxDay;//用于存储最大年月日，不能超过当天的需求


    private TextView mDayTv;
    private MonthDateView mMonthDateView;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDayTv = (TextView) findViewById(R.id.show_data_tv);

        //获得当天日期
        RealTime();

        //最大年月日，不能超过，计算当天年月日存储用于和选择年月日比对
        MaxYear = Year;
        MaxMonth = Month;
        MaxDay = Day;

        //创建弹窗
        mDialog = DateUtils.CreateMyDialog(this, R.layout.dataview);
        //点击空白处不关闭dialog
        // mDialog.setCancelable(false);
        FindView(DateUtils.getDialogView());
    }

    //点击监听
    public void ViewClickListener(View view) {
        switch (view.getId()) {
            case R.id.show_data_tv:
                mDialog.show();
                break;
            case R.id.year_last:
                mMonthDateView.onLeftsClick();
                break;
            case R.id.year_next:
                if (mMonthDateView.mSelYear >= MaxYear) {
                    showToast("超过时间选择");
                } else {
                    mMonthDateView.onRightsClick();
                }
                break;
            case R.id.month_last:
                mMonthDateView.onLeftClick();
                break;
            case R.id.month_next:
                if (mMonthDateView.mSelYear>= MaxYear && mMonthDateView.mSelMonth + 1 >= MaxMonth) {
                    showToast("超过时间选择");
                } else {
                    mMonthDateView.onRightClick();
                }
                break;
            case R.id.date_cancel:
                mDialog.dismiss();
                break;
            case R.id.date_sumber:
                //获取选中时间
                Year = mMonthDateView.getmSelYear();
                Month = mMonthDateView.getmSelMonth();
                Day = mMonthDateView.getmSelDay();
                if (Day > 0) {
                    mData = getRealTime(Year, Month, Day);
                    if (Month == MaxMonth && Day > MaxDay) {
                        showToast("您选择日期错误,请重新选择" + MaxYear + "年" + MaxMonth + "月" + (MaxDay + 1) + " 日之前的日期");
                    } else {
                        mDayTv.setText(mData);
                        showToast(mData);
                        mDialog.dismiss();
                    }
                } else {
                    showToast("请选择正确时间");
                }
                break;
            default:
                break;
        }
    }

    /**
     * 获取当天时间
     */
    private void RealTime() {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.add(Calendar.DATE, 0);
        Year = mCalendar.get(GregorianCalendar.YEAR);
        Month = mCalendar.get(GregorianCalendar.MONTH) + 1;
        Day = mCalendar.get(GregorianCalendar.DAY_OF_MONTH);
        mData = getRealTime(Year, Month, Day);
        mDayTv.setText(mData);
    }


    private void FindView(View mView) {
        mMonthDateView = (MonthDateView) mView.findViewById(R.id.monthDateView);
        mMonthDateView.setSelectYearMont(Year, (Month - 1), Day);

        //设置年月日
        TextView date_year = (TextView) mView.findViewById(R.id.date_year);
        TextView data_month = (TextView) mView.findViewById(R.id.data_month);
        mMonthDateView.setTextView(date_year, data_month);//设置显示的日期，周
        //标记天数
        List<Integer> list = new ArrayList<Integer>();
        mMonthDateView.setDaysHasThingList(list);
    }
    /**
     * 格式化年月日
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public String getRealTime(int year, int month, int day) {
        return year + "年" + (month < 10 ? "0" : "") + month + "月" + (day < 10 ? "0" : "") + day + "日";
    }

    //自定义Toast
    private void showToast(String str) {
        if (mToast != null) {
            mToast.cancel();
        }
        //自定义Toast
        mToast = DateUtils.MymakeText(this, str, Toast.LENGTH_LONG);
        mToast.show();
    }
}
