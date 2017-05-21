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
		HttpConnectionParams.setConnectionTimeout(httpParams, 10000); // �������ӳ�ʱ
		HttpConnectionParams.setSoTimeout(httpParams, 10000); // ��������ʱ

		HttpClient myHttpClient = new DefaultHttpClient(httpParams); // ����HttpClient����
		HttpGet get = new HttpGet(url);// ����get����

		try {
			HttpResponse response = myHttpClient.execute(get);// ִ��get���󣬵õ���Ӧ���ݰ�
			HttpEntity mHttpEntity = response.getEntity();// ����Ӧ���ݰ��еõ�Enity��Ӧʵ��
			json = EntityUtils.toString(mHttpEntity);// ����Ӧʵ���еõ�json��ʽ���ַ���

		} catch (ClientProtocolException e) {
			return "ClientProtocolException";//��·������δ����(�ͻ��˶˿��쳣)
			
		} catch (IOException e) {
			return "IOException";//��·������δ����(IO�쳣)
		}
		return json;
	}
}
