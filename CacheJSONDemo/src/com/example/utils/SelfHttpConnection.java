package com.example.utils;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import android.util.Log;

public class SelfHttpConnection {
	String json = null;

	public String httpGetRequest(String url) {
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 10000); // 设置连接超时
		HttpConnectionParams.setSoTimeout(httpParams, 10000); // 设置请求超时

		HttpClient myHttpClient = new DefaultHttpClient(httpParams); // 生成HttpClient对象
		HttpGet get = new HttpGet(url);// 创建get请求

		try {
			HttpResponse response = myHttpClient.execute(get);// 执行get请求，得到响应数据包
			HttpEntity mHttpEntity = response.getEntity();// 从响应数据包中得到Enity响应实体
			json = EntityUtils.toString(mHttpEntity);// 从响应实体中得到json格式的字符串

		} catch (ClientProtocolException e) {
			return "ClientProtocolException";//网路服务器未开启(客户端端口异常)
			
		} catch (IOException e) {
			return "IOException";//网路服务器未开启(IO异常)
		}
		return json;
	}
}
