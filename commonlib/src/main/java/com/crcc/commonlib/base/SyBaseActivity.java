package com.crcc.commonlib.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crcc.commonlib.R;
import com.crcc.commonlib.event.Event;
import com.crcc.commonlib.event.EventBusUtil;
import com.crcc.commonlib.utils.DisposableManager;
import com.crcc.commonlib.utils.InputTool;
import com.crcc.commonlib.utils.StringUtils;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.ref.WeakReference;

/**
 * Created  2018/3/23.
 *
 * @author six
 */

public abstract class SyBaseActivity extends AppCompatActivity {
    protected LinearLayout llBaseControl;
    private ViewStub titleStub;
    protected View contentView;
    protected TextView commTitleTitle;
    protected TextView commTitleRightText;
    protected Toolbar commTitleToolbar;
    protected View commTitleBack;
    protected LoadService loadService;
    protected DisposableManager disposableManager = DisposableManager.newInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View inflate = View.inflate(this, R.layout.common_activity_base, null);
        llBaseControl = (LinearLayout) inflate.findViewById(R.id.ll_base_control);
        titleStub = (ViewStub) inflate.findViewById(R.id.title_stub);
        ViewStub viewStub = (ViewStub) inflate.findViewById(R.id.viewStub);
        viewStub.setLayoutResource(getLayoutId());
        contentView = viewStub.inflate();
        setContentView(inflate);

        initView(savedInstanceState);

        //注册事件
        if (isRegisterEventBus() && !EventBusUtil.isRegister(this)) {
            EventBusUtil.register(this);
        }
    }


    protected abstract void initView(Bundle savedInstanceState);


    /**
     * 子类重写此方法是否需要注册Evnet
     *
     * @return
     */
    protected boolean isRegisterEventBus() {
        return false;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusCome(Event event) {
        if (event != null) {
            receiveEvent(event);
        }
    }

    protected void receiveEvent(Event event) {

    }


    protected abstract int getLayoutId();

    private void setView(Builder builder) {
        switch (builder.mTitleModel) {
            case Builder.COMM_TITLE:
                titleStub.setLayoutResource(R.layout.comm_layout_title);
                titleStub.inflate();
                commTitleToolbar = (Toolbar) findViewById(R.id.comm_title_toolbar);
                commTitleBack = findViewById(R.id.comm_title_back);
                commTitleTitle = (TextView) findViewById(R.id.comm_title_title);
                commTitleRightText = (TextView) findViewById(R.id.comm_title_right_text);
                ImageView commTitleRightImg = (ImageView) findViewById(R.id.comm_title_right_img);
                FrameLayout commTitleRight = (FrameLayout) findViewById(R.id.comm_title_right);

                setSupportActionBar(commTitleToolbar);
                int titleColor = builder.titleColor;
                int titleColorBg = builder.titleColorBg;
                int titleRightTextColor = builder.titleRightTextColor;
                int titleRightImgId = builder.titleRightImgId;
                int titleBackVis = builder.titleBackVis;


                if (titleBackVis != View.VISIBLE) {
                    commTitleBack.setVisibility(View.GONE);
                }

                if (-2 != titleColor) {
                    commTitleTitle.setTextColor(titleColor);
                }
                if (-2 != titleColorBg) {
                    commTitleToolbar.setBackgroundColor(titleColorBg);
                }
                if (-2 != titleRightTextColor) {
                    commTitleRightText.setTextColor(titleRightTextColor);
                }
                if (-2 != titleRightImgId) {
                    commTitleRightImg.setBackgroundResource(titleRightImgId);
                }
                commTitleTitle.setText(StringUtils.getString(builder.mTitle));

                commTitleBack.setOnClickListener(builder.backOnClickListener == null ? new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                } : builder.backOnClickListener);

                commTitleRightText.setText(StringUtils.getString(builder.titleRightText));

                if (builder.rightTextOnClickListener != null) {
                    commTitleRight.setOnClickListener(builder.rightTextOnClickListener);
                }
                break;
            case Builder.NO_TITLE:

                break;
            default:

                break;
        }

        //是否需要空布局和正在加载布局
        if (builder.isMuliteView) {
            loadService = LoadSir.getDefault().register(contentView);
        }
    }


    public static class Builder {
        @IntDef({COMM_TITLE, NO_TITLE})

        @Documented
        @Target({
                ElementType.FIELD,
                ElementType.METHOD,
                ElementType.PARAMETER,
        })
        @Retention(RetentionPolicy.SOURCE)
        public @interface TitleModel {

        }

        public static final int COMM_TITLE = 0;
        public static final int NO_TITLE = 1;
        @TitleModel
        private int mTitleModel;

        private int titleColorBg = -2;
        private int titleRightImgId = -2;
        private int titleColor = -2;
        private int titleRightTextColor = -2;
        private String titleRightText;
        private String mTitle;


        private int titleBackVis = View.VISIBLE;

        private final WeakReference<SyBaseActivity> mWeakReference;
        private boolean isMuliteView = true;
        private View.OnClickListener backOnClickListener, rightTextOnClickListener;


        public Builder(SyBaseActivity syBaseActivity) {
            mWeakReference = new WeakReference(syBaseActivity);
        }


        public Builder setTitle(String title) {
            this.mTitle = title;
            return this;
        }


        public Builder setTitleColor(@ColorInt int titleColor) {
            this.titleColor = titleColor;
            return this;
        }


        public Builder setBackOnClickListener(View.OnClickListener backOnClickListener) {
            this.backOnClickListener = backOnClickListener;
            return this;
        }

        public Builder setRightTextOnClickListener(View.OnClickListener rightTextOnClickListener) {
            this.rightTextOnClickListener = rightTextOnClickListener;
            return this;
        }


        public Builder setTitleRightText(String titleRightText) {
            this.titleRightText = titleRightText;
            return this;
        }


        public Builder setTitleRightColor(@ColorInt int titleRightTextColor) {
            this.titleRightTextColor = titleRightTextColor;
            return this;
        }

        public Builder isMuliteView(boolean isMuliteView) {
            this.isMuliteView = isMuliteView;
            return this;
        }


        public Builder setTitleModel(@TitleModel int titleModel) {
            this.mTitleModel = titleModel;
            return this;
        }


        public Builder setTitleRightImgId(int titleRightImgId) {
            this.titleRightImgId = titleRightImgId;
            return this;
        }


        public Builder setTitleColorBg(@ColorInt int titleColorBg) {
            this.titleColorBg = titleColorBg;
            return this;
        }

        public Builder setTitleBackVis(int titleBackVis) {
            this.titleBackVis = titleBackVis;
            return this;
        }

        public void build() {
            mWeakReference.get().setView(this);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isRegisterEventBus()) {
            EventBusUtil.unregister(this);
        }
        disposableManager.unsubscribe();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputTool.HideKeyboard(v);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }


}
