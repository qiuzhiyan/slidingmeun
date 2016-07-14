package com.example.qqslidemenu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class MyLinearLayout extends LinearLayout {

	public MyLinearLayout(Context context) {
		super(context);
	}

	public MyLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@SuppressLint("NewApi")
	public MyLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	private SlideMenu slideMenu;

	public void setSlideMenu(SlideMenu slideMenu) {
		this.slideMenu = slideMenu;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (slideMenu != null && slideMenu.getState() == SlideMenu.State.open) {

			return true;
		}

		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (slideMenu != null && slideMenu.getState() == SlideMenu.State.open) {

			slideMenu.close();
		}
		return super.onTouchEvent(event);
	}
}
