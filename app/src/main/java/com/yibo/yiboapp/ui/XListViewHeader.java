package com.yibo.yiboapp.ui;


import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.utils.Utils;


public class XListViewHeader extends LinearLayout {
	private LinearLayout mContainer;
	//private ImageView mArrowImageView;
	private ProgressBar mProgressBar;
	private TextView mHintTextView;
	private TextView updateTime;
	private int mState = STATE_NORMAL;
	
	final int ROTATION_ANIMATION_DURATION = 1200;
	private Animation mRotateAnimation;
	private Matrix mHeaderImageMatrix;
	static final Interpolator ANIMATION_INTERPOLATOR = new LinearInterpolator();

	private Animation mRotateUpAnim;
	private Animation mRotateDownAnim;

	private final int ROTATE_ANIM_DURATION = 180;

	public final static int STATE_NORMAL = 0;
	public final static int STATE_READY = 1;
	public final static int STATE_REFRESHING = 2;

	public XListViewHeader(Context context) {
		super(context);
		initView(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public XListViewHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
		LayoutParams lp = new LayoutParams(
				LayoutParams.MATCH_PARENT, 0);
		mContainer = (LinearLayout) LayoutInflater.from(context).inflate(
				R.layout.xlistview_header, null);
		addView(mContainer, lp);
		setGravity(Gravity.BOTTOM);

		//mArrowImageView = (ImageView) findViewById(R.id.xlistview_header_arrow);
		mHintTextView = (TextView) findViewById(R.id.xlistview_header_hint_textview);
		updateTime = (TextView) findViewById(R.id.xlistview_header_time);
		mProgressBar = (ProgressBar) findViewById(R.id.xlistview_header_progressbar);

		mRotateUpAnim = new RotateAnimation(0.0f, -180.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateUpAnim.setFillAfter(true);
		mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateDownAnim.setFillAfter(true);
		initRotateParams();//初始化加载动画
	}
	
	private void onLoadingStart(){
		mProgressBar.startAnimation(mRotateAnimation);
	}
	
	private void onLoadingEnd(){
		mProgressBar.clearAnimation();
//		resetImageRotation();
	}
	
	private void resetImageRotation() {
		if (null != mHeaderImageMatrix) {
			mHeaderImageMatrix.reset();
//			mProgressBar.setImageMatrix(mHeaderImageMatrix);
		}
	}
	

	private void initRotateParams(){
		mHeaderImageMatrix = new Matrix();
		mRotateAnimation = new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
		mRotateAnimation.setDuration(ROTATION_ANIMATION_DURATION);
		mRotateAnimation.setRepeatCount(Animation.INFINITE);
		mRotateAnimation.setRepeatMode(Animation.RESTART);
//		mProgressBar.setScaleType(ScaleType.MATRIX);
//		mProgressBar.setImageMatrix(mHeaderImageMatrix);
	}
	
	public void setState(int state) {
		if (state == mState)
			return;

		if (state == STATE_REFRESHING) { // ��ʾ���
			Utils.LOG("a", "state STATE_REFRESHING ==========");
			onLoadingEnd();
			mProgressBar.clearAnimation();
			//mArrowImageView.setVisibility(View.INVISIBLE);
		} else { // ��ʾ��ͷͼƬ
			//mArrowImageView.setVisibility(View.VISIBLE);
			//mProgressBar.setVisibility(View.INVISIBLE);
			Utils.LOG("a", "state other ==========");
		}

		switch (state) {
		case STATE_NORMAL:
			Utils.LOG("a", "start normal ==========");
			if (mState == STATE_READY) { 
				Utils.LOG("a", "current state STATE_READY ==========");
				mProgressBar.startAnimation(mRotateDownAnim);
			}
			if (mState == STATE_REFRESHING) {
				Utils.LOG("a", "current state STATE_REFRESHING ==========");
				mProgressBar.clearAnimation();
				onLoadingEnd();
			}
			mHintTextView.setText(R.string.xlistview_header_hint_normal);
			break;
		case STATE_READY:
			Utils.LOG("a", "start ready ==========");
			if (mState != STATE_READY) {
				Utils.LOG("a", "current state STATE_READY ==========");
				mProgressBar.clearAnimation();
				onLoadingEnd();
				mProgressBar.startAnimation(mRotateUpAnim);
				mHintTextView.setText(R.string.xlistview_header_hint_ready);
				updateTime.setVisibility(View.VISIBLE);
				updateTime.setText(String.format(getResources().getString(R.string.xlistview_header_last_time),
						Utils.formatTime(System.currentTimeMillis())));
			}
			break;
		case STATE_REFRESHING:
			Utils.LOG("a", "start refreshing ==========");
			mHintTextView.setText(R.string.xlistview_header_hint_loading);
			updateTime.setVisibility(View.GONE);
			mProgressBar.clearAnimation();
			onLoadingEnd();
			onLoadingStart();
			break;
		default:
			Utils.LOG("a", "start default ==========");
			onLoadingEnd();
		}

		mState = state;
	}

	public void setVisiableHeight(int height) {
		if (height < 0)
			height = 0;
		LayoutParams lp = (LayoutParams) mContainer
				.getLayoutParams();
		lp.height = height;
		mContainer.setLayoutParams(lp);
	}

	public int getVisiableHeight() {
		return mContainer.getHeight();
	}

}
