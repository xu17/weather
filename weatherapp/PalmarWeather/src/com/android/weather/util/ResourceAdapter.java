package com.android.weather.util;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import com.android.weather.entity.BaseWeather;
import com.android.weather.entity.Weather;

/**
 * 
 * @author xiajun
 * @version 1.0
 * 资源设配器
 */
public class ResourceAdapter extends BaseAdapter {
	private Context context;
	public static int imgs[];
	private static BaseWeather baseWeather;
	private static int messageCode;
	public static int getMessageCode() {
		return messageCode;
	}

	public static void setMessageCode(int messageCode) {
		ResourceAdapter.messageCode = messageCode;
	}

	public ResourceAdapter(Context context){
		this.context = context;
	}

	public static void setBaseWeather(BaseWeather weather) {
		baseWeather = weather;
	}

	public static BaseWeather getBaseWeather() {
		return baseWeather;
	}

	public int getCount() {
		return imgs.length;
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Weather getWeatherByIndex(int index) {
		return baseWeather.getWeatherList().get(index);
	}

	public View getView(int index, View arg1, ViewGroup arg2) {
		ImageView imageview = new ImageView(context);
		/* 设置图片给imageView 对象*/
		imageview.setImageResource(imgs[index]);
		/* 重新设置图片的宽高*/
		imageview.setScaleType(ImageView.ScaleType.FIT_XY);
		/* 重新设置Layout 的宽高*/
		imageview.setLayoutParams(new Gallery.LayoutParams(87, 60));
		/* 设置Gallery 背景图*/
		return imageview;
	}
}
