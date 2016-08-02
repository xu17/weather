package com.android.weather.control;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import com.android.weather.common.Constant;
import com.android.weather.entity.BaseWeather;
import com.android.weather.entity.Weather;
import com.android.weather.ui.R;
import com.android.weather.ui.WeatherUI;
import com.android.weather.util.ResourceAdapter;
import com.android.weather.util.WeatherUtil;

public class SMSContorl {
    private View sms_view;
    private WeatherUI weatherUI;
	 private EditText number;
	 private EditText message;
	 private ProgressDialog progressDialog;
	 private Button send_but;
	 private Button cancel;
	 private AlertDialog sms_dialog;
	 public SMSContorl(WeatherUI weatherUI) {
			this.weatherUI=weatherUI;
		}
	 public void showSMSDialog(){
		   sms_dialog = new AlertDialog.Builder(weatherUI).create();
		   sms_view=LayoutInflater.from(weatherUI).inflate(R.layout.sms_dialog, null);
		   sms_dialog.setView(sms_view);
		   sms_dialog.setTitle(R.string.sms_send);
		   cancel=(Button) sms_view.findViewById(R.id.cancel_but);
		   send_but=(Button) sms_view.findViewById(R.id.send_but);
		   send_but.setOnClickListener(new Button.OnClickListener(){
				public void onClick(View arg0) {
					 progressDialog = ProgressDialog.show(weatherUI, "",
								weatherUI.getString(R.string.sending), true);
					new SendSMSListener().start();
				}});
		    cancel.setOnClickListener(new Button.OnClickListener(){
				public void onClick(View arg0) {
					sms_dialog.dismiss();
				}});
			sms_dialog.show();
			message=(EditText) sms_view.findViewById(R.id.message);
		    number=(EditText) sms_view.findViewById(R.id.number);
		   
		    message.setText(getMessage());
		  final ListView number_list=(ListView) sms_view.findViewById(R.id.number_list);
		  number.setOnClickListener(new EditText.OnClickListener(){
			public void onClick(View arg0) {
				  SimpleAdapter listItemAdapter = new SimpleAdapter(weatherUI,WeatherUtil.getPhoneList(weatherUI), R.layout.number_list,new String[] {"name", "number"},   
				           new int[] {R.id.name,R.id.number}) ; 
				  number_list.setAdapter(listItemAdapter);
				  number_list.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> arg0, View view,
							int index, long arg3) {
						number.setText(((TextView)view.findViewById(R.id.number)).getText());
					    number_list.setVisibility(View.GONE);

					}});
				  number_list.setVisibility(View.VISIBLE);
			}});
		 
	   }
	private String getMessage() {
		BaseWeather baseWeather=ResourceAdapter.getBaseWeather();
		StringBuilder message = new StringBuilder();
		message.append(baseWeather.getCityName()).append("\n");
		for(Weather w:baseWeather.getWeatherList()){
			message.append(w.getCurrently_date()).append("  ").append(w.getCurrently_week()).append("\n");
			message.append(weatherUI.getString(R.string.min_t)).append(":").append(w.getTemperature_min()).append(Constant.T_SIGN).append(" ");
			message.append(weatherUI.getString(R.string.max_t)).append(":").append(w.getTemperature_max()).append(Constant.T_SIGN).append(" ");
			message.append(w.getCurrently_hint()).append("\n");
		}
		return message.toString();
	}
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if(msg.getData().getBoolean("isSuccess")){
				Toast.makeText(weatherUI, weatherUI.getString(R.string.send_success),
						Toast.LENGTH_LONG).show();
				sms_dialog.dismiss();
				progressDialog.dismiss();
			}else{
				Toast toast=Toast.makeText(weatherUI,weatherUI.getString(R.string.send_faild),
						Toast.LENGTH_LONG);
						toast.setGravity(Gravity.LEFT | Gravity.TOP, sms_view.getScrollX()+80, sms_view.getScrollY()+140);
						toast.show();
						progressDialog.dismiss();
			}
		}
	};
	private class SendSMSListener extends Thread {

		@Override
		public void run() {
			boolean flag=WeatherUtil.sendSMS(number.getText().toString(), message.getText().toString(), weatherUI);
			Bundle bundle=new Bundle();
			bundle.putBoolean("isSuccess", flag);
			Message message=new Message();
			message.setData(bundle);
			handler.sendMessage(message);
		}

	}
}
