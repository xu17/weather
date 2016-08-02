package com.android.weather.control;

import java.util.ArrayList;
/**
 * 
 * @author xiajun	
 * @version1.0
 */
import java.util.HashMap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import com.android.weather.ui.R;
import com.android.weather.ui.WeatherUI;
public class MenuContorl {
     private ListView listView;
     private WeatherUI weatherUI;
	 public MenuContorl(WeatherUI weatherUI) {
			this.weatherUI=weatherUI;
		}
	public void showUtilListView(){
		   listView=(ListView) weatherUI.findViewById(R.id.util_list);
	       ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();  
	       HashMap<String, Object> map0 = new HashMap<String, Object>();  
	       map0.put("ItemText", weatherUI.getString(R.string.change_city));  
	       listItem.add(map0);  
	       HashMap<String, Object> map1 = new HashMap<String, Object>();  
	       map1.put("ItemText", weatherUI.getString(R.string.sms_send));  
	       listItem.add(map1);  
	       /*HashMap<String, Object> map2 = new HashMap<String, Object>();  
	       map2.put("ItemText", weatherUI.getString(R.string.cms_send));  
	       listItem.add(map2);*/  
	       HashMap<String, Object> map3 = new HashMap<String, Object>();  
	       map3.put("ItemText", weatherUI.getString(R.string.use_hint));  
	       listItem.add(map3);  
	       HashMap<String, Object> map4 = new HashMap<String, Object>();  
	       map4.put("ItemText", weatherUI.getString(R.string.cancel));  
	       listItem.add(map4); 
	     //生成适配器的Item和动态数组对应的元素  
	       SimpleAdapter listItemAdapter = new SimpleAdapter(weatherUI,listItem,//数据源   
	           R.layout.util_list_view,//ListItem的XML实现  
	           //动态数组与ImageItem对应的子项          
	           new String[] {"ItemText"},   
	           //ImageItem的XML文件里面的一个ImageView,两个TextView ID  
	           new int[] {R.id.ItemText}  
	       );  
	       listView.setAdapter(listItemAdapter) ; 
	       listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {
				switch(index){
				case 0:
					new CityContorl(weatherUI).showCityDialog();
					listView.setVisibility(View.GONE);
					break;
				case 1:
					new SMSContorl(weatherUI).showSMSDialog();
					listView.setVisibility(View.GONE);
					break;
				/*case 2:
					listView.setVisibility(View.GONE);
                    break;*/
				case 2:
					BaseContorl.doShowExplainDialog(weatherUI, R.string.explain, LayoutInflater.from(weatherUI).inflate(R.layout.explain_dialog, null));
					listView.setVisibility(View.GONE);
					break;
				case 3:
					listView.setVisibility(View.GONE);
					break;
				}
			}  
	       });  
	       listView.setVisibility(View.VISIBLE);
	   }
	   
}
