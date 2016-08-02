package com.android.weather.util;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
public class GIFView extends View implements Runnable {
	private Bitmap bmb;
	private GIFDecode decode;

	public GIFView(Context context,int img) {
		super(context);
		decode = new GIFDecode();
		InputStream in = this.getResources().openRawResource(img);
		decode.read(in);
		//decode.
		bmb = decode.getFrame(0);
		Thread t = new Thread(this);
		t.start();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		canvas.drawBitmap(bmb, 0, 0, new Paint());
		bmb = decode.next();
	}

	public void run() {
		while (true) {
			try {
				this.postInvalidate();
				Thread.sleep(100);

			} catch (Exception ex) {

			}
		}

	}

}
