package com.example.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

public class TextCache {
//	public static final int TIME_DAY = 60 * 60 * 24;
//	private static final int MAX_SIZE = 1000 * 1000 * 50; // 50 mb
//	private static final int MAX_COUNT = Integer.MAX_VALUE; // 不限制存放数据的数量
//	private static Map<String, TextCache> mInstanceMap = new HashMap<String, TextCache>();
	private ACacheManager mCache;

//	public static TextCache get(Context ctx) {
//		return get(ctx, "TextCache");
//	}

//	public static TextCache get(Context ctx, String cacheName) {
//		File f = new File(ctx.getCacheDir(), cacheName);
//		return get(f, MAX_SIZE, MAX_COUNT);
//	}

//	public static TextCache get(File cacheDir) {
//		return get(cacheDir, MAX_SIZE, MAX_COUNT);
//	}

//	public static TextCache get(Context ctx, long max_zise, int max_count) {
//		File f = new File(ctx.getCacheDir(), "TextCache");
//		return get(f, max_zise, max_count);
//	}

//	public static TextCache get(File cacheDir, long max_zise, int max_count) {
//		TextCache manager = mInstanceMap.get(cacheDir.getAbsoluteFile()
//				+ myPid());
//		if (manager == null) {
//			manager = new TextCache(cacheDir, max_zise, max_count);
//			mInstanceMap.put(cacheDir.getAbsolutePath() + myPid(), manager);
//		}
//		return manager;
//	}
	
//	private static String myPid() {
//		return "_" + android.os.Process.myPid();
//	}

	/**  构造方法
	 * @param cacheDir 缓存文件夹
	 * @param max_size 最大缓存
	 * @param max_count
	 */
	public TextCache(File cacheDir, long max_size, int max_count) {
		if (!cacheDir.exists() && !cacheDir.mkdirs()) {
			throw new RuntimeException("can't make dirs in " + cacheDir.getAbsolutePath());
		}
		mCache = new ACacheManager(cacheDir, max_size, max_count);
	}
	
	/**
	 * 保存 String数据 到 缓存中
	 * @param key 保存的key
	 * @param value 保存的String数据
	 */
	public void put(String key, String value) {
		File file = mCache.newFile(key);
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(file), 1024);
			out.write(value);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			mCache.put(file);
		}
	}

	/**
	 * 保存 String数据 到 缓存中，含保存时间
	 * @param key
	 *            保存的key
	 * @param value
	 *            保存的String数据
	 * @param saveTime
	 *            保存的时间，单位：秒
	 */
	public void put(String key, String value, int saveTime) {
		put(key, Utils.newStringWithDateInfo(saveTime, value));
		System.out.println("1111111111111111111111");
	}

	/**
	 * 读取 String数据
	 * @param key
	 * @return String 数据
	 */
	public String getAsString(String key) {
		File file = mCache.get(key);
		if (!file.exists())
			return null;
		boolean removeFile = false;
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(file));
			String readString = "";
			String currentLine;
			while ((currentLine = in.readLine()) != null) {
				readString += currentLine;
			}
			if (!Utils.isDue(readString)) {
				return Utils.clearDateInfo(readString);
			} else {
				removeFile = true;
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (removeFile)
				remove(key);
		}
	}
	// ============ JSONObject 数据 读写 =============
	/**
	 * 保存 JSONObject数据 到 缓存中
	 * @param key
	 *            保存的key
	 * @param value
	 *            保存的JSON数据
	 */
	public void put(String key, JSONObject value) {
		put(key, value.toString());
	}

	/**
	 * 保存 JSONObject数据 到 缓存中，含保存时间
	 * @param key
	 *            保存的key
	 * @param value
	 *            保存的JSONObject数据
	 * @param saveTime
	 *            保存的时间，单位：秒
	 */
	public void put(String key, JSONObject value, int saveTime) {
		put(key, value.toString(), saveTime);
	}

	/**
	 * 读取JSONObject数据
	 * @param key
	 * @return JSONObject数据
	 */
	public JSONObject getAsJSONObject(String key) {
		String JSONString = getAsString(key);
		try {
			JSONObject obj = new JSONObject(JSONString);
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// ============ JSONArray 数据 读写 =============
	/**
	 * 保存 JSONArray数据 到 缓存中
	 * @param key
	 *            保存的key
	 * @param value
	 *            保存的JSONArray数据
	 */
	public void put(String key, JSONArray value) {
		put(key, value.toString());
	}

	/**
	 * 保存 JSONArray数据 到 缓存中
	 * @param key
	 *            保存的key
	 * @param value
	 *            保存的JSONArray数据
	 * @param saveTime
	 *            保存的时间，单位：秒
	 */
	public void put(String key, JSONArray value, int saveTime) {
		put(key, value.toString(), saveTime);
	}

	/**
	 * 读取JSONArray数据
	 * 
	 * @param key
	 * @return JSONArray数据
	 */
	public JSONArray getAsJSONArray(String key) {
		String JSONString = getAsString(key);
		try {
			JSONArray obj = new JSONArray(JSONString);
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取缓存文件
	 * @param key
	 * @return value 缓存的文件
	 */
	public File file(String key) {
		File f = mCache.newFile(key);
		if (f.exists())
			return f;
		return null;
	}

	/**
	 * 移除某个key
	 * @param key
	 * @return 是否移除成功
	 */
	public boolean remove(String key) {
		return mCache.remove(key);
	}

	/**
	 * 清除所有数据
	 */
	public void clear() {
		mCache.clear();
	}
}
