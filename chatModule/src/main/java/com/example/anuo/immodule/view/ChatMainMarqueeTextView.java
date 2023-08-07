package com.example.anuo.immodule.view;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.example.anuo.immodule.utils.ScreenUtil;

/*聊天首页跑马灯*/
public class ChatMainMarqueeTextView extends AppCompatTextView {

    /** 是否停止滚动 */
    private boolean mStopMarquee;
    private String mText;
    private float mCoordinateX = 350;
    private float mTextWidth;

    public ChatMainMarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setText(String text) {
        this.mText = text;
        if (!TextUtils.isEmpty(mText)) {
            mTextWidth = getPaint().measureText(mText);
        }
        if (mHandler.hasMessages(0))
            mHandler.removeMessages(0);
        mHandler.sendEmptyMessageDelayed(0, 2000);
        mStopMarquee = mStopMarquee;
    }

    @Override
    protected void onAttachedToWindow() {
        mStopMarquee = false;
        if (!TextUtils.isEmpty(mText))
            mHandler.sendEmptyMessageDelayed(0, 2000);
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        mStopMarquee = true;
        if (mHandler.hasMessages(0))
            mHandler.removeMessages(0);
        super.onDetachedFromWindow();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!TextUtils.isEmpty(mText))
            canvas.drawText(mText, mCoordinateX, ScreenUtil.dip2px(getContext(),12), getPaint());
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (Math.abs(mCoordinateX) > (mTextWidth + 30)) {
                        mCoordinateX = 350;
                        invalidate();
                        if (!mStopMarquee) {
                            sendEmptyMessageDelayed(0, 0);
                        }
                    } else {
                        mCoordinateX -= 1;
                        invalidate();
                        if (!mStopMarquee) {
                            sendEmptyMessageDelayed(0, 8);
                        }
                    }

                    break;
            }
            super.handleMessage(msg);
        }
    };

}
