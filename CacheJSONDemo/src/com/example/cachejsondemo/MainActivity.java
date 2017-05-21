package com.example.cachejsondemo;

import java.io.File;
import java.io.UnsupportedEncodingException;

import com.example.utils.SelfHttpConnection;
import com.example.utils.TextCache;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {
	private String url = "http://172.16.1.5:8080/json/jsonText.txt";
	private TextView tv_cache; 
	String json;
	
	File cacheDir;//缓存文件夹
	private static final int MAX_SIZE = 1000 * 1000 * 50; // 50 mb
	private static final int MAX_COUNT = Integer.MAX_VALUE; // 不限制存放数据的数量
	File cacheJson;//缓存文件
	TextCache textCache;//文字缓存类
	
	String TAG = "CahceJson测试";
	
	private Handler handler = new Handler();
	Runnable task = new Runnable() {
		@Override
		public void run() {
			if(json!=null)
				tv_cache.setText(json);
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tv_cache=(TextView)findViewById(R.id.tv_cache);
		new Thread(new Runnable() {
			@Override
			public void run() {
				cacheDir = new File(Environment.getExternalStorageDirectory(),"CacheJson");
				textCache = new TextCache(cacheDir,MAX_SIZE,MAX_COUNT);
//				if(!cacheDir.exists()){
//					cacheDir.mkdirs();
//					Log.i(TAG, "文件创建成功!");
//				}
				cacheJson = new File(cacheDir, "json".hashCode() + ".txt");
				Log.i(TAG, cacheJson.getAbsolutePath());
				if(!cacheJson.exists()){
					
					SelfHttpConnection selfHttpConnection = new SelfHttpConnection();
					json = selfHttpConnection.httpGetRequest(url);
					try {
						//json = new String (json.getBytes(),"gb2312");
						json = new String(json.getBytes("iso8859-1"), "gb2312");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					Log.i(TAG, "http json:"+json);
					textCache.put("json", json);
				}
				else{
					json = textCache.getAsString("json");
					Log.i(TAG, "file json:"+json);
				}
				handler.post(task);
			}
		}).start();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		textCache.clear();
		Log.i(TAG,"缓存已经被清理！");
	}
}
