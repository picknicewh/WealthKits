package com.openhunme.sdk.update;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import com.openhunme.sdk.common.CommonRequestUtil;
import com.openhunme.sdk.data.bean.FileMD5Bean;
import com.openhunme.sdk.data.bean.UpdateManifestBean;
import com.openhunme.sdk.data.bean.VersionManifestBean;
import com.openhunme.sdk.exception.HunmeSdkException;
import com.openhunme.sdk.utils.FileUtil;
import com.openhunme.sdk.utils.MD5Util;
import com.openhunme.sdk.utils.ZipUtil;
import com.cfjn.javacf.utility.apibase.APIUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * 1所有的对外功能，触及到有代码交叉的地方，统一都要用接口的方式对接。
 * 2这个类，原则上不可以涉及到任何UI层门调用
 * 
 * 
 * 
 * @author Administrator
 *
 */
public class ModelUpdateManager {
  public static final int INIT_OK = 1;
  public static final int INIT_ALREADY = 2;
  public static final int INIT_FAIL = -1;
  public static final int DOWNLOAD_OK = 1;
  public static final int DOWNLOAD_FAIL = -1;
  public static final int VERIFY_OK = 1;
  public static final int VERIFY_FAIL = -1;
  public static final int VERIFY_MANIFEST_FAIL = -2;
  public static final int VERIFY_ZIP_FAIL = -3;
  public static final int VERIFY_FILE_FAIL = -4;
  public static final int UNZIP_OK = 1;
  public static final int UNZIP_FAIL = -1;
  public static final int UNZIP_FIRST_FAIL = -2;
  public static final int UNZIP_SECOND_FAIL = -3;
  private static String LogTag = ModelUpdateManager.class.getName();
  private static ModelUpdateManager instance = null;
  private VersionManifestData versionManifestData;
  private UpdateManifestData updateManifestData;
  private String baseDir = "";
  private String baseUrl = "";
  private static final String tempDir = "temp/";

  public interface ModelUpdateInterface {
	public void onBegin();
	public void onEnd();
	public void onProgress(int rate);
	public void onBeginUnzip();
	public void onDownloadFail();
	public void onUnzipFail(int type);
	public void onVerifyFail(int type);
    public void onCopyFail();
	public void onFail();
  }
  
  public static ModelUpdateManager getInstance() {
    if (instance == null)
      instance = new ModelUpdateManager();
    return instance;
  }
  
  public void initBaseDir(Context context, String baseDir) {
	this.baseDir = baseDir.replace("\\", "/");
    if (!this.baseDir.endsWith("/"))
    	this.baseDir = this.baseDir + "/";
    if (!this.baseDir.startsWith("/"))
    	this.baseDir = "/" + this.baseDir;
    //this.baseDir = Environment.getExternalStorageDirectory().getAbsolutePath() + this.baseDir;
    this.baseDir = context.getFilesDir().getAbsolutePath() + this.baseDir;
  }

  public void initBaseUrl(String baseUrl) {
	this.baseUrl = baseUrl;
    if (!this.baseUrl.endsWith("/"))
    	this.baseUrl = this.baseUrl + "/";
  }

  public String getModelCurrentVersion(String model) {
	String ret = "0";
    try {
    	VersionManifestData versionManifestData = loadVersionManifestData(this.baseDir, model);
    	ret = versionManifestData.versionManifestBean.version;
	} catch (Exception e) {
		e.printStackTrace();
	}
	return ret;
  }
  
  public static class VersionManifestData {
    public VersionManifestBean versionManifestBean;
    public String versionManifestMD5;

    private VersionManifestData() {
      this.versionManifestBean = new VersionManifestBean();
      this.versionManifestBean.model = "";
      this.versionManifestBean.version = "0";
      this.versionManifestBean.md5 = "";
      this.versionManifestBean.FileMD5List = null;
      this.versionManifestMD5 = "";
    }
  }

  public static class UpdateManifestData {
    public UpdateManifestBean updateManifestBean;
    public String errorNo;
    public String errorInfo;
    public String dsName;

    private UpdateManifestData() {
      this.updateManifestBean = new UpdateManifestBean();
      this.updateManifestBean.bundleInstallPath = "";
      this.updateManifestBean.bundleMd5 = "";
      this.updateManifestBean.bundleModel = "";
      this.updateManifestBean.bundleVersion = "";
      this.updateManifestBean.releaseStatus = "";
      this.updateManifestBean.softVersion = "";
      this.updateManifestBean.status = "";
      this.updateManifestBean.upgradeNote = "";
      this.updateManifestBean.upgradeType = "";
      this.errorNo = "";
      this.errorInfo = "";
      this.dsName = "";
    }
  }

  public VersionManifestData getVersionManifestData() {
    return this.versionManifestData;
  }

  public UpdateManifestData getUpdateManifestData() {
    return this.updateManifestData;
  }

  public void setVersionManifestData(String json) {
    try {
  	  JSONObject jo = new JSONObject(json);
  	  this.versionManifestData.versionManifestBean.model = jo.getString("model");
  	  this.versionManifestData.versionManifestBean.version = jo.getString("version");
      this.versionManifestData.versionManifestBean.md5 = jo.getString("md5");
  	  JSONObject md5List = jo.getJSONObject("md5List");
  	  FileMD5Bean fb;
  	  this.versionManifestData.versionManifestBean.FileMD5List = new ArrayList<FileMD5Bean>();
  	  Iterator<String> it = md5List.keys();
  	  String key;
  	  String value;
        while (it.hasNext()) {
          key = (String) it.next();  
          value = md5List.getString(key);  
  		fb = new FileMD5Bean();
  		fb.Path = key;
  		fb.MD5 = value;
  		this.versionManifestData.versionManifestBean.FileMD5List.add(fb);
        }
  	} catch (JSONException e) {
  	  e.printStackTrace();
  	}
  }

  public void setUpdateManifestData(String json) {

    try{
      this.updateManifestData = new UpdateManifestData();
      JSONObject jo = new JSONObject(json);
      this.updateManifestData.errorNo = jo.getString("error_no");
      this.updateManifestData.errorInfo = jo.getString("error_info");
      this.updateManifestData.dsName = jo.getJSONArray("dsName").getString(0);
      String data = jo.getString(this.updateManifestData.dsName);
      jo = new JSONObject(data);
      this.updateManifestData.updateManifestBean.status = jo.getString("status");
      this.updateManifestData.updateManifestBean.softVersion = jo.getString("soft_version");
      this.updateManifestData.updateManifestBean.bundleModel = jo.getString("bundle_model");
      this.updateManifestData.updateManifestBean.bundleVersion = jo.getString("bundle_version");
      this.updateManifestData.updateManifestBean.bundleMd5 = jo.getString("bundle_md5");
      this.updateManifestData.updateManifestBean.bundleInstallPath = jo.getString("bundle_install_path");
      this.updateManifestData.updateManifestBean.releaseStatus = jo.getString("release_status");
      this.updateManifestData.updateManifestBean.upgradeNote = jo.getString("upgrade_note");
      this.updateManifestData.updateManifestBean.upgradeType = jo.getString("upgrade_type");
    } catch(Exception e) {
      e.printStackTrace(); 
    }
  }

  private static VersionManifestData loadVersionManifestData(String baseDir, String model) throws IOException {
    VersionManifestData versionManifestData = new VersionManifestData();
    try {
      File versionInfoManifestFile = new File(baseDir + "tap.txt");
      //Log.i("loadVersionManifestData", baseDir + "tap.txt");
      if (versionInfoManifestFile.exists()) {
        versionManifestData.versionManifestMD5 =
                MD5Util.getFileMD5String(versionInfoManifestFile);
        String fileContent = FileUtil.readFileContent(versionInfoManifestFile);
        //Log.i("loadVersionManifestData", fileContent);
        try {
          JSONObject jo = new JSONObject(fileContent);
          versionManifestData.versionManifestBean.model = jo.getString("model");
          versionManifestData.versionManifestBean.version = jo.getString("version");
          versionManifestData.versionManifestBean.md5 = jo.getString("md5");
          JSONObject md5List = jo.getJSONObject("md5List");
          FileMD5Bean fb;
          versionManifestData.versionManifestBean.FileMD5List = new ArrayList<FileMD5Bean>();
          Iterator<String> it = md5List.keys();
          String key;
          String value;
          while (it.hasNext()) {
            key = (String) it.next();
            value = md5List.getString(key);
            fb = new FileMD5Bean();
            fb.Path = key;
            fb.MD5 = value;
            versionManifestData.versionManifestBean.FileMD5List.add(fb);
          }
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return versionManifestData;
  }

  private static UpdateManifestData loadRemoteUpdateManifestData(String checkUrl) throws HunmeSdkException {
    String fileContent = "";
    try {
      CommonRequestUtil requestUtil = new CommonRequestUtil();
      Log.i("checkUrl", checkUrl);
      fileContent = requestUtil.HttpGetRequest(checkUrl);
      Log.i("json", fileContent);
      if (requestUtil.code != 200) {
        Log.w(LogTag, requestUtil.message);
        String errMsg = String.format("无法连接至服务器！%d => %s", new Object[]{
                Integer.valueOf(requestUtil.code), requestUtil.message});
        throw new HunmeSdkException(requestUtil.code, errMsg,
                new Exception(requestUtil.message));
      }
    } catch (Exception e) {
      e.printStackTrace();
      String errMsg = String.format("无法连接至服务器！%s", new Object[]{e.getMessage()});
      throw new HunmeSdkException(0, errMsg,
              new Exception(errMsg));
    }
    UpdateManifestData updateManifestData = new UpdateManifestData();
    try{
    	JSONObject jo = new JSONObject(fileContent);
    	updateManifestData.errorNo = jo.getString("error_no");
    	updateManifestData.errorInfo = jo.getString("error_info");
    	updateManifestData.dsName = jo.getJSONArray("dsName").getString(0);
        String data = jo.getString(updateManifestData.dsName);
		jo = new JSONObject(data);
		updateManifestData.updateManifestBean.status = jo.getString("status");
		updateManifestData.updateManifestBean.softVersion = jo.getString("soft_version");
		updateManifestData.updateManifestBean.bundleModel = jo.getString("bundle_model");
		updateManifestData.updateManifestBean.bundleVersion = jo.getString("bundle_version");
		updateManifestData.updateManifestBean.bundleMd5 = jo.getString("bundle_md5");
		updateManifestData.updateManifestBean.bundleInstallPath = jo.getString("bundle_install_path");
		updateManifestData.updateManifestBean.releaseStatus = jo.getString("release_status");
		updateManifestData.updateManifestBean.upgradeNote = jo.getString("upgrade_note");
		updateManifestData.updateManifestBean.upgradeType = jo.getString("upgrade_type");
    } catch(Exception e) { 
      e.printStackTrace(); 
    }
    return updateManifestData;
  }
  
  public static String getAppVersionName(Context context) {
	String name = "";
    try {
      PackageManager packageManager = context.getPackageManager();
      PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
      name = packInfo.versionName;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return name;
  }

  public int doCheckUpdate(Context context, String model) throws HunmeSdkException {
    try {
      this.versionManifestData = loadVersionManifestData(this.baseDir, model);
    } catch (Exception e) {
      this.versionManifestData = new VersionManifestData();
      Log.e(LogTag, e.getMessage());
    }
    boolean needUpdate = true;
    try {
      String sourceId = APIUtil.getDeviceUUId(context);
      this.updateManifestData = loadRemoteUpdateManifestData(this.baseUrl + "soft_version/v" + getAppVersionName(context) + "/bundle_model/" + model + "/bundle_version/" + getModelCurrentVersion(model) + "/source_id/" + sourceId + "/source/android");
      if (!this.updateManifestData.errorNo.equals("0"))
        needUpdate = false;
      if (!this.updateManifestData.updateManifestBean.status.equals("1"))
        needUpdate = false;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return needUpdate ? -1 : 0;
  }

  public int doCheckInitOk(Context context) {
	int ret = INIT_OK;
    try {
    	SharedPreferences settings = context.getSharedPreferences("tap", Activity.MODE_PRIVATE);
        String initOk = settings.getString("InitOk." + getAppVersionName(context), "false");
        if (initOk.equalsIgnoreCase("true"))
        	ret = INIT_ALREADY;
        else {
    		String path = this.baseDir;
    		File file = new File(path);
    		file.mkdirs();
    		path = this.baseDir + "tap.txt";
    		FileUtil.copyAssets(context, "tap.txt", path);
    		//Log.i("doCheckInitOk", path);
    		path = this.baseDir + "tap.zip";
    		FileUtil.copyAssets(context, "tap.zip", path);
    		file = new File(path);
    		path = this.baseDir;
    		ZipUtil.unzip(file, path);
    		Editor editor = settings.edit();
            editor.putString("InitOk." + getAppVersionName(context), "true");
            editor.commit();
            file.delete();
    	}
    } catch (Exception e) {
      ret = INIT_FAIL;
      Log.e(LogTag, e.getMessage());
    }
    return ret;
  }

  public String getCurrentBundleMd5(Context context, String model) {
	  String ret = "0";
	  try {
	    File versionInfoManifestFile = new File(this.baseDir + "tap.txt");
	    if (versionInfoManifestFile.exists())
	  	  ret = MD5Util.getFileMD5String(versionInfoManifestFile);
	  } catch (Exception e) {
		  e.printStackTrace();
	  }
	  return ret;
  }
	    
  private String getUpdatePath() {
    return this.baseDir;
  }
  
  final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
	public boolean verify(String hostname, SSLSession session) {
	  return true;
	}
  };

  private static void trustAllHosts() {
	TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
	  public X509Certificate[] getAcceptedIssuers() {
		return new X509Certificate[] {};
	  }
	  public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	  }
	  public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	  }
	} };
	try {
	  SSLContext sc = SSLContext.getInstance("TLS");
	  sc.init(null, trustAllCerts, new java.security.SecureRandom());
	  HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	} catch (Exception e) {
	  e.printStackTrace();
	}
  }
  
  public void doUpdate(Context context, String model, ModelUpdateInterface muicb) {
	String md5 = this.updateManifestData.updateManifestBean.bundleMd5;
	String path = this.updateManifestData.updateManifestBean.bundleInstallPath;
	doUpdate(context, model, path, md5, muicb);
  }
  
  public int doVerify(Context context, String model) {
	int ret = VERIFY_OK;
    try {
      String updateFileMd5;
      String updateFilePath;
      File targetFile;

      if (ret == VERIFY_OK) {
        this.versionManifestData = loadVersionManifestData(this.baseDir, model);
        updateFileMd5 = this.versionManifestData.versionManifestMD5;
        if (this.updateManifestData.updateManifestBean.bundleMd5.indexOf(updateFileMd5) < 0)
    	  ret = VERIFY_MANIFEST_FAIL;
      }
      if (ret == VERIFY_OK) {
        for (int i = 0; i < this.versionManifestData.versionManifestBean.FileMD5List.size(); i ++) {
          updateFilePath = String.format("%s%s", new Object[] { this.baseDir,
        		this.versionManifestData.versionManifestBean.FileMD5List.get(i).Path });
	      targetFile = new File(updateFilePath);
	      updateFileMd5 = MD5Util.getFileMD5String(targetFile);
  	      if (!this.versionManifestData.versionManifestBean.FileMD5List.get(i).MD5.equalsIgnoreCase(updateFileMd5)) {
	    	ret = VERIFY_FILE_FAIL;
	    	break;
	      }
        }
      }
      if (ret == VERIFY_OK)
        Log.i("doVerify", "ok");
    } catch (Exception e) {
      ret = VERIFY_FAIL;
    }
    return ret;
  }

  public int doVerifyFile(Context context, String model, String path) {
    int ret = VERIFY_OK;
    try {
      String updateFileMd5;
      String updateFilePath;
      File targetFile;
      if (ret == VERIFY_OK) {
        this.versionManifestData = loadVersionManifestData(this.baseDir, model);
        updateFileMd5 = this.versionManifestData.versionManifestMD5;
        if (this.updateManifestData.updateManifestBean.bundleMd5.indexOf(updateFileMd5) < 0)
          ret = VERIFY_MANIFEST_FAIL;
      }
      if (ret == VERIFY_OK) {
        ret = VERIFY_FILE_FAIL;
        for (int i = 0; i < this.versionManifestData.versionManifestBean.FileMD5List.size(); i ++) {
          if (path.equalsIgnoreCase(this.versionManifestData.versionManifestBean.FileMD5List.get(i).Path)) {
            updateFilePath = String.format("%s%s", new Object[]{this.baseDir,
                    this.versionManifestData.versionManifestBean.FileMD5List.get(i).Path});
            targetFile = new File(updateFilePath);
            updateFileMd5 = MD5Util.getFileMD5String(targetFile);
            if (this.versionManifestData.versionManifestBean.FileMD5List.get(i).MD5.equalsIgnoreCase(updateFileMd5))
              ret = VERIFY_OK;
            break;
          }
        }
      }
      if (ret == VERIFY_OK)
        Log.i("doVerifyFile", "ok");
    } catch (Exception e) {
      ret = VERIFY_FAIL;
    }
    return ret;
  }

  class RunnableOneInt implements Runnable {
    int i;
    public RunnableOneInt(int i){
      this.i = i;
    }
    public void run() {
    }
  }

  public void doUpdate(Context context, String model, String path, String md5, final ModelUpdateInterface muicb) {
    try {
      String updateFilePath;
      File targetFile;
      if (muicb != null) {
        ((Activity)context).runOnUiThread(new Runnable() {
		  @Override
		  public void run() {
	    	muicb.onBegin();
		  }
        });
      }
      try {
        String updatePath = getUpdatePath();
        FileUtil.makeDirectories(updatePath);
        updateFilePath = String.format("%supdate.zip", new Object[] { updatePath });
        targetFile = new File(updateFilePath);
        URL url = new URL(path);
        HttpURLConnection http = null;
        if (url.getProtocol().toLowerCase().equals("https")) {
    	  trustAllHosts();
          HttpsURLConnection https = (HttpsURLConnection)url.openConnection();  
          https.setHostnameVerifier(DO_NOT_VERIFY);
          http = https;
        } else {
	      http = (HttpURLConnection)url.openConnection();
        }
        http.setRequestProperty("Accept-Encoding", "identity");
        int length = http.getContentLength();
        Log.i("length", "" + length);
        InputStream is = http.getInputStream();
        byte[] bs = new byte[8192];
        OutputStream os = new FileOutputStream(updateFilePath);
        int len;
        int total = 0;
        int rate;
        while ((len = is.read(bs)) != -1) {
      	  if (length > 0) {
            if (len > 0) {
        	  total += len;
        	  rate = total * 100 / length;
        	  if (rate > 100)
        		rate = 100;
              if (muicb != null) {
                ((Activity)context).runOnUiThread(new RunnableOneInt(rate) {
                  @Override
          		  public void run() {
            		muicb.onProgress(this.i);
          		  }
                });
              }
            }
          }
          os.write(bs, 0, len);
        }
        os.close();
        is.close();
      } catch (Exception e) {
	    if (muicb != null) {
	      ((Activity)context).runOnUiThread(new Runnable() {
	        @Override
	        public void run() {
	      	  muicb.onDownloadFail();
	  		}
          });
          return;
	    }
        throw(e);
      }
      if (muicb != null) {
	    ((Activity)context).runOnUiThread(new Runnable() {
	      @Override
          public void run() {
      	    muicb.onBeginUnzip();
	  	  }
	    });
	  }

      try {
        FileUtil.deleteDirectory(this.baseDir + this.tempDir);
      } catch (Exception e) {
        e.printStackTrace();
      }
      try {
        FileUtil.makeDirectories(this.baseDir + this.tempDir);
      } catch (Exception e) {
        e.printStackTrace();
      }

      try {
        ZipUtil.unzip(targetFile, this.baseDir + this.tempDir);
      } catch (Exception e) {
	    if (muicb != null) {
	      ((Activity)context).runOnUiThread(new RunnableOneInt(UNZIP_FIRST_FAIL) {
	        @Override
	        public void run() {
	      	  muicb.onUnzipFail(this.i);
	  		}
	      });
          return;
	    }
        throw(e);
      }
      try {
        targetFile.delete();
      } catch (Exception e) {
        e.printStackTrace();
      }

      this.versionManifestData = loadVersionManifestData(this.baseDir + this.tempDir, model);
      String updateFileMd5 = this.versionManifestData.versionManifestMD5;
      if (md5.indexOf(updateFileMd5) < 0) {
	    if (muicb != null) {
	      ((Activity)context).runOnUiThread(new RunnableOneInt(VERIFY_MANIFEST_FAIL) {
	        @Override
	        public void run() {
	      	  muicb.onVerifyFail(this.i);
	  		}
	      });
          return;
	    }
        throw(new Exception("VERIFY_MANIFEST_FAIL"));
      }

      updateFilePath = String.format("%stap.zip", new Object[] { this.baseDir + this.tempDir });
      targetFile = new File(updateFilePath);
      updateFileMd5 = MD5Util.getFileMD5String(targetFile);
      if (!this.versionManifestData.versionManifestBean.md5.equalsIgnoreCase(updateFileMd5)) {
	    if (muicb != null) {
	      ((Activity)context).runOnUiThread(new RunnableOneInt(VERIFY_ZIP_FAIL) {
	        @Override
	        public void run() {
	      	  muicb.onVerifyFail(this.i);
	  		}
	      });
          return;
	    }
        throw(new Exception("VERIFY_ZIP_FAIL"));
      }

      try {
        ZipUtil.unzip(targetFile, this.baseDir + this.tempDir);
      } catch (Exception e) {
	    if (muicb != null) {
	      ((Activity)context).runOnUiThread(new RunnableOneInt(UNZIP_SECOND_FAIL) {
	        @Override
	        public void run() {
	      	  muicb.onUnzipFail(this.i);
	  		}
	      });
          return;
	    }
        throw(e);
      }
      try {
        targetFile.delete();
      } catch (Exception e) {
        e.printStackTrace();
      }

      try {
        FileUtil.copyDirectory(this.baseDir + this.tempDir, this.baseDir);
        this.versionManifestData = loadVersionManifestData(this.baseDir, model);
        FileUtil.deleteDirectory(this.baseDir + this.tempDir);
      } catch (Exception e) {
        if (muicb != null) {
          ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
              muicb.onCopyFail();
            }
          });
          return;
        }
        throw(e);
      }

      if (muicb != null) {
	    ((Activity)context).runOnUiThread(new Runnable() {
	      @Override
          public void run() {
      	    muicb.onEnd();
	  	  }
	    });
	  }
    }
    catch (ConnectException e) {
      if (muicb != null) {
	    ((Activity)context).runOnUiThread(new Runnable() {
	      @Override
          public void run() {
      	    muicb.onFail();
	  	  }
	    });
        return;
	  }
      e.printStackTrace();
      try {
        throw(e);
      } catch (Exception e1) {
        e1.printStackTrace();
      }
    } catch (Exception e) {
      if (muicb != null) {
	    ((Activity)context).runOnUiThread(new Runnable() {
	      @Override
          public void run() {
      	    muicb.onFail();
	  	  }
	    });
        return;
	  }
      e.printStackTrace();
      try {
        throw(e);
      } catch (Exception e1) {
        e1.printStackTrace();
      }
    }
  }
  
  public int doVerify(Context context, String model, String md5) {
	int ret = VERIFY_OK;
    try {
      String updateFileMd5;
      String updateFilePath;
      File targetFile;
      if (ret == VERIFY_OK) {
        this.versionManifestData = loadVersionManifestData(this.baseDir, model);
        updateFileMd5 = this.versionManifestData.versionManifestMD5;
        Log.e("doVerify", md5 + " " + updateFileMd5);
        if (md5.indexOf(updateFileMd5) < 0)
    	    ret = VERIFY_MANIFEST_FAIL;
      }
      if (ret == VERIFY_OK) {
        for (int i = 0; i < this.versionManifestData.versionManifestBean.FileMD5List.size(); i ++) {
          updateFilePath = String.format("%s%s", new Object[] { this.baseDir, 
        	 	  this.versionManifestData.versionManifestBean.FileMD5List.get(i).Path });
	      targetFile = new File(updateFilePath);
	      if (targetFile.exists()) {
	        updateFileMd5 = MD5Util.getFileMD5String(targetFile);
            Log.e("doVerify", this.versionManifestData.versionManifestBean.FileMD5List.get(i).MD5 + " " + updateFileMd5);
	        if (!this.versionManifestData.versionManifestBean.FileMD5List.get(i).MD5.equalsIgnoreCase(updateFileMd5)) {
	          ret = VERIFY_FILE_FAIL;
	          break;
	        }
	      }
	    }
      }
      if (ret == VERIFY_OK)
        Log.i("doVerify", "ok");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return ret;
  }

  public int doVerifyFile(Context context, String model, String md5, String path) {
    int ret = VERIFY_OK;
    try {
      String updateFileMd5;
      String updateFilePath;
      File targetFile;
      if (ret == VERIFY_OK) {
        this.versionManifestData = loadVersionManifestData(this.baseDir, model);
        updateFileMd5 = this.versionManifestData.versionManifestMD5;
        if (md5.indexOf(updateFileMd5) < 0)
          ret = VERIFY_MANIFEST_FAIL;
      }
      if (ret == VERIFY_OK) {
        ret = VERIFY_FILE_FAIL;
        for (int i = 0; i < this.versionManifestData.versionManifestBean.FileMD5List.size(); i ++) {
          if (path.equalsIgnoreCase(this.versionManifestData.versionManifestBean.FileMD5List.get(i).Path)) {
            updateFilePath = String.format("%s%s", new Object[]{this.baseDir,
                    this.versionManifestData.versionManifestBean.FileMD5List.get(i).Path});
            targetFile = new File(updateFilePath);
            if (targetFile.exists()) {
              updateFileMd5 = MD5Util.getFileMD5String(targetFile);
              if (this.versionManifestData.versionManifestBean.FileMD5List.get(i).MD5.equalsIgnoreCase(updateFileMd5))
                ret = VERIFY_OK;
            }
            break;
          }
        }
      }
      if (ret == VERIFY_OK)
        Log.i("doVerifyFile", "ok");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return ret;
  }
}
