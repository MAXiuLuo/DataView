# 安卓自定义日期选择器
<img src="https://raw.githubusercontent.com/MAXiuLuo/DataView/master/exhibition_img.png" width="427" height="463" alt="图片加载失败，点击项目内exhibition_img查看"/>


## WeekDayView     -onDraw(Canvas canvas)
   将周日到周一绘画出来，将周和日期分离，主要代码
```
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        //进行画上下线
        paint.setStyle(Style.FILL);
        paint.setColor(mTopLineColor);
        paint.setStrokeWidth(mStrokeWidth);
        canvas.drawLine(0, 0, width, 0, paint);
        //画下横线
        paint.setColor(mBottomLineColor);
        canvas.drawLine(0, height, width, height, paint);
        //最后画字
        paint.setStyle(Style.FILL);
        paint.setTextSize(mWeekSize * mDisplayMetrics.scaledDensity);
        paint.setAntiAlias(true);
        int columnWidth = width / 7;//宽度除以7，分成七块
        for (int i = 0; i < weekString.length; i++) {
            String text = weekString[i];
            int fontWidth = (int) paint.measureText(text);
            int startX = columnWidth * i + (columnWidth - fontWidth) / 2;
            int startY = (int) (height / 2 - (paint.ascent() + paint.descent()) / 2);
            if (text.indexOf("日") > -1 || text.indexOf("六") > -1) {
                paint.setColor(mWeekendColor);
            } else {
                paint.setColor(mWeedayColor);
            }
            canvas.drawText(text, startX, startY, paint);
        }
    }
```


##  MonthDateView  -onDraw(Canvas canvas)
  每个月的日期绘制，主要代码
```
        int mMonthDays = DateUtils.getMonthDays(mSelYear, mSelMonth);
        int weekNumber = DateUtils.getFirstDayWeek(mSelYear, mSelMonth);
        for (int day = 0; day < mMonthDays; day++) {
            dayString = (day + 1) + "";
            int column = (day + weekNumber - 1) % 7;
            int row = (day + weekNumber - 1) / 7;
            daysString[row][column] = day + 1;
            int startX = (int) (mColumnSize * column + (mColumnSize - mPaint.measureText(dayString)) / 2);
            int startY = (int) (mRowSize * row + mRowSize / 2 - (mPaint.ascent() + mPaint.descent()) / 2);
            if (dayString.equals(mSelDay + "")) {
                mPaint.setColor(mSelectBGColor);//设置颜色
                mbg_Paint.setShader(new LinearGradient(mColumnSize * column, mRowSize * row + 20, mColumnSize * column + 60, mRowSize * row + 80, Color.parseColor("#FFC045"), Color.parseColor("#FF6300"), Shader.TileMode.CLAMP));
                //radius为阴影的角度，dx和dy为阴影在x轴和y轴上的距离，color为阴影的颜色
                canvas.drawCircle(mColumnSize * column + (mColumnSize / 2), mRowSize * row + (mRowSize / 2), mColumnSize / 3, mbg_Paint);
                //记录第几行，即第几周
                weekRow = row + 1;
            }
            //绘制事务圆形标志
            drawCircle(row, column, day + 1, canvas);
            if (dayString.equals(mSelDay + "")) {
                mPaint.setColor(mSelectDayColor);//选中当天
            } else if (Integer.valueOf(dayString) > mCurrDay && mCurrMonth == mSelMonth && mCurrYear == mSelYear) {
                mPaint.setColor(Color.parseColor("#e5e5e5"));//需求屏蔽的日期
            } else {
                mPaint.setColor(mDayColor);//默认日期颜色
            }
            canvas.drawText(dayString, startX, startY, mPaint);
            if (tv_date != null) {
                tv_date.setText(mSelYear + "年");
            }
            if (tv_week != null) {
                tv_week.setText((mSelMonth + 1) + "月");
            }
```
### 选中改变年月，只需要调用MonthDateView内的方法，如下
```
- onLeftClick()
- onRightClick()

mMonthDateView.onLeftClick();
mMonthDateView.onRightClick();

```


## ScreenShot
<img src="https://raw.githubusercontent.com/MAXiuLuo/DataView/master/exhibition_img.png" width="427" height="463" alt="图片加载失败，点击项目内exhibition_img查看"/>
<img src="https://raw.githubusercontent.com/MAXiuLuo/DataView/master/exhibition_imgs.png" width="427" height="463" alt="图片加载失败，点击项目内exhibition_imgs查看"/>
