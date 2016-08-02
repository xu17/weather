/*package com.android.weather.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import com.android.weather.common.Constant;
import com.android.weather.entity.BaseWeather;
import com.android.weather.entity.Weather;
import com.android.weather.util.WeatherUtil;

*//**
 * 
 * @author xiajun
 * @version 1.0 获取网络资源
 * 
 *//*
@SuppressWarnings("unchecked")
public class WebResource { 
	public String getFirstDate(Document document) {
		Node node = document.selectSingleNode(Constant.FIRST_DATE_PATH);
		return node.getText().trim();
	}

	public int getDay(Document document) {
		String[] date = getFirstDate(document).split("-");
		int day = Integer.valueOf(date[2]);
		return day;
	}

	public int getMonth(Document document) {
		String[] date = getFirstDate(document).split("-");
		int month = Integer.valueOf(date[1]);
		return month;
	}

	public int getYear(Document document) {
		String[] date = getFirstDate(document).split("-");
		int year = Integer.valueOf(date[0]);
		return year;
	}

	public String getCityName(Document document) {
		Node cityNode = document.selectSingleNode(Constant.CITY_NAME_PATH);
		return cityNode.getText().trim();
	}
    
	public BaseWeather getWeatherFromXML(String city) throws IOException, DocumentException {
		BaseWeather baseWeather = new BaseWeather();
		ArrayList<Weather> weatherList = new ArrayList<Weather>();
		Document document = getDocumentByUrl(Constant.GOOGLE_WEATHER_URL_CN+city);
		if(document==null){
			throw new IOException("network connection has fialed!");
		}
		Node cityError = document.selectSingleNode(Constant.ERROR_PATH);
		if (cityError != null) {
			throw new IllegalArgumentException("the city is not existed!");
		}
		int day = getDay(document) - 1;
		int month = getMonth(document);
		int year = getYear(document);
		List<Node> t_min_list = document
				.selectNodes(Constant.TEMPERATURE_MIN_PATH);
		List<Node> t_max_list = document
				.selectNodes(Constant.TEMPERATURE_MAX_PATH);
		List<Node> weather_img_list = document
				.selectNodes(Constant.WEATHER_IMAGE_PATH);
		List<Node> day_week_list = document.selectNodes(Constant.DAY_WEEK_PATH);
		List<Node> condition_list = document
				.selectNodes(Constant.CONDITION_PATH);
		Node cityNode = document.selectSingleNode(Constant.CITY_NAME_PATH);
		baseWeather.setCityName(cityNode.getText().trim());
		for (int index = 0; index < t_min_list.size(); index++) {
			Weather weather = new Weather();
			Attribute attr_t_min = (Attribute) t_min_list.get(index);
			Attribute attr_t_max = (Attribute) t_max_list.get(index);
			Attribute attr_img = (Attribute) weather_img_list.get(index);
			Attribute attr_week = (Attribute) day_week_list.get(index);
			Attribute attr_hint = (Attribute) condition_list.get(index);
			weather.setCurrently_hint(attr_hint.getText());
			weather.setTemperature_min(attr_t_min.getText());
			weather.setTemperature_max(attr_t_max.getText());
			weather.setCurrently_img(attr_img.getText());
			weather.setCurrently_week(attr_week.getText());
			//weather.setCurrently_date(Util.getNextDate(year, month, day++));
			weatherList.add(weather);
		}
		baseWeather.setWeatherList(weatherList);
		return baseWeather;
	}

	public Document getDocumentByUrl(String HttpUrl) throws IOException, DocumentException {
		SAXReader saxReader = new SAXReader();
		Document document = null;
		URL url = new URL(HttpUrl);
		URLConnection conn = url.openConnection();
		StringBuffer xml = new StringBuffer();
		InputStream is = conn.getInputStream();
		InputStreamReader isr = new InputStreamReader(is, "GB2312");
		BufferedReader br = new BufferedReader(isr);
		String str = null;
		while ((str = br.readLine()) != null) {
			xml.append(str);
		}
		br.close();
		document = saxReader.read(new StringReader(xml.toString()));
		return document;
	}
}
*/