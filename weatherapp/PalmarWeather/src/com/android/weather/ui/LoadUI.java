package com.android.weather.ui;

import java.util.Timer;
import java.util.TimerTask;

import android.R.anim;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.weather.common.Constant;
import com.android.weather.service.LoadDataService;
import com.android.weather.util.GIFView;
import com.android.weather.util.ResourceAdapter;
import com.android.weather.util.WeatherUtil;

/**
 * 
 * @author xiajun
 * @version1.0 初始化UI
 * 
 */
public class LoadUI extends Activity {
	private Intent intent;
	private Timer timer = new Timer();
	/*private ImageView dot_red;
	private ImageView dot_blue;
	private ImageView dot_white;
	private ImageView dot_yellow;
	private ImageView dot_green;*/
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.load);
	/*	dot_red=(ImageView) this.findViewById(R.id.dot_red);
		dot_blue=(ImageView) this.findViewById(R.id.dot_blue);
		dot_white=(ImageView) this.findViewById(R.id.dot_white);
		dot_yellow=(ImageView) this.findViewById(R.id.dot_yellow);
		dot_green=(ImageView) this.findViewById(R.id.dot_green);*/
		GIFView gif = new GIFView(LoadUI.this, R.drawable.progress);
		((RelativeLayout) findViewById(R.id.progress)).addView(gif);
		timer.schedule(task, 800);
	}
     TimerTask task = new TimerTask() {
	    @Override
	    public void run() {
	    	intent = new Intent(LoadUI.this, LoadDataService.class);
			Bundle bundle = new Bundle();
			bundle.putString("city", WeatherUtil.getDefaultCity(LoadUI.this));
			bundle.putBoolean("check", false);
			intent.putExtras(bundle);
			LoadUI.this.startService(LoadUI.this.intent);
			while (true) {
				if (ResourceAdapter.getMessageCode() != 0) {
					handler.sendEmptyMessage(0);
					break;
				}
			}
	    }
	   };
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (ResourceAdapter.getMessageCode() == Constant.NET_LINK_ERROR) {
				Toast.makeText(LoadUI.this, getString(R.string.net_error),
						Toast.LENGTH_LONG).show();
			}
			if (ResourceAdapter.getMessageCode() == Constant.SUCCESS_FULL) {
				timer.cancel();
				Intent weatherIntent = new Intent();
				weatherIntent.setClass(LoadUI.this, WeatherUI.class);
				LoadUI.this.startActivity(weatherIntent);
				LoadUI.this.stopService(intent);
				LoadUI.this.overridePendingTransition(anim.slide_in_left, anim.slide_out_right);
				LoadUI.this.finish();
			}
		}
	};
}
