package com.neusoft.zcapplication.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.neusoft.zcapplication.R;

/**
 * 背景滑动按钮
 */

public class SlideTabBar extends FrameLayout {
    private LinearLayout btnLayout;//按钮的外层view
    private TextView bgView;//移动按钮
    private int btnWidth;//按钮的宽度
    private int btnHeight;//按钮的高度
    private int currentPosition = 0;//当前选中的按钮的下标

    private int defaultOrientation = LinearLayout.HORIZONTAL;//按钮排列方向
    private int btnOrientation ;//按钮排列方向
    private int textSize = 16;//按钮文字大小
    private int textColor = 0xffffff;//按钮文字颜色
    private int textCheckedColor = 0x000000;//选中时的按钮文字颜色

    private int moveBgId = -1;//移动的按钮背景
    private int horizontalPadding = 20;
    private int verticalPadding = 10;

    private OnTabBarChangeListener onTabBarChangeListener;
    private CharSequence[] textAry = null;
    public SlideTabBar(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public SlideTabBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray tp = context.obtainStyledAttributes(attrs, R.styleable.SlideTabBtn);

        if(null != tp){
            btnOrientation = tp.getInt(R.styleable.SlideTabBtn_tabDirect,defaultOrientation);//按钮排列方向

            textAry = tp.getTextArray(R.styleable.SlideTabBtn_tabTitleText);
            textSize = tp.getDimensionPixelSize(R.styleable.SlideTabBtn_tabTitleSize,16);

            textColor = tp.getColor(R.styleable.SlideTabBtn_tabTitleColor,textColor);
            textCheckedColor = tp.getColor(R.styleable.SlideTabBtn_tabTitleCheckedColor,textCheckedColor);

            moveBgId = tp.getResourceId(R.styleable.SlideTabBtn_tabMoveBg,-1);

            horizontalPadding = tp.getDimensionPixelSize(R.styleable.SlideTabBtn_tabBtnHorizontalPadding,20);
            verticalPadding = tp.getDimensionPixelSize(R.styleable.SlideTabBtn_tabBtnVerticalPadding,10);
            tp.recycle();
        }
        initView(context);

    }
    private void initView(Context context){
        //先计算出按钮的尺寸
        measureTextSize(context);
        if(null != textAry){
            int len = textAry.length;
            //移动按钮背景
            bgView = new TextView(context);
            bgView.setLayoutParams(new LayoutParams(btnWidth ,btnHeight));
            if(moveBgId != -1){
                bgView.setBackgroundResource(moveBgId);
            }
            this.addView(bgView);

            //按钮
            btnLayout = new LinearLayout(context);
            btnLayout.setOrientation(btnOrientation);

            for(int i = 0 ;i < textAry.length ; i++){
                TextView btnTv = new TextView(context);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(btnWidth,btnHeight);
                btnTv.setLayoutParams(params);
                btnTv.setText(textAry[i]);
                btnTv.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
                btnTv.setTextColor(textColor);
                btnTv.setGravity(Gravity.CENTER);
                btnTv.setOnClickListener(new TabClickListener(i));
                //默认选中第一个
                if(i == 0){
                    btnTv.setTextColor(textCheckedColor);
                }
                btnLayout.addView(btnTv);
            }
            this.addView(btnLayout);
        }else{

        }
    }
    /**
     * 计算出单个按钮的尺寸
     */
    private void measureTextSize(Context ctx){

        if(null != textAry){
            //遍历计算出要绘制的按钮所需要的最大长度

            for(int i = 0 ;i < textAry.length ; i++){
                Paint paint = new Paint();
                paint.setTextSize(textSize);
                Paint.FontMetrics fm = paint.getFontMetrics();
                int width = (int) paint.measureText(textAry[i].toString()) ;
                int height = (int) (Math.ceil(fm.descent - fm.ascent) + 2) ;
                if( i == 0){
                    btnWidth = (int) paint.measureText(textAry[0].toString());
                    btnHeight = (int) (Math.ceil(fm.descent - fm.ascent) + 2);
                }else{
                    if(btnWidth < width){
                        btnWidth = width;
                    }
                    if(btnHeight < height){
                        btnHeight = height;
                    }
                }

            }
            //添加垂直、水平边距
            btnHeight += verticalPadding;
            btnWidth += horizontalPadding;
        }
    }

    /**
     * 按钮点击事件
     */
    private class TabClickListener implements OnClickListener{
        private int position;

        public TabClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            TextView text = null;
            for(int i = 0; i < textAry.length; i++) {
                text = (TextView) btnLayout.getChildAt(i);
                if(position == i) {
                    //选中状态的文字颜色
                    text.setTextColor(textCheckedColor);
                }else {
                    text.setTextColor(textColor);
                }
            }

            TranslateAnimation anim = generalAnim(position);
            if(null != anim){
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        //记录当前点击的位置
                        currentPosition = position;
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                bgView.startAnimation(anim);
            }


            if(onTabBarChangeListener != null){
                onTabBarChangeListener.onTabBarChange(position);
            }
        }
    }
    /**
     * 监听TabBar的位置变化
     *
     * @param onTabBarChangeListener
     */
    public void setOnTabBarChangeListener(OnTabBarChangeListener onTabBarChangeListener) {
        this.onTabBarChangeListener = onTabBarChangeListener;
    }

    /**
     * TabBar位置监听接口
     *
     */
    public interface OnTabBarChangeListener {
        public void onTabBarChange(int position);
    }

    /**
     * 点击按钮时，生成需移动的按钮执行的动画
     * @param position
     * @return
     */
    private TranslateAnimation generalAnim(int position){
        if(currentPosition != position){
            TranslateAnimation animation;
            if(btnOrientation == defaultOrientation){
                // 水平移动
                int toXDelta = position * btnWidth;
                int fromXDelta = currentPosition * btnWidth;
                animation = new TranslateAnimation(fromXDelta,toXDelta,0,0);
            }else{
                // 垂直移动
                float toYDelta = position * btnHeight;
                float fromYDelta = currentPosition * btnHeight;
                animation = new TranslateAnimation(0,0,fromYDelta,toYDelta);
            }
            animation.setFillAfter(true);
            animation.setDuration(200);
            animation.setInterpolator(new LinearInterpolator());

            return animation;
        }
        return null;
    }
}
