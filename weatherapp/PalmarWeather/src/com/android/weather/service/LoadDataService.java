package com.android.weather.service;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.android.weather.common.Constant;
import com.android.weather.entity.BaseWeather;
import com.android.weather.entity.Weather;
import com.android.weather.util.ResourceAdapter;
import com.android.weather.util.WeatherUtil;

/**
 * 
 * @author xiajun
 * @version 1.0 加载数据服务
 */
public class LoadDataService extends Service {
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		//WebResource px = new WebResource();
		BaseWeather baseWeather = null;
		String city = intent.getExtras().getString("city");
		boolean check=intent.getExtras().getBoolean("check");
		try {
			if(check){
				city=WeatherUtil.encoderCityName(city);
			 }
			baseWeather = WeatherUtil.getWeatherByCity(city);
		} catch (IllegalArgumentException e) {
			ResourceAdapter.setMessageCode(Constant.CITY_NOT_EXIST);
			return;
		}catch (IOException e) {
			ResourceAdapter.setMessageCode(Constant.NET_LINK_ERROR);;
			return;
		} 
		catch (ParserConfigurationException e) {
			ResourceAdapter.setMessageCode(Constant.NET_LINK_ERROR);;
			return;
		}  
		catch (SAXException e) {
			ResourceAdapter.setMessageCode(Constant.NET_LINK_ERROR);;
			return;
		}
		ArrayList<Weather> weatherList = baseWeather.getWeatherList();
		ResourceAdapter.imgs = new int[weatherList.size()];
		boolean isNight=WeatherUtil.isNight();
		for (int index = 0; index < weatherList.size(); index++) {
			ResourceAdapter.imgs[index] = WeatherUtil.getImageName(weatherList.get(
					index).getCurrently_img(),isNight);
		}
		ResourceAdapter.setBaseWeather(baseWeather);
		ResourceAdapter.setMessageCode(Constant.SUCCESS_FULL);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
