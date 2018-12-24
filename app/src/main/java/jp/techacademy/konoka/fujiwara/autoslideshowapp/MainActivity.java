package jp.techacademy.konoka.fujiwara.autoslideshowapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Button;

public class MainActivity extends AppCompatActivity{

    private static final int PERMISSIONS_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Android 6.0以降の場合
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // パーミッションの許可状態を確認する
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED) {
                // 許可されている
                getContentsInfo();
            } else {
                // 許可されていないので許可ダイアログを表示する
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSIONS_REQUEST_CODE);
            }
            // Android 5系以下の場合
        } else {
            getContentsInfo();
        }




        Button mBack_button = (Button) findViewById(R.id.back_button);
        Button mStart_pause_button = (Button) findViewById(R.id.start_pause_button);
        Button mNext_button = (Button) findViewById(R.id.next_button);

        mBack_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.back_button) {

                }
            }
        });


        mStart_pause_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.start_pause_button) {

                }
            }
        });


        mNext_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.next_button) {

                }
            }
        });

    }





    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContentsInfo();
                }
                break;
            default:
                break;
        }
    }

    private void getContentsInfo() {

        // 画像の情報を取得する
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // データの種類
                null, // 項目(null = 全項目)
                null, // フィルタ条件(null = フィルタなし)
                null, // フィルタ用パラメータ
                null // ソート (null ソートなし)
        );

        if (cursor.moveToFirst()) {
            int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
            Long id = cursor.getLong(fieldIndex);
            Uri imageUri = ContentUris.withAppendedId
                    (MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            imageView.setImageURI(imageUri);
        }
        cursor.close();
    }

}

