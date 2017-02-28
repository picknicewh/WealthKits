package com.cfjn.javacf.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.ShutterCallback;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cfjn.javacf.R;
import com.cfjn.javacf.plugin.IDCheckPlugin;
import com.exidcard.ExIDCardReco;
import com.exidcard.ExIDCardResult;
import com.exidcard.camera.CameraManager;
import com.exidcard.decoding.CaptureActivityHandler;
import com.exidcard.decoding.InactivityTimer;
import com.exidcard.view.ViewfinderView;
import com.openhunme.sdk.utils.ImageUtil;

import org.apache.cordova.PluginResult;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class CaptureActivity extends Activity implements Callback {
	private static final String TAG = CaptureActivity.class.getSimpleName();
	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private TextView txtResult;
	private InactivityTimer inactivityTimer;
//	private MediaPlayer mediaPlayer;
	private boolean playBeep;
//	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;
	private byte dbpath[];
//	private Bitmap logo;
	private Bitmap idImage_for_check=null;

	//save last time recognize result
	private ExIDCardResult cardlast = null;
	//current time recognition result
	private ExIDCardResult cardRest = null;

	/** ID值，用于锁定发出请求的对象通道ID **/
	private String activeCallBackContextID;

	private final ShutterCallback shutterCallback = new ShutterCallback() {
        public void onShutter() {
            AudioManager mgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            mgr.playSoundEffect(AudioManager.FLAG_PLAY_SOUND);
        }
    };

	//===========指定识别分类器模型库文件加载到SD卡中的保存位置及文件名==========================================
	//注意：受动态库限制，路径及文件名不可更改
    private final String RESOURCEFILEPATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/exidcard/";   //模型库路径
    private final String DICTPATH = "/data/data/com.exidcard";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//activity传送数据
		Bundle bl = getIntent().getExtras();
		activeCallBackContextID = bl.getString("activeCallBackContextID");

		//CameraManager
		CameraManager.init(getApplication());
		hardwareSupportCheck();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		// FLAG_TRANSLUCENT_NAVIGATION
		if(Build.VERSION.SDK_INT >= 19){
			getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
				WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
		setContentView(R.layout.activity_capture);

		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		txtResult = (TextView) findViewById(R.id.capture_txtResult);
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
//		logo = BitmapFactory.decodeResource(this.getResources(), R.drawable.yidaoboshi96);
//		viewfinderView.setLogo(logo);//不设定logo

		//ExIDCardReco tmp = new ExIDCardReco();
//	    String path = this.getApplicationContext().getFilesDir().getAbsolutePath();

	    String SYSTEMBASEPATH = Environment.getExternalStorageDirectory().getAbsolutePath();

		String path = SYSTEMBASEPATH+"/TF";
		//InitDict(RESOURCEFILEPATH);
	    InitDict(path);
	    //天风证券
	    ButtonInit();
	    resultVisible(false);
	}
//	public static boolean hardwareSupportCheck() {
	public boolean hardwareSupportCheck() {
	      // Camera needs to open
	        Camera c = null;
	        try {
	            c = Camera.open();
	        } catch (RuntimeException e) {

//	            throw new RuntimeException();

	        	e.getStackTrace();
//	        	showTomast("摄像头无法打开，请再试一次");
	        	Toast.makeText(CaptureActivity.this.getApplicationContext(), "摄像头无法打开，请再试一次", Toast.LENGTH_LONG).show();
	        }
	        if (c == null) {
	            return false;
	        } else {
	            c.release();
	        }
	        return true;
	    }

	public boolean InitDict(String dictpath)
	{
		dbpath = new byte[256];
		//if the dict not exist, copy from the assets
		if(CheckExist(dictpath+"/zocr0.lib") == false ){
			File destDir = new File(dictpath);
			if (!destDir.exists()) { destDir.mkdirs(); }

			boolean a = copyFile("zocr0.lib", dictpath+"/zocr0.lib");
			if (a == false){
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("EXTrans dict Copy ERROR!\n");
				builder.setMessage(dictpath+" can not be found!");
				builder.setCancelable(true);
				builder.create().show();
				return false;
			}
		}
		String filepath = dictpath;
		//string to byte
		for (int i = 0; i < filepath.length(); i++)
			dbpath[i] = (byte)filepath.charAt(i);
		dbpath[filepath.length()] = 0;
		int nres = ExIDCardReco.nativeInit(dbpath);
		//到期的话，就会一直报错，需要修改手机时间
		if (nres < 0){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("EXTrans dict Init ERROR!\n");
			builder.setMessage(filepath+" can not be found!");
			builder.setCancelable(true);
			builder.create().show();
			return false;
		}else{
			//just test
			//ExTranslator.nativeExTran(imgdata, width, height, pixebyte, pitch, flft, ftop, frgt, fbtm, result, maxsize)
		}
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
//		initBeepSound();
		vibrate = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		ExIDCardReco.nativeDone();
		//清理缓存对象数据值
		if(idImage_for_check!=null){
			idImage_for_check.recycle();
			idImage_for_check=null;
		}
		IDCheckPlugin.captrue_isruning = false;
		super.onDestroy();
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		/** 当重启这个界面的时候，可能会出现 auto frail 的问题，所以对这里进行修改**/
		try{
			CameraManager.get().openDriver(surfaceHolder);
			if (handler == null) {
				handler = new CaptureActivityHandler(this);
			}
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "摄像头无法打开，请再试一次", Toast.LENGTH_LONG).show();
			return;
		}

//		try {
//			CameraManager.get().openDriver(surfaceHolder);
//		} catch (IOException ioe) {
//			return;
//		} catch (RuntimeException e) {
//			return;
//		}
//		if (handler == null) {
//			handler = new CaptureActivityHandler(this);
////			try {
////
////			} catch (Exception e) {
////				return;
////			}
//		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		if (handler == null) {
			handler = new CaptureActivityHandler(this);
		}
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();
	}
	//show the decode result
	//正确识别到后执行
	/**
	 * 当获取到照片时，就会触发这个方法，可以拿到图片
	 * @param result
	 */
	@SuppressLint("NewApi")
	public void handleDecode(ExIDCardReco result) {
		inactivityTimer.onActivity();
//		playBeepSoundAndVibrate();
//		txtResult.setText(result.getBarcodeFormat().toString() + ":"+ result.getText());
//		txtResult.setText("decode txt:\n"+ result.getText());
		//show the result
		if (result == null){
			txtResult.setText("");
		}else{
			txtResult.setText(result.getText());
			idImage_for_check = cardRest.GetBaseBitmap();
			if(idImage_for_check==null){
				idImage_for_check = result.getPicBitmap();
			}
			callback4html(idImage_for_check,result.getjson());
			resultVisible(true);
			//让中路的图片显示身份证信息
			ImageView IDimageView = (ImageView)findViewById(R.id.capture_imgResult);
			TextView IDResultView = (TextView)findViewById(R.id.capture_txtResult);
			IDimageView.setVisibility(View.VISIBLE);
			IDimageView.setImageBitmap(idImage_for_check);
			Point widthAndHeight = viewfinderView.getWidthandHeight();
			Point xAndy = viewfinderView.getXandY();
			IDimageView.getLayoutParams().width=IDResultView.getLayoutParams().width =widthAndHeight.x;
			IDimageView.getLayoutParams().height=IDResultView.getLayoutParams().height=widthAndHeight.y;
			IDimageView.setX(xAndy.x);
			IDimageView.setY(xAndy.y);
			IDResultView.setX(xAndy.x);
			IDResultView.setY(xAndy.y);
//			finish();
		}
	}

//	private void initBeepSound() {
//		if (playBeep && mediaPlayer == null) {
//			// The volume on STREAM_SYSTEM is not adjustable, and users found it
//			// too loud,
//			// so we now play on the music stream.
//			setVolumeControlStream(AudioManager.STREAM_MUSIC);
//			mediaPlayer = new MediaPlayer();
//			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//			mediaPlayer.setOnCompletionListener(beepListener);
//
//			AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
//			try {
//				mediaPlayer.setDataSource(file.getFileDescriptor(),
//						file.getStartOffset(), file.getLength());
//				file.ic_close();
//				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
//				mediaPlayer.prepare();
//			} catch (IOException e) {
//				mediaPlayer = null;
//			}
//		}
//	}

	private static final long VIBRATE_DURATION = 200L;

//	private void playBeepSoundAndVibrate() {
//		if (playBeep && mediaPlayer != null) {
//			mediaPlayer.start();
//		}
//		if (vibrate) {
//			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
//			vibrator.vibrate(VIBRATE_DURATION);
//		}
//	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

//	@Override
//	public boolean onTouchEvent (MotionEvent event){
//		float x = event.getX();
//		float y = event.getY();
//		Point res = CameraManager.get().getResolution();
//
//		if (event.getAction() == MotionEvent.ACTION_UP){
//			if(x > res.x*8/10 && y < res.y/4) { return false; }
//
//			handleDecode(null);
//			cardlast = null;
//			//点击重新聚焦
//			handler.restartAutoFocus();
//			return true;
//		}
//		return false;
//	}

	/**
	 * 重新进行识别
	 */
	@SuppressLint("NewApi")
	private void restartScan(){
		CaptureActivity.this.recreate();
		resultVisible(false);
//		Point res = CameraManager.get().getResolution();
//		handleDecode(null);
//		cardlast = null;
//		//点击重新聚焦
//		handler.restartAutoFocus();
	}

	public boolean copyFile(String from, String to) {
		// 例：from:890.salid;
		// to:/mnt/sdcard/to/890.salid
		boolean ret = false;
		try {
			int bytesum = 0;
			int byteread = 0;

			InputStream inStream = getResources().getAssets().open(from);// 将assets中的内容以流的形式展示出来
			File file = new File(to);
			OutputStream fs = new FileOutputStream(file);// to为要写入sdcard中的文件名称
			byte[] buffer = new byte[1024];
			while ((byteread = inStream.read(buffer)) != -1) {
				bytesum += byteread;
				fs.write(buffer, 0, byteread);
			}
			inStream.close();
			fs.close();
			ret = true;

		} catch (Exception e) {
			ret = false;
		}
		return ret;
	}

	//check one file
	public boolean CheckExist(String filepath)
	{
		File file = new File (filepath);
		if (file.exists()){
			return true;
		}
		return false;
	}

	public void SetRecoResult(ExIDCardResult result)
	{
		cardRest = result;
	}

	//check is equal()
	public boolean CheckIsEqual(ExIDCardResult cardcur){
		if (cardlast == null){
			cardlast = cardcur;
			return false;
		}else{
			if (cardlast.name.equals(cardcur.name) &&
			    cardlast.sex.equals(cardcur.sex) &&
			    cardlast.nation.equals(cardcur.nation) &&
			    cardlast.cardnum.equals(cardcur.cardnum) &&
			    cardlast.address.equals(cardcur.address)){
				return true;
			}else{
				cardlast = cardcur; cardcur = null;
				return false;
			}
		}
	}

	public ShutterCallback getShutterCallback(){
		return shutterCallback;
	}
	//////////////////////////////////////////////////////////////////////////
	public void OnShotBtnClick(View view) {
		handleDecode(null);
		handler.takePicture();
	}



	/**
	 * 返回PHONEGAP数据
	 *
	 * 该数据将满足html5的页面逻辑要求。
	 * 格式：base64
	 */
	private JSONObject jo;
	public void callback4html(Bitmap bt,JSONObject json){
		try {
			String base64Str = ImageUtil.bitmapToBase64(bt);
			//向JS传输数据
			jo = new JSONObject();
			jo.put("error_no", "0");
			jo.put("type", json.getString("type"));
			jo.put("code", json.getString("cardnum"));//身份证号
			jo.put("name", json.getString("name"));//姓名
			jo.put("address", json.getString("address"));//住址
			jo.put("issue", json.getString("office"));//签发机关
			jo.put("birthday",json.getString("birthday"));//?
			jo.put("valid", json.getString("validdate"));//发证日期
			jo.put("starttime", json.getString("startData"));//?
			jo.put("endtime", json.getString("endData"));//?
			jo.put("gender", json.getString("sex"));//性别
			jo.put("nation", json.getString("nation"));//名族
			jo.put("base64str", base64Str);//身份证照片

//			CordovaWebView webv = IDCheckPlugin.staWebview;
//			webv.sendJavascript("imgState('" + image_no + "','" + jo.toString() + "')");
//			webv.sendJavascript("sendIMG('" + jo.toString() + "')");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////
	///////////////////// 以上是易道的代码，下面是天风修改代码
	/////////////////////
	/////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////

	private final String IMAGE_TYPE = "image/*";
	private final int IMAGE_CODE = 0;
	@SuppressLint("NewApi")
	private void ButtonInit(){
		ImageView BTNToPhotoAlbum = (ImageView)findViewById(R.id.capture_btnPhotoalbum);
		BTNToPhotoAlbum.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				 Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
				 getAlbum.setType(IMAGE_TYPE);
				 startActivityForResult(getAlbum, IMAGE_CODE);
			}
		});
		ImageView BTNCloss = (ImageView)findViewById(R.id.capture_btnClose);
		BTNCloss.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					jo = new JSONObject();
					jo.put("error_no", "-1");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				PluginResult pr = new PluginResult(PluginResult.Status.OK, jo);
				IDCheckPlugin.staWebview.sendPluginResult(pr, activeCallBackContextID);
				CaptureActivity.this.finish();
			}
		});
		TextView BTNToSure = (TextView)findViewById(R.id.capture_btnSurePic);
		BTNToSure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PluginResult pr = new PluginResult(PluginResult.Status.OK, jo);
				IDCheckPlugin.staWebview.sendPluginResult(pr, activeCallBackContextID);
				CaptureActivity.this.finish();
			}
		});
		TextView BTNTCcanel = (TextView)findViewById(R.id.capture_btnCanelPic);
		BTNTCcanel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				CaptureActivity.this.recreate();
				restartScan();
			}
		});
	}

	private void resultVisible(boolean ifshow){
		TextView BTNToSure = (TextView)findViewById(R.id.capture_btnSurePic);
		TextView BTNTCcanel = (TextView)findViewById(R.id.capture_btnCanelPic);
		TextView IDResultView = (TextView)findViewById(R.id.capture_txtResult);
		ImageView imgResult = (ImageView)findViewById(R.id.capture_imgResult);
		ImageView photoalbum = (ImageView)findViewById(R.id.capture_btnPhotoalbum);
		ImageView BTNClose = (ImageView)findViewById(R.id.capture_btnClose);
		if(ifshow){
			BTNToSure.setVisibility(View.VISIBLE);
			BTNTCcanel.setVisibility(View.VISIBLE);
			IDResultView.setVisibility(View.VISIBLE);
			imgResult.setVisibility(View.VISIBLE);

			photoalbum.setVisibility(View.GONE);
			BTNClose.setVisibility(View.GONE);
		}else{
			BTNToSure.setVisibility(View.GONE);
			BTNTCcanel.setVisibility(View.GONE);
			IDResultView.setVisibility(View.GONE);
			imgResult.setVisibility(View.GONE);

			photoalbum.setVisibility(View.VISIBLE);
			BTNClose.setVisibility(View.VISIBLE);
		}
	}

	//返回获取数据
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) { // 此处的 RESULT_OK 是系统自定义得一个常量
			Log.e(TAG, "ActivityResult resultCode error");
			return;
		}
		// 外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
		ContentResolver resolver = getContentResolver();
		// 此处的用于判断接收的Activity是不是你想要的那个
		if (requestCode == IMAGE_CODE) {
			try {
				Uri originalUri = data.getData(); // 获得图片的uri
//				Bitmap bm = MediaStore.Images.Media.getBitmap(resolver, originalUri); // 显得到bitmap图片
//				// 这里开始的第二部分，获取图片的路径：
				String[] proj = { MediaStore.Images.Media.DATA };
				// 好像是android多媒体数据库的封装接口，具体的看Android文档
				Cursor cursor = managedQuery(originalUri, proj, null, null,null);
				// 按我个人理解 这个是获得用户选择的图片的索引值
				int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				// 将光标移至开头 ，这个很重要，不小心很容易引起越界
				cursor.moveToFirst();
//				 最后根据索引值获取图片路径
				String path = cursor.getString(column_index);
				idImage_for_check = BitmapFactory.decodeFile(path);
				 decode(idImage_for_check);
			} catch (Exception e) {
				Log.e(TAG, e.toString());
				Toast.makeText(getApplicationContext(), "照片识别失败，请重新识别", Toast.LENGTH_LONG).show();
			}
		}
	}

	@SuppressLint("ShowToast")
	 private void decode(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height=bitmap.getHeight();
		//如果图片过大，我要进行压缩处理
		int MaxWidth = 1024;
    	int Maxheight= 768;
    	/** 如果说图片灰常的大的话 **/
    	if(width>MaxWidth||height>Maxheight){
    		int scaleSzie = 1;
        	int bigFinalWidth = 1;
        	int bigFinalheight = 1;
//    		scaleSzie = (width>height?MaxWidth*100/width:Maxheight*100/height);
    		scaleSzie = MaxWidth*100/width;
    		bigFinalWidth = width*scaleSzie/100;
    		bigFinalheight= height*scaleSzie/100;
        	if(bigFinalWidth%2!=0){
        		bigFinalWidth+=1;
    		}
    		if(bigFinalheight%2!=0){
    			bigFinalheight+=1;
    		}
        	bitmap = Bitmap.createScaledBitmap(bitmap, bigFinalWidth, bigFinalheight, false);
        	//测试
        	width = bigFinalWidth;
        	height = bigFinalheight;
    	}
    	/** 如果图片不大，但是图片有可能尺寸不是2的倍数 **/
    	else{
    		int smallFinalWidth = 1;
        	int smallFinalheight = 1;
    		smallFinalWidth = width;
    		smallFinalheight= height;
        	if(smallFinalWidth%2!=0){
        		smallFinalWidth+=1;
    		}
    		if(smallFinalheight%2!=0){
    			smallFinalheight+=1;
    		}
        	bitmap = Bitmap.createScaledBitmap(bitmap, smallFinalWidth, smallFinalheight, false);
        	//测试
        	width = smallFinalWidth;
        	height = smallFinalheight;
    	}
    	//算法只能识别宽度比高度大的身份证件，所以要进行旋转,放在后面是为了优化
    	if(width<height){
    		Matrix matrix = new Matrix();
        	matrix.reset();
            matrix.setRotate(90);
        	bitmap = Bitmap.createBitmap(bitmap,0,0, bitmap.getWidth(), bitmap.getHeight(),matrix, true);
        	//获取旋转后的宽高
        	width = bitmap.getWidth();
    		height=bitmap.getHeight();
    	}

		byte[] data =  ImageUtil.getNV21(width, height, bitmap);
		long start = System.currentTimeMillis();
		// arg
		int ret = 0;
		ExIDCardReco excard = new ExIDCardReco();

		////////////////////////////////////////////////////////////////////////////////////////////////
		if(data.length>10){
			ret = ExIDCardReco.nativeRecoRawdat(data, width, height, width, 1, excard.bResultBuf, excard.bResultBuf.length);
			excard.cardcode.SetColorType(CardColorJudge(data, width, height));
		}

		if (ret > 0) {
			long end = System.currentTimeMillis();
			Log.d(TAG, "Found text (" + (end - start) + " ms):\n");

			excard.nResultLen = ret;
			excard.cardcode.SetViewType("Preview");
			excard.cardcode.SetColorType(CardColorJudge(data, width, height));
			excard.DecodeResult(excard.bResultBuf, excard.nResultLen);
			//if we have the text to show
			//if ( excard.ok && activity.CheckIsEqual(excard.cardcode) ) {
			if ( excard.ok ) {
				CaptureActivity.this.SetRecoResult(excard.cardcode);

				//下面这些代码是提取标准身份证图像的，如果客户有需求请打开
				//API For Image Return;
				idImage_for_check = ExIDCardReco.nativeGetStdCardImg(data, width, height, excard.bResultBuf, excard.bResultBuf.length, excard.rects);
				//如果需要保存图像，请您打开保存图像的语句
//				try{saveBitmap(imcard); }catch (IOException e) {e.printStackTrace();}
				excard.cardcode.SetBitmap(idImage_for_check);
				//保存各个条目的矩形框
//				excard.SetRects(excard.rects);
				//Bitmap imNum = excard.cardcode.GetIDNumBitmap();
				//返回图像结束-------END

				Message message = Message.obtain(CaptureActivity.this.getHandler(),	R.id.decode_succeeded, excard);
				message.sendToTarget();
				return;
			}
		}else{
			//识别失败
//			restartScan();
//			Toast.makeText(getApplicationContext(), "照片识别失败，请重新识别", Toast.LENGTH_LONG).show();
		}

		restartScan();
		Toast.makeText(getApplicationContext(), "照片识别失败，请重新识别", Toast.LENGTH_LONG).show();
		// retry to focus to the text
//		Message message = Message.obtain(CaptureActivity.this.getHandler(), R.id.decode_failed);
//		message.sendToTarget();
	}

	/**
	 * decode附属算法
	 * @param data
	 * @param width
	 * @param height
	 * @return
	 */
	private int CardColorJudge(byte []data, int width, int height)
	{
		int offset = width*height;
		int i;
		int iTht = 144;
		int iCnt = 255;
		int nNum = 0;
		int iSize = width*height/2;

		for(i = 0; i < iSize; ++i ){
			int val = data[i+offset]&0xFF;
			if( val > iTht){ ++nNum; }
		}

		if(nNum > iCnt) return 1;
		else return 0;
	}

	/**
	 * 用于在界面里面显示提示字符
	 *
	 * 是不是统一在这里使用呢
	 *
	 * @return
	 */
	private void showTomast(String text){
		Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
	}

}