package com.example.qqslidemenu;

import java.util.concurrent.Executors;

import com.nineoldandroids.animation.FloatEvaluator;
import com.nineoldandroids.animation.IntEvaluator;
import com.nineoldandroids.view.ViewHelper;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class SlideMenu extends FrameLayout {

	private ViewDragHelper vDragHelper;
	private View mMenuView;
	private View mMainView;
	private int width;
	private float range;
	private String tag = "SlideMenu";

	public SlideMenu(Context context) {
		super(context);
		initView();
	}

	public SlideMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public SlideMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	private void initView() {
		vDragHelper = ViewDragHelper.create(this, callback);
		floatEvaluator = new FloatEvaluator();
		intEvaluator = new IntEvaluator();

	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		mMenuView = getChildAt(0);
		mMainView = getChildAt(1);

	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		width = getMeasuredWidth();
		range = 0.6f * width;

	}

	enum State {
		open, close;
	}

	private State mCurrentState = State.close;

	public State getState() {

		return mCurrentState;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return vDragHelper.shouldInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		vDragHelper.processTouchEvent(event);
		return true;
	}

	private ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {

		@Override
		public boolean tryCaptureView(View child, int pointerId) {

			Log.i(tag, "mMenuView");

			return child == mMenuView || child == mMainView;
		}

		@Override
		public int getViewHorizontalDragRange(View child) {
			return (int) range;
		}

		/**
		 * 水平移动范围
		 */
		public int clampViewPositionHorizontal(View child, int left, int dx) {
			if (child == mMainView) {
				if (left < 0) {
					left = 0;
				} else if (left > range) {
					left = (int) range;
				}
			}

			return left;
		};

		/**
		 * 伴随移动
		 */
		@Override
		public void onViewPositionChanged(View changedView, int left, int top,
				int dx, int dy) {
			super.onViewPositionChanged(changedView, left, top, dx, dy);
			if (changedView == mMenuView) {
				mMenuView.layout(0, 0, mMenuView.getMeasuredWidth(),
						mMenuView.getMeasuredHeight());

				mMainView.layout(mMainView.getLeft() + dx, mMainView.getTop(),
						mMainView.getRight() + dx, mMainView.getBottom());

			}

			float ratio = mMainView.getLeft() / range;

			Execute(ratio);

			if (onListMenuChange != null) {
				onListMenuChange.drag(ratio);
			}
		}

		/**
		 * 释放之后，执行的方法
		 */
		@Override
		public void onViewReleased(View releasedChild, float xvel, float yvel) {
			super.onViewReleased(releasedChild, xvel, yvel);
			int center = (int) (range / 2);
			if (releasedChild.getLeft() > center) {
				open();
			} else {
				close();
			}

			if (xvel > 200) {
				open();
			} else if (xvel < -200) {
				close();
			}

		}

	};

	public void close() {
		vDragHelper.smoothSlideViewTo(mMainView, 0, mMainView.getTop());
		ViewCompat.postInvalidateOnAnimation(SlideMenu.this);

		if (onListMenuChange != null ) {
			onListMenuChange.close();
		}

		mCurrentState = State.close;
	}

	public void open() {
		vDragHelper.smoothSlideViewTo(mMainView, (int) range,
				mMainView.getTop());
		ViewCompat.postInvalidateOnAnimation(SlideMenu.this);

		if (onListMenuChange != null) {
			onListMenuChange.open();
		}

		mCurrentState = State.open;
	}

	private FloatEvaluator floatEvaluator;
	private IntEvaluator intEvaluator;

	public void computeScroll() {
		if (vDragHelper.continueSettling(true)) {
			ViewCompat.postInvalidateOnAnimation(SlideMenu.this);
		}
	}

	protected void Execute(float ratio) {

		ViewHelper.setScaleX(mMainView,
				floatEvaluator.evaluate(ratio, 1f, 0.8f));
		ViewHelper.setScaleY(mMainView,
				floatEvaluator.evaluate(ratio, 1f, 0.8f));

		ViewHelper.setTranslationX(mMenuView, intEvaluator.evaluate(ratio,
				-mMenuView.getMeasuredWidth() / 2, 0));

		ViewHelper.setScaleX(mMenuView,
				floatEvaluator.evaluate(ratio, 0.5f, 1f));
		ViewHelper.setScaleY(mMenuView,
				floatEvaluator.evaluate(ratio, 0.5f, 1f));

		ViewHelper
				.setAlpha(mMenuView, floatEvaluator.evaluate(ratio, 0.3f, 1f));

		getBackground().setColorFilter(
				(Integer) ColorUtil.evaluateColor(ratio, Color.BLACK,
						Color.TRANSPARENT), Mode.SRC_OVER);
	};

	private OnListMenuChange onListMenuChange;

	public void setOnListMenuChange(OnListMenuChange onListMenuChange) {
		this.onListMenuChange = onListMenuChange;
	}

	public interface OnListMenuChange {
		public void open();

		public void close();

		public void drag(float ratio);
	}
}
