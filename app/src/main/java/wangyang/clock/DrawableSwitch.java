package wangyang.clock;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;


/**
 * 一个简单的带提示文字的状态开关
 */
public class DrawableSwitch extends View
{
    private float radius; // 圆的半径
    private int switchOnColor; // 开关打开时的背景颜色
    private int switchOffColor; // 开关关闭时的背景颜色
    private int circleColor; // 圆的填充颜色
    private int textColor; // 文字颜色
    private boolean isSwitchOn; // 开关的状态

    private Paint paint; // 画笔
    private RectF rect; // 画中间的矩形

    private MySwitchStateChangeListener listener;
    private int width,height;

    public DrawableSwitch(Context context)
    {
        super(context);
    }

    public DrawableSwitch(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(attrs, context);
    }

    public DrawableSwitch(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(attrs, context);
    }

    /**
     * 初始化操作，获得自定义的属性值
     *
     * @param attrs
     *            属性集合
     * @param context
     *            上下文
     */
    private void init(AttributeSet attrs, Context context)
    {
     //   Logger.d("init(),  isSwitchOn=" + isSwitchOn);

        TypedArray ta = context.obtainStyledAttributes(attrs,
                R.styleable.drawableSwitch);
        int n = ta.getIndexCount();
        for (int i = 0; i < n; i++)
        {
            int attr = ta.getIndex(i);
            switch (attr)
            // 获得自定义的属性值
            {
                case R.styleable.drawableSwitch_radius:
                    radius = ta.getDimensionPixelSize(attr, (int) TypedValue
                            .applyDimension(TypedValue.COMPLEX_UNIT_DIP, height/2,
                                    getResources().getDisplayMetrics())); // 圆的半径

                    break;
                case R.styleable.drawableSwitch_switchOnColor:
                    switchOnColor = ta.getColor(attr, Color.GREEN); // 开关打开时控件的底色
                    break;
                case R.styleable.drawableSwitch_switchOffColor:
                    switchOffColor = ta.getColor(attr, Color.GRAY); // 开关关闭时控件的底色
                    break;
                case R.styleable.drawableSwitch_circleColor:
                    circleColor = ta.getColor(attr, Color.WHITE); // 圆的颜色，默认为白色
                    break;
                case R.styleable.drawableSwitch_textColor:
                    textColor = ta.getColor(attr, Color.BLACK); // 文字颜色，默认为黑色
                    break;
                case R.styleable.drawableSwitch_isSwitchOn:
                    isSwitchOn = ta.getBoolean(attr, false); // 控件的开关状态
                    break;
            }
        }
        ta.recycle();
        paint = new Paint(); // 画笔对象
        rect = new RectF();// 中间的矩形

        this.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                changeStatus();
            }
        });
    }

    /**
     * 实现更改开关状态，此处需要重绘界面
     */
    protected void changeStatus()
    {
        //Logger.d("changeStatus...");
        isSwitchOn = !isSwitchOn;

        if (listener != null)
        {
            listener.mySwitchStateChanged(isSwitchOn); // 监听开关状态更改事件
        }
        this.postInvalidate(); // 状态更改之后还要更新一下界面
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        // 系统帮我们测量的高度和宽度都是MATCH_PARNET，当我们设置明确的宽度和高度时，
        // 系统帮我们测量的结果就是我们设置的结果，当我们设置为WRAP_CONTENT,或者MATCH_PARENT时
        // 系统帮我们测量的结果就是MATCH_PARENT的长度。
        // 所以，当设置了WRAP_CONTENT时，我们需要自己进行测量，即重写onMesure方法”
        // 重写之前先了解MeasureSpec的specMode,一共三种类型：
        // MeasureSpec.EXACTLY 一般是设置了明确的值或者是MATCH_PARENT
        // MeasureSpec.AT_MOST 表示子布局限制在一个最大值内，一般为WARP_CONTENT
        // MeasureSpec.UNSPECIFIED表示子布局想要多大就多大，很少使用

         width = 0;
         height = 0;
        // 设置宽度
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        switch (specMode)
        {
            case MeasureSpec.EXACTLY: // 明确指定
                width = specSize;
                break;
            case MeasureSpec.AT_MOST:
                width = getPaddingLeft() + getPaddingRight();
                break;
        }

        // 设置高度
        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);
        switch (specMode)
        {
            case MeasureSpec.EXACTLY:
                height = specSize;
                break;
            case MeasureSpec.AT_MOST:
                height = width / 20;
                break;
        }
      //  Logger.d("onMeasure(),width=" + width + ",height=" + height);
        setMeasuredDimension(width, height);
    }

    /**
     * 绘制操作的具体实现
     */
    @Override
    protected void onDraw(Canvas canvas)
    {
        // super.onDraw(canvas);
        // 图形的绘制：左边一个半圆，中间一个矩形，右边一个半圆。实际绘制时，分两种情况：switchOn和switchOff
        // switchOn：先绘制左边的整个圆，然后再绘制矩形，然后改变颜色，绘制右边的圆，中间的矩形被右边的圆覆盖了一部分
        // switchOff：先绘制中间的矩形，再绘制右边的圆，右边的圆的颜色和矩形相同，然后再更改颜色，绘制左边的圆
        //Logger.d("onDraw(),  isSwitchOn=" + isSwitchOn);

        float switchWidth = 3.0f * radius;
        float switchHeight = 2.0f * radius;

        paint.setAntiAlias(true);// 设置抗锯齿
        if (isSwitchOn == true)
        { // 打开时的状态
            paint.setColor(switchOnColor);
            paint.setStyle(Paint.Style.FILL); // 设置为填充圆
            canvas.drawCircle(radius, radius, radius, paint);// 画左边的圆

            rect.set(radius, 0, radius + switchWidth, switchHeight);
            canvas.drawRect(rect, paint); // 画中间的矩形

            paint.setColor(circleColor); // 改一下圆的填充色
            canvas.drawCircle(radius + switchWidth, radius, radius, paint); // 画右边的圆

            paint.setColor(textColor); // 文字颜色设置为布局文件里的值
            paint.setTextSize(15); // 设置文字大小
            String text = "";
            FontMetrics fontMetrics = paint.getFontMetrics();
            float fontHeight = fontMetrics.bottom - fontMetrics.top;
            float baseline = radius + fontHeight / 2 - fontMetrics.bottom; // 计算文字绘制时的baseline
            canvas.drawText(text, radius, baseline, paint); // 绘制文字，//打开时不需要获取绘制文字的初始x坐标，直接为radius
        }
        else
        { // 关闭时的状态
            paint.setColor(switchOffColor);
            paint.setStyle(Paint.Style.FILL);
            rect.set(radius, 0, radius + switchWidth, switchHeight);
            canvas.drawRect(rect, paint); // 先画中间的矩形

            canvas.drawCircle(radius + switchWidth, radius, radius, paint); // 再画右边的圆

            paint.setColor(circleColor);
            canvas.drawCircle(radius, radius, radius, paint); // 再画左边的圆

            paint.setColor(textColor); // 文字颜色设置为布局文件里的值
            paint.setTextSize(15); // 设置文字大小
            String text = "";
            float textWidth = paint.measureText(text); // 关闭时需获取文字宽度
            FontMetrics fontMetrics = paint.getFontMetrics();
            float fontHeight = fontMetrics.bottom - fontMetrics.top;
            float baseline = radius + fontHeight / 2 - fontMetrics.bottom; // 计算文字绘制时的baseline
            canvas.drawText(text, switchWidth + radius - textWidth, baseline,
                    paint); // 绘制文字，文字绘制的初始x坐标为右圆的圆心x坐标减去文字的宽度
        }
    }

    /**
     * 获得控件的开启状态
     *
     * @return isSwitchOn 开关的状态
     */
    public boolean isSwitchOn()
    {
        return isSwitchOn;
    }

    /**
     * 设置控件的开关状态
     *
     * @param isSwitchOn
     *            开关的状态
     */
    public void setSwitchOn(boolean isSwitchOn)
    {
        this.isSwitchOn = isSwitchOn;
    }

    /**
     * 设置监听器，监听控件开关状态是否改变
     *
     * @param myListener
     *            外部传入的监听器，用来监听控件的开关状态
     */
    public void setListener(MySwitchStateChangeListener myListener)
    {
        this.listener = myListener;
    }

    /**
     * 定义的一个内部接口，用来监听控件状态更改事件
     */
    public interface MySwitchStateChangeListener
    {
        public void mySwitchStateChanged(boolean isSwitchOn);
    }
}
