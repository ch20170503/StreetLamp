package com.zzb.util;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.zzb.sl.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * @author coolszy
 * @date 2012-4-26
 * @blog http://blog.92coding.com
 */

public class UpdateManager {

	private InputStream inStream = null;
	private int inStreamState = 0;

	public static int versionCode;
	public static String versionName, ServerName;
	/* 下载中 */
	private static final int DOWNLOAD = 1;
	/* 下载结束 */
	private static final int DOWNLOAD_FINISH = 2;
	/* 保存解析的XML信息 */
	HashMap<String, String> mHashMap;
	/* 下载保存路径 */
	private String mSavePath;
	/* 记录进度条数量 */
	private int progress;
	/* 是否取消更新 */
	private boolean cancelUpdate = false;

	private Context mContext;
	/* 更新进度条 */
	private ProgressBar mProgress;
	private Dialog mDownloadDialog;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				// 正在下载
				case DOWNLOAD:
					// 设置进度条位置
					mProgress.setProgress(progress);
					break;
				case DOWNLOAD_FINISH:
					// 安装文件
					installApk();
					break;
				default:
					break;
			}
		};
	};

	public UpdateManager(Context context) {
		this.mContext = context;
	}

	/**
	 * 检测软件更新
	 */
	public void checkUpdate(boolean IsAutoCheck) {
		if (isUpdate()) {
			// 显示提示对话框
			showNoticeDialog();
		} else {
			if (!IsAutoCheck) {
				ActivityUtil.showToasts(mContext,"已经是最新版本",1*1000);
			}
		}
	}

	/**
	 * 检查软件是否有更新版本
	 *
	 * @return
	 * @throws MalformedURLException
	 */
	private boolean isUpdate() {
		Log.i(" 获取当前软件版本", "已经进入");
		// 获取当前软件版本
		versionCode = getVersionCode(mContext);
		versionName = getVersionName(mContext);
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					URL url = new URL("");
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setConnectTimeout(2000);// 设置连接超时
					conn.setRequestMethod("GET");
					inStream = conn.getInputStream();
					// 解析XML文件。 由于XML文件比较小，因此使用DOM方式进行解析
					ParseXmlService service = new ParseXmlService();
					try {
						mHashMap = service.parseXml(inStream);
						inStreamState = 1;
					} catch (Exception e) {
						e.printStackTrace();
						inStreamState = 2;
					}
				} catch (Exception e) {
					Log.e("取得网络更新文件", "失败");
					inStreamState = 2;
					e.printStackTrace();
				}
			}
		});
		t.start();

		while (inStreamState == 0) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}

		if (inStreamState == 2) {
			return false;
		}
		if (null != mHashMap) {
			int serviceCode = Integer.valueOf(mHashMap.get("version"));
			ServerName = String.valueOf(mHashMap.get("versionName"));
			// 版本判断
			if (serviceCode > versionCode) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取软件版本号
	 *
	 * @param context
	 * @return
	 */
	private int getVersionCode(Context context) {
		int versionCode = 0;
		try {
			// 获取软件版本号，对应AndroidManifest.xml下android:versionCode
			versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}
	/**
	 * 获取软件版本名
	 *
	 * @param context
	 * @return
	 */
	private String getVersionName(Context context) {
		String name = "";
		try {
			// 获取软件版本号，对应AndroidManifest.xml下android:versionCode
			name = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return name;
	}

	/**
	 * 显示软件更新对话框
	 */
	private void showNoticeDialog() {
		// 构造对话框
		Builder builder = new Builder(mContext);
		builder.setTitle("软件更新");
		builder.setMessage("Find a new version:" + ServerName);
		// 更新
		builder.setPositiveButton("更新", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// 显示下载对话框
				showDownloadDialog();
			}
		});
		// 稍后更新
		builder.setNegativeButton("稍后更新", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		Dialog noticeDialog = builder.create();
		noticeDialog.show();
	}

	/**
	 * 显示软件下载对话框
	 */
	private void showDownloadDialog() {
		Log.i("下载对话框", "进入");
		// 构造软件下载对话框
		Builder builder = new Builder(mContext);
		builder.setTitle("正在跟新");
		// 给下载对话框增加进度条
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.softupdate_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
		builder.setView(v);
		// 取消更新
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// 设置取消状态
				cancelUpdate = true;
			}
		});
		mDownloadDialog = builder.create();
		mDownloadDialog.show();
		// 现在文件
		downloadApk();
	}

	/**
	 * 下载apk文件
	 */
	private void downloadApk() {
		// 启动新线程下载软件
		Log.i("下载APK", "进入");
		new downloadApkThread().start();
	}

	/**
	 * 下载文件线程
	 *
	 * @author coolszy
	 * @blog http://blog.92coding.com
	 */
	private class downloadApkThread extends Thread {
		@Override
		public void run() {
			try {
				String sdpath = Environment.getDataDirectory() + "/";
				// mSavePath = sdpath+"app/";
				mSavePath = sdpath + "data/com.example.fragmentdemo/databases/";

				URL url = new URL(mHashMap.get("url"));
				// 创建连接
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.connect();
				conn.setReadTimeout(500);
				// 获取文件大小
				int length = conn.getContentLength();
				// 创建输入流
				InputStream is = conn.getInputStream();
				Log.i("APK文件大小", "" + length);
				File file = new File(mSavePath);
				// 判断文件目录是否存在
				if (!file.exists()) {
					Log.i("文件目录", "不存在");
					boolean a = file.mkdirs();
					Log.i("创建文件目录", "" + a);
				}
				Log.i(mSavePath, mHashMap.get("name"));
				File apkFile = new File(mSavePath, mHashMap.get("name"));
				try {
					String command = "chmod 777 " + apkFile.getAbsolutePath();

					Log.i("command", command);
					Runtime runtime = Runtime.getRuntime();
					runtime.exec(command);
				} catch (IOException e) {
					Log.i("command", "fail!!!!");
					e.printStackTrace();
				}
				if (apkFile.exists()) {
					/*
					 * Log.i("文件"+apkFile.getPath(),"存在");
					 * Log.i("文件可读",""+apkFile.canRead());
					 * Log.i("文件可写",""+apkFile.canWrite()); boolean a =
					 * apkFile.delete(); Log.i("删除文件",""+a);
					 */
					Log.i("删除文件", apkFile.delete() + "");
				}
				FileOutputStream fos = new FileOutputStream(apkFile);
				int count = 0;
				// 缓存
				byte buf[] = new byte[1024];
				// 写入到文件中
				do {
					// Log.i("下载","准备读取");
					int numread = is.read(buf);
					// Log.i("下载","读取完毕"+numread);
					count += numread;
					// 计算进度条位置
					progress = (int) (((float) count / length) * 100);
					// Log.i("下载进度",progress+"");
					// 更新进度
					mHandler.sendEmptyMessage(DOWNLOAD);
					if (numread <= 0) {
						// 下载完成
						mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
						break;
					}
					// 写入文件

					fos.write(buf, 0, numread);

				} while (!cancelUpdate);// 点击取消就停止下载.
				fos.close();
				is.close();

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 取消下载对话框显示
			mDownloadDialog.dismiss();
		}
	};

	/**
	 * 安装APK文件
	 */
	private void installApk() {
		Log.i("安装APK", "开始");
		File apkfile = new File(mSavePath, mHashMap.get("name"));
		if (!apkfile.exists()) {
			return;
		}
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		mContext.startActivity(i);
	}
}
