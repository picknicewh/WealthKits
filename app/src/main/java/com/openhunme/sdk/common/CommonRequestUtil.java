package com.openhunme.sdk.common;

//import android.util.Log;

import com.openhunme.sdk.data.bean.ErrorData;
import com.google.myjson.Gson;

import java.security.KeyStore;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;

public class CommonRequestUtil
{
  public int code = 0;
  public String codeValue = "";
  public String message = "";

  public String HttpGetRequest(String RequestUrl)
  {
    String jsonStr = "";
    HttpGet getMethod = new HttpGet(RequestUrl);
    HttpClient httpClient = getNewHttpClient();
    try
    {
      HttpResponse response = httpClient.execute(getMethod);
      this.code = response.getStatusLine().getStatusCode();
      this.message = response.getStatusLine().getReasonPhrase();
      jsonStr = EntityUtils.toString(response.getEntity(), "utf-8");
      //Log.i("http", this.code + " " + jsonStr);
      if (this.code == 200) {
        try
        {
          ErrorData error = (ErrorData)new Gson().fromJson(jsonStr, ErrorData.class);
          if ((error.Code == null) || (error.Message == null)) return jsonStr;
          this.code = -2;
          this.codeValue = error.Code;
          this.message = error.Message;
        } catch (Exception localException1) {
          localException1.printStackTrace();
        }
      }
      else if (this.code == 500) {
        this.message = "基金交易服务器正在维护中，暂时无法进行交易，敬请谅解。";
      } else {
        try {
          Gson gson = new Gson();
          ErrorData error = 
            (ErrorData)gson
            .fromJson(EntityUtils.toString(
            response.getEntity(), "utf-8"), 
            ErrorData.class);
          this.codeValue = error.Code;
          this.message = error.Message;
        } catch (Exception e) {
          this.codeValue = "";
          this.message = response.getStatusLine().toString();
        }
      }
    } catch (Exception e) {
      //Log.i("http", e.getMessage());
      e.printStackTrace();
      this.code = -1;
      this.codeValue = "";
      this.message = e.getMessage();
    }

    return jsonStr;
  }

  public HttpClient getNewHttpClient() {
    try {
      KeyStore trustStore = KeyStore.getInstance(
        KeyStore.getDefaultType());
      trustStore.load(null, null);

      SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
      sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

      HttpParams params = new BasicHttpParams();
      HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
      HttpProtocolParams.setContentCharset(params, "UTF-8");

      SchemeRegistry registry = new SchemeRegistry();
      registry.register(new Scheme("http", 
        PlainSocketFactory.getSocketFactory(), 80));
      registry.register(new Scheme("https", sf, 443));

      ClientConnectionManager ccm = new ThreadSafeClientConnManager(
        params, registry);

      return new DefaultHttpClient(ccm, params); 
    } catch (Exception e) {
      e.printStackTrace();
    }
    return new DefaultHttpClient();
  }
}
