package com.dateview;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class DateUtils {
	/**
     * 通过年份和月份 得到当月的日子
     * 
     * @param year
     * @param month
     * @return
     */
    public static int getMonthDays(int year, int month) {
		month++;
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12: 
		    return 31;
		case 4:
		case 6:
		case 9:
		case 11: 
		    return 30;
		case 2:
			if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)){
				return 29;
			}else{
				return 28;
			}
		default:
			return  -1;
		}
    }
    /**
     * 返回当前月份1号位于周几
     * @param year
     * 		年份
     * @param month
     * 		月份，传入系统获取的，不需要正常的
     * @return
     * 	日：1		一：2		二：3		三：4		四：5		五：6		六：7
     */
    public static int getFirstDayWeek(int year, int month){
    	Calendar calendar = Calendar.getInstance();
    	calendar.set(year, month, 1);
    	return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static Toast MymakeText(Context context, CharSequence text,
                             int duration) {
        Toast result = new Toast(context);
        // 获取LayoutInflater对象
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // 由layout文件创建一个View对象
        View layout = inflater.inflate(R.layout.toast_layout, null);
        // 实例化ImageView和TextView对象
        TextView textView = (TextView) layout.findViewById(R.id.message);
        textView.setText(text);
        result.setView(layout);
        result.setDuration(duration);
        return result;
    }

    private static Display mDisplay;
    private static Dialog mDialog;
    private static View mView;
    public static Dialog CreateMyDialog(Context context, int LayoutId) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mDisplay = windowManager.getDefaultDisplay();
        // 获取Dialog布局   获取自定义Dialog布局中的控件
        mView = LayoutInflater.from(context).inflate(R.layout.dataview, null);
        // 设置Dialog最小宽度为屏幕宽度
        mView.setMinimumWidth(mDisplay.getWidth());
        // 定义Dialog布局和参数
        mDialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        mDialog.setContentView(mView);

        mDialog.setCancelable(true);//空白点击不退出
        mDialog.setCanceledOnTouchOutside(true);

        Window dialogWindow = mDialog.getWindow();
        //设置位置
        dialogWindow.setGravity(Gravity.BOTTOM | Gravity.LEFT);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;

        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.height = (int) (d.heightPixels * 0.6); // 高度设置为屏幕的0.58
        lp.width = d.widthPixels;
        dialogWindow.setAttributes(lp);
        return mDialog;
    }

    public static View getDialogView() {
        return mView;
    }
}
