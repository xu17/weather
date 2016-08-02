package com.android.weather.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import com.android.weather.common.Constant;
import com.android.weather.control.BaseContorl;
import com.android.weather.control.MenuContorl;
import com.android.weather.util.ResourceAdapter;
import com.android.weather.util.WeatherUtil;

public class WeatherUI extends Activity implements ViewSwitcher.ViewFactory {
	private Gallery gallery;
	private ImageSwitcher imageview;
	private ResourceAdapter adapter = null;
	private TextView city_info;
	private TextView week_info;
	private TextView date_info;
	private TextView hint_info;
	private TextView temperature_view;
	private Button util_but;
	private Button exit_but;
	private TextView currently_time;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather);
		adapter = new ResourceAdapter(this);
		city_info = (TextView) findViewById(R.id.city);
		week_info = (TextView) findViewById(R.id.week);
		date_info = (TextView) findViewById(R.id.date);
		hint_info = (TextView) findViewById(R.id.hint);
		temperature_view = (TextView) findViewById(R.id.temperature);
		gallery = (Gallery) findViewById(R.id.gally);
		imageview = (ImageSwitcher) findViewById(R.id.photo);
		imageview.setFactory(this);
		imageview.setInAnimation(AnimationUtils.loadAnimation(this,
				    android.R.anim.slide_in_left));
		imageview.setOutAnimation(AnimationUtils.loadAnimation(this,
				    android.R.anim.slide_out_right));
		util_but = (Button) findViewById(R.id.menu_but);
		currently_time = (TextView) findViewById(R.id.currently_time);
		exit_but = (Button) findViewById(R.id.exit_but);
		exit_but.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {
				BaseContorl.doShowWarnDialog(WeatherUI.this,
						R.string.exit_warn, R.string.hint, null);
			}
		});
		util_but.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {
				new MenuContorl(WeatherUI.this).showUtilListView();
			}
		});
		// this.onKeyDown(KeyEvent.KEYCODE_CALL, KeyEvent.)
		gallery.setAdapter(adapter);
		initView(0); 
		/* 设置Gallery的点击事件监听器 */
		gallery.setOnItemSelectedListener(new Gallery.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View v,
					int index, long id) {
				initView(index);
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});
		new ShowTime().start();
	}

	public void initView(int index) {
		city_info.setText(ResourceAdapter.getBaseWeather().getCityName());
		week_info.setText(adapter.getWeatherByIndex(index).getCurrently_week());
		date_info.setText(adapter.getWeatherByIndex(index).getCurrently_date());
		hint_info.setText(adapter.getWeatherByIndex(index).getCurrently_hint());
		StringBuilder info = new StringBuilder();
		info.append(WeatherUI.this.getString(R.string.min_t)).append(": ")
				.append(adapter.getWeatherByIndex(index).getTemperature_min())
				.append(Constant.T_SIGN).append("   ").append(
						WeatherUI.this.getString(R.string.max_t)).append(": ")
				.append(adapter.getWeatherByIndex(index).getTemperature_max())
				.append(Constant.T_SIGN);
		temperature_view.setText(info);
		info.delete(0, info.length());  
		imageview.setImageResource(ResourceAdapter.imgs[index]);
	}
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			currently_time.setText(WeatherUtil.getCurrentlyDate());
		 }
		};
	private class ShowTime extends Thread {
		public void run() {
			while (true) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				handler.sendEmptyMessage(0);
			}
		}
	}
	public View makeView() {
	    ImageView img = new ImageView(this); 
        return img; 

	}

}