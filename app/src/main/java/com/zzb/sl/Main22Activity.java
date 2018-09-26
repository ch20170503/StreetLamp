package com.zzb.sl;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zzb.util.FileUtils;
import com.zzb.util.SavePictureUtil;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class Main22Activity extends AppCompatActivity {

    private static final String TAG = "Main22Activity";
    private  WebView myWebView;
    private long mExitTime;
    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private final int RESULT_CODE_PICK_FROM_ALBUM_BELLOW_LOLLILOP = 1;
    private final int RESULT_CODE_PICK_FROM_ALBUM_ABOVE_LOLLILOP = 2;
    String compressPath = "";
    private static boolean mBackKeyPressed = false;//记录是否有首次按键
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main22);
        final String infoString = getIntent().getStringExtra("URLS");
        Log.e(TAG,"链接"+infoString);
        myWebView = (WebView) findViewById(R.id.webview);

        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        myWebView.getSettings().setDomStorageEnabled(true);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setAppCacheEnabled(true);
        myWebView.setDownloadListener(new MyDownLoad());
        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url == null) return false;
                try {
                    if(url.startsWith("weixin://") || url.startsWith("alipays://") ||
                            url.startsWith("mailto://") || url.startsWith("tel://")
                        //其他自定义的scheme
                            ) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                        return true;
                    }
                } catch (Exception e) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                    return false;
                }
                //处理http和https开头的url
                view.loadUrl(url);
                return true;
            }
        });

        myWebView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final WebView.HitTestResult hitTestResult = myWebView.getHitTestResult();
                // 如果是图片类型或者是带有图片链接的类型
                if (hitTestResult.getType() == WebView.HitTestResult.IMAGE_TYPE ||
                        hitTestResult.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                    // 弹出保存图片的对话框
                    AlertDialog.Builder builder = new AlertDialog.Builder(Main22Activity.this);
                    builder.setTitle("提示");
                    builder.setMessage("保存图片到本地");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //保存图片到相册
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            SavePictureUtil.getInstance().savePic(Main22Activity.this,  GetImageInputStream(hitTestResult.getExtra())
                                                    , SavePictureUtil.getInstance().getFilePath("/Zhxzs/chat"),
                                                    SavePictureUtil.getInstance().getFileNameByTime());

                                        }
                                    }).start();
                                }
                            }).start();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        // 自动dismiss
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return true;
                }
                return false;//保持长按可以复制文字

            }
        });
        myWebView.setWebChromeClient(new MyWebChromeClient());

        myWebView.loadUrl(infoString);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode== KeyEvent.KEYCODE_BACK){
            if(this.myWebView.canGoBack()){
                //需要处理
                this.myWebView.goBack();
                return true;
            }else{
                if(!mBackKeyPressed){
                    mBackKeyPressed = true;
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            mBackKeyPressed = false;
                        }//延时两秒，如果超出则擦错第一次按键记录
                    }, 2000);
                    return true;
                }
                else{//退出程序
                    this.finish();
                }
            }
        }
        return super.onKeyDown(keyCode,event);
    }
    public boolean parseScheme(String url) {
        if (url.contains("platformapi/startapp")) {
            return true;
        } else if (url.contains("weixin://")) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
            return true;
        } else {
            return false;
        }
    }

    class MyDownLoad implements DownloadListener {
        @Override
        public void onDownloadStart(String url, String userAgent,
                                    String contentDisposition, String mimetype, long contentLength) {
            if (url.endsWith(".apk")) {
                /**
                 * 通过系统下载apk
                 */
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        }


    }
    public Bitmap GetImageInputStream(String imageurl){
        URL url;
        HttpURLConnection connection=null;
        Bitmap bitmap=null;
        try {
            url = new URL(imageurl);
            connection=(HttpURLConnection)url.openConnection();
            connection.setConnectTimeout(6000); //超时设置
            connection.setDoInput(true);
            connection.setUseCaches(false); //设置不使用缓存
            InputStream inputStream=connection.getInputStream();
            bitmap= BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**选择后，回传值*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mUploadMessage == null && mUploadCallbackAboveL == null) {
            return;
        }
        Uri uri = null;
        switch (requestCode) {
            case RESULT_CODE_PICK_FROM_ALBUM_BELLOW_LOLLILOP:
                uri = afterChosePic(data);
                if (mUploadMessage != null) {
                    mUploadMessage.onReceiveValue(uri);
                    mUploadMessage = null;
                }
                break;
            case RESULT_CODE_PICK_FROM_ALBUM_ABOVE_LOLLILOP:
                try {
                    uri = afterChosePic(data);
                    if (uri == null) {
                        mUploadCallbackAboveL.onReceiveValue(new Uri[] { });
                        mUploadCallbackAboveL = null;
                        break;
                    }
                    if (mUploadCallbackAboveL != null && uri != null) {
                        mUploadCallbackAboveL.onReceiveValue(new Uri[] { uri });
                        mUploadCallbackAboveL = null;
                    }
                } catch (Exception e) {
                    mUploadCallbackAboveL = null;
                    e.printStackTrace();
                }
                break;
        }
    }
    /**
     * 选择照片后结束
     * @param data
     */
    private Uri afterChosePic(Intent data) {
        if (data == null) {
            return null;
        }
        String path = getRealFilePath(data.getData());
        String[] names = path.split("\\.");
        String endName = null;
        if (names != null) {
            endName = names[names.length - 1];
        }
        if (endName != null) {
            compressPath = compressPath.split("\\.")[0] + "." + endName;
        }
        File newFile;
        try {
            newFile = FileUtils.compressFile(path, compressPath);
        } catch (Exception e) {
            newFile = null;
        }
        return Uri.fromFile(newFile);
    }
    /**
     * 根据Uri获取图片文件的绝对路径
     */
    public String getRealFilePath(final Uri uri) {
        if (null == uri) {
            return null;
        }
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = getContentResolver().query(uri,
                    new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(myWebView.canGoBack()){
            myWebView.goBack();
        }else{
            finish();
        }
    }
    private class WebClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }

    /**打开图库,同时处理图片（项目业务需要统一命名）*/
    private void selectImage(int resultCode) {
        compressPath = Environment.getExternalStorageDirectory().getPath() + "/QWB/temp";
        File file = new File(compressPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        compressPath = compressPath + File.separator + "compress.png";
        File image = new File(compressPath);
        if (image.exists()) {
            image.delete();
        }
        Intent intent = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, resultCode);

    }
    /**
     * 内部类
     */
    class MyWebChromeClient extends WebChromeClient {
        //openFileChooser（隐藏方法）仅适用android5.0以下的环境，android5.0及以上使用onShowFileChooser

        // For Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                    String acceptType) {
            if (mUploadMessage != null)
                return;
            mUploadMessage = uploadMsg;
            selectImage(RESULT_CODE_PICK_FROM_ALBUM_BELLOW_LOLLILOP);
        }

        // For Android < 3.0
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            openFileChooser(uploadMsg, "");
        }

        // For Android > 4.1.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                    String acceptType, String capture) {
            openFileChooser(uploadMsg, acceptType);
        }

        // For Android 5.0+
        public boolean onShowFileChooser(WebView webView,
                                         ValueCallback<Uri[]> filePathCallback,
                                         FileChooserParams fileChooserParams) {
            mUploadCallbackAboveL = filePathCallback;
            selectImage(RESULT_CODE_PICK_FROM_ALBUM_ABOVE_LOLLILOP);
            return true;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }
    }






}


