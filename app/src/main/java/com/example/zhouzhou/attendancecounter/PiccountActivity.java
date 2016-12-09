package com.example.zhouzhou.attendancecounter;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by zhouzhou on 2016/11/1.
 */

/**
 8      * Checks if the app has permission to write to device storage
 9      *
 10      * If the app does not has permission then the user will be prompted to
 11      * grant permissions
 12      *
 13      * @param activity
 14      */

public class PiccountActivity extends ListActivity {
    private Tool tool = new Tool();
    private String DETECTER_MODEL_FILE =
            "file:///android_asset/OtureoFaceDetecter.model";
    private String RECOGNIZER_MODEL_FILE =
            "file:///android_asset/OtureoFaceRecognizer.model";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piccount);

        tool.initializeDetecter(
                getResources().getAssets(), DETECTER_MODEL_FILE);
        tool.initializeRecognizer(
                getResources().getAssets(), RECOGNIZER_MODEL_FILE);

        List<String> ls = new ArrayList<String>();
        ls.add("周周");
        ls.add("万炽洋");
        ls.add("汪思学");
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ls);
        setListAdapter(adapter);
        Button btn1 = (Button) findViewById(R.id.button_piccount_1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                String path = Environment.getExternalStorageDirectory()+"/temphoto/" ;
                File file = new File(path) ;
                file.mkdirs() ;
                String name = path+"test.jpg" ;
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(name)));
                startActivityForResult(intent,1);
                /*
                Log.i("face", "hehe");
                String TestDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + File.separator + "Camera";
                Log.i("face", TestDir);
                File dir = new File(TestDir);
                File[] subfile = dir.listFiles();

                String imgname = "/storage/5CD0-9FE1/DCIM/Camera/IMG_20161121_141202.jpg";*/
               /*for(int i = 0; i < subfile.length; i++) {
                    if(subfile[i].isFile()) {
                        String filename = subfile[i].getName();
                        if (filename.trim().toLowerCase().endsWith(".jpg") ||
                                filename.trim().toLowerCase().endsWith(".jpeg")) {
                            imgname = subfile[i].getPath();
                        }
                    }
                }*/
                /*
                Log.i("face", imgname);


				int ret=0;
                if (ContextCompat.checkSelfPermission(PiccountActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    Log.i("face", "123");              
                    
                    ActivityCompat.requestPermissions(PiccountActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},ret);
                }
                Bitmap srcImage = BitmapFactory.decodeFile(imgname);
                List<Roi> rois = tool.DetectAndRecognize(srcImage, 2);
                    for (int i = 0; i < rois.size(); ++i) {
                        Roi roi = rois.get(i);
                        Log.i("face", roi.toString());}



                */
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private Bitmap getBitmapFromUrl(String url, double width, double height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 设置了此属性一定要记得将值设置为false
        Bitmap bitmap = BitmapFactory.decodeFile(url);
        // 防止OOM发生
        options.inJustDecodeBounds = false;
        int mWidth = bitmap.getWidth();
        int mHeight = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = 1;
        float scaleHeight = 1;
//        try {
//            ExifInterface exif = new ExifInterface(url);
//            String model = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        // 按照固定宽高进行缩放
        // 这里希望知道照片是横屏拍摄还是竖屏拍摄
        // 因为两种方式宽高不同，缩放效果就会不同
        // 这里用了比较笨的方式
        if(mWidth <= mHeight) {
            scaleWidth = (float) (width/mWidth);
            scaleHeight = (float) (height/mHeight);
        } else {
            scaleWidth = (float) (height/mWidth);
            scaleHeight = (float) (width/mHeight);
        }
//        matrix.postRotate(90); /* 翻转90度 */
        // 按照固定大小对图片进行缩放
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, mWidth, mHeight, matrix, true);
        // 用完了记得回收
        bitmap.recycle();
        return newBitmap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode == Activity.RESULT_OK) {
            int ret = 0;
            if (ContextCompat.checkSelfPermission(PiccountActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.i("face", "123");

                ActivityCompat.requestPermissions(PiccountActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, ret);
            }
            String path = Environment.getExternalStorageDirectory()+"/temphoto/" ;
            String name = path+"test.jpg" ;
            Bitmap bitmap  =getBitmapFromUrl(name,400,600) ;

            /*Date date=new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");//获取当前时间，进一步转化为字符串
            String name= format.format(date)+".jpg";

            String path = "/sdcard/logphoto/"+name ;
            Log.i("time",path);
            Bundle bundle = data.getExtras() ;
            Bitmap bitmap = (Bitmap) bundle.get("data");
            File file = new File("/sdcard/logphoto") ;
            file.mkdir() ;
            FileOutputStream fileOutputStream = null ;
            try {
                fileOutputStream = new FileOutputStream(path) ;
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream) ;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }finally {
                try {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            */
            ImageView image = (ImageView)findViewById(R.id.piccount_image) ;
            image.setMaxHeight(500);
            image.setMaxWidth(500);


            Log.i("size",Integer.toString(bitmap.getHeight())+" "+Integer.toString(bitmap.getWidth()));
            image.setImageBitmap(bitmap);
        }

    }

        @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        String s = ((TextView) v).getText().toString();
        Toast.makeText(this, "提示" + position + s, Toast.LENGTH_LONG).show();
        super.onListItemClick(l, v, position, id);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Piccount Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}