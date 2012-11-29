package ru.rostpro.netUtils;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import ru.rostpro.exceptions.MException;
import ru.rostpro.gpsnavigation.R;
import ru.rostpro.utils.MLogger;
import android.content.Context;
import android.util.Log;

public class HttpClientWrapper {
	static HttpContext localContext;	
	
	
	public static  String executeGet(Context context,String url) throws  MException {
		HttpGet request = new HttpGet(url);
		HttpResponse responseBody = null;
		try {
			MLogger.console(HttpClientWrapper.class, "URL: " + url);
			HttpClient client = new DefaultHttpClient();
			responseBody = client.execute(request, localContext);	
			HttpEntity entity = responseBody.getEntity();
            //request.abort();			
			return EntityUtils.toString(entity);
		} catch (IOException ee) {
			Log.e(HttpClientWrapper.class.getName(), ee.getMessage(), ee);
			String message = context.getString(R.string.httpErrorBadUrl);
			throw new MException(message, ee);
		}

	}
	public static  String executePost(Context context,String url, List<NameValuePair> args) throws  MException {
		 HttpClient httpclient = new DefaultHttpClient();
		 HttpPost httppost = new HttpPost(url);
		 String responseBody = null;
		 CookieStore cookieStore = new BasicCookieStore();
		 if(localContext == null){
		 localContext = new BasicHttpContext();
		 localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		 }
		 List<NameValuePair> nameValuePairs = args;
		 try {
		   	httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		   	MLogger.console(HttpClientWrapper.class, "URL: " + url);
			 httppost.setEntity(new UrlEncodedFormEntity(args));
		   	HttpResponse response = httpclient.execute(httppost,localContext);
		    HttpEntity entity = response.getEntity();
		    responseBody = EntityUtils.toString(entity);		                
		    MLogger.console(HttpClientWrapper.class,response.getStatusLine().toString());
		    if (entity != null) {
		       	MLogger.console(HttpClientWrapper.class,"Response content length: " + entity.getContentLength());
		    }
		    return responseBody;  
		 } catch (Exception e) {
			 MLogger.console(HttpClientWrapper.class, e.getMessage(), e);
			 //String message = context.getString(R.string.httpErrorBadUrl);
			 throw new MException("internet exception", e);
		 }
		
	}
	
	public static void SimplePost(String url) {
	    // Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(url);

	    try {
	        HttpResponse response = httpclient.execute(httppost);
	       MLogger.console(HttpClientWrapper.class, response.toString());
	    } catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	    }
	}

}
