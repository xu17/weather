package com.android.weather.entity;

import java.util.ArrayList;

/**
 * 
 * @author xiajun
 * @version 1.0
 */
public class BaseWeather {
	private String cityName;
	private ArrayList<Weather> weatherList;

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public ArrayList<Weather> getWeatherList() {
		return weatherList;
	}

	public void setWeatherList(ArrayList<Weather> weatherList) {
		this.weatherList = weatherList;
	}
}
