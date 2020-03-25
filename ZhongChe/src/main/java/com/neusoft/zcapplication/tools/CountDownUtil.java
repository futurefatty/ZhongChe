package com.neusoft.zcapplication.tools;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.widget.TextView;

/**
 * Author: TenzLiu
 * Time: 2018/10/16 10:55
 * Desc:
 */

public abstract class CountDownUtil {


    public abstract void onFinish(Object tag);


    /**
     * Millis since epoch when alarm should stop.
     */
    private final long mMillisInFuture;

    /**
     * The interval in millis that the user receives callbacks
     */
    private final long mCountdownInterval;

    private long mStopTimeInFuture;

    /**
     * boolean representing if the timer was cancelled
     */
    private boolean mCancelled = false;


    public CountDownUtil(long millisInFuture, long countDownInterval) {
        mMillisInFuture = millisInFuture;
        mCountdownInterval = countDownInterval;
    }

    private Object tag;

    public void setTag(Object tag) {
        this.tag = tag;
    }

    /**
     * Cancel the countdown.
     */
    public synchronized final void cancel() {
        mCancelled = true;
        mHandler.removeMessages(MSG);
    }

    /**
     * Start the countdown.
     */
    public synchronized final CountDownUtil start() {
        mCancelled = false;
        if (mMillisInFuture <= 0) {
            onFinish(tag);
            return this;
        }
        mStopTimeInFuture = SystemClock.elapsedRealtime() + mMillisInFuture;
        Message message = mHandler.obtainMessage(MSG);
        message.obj = tag;
        mHandler.sendMessage(message);
        return this;
    }


    /**
     * Callback fired on regular interval.
     *
     * @param millisUntilFinished The amount of time until finished.
     */
    public abstract void onTick(long millisUntilFinished, Object tag);


    private static final int MSG = 1;


    // handles counting down
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            synchronized (CountDownUtil.this) {
                if (mCancelled) {
                    return;
                }

                final long millisLeft = mStopTimeInFuture - SystemClock.elapsedRealtime();

                if (millisLeft <= 0) {
                    onFinish(msg.obj);
                } else {
                    long lastTickStart = SystemClock.elapsedRealtime();
                    onTick(millisLeft, msg.obj);

                    // take into account user's onTick taking time to execute
                    long lastTickDuration = SystemClock.elapsedRealtime() - lastTickStart;
                    long delay;

                    if (millisLeft < mCountdownInterval) {
                        // just delay until done
                        delay = millisLeft - lastTickDuration;

                        // special case: user's onTick took more than interval to
                        // complete, trigger onFinish without delay
                        if (delay < 0) delay = 0;
                    } else {
                        delay = mCountdownInterval - lastTickDuration;

                        // special case: user's onTick took more than interval to
                        // complete, skip to next interval
                        while (delay < 0) delay += mCountdownInterval;
                    }
                    Message message = obtainMessage(MSG);
                    message.obj = msg.obj;
                    sendMessageDelayed(message, delay);
                }
            }
        }
    };
}

