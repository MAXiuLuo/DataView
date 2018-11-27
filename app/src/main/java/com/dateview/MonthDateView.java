package com.dateview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;



import java.util.Calendar;
import java.util.List;

public class MonthDateView extends View {
	private static final int NUM_COLUMNS = 7;
	private static final int NUM_ROWS = 6;
	private Paint mPaint,mbg_Paint;
	private int mDayColor = Color.parseColor("#FF2C3135");
	private int mSelectDayColor = Color.parseColor("#ffffff");
	private int mSelectBGColor = Color.parseColor("#ffa000");
    //paint可以调用settShader方法设置画笔的渐变色
	private int mCurrentColor = Color.parseColor("#FF2C3135");
	private int mCurrYear,mCurrMonth,mCurrDay;

	public int mSelYear,mSelMonth,mSelDay;

	private int mColumnSize,mRowSize;
	private DisplayMetrics mDisplayMetrics;
	private int mDaySize = 19;
	private TextView tv_date,tv_week;
	private int weekRow;
	private int [][] daysString;
	private int mCircleRadius = 6;
	private DateClick dateClick;
	private int mCircleColor = Color.parseColor("#ff0000");
	private List<Integer> daysHasThingList;

	public MonthDateView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mDisplayMetrics = getResources().getDisplayMetrics();
		Calendar calendar = Calendar.getInstance();
		mPaint = new Paint();
        mbg_Paint = new Paint();
		mCurrYear = calendar.get(Calendar.YEAR);
		mCurrMonth = calendar.get(Calendar.MONTH);
		mCurrDay = calendar.get(Calendar.DATE);
		setSelectYearMonth(mCurrYear,mCurrMonth,mCurrDay);
	}

    public void setSelectYearMont(int year , int month, int day){
        setSelectYearMonth(year,month,day);
        invalidate();
    }
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		initSize();
		daysString = new int[6][7];
		mPaint.setTextSize(mDaySize*mDisplayMetrics.scaledDensity);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mbg_Paint.setTextSize(mDaySize*mDisplayMetrics.scaledDensity);
        mbg_Paint.setAntiAlias(true);
		String dayString;
		int mMonthDays = DateUtils.getMonthDays(mSelYear, mSelMonth);
		int weekNumber = DateUtils.getFirstDayWeek(mSelYear, mSelMonth);
		for(int day = 0;day < mMonthDays;day++){
			dayString = (day + 1) + "";

			int column = (day+weekNumber - 1) % 7;
			int row = (day+weekNumber - 1) / 7;
			daysString[row][column]=day + 1;
			int startX = (int) (mColumnSize * column + (mColumnSize - mPaint.measureText(dayString))/2);
			int startY = (int) (mRowSize * row + mRowSize/2 - (mPaint.ascent() + mPaint.descent())/2);
			if(dayString.equals(mSelDay+"")){
				mPaint.setColor(mSelectBGColor);//设置颜色
                mbg_Paint.setShader(new LinearGradient(mColumnSize * column , mRowSize * row + 20 ,
                        mColumnSize * column+60,mRowSize * row+ 80,
                        Color.parseColor("#FFC045"), Color.parseColor("#FF6300"), Shader.TileMode.CLAMP));
                //radius为阴影的角度，dx和dy为阴影在x轴和y轴上的距离，color为阴影的颜色
                // 设定阴影(柔边, X 轴位移, Y 轴位移, 阴影颜色)
                //mbg_Paint.setShadowLayer(100, 100,10, 0xFFFF00FF);
                canvas.drawCircle(mColumnSize * column+(mColumnSize/2),mRowSize * row+(mRowSize/2),mColumnSize/3,mbg_Paint);
				//记录第几行，即第几周
				weekRow = row + 1;
			}
			//绘制事务圆形标志
			drawCircle(row,column,day + 1,canvas);
			if(dayString.equals(mSelDay+"")){
				mPaint.setColor(mSelectDayColor);//选中当天
			}else if(Integer.valueOf(dayString)>mCurrDay  && mCurrMonth == mSelMonth &&  mCurrYear == mSelYear){
                mPaint.setColor(Color.parseColor("#e5e5e5"));//需求屏蔽的日期
			}else{
				mPaint.setColor(mDayColor);//默认日期颜色
			}
			canvas.drawText(dayString, startX, startY, mPaint);
			if(tv_date != null){
				tv_date.setText(mSelYear + "年");
			}
			if(tv_week != null){
				tv_week.setText((mSelMonth + 1) + "月");
			}
		}
	}
	
	private void drawCircle(int row,int column,int day,Canvas canvas){
		if(daysHasThingList != null && daysHasThingList.size() >0){
			if(!daysHasThingList.contains(day))return;
			mPaint.setColor(mCircleColor);
			float circleX = (float) (mColumnSize * column +	mColumnSize*0.8);
			float circley = (float) (mRowSize * row + mRowSize*0.2);
			canvas.drawCircle(circleX, circley, mCircleRadius, mPaint);
		}
	}
	@Override
	public boolean performClick() {
		return super.performClick();
	}

	private int downX = 0,downY = 0;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int eventCode=  event.getAction();
		switch(eventCode){
		case MotionEvent.ACTION_DOWN:
			downX = (int) event.getX();
			downY = (int) event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			int upX = (int) event.getX();
			int upY = (int) event.getY();
			if(Math.abs(upX-downX) < 10 && Math.abs(upY - downY) < 10){//点击事件
				performClick();
				doClickAction((upX + downX)/2,(upY + downY)/2);
			}
			break;
		}
		return true;
	}

	/**
	 * 初始化列宽行高
	 */
	private void initSize(){
		mColumnSize = getWidth() / NUM_COLUMNS;
		mRowSize = getHeight() / NUM_ROWS;
//        mRowSize = getHeight() / NUM_ROWS-1;是否保存下一行
	}
	
	/**
	 * 设置年月
	 * @param year
	 * @param month
	 */
	private void setSelectYearMonth(int year,int month,int day){
		mSelYear = year;
		mSelMonth = month;
		mSelDay = day;
	}
	/**
	 * 执行点击事件
	 * @param x
	 * @param y
	 */
	private void doClickAction(int x,int y){
		int row = y / mRowSize;
		int column = x / mColumnSize;
		setSelectYearMonth(mSelYear,mSelMonth,daysString[row][column]);
		invalidate();
		//执行activity发送过来的点击处理事件
		if(dateClick != null){
			dateClick.onClickOnDate();
		}
	}

	/**
	 * 左点击，日历向后翻页
	 */
	public void onLeftClick(){
		int year = mSelYear;
		int month = mSelMonth;
		int day = mSelDay;
		if(month == 0){//若果是1月份，则变成12月份
			year = mSelYear-1;
			month = 11;
		}else if(DateUtils.getMonthDays(year, month) == day){
			//如果当前日期为该月最后一点，当向前推的时候，就需要改变选中的日期
			month = month-1;
			day = DateUtils.getMonthDays(year, month);
		}else{
			month = month-1;
		}
		setSelectYearMonth(year,month,day);
		invalidate();
	}

    public void onLeftsClick(){
        int year = mSelYear-1;
        int month = mSelMonth;
        int day = mSelDay;
        if(month == 0){//若果是1月份，则变成12月份
            year = mSelYear-1;
            month = 11;
        }else if(DateUtils.getMonthDays(year, month) == day){
            //如果当前日期为该月最后一点，当向前推的时候，就需要改变选中的日期
            month = month;
            day = DateUtils.getMonthDays(year, month);
        }else{
            month = month;
        }
        setSelectYearMonth(year,month,day);
        invalidate();
    }
	
	/**
	 * 右点击，日历向前翻页
	 */
	public void onRightClick(){
		int year = mSelYear;
		int month = mSelMonth;
		int day = mSelDay;
		if(month == 11){//若果是12月份，则变成1月份
			year = mSelYear+1;
			month = 0;
		}else if(DateUtils.getMonthDays(year, month) == day){
			//如果当前日期为该月最后一点，当向前推的时候，就需要改变选中的日期
			month = month + 1;
			day = DateUtils.getMonthDays(year, month);
		}else{
			month = month + 1;
		}
		setSelectYearMonth(year,month,day);
		invalidate();
	}


    /**
     * 右点击，日历向前翻页
     */
    public void onRightsClick(){
        int year = mSelYear+1;
        int month = mSelMonth;
        int day = mSelDay;
        if(month == 11){//若果是12月份，则变成1月份
            year = mSelYear+1;
            month = 0;
        }else if(DateUtils.getMonthDays(year, month) == day){
            //如果当前日期为该月最后一点，当向前推的时候，就需要改变选中的日期
            month = month ;
            day = DateUtils.getMonthDays(year, month);
        }else{
            month = month ;
        }
        setSelectYearMonth(year,month,day);
        invalidate();
    }

    /**
	 * 获取选择的年份
	 * @return
	 */
	public int getmSelYear() {
		return mSelYear;
	}
	/**
	 * 获取选择的月份
	 * @return
	 */
	public int getmSelMonth() {
		return mSelMonth+1 ;
	}
	/**
	 * 获取选择的日期
	 * @param
	 */
	public int getmSelDay() {
		return this.mSelDay;
	}
	/**
	 * 普通日期的字体颜色，默认黑色
	 * @param mDayColor
	 */
	public void setmDayColor(int mDayColor) {
		this.mDayColor = mDayColor;
	}
	
	/**
	 * 选择日期的颜色，默认为白色
	 * @param mSelectDayColor
	 */
	public void setmSelectDayColor(int mSelectDayColor) {
		this.mSelectDayColor = mSelectDayColor;
	}

	/**
	 * 选中日期的背景颜色，默认蓝色
	 * @param mSelectBGColor
	 */
	public void setmSelectBGColor(int mSelectBGColor) {
		this.mSelectBGColor = mSelectBGColor;
	}
	/**
	 * 当前日期不是选中的颜色，默认红色
	 * @param mCurrentColor
	 */
	public void setmCurrentColor(int mCurrentColor) {
		this.mCurrentColor = mCurrentColor;
	}

	/**
	 * 日期的大小，默认18sp
	 * @param mDaySize
	 */
	public void setmDaySize(int mDaySize) {
		this.mDaySize = mDaySize;
	}
	/**
	 * 设置显示当前日期的控件
	 * @param tv_date
	 * 		显示日期
	 * @param tv_week
	 * 		显示周
	 */
	public void setTextView(TextView tv_date, TextView tv_week){
		this.tv_date = tv_date;
		this.tv_week = tv_week;
		invalidate();
	}

	/**
	 * 设置事务天数
	 * @param daysHasThingList
	 */
	public void setDaysHasThingList(List<Integer> daysHasThingList) {
		this.daysHasThingList = daysHasThingList;
	}

	/***
	 * 设置圆圈的半径，默认为6
	 * @param mCircleRadius
	 */
	public void setmCircleRadius(int mCircleRadius) {
		this.mCircleRadius = mCircleRadius;
	}
	
	/**
	 * 设置圆圈的半径
	 * @param mCircleColor
	 */
	public void setmCircleColor(int mCircleColor) {
		this.mCircleColor = mCircleColor;
	}
	
	/**
	 * 设置日期的点击回调事件
	 * @author shiwei.deng
	 *
	 */
	public interface DateClick{
		public void onClickOnDate();
	}

	/**
	 * 设置日期点击事件
	 * @param dateClick
	 */
	public void setDateClick(DateClick dateClick) {
		this.dateClick = dateClick;
	}
	
	/**
	 * 跳转至今天
	 */
	public void setTodayToView(){
		setSelectYearMonth(mCurrYear,mCurrMonth,mCurrDay);
		invalidate();
	}
}
