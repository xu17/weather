package com.android.weather.control;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.android.weather.common.Constant;
import com.android.weather.service.LoadDataService;
import com.android.weather.ui.R;
import com.android.weather.ui.WeatherUI;
import com.android.weather.util.ResourceAdapter;
import com.android.weather.util.WeatherUtil;
/**
 * 
 * @author xiajun	
 * @version1.0
 */
public class CityContorl extends BaseContorl{
	private WeatherUI weatherUI;
	private Button opt_full;
	private Button cancel_but;
	private EditText city_name;
	private AlertDialog city_dialog;
	private LayoutInflater inflater;
	private View city_view;
	private Intent intent;
	private LinearLayout eror_layout;
	private ProgressDialog progressDialog;
	private CheckBox set_checkBox;
	public void showCityDialog() {
		city_dialog = new AlertDialog.Builder(weatherUI).create();
		inflater = LayoutInflater.from(weatherUI);
		city_view = inflater.inflate(R.layout.city_dialog, null);
		city_dialog.setView(city_view);
		city_dialog.setIcon(R.drawable.weather_icon);
		city_dialog.setTitle(R.string.input_city_hint);
		city_name = (EditText) city_view.findViewById(R.id.city_name);
		opt_full = (Button) city_view.findViewById(R.id.opt_full);
		opt_full.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) { 
				if (checkParam()) {
					progressDialog = ProgressDialog.show(weatherUI, "",
							weatherUI.getString(R.string.loading), true);
					eror_layout.removeAllViews();
					intent = new Intent(weatherUI, LoadDataService.class);
					Bundle bundle = new Bundle();
					bundle.putString("city", city_name.getText().toString()
							.trim());
					bundle.putBoolean("check", true);
					intent.putExtras(bundle);
					ResourceAdapter.setBaseWeather(null);
					ResourceAdapter.setMessageCode(0);
					weatherUI.startService(intent);
					new ListenerProgress().start();
				}
			}
		});
		set_checkBox = (CheckBox) city_view.findViewById(R.id.set_checkbox);
		cancel_but = (Button) city_view.findViewById(R.id.cancel_but);
		cancel_but.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				city_view=null;
				city_dialog.dismiss();
			}
		});
		eror_layout = ((LinearLayout) city_view.findViewById(R.id.error_img));
		city_dialog.show();
	}

	private boolean checkParam() {
		if (city_name.getText().toString().trim().equals("")) {
			Toast.makeText(weatherUI, weatherUI.getString(R.string.can_not_null),
					Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			progressDialog.dismiss();
			if (ResourceAdapter.getMessageCode() == Constant.CITY_NOT_EXIST) {
				ImageView error_img = new ImageView(weatherUI);
				error_img.setImageResource(R.drawable.error);
				eror_layout.addView(error_img);
				Toast toast=Toast.makeText(weatherUI,weatherUI.getString(R.string.city_error),
						Toast.LENGTH_LONG);
				toast.setGravity(Gravity.LEFT | Gravity.TOP, city_view.getScrollX()+50, city_view.getScrollY()+140);
				toast.show();
				weatherUI.stopService(intent);
			}
			if (ResourceAdapter.getMessageCode() == Constant.NET_LINK_ERROR) {
				ImageView error_img = new ImageView(weatherUI);
				error_img.setImageResource(R.drawable.error);
				eror_layout.addView(error_img);
				Toast.makeText(weatherUI, weatherUI.getString(R.string.net_error),
						Toast.LENGTH_LONG).show();
				weatherUI.stopService(intent);
			}
			if (ResourceAdapter.getMessageCode() == Constant.SUCCESS_FULL) {
				weatherUI.initView(0);
				city_dialog.dismiss();
				if (set_checkBox.isChecked()) {
					WeatherUtil.setDefaultCity(weatherUI, city_name.getText()
							.toString().trim());
					Toast.makeText(weatherUI, weatherUI.getString(R.string.config_success),Toast.LENGTH_LONG).show();
				}
			}
		}
	};

	private class ListenerProgress extends Thread {

		@Override
		public void run() {
			while (true) {
				if (ResourceAdapter.getMessageCode() != 0) {
					handler.sendEmptyMessage(0);
					break;
				}

			}
		}

	}

	
	public  CityContorl(WeatherUI weatherUI) {
		this.weatherUI=weatherUI;
	}
}
