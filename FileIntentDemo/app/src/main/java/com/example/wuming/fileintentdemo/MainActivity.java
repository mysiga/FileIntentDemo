package com.example.wuming.fileintentdemo;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String UR_IMAGE = "http://pic1.nipic.com/2008-12-24/2008122414255777_2.jpg";

    private AppCompatTextView mDownLoadPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDownLoadPath = (AppCompatTextView) findViewById(R.id.download_path);
        File file = new File(setPath());
        if (file.exists()) {
            mDownLoadPath.setText(file.toString());
        }

        findViewById(R.id.download).setOnClickListener(this);
        findViewById(R.id.open_file).setOnClickListener(this);
        findViewById(R.id.open_file_context).setOnClickListener(this);
    }


    private String setPath() {
        return Environment.getExternalStorageDirectory() + File.separator + "milin/download/milin.jpg";
    }

    @Override
    public void onClick(View v) {
        File file = new File(setPath());
        switch (v.getId()) {
            case R.id.download:
                BaseDownloadTask baseDownloadTask = FileDownloader.getImpl().create(UR_IMAGE).setPath(setPath()).setListener(new FileDownloadSampleListener() {
                    @Override
                    protected void completed(BaseDownloadTask task) {
                        super.completed(task);
                        mDownLoadPath.setText(task.getPath());
                        Toast.makeText(MainActivity.this, "下载完成", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        super.error(task, e);
                        Toast.makeText(MainActivity.this, "网络错误", Toast.LENGTH_LONG).show();
                    }
                });
                baseDownloadTask.start();
                break;
            case R.id.open_file:
                if (file.exists()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    Uri uri=Uri.fromFile(file);
                    intent.setDataAndType(uri, "image/*");
                    try {
                        ActivityCompat.startActivity(this, intent, null);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(MainActivity.this, "没有找到打开的工具", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.open_file_context:
                if (file.exists()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    String authority = getString(R.string.file_provider_authorities);
                    Uri contentUri = FileProvider.getUriForFile(this, authority, file);
                    Log.i("milin", contentUri.toString());
                    String MimeType = getContentResolver().getType(contentUri);
                    intent.setDataAndType(contentUri, MimeType);
                    try {
                        ActivityCompat.startActivity(this, intent, null);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(MainActivity.this, "没有找到打开的工具", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }
}
