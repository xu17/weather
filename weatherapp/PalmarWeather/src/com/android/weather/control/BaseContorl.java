package com.android.weather.control;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;

import com.android.weather.ui.R;
/**
 * 
 * @author xiajun	
 * @version1.0
 */
public  class BaseContorl {
	  public static void doShowWarnDialog(Context context,Integer message,int title,View view){
			final AlertDialog.Builder warn_dialog = new AlertDialog.Builder(context);
			warn_dialog.setMessage(context.getString(message));
			warn_dialog.setIcon(R.drawable.icon_alert);
			warn_dialog.setTitle(title);
			warn_dialog.setPositiveButton(context.getString(R.string.ok), new OnClickListener(){

				public void onClick(DialogInterface arg0, int arg1) {
	                System.exit(0);						
				}});
			warn_dialog.setNegativeButton(context.getString(R.string.cancel), new OnClickListener(){

				public void onClick(DialogInterface arg0, int arg1) {
					warn_dialog.create().dismiss();
					
				}});
			warn_dialog.show();
	   }
	  public static void doShowExplainDialog(Context context,int title,View view){
			final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
			dialog.setView(view);
			dialog.setIcon(R.drawable.icon_alert);
			dialog.setTitle(title);
			dialog.setPositiveButton(context.getString(R.string.ok), new OnClickListener(){
				public void onClick(DialogInterface arg0, int arg1) {
				}});
			dialog.show();
	   }
}
