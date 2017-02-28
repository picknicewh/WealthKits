package com.cfjn.javacf.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.cfjn.javacf.R;
import com.cfjn.javacf.modle.CheckUpadteVo;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
/**
 * 作者： zll
 * 时间： 2016-6-7
 * 名称：版本更新
 * 版本说明：
 * 附加注释：
 * 主要接口：获取版本更新
 */
public class UpdateManager {
	/**
	 * 连接服务解析完毕的
	 */
	private static final int CHECK_UPDATE = 0x000;
	/**
	 * 下载中
	 */
	private static final int DOWNLOAD = 0x001;
	/**
	 * 下载结束
	 */
	private static final int DOWNLOAD_FINISH = 0x002;
	/**
	 * 下载地址
	 */
	private String downLoadURL;
	/**
	 * 下载保存路径
	 */
	private String mSavePath;
	/**
	 * 记录进度条数量
	 */
	private int progress;
	/**
	 * 更新进度条
	 */
	private ProgressBar mProgress;
	/**
	 * 下载对话框
	 */
	private Dialog mDownloadDialog;
	/**
	 * 用户名
	 */
	private String loginName;
	/**
	 * 当前上下文
	 */
	private Context context;
	/**
	 * 设备ID
	 */
	private String UUID = null;
	/**
	 * 电话类型
	 */
	private String phoneType = null;
	/**
	 * 更新类型
	 */
	private int updateType;
	/**
	 * xutils下载的handler
	 */
	HttpHandler handler;
	/**
	 * 是否正在下載
	 */
	private boolean isDownLoading;
	/**
	 * 新版本号
	 */
	private String version;
	private AlertDialog dialog;
	private Builder builder;
	private int actype;
	private CheckUpadteVo checkUpadteVo;
	public UpdateManager(Activity activity) {
		this.context = activity;
//		TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
//		UUID = tm.getDeviceId();
//		phoneType = String.valueOf(tm.getPhoneType());
		// 判断SD卡是否存在，并且是否具有读写权限
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			// 获得存储卡的路径
			mSavePath = Environment.getExternalStorageDirectory().toString() + "/download";
		}

	}

	/**
	 * 检测软件更新
	 * updateType 0==点击了版本更新触发的更新 1==进入app自动检查更新的更新
	 */
	public void checkUpdate(int click) {
		this.updateType = click;
		if(NetWorkUitls.isNetworkAvailable(context)){
			new PasrseThread().checkUpdate();
		}else if(updateType==0) {
			G.showToast(context, "无网络，无法更新！");
		}
	}

	/**
	 * 检查软件是否有更新版本
	 */
	private class PasrseThread implements OKHttpListener {

		public void checkUpdate() {
			Map<String, String> map = new HashMap<>();
			map.put("versionCode", String.valueOf(PackageUtils.getVersionCode(context)));
			map.put("appId","0");
			OkHttpUtil.sendPost(ApiUri.CHECKUPADTE,map,this);
		}
		private  Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.what ==0x01){
					downLoadURL = checkUpadteVo.getDate().getUrl();
					version = checkUpadteVo.getDate().getVersion();
					actype = checkUpadteVo.getDate().getAction();
					Message msg2 = mHandler.obtainMessage(CHECK_UPDATE);
					// 更新类型
					msg2.arg1 = checkUpadteVo.getDate().getAction();
					// 更新内容
					msg2.obj = checkUpadteVo.getDate().getExplain();
					mHandler.sendMessage(msg2);
				}else if (msg.what==0x02){
					if(updateType==0) {
						G.showToast(context, "无网络，无法更新！");
					}
				}
			}
		};
		@Override
		public void onSuccess(String uri, String result) {
			if (uri.equals(ApiUri.CHECKUPADTE)){
				Gson gson = new Gson();
				checkUpadteVo = gson.fromJson(result,CheckUpadteVo.class);
				handler.sendEmptyMessage(0x01);
			}
		}

		@Override
		public void onError(String uri, String error) {
			if (uri.equals(ApiUri.CHECKUPADTE)){
				handler.sendEmptyMessage(0x02);
			}
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case CHECK_UPDATE:
					// msg.arg1 = result = action : 0 没有跟新；1强制跟新；2普通跟新
					switch (msg.arg1) {
						case 0:
							if (updateType == 0) {
								Toast.makeText(context, R.string.soft_update_no, Toast.LENGTH_LONG).show();
							}
							break;
						case 1:
							if (isDownLoading) {
								Toast.makeText(context, R.string.soft_update_doing, Toast.LENGTH_LONG).show();
								break;
							}
							showNoticeDialog(msg.arg1, msg.obj.toString());
						case 2:
							showNoticeDialog(msg.arg1, msg.obj.toString());
							break;
					}
					break;
				case DOWNLOAD:
					// 设置UI
					setProgressUI(msg.arg1);
					break;
				case DOWNLOAD_FINISH:
					// 安装文件
					installApk();
					break;
			}
		}

	};

	/**
	 * 显示软件更新对话框
	 */

	private void showNoticeDialog(final int action, String content) {
		// 构造对话框
		builder = new Builder(context);
		builder.setTitle("检测到新版本，立即更新吗?");
		builder.setMessage(content);
		builder.setCancelable(false);
		// 更新
		builder.setPositiveButton(R.string.soft_update_updatebtn, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// 是否已下载
				boolean isDownload = installApk();
				if (!isDownload) {
					if (action == 1) {
						File apkfile = new File(mSavePath, "财富锦囊" + MD5Utils.encode(version) + ".apk.temp");
						if (!apkfile.exists()) {
							G.showToast(context,"开始下载财富锦囊");
						} else {
							G.showToast(context, "正在下载中，请稍候");
						}
						showNotification(action, 0);
						downloadApk(action);
					} else {
						showDownloadDialog(action);
					}
				}
			}
		});
		if (action == 2) {
			// 稍后更新
			dialog = builder.setNegativeButton(R.string.soft_update_later, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			}).show();

		} else {
			// 退出
			builder.setNegativeButton(R.string.soft_update_exit, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					HunmeApplication.getInstance().exit();
				}
			}).show();
		}
	}

	protected void setProgressUI(int action) {
		// TODO Auto-generated method stub
		if (progress != lastProgress) {
			if (action == 1) {
				showNotification(action, 1);
			} else {
				mProgress.setProgress(progress);
			}
		}
	}

	/**
	 *
	 * @param action
	 *            更新类型
	 * @param flag
	 *            1表示下载中；2表示下载成功；3表示下载失败
	 */
	private void showNotification(int action, int flag) {
		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification();
		notification.icon = R.drawable.ic_launcher;
		// Intent intent = new Intent(activity, CF0000BootActivity.class);
		Intent intent = new Intent();
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//		notification.setLatestEventInfo(context, "拾惠锁", "拾惠锁", pendingIntent);
		notification.flags = Notification.FLAG_ONGOING_EVENT;// 常驻notification；
		RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.notification_update);
		notification.contentView = rv;
		switch (flag) {
			case 1:
				rv.setTextViewText(R.id.tv_apk_name, "财富锦囊下载中");
				rv.setProgressBar(R.id.pb_download, 100, progress, false);
				rv.setTextViewText(R.id.tv_progress_percent, progress + "%");
				break;
			case 2:
				intent = getInstallIntent();
				pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
				notification.defaults = Notification.DEFAULT_SOUND;
//			notification.setLatestEventInfo(context, "拾惠锁", "下载完成", pendingIntent);
				notification.flags = Notification.FLAG_AUTO_CANCEL;
				break;
			case 3:
//			notification.setLatestEventInfo(context, "拾惠锁", "下载失败", pendingIntent);
				notification.flags = Notification.FLAG_AUTO_CANCEL;
				break;
			default:
		}
		manager.notify(0, notification);
	}

	/**
	 * 显示软件下载对话框
	 */
	private void showDownloadDialog(int action) {
		// 构造软件下载对话框
		Builder builder = new Builder(context);
		builder.setTitle(R.string.soft_updating);
		// 给下载对话框增加进度条
		final LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.dialog_update_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
		builder.setView(v);
		builder.setCancelable(false);
		if (action == 2) {
			// 取消更新
			builder.setNegativeButton(R.string.soft_update_cancel, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					handler.cancel();
				}
			});
		} else {
			// 退出
			builder.setNegativeButton(R.string.soft_update_exit, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					handler.cancel();
					HunmeApplication.getInstance().exit();
				}
			});
		}

		mDownloadDialog = builder.create();
		mDownloadDialog.show();
		// 现在文件
		downloadApk(action);
	}

	/**
	 * 下载apk文件
	 */
	private void downloadApk(final int action) {

		String target = mSavePath + "/财富锦囊" + MD5Utils.encode(version) + ".apk.temp";
		HttpUtils httputils = new HttpUtils();
		handler = httputils.download(downLoadURL, target, true, true, new RequestCallBack<File>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				if (action == 1) {
					showNotification(action, 3);
					isDownLoading = false;
				}
			}

			@Override
			public void onSuccess(ResponseInfo<File> arg0) {
				mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
				File file = new File(mSavePath, "财富锦囊" + MD5Utils.encode(version) + ".apk.temp");
				file.renameTo(new File(mSavePath, "财富锦囊" + MD5Utils.encode(version) + ".apk"));
				if (action == 1) {
					showNotification(action, 2);
					isDownLoading = false;
				}

			}

			@Override
			public void onStart() {
				isDownLoading = true;
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				lastProgress = progress;
				progress = (int) (((float) current / total) * 100);
				setProgressUI(action);
			}
		});
	}

	private int lastProgress;

	/**
	 * 安装APK文件
	 */
	private boolean installApk() {
		File apkfile = new File(mSavePath, "财富锦囊" + MD5Utils.encode(version) + ".apk");
		if (!apkfile.exists()) {
			return false;

		}
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");

		context.startActivity(i);
		StatusBarUtils.collapseStatusBar(context);
		if(actype==1){
			HunmeApplication.getInstance().exit();
		}

		return true;
	}

	/**
	 * 安装意图
	 */
	private Intent installIntent;

	private Intent getInstallIntent() {
		if (installIntent != null) {
			return installIntent;
		}
		File apkfile = new File(mSavePath, "财富锦囊" + MD5Utils.encode(version) + ".apk");
		installIntent = new Intent(Intent.ACTION_VIEW);
		installIntent.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		return installIntent;
	}

}
