package jp.techacademy.konoka.fujiwara.autoslideshowapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_CODE = 100;

    ImageView imageView;

    /*タイマーに関する変数
    ----------------------------------------------------------------------------------*/
    Timer mTimer;

    // タイマー用の時間のための変数
    double mTimerSec = 0.0;
    Handler mHandler = new Handler();
    /*--------------------------------------------------------------------------------*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Android 6.0以降の場合のパーミッションの設定
        ----------------------------------------------------------------------------------*/
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



        /*ここから下はボタン設定 始まり
        ----------------------------------------------------------------------------------*/
        Button mNext_button = (Button) findViewById(R.id.next_button);
        mNext_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cursor.moveToNext() == false) {
                    cursor.moveToFirst();//こちらで次の画像へ指し示すようにして，以下の画像設定の処理を行う。
                }
                showImage();
            }
        });

        Button mStart_pause_button = (Button) findViewById(R.id.start_pause_button);
        mStart_pause_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.start_pause_button) {

                    // タイマーの作成
                    mTimer = new Timer();

                    // タイマーの始動
                    mTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            mTimerSec += 0.1;

                            mHandler.post(new Runnable() {
                                @Override
                                public void run() { //runの中身はボタンNEXTと同じ
                                    if (cursor.moveToNext() == false) {
                                        cursor.moveToFirst();//こちらで次の画像へ指し示すようにして，以下
                                        // の画像設定の処理を行う。
                                    }
                                     showImage();
                                    }
                            });
                        }
                    }, 2000, 500);    // 最初に始動させるまで 100ミリ秒、ループの間隔を
                    // 100ミリ秒 に設定
                }else{
                    mTimer.cancel();
                    mTimer = null;
                }
            }
        });

        Button mBack_button = (Button) findViewById(R.id.back_button);
        mBack_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cursor.moveToPrevious() == false) {
                    cursor.moveToLast();//こちらで次の画像へ指し示すようにして，以下の画像設定の処理を行う。
                }
                showImage();
            }
        });
        /*ボタンの設定 終わり
        ----------------------------------------------------------------------------------*/

    }/*オンクリエイトの蓋*/



        /*ユーザのパーミッションへの選択結果を受け取る コード
        ----------------------------------------------------------------------------------*/
        @Override
        public void onRequestPermissionsResult ( int requestCode, String[] permissions,
        int[] grantResults){
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


        /*Cursorを定義（データベース上の検索結果を格納するもの）
        ----------------------------------------------------------------------------------*/
        Cursor cursor; //Cursorの変数を定義


        //画像のIDを取得して表示する
        public void showImage() {
            int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
            Long id = cursor.getLong(fieldIndex);
            Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media
                    .EXTERNAL_CONTENT_URI, id);

            imageView = (ImageView) findViewById(R.id.imageView);
            imageView.setImageURI(imageUri);
        }


        private void getContentsInfo () {

            // 画像の情報を取得する
            ContentResolver resolver = getContentResolver();
            cursor = resolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // データの種類
                    null, // 項目(null = 全項目)
                    null, // フィルタ条件(null = フィルタなし)
                    null, // フィルタ用パラメータ
                    null // ソート (null ソートなし)
            );

            if (cursor.moveToNext());{
                showImage();
            }
        }


        @Override
        protected void onDestroy() {
            super.onDestroy();

            cursor.close();
        }

    }/*MainActivityの蓋*/



