package com.example.qqslidemenu;

import com.example.qqslidemenu.SlideMenu.OnListMenuChange;
import com.nineoldandroids.animation.FloatEvaluator;
import com.nineoldandroids.animation.IntEvaluator;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.CycleInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private ListView main_listview;
	private ListView menu_listview;
	private ImageView iv_main_head;
	private ImageView iv_menu_head;
	private SlideMenu slideMenu;
	private String tag="MainActivity";
	private FloatEvaluator floatEvaluator;
	private IntEvaluator intEvaluator;
	private MyLinearLayout my_layout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		initView();
		initData();
	}

	private void initData() {
		menu_listview.setAdapter(new ArrayAdapter<String>(getApplicationContext(), 
				android.R.layout.simple_list_item_1, Constant.sCheeseStrings){
			
			@Override
			public View getView(int position, View convertView,
					ViewGroup parent) {
				
				TextView view=(TextView) super.getView(position, convertView, parent);
				view.setTextColor(Color.WHITE);

				return view;
			}
		});
		
		main_listview.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
				android.R.layout.simple_list_item_1, Constant.NAMES){
			@Override
			public View getView(int position, View convertView,
					ViewGroup parent) {

				TextView view=(TextView) ((convertView==null)?super.getView(position, convertView, parent):convertView);
				view.setTextColor(Color.BLACK);
				
				return view;
			}
		});
		
		slideMenu.setOnListMenuChange(new OnListMenuChange() {
			
	

			@Override
			public void open() {
				Toast.makeText(getApplicationContext(), "菜单打开了", 0).show();
			
			}
			
			@Override
			public void drag(float ratio) {
				Log.i(tag, "ratio:"+ratio);
				
				ViewHelper.setAlpha(iv_main_head, floatEvaluator.evaluate(ratio, 1f, 0f));
				
				
			}
			
			@Override
			public void close() {
				Toast.makeText(getApplicationContext(), "菜单关闭了", 0).show();
				
				ViewPropertyAnimator.animate(iv_main_head).translationXBy(15)
				.setInterpolator(new CycleInterpolator(4))
				.setDuration(500)
				.start();
			}
		});
		
		my_layout.setSlideMenu(slideMenu);
	}

	private void initView() {
		floatEvaluator = new FloatEvaluator();
		intEvaluator = new IntEvaluator();
		
		main_listview = (ListView) findViewById(R.id.main_listview);
		menu_listview = (ListView) findViewById(R.id.menu_listview);
		iv_main_head = (ImageView) findViewById(R.id.iv_main_head);
		iv_menu_head = (ImageView) findViewById(R.id.iv_menu_head);
		slideMenu = (SlideMenu) findViewById(R.id.slideMenu);
		my_layout = (MyLinearLayout) findViewById(R.id.my_layout);
		
	}
}
