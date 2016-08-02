package com.android.weather.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

import com.android.weather.common.Constant;
import com.android.weather.entity.BaseWeather;
import com.android.weather.service.XmlParse;
import com.android.weather.ui.R;
 
/**
 * 
 * @author xiajun
 * @version 1.0
 */
public class WeatherUtil {

	public static String getCurrentlyDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(new Date());
	}

	public static String getNextDate(String dateTime) {
		String[] date = dateTime.split("-");
		int day = Integer.valueOf(date[2]);
		int month = Integer.valueOf(date[1]);
		int year = Integer.valueOf(date[0]);
		StringBuffer buffer = new StringBuffer();
		if (Constant.MONTH_MAP.get(month)==(day)) {
			day = 1;
			month++;
			if (month == 13) {
				month = 1;
				year++;
			}
		} else {
			day++;
		}
		buffer.append(year).append("-");
		if (month < 10) {
			buffer.append("0");
		}
		buffer.append(month).append("-");
		if (day < 10) {
			buffer.append("0");
		}
		buffer.append(day);
		return buffer.toString();
	}

	public static int getImageName(String imageUrl, boolean isNight) {
		String[] str = imageUrl.split("/");
		if (isNight) {
			if (Constant.NIGHT_IMAGE_MAP.containsKey(str[str.length - 1])) {
				return Constant.NIGHT_IMAGE_MAP.get(str[str.length - 1]);
			} else {
				return Constant.DEFAULT_IMG;
			}
		} else {
			if (Constant.DAY_IMAGE_MAP.containsKey(str[str.length - 1])) {
				return Constant.DAY_IMAGE_MAP.get(str[str.length - 1]);
			} else {
				return Constant.DEFAULT_IMG;
			}
		}

	}

	public static String getPhoneNumber(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String phoneNumber = tm.getLine1Number();
		return phoneNumber;
	}

	public static boolean isNight() {
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		if (hour >= 20 || hour <= 7) {
			return true;
		}
		return false;
	}

	public static String encoderCityName(String city) {
		try {
			city = URLEncoder.encode(city, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return city;
	}

	public static void setDefaultCity(Context context, String city) {
		OutputStream out = null;
		try {
			out = context.openFileOutput("weather.cfg", Context.MODE_PRIVATE);
			Properties properties = new Properties();
			properties.setProperty("city", encoderCityName(city));
			properties.store(out, "");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static String getDefaultCity(Context context) {
		InputStream in;
		try {
			in = context.openFileInput("weather.cfg");
		} catch (FileNotFoundException e1) {
			return encoderCityName(context.getString(R.string.defulat_city));
		}
		Properties properties = new Properties();
		try {
			properties.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (String) properties.get("city");
	}
	
	public static ArrayList<HashMap<String, String>> getPhoneList(Context context){
		
		ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();     
		 Cursor cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,null, null, null, null);
		   while (cursor.moveToNext())    
	        {     
	         HashMap<String, String> map = new HashMap<String, String>();
	         String phoneName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
	         map.put("name", phoneName);//电话姓名
	         String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));   
	            String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));   
	            
	            if (hasPhone.compareTo("1") == 0)    
	            {   
	                Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId,null, null);        
	                while (phones.moveToNext())    
	                {      
	                    String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));       
	                    map.put("number", phoneNumber); // 多个号码如何处理
	                    
	                }        
	                phones.close();       
	            }  
	            listItem.add(map);
	        }
         return listItem;

	}

	public static  boolean sendSMS(String number,String message,Activity activity){
		  SmsManager smsManager = SmsManager.getDefault();  
		  try{
		  PendingIntent mPI = PendingIntent.getBroadcast(activity, 0, new Intent(), 0);  
          smsManager.sendTextMessage(number, null, message, mPI, null);  
		  }catch(Exception e){
			  e.printStackTrace();
			  return false;
		  }
		  return true;
	}
	
	 public static BaseWeather getWeatherByCity(String city) throws ParserConfigurationException, SAXException, IOException {
	        
         SAXParserFactory spf = SAXParserFactory.newInstance();
         SAXParser sp = spf.newSAXParser();
         XMLReader reader = sp.getXMLReader();
         XmlParse  handler = new XmlParse();
         reader.setContentHandler(handler);            
         URL url = new URL(Constant.GOOGLE_WEATHER_URL_CN+city);
         InputStream is = url.openStream();
         InputStreamReader isr = new InputStreamReader(is,"GB2312");
         InputSource source = new InputSource(isr);
         reader.parse(source);
         return handler.getBaseWeather();
  }
}
