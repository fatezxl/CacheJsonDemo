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
//	private static final int MAX_COUNT = Integer.MAX_VALUE; // �����ƴ�����ݵ�����
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

	/**  ���췽��
	 * @param cacheDir �����ļ���
	 * @param max_size ��󻺴�
	 * @param max_count
	 */
	public TextCache(File cacheDir, long max_size, int max_count) {
		if (!cacheDir.exists() && !cacheDir.mkdirs()) {
			throw new RuntimeException("can't make dirs in " + cacheDir.getAbsolutePath());
		}
		mCache = new ACacheManager(cacheDir, max_size, max_count);
	}
	
	/**
	 * ���� String���� �� ������
	 * @param key �����key
	 * @param value �����String����
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
	 * ���� String���� �� �����У�������ʱ��
	 * @param key
	 *            �����key
	 * @param value
	 *            �����String����
	 * @param saveTime
	 *            �����ʱ�䣬��λ����
	 */
	public void put(String key, String value, int saveTime) {
		put(key, Utils.newStringWithDateInfo(saveTime, value));
		System.out.println("1111111111111111111111");
	}

	/**
	 * ��ȡ String����
	 * @param key
	 * @return String ����
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
	// ============ JSONObject ���� ��д =============
	/**
	 * ���� JSONObject���� �� ������
	 * @param key
	 *            �����key
	 * @param value
	 *            �����JSON����
	 */
	public void put(String key, JSONObject value) {
		put(key, value.toString());
	}

	/**
	 * ���� JSONObject���� �� �����У�������ʱ��
	 * @param key
	 *            �����key
	 * @param value
	 *            �����JSONObject����
	 * @param saveTime
	 *            �����ʱ�䣬��λ����
	 */
	public void put(String key, JSONObject value, int saveTime) {
		put(key, value.toString(), saveTime);
	}

	/**
	 * ��ȡJSONObject����
	 * @param key
	 * @return JSONObject����
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

	// ============ JSONArray ���� ��д =============
	/**
	 * ���� JSONArray���� �� ������
	 * @param key
	 *            �����key
	 * @param value
	 *            �����JSONArray����
	 */
	public void put(String key, JSONArray value) {
		put(key, value.toString());
	}

	/**
	 * ���� JSONArray���� �� ������
	 * @param key
	 *            �����key
	 * @param value
	 *            �����JSONArray����
	 * @param saveTime
	 *            �����ʱ�䣬��λ����
	 */
	public void put(String key, JSONArray value, int saveTime) {
		put(key, value.toString(), saveTime);
	}

	/**
	 * ��ȡJSONArray����
	 * 
	 * @param key
	 * @return JSONArray����
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
	 * ��ȡ�����ļ�
	 * @param key
	 * @return value ������ļ�
	 */
	public File file(String key) {
		File f = mCache.newFile(key);
		if (f.exists())
			return f;
		return null;
	}

	/**
	 * �Ƴ�ĳ��key
	 * @param key
	 * @return �Ƿ��Ƴ��ɹ�
	 */
	public boolean remove(String key) {
		return mCache.remove(key);
	}

	/**
	 * �����������
	 */
	public void clear() {
		mCache.clear();
	}
}
